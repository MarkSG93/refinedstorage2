package com.refinedmods.refinedstorage2.platform.api.support.energy;

import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.apiguardian.api.API;

@API(status = API.Status.STABLE, since = "2.0.0-milestone.3.1")
public abstract class AbstractEnergyBlockItem extends BlockItem {
    private final EnergyItemHelper helper;

    protected AbstractEnergyBlockItem(final Block block, final Properties properties, final EnergyItemHelper helper) {
        super(block, properties);
        this.helper = helper;
    }

    @Override
    public void appendHoverText(
        final ItemStack stack,
        final TooltipContext context,
        final List<Component> lines,
        final TooltipFlag flag
    ) {
        super.appendHoverText(stack, context, lines, flag);
        helper.addTooltip(stack, lines);
    }

    @Override
    public boolean isBarVisible(final ItemStack stack) {
        return helper.isBarVisible(stack);
    }

    @Override
    public int getBarWidth(final ItemStack stack) {
        return helper.getBarWidth(stack);
    }

    @Override
    public int getBarColor(final ItemStack stack) {
        return helper.getBarColor(stack);
    }

    public ItemStack createAtEnergyCapacity() {
        return helper.createAtEnergyCapacity(this);
    }

    @Override
    protected boolean updateCustomBlockEntityTag(
        final BlockPos pos,
        final Level level,
        @Nullable final Player player,
        final ItemStack stack,
        final BlockState blockState
    ) {
        final boolean result = super.updateCustomBlockEntityTag(pos, level, player, stack, blockState);
        helper.passEnergyToBlockEntity(pos, level, stack);
        return result;
    }
}
