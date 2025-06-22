package com.xkingdark.kingsveinminer;

import com.xkingdark.kingsveinminer.helpers.Registry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.packet.s2c.common.ResourcePackSendS2CPacket;
import net.minecraft.resource.ResourcePack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.security.MessageDigest;
import java.util.UUID;

public class Main implements ModInitializer {
    public static final String MOD_ID = "xkingdark";
    public static final Logger LOGGER = LoggerFactory.getLogger("King's Vein Miner");
    
    @Override
    public void onInitialize() {
        LOGGER.info("Version: {}", BuildInfo.VERSION);
        LOGGER.info("Branch: {}", BuildInfo.BRANCH);
        LOGGER.info("Commit hash: {}", BuildInfo.COMMIT_HASH);
        LOGGER.info("Build ID: {}", BuildInfo.BUILD_ID);

        String packHash = computePackHash();
        if (packHash != null) {
            LOGGER.info("Resource pack hash: {}", packHash);

            ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
                ResourcePackSendS2CPacket packet = new ResourcePackSendS2CPacket(
                    UUID.randomUUID(),
                    "https://raw.githubusercontent.com/DarkGamerYT/Kings-Vein-Miner/" + BuildInfo.COMMIT_HASH + "/src/main/resources/resourcepacks/kings-vein-miner.zip",
                    packHash,
                    true,
                    null
                );
                handler.player.networkHandler.sendPacket(packet);
            });
        };

        Registry.initialize();
    };

    public static String computePackHash() {
        try (InputStream input = ResourcePack.class.getResourceAsStream("/resourcepacks/kings-vein-miner.zip")) {
            if (input == null) {
                throw new IllegalStateException("Resource pack not found in JAR.");
            }

            MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
            byte[] buffer = new byte[8192];
            int read;
            while ((read = input.read(buffer)) != -1)
                sha1.update(buffer, 0, read);

            StringBuilder sb = new StringBuilder();
            for (byte b : sha1.digest())
                sb.append(String.format("%02x", b));

            return sb.toString();

        } catch (Exception e) {
            return null;
        }
    };
};