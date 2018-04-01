package WayofTime.bloodmagic.alchemyArray;

import java.util.Random;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import WayofTime.bloodmagic.event.TeleposeEvent;
import WayofTime.bloodmagic.tile.TileAlchemyArray;
import WayofTime.bloodmagic.util.Constants;
import WayofTime.bloodmagic.util.Utils;

public class AlchemyArrayEffectLaputa extends AlchemyArrayEffect
{
    public static final int TELEPOSE_DELAY = 4;

    private BlockPos currentPos = BlockPos.ORIGIN;

    private int radius = -1;
    private int teleportHeightOffset = 5;

    public AlchemyArrayEffectLaputa(String key)
    {
        super(key);
    }

    @Override
    public boolean update(TileEntity tile, int ticksActive)
    {
        if (ticksActive >= 100)
        {
            World world = tile.getWorld();

            if (radius == -1)
            {
                ((TileAlchemyArray) tile).setItemDrop(false);
                radius = getRandomRadius(world.rand);
                teleportHeightOffset = getRandomHeightOffset(world.rand);
                currentPos = new BlockPos(-radius, -radius, -radius);
            }

            BlockPos pos = tile.getPos();
            if (world.isRemote)
            {
                return false;
            }

            int j = -radius;
            int i = -radius;
            int k = -radius;

            if (currentPos != null)
            {
                j = currentPos.getY();
                i = currentPos.getX();
                k = currentPos.getZ();
            }
            int checks = 0;
            int maxChecks = 100;

            while (j <= radius)
            {
                while (i <= radius)
                {
                    while (k <= radius)
                    {
                        if (i == 0 && j == 0 && k == 0)
                        {
                            k++;
                            continue;
                        }

                        checks++;
                        if (checks >= maxChecks)
                        {
                            this.currentPos = new BlockPos(i, j, k);
                            return false;
                        }

                        if (checkIfSphere(radius, i, j, k))
                        {
                            BlockPos newPos = pos.add(i, j, k);
                            BlockPos offsetPos = newPos.up(teleportHeightOffset);

                            TeleposeEvent event = new TeleposeEvent(world, newPos, world, offsetPos);
                            if (Utils.swapLocations(event.initalWorld, event.initialBlockPos, event.finalWorld, event.finalBlockPos) && !MinecraftForge.EVENT_BUS.post(event))
                            {
                                k++;
                                this.currentPos = new BlockPos(i, j, k);

                                return false;
                            }
                        }
                        k++;
                    }
                    i++;
                    k = -radius;
                }
                j++;
                i = -radius;
                this.currentPos = new BlockPos(i, j, k);
                return false;
            }

            return true;
        }

        return false;
    }

    public boolean checkIfSphere(float radius, float xOff, float yOff, float zOff)
    {
        float possOffset = 0.5f;
        return xOff * xOff + yOff * yOff + zOff * zOff <= ((radius + possOffset) * (radius + possOffset));
    }

    public int getRandomRadius(Random rand)
    {
        return rand.nextInt(5) + 4;
    }

    public int getRandomHeightOffset(Random rand)
    {
        return radius * 2 + 1 + rand.nextInt(5);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
        tag.setInteger("radius", radius);
        tag.setInteger("teleportHeightOffset", teleportHeightOffset);
        tag.setInteger(Constants.NBT.X_COORD, currentPos.getX());
        tag.setInteger(Constants.NBT.Y_COORD, currentPos.getY());
        tag.setInteger(Constants.NBT.Z_COORD, currentPos.getZ());
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        radius = tag.getInteger("radius");
        teleportHeightOffset = tag.getInteger("teleportHeightOffset");
        currentPos = new BlockPos(tag.getInteger(Constants.NBT.X_COORD), tag.getInteger(Constants.NBT.Y_COORD), tag.getInteger(Constants.NBT.Z_COORD));
    }

    @Override
    public AlchemyArrayEffect getNewCopy()
    {
        return new AlchemyArrayEffectLaputa(key);
    }
}
