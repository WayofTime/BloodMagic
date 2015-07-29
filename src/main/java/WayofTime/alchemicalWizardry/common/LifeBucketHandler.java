package WayofTime.alchemicalWizardry.common;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import WayofTime.alchemicalWizardry.ModBlocks;
import WayofTime.alchemicalWizardry.ModItems;

public class LifeBucketHandler
{
    @SubscribeEvent
    public void onBucketFill(FillBucketEvent event)
    {
        ItemStack result = fillCustomBucket(event.world, event.target);

        if (result == null)
        {
            return;
        }

        event.result = result;
        event.setResult(Result.ALLOW);
    }

    public ItemStack fillCustomBucket(World world, MovingObjectPosition pos)
    {
        IBlockState state = world.getBlockState(pos.func_178782_a());
        Block block = state.getBlock();

        if (block != null && (block.equals(ModBlocks.blockLifeEssence)) && block.getMetaFromState(state) == 0)
        {
            world.setBlockToAir(pos.func_178782_a());
            return new ItemStack(ModItems.bucketLife);
        } else
        {
            return null;
        }
    }
}