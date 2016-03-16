package WayofTime.bloodmagic.item.routing;

import java.util.ArrayList;
import java.util.List;

import WayofTime.bloodmagic.client.IVariantProvider;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.item.inventory.ItemInventory;
import WayofTime.bloodmagic.routing.DefaultItemFilter;
import WayofTime.bloodmagic.routing.IItemFilter;
import WayofTime.bloodmagic.routing.IgnoreNBTItemFilter;
import WayofTime.bloodmagic.routing.ModIdItemFilter;
import WayofTime.bloodmagic.routing.OreDictItemFilter;
import WayofTime.bloodmagic.routing.TestItemFilter;
import WayofTime.bloodmagic.util.GhostItemHelper;
import WayofTime.bloodmagic.util.helper.TextHelper;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class ItemRouterFilter extends Item implements IItemFilterProvider, IVariantProvider
{
    public static String[] names = { "exact", "ignoreNBT", "modItems", "oreDict" };

    public ItemRouterFilter()
    {
        super();

        setUnlocalizedName(Constants.Mod.MODID + ".itemFilter.");
        setRegistryName(Constants.BloodMagicItem.ROUTER_FILTER.getRegName());
        setHasSubtypes(true);
        setCreativeTab(BloodMagic.tabBloodMagic);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return super.getUnlocalizedName(stack) + names[stack.getItemDamage()];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item id, CreativeTabs creativeTab, List<ItemStack> list)
    {
        for (int i = 0; i < names.length; i++)
            list.add(new ItemStack(id, 1, i));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced)
    {
        tooltip.add(TextHelper.localize("tooltip.BloodMagic.itemFilter." + names[stack.getItemDamage()]));

        super.addInformation(stack, player, tooltip, advanced);
    }

    @Override
    public IItemFilter getInputItemFilter(ItemStack filterStack, IInventory inventory, EnumFacing syphonDirection)
    {
        IItemFilter testFilter = new TestItemFilter();

        switch (filterStack.getMetadata())
        {
        case 0:
            testFilter = new TestItemFilter();
            break;
        case 1:
            testFilter = new IgnoreNBTItemFilter();
            break;
        case 2:
            testFilter = new ModIdItemFilter();
            break;
        case 3:
            testFilter = new OreDictItemFilter();
            break;

        default:
            testFilter = new DefaultItemFilter();
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

        testFilter.initializeFilter(filteredList, inventory, syphonDirection, false);
        return testFilter;
    }

    @Override
    public IItemFilter getOutputItemFilter(ItemStack filterStack, IInventory inventory, EnumFacing syphonDirection)
    {
        IItemFilter testFilter = new TestItemFilter();

        switch (filterStack.getMetadata())
        {
        case 0:
            testFilter = new TestItemFilter();
            break;
        case 1:
            testFilter = new IgnoreNBTItemFilter();
            break;
        case 2:
            testFilter = new ModIdItemFilter();
            break;
        case 3:
            testFilter = new OreDictItemFilter();
            break;

        default:
            testFilter = new DefaultItemFilter();
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
            if (ghostStack.stackSize == 0)
            {
                ghostStack.stackSize = Integer.MAX_VALUE;
            }

            filteredList.add(ghostStack);
        }

        testFilter.initializeFilter(filteredList, inventory, syphonDirection, true);
        return testFilter;
    }

    @Override
    public List<Pair<Integer, String>> getVariants()
    {
        List<Pair<Integer, String>> ret = new ArrayList<Pair<Integer, String>>();
        ret.add(new ImmutablePair<Integer, String>(0, "type=exact"));
        ret.add(new ImmutablePair<Integer, String>(1, "type=ignorenbt"));
        ret.add(new ImmutablePair<Integer, String>(2, "type=moditems"));
        ret.add(new ImmutablePair<Integer, String>(3, "type=oredict"));
        return ret;
    }
}
