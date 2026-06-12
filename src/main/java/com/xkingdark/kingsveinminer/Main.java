package com.xkingdark.kingsveinminer;

import com.xkingdark.kingsveinminer.helpers.PackHashing;
import com.xkingdark.kingsveinminer.helpers.Registry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.ClientboundResourcePackPushPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
                ClientboundResourcePackPushPacket packet = new ClientboundResourcePackPushPacket(
                    packUUID,
                    "https://raw.githubusercontent.com/xKingDark/Kings-Vein-Miner/" + BuildInfo.COMMIT_HASH + "/src/main/resources/resourcepacks/kings-vein-miner.zip",
                    packHash, true,
                    Optional.of(Component.literal("King's Vein Miner resources"))
                );

                handler.player.connection.send(packet);
            });
        };

        Registry.initialize();
    };


};