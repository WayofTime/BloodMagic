package WayofTime.bloodmagic.tile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.world.WorldServer;
import WayofTime.bloodmagic.api.incense.EnumTranquilityType;
import WayofTime.bloodmagic.api.incense.IIncensePath;
import WayofTime.bloodmagic.api.incense.IncenseTranquilityRegistry;
import WayofTime.bloodmagic.api.incense.TranquilityStack;
import WayofTime.bloodmagic.api.ritual.AreaDescriptor;
import WayofTime.bloodmagic.api.util.helper.PlayerSacrificeHelper;
import WayofTime.bloodmagic.incense.IncenseAltarHandler;

public class TileIncenseAltar extends TileInventory implements ITickable
{
    public AreaDescriptor incenseArea = new AreaDescriptor.Rectangle(new BlockPos(-5, -5, -5), 11);
    public static int maxCheckRange = 5;
    public Map<EnumTranquilityType, Double> tranquilityMap = new HashMap<EnumTranquilityType, Double>();

    public double incenseAddition = 0; //Self-sacrifice is multiplied by 1 plus this value.
    public double tranquility = 0;
    public int roadDistance = 0; //Number of road blocks laid down

    public TileIncenseAltar()
    {
        super(1, "incenseAltar");
    }

    @Override
    public void update()
    {
        AxisAlignedBB aabb = incenseArea.getAABB(getPos());
        List<EntityPlayer> playerList = worldObj.getEntitiesWithinAABB(EntityPlayer.class, aabb);
        if (playerList.isEmpty())
        {
            return;
        }

        if (worldObj.getTotalWorldTime() % 100 == 0)
        {
            recheckConstruction();
        }

        boolean hasPerformed = false;

        for (EntityPlayer player : playerList)
        {
            if (PlayerSacrificeHelper.incrementIncense(player, 0, incenseAddition, incenseAddition / 100))
            {
                hasPerformed = true;
            }
        }

        if (hasPerformed)
        {
            if (worldObj.rand.nextInt(4) == 0 && worldObj instanceof WorldServer)
            {
                WorldServer server = (WorldServer) worldObj;
                server.spawnParticle(EnumParticleTypes.FLAME, pos.getX() + 0.5, pos.getY() + 1.2, pos.getZ() + 0.5, 1, 0.02, 0.03, 0.02, 0, new int[0]);
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);
        tranquility = tag.getDouble("tranquility");
        incenseAddition = tag.getDouble("incenseAddition");
    }

    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
        super.writeToNBT(tag);
        tag.setDouble("tranquility", tranquility);
        tag.setDouble("incenseAddition", incenseAddition);
    }

    public void recheckConstruction()
    {
        //TODO: Check the physical construction of the incense altar to determine the maximum length.
        int maxLength = 11; //Max length of the path. The path starts two blocks away from the center block.
        int yOffset = 0;

        Map<EnumTranquilityType, Double> tranquilityMap = new HashMap<EnumTranquilityType, Double>();

        for (int currentDistance = 2; currentDistance < currentDistance + maxLength; currentDistance++)
        {
            boolean canFormRoad = false;

            for (int i = -maxCheckRange + yOffset; i <= maxCheckRange + yOffset; i++)
            {
                BlockPos verticalPos = pos.add(0, i, 0);

                canFormRoad = true;
                level: for (EnumFacing horizontalFacing : EnumFacing.HORIZONTALS)
                {
                    BlockPos facingOffsetPos = verticalPos.offset(horizontalFacing, currentDistance);
                    for (int j = -1; j <= 1; j++)
                    {
                        BlockPos offsetPos = facingOffsetPos.offset(horizontalFacing.rotateY(), j);
                        IBlockState state = worldObj.getBlockState(offsetPos);
                        Block block = state.getBlock();
                        if (!(block instanceof IIncensePath && ((IIncensePath) block).getLevelOfPath(worldObj, offsetPos, state) >= currentDistance - 2))
                        {
                            canFormRoad = false;
                            break level;
                        }
                    }
                }

                if (canFormRoad)
                {
                    yOffset = i;
                    break;
                }
            }

            if (canFormRoad)
            {
                for (int i = -currentDistance; i <= currentDistance; i++)
                {
                    for (int j = -currentDistance; j <= currentDistance; j++)
                    {
                        if (Math.abs(i) != currentDistance && Math.abs(j) != currentDistance)
                        {
                            break; //TODO: Can make this just set j to currentDistance to speed it up.
                        }

                        for (int y = 0 + yOffset; y <= 2 + yOffset; y++)
                        {
                            BlockPos offsetPos = pos.add(i, y, j);
                            IBlockState state = worldObj.getBlockState(offsetPos);
                            Block block = state.getBlock();
                            TranquilityStack stack = IncenseTranquilityRegistry.getTranquilityOfBlock(worldObj, offsetPos, block, state);
                            if (stack != null)
                            {
                                if (!tranquilityMap.containsKey(stack.type))
                                {
                                    tranquilityMap.put(stack.type, stack.value);
                                } else
                                {
                                    tranquilityMap.put(stack.type, tranquilityMap.get(stack.type) + stack.value);
                                }
                            }
                        }
                    }
                }
            } else
            {
                roadDistance = currentDistance - 2;
                break;
            }
        }

        this.tranquilityMap = tranquilityMap;

        double totalTranquility = 0;
        for (Entry<EnumTranquilityType, Double> entry : tranquilityMap.entrySet())
        {
            totalTranquility += entry.getValue();
        }

        if (totalTranquility < 0)
        {
            return;
        }

        double appliedTranquility = 0;
        for (Entry<EnumTranquilityType, Double> entry : tranquilityMap.entrySet())
        {
            appliedTranquility += Math.sqrt(entry.getValue());
        }

        double bonus = IncenseAltarHandler.getIncenseBonusFromComponents(worldObj, pos, appliedTranquility, roadDistance);
        incenseAddition = bonus;
        this.tranquility = appliedTranquility;
    }
}
