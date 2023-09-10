package io.github.gaming32.bingo.mixin.common;

import io.github.gaming32.bingo.triggers.BingoTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BedBlock.class)
public class MixinBedBlock {
    @Inject(
        method = "bounceUp",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/Entity;setDeltaMovement(DDD)V"
        )
    )
    private void bounceUpTrigger(Entity entity, CallbackInfo ci) {
        if (entity instanceof ServerPlayer player && player.getDeltaMovement().y <= -0.1) {
            BingoTriggers.BOUNCE_ON_BED.trigger(player);
        }
    }

    @Inject(method = "setPlacedBy", at = @At("RETURN"))
    private void onPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack, CallbackInfo ci) {
        if (placer instanceof ServerPlayer player) {
            BingoTriggers.BED_ROW.trigger(player, level, pos);
        }
    }
}
