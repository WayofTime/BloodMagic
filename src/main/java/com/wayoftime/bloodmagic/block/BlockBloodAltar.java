package com.wayoftime.bloodmagic.block;

import com.wayoftime.bloodmagic.BloodMagic;
import com.wayoftime.bloodmagic.core.altar.IAltarManipulator;
import com.wayoftime.bloodmagic.core.util.register.IItemProvider;
import com.wayoftime.bloodmagic.tile.TileBloodAltar;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;

public class BlockBloodAltar extends Block implements IItemProvider {

    public BlockBloodAltar() {
        super(Material.ROCK);

        setTranslationKey(BloodMagic.MODID + ":blood_altar");
        setCreativeTab(BloodMagic.TAB_BM);
        setHardness(2.0F);
        setResistance(5.0F);
        setSoundType(SoundType.STONE);
        setHarvestLevel("pickaxe", 1);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        TileEntity tile = world.getTileEntity(pos);
        if (!(tile instanceof TileBloodAltar) || !tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null))
            return false;

        IItemHandler altarInv = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        if (altarInv == null)
            return false;

        if (player.isSneaking()) {
            ItemStack extracted = altarInv.extractItem(0, altarInv.getSlotLimit(0), false);
            if (extracted.isEmpty())
                return false;

            ItemHandlerHelper.giveItemToPlayer(player, extracted);
            tile.markDirty();
            return true;
        } else {
            ItemStack held = player.getHeldItem(hand);
            if (held.isEmpty())
                return false;

            if (held.getItem() instanceof IAltarManipulator && ((IAltarManipulator) held.getItem()).tryManipulate(player, held, world, pos))
                return false;

            if (!altarInv.extractItem(0, 1, true).isEmpty())
                return false;

            ItemStack insert = held.copy();
            insert.setCount(1);
            ItemHandlerHelper.insertItem(altarInv, insert, false);
            ((TileBloodAltar) tile).resetProgress();
            tile.markDirty();
            if (!player.capabilities.isCreativeMode)
                held.shrink(1);
            return true;
        }
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile == null)
            return;

        IItemHandler itemHandler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        if (itemHandler != null)
            for (int i = 0; i < itemHandler.getSlots(); i++)
                InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), itemHandler.getStackInSlot(i));
    }

    @Override
    public boolean isNormalCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean causesSuffocation(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullBlock(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileBloodAltar();
    }

    @Nullable
    @Override
    public Item getItem() {
        return new ItemBlock(this);
    }
}
