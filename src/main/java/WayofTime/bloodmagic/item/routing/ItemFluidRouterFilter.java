package WayofTime.bloodmagic.item.routing;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.client.IVariantProvider;
import WayofTime.bloodmagic.item.inventory.ItemInventory;
import WayofTime.bloodmagic.routing.IFluidFilter;
import WayofTime.bloodmagic.routing.RoutingFluidFilter;
import WayofTime.bloodmagic.util.GhostItemHelper;
import WayofTime.bloodmagic.util.helper.TextHelper;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class ItemFluidRouterFilter extends Item implements IFluidFilterProvider, IVariantProvider {
    public static String[] names = {"exact"};

    public ItemFluidRouterFilter() {
        super();

        setUnlocalizedName(BloodMagic.MODID + ".fluidFilter.");
        setHasSubtypes(true);
        setCreativeTab(BloodMagic.TAB_BM);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName(stack) + names[stack.getItemDamage()];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(CreativeTabs creativeTab, NonNullList<ItemStack> list) {
        if (!isInCreativeTab(creativeTab))
            return;

        for (int i = 0; i < names.length; i++)
            list.add(new ItemStack(this, 1, i));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
        tooltip.add(TextHelper.localize("tooltip.bloodmagic.fluidFilter." + names[stack.getItemDamage()]));

        super.addInformation(stack, world, tooltip, flag);
    }

    @Override
    public IFluidFilter getInputFluidFilter(ItemStack filterStack, TileEntity tile, IFluidHandler handler) {
        IFluidFilter testFilter;

        switch (filterStack.getMetadata()) {
            case 0:
                testFilter = new RoutingFluidFilter();
                break;

            default:
                testFilter = new RoutingFluidFilter();
        }

        List<ItemStack> filteredList = new ArrayList<>();
        ItemInventory inv = new ItemInventory(filterStack, 9, "");
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack == null) {
                continue;
            }

            ItemStack ghostStack = GhostItemHelper.getStackFromGhost(stack);

            filteredList.add(ghostStack);
        }

        testFilter.initializeFilter(filteredList, tile, handler, false);
        return testFilter;
    }

    @Override
    public IFluidFilter getOutputFluidFilter(ItemStack filterStack, TileEntity tile, IFluidHandler handler) {
        IFluidFilter testFilter;
        switch (filterStack.getMetadata()) {
            case 0:
                testFilter = new RoutingFluidFilter();
                break;

            default:
                testFilter = new RoutingFluidFilter();
        }

        List<ItemStack> filteredList = new ArrayList<>();
        ItemInventory inv = new ItemInventory(filterStack, 9, ""); //TODO: Change to grab the filter from the Item later.
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack.isEmpty()) {
                continue;
            }

            ItemStack ghostStack = GhostItemHelper.getStackFromGhost(stack);
            if (ghostStack.isEmpty()) {
                ghostStack.setCount(Integer.MAX_VALUE);
            }

            filteredList.add(ghostStack);
        }

        testFilter.initializeFilter(filteredList, tile, handler, true);
        return testFilter;
    }

    @Override
    public void gatherVariants(@Nonnull Int2ObjectMap<String> variants) {
        variants.put(0, "type=exact");
    }

    @Override
    public ItemStack getContainedStackForItem(ItemStack filterStack, ItemStack keyStack) {
        ItemStack copyStack = keyStack.copy();
        GhostItemHelper.setItemGhostAmount(copyStack, 0);
        copyStack.setCount(1);
        return copyStack;
    }
}
