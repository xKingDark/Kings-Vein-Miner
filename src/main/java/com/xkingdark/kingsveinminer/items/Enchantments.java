package com.xkingdark.kingsveinminer.items;

import com.xkingdark.kingsveinminer.Main;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class Enchantments {
    public static final ResourceKey<Enchantment> VEIN_MINER = keyOf("vein_miner");
    public static final ResourceKey<Enchantment> TREE_CAPITATOR = keyOf("tree_capitator");

    private static ResourceKey<Enchantment> keyOf(String id) {
        return ResourceKey.create(Registries.ENCHANTMENT, Identifier.fromNamespaceAndPath(Main.MOD_ID, id));
    }

    private static List<BlockPos> getVeinShape(BlockPos pos) {
        return List.of(
            pos.above(), pos.below(),

            pos.north(), pos.south(),
            pos.east(), pos.west()
        );
    }

    public static void applyVeinMiner(
        List<BlockPos> visitedBlocks,
        Level world,
        Player player,
        ItemStack itemStack,
        BlockState mainState,
        BlockPos pos
    ) {
        List<BlockPos> blocks = Enchantments.getVeinShape(pos);
        for (BlockPos blockPos : blocks) {
            if (visitedBlocks.size() >= 128
                || itemStack.getDamageValue() + visitedBlocks.size() == itemStack.getMaxDamage())
                break;

            if (visitedBlocks.contains(blockPos))
                continue;

            BlockState blockState = world.getBlockState(blockPos);
            Block block = blockState.getBlock();
            if (!block.equals(mainState.getBlock())
                || !itemStack.isCorrectToolForDrops(blockState))
                continue;

            visitedBlocks.add(blockPos);

            block.playerDestroy(world, player, blockPos, blockState, null, itemStack);
            world.setBlockAndUpdate(blockPos, Blocks.AIR.defaultBlockState());
            block.playerWillDestroy(world, blockPos, blockState, player);

            applyVeinMiner(visitedBlocks, world, player, itemStack, mainState, blockPos);
        }
    }

    public static boolean hasEnchantment(Level level, ItemStack itemStack, ResourceKey<Enchantment> key) {
        Registry<Enchantment> registry = level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
        Holder.Reference<Enchantment> enchantment = registry.getOrThrow(key);

        return itemStack.getEnchantments().getLevel(enchantment) > 0;
    }
}
