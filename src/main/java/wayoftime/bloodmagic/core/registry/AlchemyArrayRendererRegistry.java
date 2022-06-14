package wayoftime.bloodmagic.core.registry;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wayoftime.bloodmagic.client.render.alchemyarray.AlchemyArrayRenderer;
import wayoftime.bloodmagic.client.render.alchemyarray.BindingAlchemyCircleRenderer;
import wayoftime.bloodmagic.impl.BloodMagicAPI;
import wayoftime.bloodmagic.recipe.RecipeAlchemyArray;

public class AlchemyArrayRendererRegistry
{
	@OnlyIn(Dist.CLIENT)
	public static final AlchemyArrayRenderer DEFAULT_RENDERER = new AlchemyArrayRenderer(new ResourceLocation("bloodmagic", "textures/models/alchemyarrays/basearray.png"));

	@OnlyIn(Dist.CLIENT)
	public static Map<ResourceLocation, AlchemyArrayRenderer> rendererMap = new HashMap<ResourceLocation, AlchemyArrayRenderer>();

	/**
	 * 
	 * @param rl
	 * @param renderer
	 * @return True if there was already a renderer registered for this rl.
	 */
	@OnlyIn(Dist.CLIENT)
	public static boolean registerRenderer(ResourceLocation rl, AlchemyArrayRenderer renderer)
	{
		boolean hadKey = rendererMap.containsKey(rl);

		rendererMap.put(rl, renderer);

		return hadKey;
	}

	@OnlyIn(Dist.CLIENT)
	public static AlchemyArrayRenderer getRenderer(Level world, ResourceLocation rl, RecipeAlchemyArray recipe)
	{
		if (rendererMap.containsKey(rl))
		{
			return rendererMap.get(rl);
		}

		ResourceLocation texture = recipe.getTexture();
		if (AlchemyArrayRegistry.BINDING_ARRAY.equals(texture))
			return new BindingAlchemyCircleRenderer();

		if (texture != null)
			return new AlchemyArrayRenderer(texture);

		return null;
	}

	@OnlyIn(Dist.CLIENT)
	public static AlchemyArrayRenderer getRenderer(Level world, ItemStack input, ItemStack catalyst)
	{
		Pair<Boolean, RecipeAlchemyArray> array = BloodMagicAPI.INSTANCE.getRecipeRegistrar().getAlchemyArray(world, input, catalyst);
		if (array == null || array.getRight() == null)
		{
			return null;
		}

		return getRenderer(world, array.getRight().getId(), array.getRight());
	}
}
