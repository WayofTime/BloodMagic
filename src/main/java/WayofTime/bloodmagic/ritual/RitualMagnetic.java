package WayofTime.bloodmagic.ritual;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.util.BlockStack;
import WayofTime.bloodmagic.ritual.data.*;
import WayofTime.bloodmagic.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockOre;
import net.minecraft.block.BlockRedstoneOre;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RitualMagnetic extends Ritual {
    public static final String PLACEMENT_RANGE = "placementRange";
    private static final Map<BlockStack, Boolean> oreBlockCache = new HashMap<BlockStack, Boolean>();
    //    public static final String SEARCH_RANGE = "searchRange";
    public BlockPos lastPos; // An offset

    public RitualMagnetic() {
        super("ritualMagnetic", 0, 5000, "ritual." + BloodMagic.MODID + ".magneticRitual");
        addBlockRange(PLACEMENT_RANGE, new AreaDescriptor.Rectangle(new BlockPos(-1, 1, -1), 3));
        setMaximumVolumeAndDistanceOfRange(PLACEMENT_RANGE, 50, 4, 4);
    }

    @Override
    public void performRitual(IMasterRitualStone masterRitualStone) {
        World world = masterRitualStone.getWorldObj();
        int currentEssence = masterRitualStone.getOwnerNetwork().getCurrentEssence();

        if (currentEssence < getRefreshCost()) {
            masterRitualStone.getOwnerNetwork().causeNausea();
            return;
        }

        BlockPos pos = masterRitualStone.getBlockPos();

        AreaDescriptor placementRange = getBlockRange(PLACEMENT_RANGE);

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
        Block downBlock = downState.getBlock();

        int radius = getRadius(downBlock);

        if (replace) {
            int j = -1;
            int i = -radius;
            int k = -radius;

            if (lastPos != null) {
                j = lastPos.getY();
                i = Math.min(radius, Math.max(-radius, lastPos.getX()));
                k = Math.min(radius, Math.max(-radius, lastPos.getZ()));
            }

            while (j + pos.getY() >= 0) {
                while (i <= radius) {
                    while (k <= radius) {
                        BlockPos newPos = pos.add(i, j, k);
                        IBlockState state = world.getBlockState(newPos);
                        Block block = state.getBlock();
                        ItemStack checkStack = block.getItem(world, newPos, state);
//                        int meta = block.getMetaFromState(state);

                        if (isBlockOre(checkStack)) {
                            Utils.swapLocations(world, newPos, world, replacement);
                            masterRitualStone.getOwnerNetwork().syphon(getRefreshCost());
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
            return;
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
    public ArrayList<RitualComponent> getComponents() {
        ArrayList<RitualComponent> components = new ArrayList<RitualComponent>();

        this.addCornerRunes(components, 1, 0, EnumRuneType.EARTH);
        this.addParallelRunes(components, 2, 1, EnumRuneType.EARTH);
        this.addCornerRunes(components, 2, 1, EnumRuneType.AIR);
        this.addParallelRunes(components, 2, 2, EnumRuneType.FIRE);

        return components;
    }

    @Override
    public Ritual getNewCopy() {
        return new RitualMagnetic();
    }

    public static boolean isBlockOre(Block block, int meta) {
        if (block == null)
            return false;

        if (block instanceof BlockOre || block instanceof BlockRedstoneOre)
            return true;

        if (Item.getItemFromBlock(block) == Items.AIR)
            return false;

        BlockStack type = new BlockStack(block, meta);
        Boolean result = oreBlockCache.get(type);
        if (result == null) {
            result = computeIsItemOre(type);
            oreBlockCache.put(type, result);
        }
        return result;
    }

    private static boolean computeIsItemOre(BlockStack type) {
        ItemStack stack = new ItemStack(type.getBlock(), type.getMeta());
        return isBlockOre(stack);
    }

    public static boolean isBlockOre(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }

        for (int id : OreDictionary.getOreIDs(stack)) {
            String oreName = OreDictionary.getOreName(id);
            if (oreName.contains("ore"))
                return true;
        }
        return false;
    }
}
