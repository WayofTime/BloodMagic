package wayoftime.bloodmagic.core.data;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.saveddata.SavedData;
import wayoftime.bloodmagic.util.helper.PlayerHelper;

public class BMWorldSavedData extends SavedData
{
	public static final String ID = "BloodMagic-SoulNetworks";

	private int numberOfDungeons = 0;
	public static final int DUNGEON_DISPLACEMENT = 1000;

	private Map<UUID, SoulNetwork> soulNetworks = new HashMap<>();

	public BMWorldSavedData(String id)
	{
//		super(id);
	}

	public BMWorldSavedData()
	{
		this(ID);
	}

	public SoulNetwork getNetwork(Player player)
	{
		return getNetwork(PlayerHelper.getUUIDFromPlayer(player));
	}

	public SoulNetwork getNetwork(UUID playerId)
	{
		if (!soulNetworks.containsKey(playerId))
			soulNetworks.put(playerId, SoulNetwork.newEmpty(playerId).setParent(this));
		return soulNetworks.get(playerId);
	}

	public static BMWorldSavedData load(CompoundTag tagCompound)
	{
		BMWorldSavedData worldData = new BMWorldSavedData();
		ListTag networkData = tagCompound.getList("networkData", 10);

		for (int i = 0; i < networkData.size(); i++)
		{
			CompoundTag data = networkData.getCompound(i);
			SoulNetwork network = SoulNetwork.fromNBT(data);
			network.setParent(worldData);
			worldData.soulNetworks.put(network.getPlayerId(), network);
		}

		worldData.numberOfDungeons = tagCompound.getInt("numberOfDungeons");

		return worldData;
	}

//	@Override
//	public void load(CompoundTag tagCompound)
//	{
//		ListTag networkData = tagCompound.getList("networkData", 10);
//
//		for (int i = 0; i < networkData.size(); i++)
//		{
//			CompoundTag data = networkData.getCompound(i);
//			SoulNetwork network = SoulNetwork.fromNBT(data);
//			network.setParent(this);
//			soulNetworks.put(network.getPlayerId(), network);
//		}
//
//		numberOfDungeons = tagCompound.getInt("numberOfDungeons");
//	}

	@Override
	public CompoundTag save(CompoundTag tagCompound)
	{
		ListTag networkData = new ListTag();
		for (SoulNetwork soulNetwork : soulNetworks.values()) networkData.add(soulNetwork.serializeNBT());

		tagCompound.put("networkData", networkData);
		tagCompound.putInt("numberOfDungeons", numberOfDungeons);

		return tagCompound;
	}

	public int getNumberOfDungeons()
	{
		return numberOfDungeons;
	}

	public void setNumberOfDungeons(int numberOfDungeons)
	{
		this.numberOfDungeons = numberOfDungeons;
	}
}