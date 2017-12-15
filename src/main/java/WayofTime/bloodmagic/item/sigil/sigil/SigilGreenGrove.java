package WayofTime.bloodmagic.item.sigil.sigil;

import WayofTime.bloodmagic.api_impl.BloodMagicAPI;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.fml.common.eventhandler.Event;

import javax.annotation.Nonnull;

public class SigilGreenGrove implements ISigil.Toggle {

    private static final int RANGE_HORIZONTAL = 3;
    private static final int RANGE_VERTICAL = 2;

    @Override
    public int getCost() {
        return 150;
    }

    @Override
    public EnumActionResult onInteract(@Nonnull ItemStack stack, @Nonnull EntityPlayer player, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull EnumFacing side, @Nonnull EnumHand hand) {
        if (applyBonemeal(world, pos, player, stack, hand)) {
            if (!world.isRemote)
                world.playEvent(2005, pos, 0);

            return EnumActionResult.SUCCESS;
        }

        return EnumActionResult.FAIL;
    }

    @Override
    public void onUpdate(@Nonnull ItemStack stack, @Nonnull EntityPlayer player, @Nonnull World world, int itemSlot, boolean isHeld) {
        int posX = (int) Math.round(player.posX - 0.5f);
        int posY = (int) player.posY;
        int posZ = (int) Math.round(player.posZ - 0.5f);

        for (int ix = posX - RANGE_HORIZONTAL; ix <= posX + RANGE_HORIZONTAL; ix++) {
            for (int iz = posZ - RANGE_HORIZONTAL; iz <= posZ + RANGE_HORIZONTAL; iz++) {
                for (int iy = posY - RANGE_VERTICAL; iy <= posY + RANGE_VERTICAL; iy++) {
                    BlockPos blockPos = new BlockPos(ix, iy, iz);
                    IBlockState state = world.getBlockState(blockPos);

                    if (!BloodMagicAPI.INSTANCE.getBlacklist().getGreenGrove().contains(state)) {
                        if (state.getBlock() instanceof IGrowable) {
                            if (world.rand.nextInt(50) == 0) {
                                state.getBlock().updateTick(world, blockPos, state, world.rand);
                                IBlockState newState = world.getBlockState(blockPos);

                                if (!newState.equals(state) && !world.isRemote)
                                    world.playEvent(2005, blockPos, 0);
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean applyBonemeal(World world, BlockPos target, EntityPlayer player, ItemStack sigilStack, EnumHand hand) {
        IBlockState state = world.getBlockState(target);

        BonemealEvent event = new BonemealEvent(player, world, target, state, hand, sigilStack);
        if (MinecraftForge.EVENT_BUS.post(event))
            return false;
        else if (event.getResult() == Event.Result.ALLOW)
            return true;

        if (state.getBlock() instanceof IGrowable) {
            IGrowable growable = (IGrowable) state.getBlock();

            if (growable.canGrow(world, target, state, world.isRemote)) {
                if (!world.isRemote && growable.canUseBonemeal(world, world.rand, target, state))
                    growable.grow(world, world.rand, target, state);

                return true;
            }
        }

        return false;
    }
}
