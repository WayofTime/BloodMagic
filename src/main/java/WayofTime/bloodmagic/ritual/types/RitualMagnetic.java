package WayofTime.bloodmagic.ritual.types;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.ritual.*;
import WayofTime.bloodmagic.util.Utils;
import com.mojang.authlib.GameProfile;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;


import java.util.function.Consumer;

@RitualRegister("magnetism")
public class RitualMagnetic extends Ritual {
    public static final String PLACEMENT_RANGE = "placementRange";
    //    public static final String SEARCH_RANGE = "searchRange";
    public BlockPos lastPos; // An offset
    private FakePlayer fakePlayer;

    public RitualMagnetic() {
        super("ritualMagnetic", 0, 5000, "ritual." + BloodMagic.MODID + ".magneticRitual");
        addBlockRange(PLACEMENT_RANGE, new AreaDescriptor.Rectangle(new BlockPos(-1, 1, -1), 3));
        setMaximumVolumeAndDistanceOfRange(PLACEMENT_RANGE, 50, 4, 4);
    }

    @Override
    public void performRitual(IMasterRitualStone masterRitualStone) {
        World world = masterRitualStone.getWorldObj();
        Vec3d MRSpos = new Vec3d(masterRitualStone.getBlockPos());
        int currentEssence = masterRitualStone.getOwnerNetwork().getCurrentEssence();

        if (currentEssence < getRefreshCost()) {
            masterRitualStone.getOwnerNetwork().causeNausea();
            return;
        }

        BlockPos pos = masterRitualStone.getBlockPos();

        AreaDescriptor placementRange = masterRitualStone.getBlockRange(PLACEMENT_RANGE);

        BlockPos replacement = pos;
        boolean replace = false;

        for (BlockPos offset : placementRange.getContainedPositions(pos)) {
            if (world.isAirBlock(offset)) {
                replacement = offset;
                replace = true;
                break;
            }
        }

        IBlockState downState = world.getBlockState(pos.down());
        int radius = getRadius(downState.getBlock());

        if (replace) {
            int j = -1;
            int i = -radius;
            int k = -radius;

            if (lastPos != null) {
                j = lastPos.getY();
                i = Math.min(radius, Math.max(-radius, lastPos.getX()));
                k = Math.min(radius, Math.max(-radius, lastPos.getZ()));
            }

            if (j + pos.getY() >= 0) {
                while (i <= radius) {
                    while (k <= radius) {
                        BlockPos newPos = pos.add(i, j, k);
                        Vec3d newPosVector = new Vec3d(newPos);
                        IBlockState state = world.getBlockState(newPos);
                        RayTraceResult fakeRayTrace = world.rayTraceBlocks(MRSpos, newPosVector, false);
                        ItemStack checkStack = state.getBlock().getPickBlock(state, fakeRayTrace, world, newPos, getFakePlayer((WorldServer) world));
                        if (isBlockOre(checkStack)) {
                            Utils.swapLocations(world, newPos, world, replacement);
                            masterRitualStone.getOwnerNetwork().syphon(masterRitualStone.ticket(getRefreshCost()));
                            k++;
                            this.lastPos = new BlockPos(i, j, k);
                            return;
                        } else {
                            k++;
                        }
                    }
                    i++;
                    k = -radius;
                }
                j--;
                i = -radius;
                this.lastPos = new BlockPos(i, j, k);
                return;
            }

            j = -1;
            this.lastPos = new BlockPos(i, j, k);
        }

    }

    public int getRadius(Block block) {
        if (block == Blocks.IRON_BLOCK) {
            return 7;
        }

        if (block == Blocks.GOLD_BLOCK) {
            return 15;
        }

        if (block == Blocks.DIAMOND_BLOCK) {
            return 31;
        }

        return 3;
    }

    @Override
    public int getRefreshTime() {
        return 40;
    }

    @Override
    public int getRefreshCost() {
        return 50;
    }

    @Override
    public void gatherComponents(Consumer<RitualComponent> components) {
        addCornerRunes(components, 1, 0, EnumRuneType.EARTH);
        addParallelRunes(components, 2, 1, EnumRuneType.EARTH);
        addCornerRunes(components, 2, 1, EnumRuneType.AIR);
        addParallelRunes(components, 2, 2, EnumRuneType.FIRE);
    }

    @Override
    public Ritual getNewCopy() {
        return new RitualMagnetic();
    }

    private FakePlayer getFakePlayer(WorldServer world) {
        return fakePlayer == null ? fakePlayer = FakePlayerFactory.get(world, new GameProfile(null, BloodMagic.MODID + "_ritual_magnetic")) : fakePlayer;
    }

    public static boolean isBlockOre(ItemStack stack) {
        if (stack.isEmpty())
            return false;

        for (int id : OreDictionary.getOreIDs(stack)) {
            String oreName = OreDictionary.getOreName(id);
            if (oreName.contains("ore"))
                return true;
        }

        return false;
    }
}
