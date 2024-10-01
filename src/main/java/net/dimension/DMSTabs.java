package net.dimension;

import net.dimension.Config.Config;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.protocol.game.ClientboundTabListPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class DMSTabs implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("DMSTabs");
//    private int onlinePlayers = 0;

    public void GetPlayer(MinecraftServer server, ServerGamePacketListenerImpl handler, Boolean joined) {

        String headerText = Config.INSTANCE.header != null ? Config.INSTANCE.header : "Welcome!";
        String footerText = Config.INSTANCE.footer != null ? Config.INSTANCE.footer : "Enjoy your stay!";

        // Add player count to footer instead of header
        if (Config.INSTANCE.online_player) {
            String playerCountText = Config.INSTANCE.onlinePlayerText
                    .replace("{online}", String.valueOf(server.getPlayerCount() + (joined ? + 1 : -1)));
            headerText += "\n" + playerCountText;
        }
        server.getPlayerList().broadcastAll(new ClientboundTabListPacket(
                Component.literal(headerText).setStyle(Style.EMPTY.withColor(ChatFormatting.GREEN)),
                Component.literal(footerText).setStyle(Style.EMPTY.withColor(ChatFormatting.GREEN))));

        handler.player.connection.send(new ClientboundTabListPacket(
                Component.literal(headerText).setStyle(Style.EMPTY.withColor(ChatFormatting.GREEN)),
                Component.literal(footerText).setStyle(Style.EMPTY.withColor(ChatFormatting.GREEN))));
    }

    @Override
    public void onInitialize() {
        try {
            Config.INSTANCE.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            GetPlayer(server, handler, true);
        });

        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            GetPlayer(server, handler, false);
        });

        LOGGER.info("Loaded DMSTabs");
    }
}
