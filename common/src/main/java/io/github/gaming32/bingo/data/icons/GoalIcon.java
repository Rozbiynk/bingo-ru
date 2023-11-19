package io.github.gaming32.bingo.data.icons;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import io.github.gaming32.bingo.util.BingoCodecs;
import io.github.gaming32.bingo.util.BingoUtil;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.ApiStatus;

public interface GoalIcon {
    Codec<GoalIcon> CODEC = BingoCodecs.registrarByName(GoalIconType.REGISTRAR)
        .dispatch(GoalIcon::type, GoalIconType::codec);

    /**
     * Used for rendering count, as well as for a fallback for Vanilla clients.
     */
    ItemStack item();

    GoalIconType<?> type();

    @ApiStatus.NonExtendable
    default JsonObject serializeToJson() {
        return BingoUtil.toJsonObject(CODEC, this);
    }

    static GoalIcon deserialize(JsonElement element) {
        return BingoUtil.fromJsonElement(CODEC, element);
    }

    static GoalIcon infer(Object obj) {
        if (obj == null) {
            return EmptyIcon.INSTANCE;
        }
        if (obj instanceof GoalIcon icon) {
            return icon;
        }
        if (obj instanceof ItemStack stack) {
            return new ItemIcon(stack);
        }
        if (obj instanceof Block block) {
            return BlockIcon.ofBlock(block);
        }
        if (obj instanceof BlockState state) {
            return BlockIcon.ofBlock(state);
        }
        if (obj instanceof ItemLike item) {
            return ItemIcon.ofItem(item);
        }
        if (obj instanceof EntityType<?> entityType) {
            return EntityIcon.ofSpawnEgg(entityType);
        }
        throw new IllegalArgumentException("Couldn't infer GoalIcon from " + obj);
    }
}
