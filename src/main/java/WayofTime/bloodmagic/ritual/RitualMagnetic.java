package WayofTime.bloodmagic.ritual;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.BlockOre;
import net.minecraft.block.BlockRedstoneOre;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import WayofTime.bloodmagic.api.BlockStack;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.network.SoulNetwork;
import WayofTime.bloodmagic.api.ritual.AreaDescriptor;
import WayofTime.bloodmagic.api.ritual.EnumRuneType;
import WayofTime.bloodmagic.api.ritual.IMasterRitualStone;
import WayofTime.bloodmagic.api.ritual.Ritual;
import WayofTime.bloodmagic.api.ritual.RitualComponent;
import WayofTime.bloodmagic.api.util.helper.NetworkHelper;
import WayofTime.bloodmagic.tile.TileTeleposer;

public class RitualMagnetic extends Ritual
{
    private static final Map<BlockStack, Boolean> oreBlockCache = new HashMap<BlockStack, Boolean>();

    public static final String PLACEMENT_RANGE = "placementRange";
//    public static final String SEARCH_RANGE = "searchRange";

    public BlockPos lastPos; // An offset

    public RitualMagnetic()
    {
        super("ritualMagnetic", 0, 5000, "ritual." + Constants.Mod.MODID + ".magneticRitual");
        addBlockRange(PLACEMENT_RANGE, new AreaDescriptor.Rectangle(new BlockPos(-1, 1, -1), 3));
    }

    public static boolean isBlockOre(Block block, int meta)
    {
        if (block == null)
            return false;

        if (block instanceof BlockOre || block instanceof BlockRedstoneOre)
            return true;

        if (Item.getItemFromBlock(block) == null)
            return false;

        BlockStack type = new BlockStack(block, meta);
        Boolean result = oreBlockCache.get(type);
        if (result == null)
        {
            result = computeIsItemOre(type);
            oreBlockCache.put(type, result);
        }
        return result;
    }

    private static boolean computeIsItemOre(BlockStack type)
    {
        ItemStack itemStack = new ItemStack(type.getBlock(), type.getMeta());
        for (int id : OreDictionary.getOreIDs(itemStack))
        {
            String oreName = OreDictionary.getOreName(id);
            if (oreName.contains("ore"))
                return true;
        }
        return false;
    }

    @Override
    public void performRitual(IMasterRitualStone masterRitualStone)
    {
        World world = masterRitualStone.getWorldObj();
        SoulNetwork network = NetworkHelper.getSoulNetwork(masterRitualStone.getOwner());
        int currentEssence = network.getCurrentEssence();

        if (currentEssence < getRefreshCost())
        {
            network.causeNauseaToPlayer();
            return;
        }

        BlockPos pos = masterRitualStone.getBlockPos();

        AreaDescriptor placementRange = getBlockRange(PLACEMENT_RANGE);

        BlockPos replacement = pos;
        boolean replace = false;

        for (BlockPos offset : placementRange.getContainedPositions(pos))
        {
            if (world.isAirBlock(offset))
            {
                replacement = offset;
                replace = true;
                break;
            }
        }

        IBlockState downState = world.getBlockState(pos.down());
        Block downBlock = downState.getBlock();

        int radius = getRadius(downBlock);

        if (replace)
        {
            int j = -1;
            int i = -radius;
            int k = -radius;

            if (lastPos != null)
            {
                j = lastPos.getY();
                i = Math.min(radius, Math.max(-radius, lastPos.getX()));
                k = Math.min(radius, Math.max(-radius, lastPos.getZ()));
            }

            while (j + pos.getY() >= 0)
            {
                while (i <= radius)
                {
                    while (k <= radius)
                    {
                        BlockPos newPos = pos.add(i, j, k);
                        IBlockState state = world.getBlockState(newPos);
                        Block block = state.getBlock();
                        int meta = block.getMetaFromState(state);

                        if (isBlockOre(block, meta))
                        {
                            TileTeleposer.teleportBlocks(this, world, newPos, world, replacement);
                            network.syphon(getRefreshCost());
                            k++;
                            this.lastPos = new BlockPos(i, j, k);
                            return;
                        } else
                        {
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

    public int getRadius(Block block)
    {
        if (block == Blocks.iron_block)
        {
            return 7;
        }

        if (block == Blocks.gold_block)
        {
            return 15;
        }

        if (block == Blocks.diamond_block)
        {
            return 31;
        }

        return 3;
    }

    @Override
    public int getRefreshTime()
    {
        return 40;
    }

    @Override
    public int getRefreshCost()
    {
        return 50;
    }

    @Override
    public ArrayList<RitualComponent> getComponents()
    {
        ArrayList<RitualComponent> components = new ArrayList<RitualComponent>();

        this.addCornerRunes(components, 1, 0, EnumRuneType.EARTH);
        this.addParallelRunes(components, 2, 1, EnumRuneType.EARTH);
        this.addCornerRunes(components, 2, 1, EnumRuneType.AIR);
        this.addParallelRunes(components, 2, 2, EnumRuneType.FIRE);

        return components;
    }

    @Override
    public Ritual getNewCopy()
    {
        return new RitualMagnetic();
    }
}
