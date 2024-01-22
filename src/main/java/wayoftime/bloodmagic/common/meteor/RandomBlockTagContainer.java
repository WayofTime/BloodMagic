package wayoftime.bloodmagic.common.meteor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import com.google.gson.JsonObject;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITag;
import wayoftime.bloodmagic.util.Constants;

public class RandomBlockTagContainer extends RandomBlockContainer
{
	public final TagKey<Block> tag;
	public final int index;;

	public RandomBlockTagContainer(TagKey<Block> tag2, int index)
	{
		this.tag = tag2;
		this.index = index;
	}

	@Override
	public Block getRandomBlock(RandomSource rand, Level world)
	{
		List<Block> list = new ArrayList<>();
//
//        for(Holder<Item> holder : Registry.ITEM.getTagOrEmpty(this.tag)) {
//           list.add(new ItemStack(holder));
//        }
//		Registry.BLOCK.getTagOrEmpty(tag);
		ITag<Block> blockTag = ForgeRegistries.BLOCKS.tags().getTag(tag);
		blockTag.forEach(a -> { list.add(a); });
//		Optional<HolderSet.Named<Block>> opt = Registry.BLOCK.getTag(tag);

		if (list.size() <= 0)
		{
			return null;
		}

		if (index >= 0 && index < list.size())
		{
			return list.get(index);
		}

		Optional<Block> optionalBlock = blockTag.getRandomElement(rand);

		return optionalBlock.orElse(null);
	}

	@Override
	public String getEntry()
	{
//		 jsonobject.addProperty("tag", this.tag.location().toString());
		ResourceLocation rl = tag.location();
		String entry = "#" + rl.toString();
		if (index >= 0)
		{
			entry = entry + "#" + index;
		}

		return entry;
	}

	@Override
	public JsonObject serialize(int weight)
	{
		JsonObject jsonObj = new JsonObject();
		jsonObj.addProperty(Constants.JSON.TAG, tag.location().toString());
		jsonObj.addProperty(Constants.JSON.INDEX, index);
		jsonObj.addProperty(Constants.JSON.WEIGHT, weight);

		return jsonObj;
	}

	@Override
	public JsonObject serialize()
	{
		JsonObject jsonObj = new JsonObject();
		jsonObj.addProperty(Constants.JSON.TAG, tag.location().toString());
		jsonObj.addProperty(Constants.JSON.INDEX, index);

		return jsonObj;
	}
}
