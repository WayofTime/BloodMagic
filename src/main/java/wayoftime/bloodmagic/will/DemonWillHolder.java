package wayoftime.bloodmagic.will;

import java.util.HashMap;
import java.util.Map.Entry;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wayoftime.bloodmagic.api.compat.EnumDemonWillType;

public class DemonWillHolder implements INBTSerializable<CompoundTag>
{
	public HashMap<EnumDemonWillType, Double> willMap = new HashMap<>();

	public double addWill(EnumDemonWillType type, double amount, double max)
	{
		double current = 0;
		if (willMap.containsKey(type))
		{
			current = willMap.get(type);
		}

		double added = Math.min(max - current, amount);
		addWill(type, amount);

		return added;
	}

	public void addWill(EnumDemonWillType type, double amount)
	{
		if (willMap.containsKey(type))
		{
			willMap.put(type, amount + willMap.get(type));
			onContentsChanged();
		} else
		{
			willMap.put(type, amount);
			onContentsChanged();
		}
	}

	public double drainWill(EnumDemonWillType type, double amount)
	{
		if (willMap.containsKey(type))
		{
			double current = willMap.get(type);
			double reduced = Math.min(current, amount);

			if (reduced >= current)
			{
				willMap.remove(type);
				onContentsChanged();
			} else
			{
				willMap.put(type, current - reduced);
				onContentsChanged();
			}

			return reduced;
		}

		return 0;
	}

	public double getWill(EnumDemonWillType type)
	{
		if (willMap.containsKey(type))
		{
			return willMap.get(type);
		}

		return 0;
	}

	@Override
	public CompoundTag serializeNBT() {
		CompoundTag willTag = new CompoundTag();
		for (Entry<EnumDemonWillType, Double> entry : willMap.entrySet())
		{
			willTag.putDouble("EnumWill" + entry.getKey().name(), entry.getValue());
		}
		return willTag;
	}

	public void writeToNBT(CompoundTag tag, String key)
	{
		tag.put(key, serializeNBT());
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		willMap.clear();

		for (EnumDemonWillType type : EnumDemonWillType.values())
		{
			double amount = nbt.getDouble("EnumWill" + type.name());
			if (amount > 0)
			{
				willMap.put(type, amount);
			}
		}
	}

	public void readFromNBT(CompoundTag tag, String key)
	{
		deserializeNBT(tag.getCompound(key));
	}

	public void onContentsChanged() {}
}
