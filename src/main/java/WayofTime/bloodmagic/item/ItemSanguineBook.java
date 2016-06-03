package WayofTime.bloodmagic.item;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.iface.IDocumentedBlock;
import WayofTime.bloodmagic.client.IVariantProvider;
import WayofTime.bloodmagic.util.ChatUtil;
import WayofTime.bloodmagic.util.helper.TextHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Collections;
import java.util.List;

public class ItemSanguineBook extends Item implements IVariantProvider
{
    public ItemSanguineBook()
    {
        setUnlocalizedName(Constants.Mod.MODID + ".sanguineBook");
        setCreativeTab(BloodMagic.tabBloodMagic);
        setMaxStackSize(1);
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (world.isRemote)
            return super.onItemUse(stack, player, world, pos, hand, facing, hitX, hitY, hitZ);

        IBlockState hitState = world.getBlockState(pos);
        if (player.isSneaking() && hitState.getBlock() instanceof IDocumentedBlock)
        {
            IDocumentedBlock documentedBlock = (IDocumentedBlock) hitState.getBlock();
            List<ITextComponent> docs = documentedBlock.getDocumentation(player, world, pos, hitState);
            if (!docs.isEmpty())
            {
                ChatUtil.sendNoSpam(player, docs.toArray(new ITextComponent[docs.size()]));
                return EnumActionResult.SUCCESS;
            }
        }

        return super.onItemUse(stack, player, world, pos, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
    {
        tooltip.add(TextHelper.localizeEffect("tooltip.BloodMagic.book.shifting"));
        tooltip.add(TextFormatting.OBFUSCATED + "~ILikeTehNutsAndICannotLie");
    }

    // IVariantProvider

    @Override
    public List<Pair<Integer, String>> getVariants()
    {
        return Collections.singletonList(Pair.of(0, "type=normal"));
    }
}
