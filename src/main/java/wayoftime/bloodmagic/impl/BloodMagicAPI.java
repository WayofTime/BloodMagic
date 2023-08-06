package wayoftime.bloodmagic.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

import javax.annotation.Nonnull;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.registries.IForgeRegistryEntry;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.altar.ComponentType;
import wayoftime.bloodmagic.anointment.Anointment;
import wayoftime.bloodmagic.api.IBloodMagicAPI;
import wayoftime.bloodmagic.api.compat.EnumDemonWillType;
import wayoftime.bloodmagic.common.item.BloodOrb;
import wayoftime.bloodmagic.core.living.LivingUpgrade;
import wayoftime.bloodmagic.incense.EnumTranquilityType;
import wayoftime.bloodmagic.incense.IncenseTranquilityRegistry;
import wayoftime.bloodmagic.incense.TranquilityStack;
import wayoftime.bloodmagic.util.BMLog;
import wayoftime.bloodmagic.will.PlayerDemonWillHandler;

public class BloodMagicAPI implements IBloodMagicAPI
{
	public static final BloodMagicAPI INSTANCE = new BloodMagicAPI();

	private final BloodMagicBlacklist blacklist;
	private final BloodMagicRecipeRegistrar recipeRegistrar;
	private final BloodMagicValueManager valueManager;
	private final Multimap<ComponentType, BlockState> altarComponents;
	private final Map<String, Function<Player, NonNullList<ItemStack>>> inventoryProvider;
	private final List<String> activeInventories;

	@Nonnull
	private static final Lazy<ResourceKey<? extends Registry<Anointment>>> ANOINTMENT_REGISTRY_NAME = registryKey(Anointment.class, "anointment");
	@Nonnull
	private static final Lazy<ResourceKey<? extends Registry<BloodOrb>>> BLOOD_ORB_REGISTRY_NAME = registryKey(BloodOrb.class, "bloodorbs");
	@Nonnull
	private static final Lazy<ResourceKey<? extends Registry<LivingUpgrade>>> LIVING_UPGRADE_REGISTRY_NAME = registryKey(LivingUpgrade.class, "upgrades");

	public BloodMagicAPI()
	{
		this.blacklist = new BloodMagicBlacklist();
		this.recipeRegistrar = new BloodMagicRecipeRegistrar();
		this.valueManager = new BloodMagicValueManager();
		this.altarComponents = ArrayListMultimap.create();
		this.inventoryProvider = new HashMap<String, Function<Player, NonNullList<ItemStack>>>();
		this.activeInventories = new ArrayList<String>();
	}

	// Copied from Mekanism. Again.
	@Nonnull
	private static <T extends IForgeRegistryEntry<T>> Lazy<ResourceKey<? extends Registry<T>>> registryKey(@SuppressWarnings("unused") @Nonnull Class<T> compileTimeTypeValidator, @Nonnull String path)
	{
		return Lazy.of(() -> ResourceKey.createRegistryKey(new ResourceLocation(BloodMagic.MODID, path)));
	}

	@Nonnull
	public static ResourceKey<? extends Registry<Anointment>> anointmentRegistryName()
	{
		return ANOINTMENT_REGISTRY_NAME.get();
	}

	@Nonnull
	public static ResourceKey<? extends Registry<BloodOrb>> bloodOrbRegistryName()
	{
		return BLOOD_ORB_REGISTRY_NAME.get();
	}

	@Nonnull
	public static ResourceKey<? extends Registry<LivingUpgrade>> LivingUpgradeRegistryName()
	{
		return LIVING_UPGRADE_REGISTRY_NAME.get();
	}

	@Nonnull
	@Override
	public BloodMagicBlacklist getBlacklist()
	{
		return blacklist;
	}

	@Nonnull
	public BloodMagicRecipeRegistrar getRecipeRegistrar()
	{
		return recipeRegistrar;
	}

//
	@Nonnull
	@Override
	public BloodMagicValueManager getValueManager()
	{
		return valueManager;
	}

	@Nonnull
	public Map<String, Function<Player, NonNullList<ItemStack>>> getInventoryProvider()
	{
		return inventoryProvider;
	}

	public Map<String, Function<Player, NonNullList<ItemStack>>> getActiveInventoryProvider()
	{
		Map<String, Function<Player, NonNullList<ItemStack>>> activeInventoryProvider = new HashMap<String, Function<Player, NonNullList<ItemStack>>>();

		activeInventories.forEach(key -> activeInventoryProvider.put(key, inventoryProvider.get(key)));

		return activeInventoryProvider;
	}

	public void registerActiveInventoryProvider(String inventoryIdentifier)
	{
		activeInventories.add(inventoryIdentifier);
	}

	@Override
	public void registerAltarComponent(@Nonnull BlockState state, @Nonnull String componentType)
	{
		ComponentType component = ComponentType.getType(componentType);

		if (component != null)
		{
			BMLog.API_VERBOSE.info("Registered {} as a {} altar component.", state, componentType);
			altarComponents.put(component, state);
		} else
			BMLog.API.warn("Invalid Altar component type: {}.", componentType);
	}

	@Override
	public void unregisterAltarComponent(@Nonnull BlockState state, @Nonnull String componentType)
	{
		ComponentType component = ComponentType.getType(componentType);

		if (component != null)
		{
			BMLog.API_VERBOSE.info("Unregistered {} from being a {} altar component.", state, componentType);
			altarComponents.remove(component, state);
		} else
			BMLog.API.warn("Invalid Altar component type: {}.", componentType);
	}

	@Override
	public void registerTranquilityHandler(@Nonnull Predicate<BlockState> blockState, @Nonnull String tranquilityType, double value)
	{
		EnumTranquilityType type = EnumTranquilityType.getType(tranquilityType);

		if (type != null)
		{
			IncenseTranquilityRegistry.registerTranquilityHandler((world, pos, block, state) -> blockState.test(state)
					? new TranquilityStack(type, value)
					: null);
		} else
		{
			BMLog.API.warn("Invalid Tranquility type: {}.", tranquilityType);
		}
	}

	@Override
	public void registerInventoryProvider(String inventoryIdentifier, Function<Player, NonNullList<ItemStack>> provider)
	{
		inventoryProvider.put(inventoryIdentifier, provider);
	}

	@Override
	public double getTotalDemonWill(String willType, Player player)
	{
		return PlayerDemonWillHandler.getTotalDemonWill(EnumDemonWillType.getType(willType), player);
	}

	@Nonnull
	public List<BlockState> getComponentStates(ComponentType component)
	{
		return (List<BlockState>) altarComponents.get(component);
	}
}