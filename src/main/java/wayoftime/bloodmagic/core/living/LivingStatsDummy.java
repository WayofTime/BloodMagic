package wayoftime.bloodmagic.core.living;

import java.util.Map;

import com.google.common.collect.Maps;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;

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

	public CompoundNBT serialize()
	{
		CompoundNBT compound = new CompoundNBT();
		ListNBT statList = new ListNBT();
		upgrade_keys.forEach((k, v) -> {
			CompoundNBT upgrade = new CompoundNBT();
			upgrade.putString("key", k.toString());
			upgrade.putDouble("exp", v);
			statList.add(upgrade);
		});
		compound.put("upgrades", statList);

		compound.putInt("maxPoints", maxPoints);

		return compound;
	}
}
