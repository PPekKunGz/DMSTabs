package net.dimension;

import net.dimension.Config.Config;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.protocol.game.ClientboundTabListPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


public class DMSTabs implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("DMSTabs");

    @Override
    public void onInitialize() {

        try {
            Config.INSTANCE.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
                handler.player.connection.send(new ClientboundTabListPacket(
                        Component.literal(Config.INSTANCE.header != null ? Config.INSTANCE.header : "asdasd").setStyle(Style.EMPTY.withColor(ChatFormatting.GREEN)),
                        Component.literal(Config.INSTANCE.footer != null ? Config.INSTANCE.footer : "asdasd").setStyle(Style.EMPTY.withColor(ChatFormatting.GREEN))));
        });
        LOGGER.info("Loaded DMSTabs");
    }
}
