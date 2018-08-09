package WayofTime.bloodmagic.item.sigil;

import WayofTime.bloodmagic.core.RegistrarBloodMagicBlocks;
import WayofTime.bloodmagic.util.helper.PlayerHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.Map;

public class ItemSigilPhantomBridge extends ItemSigilToggleableBase {
    private static final int RANGE = 3; //this affects template naming scheme

    private Map<EntityPlayer, Pair<Double, Double>> prevPositionMap = new HashMap<>();

    public ItemSigilPhantomBridge() {
        super("phantom_bridge", 100);
    }

    @Override
    public void onSigilUpdate(ItemStack stack, World world, EntityPlayer player, int itemSlot, boolean isSelected) {
        if (PlayerHelper.isFakePlayer(player))
            return;

        if (!prevPositionMap.containsKey(player)) {
            prevPositionMap.put(player, Pair.of(player.posX, player.posZ));
        }

        if ((!player.onGround && !player.isRiding()) && !player.isSneaking()) {
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
        //Moves more than totalVel^2 blocks in any direction within a tick
        if (totalVel > 2) {
            //I am SURE you are teleporting!
            playerVelX = 0;
            playerVelZ = 0;
            totalVel = 0;
        }

        BlockPos playerPos = player.getPosition();
        int posX = playerPos.getX();
        int posY = playerPos.getY();
        int posZ = playerPos.getZ();

        //Standing still, sneaking or walking with framerate drops
        if (0 <= totalVel && totalVel < 0.1) {
            circleTemplate7x7(posX, posY, posZ, verticalOffset, world);
        } else //anything between the first case and being slightly faster than walking
            //walking fairly quickly on X-axis
            if (-0.4 < playerVelZ && playerVelZ < 0.4) {
                if (playerVelX > 0.4) {
                    if (playerVelX > 1) {
                        System.out.println("Using Long X template!");
                        rectangleTemplatePosXLong(posX, posY, posZ, verticalOffset, world);
                    }
                    rectangleTemplatePosX(posX, posY, posZ, verticalOffset, world);
                }
                if (playerVelX < -0.4) {
                    if (playerVelX < -1) {
                        System.out.println("Using Long -X template!");
                        rectangleTemplateNegXLong(posX, posY, posZ, verticalOffset, world);
                    }
                    rectangleTemplateNegX(posX, posY, posZ, verticalOffset, world);
                }
                //walking fairly quickly on Z-axis
            } else if (-0.4 < playerVelX && playerVelX < 0.4) {
                if (playerVelZ > 0.4) {
                    if (playerVelZ > 1) {
                        System.out.println("Using Long Z template!");
                        rectangleTemplatePosZLong(posX, posY, posZ, verticalOffset, world);
                    }
                    rectangleTemplatePosZ(posX, posY, posZ, verticalOffset, world);
                }
                if (playerVelZ < -0.4) {
                    if (playerVelZ < -1) {
                        System.out.println("Using Long -Z template!");
                        rectangleTemplateNegZLong(posX, posY, posZ, verticalOffset, world);
                    }
                    rectangleTemplateNegZ(posX, posY, posZ, verticalOffset, world);
                }
            } //doesn't work yet, variables for template need to be switched. appox time needed: 30 mins - 1 hour.
            /* else //diagonal movement
                if (playerVelX > 0.1) {
                    if (playerVelZ > 0.1) {
                        diagTemplatePoxXPosZ(posX, posY, posZ, verticalOffset, world);
                    }else if (playerVelZ < -0.1) {
                        diagTemplatePoxXNegZ(posX, posY, posZ, verticalOffset, world);
                    }
                } else if (playerVelX < -0.1) {
                    if (playerVelZ > 0.1) {
                        diagTemplateNegXPosZ(posX, posY, posZ, verticalOffset, world);
                    } else if (playerVelZ < -0.1) {
                        diagTemplateNegXNegZ(posX, posY, posZ, verticalOffset, world);
                    }
                }*/
            else
                circleTemplate9x9(posX,posY,posZ,verticalOffset,world);

        prevPositionMap.put(player, Pair.of(player.posX, player.posZ));
    }

    private static void circleTemplate9x9(int posX, int posY, int posZ, int verticalOffset, World world) {
        int counter = 0;
        for (int radius = -(RANGE + 1); radius <= RANGE + 1; radius++) {
            for (int radius2 = -(RANGE + 1); radius2 <= RANGE + 1; radius2++) {
                counter++;
                //this is a radius*2+1 * radius2*2+1 rectangle, beginning in -x -z
                //cutting corners
                switch (counter) {
                    //-x -z corner
                    case 1:
                    case 2:
                    case 3:
                    case 10:
                    case 19:
                        //-x +z
                    case 7:
                    case 8:
                    case 9:
                    case 18:
                    case 27:
                        //+x -z
                    case 55:
                    case 64:
                    case 73:
                    case 74:
                    case 75:
                        //+x +z
                    case 63:
                    case 72:
                    case 79:
                    case 80:
                    case 81:
                        continue;
                    default:
                        BlockPos blockPos = new BlockPos(posX + radius, posY + verticalOffset, posZ + radius2);

                        if (world.isAirBlock(blockPos))
                            world.setBlockState(blockPos, RegistrarBloodMagicBlocks.PHANTOM.getDefaultState());
                }

            }
        }
    }

    private static void circleTemplate7x7(int posX, int posY, int posZ, int verticalOffset, World world) {
        int counter = 0;
        for (int radius = -RANGE; radius <= RANGE; radius++) {
            for (int radius2 = -RANGE; radius2 <= RANGE; radius2++) {
                counter++;
                //this is a radius*2+1 * radius2*2+1 rectangle, beginning in -x -z
                //cutting corners
                switch (counter) {
                    //-x -z corner
                    case 1:
                    case 2:
                    case 8:
                        //-x +z
                    case 6:
                    case 7:
                    case 14:
                        //+x -z
                    case 36:
                    case 43:
                    case 44:
                        //+x +z
                    case 42:
                    case 48:
                    case 49:
                        continue;
                    default:
                        BlockPos blockPos = new BlockPos(posX + radius, posY + verticalOffset, posZ + radius2);

                        if (world.isAirBlock(blockPos))
                            world.setBlockState(blockPos, RegistrarBloodMagicBlocks.PHANTOM.getDefaultState());
                }

            }
        }
    }

    private static void rectangleTemplatePosX(int posX, int posY, int posZ, int verticalOffset, World world) {
        templateCalculationHelper(posX, posY, posZ, verticalOffset, world, RANGE, 2 * RANGE, -2, 2);
    }

    private static void rectangleTemplatePosXLong(int posX, int posY, int posZ, int verticalOffset, World world) {
        templateCalculationHelper(posX, posY, posZ, verticalOffset, world, RANGE, 3 * RANGE, -1, 1);
    }

    private static void rectangleTemplateNegX(int posX, int posY, int posZ, int verticalOffset, World world) {
        templateCalculationHelper(posX, posY, posZ, verticalOffset, world, -2 * RANGE, -RANGE, -2, 2);
    }

    private static void rectangleTemplateNegXLong(int posX, int posY, int posZ, int verticalOffset, World world) {
        templateCalculationHelper(posX, posY, posZ, verticalOffset, world, -3 * RANGE, -RANGE, -1, 1);
    }

    private static void rectangleTemplatePosZ(int posX, int posY, int posZ, int verticalOffset, World world) {
        templateCalculationHelper(posX, posY, posZ, verticalOffset, world, -2, 2, RANGE, 2 * RANGE);
    }

    private static void rectangleTemplatePosZLong(int posX, int posY, int posZ, int verticalOffset, World world) {
        templateCalculationHelper(posX, posY, posZ, verticalOffset, world, -1, 1, RANGE, 3 * RANGE);
    }

    private static void rectangleTemplateNegZ(int posX, int posY, int posZ, int verticalOffset, World world) {
        templateCalculationHelper(posX, posY, posZ, verticalOffset, world, -2, 2, -2 * RANGE, -RANGE);
    }

    private static void rectangleTemplateNegZLong(int posX, int posY, int posZ, int verticalOffset, World world) {
        templateCalculationHelper(posX, posY, posZ, verticalOffset, world, -1, 1, -3 * RANGE, -RANGE);
    }

    private static void diagTemplatePoxXPosZ(int posX, int posY, int posZ, int verticalOffset, World world) {
        int counter = 0;
        for (int radius = 1; radius <= 2 * RANGE; radius++) {
            diagTemplateHelperPosZ(posX, posY, posZ, verticalOffset, world, counter, radius);
        }
    }

    private static void diagTemplatePoxXNegZ(int posX, int posY, int posZ, int verticalOffset, World world) {
        int counter = 0;
        for (int radius = 1; radius <= 2 * RANGE; radius++) {
            for (int radius2 = -1; radius2 >= -2 * RANGE; radius2--) {
                counter++;
                //carving a path
                diagTemplateHelperNegZ(posX, posY, posZ, verticalOffset, world, counter, radius, radius2);

            }
        }
    }

    private static void diagTemplateNegXPosZ(int posX, int posY, int posZ, int verticalOffset, World world) {
        int counter = 0;
        for (int radius = -1; radius >= -2 * RANGE; radius--) {
            diagTemplateHelperPosZ(posX, posY, posZ, verticalOffset, world, counter, radius);
        }
    }

    private static void diagTemplateNegXNegZ(int posX, int posY, int posZ, int verticalOffset, World world) {
        int counter = 0;
        for (int radius = -1; radius >= -2 * RANGE; radius--) {
            for (int radius2 = -1; radius2 >= -2 * RANGE; radius2--) {
                counter++;
                //carving a path
                diagTemplateHelperNegZ(posX, posY, posZ, verticalOffset, world, counter, radius, radius2);

            }
        }
    }

    private static void templateCalculationHelper(int posX, int posY, int posZ, int verticalOffset, World world, int i, int i2, int i3, int i4) {
        for (int radius = i; radius <= i2; radius++) {
            for (int radius2 = i3; radius2 <= i4; radius2++) {
                BlockPos blockPos = new BlockPos(posX + radius, posY + verticalOffset, posZ + radius2);

                if (world.isAirBlock(blockPos))
                    world.setBlockState(blockPos, RegistrarBloodMagicBlocks.PHANTOM.getDefaultState());
            }
        }
    }

    private static void diagTemplateHelperPosZ(int posX, int posY, int posZ, int verticalOffset, World world, int counter, int radius) {
        for (int radius2 = 1; radius2 <= 2 * RANGE; radius2++) {
            counter++;
            //carving a path
            diagTemplateHelperNegZ(posX, posY, posZ, verticalOffset, world, counter, radius, radius2);

        }
    }

    private static void diagTemplateHelperNegZ(int posX, int posY, int posZ, int verticalOffset, World world, int counter, int radius, int radius2) {
        switch (counter) {
            // want to edit this? draw a rectangle radius*radius2 on a squared sheet of paper. Every Square is a block.
            // start numbering them from bottom left to right (every new line starts on the left)
            // the player is to the diagonal bottom left (one down, one left) from the first square.
            // every case listed is a square that will not be generated.
            case 4:
            case 5:
            case 10:
            case 16:
            case 21:
            case 22:
                return;
            default:
                BlockPos blockPos = new BlockPos(posX + radius, posY + verticalOffset, posZ + radius2);

                if (world.isAirBlock(blockPos))
                    world.setBlockState(blockPos, RegistrarBloodMagicBlocks.PHANTOM.getDefaultState());
        }
    }

}
