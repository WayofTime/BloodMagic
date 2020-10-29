package wayoftime.bloodmagic.core.registry;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wayoftime.bloodmagic.api.impl.BloodMagicAPI;
import wayoftime.bloodmagic.api.impl.recipe.RecipeAlchemyArray;
import wayoftime.bloodmagic.client.render.alchemyarray.AlchemyArrayRenderer;

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
	public static AlchemyArrayRenderer getRenderer(World world, ResourceLocation rl, RecipeAlchemyArray recipe)
	{
		if (rendererMap.containsKey(rl))
		{
			return rendererMap.get(rl);
		}

		ResourceLocation texture = recipe.getTexture();
		if (texture != null)
			return new AlchemyArrayRenderer(texture);

		return null;
	}

	@OnlyIn(Dist.CLIENT)
	public static AlchemyArrayRenderer getRenderer(World world, ItemStack input, ItemStack catalyst)
	{
		Pair<Boolean, RecipeAlchemyArray> array = BloodMagicAPI.INSTANCE.getRecipeRegistrar().getAlchemyArray(world, input, catalyst);
		if (array == null || array.getRight() == null)
		{
			return null;
		}

		return getRenderer(world, array.getRight().getId(), array.getRight());
	}
}
