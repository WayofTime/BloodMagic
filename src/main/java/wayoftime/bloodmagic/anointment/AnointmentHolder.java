package wayoftime.bloodmagic.anointment;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import wayoftime.bloodmagic.anointment.Anointment.IDamageProvider;
import wayoftime.bloodmagic.core.AnointmentRegistrar;
import wayoftime.bloodmagic.util.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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

	public boolean isEmpty()
	{
		return anointments.isEmpty();
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

	public boolean consumeAnointmentDurabilityOnHit(ItemStack weaponStack, EquipmentSlot type, LivingEntity user)
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
			removeAnointment(weaponStack, type, anointment, user);
		}

		return didConsume;
	}

	public boolean consumeAnointmentDurabilityOnUseFinish(ItemStack weaponStack, EquipmentSlot type, LivingEntity user)
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
			removeAnointment(weaponStack, type, anointment, user);
		}

		return didConsume;
	}

	public boolean consumeAnointmentDurabilityOnHarvest(ItemStack weaponStack, EquipmentSlot type, LivingEntity user)
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
			removeAnointment(weaponStack, type, anointment, user);
		}

		return didConsume;
	}

	public boolean consumeAnointmentDurability(ItemStack stack, EquipmentSlot type, Anointment anointment, LivingEntity user)
	{
		if (anointments.containsKey(anointment))
		{
			AnointmentData data = anointments.get(anointment);
			data.damage(1);
			if (data.isMaxDamage())
			{
				removeAnointment(stack, type, anointment, user);
			}

			return true;
		}

		return false;
	}

	// Called when the specified anointment is to be removed. Occurs if the
	// anointment runs out of uses or if removed via another source.
	public boolean removeAnointment(ItemStack weaponStack, EquipmentSlot type, Anointment anointment, LivingEntity user)
	{
		anointments.remove(anointment);
		anointment.removeAnointment(this, weaponStack, type);

		SoundEvent soundevent = SoundEvents.SPLASH_POTION_BREAK;
		user.level().playSound(null, user.blockPosition(), soundevent, SoundSource.BLOCKS, 1.0F, 1.0F);

		if (user.level() instanceof ServerLevel server)
		{
			server.sendParticles(ParticleTypes.LARGE_SMOKE, user.getX(), user.getY() + 1, user.getZ(), 16, 0.3, 0, 0.3, 0);
		}

		return true;
	}

	public Map<Anointment, AnointmentData> getAnointments()
	{
		return ImmutableMap.copyOf(anointments);
	}

	public double getAdditionalDamage(Player player, ItemStack weapon, double damage, LivingEntity attacked)
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

	public CompoundTag serialize()
	{
		CompoundTag compound = new CompoundTag();
		ListTag statList = new ListTag();
		anointments.forEach((k, v) -> {
			CompoundTag anoint = new CompoundTag();
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

	public void deserialize(CompoundTag nbt)
	{
		ListTag statList = nbt.getList("anointments", 10);
		statList.forEach(tag -> {
			if (!(tag instanceof CompoundTag))
				return;
			Anointment anoint = AnointmentRegistrar.ANOINTMENT_MAP.getOrDefault(new ResourceLocation(((CompoundTag) tag).getString("key")), Anointment.DUMMY);
//			LivingUpgrade upgrade = LivingArmorRegistrar.UPGRADE_MAP.getOrDefault(new ResourceLocation(((CompoundNBT) tag).getString("key")), LivingUpgrade.DUMMY);
			if (anoint == Anointment.DUMMY)
				return;
//			double experience = ((CompoundNBT) tag).getDouble("exp");
			AnointmentData data = new AnointmentData(((CompoundTag) tag).getInt("level"), ((CompoundTag) tag).getInt("damage"), ((CompoundTag) tag).getInt("max_damage"));
			anointments.put(anoint, data);
		});
//
//		maxPoints = nbt.getInt("maxPoints");
	}

	public static AnointmentHolder fromNBT(CompoundTag holderTag)
	{
		AnointmentHolder holder = new AnointmentHolder();
		holder.deserialize(holderTag);
		return holder;
	}

	public static AnointmentHolder fromItemStack(ItemStack stack)
	{
		CompoundTag nbtTag = stack.getTag();
		if (nbtTag == null)
		{
			return null;
		}

		CompoundTag holderTag = nbtTag.getCompound(Constants.NBT.ANOINTMENTS);
		if (holderTag != null)
		{
			return fromNBT(holderTag);
		}

		return null;
	}

	public void toItemStack(ItemStack stack)
	{
		CompoundTag nbtTag = stack.getOrCreateTag();
		CompoundTag childTag = this.serialize();

		nbtTag.put(Constants.NBT.ANOINTMENTS, childTag);
	}

	public static AnointmentHolder fromPlayer(Player player, InteractionHand hand)
	{
		return fromPlayer(player, hand, false);
	}

	public static AnointmentHolder fromPlayer(Player player, InteractionHand hand, boolean createNew)
	{
		ItemStack heldItem = player.getItemInHand(hand);

		AnointmentHolder holder = fromItemStack(heldItem);
		return holder == null && createNew ? new AnointmentHolder() : holder;
	}

	public static void toPlayer(Player player, InteractionHand hand, AnointmentHolder holder)
	{
		ItemStack heldItem = player.getItemInHand(hand);
		holder.toItemStack(heldItem);
	}

	public static void appendAnointmentTooltip(AnointmentHolder holder, List<Component> tooltip)
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
					tooltip.add(Component.translatable("%s %s", Component.translatable(k.getTranslationKey()), Component.translatable("enchantment.level." + v.getLevel())));
				else
					tooltip.add(Component.translatable("%s %s", Component.translatable(k.getTranslationKey()), (": (" + v.getDamageString() + ")")));
			});
		}
	}
}
