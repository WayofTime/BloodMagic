package wayoftime.bloodmagic.compat.jei;

import java.util.Objects;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.api.impl.BloodMagicAPI;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.common.item.BloodMagicItems;
import wayoftime.bloodmagic.compat.jei.altar.BloodAltarRecipeCategory;
import wayoftime.bloodmagic.compat.jei.array.AlchemyArrayCraftingCategory;
import wayoftime.bloodmagic.compat.jei.forge.TartaricForgeRecipeCategory;

@JeiPlugin
public class BloodMagicJEIPlugin implements IModPlugin
{
	public static IJeiHelpers jeiHelper;

	private static final ResourceLocation ID = BloodMagic.rl("jei_plugin");

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration)
	{
		registration.addRecipeCatalyst(new ItemStack(BloodMagicBlocks.SOUL_FORGE.get()), TartaricForgeRecipeCategory.UID);
		registration.addRecipeCatalyst(new ItemStack(BloodMagicBlocks.BLOOD_ALTAR.get()), BloodAltarRecipeCategory.UID);
		registration.addRecipeCatalyst(new ItemStack(BloodMagicItems.ARCANE_ASHES.get()), AlchemyArrayCraftingCategory.UID);
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registration)
	{
		jeiHelper = registration.getJeiHelpers();
		registration.addRecipeCategories(new TartaricForgeRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
		registration.addRecipeCategories(new BloodAltarRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
		registration.addRecipeCategories(new AlchemyArrayCraftingCategory(registration.getJeiHelpers().getGuiHelper()));
	}

	@Override
	public void registerRecipes(IRecipeRegistration registration)
	{
		ClientWorld world = Objects.requireNonNull(Minecraft.getInstance().world);
		registration.addRecipes(BloodMagicAPI.INSTANCE.getRecipeRegistrar().getTartaricForgeRecipes(world), TartaricForgeRecipeCategory.UID);
		registration.addRecipes(BloodMagicAPI.INSTANCE.getRecipeRegistrar().getAltarRecipes(world), BloodAltarRecipeCategory.UID);
		registration.addRecipes(BloodMagicAPI.INSTANCE.getRecipeRegistrar().getAlchemyArrayRecipes(world), AlchemyArrayCraftingCategory.UID);
	}

	@Override
	public ResourceLocation getPluginUid()
	{
		return ID;
	}

}
