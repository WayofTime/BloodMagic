package WayofTime.bloodmagic.item;

import java.util.ArrayList;
import java.util.List;

import WayofTime.bloodmagic.client.IVariantProvider;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.altar.BloodAltar;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.altar.AltarComponent;
import WayofTime.bloodmagic.api.altar.EnumAltarTier;
import WayofTime.bloodmagic.api.altar.IAltarManipulator;
import WayofTime.bloodmagic.api.altar.IBloodAltar;
import WayofTime.bloodmagic.api.util.helper.NBTHelper;
import WayofTime.bloodmagic.block.BlockAltar;
import WayofTime.bloodmagic.util.ChatUtil;
import WayofTime.bloodmagic.util.Utils;
import WayofTime.bloodmagic.util.helper.TextHelper;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class ItemAltarMaker extends Item implements IAltarManipulator, IVariantProvider
{
    private EnumAltarTier tierToBuild = EnumAltarTier.ONE;

    public ItemAltarMaker()
    {
        super();
        setUnlocalizedName(Constants.Mod.MODID + ".altarMaker");
        setRegistryName(Constants.BloodMagicItem.ALTAR_MAKER.getRegName());
        setCreativeTab(BloodMagic.tabBloodMagic);
        setMaxStackSize(1);
        setFull3D();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
    {
        stack = NBTHelper.checkNBT(stack);
        tooltip.add(TextHelper.localizeEffect("tooltip.BloodMagic.currentTier", stack.getTagCompound().getInteger(Constants.NBT.ALTARMAKER_CURRENT_TIER) + 1));
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
    {
        if (world.isRemote)
            return stack;

        if (!player.capabilities.isCreativeMode)
        {
            ChatUtil.sendNoSpam(player, TextHelper.localizeEffect("chat.BloodMagic.altarMaker.creativeOnly"));
            return stack;
        }

        stack = NBTHelper.checkNBT(stack);

        if (player.isSneaking())
        {
            if (stack.getTagCompound().getInteger(Constants.NBT.ALTARMAKER_CURRENT_TIER) >= EnumAltarTier.MAXTIERS - 1)
                stack.getTagCompound().setInteger(Constants.NBT.ALTARMAKER_CURRENT_TIER, 0);
            else
                stack.getTagCompound().setInteger(Constants.NBT.ALTARMAKER_CURRENT_TIER, stack.getTagCompound().getInteger(Constants.NBT.ALTARMAKER_CURRENT_TIER) + 1);

            setTierToBuild(EnumAltarTier.values()[stack.getTagCompound().getInteger(Constants.NBT.ALTARMAKER_CURRENT_TIER)]);
            ChatUtil.sendNoSpam(player, TextHelper.localizeEffect("chat.BloodMagic.altarMaker.setTier", stack.getTagCompound().getInteger(Constants.NBT.ALTARMAKER_CURRENT_TIER) + 1));
            return stack;
        }

        MovingObjectPosition mop = getMovingObjectPositionFromPlayer(world, player, false);
        if (mop == null || mop.typeOfHit == MovingObjectPosition.MovingObjectType.MISS || mop.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY)
            return stack;

        if (mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && world.getBlockState(mop.getBlockPos()).getBlock() instanceof BlockAltar)
        {
            ChatUtil.sendNoSpam(player, TextHelper.localizeEffect("chat.BloodMagic.altarMaker.building", tierToBuild));
            buildAltar(world, mop.getBlockPos());

            world.markBlockForUpdate(mop.getBlockPos());
        }

        return stack;
    }

    @Override
    public List<Pair<Integer, String>> getVariants()
    {
        List<Pair<Integer, String>> ret = new ArrayList<Pair<Integer, String>>();
        ret.add(new ImmutablePair<Integer, String>(0, "type=altarmaker"));
        return ret;
    }

    public void setTierToBuild(EnumAltarTier tierToBuild)
    {
        this.tierToBuild = tierToBuild;
    }

    public void buildAltar(World world, BlockPos pos)
    {
        if (world.isRemote)
            return;

        if (tierToBuild == EnumAltarTier.ONE)
            return;

        for (AltarComponent altarComponent : tierToBuild.getAltarComponents())
        {
            BlockPos componentPos = pos.add(altarComponent.getOffset());
            Block blockForComponent = Utils.getBlockForComponent(altarComponent.getComponent());

            world.setBlockState(componentPos, blockForComponent.getDefaultState(), 3);
        }

        ((IBloodAltar) world.getTileEntity(pos)).checkTier();
    }

    public String destroyAltar(EntityPlayer player)
    {
        World world = player.worldObj;
        if (world.isRemote)
            return "";

        MovingObjectPosition mop = getMovingObjectPositionFromPlayer(world, player, false);
        BlockPos pos = mop.getBlockPos();
        EnumAltarTier altarTier = BloodAltar.getAltarTier(world, pos);

        if (altarTier.equals(EnumAltarTier.ONE))
            return "" + altarTier.toInt();
        else
        {
            for (AltarComponent altarComponent : altarTier.getAltarComponents())
            {
                BlockPos componentPos = pos.add(altarComponent.getOffset());

                world.setBlockToAir(componentPos);
                world.markBlockForUpdate(componentPos);
            }
        }

        world.markBlockForUpdate(pos);
        return String.valueOf(altarTier.toInt());
    }
}
