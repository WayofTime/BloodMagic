package wayoftime.bloodmagic.common.meteor;

import java.util.Random;

import com.google.gson.JsonObject;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import wayoftime.bloodmagic.util.Constants;

public class StaticBlockContainer extends RandomBlockContainer
{
	private Block block;

	public StaticBlockContainer(Block block)
	{
		this.block = block;
	}

	@Override
	public Block getRandomBlock(RandomSource rand, Level world)
	{
		return block;
	}

	@Override
	public String getEntry()
	{
		return ForgeRegistries.BLOCKS.getKey(block).toString();
	}

	@Override
	public JsonObject serialize(int weight)
	{
		JsonObject jsonObj = new JsonObject();
		jsonObj.addProperty(Constants.JSON.BLOCK, getEntry());
		jsonObj.addProperty(Constants.JSON.WEIGHT, weight);

		return jsonObj;
	}

	@Override
	public JsonObject serialize()
	{
		JsonObject jsonObj = new JsonObject();
		jsonObj.addProperty(Constants.JSON.BLOCK, getEntry());

		return jsonObj;
	}
}
