package WayofTime.bloodmagic.item;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.altar.BloodAltar;
import WayofTime.bloodmagic.api.impl.BloodMagicAPI;
import WayofTime.bloodmagic.apibutnotreally.Constants;
import WayofTime.bloodmagic.apibutnotreally.altar.*;
import WayofTime.bloodmagic.apibutnotreally.util.helper.NBTHelper;
import WayofTime.bloodmagic.block.BlockAltar;
import WayofTime.bloodmagic.client.IVariantProvider;
import WayofTime.bloodmagic.util.ChatUtil;
import WayofTime.bloodmagic.util.helper.NumeralHelper;
import WayofTime.bloodmagic.util.helper.TextHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class ItemAltarMaker extends Item implements IAltarManipulator, IVariantProvider {
    private EnumAltarTier tierToBuild = EnumAltarTier.ONE;

    public ItemAltarMaker() {
        super();
        setUnlocalizedName(BloodMagic.MODID + ".altarMaker");
        setCreativeTab(BloodMagic.TAB_BM);
        setMaxStackSize(1);
        setFull3D();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
        if (!stack.hasTagCompound())
            return;
        tooltip.add(TextHelper.localizeEffect("tooltip.bloodmagic.currentTier", stack.getTagCompound().getInteger(Constants.NBT.ALTARMAKER_CURRENT_TIER) + 1));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (world.isRemote)
            return super.onItemRightClick(world, player, hand);

        if (!player.capabilities.isCreativeMode) {
            ChatUtil.sendNoSpam(player, TextHelper.localizeEffect("chat.bloodmagic.altarMaker.creativeOnly"));
            return super.onItemRightClick(world, player, hand);
        }

        stack = NBTHelper.checkNBT(stack);

        if (player.isSneaking()) {
            if (stack.getTagCompound().getInteger(Constants.NBT.ALTARMAKER_CURRENT_TIER) >= EnumAltarTier.MAXTIERS - 1)
                stack.getTagCompound().setInteger(Constants.NBT.ALTARMAKER_CURRENT_TIER, 0);
            else
                stack.getTagCompound().setInteger(Constants.NBT.ALTARMAKER_CURRENT_TIER, stack.getTagCompound().getInteger(Constants.NBT.ALTARMAKER_CURRENT_TIER) + 1);

            setTierToBuild(EnumAltarTier.values()[stack.getTagCompound().getInteger(Constants.NBT.ALTARMAKER_CURRENT_TIER)]);
            ChatUtil.sendNoSpam(player, TextHelper.localizeEffect("chat.bloodmagic.altarMaker.setTier", NumeralHelper.toRoman(stack.getTagCompound().getInteger(Constants.NBT.ALTARMAKER_CURRENT_TIER) + 1)));
            return super.onItemRightClick(world, player, hand);
        }

        RayTraceResult rayTrace = rayTrace(world, player, false);
        if (rayTrace == null || rayTrace.typeOfHit == RayTraceResult.Type.MISS || rayTrace.typeOfHit == RayTraceResult.Type.ENTITY)
            return super.onItemRightClick(world, player, hand);

        if (rayTrace.typeOfHit == RayTraceResult.Type.BLOCK && world.getBlockState(rayTrace.getBlockPos()).getBlock() instanceof BlockAltar) {
            ChatUtil.sendNoSpam(player, TextHelper.localizeEffect("chat.bloodmagic.altarMaker.building", NumeralHelper.toRoman(tierToBuild.toInt())));
            buildAltar(world, rayTrace.getBlockPos());
            IBlockState state = world.getBlockState(rayTrace.getBlockPos());

            world.notifyBlockUpdate(rayTrace.getBlockPos(), state, state, 3);
        }

        return super.onItemRightClick(world, player, hand);
    }

    @Override
    public List<Pair<Integer, String>> getVariants() {
        List<Pair<Integer, String>> ret = new ArrayList<Pair<Integer, String>>();
        ret.add(new ImmutablePair<Integer, String>(0, "type=altarmaker"));
        return ret;
    }

    public void setTierToBuild(EnumAltarTier tierToBuild) {
        this.tierToBuild = tierToBuild;
    }

    public void buildAltar(World world, BlockPos pos) {
        if (world.isRemote)
            return;

        if (tierToBuild == EnumAltarTier.ONE)
            return;

        for (AltarComponent altarComponent : tierToBuild.getAltarComponents()) {
            BlockPos componentPos = pos.add(altarComponent.getOffset());
            if (altarComponent.getComponent() == EnumAltarComponent.NOTAIR) {
                world.setBlockState(componentPos, Blocks.STONEBRICK.getDefaultState());
                continue;
            }

            world.setBlockState(componentPos, BloodMagicAPI.INSTANCE.getComponentStates(altarComponent.getComponent()).get(0));
        }

        ((IBloodAltar) world.getTileEntity(pos)).checkTier();
    }

    public String destroyAltar(EntityPlayer player) {
        World world = player.getEntityWorld();
        if (world.isRemote)
            return "";

        RayTraceResult rayTrace = rayTrace(world, player, false);
        BlockPos pos = rayTrace.getBlockPos();
        IBlockState state = world.getBlockState(pos);
        EnumAltarTier altarTier = BloodAltar.getAltarTier(world, pos);

        if (altarTier.equals(EnumAltarTier.ONE))
            return "" + altarTier.toInt();
        else {
            for (AltarComponent altarComponent : altarTier.getAltarComponents()) {
                BlockPos componentPos = pos.add(altarComponent.getOffset());
                world.setBlockToAir(componentPos);
            }
        }

        world.notifyBlockUpdate(pos, state, state, 3);
        return String.valueOf(altarTier.toInt());
    }
}
