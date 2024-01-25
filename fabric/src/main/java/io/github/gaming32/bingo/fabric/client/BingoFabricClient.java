package io.github.gaming32.bingo.fabric.client;

import io.github.gaming32.bingo.Bingo;
import io.github.gaming32.bingo.client.ClientIconTooltip;
import io.github.gaming32.bingo.client.IconTooltip;
import io.github.gaming32.bingo.fabric.BingoFabric;
import io.github.gaming32.bingo.network.BingoNetworking;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.FriendlyByteBuf;

import java.util.concurrent.CompletableFuture;

public class BingoFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientLoginNetworking.registerGlobalReceiver(BingoFabric.PROTOCOL_VERSION_PACKET, (client, handler, buf, listenerAdder) -> {
            final int serverVersion = buf.readVarInt();
            if (serverVersion != BingoNetworking.PROTOCOL_VERSION) {
                Bingo.LOGGER.warn("Bingo client and server versions don't match. A disconnect is probably imminent.");
            }
            final FriendlyByteBuf response = PacketByteBufs.create();
            response.writeVarInt(BingoNetworking.PROTOCOL_VERSION);
            return CompletableFuture.completedFuture(response);
        });

        TooltipComponentCallback.EVENT.register(data -> {
            if (data instanceof IconTooltip iconTooltip) {
                return new ClientIconTooltip(iconTooltip);
            }
            return null;
        });
    }
}
