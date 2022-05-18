package wayoftime.bloodmagic.common.meteor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.tuple.Pair;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tags.ITag;
import net.minecraft.tags.TagCollectionManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;
import wayoftime.bloodmagic.util.Constants;

public class MeteorLayer
{
	public int layerRadius;
	public int maxWeight;
	public List<Pair<ITag<Block>, Integer>> weightList;
	public Block fillBlock;

	// TODO: Add option to have a shell at the meteor's layerRadius

	public MeteorLayer(int layerRadius, int maxWeight, List<Pair<ITag<Block>, Integer>> weightList, Block fillBlock)
	{
		this.layerRadius = layerRadius;
		this.maxWeight = maxWeight;
		this.weightList = weightList;
		this.fillBlock = fillBlock;
	}

	public MeteorLayer(int layerRadius, int maxWeight, Block fillBlock)
	{
		this(layerRadius, maxWeight, new ArrayList<>(), fillBlock);
	}

	public MeteorLayer addWeightedTag(ITag<Block> tag, int weight)
	{
		weightList.add(Pair.of(tag, weight));
		return this;
	}

	public void buildLayer(World world, BlockPos centerPos, int emptyRadius)
	{
		boolean alwaysFirst = false;

		int radius = layerRadius;
		for (int i = -radius; i <= radius; i++)
		{
			for (int j = -radius; j <= 0; j++)
			{
				for (int k = -radius; k <= radius; k++)
				{
					if (emptyRadius >= 0 && checkIfSphere(emptyRadius, i, j, k))
					{
						continue;
					}

					if (checkIfSphere(radius, i, j, k))
					{
						BlockPos pos = centerPos.add(i, j, k);
						if (checkIfSphereShell(radius, i, j, k))
						{
							world.setBlockState(pos, Blocks.CRACKED_NETHER_BRICKS.getDefaultState());
						} else
						{
							world.setBlockState(pos, getRandomState(world.rand, this, alwaysFirst));
						}
					}
				}
			}
		}
	}

	public BlockState getRandomState(Random rand, MeteorLayer layer, boolean alwaysFirst)
	{
		Block block = layer.fillBlock;
		int randNum = rand.nextInt(layer.maxWeight);
		for (Pair<ITag<Block>, Integer> entry : layer.weightList)
		{
			randNum -= entry.getValue();
			if (randNum < 0)
			{
				if (alwaysFirst)
				{
					block = entry.getKey().getAllElements().get(0);
				} else
				{
					block = entry.getKey().getRandomElement(rand);
				}
				break;
			}
		}

		if (block != null)
		{
			return block.getDefaultState();
		} else
		{
			return Blocks.AIR.getDefaultState();
		}
	}

	public boolean checkIfSphereShell(int xR, int xOff, int yOff, int zOff)
	{
		// Checking shell in the x-direction
		if (!checkIfSphere(xR, xOff, yOff, zOff))
		{
			return false;
		}

		return !((checkIfSphere(xR, xOff + 1, yOff, zOff) && checkIfSphere(xR, xOff - 1, yOff, zOff)) && (checkIfSphere(xR, xOff, yOff + 1, zOff) && checkIfSphere(xR, xOff, yOff - 1, zOff)) && (checkIfSphere(xR, xOff, yOff, zOff + 1) && checkIfSphere(xR, xOff, yOff, zOff - 1)));
	}

	public boolean checkIfSphere(float R, float xOff, float yOff, float zOff)
	{
		float possOffset = 0.5f;
		return xOff * xOff + yOff * yOff + zOff * zOff <= ((R + possOffset) * (R + possOffset));
	}

	public JsonObject serialize()
	{
		JsonObject json = new JsonObject();

		json.addProperty(Constants.JSON.RADIUS, layerRadius);
		json.addProperty(Constants.JSON.MAX_WEIGHT, maxWeight);

		if (weightList.size() > 0)
		{
			JsonArray mainArray = new JsonArray();
			for (Pair<ITag<Block>, Integer> weightedPair : weightList)
			{
				JsonObject jsonObj = new JsonObject();
				ResourceLocation rl = TagCollectionManager.getManager().getBlockTags().getDirectIdFromTag(weightedPair.getKey());
				if (rl == null)
				{
					continue;
				}
				jsonObj.addProperty(Constants.JSON.TAG, rl.toString());
				jsonObj.addProperty(Constants.JSON.WEIGHT, weightedPair.getValue());

				mainArray.add(jsonObj);
			}

			json.add(Constants.JSON.WEIGHT_MAP, mainArray);
		}

		json.addProperty(Constants.JSON.FILL, ForgeRegistries.BLOCKS.getKey(fillBlock).toString());

		return json;
	}

	public static MeteorLayer deserialize(JsonObject json)
	{
		int layerRadius = JSONUtils.getInt(json, Constants.JSON.RADIUS);
		int maxWeight = JSONUtils.getInt(json, Constants.JSON.MAX_WEIGHT);
		List<Pair<ITag<Block>, Integer>> weightList = new ArrayList<>();

		if (json.has(Constants.JSON.WEIGHT_MAP) && JSONUtils.isJsonArray(json, Constants.JSON.WEIGHT_MAP))
		{
			JsonArray mainArray = JSONUtils.getJsonArray(json, Constants.JSON.WEIGHT_MAP);

			for (JsonElement element : mainArray)
			{
				JsonObject obj = element.getAsJsonObject();
				ITag<Block> itag = TagCollectionManager.getManager().getBlockTags().get(new ResourceLocation(JSONUtils.getString(obj, Constants.JSON.TAG)));
				int weight = JSONUtils.getInt(obj, Constants.JSON.WEIGHT);

				if (itag != null)
				{
					weightList.add(Pair.of(itag, weight));
				}
			}
		}

		Block fillBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(JSONUtils.getString(json, Constants.JSON.FILL)));

		return new MeteorLayer(layerRadius, maxWeight, weightList, fillBlock);
	}

	public void write(PacketBuffer buffer)
	{
		buffer.writeInt(layerRadius);
		buffer.writeInt(maxWeight);
		buffer.writeInt(weightList.size());
		for (int i = 0; i < weightList.size(); i++)
		{
//			input.get(i).write(buffer);
			ITag<Block> tag = weightList.get(i).getKey();

			ResourceLocation rl = TagCollectionManager.getManager().getBlockTags().getDirectIdFromTag(tag);
			buffer.writeString(rl == null ? "" : rl.toString());
			buffer.writeInt(weightList.get(i).getValue());
//			tag = Tags.Blocks.BARRELS;
//			BlockTags.getCollection().get(new ResourceLocation(name));
//			ITag<Block> itag = TagCollectionManager.getManager().getBlockTags().get(resourcelocation);
		}

		buffer.writeInt(Item.getIdFromItem(fillBlock.asItem()));
	}

	public static MeteorLayer read(@Nonnull PacketBuffer buffer)
	{
		int layerRadius = buffer.readInt();
		int maxWeight = buffer.readInt();
		int listSize = buffer.readInt();
		List<Pair<ITag<Block>, Integer>> weightList = new ArrayList<>();
		for (int i = 0; i < listSize; i++)
		{
			String name = buffer.readString();
			int weight = buffer.readInt();
			if (!name.isEmpty())
			{
				ITag<Block> itag = TagCollectionManager.getManager().getBlockTags().get(new ResourceLocation(name));
				if (itag != null)
				{
					weightList.add(Pair.of(itag, weight));
				}
			}
		}

		Block fillBlock = Block.getBlockFromItem(Item.getItemById(buffer.readInt()));

		return new MeteorLayer(layerRadius, maxWeight, weightList, fillBlock);
	}
}
