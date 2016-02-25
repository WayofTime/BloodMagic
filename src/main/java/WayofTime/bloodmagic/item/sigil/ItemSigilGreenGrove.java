package WayofTime.bloodmagic.item.sigil;

import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import WayofTime.bloodmagic.api.BloodMagicAPI;
import WayofTime.bloodmagic.api.Constants;

public class ItemSigilGreenGrove extends ItemSigilToggleable
{
    public ItemSigilGreenGrove()
    {
        super("greenGrove", 150);
        setRegistryName(Constants.BloodMagicItem.SIGIL_GREEN_GROVE.getRegName());
    }

    @Override
    public boolean onSigilUse(ItemStack stack, EntityPlayer player, World world, BlockPos blockPos, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (applyBonemeal(stack, world, blockPos))
        {
            world.playAuxSFX(2005, blockPos, 0);
            return true;
        }

        return false;
    }

    @Override
    public void onSigilUpdate(ItemStack stack, World worldIn, EntityPlayer player, int itemSlot, boolean isSelected)
    {
        int range = 3;
        int verticalRange = 2;
        int posX = (int) Math.round(player.posX - 0.5f);
        int posY = (int) player.posY;
        int posZ = (int) Math.round(player.posZ - 0.5f);

        for (int ix = posX - range; ix <= posX + range; ix++)
        {
            for (int iz = posZ - range; iz <= posZ + range; iz++)
            {
                for (int iy = posY - verticalRange; iy <= posY + verticalRange; iy++)
                {
                    BlockPos blockPos = new BlockPos(ix, iy, iz);
                    Block block = worldIn.getBlockState(blockPos).getBlock();

                    if (!BloodMagicAPI.getGreenGroveBlacklist().contains(block))
                    {
                        if (block instanceof IPlantable || block instanceof IGrowable)
                        {
                            if (worldIn.rand.nextInt(50) == 0)
                            {
                                IBlockState preBlockState = worldIn.getBlockState(blockPos);
                                block.updateTick(worldIn, blockPos, worldIn.getBlockState(blockPos), worldIn.rand);

                                if (!worldIn.getBlockState(blockPos).equals(preBlockState))
                                    worldIn.playAuxSFX(2005, blockPos, 0);
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean applyBonemeal(ItemStack stack, World worldIn, BlockPos target)
    {
        return worldIn instanceof net.minecraft.world.WorldServer && applyBonemeal(stack, worldIn, target, net.minecraftforge.common.util.FakePlayerFactory.getMinecraft((net.minecraft.world.WorldServer) worldIn));
    }

    private boolean applyBonemeal(ItemStack stack, World worldIn, BlockPos target, EntityPlayer player)
    {
        IBlockState iblockstate = worldIn.getBlockState(target);

        int hook = net.minecraftforge.event.ForgeEventFactory.onApplyBonemeal(player, worldIn, target, iblockstate, stack);
        if (hook != 0)
            return hook > 0;

        if (iblockstate.getBlock() instanceof IGrowable)
        {
            IGrowable igrowable = (IGrowable) iblockstate.getBlock();

            if (igrowable.canGrow(worldIn, target, iblockstate, worldIn.isRemote))
            {
                if (!worldIn.isRemote)
                {
                    if (igrowable.canUseBonemeal(worldIn, worldIn.rand, target, iblockstate))
                        igrowable.grow(worldIn, worldIn.rand, target, iblockstate);
                }
                return true;
            }
        }

        return false;
    }
}
