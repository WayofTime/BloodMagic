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

    public static final String[] CIRCLE7X7 = new String[]{
            "  XXX  ",
            " XXXXX ",
            "XXXXXXX",
            "XXXXXXX",
            "XXXXXXX",
            " XXXXX ",
            "  XXX  "
    };

    public static final String[] CIRCLE9X9 = new String[]{
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
    public static final String[] DIAG = new String[]{
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
        if (totalVel >= 0 && totalVel < 0.3) {
            circleTemplate7x7(posX, posY, posZ, verticalOffset, world);
            //anything between the first case and being slightly faster than walking
            //walking fairly quickly on X-axis
        } else if (playerVelZ > -0.2 && playerVelZ < 0.2) {
            if (playerVelX > 0.4) {
                if (playerVelX > 1) {
                    rectangleBridge(posX, posY, posZ, verticalOffset, world, -1, 1, 7, 9); // long bridge
                }
                rectangleBridge(posX, posY, posZ, verticalOffset, world, -2, 2, 1, 6); // short bridge
            }
            if (playerVelX < -0.4) {
                if (playerVelX < -1) {
                    rectangleBridge(posX, posY, posZ, verticalOffset, world, 7, 9, -1, 1);
                }
                rectangleBridge(posX, posY, posZ, verticalOffset, world, -2, 2, -6, -1);
            }
            //walking fairly quickly on Z-axis
        } else if (playerVelX > -0.2 && playerVelX < 0.2) {
            if (playerVelZ > 0.4) {
                if (playerVelZ > 1) {
                    rectangleBridge(posX, posY, posZ, verticalOffset, world, 2, 6, -2, 2);
                }
                rectangleBridge(posX, posY, posZ, verticalOffset, world, 1, 6, -2, 2);
            }
            if (playerVelZ < -0.4) {
                if (playerVelZ < -1) {
                    rectangleBridge(posX, posY, posZ, verticalOffset, world, -9, -7, -1, 1);
                }
                rectangleBridge(posX, posY, posZ, verticalOffset, world, -6, -1, -2, 2);
            }
        } else if (playerVelX > 0.2) { // diagonal movement
            if (playerVelZ > 0.2) {
                templateReaderCustom(posX, posY, posZ, verticalOffset, world, 1, 1, DIAG, false, false);
            } else if (playerVelZ < -0.2) {
                templateReaderCustom(posX, posY, posZ, verticalOffset, world, 1, -1, DIAG, false, true);
            }
        } else if (playerVelX < -0.2) {
            if (playerVelZ > 0.2) {
                templateReaderCustom(posX, posY, posZ, verticalOffset, world, -1, 1, DIAG, true, true);
            } else if (playerVelZ < -0.2) {
                templateReaderCustom(posX, posY, posZ, verticalOffset, world, -1, -1, DIAG, true, false);
            }
        } else {
            circleTemplate9x9(posX, posY, posZ, verticalOffset, world);
        }

        prevPositionMap.put(player, Pair.of(player.posX, player.posZ));
    }

    private static void circleTemplate9x9(int posX, int posY, int posZ, int verticalOffset, World world) {
        int x = -5;
        int z = -5;
        templateReader(posX, posY, posZ, verticalOffset, world, z, x, CIRCLE9X9);
    }

    private static void circleTemplate7x7(int posX, int posY, int posZ, int verticalOffset, World world) {
        int x = -4;
        int z = -4;
        templateReader(posX, posY, posZ, verticalOffset, world, z, x, CIRCLE7X7);
    }

    private static void rectangleBridge(int posX, int posY, int posZ, int verticalOffset, World world, int startZ, int endZ, int startX, int endX) {
        for (int z = startZ; z <= endZ; z++) {
            for (int x = startX; x <= endX; x++) {
                BlockPos blockPos = new BlockPos(posX + x, posY + verticalOffset, posZ + z);

                if (world.isAirBlock(blockPos))
                    world.setBlockState(blockPos, RegistrarBloodMagicBlocks.PHANTOM.getDefaultState());
            }
        }
    }

    private static void templateReader(int posX, int posY, int posZ, int verticalOffset, World world, int offsetZ, int offsetX, String[] template) {
        templateReaderCustom(posX, posY, posZ, verticalOffset, world, offsetZ, offsetX, template, false, false);
    }

    private static void templateReaderCustom(int posX, int posY, int posZ, int verticalOffset, World world, int offsetZ, int offsetX, String[] template, boolean inverted, boolean XnZ) {
        int x = 0;
        for (String row : template) {
            if (inverted)
                x--;
            else
                x++;
            int z = 0;
            for (char block : row.toCharArray()) {
                if (inverted && !XnZ || XnZ && !inverted)
                    z--;
                else
                    z++;
                if (block == 'X') {
                    BlockPos blockPos = new BlockPos(posX + offsetX + x, posY + verticalOffset, posZ + offsetZ + z);

                    if (world.isAirBlock(blockPos))
                        world.setBlockState(blockPos, RegistrarBloodMagicBlocks.PHANTOM.getDefaultState());
                }
            }
        }
    }

    public static boolean isPhantomActive(EntityPlayer entity) {
        for (int i = 0; i < entity.inventory.getSizeInventory(); i++) {
            ItemStack stack = entity.inventory.getStackInSlot(i);
            if (stack.getItem() instanceof ItemSigilPhantomBridge)
                return NBTHelper.checkNBT(stack).getTagCompound().getBoolean(Constants.NBT.ACTIVATED);
            if (stack.getItem() instanceof ItemSigilHolding) {
                List<ItemStack> inv = ItemSigilHolding.getInternalInventory(stack);
                for (int j = 0; j < ItemSigilHolding.inventorySize; j++) {
                    ItemStack invStack = inv.get(j);
                    if (invStack.getItem() instanceof ItemSigilPhantomBridge)
                        return NBTHelper.checkNBT(invStack).getTagCompound().getBoolean(Constants.NBT.ACTIVATED);
                }
            }

        }
        return false;
    }
}
