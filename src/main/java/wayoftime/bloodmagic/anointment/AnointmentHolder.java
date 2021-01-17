package wayoftime.bloodmagic.anointment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import wayoftime.bloodmagic.anointment.Anointment.IDamageProvider;
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

	// Returns true if the anointment is applied successfully.
	public boolean applyAnointment(ItemStack stack, Anointment anointment, AnointmentData data)
	{
		if (canApplyAnointment(stack, anointment, data))
		{
			anointments.put(anointment, data);
			anointment.applyAnointment(this, stack, data.getLevel());
			return true;
		}

		return false;
	}

	public boolean canApplyAnointment(ItemStack stack, Anointment anointment, AnointmentData data)
	{
		ResourceLocation key = anointment.getKey();
		for (Anointment containedAnoint : anointments.keySet())
		{
			ResourceLocation containedKey = containedAnoint.getKey();
			if (!anointment.isCompatible(containedKey) || !containedAnoint.isCompatible(key))
			{
				return false;
			}
		}

		if (anointments.containsKey(anointment))
		{
			AnointmentData prevData = anointments.get(anointment);
			int level = prevData.getLevel();
			int remainingDur = prevData.getMaxDamage() - prevData.getDamage();
			if (level < data.getLevel() || (level == data.getLevel() && remainingDur < (data.getMaxDamage() - data.getDamage())))
			{
				return true;
			} else
			{
				return false;
			}
		}

		return true;
	}

	public int getAnointmentLevel(Anointment anointment)
	{
		if (anointments.containsKey(anointment))
		{
			return anointments.get(anointment).getLevel();
		}

		return 0;
	}

	public boolean consumeAnointmentDurabilityOnHit(ItemStack weaponStack, EquipmentSlotType type)
	{
//		System.out.println("Attempting consumption");
		boolean didConsume = false;
		List<Anointment> removedAnointments = new ArrayList<Anointment>();
		for (Entry<Anointment, AnointmentData> entry : anointments.entrySet())
		{
			Anointment annointment = entry.getKey();
			if (annointment.consumeOnAttack())
			{
				AnointmentData data = entry.getValue();
				data.damage(1);
				didConsume = true;
				if (data.isMaxDamage())
				{
					removedAnointments.add(annointment);
				}
			}
		}

		for (Anointment anointment : removedAnointments)
		{
			removeAnointment(weaponStack, type, anointment);
		}

		return didConsume;
	}

	public boolean consumeAnointmentDurabilityOnUseFinish(ItemStack weaponStack, EquipmentSlotType type)
	{
		boolean didConsume = false;
		List<Anointment> removedAnointments = new ArrayList<Anointment>();
		for (Entry<Anointment, AnointmentData> entry : anointments.entrySet())
		{
			Anointment annointment = entry.getKey();
			if (annointment.consumeOnUseFinish())
			{
				AnointmentData data = entry.getValue();
				data.damage(1);
				didConsume = true;
				if (data.isMaxDamage())
				{
					removedAnointments.add(annointment);
				}
			}
		}

		for (Anointment anointment : removedAnointments)
		{
			removeAnointment(weaponStack, type, anointment);
		}

		return didConsume;
	}

	public boolean consumeAnointmentDurabilityOnHarvest(ItemStack weaponStack, EquipmentSlotType type)
	{
		boolean didConsume = false;
		List<Anointment> removedAnointments = new ArrayList<Anointment>();
		for (Entry<Anointment, AnointmentData> entry : anointments.entrySet())
		{
			Anointment annointment = entry.getKey();
			if (annointment.consumeOnHarvest())
			{
				AnointmentData data = entry.getValue();
				data.damage(1);
				didConsume = true;
				if (data.isMaxDamage())
				{
					removedAnointments.add(annointment);
				}
			}
		}

		for (Anointment anointment : removedAnointments)
		{
			removeAnointment(weaponStack, type, anointment);
		}

		return didConsume;
	}

	// Called when the specified anointment is to be removed. Occurs if the
	// anointment runs out of uses or if removed via another source.
	public boolean removeAnointment(ItemStack weaponStack, EquipmentSlotType type, Anointment anointment)
	{
		anointments.remove(anointment);
		anointment.removeAnointment(this, weaponStack, type);
		return true;
	}

	public Map<Anointment, AnointmentData> getAnointments()
	{
		return ImmutableMap.copyOf(anointments);
	}

	public double getAdditionalDamage(PlayerEntity player, ItemStack weapon, double damage, LivingEntity attacked)
	{
		double additionalDamage = 0;
		for (Entry<Anointment, AnointmentData> entry : anointments.entrySet())
		{
			IDamageProvider prov = entry.getKey().getDamageProvider();
			if (prov != null)
			{
				additionalDamage += prov.getAdditionalDamage(player, weapon, damage, this, attacked, entry.getKey(), entry.getValue().getLevel());
			}
		}
		return additionalDamage;
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
		compound.put("anointments", statList);
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

	public static void appendAnointmentTooltip(AnointmentHolder holder, List<ITextComponent> tooltip)
	{
		if (holder != null)
		{
//			System.out.println("Holder is not null. Size: " + holder.getAnointments().size());
//			if (trainable)
//				tooltip.add(new TranslationTextComponent("tooltip.bloodmagic.livingarmour.upgrade.points", stats.getUsedPoints(), stats.getMaxPoints()).mergeStyle(TextFormatting.GOLD));

			holder.getAnointments().forEach((k, v) -> {

//				if (k.getLevel(v.intValue()) <= 0)
//					return;
				boolean sneaking = Screen.hasShiftDown();
//				if (!InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), 340) || k.getNextRequirement(v) == 0)
				if (!sneaking)
					tooltip.add(new TranslationTextComponent("%s %s", new TranslationTextComponent(k.getTranslationKey()), new TranslationTextComponent("enchantment.level." + v.getLevel())));
				else
					tooltip.add(new TranslationTextComponent("%s %s", new TranslationTextComponent(k.getTranslationKey()), (": (" + v.getDamageString() + ")")));
			});
		}
	}
}
