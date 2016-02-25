package WayofTime.bloodmagic.item.sigil;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.entity.projectile.EntityBloodLight;
import WayofTime.bloodmagic.registry.ModBlocks;

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
        MovingObjectPosition mop = this.getMovingObjectPositionFromPlayer(world, player, false);

        if (mop != null && mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
        {
            BlockPos blockPos = mop.getBlockPos().offset(mop.sideHit);

            if (world.isAirBlock(blockPos))
                world.setBlockState(blockPos, ModBlocks.bloodLight.getDefaultState());
        } else
        {
            world.spawnEntityInWorld(new EntityBloodLight(world, player));
        }

        return stack;
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos blockPos, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        super.onItemUse(stack, player, world, blockPos, side, hitX, hitY, hitZ);

        if (world.isRemote)
            return false;

        BlockPos newPos = blockPos.offset(side);

        if (world.isAirBlock(newPos))
            world.setBlockState(newPos, ModBlocks.bloodLight.getDefaultState());

        return true;
    }
}
