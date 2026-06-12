package com.xkingdark.kingsveinminer.helpers;

import com.xkingdark.kingsveinminer.Main;
import com.xkingdark.kingsveinminer.items.Enchantments;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;

public class Registry {
    public static void initialize() {
        PlayerBlockBreakEvents.AFTER.register((level, player, pos, state, entity) -> {
            Main.LOGGER.info("Meow!");

            ItemStack mainhand = player.getMainHandItem();
            if (player.isCrouching()) {
                return;
            }

            List<BlockPos> visitedBlocks = new ArrayList<>();
            TagKey<Block> ORES = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(Main.MOD_ID, "ores"));
            if (
                (Enchantments.hasEnchantment(level, mainhand, Enchantments.VEIN_MINER) && state.is(ORES)) ||
                    (Enchantments.hasEnchantment(level, mainhand, Enchantments.TREE_CAPITATOR) && state.is(BlockTags.LOGS))
            ) {
                Enchantments.applyVeinMiner(visitedBlocks, level, player, mainhand, state, pos);
            }

            mainhand.hurtWithoutBreaking(visitedBlocks.size(), player);
        });
    }
}
