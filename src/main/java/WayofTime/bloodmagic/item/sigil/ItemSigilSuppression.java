package WayofTime.bloodmagic.item.sigil;

import WayofTime.bloodmagic.tile.TileSpectralBlock;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.IFluidBlock;

public class ItemSigilSuppression extends ItemSigilToggleable {

    public ItemSigilSuppression() {
        super("suppression", 400);
    }

    @Override
    public void onSigilUpdate(ItemStack stack, World world, EntityPlayer player, int itemSlot, boolean isSelected) {
        int x = (int) player.posX;
        int y = (int) player.posY;
        int z = (int) player.posZ;
        final int radius = 5;
        final int refresh = 100;

        for (int i = -radius; i <= radius; i++) {
            for (int j = -radius; j <= radius; j++) {
                for (int k = -radius; k <= radius; k++) {
                    if (i * i + j * j + k * k >= (radius + 0.50f) * (radius + 0.50f)) {
                        continue;
                    }

                    BlockPos blockPos = new BlockPos(x + i, y + j, z + k);
                    Block block = world.getBlockState(blockPos).getBlock();

                    if (isBlockLiquid(block) && world.getTileEntity(blockPos) == null)
                        TileSpectralBlock.createSpectralBlock(world, blockPos, refresh);
                    else {
                        TileEntity tile = world.getTileEntity(blockPos);
                        if (tile instanceof TileSpectralBlock)
                            ((TileSpectralBlock) tile).resetDuration(refresh);
                    }
                }
            }
        }
    }

    private boolean isBlockLiquid(Block block) {
        return (block instanceof IFluidBlock || block.getMaterial().isLiquid());
    }
}
