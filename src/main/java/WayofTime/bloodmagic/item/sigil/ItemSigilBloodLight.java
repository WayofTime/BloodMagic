package WayofTime.bloodmagic.item.sigil;

import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.util.helper.BindableHelper;
import WayofTime.bloodmagic.entity.projectile.EntityBloodLight;
import WayofTime.bloodmagic.item.ItemBindable;
import WayofTime.bloodmagic.registry.ModBlocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemSigilBloodLight extends ItemSigilBase
{
    public ItemSigilBloodLight()
    {
        super("bloodLight", 10);
        setRegistryName(Constants.BloodMagicItem.SIGIL_BLOOD_LIGHT.getRegName());
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
    {
        if (BindableHelper.checkAndSetItemOwner(stack, player) && ItemBindable.syphonNetwork(stack, player, getLPUsed() * 5) && !world.isRemote)
            world.spawnEntityInWorld(new EntityBloodLight(world, player));

        return stack;
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos blockPos, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        super.onItemUse(stack, player, world, blockPos, side, hitX, hitY, hitZ);

        if (world.isRemote)
            return false;

        if (BindableHelper.checkAndSetItemOwner(stack, player) && ItemBindable.syphonNetwork(stack, player, getLPUsed()))
        {
            BlockPos newPos = blockPos.offset(side);

            if (world.isAirBlock(newPos))
            {
                world.setBlockState(newPos, ModBlocks.bloodLight.getDefaultState());
            }
        }

        return true;
    }
}
