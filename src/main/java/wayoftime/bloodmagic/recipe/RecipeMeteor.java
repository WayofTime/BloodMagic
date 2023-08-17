package wayoftime.bloodmagic.recipe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import com.google.common.base.Preconditions;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Explosion.BlockInteraction;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import wayoftime.bloodmagic.common.meteor.MeteorLayer;
import wayoftime.bloodmagic.common.recipe.BloodMagicRecipeType;
import wayoftime.bloodmagic.common.registries.BloodMagicRecipeSerializers;

public class RecipeMeteor extends BloodMagicRecipe
{

	@Nonnull
	protected final Ingredient input;

	@Nonnegative
	private final int syphon;

	private final float explosionRadius;

	private final List<MeteorLayer> layerList;

	public RecipeMeteor(ResourceLocation id, Ingredient input, int syphon, float explosionRadius, List<MeteorLayer> layerList)
	{
		super(id);
		Preconditions.checkNotNull(input, "input cannot be null.");
		Preconditions.checkArgument(syphon >= 0, "syphon cannot be negative.");
		Preconditions.checkArgument(explosionRadius >= 0, "explosionRadius cannot be negative.");

		this.input = input;
		this.syphon = syphon;
		this.explosionRadius = explosionRadius;
		this.layerList = layerList;
	}

	public void spawnMeteorInWorld(Level world, BlockPos centerPos)
	{
		if (explosionRadius > 0)
			world.explode(null, centerPos.getX(), centerPos.getY(), centerPos.getZ(), explosionRadius, true, Level.ExplosionInteraction.TNT);

		Map<Integer, MeteorLayer> layerMap = new HashMap<>();
		for (MeteorLayer layer : layerList)
		{
			layerMap.put(layer.layerRadius, layer);
		}

		List<Integer> keyList = new ArrayList<>(layerMap.keySet());
		Collections.sort(keyList);

		int prevRadius = -1;
		for (int i = 0; i < keyList.size(); i++)
		{
			MeteorLayer layer = layerMap.get(keyList.get(i));
			layer.buildLayer(world, centerPos, prevRadius);
			prevRadius = layer.layerRadius;
		}
	}

	@Nonnull
	public Ingredient getInput()
	{
		return input;
	}

//	@Nonnull
//	public abstract ItemStack getOutput(ItemStack flaskStack, List<EffectHolder> flaskEffectList, List<ItemStack> inputs);

	public final int getSyphon()
	{
		return syphon;
	}

	@Override
	public void write(FriendlyByteBuf buffer)
	{
		input.toNetwork(buffer);
		buffer.writeInt(getSyphon());
		buffer.writeFloat(explosionRadius);

		buffer.writeInt(layerList.size());
		for (int i = 0; i < layerList.size(); i++)
		{
			layerList.get(i).write(buffer);
		}
//		Block.getBlockFromItem(itemIn)
//		Registry.BLOCK.g
	}

	@Override
	public RecipeSerializer<? extends RecipeMeteor> getSerializer()
	{
		return BloodMagicRecipeSerializers.METEOR.getRecipeSerializer();
	}

	@Override
	public RecipeType<RecipeMeteor> getType()
	{
		return BloodMagicRecipeType.METEOR.get();
	}
}
