package com.xkingdark.kingsveinminer.helpers;

import com.xkingdark.kingsveinminer.Main;
import com.xkingdark.kingsveinminer.items.Enchantments;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class Registry {
    public static void initialize() {
        PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, entity) -> {
            ItemStack mainhand = player.getMainHandStack();
            if (!player.isSneaking()) {
                List<BlockPos> visitedBlocks = new ArrayList<>();
                TagKey<Block> ORES = TagKey.of(RegistryKeys.BLOCK, Identifier.of(Main.MOD_ID, "ores"));
                if ((
                    Enchantments.hasEnchantment(world, mainhand, Enchantments.VEIN_MINER)
                        && state.isIn(ORES)
                ) || (
                    Enchantments.hasEnchantment(world, mainhand, Enchantments.TREE_CAPITATOR)
                        && state.isIn(BlockTags.LOGS)
                )) Enchantments.applyVeinMiner(visitedBlocks, world, player, mainhand, state, pos);

                mainhand.damage(visitedBlocks.size(), player);
            }
        });
    }
}
