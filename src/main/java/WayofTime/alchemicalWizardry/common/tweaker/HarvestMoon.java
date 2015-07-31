package WayofTime.alchemicalWizardry.common.tweaker;

import WayofTime.alchemicalWizardry.api.harvest.HarvestRegistry;
import WayofTime.alchemicalWizardry.api.harvest.IHarvestHandler;
import WayofTime.alchemicalWizardry.common.harvest.GenericItemStackHarvestHandler;
import minetweaker.IUndoableAction;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.IPlantable;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

/**
 * MineTweaker3 Harvest Moon Handler by hilburn *
 */
@ZenClass("mods.bloodmagic.HarvestMoon")
public class HarvestMoon
{

    @ZenMethod
    public static void addHarvestable(IItemStack block, IItemStack seed)
    {
        addHarvestable(block,block.getDamage(),seed);
    }

    @ZenMethod
    public static void addHarvestable(IItemStack block, int meta, IItemStack seed)
    {
        ItemStack seedStack = MTHelper.toStack(seed);
        Block plantBlock = Block.getBlockFromItem(MTHelper.toStack(block).getItem());
        if (!(plantBlock==null || plantBlock== Blocks.air || seedStack==null || !(seedStack.getItem() instanceof IPlantable)))
        {
            MineTweakerAPI.apply(new Add(plantBlock, meta, seedStack));
        }
        else
        {
            throw new IllegalArgumentException("Invalid Harvest Block or Seed");
        }
    }

    private static class Add implements IUndoableAction
    {
        private IHarvestHandler handler;
        private String name;

        public Add(Block block, int meta, ItemStack seed)
        {
            handler = new GenericItemStackHarvestHandler(block,meta,seed);
            name = seed.getDisplayName();
        }

        @Override
        public void apply()
        {
            HarvestRegistry.registerHarvestHandler(handler);
        }

        @Override
        public boolean canUndo()
        {
            return HarvestRegistry.handlerList!=null;
        }

        @Override
        public void undo()
        {
            HarvestRegistry.handlerList.remove(handler);
        }

        @Override
        public String describe() {
            return "Adding Harvest Moon Support for " + name;
        }

        @Override
        public String describeUndo()
        {
            return "Removing Harvest Moon Support for " + name;
        }

        @Override
        public Object getOverrideKey()
        {
            return null;
        }
    }
}
