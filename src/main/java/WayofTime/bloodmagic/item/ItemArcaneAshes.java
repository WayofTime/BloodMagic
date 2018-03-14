package WayofTime.bloodmagic.item;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.client.IVariantProvider;
import WayofTime.bloodmagic.core.RegistrarBloodMagicBlocks;
import WayofTime.bloodmagic.tile.TileAlchemyArray;
import WayofTime.bloodmagic.util.helper.TextHelper;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemArcaneAshes extends Item implements IVariantProvider {
    public ItemArcaneAshes() {
        setUnlocalizedName(BloodMagic.MODID + ".arcaneAshes");
        setMaxStackSize(1);
        setMaxDamage(19); //Allows for 20 uses
        setCreativeTab(BloodMagic.TAB_BM);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
        tooltip.add(TextHelper.localizeEffect("tooltip.bloodmagic.arcaneAshes"));
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos blockPos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);
        BlockPos newPos = blockPos.offset(side);

        if (world.isAirBlock(newPos)) {
            if (!world.isRemote) {
                EnumFacing rotation = EnumFacing.fromAngle(player.getRotationYawHead());
                world.setBlockState(newPos, RegistrarBloodMagicBlocks.ALCHEMY_ARRAY.getDefaultState());
                TileEntity tile = world.getTileEntity(newPos);
                if (tile instanceof TileAlchemyArray) {
                    ((TileAlchemyArray) tile).setRotation(rotation);
                }

                stack.damageItem(1, player);
            }

            return EnumActionResult.SUCCESS;
        }

        return EnumActionResult.FAIL;
    }

    @Override
    public void gatherVariants(Int2ObjectMap<String> variants) {
        variants.put(0, "type=arcaneashes");
    }
}
