package WayofTime.bloodmagic.core.recipe;

import WayofTime.bloodmagic.orb.BloodOrb;
import WayofTime.bloodmagic.orb.IBloodOrb;
import WayofTime.bloodmagic.core.registry.OrbRegistry;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntComparators;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class IngredientBloodOrb extends Ingredient {

    private final BloodOrb orb;
    private NonNullList<ItemStack> orbs;
    private IntList itemIds = null;
    private ItemStack[] items;

    public IngredientBloodOrb(BloodOrb orb) {
        super();

        this.orb = orb;

        List<ItemStack> orbGet = OrbRegistry.getOrbsDownToTier(orb.getTier());
        orbs = NonNullList.withSize(orbGet.size(), ItemStack.EMPTY);

        for (int i = 0; i < orbGet.size(); i++)
            orbs.set(i, orbGet.get(i));
    }

    @Override
    @Nonnull
    public ItemStack[] getMatchingStacks() {
        if (items == null)
            items = orbs.toArray(new ItemStack[0]);
        return items;
    }

    @Override
    @Nonnull
    @SideOnly(Side.CLIENT)
    public IntList getValidItemStacksPacked() {
        if (this.itemIds == null || itemIds.size() != orbs.size()) {
            this.itemIds = new IntArrayList(orbs.size());

            for (ItemStack itemstack : orbs)
                this.itemIds.add(RecipeItemHelper.pack(itemstack));

            this.itemIds.sort(IntComparators.NATURAL_COMPARATOR);
        }

        return this.itemIds;
    }

    @Override
    public boolean apply(@Nullable ItemStack input) {
        if (input == null || input.isEmpty())
            return false;

        if (!input.hasTagCompound())
            return false;

        if (!(input.getItem() instanceof IBloodOrb))
            return false;

        BloodOrb orb = ((IBloodOrb) input.getItem()).getOrb(input);
        return orb != null && orb.getTier() >= this.orb.getTier();
    }

    @Override
    protected void invalidate() {
        this.itemIds = null;
    }
}
