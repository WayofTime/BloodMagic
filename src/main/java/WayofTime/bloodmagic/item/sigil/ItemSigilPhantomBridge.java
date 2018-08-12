package WayofTime.bloodmagic.item.sigil;

import WayofTime.bloodmagic.core.RegistrarBloodMagicBlocks;
import WayofTime.bloodmagic.util.Constants;
import WayofTime.bloodmagic.util.helper.NBTHelper;
import WayofTime.bloodmagic.util.helper.PlayerHelper;
import com.google.common.base.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemSigilPhantomBridge extends ItemSigilToggleableBase {
    public static final Predicate<Entity> IS_PHANTOM_ACTIVE = (Entity entity) -> entity instanceof EntityPlayer && isPhantomActive((EntityPlayer) entity);

    public static boolean isPhantomActive(EntityPlayer entity) {
        for (int i = 0; i < entity.inventory.getSizeInventory(); i++) {
            ItemStack stack = entity.inventory.getStackInSlot(i);
            if (stack.getItem() instanceof ItemSigilPhantomBridge)
                return NBTHelper.checkNBT(stack).getTagCompound().getBoolean(Constants.NBT.ACTIVATED);
            if (stack.getItem() instanceof ItemSigilHolding){
                List<ItemStack> inv = ItemSigilHolding.getInternalInventory(stack);
                for (int j = 0; i < ItemSigilHolding.inventorySize; i++) {
                    ItemStack invStack = inv.get(i);
                    if (invStack.getItem() instanceof ItemSigilPhantomBridge)
                        return NBTHelper.checkNBT(invStack).getTagCompound().getBoolean(Constants.NBT.ACTIVATED);
                }
            }

        }
        return false;
    }

    public static final String[] circle7x7 = new String[]{
            "  XXX  ",
            " XXXXX ",
            "XXXXXXX",
            "XXXXXXX",
            "XXXXXXX",
            " XXXXX ",
            "  XXX  "
    };

    public static final String[] circle9x9 = new String[]{
            "  XXXXX  ",
            " XXXXXXX ",
            "XXXXXXXXX",
            "XXXXXXXXX",
            "XXXXXXXXX",
            "XXXXXXXXX",
            "XXXXXXXXX",
            " XXXXXXX ",
            "  XXXXX  "
    };

    //imagine you're looking into positive Z
    public static final String[] diagXZ = new String[] {
            "XXX   ",           // -----------------
            "XXXX  ",           // Template Guide
            "XXXXX ",           // -----------------
            " XXXXX",// ^       // You stand in the bottom right corner, 1 block right, 1 block below the bottom right X
            "  XXXX",// |       // inverted: flips it so you are in top left corner
            "   XXX" // Z       // XnZ: mirrors the template on the X axis
            // <--X
    };

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
            if (-0.3 < playerVelZ && playerVelZ < 0.3) {
                if (playerVelX > 0.3) {
                    if (playerVelX > 1) {
                        rectangleTemplatePosXLong(posX, posY, posZ, verticalOffset, world);
                    }
                    rectangleTemplatePosX(posX, posY, posZ, verticalOffset, world);
                }
                if (playerVelX < -0.3) {
                    if (playerVelX < -1) {
                        rectangleTemplateNegXLong(posX, posY, posZ, verticalOffset, world);
                    }
                    rectangleTemplateNegX(posX, posY, posZ, verticalOffset, world);
                }
                //walking fairly quickly on Z-axis
            } else if (-0.3 < playerVelX && playerVelX < 0.3) {
                if (playerVelZ > 0.3) {
                    if (playerVelZ > 1) {
                        rectangleTemplatePosZLong(posX, posY, posZ, verticalOffset, world);
                    }
                    rectangleTemplatePosZ(posX, posY, posZ, verticalOffset, world);
                }
                if (playerVelZ < -0.3) {
                    if (playerVelZ < -1) {
                        rectangleTemplateNegZLong(posX, posY, posZ, verticalOffset, world);
                    }
                    rectangleTemplateNegZ(posX, posY, posZ, verticalOffset, world);
                }
            }
            else //diagonal movement
                if (playerVelX > 0.2) {
                    if (playerVelZ > 0.2) {
                        templateReaderDiag(posX, posY, posZ, verticalOffset, world, 1, 1, diagXZ, false, false);
                    }else if (playerVelZ < -0.2) {
                        templateReaderDiag(posX, posY, posZ, verticalOffset, world, 1, -1, diagXZ, false, true);
                    }
                } else if (playerVelX < -0.2) {
                    if (playerVelZ > 0.2) {
                        templateReaderDiag(posX, posY, posZ, verticalOffset, world, -1, 1, diagXZ, true,true);
                    } else if (playerVelZ < -0.2) {
                        templateReaderDiag(posX, posY, posZ, verticalOffset, world,-1,-1,diagXZ, true,false);
                    }
                }
            else
                circleTemplate9x9(posX, posY, posZ, verticalOffset, world);

        prevPositionMap.put(player, Pair.of(player.posX, player.posZ));
    }

    private static void circleTemplate9x9(int posX, int posY, int posZ, int verticalOffset, World world) {
        int x = -4;
        int z = -4;
        templateReader(posX, posY, posZ, verticalOffset, world, z, x, circle9x9);
    }


    private static void circleTemplate7x7(int posX, int posY, int posZ, int verticalOffset, World world) {
        int x = -3;
        int z = -3;
        templateReader(posX, posY, posZ, verticalOffset, world, z, x, circle7x7);
    }

    private static void rectangleTemplatePosX(int posX, int posY, int posZ, int verticalOffset, World world) {
        rectangleBridge(posX, posY, posZ, verticalOffset, world, -2, 2, 2, 6);
    }

    private static void rectangleTemplatePosXLong(int posX, int posY, int posZ, int verticalOffset, World world) {
        rectangleBridge(posX, posY, posZ, verticalOffset, world, -1, 1, 7, 9);
    }

    private static void rectangleTemplateNegX(int posX, int posY, int posZ, int verticalOffset, World world) {
        rectangleBridge(posX, posY, posZ, verticalOffset, world, -2, 2, -6, -2);
    }

    private static void rectangleTemplateNegXLong(int posX, int posY, int posZ, int verticalOffset, World world) {
        rectangleBridge(posX, posY, posZ, verticalOffset, world, -1, 1, -9, -7);
    }

    private static void rectangleTemplatePosZ(int posX, int posY, int posZ, int verticalOffset, World world) {
        rectangleBridge(posX, posY, posZ, verticalOffset, world, 2, 6, -2, 2);
    }

    private static void rectangleTemplatePosZLong(int posX, int posY, int posZ, int verticalOffset, World world) {
        rectangleBridge(posX, posY, posZ, verticalOffset, world, 7, 9, -1, 1);
    }

    private static void rectangleTemplateNegZ(int posX, int posY, int posZ, int verticalOffset, World world) {
        rectangleBridge(posX, posY, posZ, verticalOffset, world, -6, -2, -2, 2);
    }

    private static void rectangleTemplateNegZLong(int posX, int posY, int posZ, int verticalOffset, World world) {
        rectangleBridge(posX, posY, posZ, verticalOffset, world, -9, -7, -1, 1);
    }

    private static void rectangleBridge(int posX, int posY, int posZ, int verticalOffset, World world, int startZ, int endZ, int startX, int endX) {
        for (int Z = startZ; Z <= endZ; Z++) {
            for (int X = startX; X <= endX; X++) {
                BlockPos blockPos = new BlockPos(posX + X, posY + verticalOffset, posZ + Z);

                if (world.isAirBlock(blockPos))
                    world.setBlockState(blockPos, RegistrarBloodMagicBlocks.PHANTOM.getDefaultState());
            }
        }
    }

    private static void templateReader(int posX, int posY, int posZ, int verticalOffset, World world, int offsetZ, int offsetX, String[] template) {
        for (int i = 0; i < template.length; i++) {
            for (int j = 0; j < template[i].length(); j++) {
                if(template[i].charAt(j) == 'X') {
                    BlockPos blockPos = new BlockPos(posX + offsetX + i , posY + verticalOffset, posZ + offsetZ + j);

                    if (world.isAirBlock(blockPos))
                        world.setBlockState(blockPos, RegistrarBloodMagicBlocks.PHANTOM.getDefaultState());
                }
            }

        }
    }
    private static void templateReaderDiag(int posX, int posY, int posZ, int verticalOffset, World world, int offsetZ, int offsetX, String[] template, boolean inverted, boolean XnZ) {
        int i = 0;
        for (String a: template) {
            if(inverted) i--;
            else i++;
            int j = 0;
            for (char b: a.toCharArray()) {
                if(inverted && !XnZ || XnZ && !inverted) j--;
                else j++;
                if(b == 'X') {
                    BlockPos blockPos = new BlockPos(posX + offsetX + i, posY + verticalOffset, posZ + offsetZ + j);

                    if (world.isAirBlock(blockPos))
                        world.setBlockState(blockPos, RegistrarBloodMagicBlocks.PHANTOM.getDefaultState());
                }
            }

        }
    }

}
