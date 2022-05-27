package wayoftime.bloodmagic.common.meteor;

import java.util.ArrayList;
import java.util.Iterator;
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
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tags.ITag;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import wayoftime.bloodmagic.util.Constants;

public class MeteorLayer
{
	public int layerRadius;
	public int additionalTotalWeight;
	public int totalMaxWeight = 0;
	public List<Pair<RandomBlockContainer, Integer>> weightList;
	public RandomBlockContainer fillBlock;
	public RandomBlockContainer shellBlock;

	// TODO: Add option to have a shell at the meteor's layerRadius

	public MeteorLayer(int layerRadius, int additionalMaxWeight, List<Pair<RandomBlockContainer, Integer>> weightList, RandomBlockContainer fillBlock)
	{
		this.layerRadius = layerRadius;
		this.additionalTotalWeight = additionalMaxWeight;
		this.weightList = weightList;
		this.fillBlock = fillBlock;
	}

	public MeteorLayer(int layerRadius, int additionalMaxWeight, Block fillBlock)
	{
		this(layerRadius, additionalMaxWeight, new ArrayList<>(), new StaticBlockContainer(fillBlock));
	}

	public MeteorLayer(int layerRadius, int additionalMaxWeight, ITag<Block> fillTag)
	{
		this(layerRadius, additionalMaxWeight, fillTag, -1);
	}

	public MeteorLayer(int layerRadius, int additionalMaxWeight, ITag<Block> fillTag, int staticIndex)
	{
		this(layerRadius, additionalMaxWeight, new ArrayList<>(), new RandomBlockTagContainer(fillTag, staticIndex));
	}

	public MeteorLayer addShellBlock(RandomBlockContainer shellBlock)
	{
		this.shellBlock = shellBlock;
		return this;
	}

	public MeteorLayer addShellBlock(ITag<Block> tag)
	{
		return addShellBlock(tag, -1);
	}

	public MeteorLayer addShellBlock(ITag<Block> tag, int staticIndex)
	{
		return addShellBlock(new RandomBlockTagContainer(tag, staticIndex));
	}

	public MeteorLayer addShellBlock(Block block)
	{
		return addShellBlock(new StaticBlockContainer(block));
	}

	public MeteorLayer addWeightedTag(ITag<Block> tag, int weight)
	{
		return addWeightedTag(tag, weight, -1);
	}

	public MeteorLayer addWeightedTag(ITag<Block> tag, int weight, int staticIndex)
	{
		weightList.add(Pair.of(new RandomBlockTagContainer(tag, staticIndex), weight));
		return this;
	}

	public MeteorLayer addWeightedBlock(Block block, int weight)
	{
		weightList.add(Pair.of(new StaticBlockContainer(block), weight));
		return this;
	}

	public void buildLayer(World world, BlockPos centerPos, int emptyRadius)
	{
		recalculateMaxWeight(world.rand, world);

		int radius = layerRadius;
		for (int i = -radius; i <= radius; i++)
		{
			for (int j = -radius; j <= radius; j++)
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
						BlockState currentState = world.getBlockState(pos);
						BlockItemUseContext ctx = new BlockItemUseContext(world, null, Hand.MAIN_HAND, ItemStack.EMPTY, BlockRayTraceResult.createMiss(new Vector3d(0, 0, 0), Direction.UP, pos));
						if (!currentState.isReplaceable(ctx))
						{
							continue;
						}
						if (shellBlock != null && checkIfSphereShell(radius, i, j, k))
						{
							world.setBlockState(pos, shellBlock.getRandomBlock(world.rand, world).getDefaultState());
						} else
						{
							world.setBlockState(pos, getRandomState(world.rand, world));
						}
					}
				}
			}
		}
	}

	public void recalculateMaxWeight(Random rand, World world)
	{
		totalMaxWeight = additionalTotalWeight;

		Iterator<Pair<RandomBlockContainer, Integer>> itr = weightList.iterator();
		while (itr.hasNext())
		{
			Pair<RandomBlockContainer, Integer> entry = itr.next();
			Block newBlock = entry.getKey().getRandomBlock(rand, world);
			if (newBlock == null)
			{
				itr.remove();
//				weightList.remove(entry);
				continue;
			}

			totalMaxWeight += entry.getRight();
		}
	}

	public BlockState getRandomState(Random rand, World world)
	{
		Block block = fillBlock.getRandomBlock(rand, world);
		int randNum = rand.nextInt(totalMaxWeight);
		for (Pair<RandomBlockContainer, Integer> entry : weightList)
		{
			randNum -= entry.getValue();
			if (randNum < 0)
			{
				Block newBlock = entry.getKey().getRandomBlock(rand, world);
				if (newBlock != null)
					block = newBlock;
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

//	public void 

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
		json.addProperty(Constants.JSON.ADDITIONAL_MAX_WEIGHT, additionalTotalWeight);

		if (weightList.size() > 0)
		{
			JsonArray mainArray = new JsonArray();
			for (Pair<RandomBlockContainer, Integer> weightedPair : weightList)
			{
				JsonObject jsonObj = new JsonObject();
//				ResourceLocation rl = TagCollectionManager.getManager().getBlockTags().getDirectIdFromTag(weightedPair.getKey());
//				if (rl == null)
//				{
//					continue;
//				}
				jsonObj.addProperty(Constants.JSON.TAG, weightedPair.getKey().getEntry());
				jsonObj.addProperty(Constants.JSON.WEIGHT, weightedPair.getValue());

				mainArray.add(jsonObj);
			}

			json.add(Constants.JSON.WEIGHT_MAP, mainArray);
		}

		json.addProperty(Constants.JSON.FILL, fillBlock.getEntry());
		if (shellBlock != null)
		{
			json.addProperty(Constants.JSON.SHELL, shellBlock.getEntry());
		}

		return json;
	}

	public static MeteorLayer deserialize(JsonObject json)
	{
		int layerRadius = JSONUtils.getInt(json, Constants.JSON.RADIUS);
		int maxWeight = JSONUtils.getInt(json, Constants.JSON.ADDITIONAL_MAX_WEIGHT);
		List<Pair<RandomBlockContainer, Integer>> weightList = new ArrayList<>();

		if (json.has(Constants.JSON.WEIGHT_MAP) && JSONUtils.isJsonArray(json, Constants.JSON.WEIGHT_MAP))
		{
			JsonArray mainArray = JSONUtils.getJsonArray(json, Constants.JSON.WEIGHT_MAP);

			for (JsonElement element : mainArray)
			{
				JsonObject obj = element.getAsJsonObject();
				RandomBlockContainer container = RandomBlockContainer.parseEntry(JSONUtils.getString(obj, Constants.JSON.TAG));
//				ITag<Block> itag = TagCollectionManager.getManager().getBlockTags().get(new ResourceLocation(JSONUtils.getString(obj, Constants.JSON.TAG)));
				int weight = JSONUtils.getInt(obj, Constants.JSON.WEIGHT);

				if (container != null)
				{
					weightList.add(Pair.of(container, weight));
				}
			}
		}

//		Block fillBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(JSONUtils.getString(json, Constants.JSON.FILL)));
		RandomBlockContainer fillBlock = RandomBlockContainer.parseEntry(JSONUtils.getString(json, Constants.JSON.FILL));

		MeteorLayer layer = new MeteorLayer(layerRadius, maxWeight, weightList, fillBlock);
		if (JSONUtils.hasField(json, Constants.JSON.SHELL))
		{
			layer.addShellBlock(RandomBlockContainer.parseEntry(JSONUtils.getString(json, Constants.JSON.SHELL)));
		}

		return layer;
	}

	public void write(PacketBuffer buffer)
	{
		buffer.writeInt(layerRadius);
		buffer.writeInt(additionalTotalWeight);
		buffer.writeInt(weightList.size());
		for (int i = 0; i < weightList.size(); i++)
		{
//			input.get(i).write(buffer);
//			ITag<Block> tag = weightList.get(i).getKey();
//
//			ResourceLocation rl = TagCollectionManager.getManager().getBlockTags().getDirectIdFromTag(tag);
//			buffer.writeString(rl == null ? "" : rl.toString());
			buffer.writeString(weightList.get(i).getKey().getEntry());
			buffer.writeInt(weightList.get(i).getValue());
//			tag = Tags.Blocks.BARRELS;
//			BlockTags.getCollection().get(new ResourceLocation(name));
//			ITag<Block> itag = TagCollectionManager.getManager().getBlockTags().get(resourcelocation);
		}

		buffer.writeString(fillBlock.getEntry());
		if (shellBlock == null)
		{
			buffer.writeString("");
		} else
		{
			buffer.writeString(shellBlock.getEntry());
		}
	}

	public static MeteorLayer read(@Nonnull PacketBuffer buffer)
	{
		int layerRadius = buffer.readInt();
		int maxWeight = buffer.readInt();
		int listSize = buffer.readInt();
		List<Pair<RandomBlockContainer, Integer>> weightList = new ArrayList<>();
		for (int i = 0; i < listSize; i++)
		{
			String entry = buffer.readString();
			int weight = buffer.readInt();
			if (!entry.isEmpty())
			{
				RandomBlockContainer container = RandomBlockContainer.parseEntry(entry);

				weightList.add(Pair.of(container, weight));
			}
		}

		RandomBlockContainer fillBlock = RandomBlockContainer.parseEntry(buffer.readString());

		MeteorLayer layer = new MeteorLayer(layerRadius, maxWeight, weightList, fillBlock);
		String shellEntry = buffer.readString();
		if (!shellEntry.isEmpty())
		{
			layer.addShellBlock(RandomBlockContainer.parseEntry(shellEntry));
		}

		return layer;
	}
}
