package wayoftime.bloodmagic.altar;

import com.google.common.base.Enums;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RedstoneLampBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.ItemHandlerHelper;
import wayoftime.bloodmagic.api.event.BloodMagicCraftedEvent;
import wayoftime.bloodmagic.block.enums.BloodRuneType;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.common.fluid.BloodMagicFluids;
import wayoftime.bloodmagic.common.item.BloodOrb;
import wayoftime.bloodmagic.common.item.IBindable;
import wayoftime.bloodmagic.common.item.IBloodOrb;
import wayoftime.bloodmagic.common.tile.TileAltar;
import wayoftime.bloodmagic.core.data.Binding;
import wayoftime.bloodmagic.impl.BloodMagicAPI;
import wayoftime.bloodmagic.recipe.RecipeBloodAltar;
import wayoftime.bloodmagic.util.Constants;
import wayoftime.bloodmagic.util.helper.NetworkHelper;

public class BloodAltar// implements IFluidHandler
{

	public boolean isActive;

	protected FluidStack fluidOutput = new FluidStack(BloodMagicFluids.LIFE_ESSENCE_FLUID.get(), 0);
	protected FluidStack fluidInput = new FluidStack(BloodMagicFluids.LIFE_ESSENCE_FLUID.get(), 0);

	protected FluidTank tank = new FluidTank(FluidType.BUCKET_VOLUME);

	private final LazyOptional<IFluidHandler> holder = LazyOptional.of(() -> tank);

	private TileAltar tileAltar;
	private int internalCounter = 0;
	private AltarTier altarTier = AltarTier.ONE;
	private AltarUpgrade upgrade;
	private int capacity = FluidType.BUCKET_VOLUME * 10;
	private FluidStack fluid = new FluidStack(BloodMagicFluids.LIFE_ESSENCE_FLUID.get(), 0);
	private int liquidRequired; // mB
	private boolean canBeFilled;
	private int consumptionRate;
	private int drainRate;
	private float consumptionMultiplier;
	private float efficiencyMultiplier;
	private float sacrificeEfficiencyMultiplier;
	private float selfSacrificeEfficiencyMultiplier;
	private float capacityMultiplier = 1;
	private float orbCapacityMultiplier;
	private float dislocationMultiplier;
	private int accelerationUpgrades;
	private boolean isUpgraded;
	private boolean isResultBlock;
	private int bufferCapacity = FluidType.BUCKET_VOLUME;
	private int progress;
	private int lockdownDuration;
	private int demonBloodDuration;
	private int totalCharge = 0; // TODO save
	private int chargingRate = 0;
	private int chargingFrequency = 0;
	private int maxCharge = 0;
	private int cooldownAfterCrafting = 60;
	private RecipeBloodAltar recipe;
	private AltarTier currentTierDisplayed = AltarTier.ONE;

	public BloodAltar(TileAltar tileAltar)
	{
		this.tileAltar = tileAltar;
	}

	public void readFromNBT(CompoundTag tagCompound)
	{
		if (!tagCompound.contains(Constants.NBT.EMPTY))
		{
			FluidStack fluid = FluidStack.loadFluidStackFromNBT(tagCompound);

			if (fluid != null)
			{
				setMainFluid(new FluidStack(BloodMagicFluids.LIFE_ESSENCE_FLUID.get(), fluid.getAmount()));
//				setMainFluid(fluid);
			} else
			{
//				setMainFluid(new FluidStack(BloodMagicBlocks.LIFE_ESSENCE_FLUID.get(), fluid.getAmount()));
			}

			FluidStack fluidOut = new FluidStack(BloodMagicFluids.LIFE_ESSENCE_FLUID.get(), tagCompound.getInt(Constants.NBT.OUTPUT_AMOUNT));
			setOutputFluid(fluidOut);

			FluidStack fluidIn = new FluidStack(BloodMagicFluids.LIFE_ESSENCE_FLUID.get(), tagCompound.getInt(Constants.NBT.INPUT_AMOUNT));
			setInputFluid(fluidIn);
		}

		internalCounter = tagCompound.getInt("internalCounter");
		altarTier = Enums.getIfPresent(AltarTier.class, tagCompound.getString(Constants.NBT.ALTAR_TIER)).or(AltarTier.ONE);
		isActive = tagCompound.getBoolean(Constants.NBT.ALTAR_ACTIVE);
		liquidRequired = tagCompound.getInt(Constants.NBT.ALTAR_LIQUID_REQ);
		canBeFilled = tagCompound.getBoolean(Constants.NBT.ALTAR_FILLABLE);
		isUpgraded = tagCompound.getBoolean(Constants.NBT.ALTAR_UPGRADED);
		consumptionRate = tagCompound.getInt(Constants.NBT.ALTAR_CONSUMPTION_RATE);
		drainRate = tagCompound.getInt(Constants.NBT.ALTAR_DRAIN_RATE);
		consumptionMultiplier = tagCompound.getFloat(Constants.NBT.ALTAR_CONSUMPTION_MULTIPLIER);
		efficiencyMultiplier = tagCompound.getFloat(Constants.NBT.ALTAR_EFFICIENCY_MULTIPLIER);
		selfSacrificeEfficiencyMultiplier = tagCompound.getFloat(Constants.NBT.ALTAR_SELF_SACRIFICE_MULTIPLIER);
		sacrificeEfficiencyMultiplier = tagCompound.getFloat(Constants.NBT.ALTAR_SACRIFICE_MULTIPLIER);
		capacityMultiplier = tagCompound.getFloat(Constants.NBT.ALTAR_CAPACITY_MULTIPLIER);
		orbCapacityMultiplier = tagCompound.getFloat(Constants.NBT.ALTAR_ORB_CAPACITY_MULTIPLIER);
		dislocationMultiplier = tagCompound.getFloat(Constants.NBT.ALTAR_DISLOCATION_MULTIPLIER);
		capacity = tagCompound.getInt(Constants.NBT.ALTAR_CAPACITY);
		bufferCapacity = tagCompound.getInt(Constants.NBT.ALTAR_BUFFER_CAPACITY);
		progress = tagCompound.getInt(Constants.NBT.ALTAR_PROGRESS);
		isResultBlock = tagCompound.getBoolean(Constants.NBT.ALTAR_IS_RESULT_BLOCK);
		lockdownDuration = tagCompound.getInt(Constants.NBT.ALTAR_LOCKDOWN_DURATION);
		accelerationUpgrades = tagCompound.getInt(Constants.NBT.ALTAR_ACCELERATION_UPGRADES);
		demonBloodDuration = tagCompound.getInt(Constants.NBT.ALTAR_DEMON_BLOOD_DURATION);
		cooldownAfterCrafting = tagCompound.getInt(Constants.NBT.ALTAR_COOLDOWN_AFTER_CRAFTING);
		chargingRate = tagCompound.getInt(Constants.NBT.ALTAR_CHARGE_RATE);
		chargingFrequency = tagCompound.getInt(Constants.NBT.ALTAR_CHARGE_FREQUENCY);
		totalCharge = tagCompound.getInt(Constants.NBT.ALTAR_TOTAL_CHARGE);
		maxCharge = tagCompound.getInt(Constants.NBT.ALTAR_MAX_CHARGE);
		currentTierDisplayed = Enums.getIfPresent(AltarTier.class, tagCompound.getString(Constants.NBT.ALTAR_CURRENT_TIER_DISPLAYED)).or(AltarTier.ONE);
	}

	public void writeToNBT(CompoundTag tagCompound)
	{

		if (fluid != null)
			fluid.writeToNBT(tagCompound);
		else
			tagCompound.putString(Constants.NBT.EMPTY, "");

		if (fluidOutput != null)
			tagCompound.putInt(Constants.NBT.OUTPUT_AMOUNT, fluidOutput.getAmount());

		if (fluidInput != null)
			tagCompound.putInt(Constants.NBT.INPUT_AMOUNT, fluidInput.getAmount());

		tagCompound.putInt("internalCounter", internalCounter);
		tagCompound.putString(Constants.NBT.ALTAR_TIER, altarTier.name());
		tagCompound.putBoolean(Constants.NBT.ALTAR_ACTIVE, isActive);
		tagCompound.putInt(Constants.NBT.ALTAR_LIQUID_REQ, liquidRequired);
		tagCompound.putBoolean(Constants.NBT.ALTAR_FILLABLE, canBeFilled);
		tagCompound.putBoolean(Constants.NBT.ALTAR_UPGRADED, isUpgraded);
		tagCompound.putInt(Constants.NBT.ALTAR_CONSUMPTION_RATE, consumptionRate);
		tagCompound.putInt(Constants.NBT.ALTAR_DRAIN_RATE, drainRate);
		tagCompound.putFloat(Constants.NBT.ALTAR_CONSUMPTION_MULTIPLIER, consumptionMultiplier);
		tagCompound.putFloat(Constants.NBT.ALTAR_EFFICIENCY_MULTIPLIER, efficiencyMultiplier);
		tagCompound.putFloat(Constants.NBT.ALTAR_SACRIFICE_MULTIPLIER, sacrificeEfficiencyMultiplier);
		tagCompound.putFloat(Constants.NBT.ALTAR_SELF_SACRIFICE_MULTIPLIER, selfSacrificeEfficiencyMultiplier);
		tagCompound.putBoolean(Constants.NBT.ALTAR_IS_RESULT_BLOCK, isResultBlock);
		tagCompound.putFloat(Constants.NBT.ALTAR_CAPACITY_MULTIPLIER, capacityMultiplier);
		tagCompound.putFloat(Constants.NBT.ALTAR_ORB_CAPACITY_MULTIPLIER, orbCapacityMultiplier);
		tagCompound.putFloat(Constants.NBT.ALTAR_DISLOCATION_MULTIPLIER, dislocationMultiplier);
		tagCompound.putInt(Constants.NBT.ALTAR_CAPACITY, capacity);
		tagCompound.putInt(Constants.NBT.ALTAR_PROGRESS, progress);
		tagCompound.putInt(Constants.NBT.ALTAR_BUFFER_CAPACITY, bufferCapacity);
		tagCompound.putInt(Constants.NBT.ALTAR_LOCKDOWN_DURATION, lockdownDuration);
		tagCompound.putInt(Constants.NBT.ALTAR_ACCELERATION_UPGRADES, accelerationUpgrades);
		tagCompound.putInt(Constants.NBT.ALTAR_DEMON_BLOOD_DURATION, demonBloodDuration);
		tagCompound.putInt(Constants.NBT.ALTAR_COOLDOWN_AFTER_CRAFTING, cooldownAfterCrafting);
		tagCompound.putInt(Constants.NBT.ALTAR_CHARGE_RATE, chargingRate);
		tagCompound.putInt(Constants.NBT.ALTAR_CHARGE_FREQUENCY, chargingFrequency);
		tagCompound.putInt(Constants.NBT.ALTAR_TOTAL_CHARGE, totalCharge);
		tagCompound.putInt(Constants.NBT.ALTAR_MAX_CHARGE, maxCharge);
		tagCompound.putString(Constants.NBT.ALTAR_CURRENT_TIER_DISPLAYED, currentTierDisplayed.name());
	}

	public void startCycle()
	{
		if (tileAltar.getLevel() != null)
			tileAltar.getLevel().sendBlockUpdated(tileAltar.getBlockPos(), tileAltar.getLevel().getBlockState(tileAltar.getBlockPos()), tileAltar.getLevel().getBlockState(tileAltar.getBlockPos()), 3);

		checkTier();

		// Temporary thing to test the recipes.
//		fluid.setAmount(10000);
//		this.setMainFluid(new FluidStack(BloodMagicBlocks.LIFE_ESSENCE_FLUID.get(), 10000));

		if ((fluid == null || fluid.getAmount() <= 0) && totalCharge <= 0)
			return;

		if (!isActive)
			progress = 0;

		ItemStack input = tileAltar.getItem(0);

		if (!input.isEmpty())
		{
			// Do recipes
			RecipeBloodAltar recipe = BloodMagicAPI.INSTANCE.getRecipeRegistrar().getBloodAltar(tileAltar.getLevel(), input);
			if (recipe != null)
			{
				if (recipe.getMinimumTier() <= altarTier.ordinal())
				{
					this.isActive = true;
					this.recipe = recipe;
					this.liquidRequired = recipe.getSyphon();
					this.consumptionRate = recipe.getConsumeRate();
					this.drainRate = recipe.getDrainRate();
					this.canBeFilled = false;
					return;
				}
			} else if (input.getItem() instanceof IBloodOrb)
			{
				this.isActive = true;
				this.canBeFilled = true;
				return;
			}
		}

		isActive = false;
	}

	public void update()
	{
//		World world = tileAltar.getWorld();
//
//		RecipeBloodAltar recipe = BloodMagicAPI.INSTANCE.getRecipeRegistrar().getBloodAltar(world, new ItemStack(Items.DIAMOND));
//
//		if (recipe != null)
//		{
//			System.out.println("Found a recipe!");
//		}
//
//		List<RecipeBloodAltar> altarRecipes = world.getRecipeManager().getRecipesForType(BloodMagicRecipeType.ALTAR);
//
//		System.out.println("There are currently " + altarRecipes.size() + " Altar Recipes loaded.");
//
//		this.fluidInput = new FluidStack(BloodMagicBlocks.LIFE_ESSENCE_FLUID.get(), 1000);

//		System.out.println(this.fluidOutput.getAmount());

		Level world = tileAltar.getLevel();
		BlockPos pos = tileAltar.getBlockPos();

		if (world.isClientSide)
			return;

		// Used instead of the world time for checks that do not happen every tick
		internalCounter++;

		if (lockdownDuration > 0)
			lockdownDuration--;

		if (internalCounter % 20 == 0)
		{
			for (Direction facing : Direction.values())
			{
				BlockPos newPos = pos.relative(facing);
				BlockState block = world.getBlockState(newPos);
				block.getBlock().onNeighborChange(block, world, newPos, pos);
			}
		}
		if (internalCounter % (Math.max(20 - this.accelerationUpgrades, 1)) == 0)
		{
			int syphonMax = (int) (20 * this.dislocationMultiplier);
			int fluidInputted;
			int fluidOutputted;
			fluidInputted = Math.min(syphonMax, -this.fluid.getAmount() + capacity);
			fluidInputted = Math.min(this.fluidInput.getAmount(), fluidInputted);
			this.fluid.setAmount(this.fluid.getAmount() + fluidInputted);
			this.fluidInput.setAmount(this.fluidInput.getAmount() - fluidInputted);
			fluidOutputted = Math.min(syphonMax, this.bufferCapacity - this.fluidOutput.getAmount());
			fluidOutputted = Math.min(this.fluid.getAmount(), fluidOutputted);
			this.fluidOutput.setAmount(this.fluidOutput.getAmount() + fluidOutputted);
			this.fluid.setAmount(this.fluid.getAmount() - fluidOutputted);
			tileAltar.getLevel().sendBlockUpdated(tileAltar.getBlockPos(), tileAltar.getLevel().getBlockState(tileAltar.getBlockPos()), tileAltar.getLevel().getBlockState(tileAltar.getBlockPos()), 3);
		}

		if (internalCounter % this.getChargingFrequency() == 0 && !this.isActive)
		{
			int chargeInputted = Math.min(chargingRate, this.fluid.getAmount());
			chargeInputted = Math.min(chargeInputted, maxCharge - totalCharge);
			totalCharge += chargeInputted;
			this.fluid.setAmount(this.fluid.getAmount() - chargeInputted);
			tileAltar.getLevel().sendBlockUpdated(tileAltar.getBlockPos(), tileAltar.getLevel().getBlockState(tileAltar.getBlockPos()), tileAltar.getLevel().getBlockState(tileAltar.getBlockPos()), 3);
		}

		if (internalCounter % 100 == 0 && (this.isActive || this.cooldownAfterCrafting <= 0))
			startCycle();

		updateAltar();
	}

	private void updateAltar()
	{
//		System.out.println("Updating altar.");
		if (tileAltar.getOutputState())
			tileAltar.setOutputState(false);

		if (!isActive)
		{
			if (cooldownAfterCrafting > 0)
			{
				cooldownAfterCrafting--;
				if (cooldownAfterCrafting <= 0)
				{
					startCycle();
				}
			}

			return;
		}

		if (!canBeFilled && recipe == null)
		{
			startCycle();
			return;
		}

		ItemStack input = tileAltar.getItem(0);

		if (input.isEmpty())
			return;

		Level world = tileAltar.getLevel();
		BlockPos pos = tileAltar.getBlockPos();

		if (world.isClientSide)
			return;

		if (!canBeFilled)
		{
			boolean hasOperated = false;
			int stackSize = input.getCount();

			if (totalCharge > 0)
			{
				int chargeDrained = Math.min(liquidRequired * stackSize - progress, totalCharge);

				totalCharge -= chargeDrained;
				progress += chargeDrained;
				hasOperated = true;
			}
			if (fluid != null && fluid.getAmount() >= 1)
			{
//				int liquidDrained = Math.min((int) (altarTier.ordinal() >= 1
//						? consumptionRate * (1 + consumptionMultiplier)
//						: consumptionRate), fluid.getAmount());
				int liquidDrained = Math.min((int) (consumptionRate * (1 + consumptionMultiplier)), fluid.getAmount());

				if (liquidDrained > (liquidRequired * stackSize - progress))
					liquidDrained = liquidRequired * stackSize - progress;

				fluid.setAmount(fluid.getAmount() - liquidDrained);
				progress += liquidDrained;

				hasOperated = true;

				if (internalCounter % 4 == 0 && world instanceof ServerLevel)
				{
					ServerLevel server = (ServerLevel) world;
//					server.spawnParticle(ParticleTypes.SPLASH, (double) pos.getX()
//							+ worldIn.rand.nextDouble(), (double) (pos.getY() + 1), (double) pos.getZ()
//									+ worldIn.rand.nextDouble(), 1, 0.0D, 0.0D, 0.0D, 1.0D);
					server.sendParticles(DustParticleOptions.REDSTONE, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, 1, 0.2, 0.0, 0.2, 0.0);
				}

			} else if (!hasOperated && progress > 0)
			{
				progress -= (int) (efficiencyMultiplier * drainRate);

				if (progress < 0)
					progress = 0;

				if (internalCounter % 2 == 0 && world instanceof ServerLevel)
				{
					ServerLevel server = (ServerLevel) world;
					server.sendParticles(ParticleTypes.LARGE_SMOKE, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, 1, 0.1, 0, 0.1, 0);
				}
			}

			if (hasOperated)
			{
				if (progress >= liquidRequired * stackSize)
				{
					ItemStack result = ItemHandlerHelper.copyStackWithSize(recipe.getOutput(), stackSize);

					BloodMagicCraftedEvent.Altar event = new BloodMagicCraftedEvent.Altar(result, input.copy());
					MinecraftForge.EVENT_BUS.post(event);
					tileAltar.setItem(0, event.getOutput());
					if (tileAltar.getLevel().getBlockState(tileAltar.getBlockPos().below()).getBlock() instanceof RedstoneLampBlock)
						tileAltar.setOutputState(true);

					progress = 0;

					if (world instanceof ServerLevel)
					{
						ServerLevel server = (ServerLevel) world;
						server.sendParticles(DustParticleOptions.REDSTONE, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, 40, 0.3, 0, 0.3, 0);
					}

					this.cooldownAfterCrafting = 30;
					this.isActive = false;
				}
			}

		} else
		{
			ItemStack contained = tileAltar.getItem(0);

			if (contained.isEmpty() || !(contained.getItem() instanceof IBloodOrb) || !(contained.getItem() instanceof IBindable))
				return;

			BloodOrb orb = ((IBloodOrb) contained.getItem()).getOrb(contained);
			Binding binding = ((IBindable) contained.getItem()).getBinding(contained);

			if (binding == null || orb == null)
				return;

			if (fluid != null && fluid.getAmount() >= 1)
			{
//				int liquidDrained = Math.min((int) (altarTier.ordinal() >= 2
//						? orb.getFillRate() * (1 + consumptionMultiplier)
//						: orb.getFillRate()), fluid.getAmount());
				int liquidDrained = Math.min((int) (orb.getFillRate() * (1 + consumptionMultiplier)), fluid.getAmount());
				int drain = NetworkHelper.getSoulNetwork(binding).add(liquidDrained, (int) (orb.getCapacity() * this.orbCapacityMultiplier));
				fluid.setAmount(fluid.getAmount() - drain);

				if (drain > 0 && internalCounter % 4 == 0 && world instanceof ServerLevel)
				{
					ServerLevel server = (ServerLevel) world;
					server.sendParticles(ParticleTypes.WITCH, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, 1, 0, 0, 0, 0.001);
				}
			}
		}

		tileAltar.getLevel().sendBlockUpdated(tileAltar.getBlockPos(), tileAltar.getLevel().getBlockState(tileAltar.getBlockPos()), tileAltar.getLevel().getBlockState(tileAltar.getBlockPos()), 3);
	}

	public void checkTier()
	{
		AltarTier tier = AltarUtil.getTier(tileAltar.getLevel(), tileAltar.getBlockPos());
		this.altarTier = tier;

		upgrade = AltarUtil.getUpgrades(tileAltar.getLevel(), tileAltar.getBlockPos(), tier);

		if (tier.equals(currentTierDisplayed))
			currentTierDisplayed = AltarTier.ONE;

		if (tier.equals(AltarTier.ONE))
		{
			upgrade = null;
			isUpgraded = false;
			this.consumptionMultiplier = 0;
			this.efficiencyMultiplier = 1;
			this.sacrificeEfficiencyMultiplier = 0;
			this.selfSacrificeEfficiencyMultiplier = 0;
			this.capacityMultiplier = 1;
			this.orbCapacityMultiplier = 1;
			this.dislocationMultiplier = 1;
			this.accelerationUpgrades = 0;
			this.chargingFrequency = 20;
			this.chargingRate = 0;
			this.maxCharge = 0;
			this.totalCharge = 0;
			return;
		} else if (!tier.equals(AltarTier.ONE))
		{
			this.isUpgraded = true;
			this.accelerationUpgrades = upgrade.getLevel(BloodRuneType.ACCELERATION);
			this.consumptionMultiplier = (float) (0.20 * upgrade.getLevel(BloodRuneType.SPEED));
			this.efficiencyMultiplier = (float) Math.pow(0.85, upgrade.getLevel(BloodRuneType.EFFICIENCY));
			this.sacrificeEfficiencyMultiplier = (float) (0.10 * upgrade.getLevel(BloodRuneType.SACRIFICE));
			this.selfSacrificeEfficiencyMultiplier = (float) (0.10 * upgrade.getLevel(BloodRuneType.SELF_SACRIFICE));
			int cap = upgrade.getLevel(BloodRuneType.CAPACITY);
			int cap_aug = upgrade.getLevel(BloodRuneType.AUGMENTED_CAPACITY);
//			this.capacityMultiplier = (float) ((1 + 0.20 * cap) * Math.pow(1.1, cap_aug * Math.pow(0.99, Math.abs(cap_aug - cap))));
			this.capacityMultiplier = (float) ((1 + 0.20 * cap) * Math.pow(1.075, cap_aug));
			this.dislocationMultiplier = (float) (Math.pow(1.2, upgrade.getLevel(BloodRuneType.DISPLACEMENT)));
			this.orbCapacityMultiplier = (float) (1 + 0.02 * upgrade.getLevel(BloodRuneType.ORB));
			this.chargingFrequency = Math.max(20 - accelerationUpgrades, 1);
			this.chargingRate = (int) (10 * upgrade.getLevel(BloodRuneType.CHARGING) * (1 + consumptionMultiplier / 2));
			this.maxCharge = (int) (FluidType.BUCKET_VOLUME * Math.max(0.5 * capacityMultiplier, 1) * upgrade.getLevel(BloodRuneType.CHARGING));
		}

		this.capacity = (int) (FluidType.BUCKET_VOLUME * 10 * capacityMultiplier);
		this.bufferCapacity = (int) (FluidType.BUCKET_VOLUME * 1 * capacityMultiplier);

		if (this.fluid.getAmount() > this.capacity)
			this.fluid.setAmount(this.capacity);
		if (this.fluidOutput.getAmount() > this.bufferCapacity)
			this.fluidOutput.setAmount(this.bufferCapacity);
		if (this.fluidInput.getAmount() > this.bufferCapacity)
			this.fluidInput.setAmount(this.bufferCapacity);
		if (this.totalCharge > this.maxCharge)
			this.totalCharge = this.maxCharge;

		tileAltar.getLevel().sendBlockUpdated(tileAltar.getBlockPos(), tileAltar.getLevel().getBlockState(tileAltar.getBlockPos()), tileAltar.getLevel().getBlockState(tileAltar.getBlockPos()), 3);
	}

	public int fillMainTank(int amount)
	{
		int filledAmount = Math.min(capacity - fluid.getAmount(), amount);
		fluid.setAmount(fluid.getAmount() + filledAmount);

		return filledAmount;
	}

	public void sacrificialDaggerCall(int amount, boolean isSacrifice)
	{
		if (this.lockdownDuration > 0)
		{
			int amt = (int) Math.min(bufferCapacity - fluidInput.getAmount(), (isSacrifice
					? 1 + sacrificeEfficiencyMultiplier
					: 1 + selfSacrificeEfficiencyMultiplier) * amount);
			fluidInput.setAmount(fluidInput.getAmount() + amt);
		} else
		{
			fluid.setAmount((int) (fluid.getAmount() + Math.min(capacity - fluid.getAmount(), (isSacrifice
					? 1 + sacrificeEfficiencyMultiplier
					: 1 + selfSacrificeEfficiencyMultiplier) * amount)));
		}
	}

	public void setMainFluid(FluidStack fluid)
	{
		this.fluid = fluid;
	}

	public void setOutputFluid(FluidStack fluid)
	{
		this.fluidOutput = fluid;
	}

	public void setInputFluid(FluidStack fluid)
	{
		this.fluidInput = fluid;
	}

	public AltarUpgrade getUpgrade()
	{
		return upgrade;
	}

	public void setUpgrade(AltarUpgrade upgrade)
	{
		this.upgrade = upgrade;
	}

	public int getCapacity()
	{
		return capacity;
	}

	public FluidStack getFluid()
	{
		return fluid;
	}

	public int getFluidAmount()
	{
		return fluid.getAmount();
	}

	public int getCurrentBlood()
	{
		return getFluidAmount();
	}

	public AltarTier getTier()
	{
		return altarTier;
	}

	public void setTier(AltarTier tier)
	{
		this.altarTier = tier;
	}

	public int getProgress()
	{
		return progress;
	}

	public float getSacrificeMultiplier()
	{
		return sacrificeEfficiencyMultiplier;
	}

	public float getSelfSacrificeMultiplier()
	{
		return selfSacrificeEfficiencyMultiplier;
	}

	public float getOrbMultiplier()
	{
		return orbCapacityMultiplier;
	}

	public float getDislocationMultiplier()
	{
		return dislocationMultiplier;
	}

	public float getConsumptionMultiplier()
	{
		return consumptionMultiplier;
	}

	public float getConsumptionRate()
	{
		return consumptionRate;
	}

	public int getLiquidRequired()
	{
		return liquidRequired;
	}

	public int getBufferCapacity()
	{
		return bufferCapacity;
	}

	public boolean setCurrentTierDisplayed(AltarTier altarTier)
	{
		if (currentTierDisplayed == altarTier)
			return false;
		else
			currentTierDisplayed = altarTier;
		return true;
	}

	public void addToDemonBloodDuration(int dur)
	{
		this.demonBloodDuration += dur;
	}

	public boolean hasDemonBlood()
	{
		return this.demonBloodDuration > 0;
	}

	public void decrementDemonBlood()
	{
		this.demonBloodDuration = Math.max(0, this.demonBloodDuration - 1);
	}

	public void setActive()
	{
//		if (tileAltar.getStackInSlot(0).isEmpty())
//		{
//			isActive = false;
//		}
	}

	public boolean isActive()
	{
		return isActive;
	}

	public void requestPauseAfterCrafting(int amount)
	{
		if (this.isActive)
		{
			this.cooldownAfterCrafting = amount;
		}
	}

	public int getChargingRate()
	{
		return chargingRate;
	}

	public int getTotalCharge()
	{
		return totalCharge;
	}

	public int getChargingFrequency()
	{
		return chargingFrequency == 0 ? 1 : chargingFrequency;
	}

	public int fill(FluidStack resource, boolean doFill)
	{
		if (resource == null || resource.getFluid() != BloodMagicFluids.LIFE_ESSENCE_FLUID.get())
		{
			return 0;
		}

		if (!doFill)
		{
			if (fluidInput == null || fluidInput.isEmpty())
			{
				return Math.min(bufferCapacity, resource.getAmount());
			}

			if (!fluidInput.isFluidEqual(resource))
			{
				return 0;
			}

			return Math.min(bufferCapacity - fluidInput.getAmount(), resource.getAmount());
		}

		if (fluidInput == null || fluidInput.isEmpty())
		{
			fluidInput = new FluidStack(resource, Math.min(bufferCapacity, resource.getAmount()));

			return fluidInput.getAmount();
		}

		if (!fluidInput.isFluidEqual(resource))
		{
			return 0;
		}
		int filled = bufferCapacity - fluidInput.getAmount();

		if (resource.getAmount() < filled)
		{
			fluidInput.setAmount(fluidInput.getAmount() + resource.getAmount());
			filled = resource.getAmount();
		} else
		{
			fluidInput.setAmount(bufferCapacity);
		}

		return filled;
	}

	public FluidStack drain(FluidStack resource, boolean doDrain)
	{
		if (resource == null || !resource.isFluidEqual(fluidOutput))
		{
			return FluidStack.EMPTY;
		}
		return drain(resource.getAmount(), doDrain);
	}

	public FluidStack drain(int maxDrain, boolean doDrain)
	{
		if (fluidOutput == null)
		{
			return FluidStack.EMPTY;
		}

		int drained = maxDrain;
		if (fluidOutput.getAmount() < drained)
		{
			drained = fluidOutput.getAmount();
		}

		FluidStack stack = new FluidStack(fluidOutput, drained);
		if (doDrain)
		{
			fluidOutput.setAmount(fluidOutput.getAmount() - drained);
		}
		return stack;
	}

//	@Override
//	public IFluidTankProperties[] getTankProperties()
//	{
//		return new IFluidTankProperties[]
//		{ new FluidTankPropertiesWrapper(new FluidTank(fluid, capacity)) };
//	}

	public AltarTier getCurrentTierDisplayed()
	{
		return currentTierDisplayed;
	}

	public int getAnalogSignalStrength(int redstoneMode)
	{
		switch (redstoneMode)
		{
		case 0:
			return getCurrentBlood() * 15 / getCapacity();

		case 1:
			ItemStack contained = tileAltar.getItem(0);

			if (contained.isEmpty() || !(contained.getItem() instanceof IBloodOrb) || !(contained.getItem() instanceof IBindable))
				return 0;

			BloodOrb orb = ((IBloodOrb) contained.getItem()).getOrb(contained);
			Binding binding = ((IBindable) contained.getItem()).getBinding(contained);

			if (binding == null || orb == null)
				return 0;

			return NetworkHelper.getSoulNetwork(binding).getCurrentEssence() * 15 / orb.getCapacity();

		default:
			return 0;
		}
	}

	public static class VariableSizeFluidHandler implements IFluidHandler
	{
		BloodAltar altar;

		public VariableSizeFluidHandler(BloodAltar altar)
		{
			this.altar = altar;
		}

		@Override
		public int getTanks()
		{
			// TODO Auto-generated method stub
			return 1;
		}

		@Override
		public FluidStack getFluidInTank(int tank)
		{
			return altar.fluid;
		}

		@Override
		public int getTankCapacity(int tank)
		{
			return altar.getCapacity();
		}

		@Override
		public boolean isFluidValid(int tank, FluidStack stack)
		{
			return stack.getFluid() == BloodMagicFluids.LIFE_ESSENCE_FLUID.get();
		}

		@Override
		public int fill(FluidStack resource, FluidAction action)
		{
			return altar.fill(resource, action == FluidAction.EXECUTE);
		}

		@Override
		public FluidStack drain(FluidStack resource, FluidAction action)
		{
			return altar.drain(resource, action == FluidAction.EXECUTE);
		}

		@Override
		public FluidStack drain(int maxDrain, FluidAction action)
		{
			return altar.drain(maxDrain, action == FluidAction.EXECUTE);
		}
	}
}
