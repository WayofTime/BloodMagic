package WayofTime.bloodmagic.item;

import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.ritual.EnumRuneType;
import WayofTime.bloodmagic.block.BlockRitualStone;
import WayofTime.bloodmagic.util.helper.TextHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;
import java.util.List;

// TODO - NBT based damage
public class ItemInscriptionTool extends ItemBindable {

    public ItemInscriptionTool() {
        super();

        setEnergyUsed(100);
        setUnlocalizedName(Constants.Mod.MODID + ".scribe.");
        setHasSubtypes(true);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName(stack) + EnumRuneType.values()[stack.getItemDamage()];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item id, CreativeTabs creativeTab, List<ItemStack> list) {
        for (int i = 1; i < EnumRuneType.values().length; i++)
            list.add(new ItemStack(id, 1, i));
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {

        IBlockState state = world.getBlockState(pos);

        if (state.getBlock() instanceof BlockRitualStone && !((BlockRitualStone)state.getBlock()).isRuneType(world, pos, getType(stack))) {
            world.setBlockState(pos, state.withProperty(((BlockRitualStone) state.getBlock()).getStringProp(), getType(stack).getName()));
            return true;
        }

        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean advanced) {
        list.addAll(Arrays.asList(TextHelper.cutLongString(TextHelper.localizeEffect("tooltip.BloodMagic.inscriber.desc"))));
        super.addInformation(stack, player, list, advanced);
    }

    public EnumRuneType getType(ItemStack stack) {
        return EnumRuneType.values()[stack.getItemDamage()];
    }
}
