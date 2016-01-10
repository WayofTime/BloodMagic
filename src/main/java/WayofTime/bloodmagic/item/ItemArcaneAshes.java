package WayofTime.bloodmagic.item;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.registry.ModBlocks;
import WayofTime.bloodmagic.util.helper.TextHelper;

public class ItemArcaneAshes extends Item
{
    public ItemArcaneAshes()
    {
        setUnlocalizedName(Constants.Mod.MODID + ".arcaneAshes");
        setMaxStackSize(1);
        setMaxDamage(19); //Allows for 20 uses
        setCreativeTab(BloodMagic.tabBloodMagic);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced)
    {
        tooltip.add(TextHelper.localizeEffect("tooltip.BloodMagic.arcaneAshes"));
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos blockPos, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        BlockPos newPos = blockPos.offset(side);

        if (world.isAirBlock(newPos))
        {
            if (!world.isRemote)
            {
                world.setBlockState(newPos, ModBlocks.alchemyArray.getDefaultState());
                stack.damageItem(1, player);
            }

            return true;
        }

        return false;
    }
}
