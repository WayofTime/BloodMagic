package wayoftime.bloodmagic.anointment;

import java.util.Map;

import com.google.common.collect.Maps;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import wayoftime.bloodmagic.core.AnointmentRegistrar;
import wayoftime.bloodmagic.util.Constants;

public class AnointmentHolder
{
	private final Map<Anointment, AnointmentData> anointments;

	public AnointmentHolder(Map<Anointment, AnointmentData> anointments)
	{
		this.anointments = anointments;
	}

	public AnointmentHolder()
	{
		this(Maps.newHashMap());
	}

	public CompoundNBT serialize()
	{
		CompoundNBT compound = new CompoundNBT();
		ListNBT statList = new ListNBT();
		anointments.forEach((k, v) -> {
			CompoundNBT anoint = new CompoundNBT();
			anoint.putString("key", k.getKey().toString());
			anoint.putInt("level", v.getLevel());
			anoint.putInt("damage", v.getDamage());
			anoint.putInt("max_damage", v.getMaxDamage());
			statList.add(anoint);
		});
		compound.put("upgrades", statList);
//
//		compound.putInt("maxPoints", maxPoints);

		return compound;
	}

	public void deserialize(CompoundNBT nbt)
	{
		ListNBT statList = nbt.getList("anointments", 10);
		statList.forEach(tag -> {
			if (!(tag instanceof CompoundNBT))
				return;

			Anointment anoint = AnointmentRegistrar.ANOINTMENT_MAP.getOrDefault(new ResourceLocation(((CompoundNBT) tag).getString("key")), Anointment.DUMMY);
//			LivingUpgrade upgrade = LivingArmorRegistrar.UPGRADE_MAP.getOrDefault(new ResourceLocation(((CompoundNBT) tag).getString("key")), LivingUpgrade.DUMMY);
			if (anoint == Anointment.DUMMY)
				return;
//			double experience = ((CompoundNBT) tag).getDouble("exp");
			AnointmentData data = new AnointmentData(((CompoundNBT) tag).getInt("level"), ((CompoundNBT) tag).getInt("damage"), ((CompoundNBT) tag).getInt("max_damage"));
			anointments.put(anoint, data);
		});
//
//		maxPoints = nbt.getInt("maxPoints");
	}

	public static AnointmentHolder fromNBT(CompoundNBT holderTag)
	{
		AnointmentHolder holder = new AnointmentHolder();
		holder.deserialize(holderTag);
		return holder;
	}

	public static AnointmentHolder fromItemStack(ItemStack stack)
	{
		CompoundNBT nbtTag = stack.getTag();
		if (nbtTag == null)
		{
			return null;
		}

		CompoundNBT holderTag = nbtTag.getCompound(Constants.NBT.ANOINTMENTS);
		if (holderTag != null)
		{
			return fromNBT(holderTag);
		}

		return null;
	}

	public void toItemStack(ItemStack stack)
	{
		CompoundNBT nbtTag = stack.getOrCreateTag();
		CompoundNBT childTag = this.serialize();

		nbtTag.put(Constants.NBT.ANOINTMENTS, childTag);
	}

	public static AnointmentHolder fromPlayer(PlayerEntity player, Hand hand)
	{
		return fromPlayer(player, hand, false);
	}

	public static AnointmentHolder fromPlayer(PlayerEntity player, Hand hand, boolean createNew)
	{
		ItemStack heldItem = player.getHeldItem(hand);

		AnointmentHolder holder = fromItemStack(heldItem);
		return holder == null && createNew ? new AnointmentHolder() : holder;
	}

	public static void toPlayer(PlayerEntity player, Hand hand, AnointmentHolder holder)
	{
		ItemStack heldItem = player.getHeldItem(hand);
		holder.toItemStack(heldItem);
	}

	public static class AnointmentData
	{
		private int level;
		private int damage;
		private int maxDamage;

		public AnointmentData(int level, int damage, int maxDamage)
		{
			this.level = level;
			this.damage = damage;
			this.maxDamage = maxDamage;
		}

		public int getLevel()
		{
			return this.level;
		}

		public int getDamage()
		{
			return this.damage;
		}

		public int getMaxDamage()
		{
			return this.maxDamage;
		}
	}
}
