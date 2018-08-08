package WayofTime.bloodmagic.item.sigil;

import WayofTime.bloodmagic.api.impl.BloodMagicAPI;
import WayofTime.bloodmagic.core.data.SoulTicket;
import WayofTime.bloodmagic.util.helper.NetworkHelper;
import WayofTime.bloodmagic.util.helper.PlayerHelper;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;

public class ItemSigilGreenGrove extends ItemSigilToggleableBase {
    public ItemSigilGreenGrove() {
        super("green_grove", 150);
    }

    @Override
    public boolean onSigilUse(ItemStack stack, EntityPlayer player, World world, BlockPos blockPos, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (PlayerHelper.isFakePlayer(player))
            return false;

        if (NetworkHelper.getSoulNetwork(player).syphonAndDamage(player, SoulTicket.item(stack, world, player, getLpUsed())).isSuccess() && applyBonemeal(world, blockPos, player, stack)) {
            if (!world.isRemote) {
                world.playEvent(2005, blockPos, 0);
            }
            return true;
        }

        return false;
    }

    @Override
    public void onSigilUpdate(ItemStack stack, World worldIn, EntityPlayer player, int itemSlot, boolean isSelected) {
        if (PlayerHelper.isFakePlayer(player))
            return;

        int range = 3;
        int verticalRange = 2;
        int posX = (int) Math.round(player.posX - 0.5f);
        int posY = (int) player.posY;
        int posZ = (int) Math.round(player.posZ - 0.5f);

        for (int ix = posX - range; ix <= posX + range; ix++) {
            for (int iz = posZ - range; iz <= posZ + range; iz++) {
                for (int iy = posY - verticalRange; iy <= posY + verticalRange; iy++) {
                    BlockPos blockPos = new BlockPos(ix, iy, iz);
                    IBlockState state = worldIn.getBlockState(blockPos);

                    if (!BloodMagicAPI.INSTANCE.getBlacklist().getGreenGrove().contains(state)) {
                        if (state.getBlock() instanceof IGrowable) {
                            if (worldIn.rand.nextInt(50) == 0) {
                                IBlockState preBlockState = worldIn.getBlockState(blockPos);
                                state.getBlock().updateTick(worldIn, blockPos, state, worldIn.rand);

                                IBlockState newState = worldIn.getBlockState(blockPos);
                                if (!newState.equals(preBlockState) && !worldIn.isRemote)
                                    worldIn.playEvent(2005, blockPos, 0);
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean applyBonemeal(World worldIn, BlockPos target, EntityPlayer player, ItemStack sigilStack) {
        IBlockState iblockstate = worldIn.getBlockState(target);

        BonemealEvent event = new BonemealEvent(player, worldIn, target, iblockstate, EnumHand.MAIN_HAND, sigilStack);
        if (MinecraftForge.EVENT_BUS.post(event))
            return false;
        else if (event.getResult() == Result.ALLOW)
            return true;

        if (iblockstate.getBlock() instanceof IGrowable) {
            IGrowable igrowable = (IGrowable) iblockstate.getBlock();

            if (igrowable.canGrow(worldIn, target, iblockstate, worldIn.isRemote)) {
                if (!worldIn.isRemote) {
                    if (igrowable.canUseBonemeal(worldIn, worldIn.rand, target, iblockstate))
                        igrowable.grow(worldIn, worldIn.rand, target, iblockstate);
                }
                return true;
            }
        }

        return false;
    }
}
