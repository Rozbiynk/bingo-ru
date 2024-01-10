package io.github.gaming32.bingo.data.tags;

import io.github.gaming32.bingo.Bingo;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.decoration.PaintingVariant;

public final class BingoPaintingVariantTags {
    private BingoPaintingVariantTags() {
    }

    public static final TagKey<PaintingVariant> SIZE_1X1 = create("size_1x1");
    public static final TagKey<PaintingVariant> SIZE_1X2 = create("size_1x2");
    public static final TagKey<PaintingVariant> SIZE_2X1 = create("size_2x1");
    public static final TagKey<PaintingVariant> SIZE_2X2 = create("size_2x2");
    public static final TagKey<PaintingVariant> SIZE_4X2 = create("size_4x2");
    public static final TagKey<PaintingVariant> SIZE_4X3 = create("size_4x3");
    public static final TagKey<PaintingVariant> SIZE_4X4 = create("size_4x4");

    private static TagKey<PaintingVariant> create(String name) {
        return TagKey.create(Registries.PAINTING_VARIANT, new ResourceLocation(Bingo.MOD_ID, name));
    }
}
