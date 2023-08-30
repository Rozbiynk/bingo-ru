package io.github.gaming32.bingo.network;

import io.github.gaming32.bingo.game.ActiveGoal;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public record ClientGoal(Component name, @Nullable Component tooltip, ItemStack icon) {
    public ClientGoal(ActiveGoal goal) {
        this(goal.getName(), goal.getTooltip(), goal.getIcon());
    }

    public ClientGoal(FriendlyByteBuf buf) {
        this(buf.readComponent(), buf.readNullable(FriendlyByteBuf::readComponent), buf.readItem());
    }

    public void serialize(FriendlyByteBuf buf) {
        buf.writeComponent(name);
        buf.writeNullable(tooltip, FriendlyByteBuf::writeComponent);
        buf.writeItem(icon);
    }
}