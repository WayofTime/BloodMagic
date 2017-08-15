package WayofTime.bloodmagic.item.routing;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.client.IVariantProvider;
import WayofTime.bloodmagic.item.inventory.ItemInventory;
import WayofTime.bloodmagic.routing.IFluidFilter;
import WayofTime.bloodmagic.routing.RoutingFluidFilter;
import WayofTime.bloodmagic.util.GhostItemHelper;
import WayofTime.bloodmagic.util.helper.TextHelper;

public class ItemFluidRouterFilter extends Item implements IFluidFilterProvider, IVariantProvider
{
    public static String[] names = { "exact" };

    public ItemFluidRouterFilter()
    {
        super();

        setUnlocalizedName(BloodMagic.MODID + ".fluidFilter.");
        setHasSubtypes(true);
        setCreativeTab(BloodMagic.TAB_BM);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return super.getUnlocalizedName(stack) + names[stack.getItemDamage()];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item id, CreativeTabs creativeTab, NonNullList<ItemStack> list)
    {
        for (int i = 0; i < names.length; i++)
            list.add(new ItemStack(id, 1, i));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced)
    {
        tooltip.add(TextHelper.localize("tooltip.BloodMagic.fluidFilter." + names[stack.getItemDamage()]));

        super.addInformation(stack, player, tooltip, advanced);
    }

    @Override
    public IFluidFilter getInputFluidFilter(ItemStack filterStack, TileEntity tile, IFluidHandler handler)
    {
        IFluidFilter testFilter;

        switch (filterStack.getMetadata())
        {
        case 0:
            testFilter = new RoutingFluidFilter();
            break;

        default:
            testFilter = new RoutingFluidFilter();
        }

        List<ItemStack> filteredList = new ArrayList<ItemStack>();
        ItemInventory inv = new ItemInventory(filterStack, 9, "");
        for (int i = 0; i < inv.getSizeInventory(); i++)
        {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack == null)
            {
                continue;
            }

            ItemStack ghostStack = GhostItemHelper.getStackFromGhost(stack);

            filteredList.add(ghostStack);
        }

        testFilter.initializeFilter(filteredList, tile, handler, false);
        return testFilter;
    }

    @Override
    public IFluidFilter getOutputFluidFilter(ItemStack filterStack, TileEntity tile, IFluidHandler handler)
    {
        IFluidFilter testFilter = new RoutingFluidFilter();

        switch (filterStack.getMetadata())
        {
        case 0:
            testFilter = new RoutingFluidFilter();
            break;

        default:
            testFilter = new RoutingFluidFilter();
        }

        List<ItemStack> filteredList = new ArrayList<ItemStack>();
        ItemInventory inv = new ItemInventory(filterStack, 9, ""); //TODO: Change to grab the filter from the Item later.
        for (int i = 0; i < inv.getSizeInventory(); i++)
        {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack == null)
            {
                continue;
            }

            ItemStack ghostStack = GhostItemHelper.getStackFromGhost(stack);
            if (ghostStack.isEmpty())
            {
                ghostStack.setCount(Integer.MAX_VALUE);
            }

            filteredList.add(ghostStack);
        }

        testFilter.initializeFilter(filteredList, tile, handler, true);
        return testFilter;
    }

    @Override
    public List<Pair<Integer, String>> getVariants()
    {
        List<Pair<Integer, String>> ret = new ArrayList<Pair<Integer, String>>();
        ret.add(new ImmutablePair<Integer, String>(0, "type=exact"));
        return ret;
    }

    @Override
    public ItemStack getContainedStackForItem(ItemStack filterStack, ItemStack keyStack)
    {
        ItemStack copyStack = keyStack.copy();
        GhostItemHelper.setItemGhostAmount(copyStack, 0);
        copyStack.setCount(1);
        return copyStack;
    }
}
