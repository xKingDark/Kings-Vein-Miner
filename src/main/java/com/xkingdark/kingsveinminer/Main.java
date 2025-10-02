package com.xkingdark.kingsveinminer;

import com.xkingdark.kingsveinminer.helpers.PackHashing;
import com.xkingdark.kingsveinminer.helpers.Registry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.network.packet.s2c.common.ResourcePackSendS2CPacket;
import net.minecraft.resource.ResourcePack;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Optional;
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

        byte[] digest = PackHashing.computePackHash();
        if (digest != null) {
            String packHash = PackHashing.toHex(digest);
            UUID packUUID = PackHashing.calculateUUID(digest);

            LOGGER.info("Resource pack hash: {}", packHash);
            LOGGER.info("Resource pack UUID: {}", packUUID);

            ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
                ResourcePackSendS2CPacket packet = new ResourcePackSendS2CPacket(
                    packUUID,
                    "https://raw.githubusercontent.com/xKingdark/Kings-Vein-Miner/" + BuildInfo.COMMIT_HASH + "/src/main/resources/resourcepacks/kings-vein-miner.zip",
                    packHash, true,
                    Optional.of(Text.of("King's Vein Miner resources"))
                );

                handler.player.networkHandler.sendPacket(packet);
            });
        };

        Registry.initialize();
    };


};