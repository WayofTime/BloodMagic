package WayofTime.bloodmagic.item;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.altar.BloodAltar;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.altar.AltarComponent;
import WayofTime.bloodmagic.api.altar.EnumAltarTier;
import WayofTime.bloodmagic.api.altar.IAltarManipulator;
import WayofTime.bloodmagic.block.BlockAltar;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemAltarMaker extends Item implements IAltarManipulator {

    private EnumAltarTier tierToBuild = EnumAltarTier.ONE;

    public ItemAltarMaker() {
        super();
        setUnlocalizedName(Constants.Mod.MODID + ".altarMaker");
        setCreativeTab(BloodMagic.tabBloodMagic);
        setMaxStackSize(1);
        setFull3D();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        tooltip.add(StatCollector.translateToLocal("tooltip.BloodMagic.currentTier") + " " + ((stack.getTagCompound() != null ? stack.getTagCompound().getInteger(Constants.NBT.ALTARMAKER_CURRENT_TIER) : 0) + 1));
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        if (!player.capabilities.isCreativeMode || world.isRemote) return itemStack;

        if (itemStack.getTagCompound() == null) {
            NBTTagCompound tag = new NBTTagCompound();
            itemStack.setTagCompound(tag);
            itemStack.getTagCompound().setInteger(Constants.NBT.ALTARMAKER_CURRENT_TIER, 0);
        }
        if (player.isSneaking()) {
            if (itemStack.getTagCompound().getInteger(Constants.NBT.ALTARMAKER_CURRENT_TIER) >= EnumAltarTier.MAXTIERS - 1) {
                itemStack.getTagCompound().setInteger(Constants.NBT.ALTARMAKER_CURRENT_TIER, 0);
                setTierToBuild(EnumAltarTier.values()[itemStack.getTagCompound().getInteger(Constants.NBT.ALTARMAKER_CURRENT_TIER)]);
                player.addChatComponentMessage(new ChatComponentTranslation(StatCollector.translateToLocal("misc.altarMaker.setTier"), itemStack.getTagCompound().getInteger(Constants.NBT.ALTARMAKER_CURRENT_TIER) + 1));
                return itemStack;
            }
            else {
                itemStack.getTagCompound().setInteger(Constants.NBT.ALTARMAKER_CURRENT_TIER, itemStack.getTagCompound().getInteger(Constants.NBT.ALTARMAKER_CURRENT_TIER) + 1);
                setTierToBuild(EnumAltarTier.values()[itemStack.getTagCompound().getInteger(Constants.NBT.ALTARMAKER_CURRENT_TIER)]);
                player.addChatComponentMessage(new ChatComponentTranslation(StatCollector.translateToLocal("misc.altarMaker.setTier"), itemStack.getTagCompound().getInteger(Constants.NBT.ALTARMAKER_CURRENT_TIER) + 1));
                return itemStack;
            }
        }

        MovingObjectPosition mop = getMovingObjectPositionFromPlayer(world, player, false);
        if (mop == null || mop.typeOfHit == MovingObjectPosition.MovingObjectType.MISS || mop.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) return itemStack;

        if (mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && world.getBlockState(mop.getBlockPos()).getBlock() instanceof BlockAltar) {

            player.addChatComponentMessage(new ChatComponentTranslation(StatCollector.translateToLocal("misc.altarMaker.building"), tierToBuild));
            buildAltar(world, mop.getBlockPos());

            world.markBlockForUpdate(mop.getBlockPos());
        }

        return itemStack;
    }

    public void setTierToBuild(EnumAltarTier tierToBuild) {
        this.tierToBuild = tierToBuild;
    }

    public void buildAltar(World world, BlockPos pos) {

        if (world.isRemote) return;

        if (tierToBuild == EnumAltarTier.ONE) {
            return;
        }

        for (AltarComponent altarComponent : tierToBuild.getAltarComponents()) {
            BlockPos componentPos = pos.add(altarComponent.getOffset());
            IBlockState blockState = altarComponent.getBlockStack().getBlock().getStateFromMeta(altarComponent.getBlockStack().getMeta());

            if (altarComponent.getBlockStack().getBlock().equals(Blocks.air))
                world.setBlockState(componentPos, Blocks.stonebrick.getStateFromMeta(0));
            else
                world.setBlockState(componentPos, blockState);

            world.markBlockForUpdate(componentPos);
        }

        world.markBlockForUpdate(pos);
    }

    public String destroyAltar(EntityPlayer player) {
        World world = player.worldObj;
        if (world.isRemote) return "";

        MovingObjectPosition mop = getMovingObjectPositionFromPlayer(world, player, false);
        BlockPos pos = mop.getBlockPos();
        EnumAltarTier altarTier = BloodAltar.getAltarTier(world, pos);

        if (altarTier.equals(EnumAltarTier.ONE)) return "" + altarTier.toInt();
        else {
            for (AltarComponent altarComponent : altarTier.getAltarComponents()) {
                BlockPos componentPos = pos.add(altarComponent.getOffset());

                world.setBlockToAir(componentPos);
                world.markBlockForUpdate(componentPos);
            }
        }

        world.markBlockForUpdate(pos);
        return "" + altarTier.toInt();
    }
}
