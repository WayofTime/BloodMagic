package WayofTime.bloodmagic.ritual;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.ritual.data.*;
import WayofTime.bloodmagic.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;

import java.util.ArrayList;
import java.util.Iterator;

public class RitualEllipsoid extends Ritual
{
//    public static final String FELLING_RANGE = "fellingRange";
    public static final String CHEST_RANGE = "chest";

    private boolean cached = false;
    private BlockPos currentPos; //Offset

    public RitualEllipsoid()
    {
        super("ritualEllipsoid", 0, 20000, "ritual." + BloodMagic.MODID + ".ellipseRitual");
//        addBlockRange(FELLING_RANGE, new AreaDescriptor.Rectangle(new BlockPos(-10, -3, -10), new BlockPos(11, 27, 11)));
        addBlockRange(CHEST_RANGE, new AreaDescriptor.Rectangle(new BlockPos(0, 1, 0), 1));

//        setMaximumVolumeAndDistanceOfRange(FELLING_RANGE, 14000, 15, 30);
        setMaximumVolumeAndDistanceOfRange(CHEST_RANGE, 1, 3, 3);
    }

    @Override
    public void performRitual(IMasterRitualStone masterRitualStone)
    {
        World world = masterRitualStone.getWorldObj();
        int currentEssence = masterRitualStone.getOwnerNetwork().getCurrentEssence();

        BlockPos masterPos = masterRitualStone.getBlockPos();
        AreaDescriptor chestRange = getBlockRange(CHEST_RANGE);
        TileEntity tileInventory = world.getTileEntity(chestRange.getContainedPositions(masterPos).get(0));

        if (currentEssence < getRefreshCost())
        {
            masterRitualStone.getOwnerNetwork().causeNausea();
            return;
        }

        if (tileInventory != null)
        {
            IItemHandler inventory = Utils.getInventory(tileInventory, EnumFacing.DOWN);
            int numSlots = inventory.getSlots();
            if (numSlots >= 3)
            {
                ItemStack xStack = inventory.getStackInSlot(0);
                ItemStack yStack = inventory.getStackInSlot(1);
                ItemStack zStack = inventory.getStackInSlot(2);

                if (xStack.isEmpty() || yStack.isEmpty() || zStack.isEmpty())
                {
                    return;
                }

                int xR = xStack.getCount();
                int yR = yStack.getCount();
                int zR = zStack.getCount();

                int j = -yR;
                int i = -xR;
                int k = -zR;

                if (currentPos != null)
                {
                    j = currentPos.getY();
                    i = Math.min(xR, Math.max(-xR, currentPos.getX()));
                    k = Math.min(zR, Math.max(-zR, currentPos.getZ()));
                }

                while (j <= yR)
                {
                    while (i <= xR)
                    {
                        while (k <= zR)
                        {
                            if (checkIfEllipsoidShell(xR, yR, zR, i, j, k))
                            {
                                BlockPos newPos = masterPos.add(i, j, k);

                                if (world.isAirBlock(newPos))
                                {
                                    if (j > 0)
                                    {
                                        world.setBlockState(newPos, Blocks.GLASS.getDefaultState());
                                    } else
                                    {
                                        world.setBlockState(newPos, Blocks.STONE.getDefaultState());
                                    }
                                    k++;
                                    this.currentPos = new BlockPos(i, j, k);
                                    return;
                                }
                            }
                            k++;
                        }
                        i++;
                        k = -zR;
                    }
                    j++;
                    i = -xR;
                    this.currentPos = new BlockPos(i, j, k);
                    return;
                }

                j = -yR;
                this.currentPos = new BlockPos(i, j, k);
                return;

            }
        }
    }

    public boolean checkIfEllipsoidShell(int xR, int yR, int zR, int xOff, int yOff, int zOff)
    {
        //Checking shell in the x-direction
        if (!checkIfEllipsoid(xR, yR, zR, xOff, yOff, zOff))
        {
            return false;
        }

        return !((checkIfEllipsoid(xR, yR, zR, xOff + 1, yOff, zOff) && checkIfEllipsoid(xR, yR, zR, xOff - 1, yOff, zOff)) && (checkIfEllipsoid(xR, yR, zR, xOff, yOff + 1, zOff) && checkIfEllipsoid(xR, yR, zR, xOff, yOff - 1, zOff)) && (checkIfEllipsoid(xR, yR, zR, xOff, yOff, zOff + 1) && checkIfEllipsoid(xR, yR, zR, xOff, yOff, zOff - 1)));
//        if (xOff * xOff + yOff * yOff + zOff * zOff >= (xR - 0.5) * (xR - 0.5) && xOff * xOff + yOff * yOff + zOff * zOff <= (xR + 0.5) * (xR + 0.5))
//        if (checkIfEllipsoid(xR, yR, zR, xOff, yOff, zOff))
//        {
//            if (xOff * xOff / ((xR - 0.5) * (xR - 0.5)) + yOff * yOff / ((yR - 0.5) * (yR - 0.5)) >= 1 - zOff * zOff / ((zR + possOffset) * (zR + possOffset)))
//            {
//                return true;
//            }
//
//            if (xOff * xOff / ((xR - 0.5) * (xR - 0.5)) + zOff * zOff / ((zR - 0.5) * (zR - 0.5)) >= 1 - yOff * yOff / ((yR + possOffset) * (yR + possOffset)))
//            {
//                return true;
//            }
//
//            if (zOff * zOff / ((zR - 0.5) * (zR - 0.5)) + yOff * yOff / ((yR - 0.5) * (yR - 0.5)) >= 1 - xOff * xOff / ((xR + possOffset) * (xR + possOffset)))
//            {
//                return true;
//            }
//        }
//        return false;
    }

    public boolean checkIfEllipsoid(float xR, float yR, float zR, float xOff, float yOff, float zOff)
    {
        float possOffset = 0.5f;
        return xOff * xOff / ((xR + possOffset) * (xR + possOffset)) + yOff * yOff / ((yR + possOffset) * (yR + possOffset)) + zOff * zOff / ((zR + possOffset) * (zR + possOffset)) <= 1;
    }

    @Override
    public int getRefreshCost()
    {
        return 0;
    }

    @Override
    public int getRefreshTime()
    {
        return 1;
    }

//    @Override
//    public void readFromNBT(NBTTagCompound tag)
//    {
//        super.readFromNBT(tag);
//        tag
//    }

    @Override
    public ArrayList<RitualComponent> getComponents()
    {
        ArrayList<RitualComponent> components = new ArrayList<RitualComponent>();

        addCornerRunes(components, 1, 0, EnumRuneType.WATER);
        addCornerRunes(components, 1, 1, EnumRuneType.WATER);

        return components;
    }

    @Override
    public Ritual getNewCopy()
    {
        return new RitualEllipsoid();
    }
}
