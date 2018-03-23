package WayofTime.bloodmagic.item;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.altar.AltarTier;
import WayofTime.bloodmagic.altar.IAltarManipulator;
import WayofTime.bloodmagic.client.IVariantProvider;
import WayofTime.bloodmagic.iface.IDocumentedBlock;
import WayofTime.bloodmagic.tile.TileAltar;
import WayofTime.bloodmagic.util.ChatUtil;
import WayofTime.bloodmagic.util.Constants;
import WayofTime.bloodmagic.util.helper.NBTHelper;
import WayofTime.bloodmagic.util.helper.NumeralHelper;
import WayofTime.bloodmagic.util.helper.TextHelper;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemSanguineBook extends Item implements IVariantProvider, IAltarManipulator {
    private AltarTier currentDisplayedTier = AltarTier.ONE;

    public ItemSanguineBook() {
        setUnlocalizedName(BloodMagic.MODID + ".sanguineBook");
        setCreativeTab(BloodMagic.TAB_BM);
        setMaxStackSize(1);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (world.isRemote)
            return super.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);

        IBlockState hitState = world.getBlockState(pos);
        if (player.isSneaking()) {
            if (hitState.getBlock() instanceof IDocumentedBlock) {
                trySetDisplayedTier(world, pos);
                IDocumentedBlock documentedBlock = (IDocumentedBlock) hitState.getBlock();
                List<ITextComponent> docs = documentedBlock.getDocumentation(player, world, pos, hitState);
                if (!docs.isEmpty()) {
                    ChatUtil.sendNoSpam(player, docs.toArray(new ITextComponent[docs.size()]));
                    return super.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
                }
            }
        }

        return super.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (world.isRemote)
            return super.onItemRightClick(world, player, hand);

        stack = NBTHelper.checkNBT(stack);

        RayTraceResult rayTrace = rayTrace(world, player, false);
        if (rayTrace == null || rayTrace.typeOfHit == RayTraceResult.Type.MISS || rayTrace.typeOfHit == RayTraceResult.Type.ENTITY) {
            if (stack.getTagCompound().getInteger(Constants.NBT.ALTARMAKER_CURRENT_TIER) >= AltarTier.MAXTIERS - 1)
                stack.getTagCompound().setInteger(Constants.NBT.ALTARMAKER_CURRENT_TIER, 0);
            else
                stack.getTagCompound().setInteger(Constants.NBT.ALTARMAKER_CURRENT_TIER, stack.getTagCompound().getInteger(Constants.NBT.ALTARMAKER_CURRENT_TIER) + 1);

            currentDisplayedTier = AltarTier.values()[stack.getTagCompound().getInteger(Constants.NBT.ALTARMAKER_CURRENT_TIER)];
            player.sendStatusMessage(new TextComponentTranslation("chat.bloodmagic.altarMaker.setTier", NumeralHelper.toRoman(stack.getTagCompound().getInteger(Constants.NBT.ALTARMAKER_CURRENT_TIER) + 1)), true);

            return super.onItemRightClick(world, player, hand);
        }

        return super.onItemRightClick(world, player, hand);
    }

    public boolean trySetDisplayedTier(World world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileAltar) {
            return !((TileAltar) tile).setCurrentTierDisplayed(currentDisplayedTier);
        }

        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
        if (!stack.hasTagCompound())
            return;
        tooltip.add(TextHelper.localizeEffect("tooltip.bloodmagic.book.shifting"));
        tooltip.add(TextFormatting.OBFUSCATED + "~ILikeTehNutsAndICannotLie");
        tooltip.add(TextHelper.localizeEffect("tooltip.bloodmagic.currentTier", stack.getTagCompound().getInteger(Constants.NBT.ALTARMAKER_CURRENT_TIER) + 1));
    }

    // IVariantProvider

    @Override
    public void gatherVariants(@Nonnull Int2ObjectMap<String> variants) {
        variants.put(0, "type=normal");
    }
}
