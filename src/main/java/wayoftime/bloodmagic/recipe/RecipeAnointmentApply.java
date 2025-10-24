package wayoftime.bloodmagic.recipe;


import com.google.gson.JsonObject;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.anointment.AnointmentData;
import wayoftime.bloodmagic.anointment.AnointmentHolder;
import wayoftime.bloodmagic.common.item.ItemAnointmentProvider;
import wayoftime.bloodmagic.common.item.ItemBowAnointmentProvider;
import wayoftime.bloodmagic.core.AnointmentRegistrar;

public class RecipeAnointmentApply implements SmithingRecipe {
    @Override
    public boolean isTemplateIngredient(ItemStack stack) {
        return stack.getItem() instanceof ItemAnointmentProvider;
    }

    @Override
    public boolean isBaseIngredient(ItemStack stack) {
        return ItemAnointmentProvider.isItemSword(stack)
                || ItemAnointmentProvider.isItemTool(stack)
                || ItemBowAnointmentProvider.isItemBow(stack)
                || ItemBowAnointmentProvider.isItemCrossbow(stack);
    }

    @Override
    public boolean isAdditionIngredient(ItemStack stack) {
        return false;
    }

    @Override
    public boolean matches(Container container, Level level) {
        // 0 Template, 1 Base, 2 Extra
        ItemStack anointmentStack = container.getItem(0);
        ItemStack toolStack = container.getItem(1);
        if (anointmentStack.getItem() instanceof ItemAnointmentProvider toolProvider) {
            return toolProvider.isItemValidForApplication(toolStack);
        }

        return false;
    }

    @Override
    public ItemStack assemble(Container container, RegistryAccess registries) {
        // 0 Template, 1 Base, 2 Extra
        ItemAnointmentProvider anointmentStack = (ItemAnointmentProvider) container.getItem(0).getItem();
        ItemStack weaponStack = container.getItem(1).copy();

        AnointmentHolder holder = AnointmentHolder.fromItemStack(weaponStack);
        if (holder == null)
        {
            holder = new AnointmentHolder();
        }

        if (holder.applyAnointment(weaponStack, AnointmentRegistrar.ANOINTMENT_MAP.get(anointmentStack.anointRL), new AnointmentData(anointmentStack.level, 0, anointmentStack.maxDamage)))
        {
            holder.toItemStack(weaponStack);
            return weaponStack;
        }

        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registries) {
        return ItemStack.EMPTY;
    }

    @Override
    public ResourceLocation getId() {
        return BloodMagic.rl("anointment_apply");
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return null;
    }

    public static class Serializer implements RecipeSerializer<RecipeAnointmentApply> {
        public RecipeAnointmentApply fromJson(ResourceLocation p_266953_, JsonObject p_266720_) {
            return new RecipeAnointmentApply();
        }

        public RecipeAnointmentApply fromNetwork(ResourceLocation p_267117_, FriendlyByteBuf p_267316_) {
            return new RecipeAnointmentApply();
        }

        @Override
        public void toNetwork(FriendlyByteBuf p_44101_, RecipeAnointmentApply p_44102_) {
        }
    }
}
