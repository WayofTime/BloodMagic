package wayoftime.bloodmagic.recipe;

import java.util.List;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.base.Preconditions;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tags.TagCollectionManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

public class RecipeMeteor extends BloodMagicRecipe
{
	@Nonnull
	protected final Ingredient input;

	@Nonnegative
	private final int syphon;

	private final int maxWeight;
	private final List<Pair<Tags.IOptionalNamedTag<Block>, Integer>> weightList;

	private final Block fillBlock;

	public RecipeMeteor(ResourceLocation id, Ingredient input, int syphon, int maxWeight, List<Pair<Tags.IOptionalNamedTag<Block>, Integer>> weightList, Block fillBlock)
	{
		super(id);
		Preconditions.checkNotNull(input, "input cannot be null.");
		Preconditions.checkArgument(syphon >= 0, "syphon cannot be negative.");
		Preconditions.checkArgument(maxWeight >= 0, "maxWeight cannot be negative.");

		this.input = input;
		this.syphon = syphon;
		this.maxWeight = maxWeight;
		this.weightList = weightList;
		this.fillBlock = fillBlock;
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
	public void write(PacketBuffer buffer)
	{
		input.write(buffer);
		buffer.writeInt(getSyphon());
		buffer.writeInt(maxWeight);
		buffer.writeInt(weightList.size());
		for (int i = 0; i < weightList.size(); i++)
		{
//			input.get(i).write(buffer);
			Tags.IOptionalNamedTag<Block> tag = weightList.get(i).getKey();

			ResourceLocation rl = TagCollectionManager.getManager().getBlockTags().getDirectIdFromTag(tag);
			buffer.writeString(rl == null ? "" : rl.toString());
			buffer.writeInt(weightList.get(i).getValue());
//			tag = Tags.Blocks.BARRELS;
//			BlockTags.getCollection().get(new ResourceLocation(name));
//			ITag<Block> itag = TagCollectionManager.getManager().getBlockTags().get(resourcelocation);
		}

		buffer.writeInt(Item.getIdFromItem(fillBlock.asItem()));
//		Block.getBlockFromItem(itemIn)
//		Registry.BLOCK.g
	}

	@Override
	public IRecipeSerializer<?> getSerializer()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IRecipeType<RecipeMeteor> getType()
	{
//		return BloodMagicRecipeType.POTIONFLASK;
		return null;
	}
}
