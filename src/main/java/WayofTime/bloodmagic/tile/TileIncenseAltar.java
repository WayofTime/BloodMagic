package WayofTime.bloodmagic.tile;

import WayofTime.bloodmagic.ritual.AreaDescriptor;
import WayofTime.bloodmagic.util.helper.PlayerSacrificeHelper;
import WayofTime.bloodmagic.incense.*;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class TileIncenseAltar extends TileInventory implements ITickable {
    public static int maxCheckRange = 5;
    public AreaDescriptor incenseArea = new AreaDescriptor.Rectangle(new BlockPos(-5, -5, -5), 11);
    public Map<EnumTranquilityType, Double> tranquilityMap = new HashMap<>();

    public double incenseAddition = 0; //Self-sacrifice is multiplied by 1 plus this value.
    public double tranquility = 0;
    public int roadDistance = 0; //Number of road blocks laid down

    public TileIncenseAltar() {
        super(1, "incenseAltar");
    }

    @Override
    public void update() {
        AxisAlignedBB aabb = incenseArea.getAABB(getPos());
        List<EntityPlayer> playerList = getWorld().getEntitiesWithinAABB(EntityPlayer.class, aabb);
        if (playerList.isEmpty()) {
            return;
        }

        if (getWorld().getTotalWorldTime() % 100 == 0) {
            recheckConstruction();
        }

        boolean hasPerformed = false;

        for (EntityPlayer player : playerList) {
            if (PlayerSacrificeHelper.incrementIncense(player, 0, incenseAddition, incenseAddition / 100)) {
                hasPerformed = true;
            }
        }

        if (hasPerformed) {
            if (getWorld().rand.nextInt(4) == 0 && getWorld() instanceof WorldServer) {
                WorldServer server = (WorldServer) getWorld();
                server.spawnParticle(EnumParticleTypes.FLAME, pos.getX() + 0.5, pos.getY() + 1.2, pos.getZ() + 0.5, 1, 0.02, 0.03, 0.02, 0);
            }
        }
    }

    @Override
    public void deserialize(NBTTagCompound tag) {
        super.deserialize(tag);
        tranquility = tag.getDouble("tranquility");
        incenseAddition = tag.getDouble("incenseAddition");
    }

    @Override
    public NBTTagCompound serialize(NBTTagCompound tag) {
        super.serialize(tag);
        tag.setDouble("tranquility", tranquility);
        tag.setDouble("incenseAddition", incenseAddition);
        return tag;
    }

    public void recheckConstruction() {
        //TODO: Check the physical construction of the incense altar to determine the maximum length.
        int maxLength = 11; //Max length of the path. The path starts two blocks away from the center block.
        int yOffset = 0;

        Map<EnumTranquilityType, Double> tranquilityMap = new HashMap<>();

        for (int currentDistance = 2; currentDistance < currentDistance + maxLength; currentDistance++) {
            boolean canFormRoad = false;

            for (int i = -maxCheckRange + yOffset; i <= maxCheckRange + yOffset; i++) {
                BlockPos verticalPos = pos.add(0, i, 0);

                canFormRoad = true;
                level:
                for (EnumFacing horizontalFacing : EnumFacing.HORIZONTALS) {
                    BlockPos facingOffsetPos = verticalPos.offset(horizontalFacing, currentDistance);
                    for (int j = -1; j <= 1; j++) {
                        BlockPos offsetPos = facingOffsetPos.offset(horizontalFacing.rotateY(), j);
                        IBlockState state = getWorld().getBlockState(offsetPos);
                        Block block = state.getBlock();
                        if (!(block instanceof IIncensePath && ((IIncensePath) block).getLevelOfPath(getWorld(), offsetPos, state) >= currentDistance - 2)) {
                            canFormRoad = false;
                            break level;
                        }
                    }
                }

                if (canFormRoad) {
                    yOffset = i;
                    break;
                }
            }

            if (canFormRoad) {
                for (int i = -currentDistance; i <= currentDistance; i++) {
                    for (int j = -currentDistance; j <= currentDistance; j++) {
                        if (Math.abs(i) != currentDistance && Math.abs(j) != currentDistance) {
                            continue; //TODO: Can make this just set j to currentDistance to speed it up.
                        }

                        for (int y = yOffset; y <= 2 + yOffset; y++) {
                            BlockPos offsetPos = pos.add(i, y, j);
                            IBlockState state = getWorld().getBlockState(offsetPos);
                            Block block = state.getBlock();
                            TranquilityStack stack = IncenseTranquilityRegistry.getTranquilityOfBlock(getWorld(), offsetPos, block, state);
                            if (stack != null) {
                                if (!tranquilityMap.containsKey(stack.type)) {
                                    tranquilityMap.put(stack.type, stack.value);
                                } else {
                                    tranquilityMap.put(stack.type, tranquilityMap.get(stack.type) + stack.value);
                                }
                            }
                        }
                    }
                }
            } else {
                roadDistance = currentDistance - 2;
                break;
            }
        }

        this.tranquilityMap = tranquilityMap;

        double totalTranquility = 0;
        for (Entry<EnumTranquilityType, Double> entry : tranquilityMap.entrySet()) {
            totalTranquility += entry.getValue();
        }

        if (totalTranquility < 0) {
            return;
        }

        double appliedTranquility = 0;
        for (Entry<EnumTranquilityType, Double> entry : tranquilityMap.entrySet()) {
            appliedTranquility += Math.sqrt(entry.getValue());
        }

        double bonus = IncenseAltarHandler.getIncenseBonusFromComponents(getWorld(), pos, appliedTranquility, roadDistance);
        incenseAddition = bonus;
        this.tranquility = appliedTranquility;
    }
}
