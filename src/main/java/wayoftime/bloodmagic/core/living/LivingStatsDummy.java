package wayoftime.bloodmagic.core.living;

import java.util.Map;

import com.google.common.collect.Maps;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;

public class LivingStatsDummy extends LivingStats
{
	protected final Map<ResourceLocation, Double> upgrade_keys = Maps.newHashMap();

	public LivingStatsDummy()
	{
		super();
	}

	public LivingStats addExperience(ResourceLocation key, double experience)
	{
		super.addExperience(key, experience);

		double current = upgrade_keys.getOrDefault(key, 0d);
		upgrade_keys.put(key, current + experience);

		return this;
	}

	public CompoundTag serialize()
	{
		CompoundTag compound = new CompoundTag();
		ListTag statList = new ListTag();
		upgrade_keys.forEach((k, v) -> {
			CompoundTag upgrade = new CompoundTag();
			upgrade.putString("key", k.toString());
			upgrade.putDouble("exp", v);
			statList.add(upgrade);
		});
		compound.put("upgrades", statList);

		compound.putInt("maxPoints", maxPoints);

		return compound;
	}
}
