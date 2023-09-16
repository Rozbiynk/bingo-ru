package io.github.gaming32.bingo.data;

import com.google.common.collect.ImmutableMap;
import com.google.gson.*;
import io.github.gaming32.bingo.Bingo;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Map;

public record BingoTag(
    ResourceLocation id,
    IntList difficultyMax,
    boolean allowedOnSameLine,
    boolean isNever
) {
    private static Map<ResourceLocation, BingoTag> tags = Collections.emptyMap();

    public static BingoTag getTag(ResourceLocation id) {
        return tags.get(id);
    }

    public static BingoTag deserialize(ResourceLocation id, JsonObject json) {
        final JsonArray difficultyMaxArray = GsonHelper.getAsJsonArray(json, "difficulty_max");
        final int[] difficultyMax = new int[5];
        if (difficultyMaxArray.size() != difficultyMax.length) {
            throw new JsonSyntaxException("difficulty_max must be exactly 5 elements long");
        }
        for (int i = 0; i < difficultyMax.length; i++) {
            difficultyMax[i] = GsonHelper.convertToInt(difficultyMaxArray.get(i), "difficulty_max[" + i + "]");
        }
        return new BingoTag(
            id,
            IntList.of(difficultyMax),
            GsonHelper.getAsBoolean(json, "allowed_on_same_line", true),
            GsonHelper.getAsBoolean(json, "is_never", false)
        );
    }

    public JsonObject serialize() {
        final JsonObject result = new JsonObject();

        final JsonArray difficultyMaxArray = new JsonArray(difficultyMax.size());
        for (final Integer max : difficultyMax) {
            difficultyMaxArray.add(new JsonPrimitive(max));
        }
        result.add("difficulty_max", difficultyMaxArray);

        if (!allowedOnSameLine) {
            result.addProperty("allowed_on_same_line", false);
        }

        if (isNever) {
            result.addProperty("is_never", true);
        }

        return result;
    }

    public static Builder builder(ResourceLocation id) {
        return new Builder(id);
    }

    public static final class Builder {
        private final ResourceLocation id;
        private IntList difficultyMax = new IntArrayList();
        private boolean allowedOnSameLine = true;
        private boolean isNever = false;

        private Builder(ResourceLocation id) {
            this.id = id;
        }

        public Builder difficultyMax(int veryEasy, int easy, int medium, int hard, int veryHard) {
            this.difficultyMax = IntList.of(veryEasy, easy, medium, hard, veryHard);
            return this;
        }

        public Builder disallowOnSameLine() {
            this.allowedOnSameLine = false;
            return this;
        }

        public Builder isNever() {
            this.isNever = true;
            return this;
        }

        public BingoTag build() {
            return new BingoTag(id, difficultyMax, allowedOnSameLine, isNever);
        }
    }

    public static class ReloadListener extends SimpleJsonResourceReloadListener {
        public static final ResourceLocation ID = new ResourceLocation("bingo:tags");
        private static final Gson GSON = new GsonBuilder().create();

        public ReloadListener() {
            super(GSON, "bingo/tags");
        }

        @NotNull
        @Override
        public String getName() {
            return ID.toString();
        }

        @Override
        protected void apply(Map<ResourceLocation, JsonElement> jsons, ResourceManager resourceManager, ProfilerFiller profiler) {
            final ImmutableMap.Builder<ResourceLocation, BingoTag> result = ImmutableMap.builder();
            for (final var entry : jsons.entrySet()) {
                try {
                    final JsonObject json = GsonHelper.convertToJsonObject(entry.getValue(), "bingo tag");
                    result.put(entry.getKey(), deserialize(entry.getKey(), json));
                } catch (Exception e) {
                    Bingo.LOGGER.error("Parsing error in bingo tag {}: {}", entry.getKey(), e.getMessage());
                }
            }
            tags = result.build();
            Bingo.LOGGER.info("Loaded {} bingo tags", tags.size());
        }
    }
}
