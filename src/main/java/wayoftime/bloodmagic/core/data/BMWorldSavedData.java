package wayoftime.bloodmagic.core.data;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.world.storage.WorldSavedData;
import wayoftime.bloodmagic.util.helper.PlayerHelper;

public class BMWorldSavedData extends WorldSavedData
{
	public static final String ID = "BloodMagic-SoulNetworks";

	private int numberOfDungeons = 0;
	public static final int DUNGEON_DISPLACEMENT = 1000;

	private Map<UUID, SoulNetwork> soulNetworks = new HashMap<>();

	public BMWorldSavedData(String id)
	{
		super(id);
	}

	public BMWorldSavedData()
	{
		this(ID);
	}

	public SoulNetwork getNetwork(PlayerEntity player)
	{
		return getNetwork(PlayerHelper.getUUIDFromPlayer(player));
	}

	public SoulNetwork getNetwork(UUID playerId)
	{
		if (!soulNetworks.containsKey(playerId))
			soulNetworks.put(playerId, SoulNetwork.newEmpty(playerId).setParent(this));
		return soulNetworks.get(playerId);
	}

	@Override
	public void read(CompoundNBT tagCompound)
	{
		ListNBT networkData = tagCompound.getList("networkData", 10);

		for (int i = 0; i < networkData.size(); i++)
		{
			CompoundNBT data = networkData.getCompound(i);
			SoulNetwork network = SoulNetwork.fromNBT(data);
			network.setParent(this);
			soulNetworks.put(network.getPlayerId(), network);
		}

		numberOfDungeons = tagCompound.getInt("numberOfDungeons");
	}

	@Override
	public CompoundNBT write(CompoundNBT tagCompound)
	{
		ListNBT networkData = new ListNBT();
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