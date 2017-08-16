package WayofTime.bloodmagic.item.sigil;

import java.util.HashMap;
import java.util.Map;

import WayofTime.bloodmagic.api.util.helper.PlayerHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import org.apache.commons.lang3.tuple.Pair;

import WayofTime.bloodmagic.core.RegistrarBloodMagicBlocks;

public class ItemSigilPhantomBridge extends ItemSigilToggleableBase
{
    private Map<EntityPlayer, Pair<Double, Double>> prevPositionMap = new HashMap<EntityPlayer, Pair<Double, Double>>();
    private double expansionFactor = 2;
    private int range = 3;

    public ItemSigilPhantomBridge()
    {
        super("phantomBridge", 100);
    }

    @Override
    public void onSigilUpdate(ItemStack stack, World world, EntityPlayer player, int itemSlot, boolean isSelected)
    {
        if (PlayerHelper.isFakePlayer(player))
            return;

        if (!prevPositionMap.containsKey(player))
        {
            prevPositionMap.put(player, Pair.of(player.posX, player.posZ));
        }

        if ((!player.onGround && !player.isRiding()) && !player.isSneaking())
        {
            prevPositionMap.put(player, Pair.of(player.posX, player.posZ));
            return;
        }

        int verticalOffset = -1;

        if (player.isSneaking())
            verticalOffset--;

        Pair<Double, Double> prevPosition = prevPositionMap.get(player);

        double playerVelX = player.posX - prevPosition.getLeft();
        double playerVelZ = player.posZ - prevPosition.getRight();

        double totalVel = Math.sqrt(playerVelX * playerVelX + playerVelZ * playerVelZ);
        if (totalVel > 2)
        {
            //I am SURE you are teleporting!
            playerVelX = 0;
            playerVelZ = 0;
            totalVel = 0;
        }

        BlockPos playerPos = player.getPosition();
        int posX = playerPos.getX();
        int posY = playerPos.getY();
        int posZ = playerPos.getZ();

        double offsetPosX = posX + expansionFactor * playerVelX;
        double offsetPosZ = posZ + expansionFactor * playerVelZ;
        double avgX = (posX + offsetPosX) / 2;
        double avgZ = (posZ + offsetPosZ) / 2;

        double C = 2 * (range + expansionFactor * totalVel) + 1;
        int truncC = (int) C;

        //TODO: Make this for-loop better.
        for (int ix = -truncC; ix <= truncC; ix++)
        {
            for (int iz = -truncC; iz <= truncC; iz++)
            {
                if (computeEllipse(ix + avgX, iz + avgZ, posX, posZ, offsetPosX, offsetPosZ) > C)
                {
                    continue;
                }

                BlockPos blockPos = new BlockPos(ix + posX, posY + verticalOffset, iz + posZ);

                if (world.isAirBlock(blockPos))
                    world.setBlockState(blockPos, RegistrarBloodMagicBlocks.PHANTOM.getDefaultState());
            }
        }

        prevPositionMap.put(player, Pair.of(player.posX, player.posZ));
    }

    public static double computeEllipse(double x, double z, double focusX1, double focusZ1, double focusX2, double focusZ2)
    {
        return Math.sqrt((x - focusX1) * (x - focusX1) + (z - focusZ1) * (z - focusZ1)) + Math.sqrt((x - focusX2) * (x - focusX2) + (z - focusZ2) * (z - focusZ2));
    }
}
