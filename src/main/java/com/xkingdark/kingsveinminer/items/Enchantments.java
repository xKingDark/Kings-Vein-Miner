package com.xkingdark.kingsveinminer.items;

import com.xkingdark.kingsveinminer.Main;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class Enchantments {
    public static final RegistryKey<Enchantment> VEIN_MINER = keyOf("vein_miner");
    public static final RegistryKey<Enchantment> TREE_CAPITATOR = keyOf("tree_capitator");

    private static RegistryKey<Enchantment> keyOf(String id) {
        return RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(Main.MOD_ID, id));
    }

    private static List<BlockPos> getVeinShape(BlockPos pos) {
        return List.of(
            pos.up(), pos.down(),

            pos.north(), pos.south(),
            pos.east(), pos.west()
        );
    }

    public static void applyVeinMiner(
        List<BlockPos> visitedBlocks,
        World world,
        PlayerEntity player,
        ItemStack itemStack,
        BlockState mainState,
        BlockPos pos
    ) {
        List<BlockPos> blocks = Enchantments.getVeinShape(pos);
        for (BlockPos blockPos : blocks) {
            if (visitedBlocks.size() >= 128
                || itemStack.getDamage() + visitedBlocks.size() == itemStack.getMaxDamage())
                break;

            if (visitedBlocks.contains(blockPos))
                continue;

            BlockState blockState = world.getBlockState(blockPos);
            Block block = blockState.getBlock();
            if (!block.equals(mainState.getBlock())
                || !itemStack.isSuitableFor(blockState))
                continue;

            visitedBlocks.add(blockPos);
            world.breakBlock(blockPos, true, player);

            applyVeinMiner(visitedBlocks, world, player, itemStack, mainState, blockPos);
        }
    }

    public static boolean hasEnchantment(World world, ItemStack itemStack, RegistryKey<Enchantment> key) {
        Registry<Enchantment> registry = world.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT);
        RegistryEntry<Enchantment> enchantment = registry.getOrThrow(key);

        return EnchantmentHelper.getLevel(enchantment, itemStack) > 0;
    }
}
