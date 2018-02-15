package WayofTime.bloodmagic.altar;

import WayofTime.bloodmagic.api.event.BloodMagicCraftedEvent;
import WayofTime.bloodmagic.api.impl.BloodMagicAPI;
import WayofTime.bloodmagic.api.impl.recipe.RecipeBloodAltar;
import WayofTime.bloodmagic.apibutnotreally.BlockStack;
import WayofTime.bloodmagic.apibutnotreally.Constants;
import WayofTime.bloodmagic.apibutnotreally.altar.*;
import WayofTime.bloodmagic.apibutnotreally.orb.BloodOrb;
import WayofTime.bloodmagic.apibutnotreally.orb.IBloodOrb;
import WayofTime.bloodmagic.apibutnotreally.util.helper.NetworkHelper;
import WayofTime.bloodmagic.block.BlockBloodRune;
import WayofTime.bloodmagic.block.BlockLifeEssence;
import WayofTime.bloodmagic.tile.TileAltar;
import com.google.common.base.Enums;
import com.google.common.base.Strings;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.FluidTankPropertiesWrapper;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class BloodAltar implements IFluidHandler {
    static {
        EnumAltarTier.ONE.buildComponents();
        EnumAltarTier.TWO.buildComponents();
        EnumAltarTier.THREE.buildComponents();
        EnumAltarTier.FOUR.buildComponents();
        EnumAltarTier.FIVE.buildComponents();
        EnumAltarTier.SIX.buildComponents();
    }

    public boolean isActive;
    protected FluidStack fluidOutput = new FluidStack(BlockLifeEssence.getLifeEssence(), 0);
    protected FluidStack fluidInput = new FluidStack(BlockLifeEssence.getLifeEssence(), 0);
    private TileAltar tileAltar;
    private int internalCounter = 0;
    private EnumAltarTier altarTier = EnumAltarTier.ONE;
    private AltarUpgrade upgrade;
    private int capacity = Fluid.BUCKET_VOLUME * 10;
    private FluidStack fluid = new FluidStack(BlockLifeEssence.getLifeEssence(), 0);
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
    private int bufferCapacity = Fluid.BUCKET_VOLUME;
    private int progress;
    private int lockdownDuration;
    private int demonBloodDuration;
    private int totalCharge = 0; //TODO save
    private int chargingRate = 0;
    private int chargingFrequency = 0;
    private int maxCharge = 0;
    private int cooldownAfterCrafting = 60;
    private RecipeBloodAltar recipe;
    private EnumAltarTier currentTierDisplayed = EnumAltarTier.ONE;

    public BloodAltar(TileAltar tileAltar) {
        this.tileAltar = tileAltar;
    }

    public void readFromNBT(NBTTagCompound tagCompound) {
        if (!tagCompound.hasKey(Constants.NBT.EMPTY)) {
            FluidStack fluid = FluidStack.loadFluidStackFromNBT(tagCompound);

            if (fluid != null)
                setMainFluid(fluid);

            FluidStack fluidOut = new FluidStack(BlockLifeEssence.getLifeEssence(), tagCompound.getInteger(Constants.NBT.OUTPUT_AMOUNT));
            setOutputFluid(fluidOut);

            FluidStack fluidIn = new FluidStack(BlockLifeEssence.getLifeEssence(), tagCompound.getInteger(Constants.NBT.INPUT_AMOUNT));
            setInputFluid(fluidIn);
        }

        internalCounter = tagCompound.getInteger("internalCounter");
        altarTier = Enums.getIfPresent(EnumAltarTier.class, tagCompound.getString(Constants.NBT.ALTAR_TIER)).or(EnumAltarTier.ONE);
        isActive = tagCompound.getBoolean(Constants.NBT.ALTAR_ACTIVE);
        liquidRequired = tagCompound.getInteger(Constants.NBT.ALTAR_LIQUID_REQ);
        canBeFilled = tagCompound.getBoolean(Constants.NBT.ALTAR_FILLABLE);
        isUpgraded = tagCompound.getBoolean(Constants.NBT.ALTAR_UPGRADED);
        consumptionRate = tagCompound.getInteger(Constants.NBT.ALTAR_CONSUMPTION_RATE);
        drainRate = tagCompound.getInteger(Constants.NBT.ALTAR_DRAIN_RATE);
        consumptionMultiplier = tagCompound.getFloat(Constants.NBT.ALTAR_CONSUMPTION_MULTIPLIER);
        efficiencyMultiplier = tagCompound.getFloat(Constants.NBT.ALTAR_EFFICIENCY_MULTIPLIER);
        selfSacrificeEfficiencyMultiplier = tagCompound.getFloat(Constants.NBT.ALTAR_SELF_SACRIFICE_MULTIPLIER);
        sacrificeEfficiencyMultiplier = tagCompound.getFloat(Constants.NBT.ALTAR_SACRIFICE_MULTIPLIER);
        capacityMultiplier = tagCompound.getFloat(Constants.NBT.ALTAR_CAPACITY_MULTIPLIER);
        orbCapacityMultiplier = tagCompound.getFloat(Constants.NBT.ALTAR_ORB_CAPACITY_MULTIPLIER);
        dislocationMultiplier = tagCompound.getFloat(Constants.NBT.ALTAR_DISLOCATION_MULTIPLIER);
        capacity = tagCompound.getInteger(Constants.NBT.ALTAR_CAPACITY);
        bufferCapacity = tagCompound.getInteger(Constants.NBT.ALTAR_BUFFER_CAPACITY);
        progress = tagCompound.getInteger(Constants.NBT.ALTAR_PROGRESS);
        isResultBlock = tagCompound.getBoolean(Constants.NBT.ALTAR_IS_RESULT_BLOCK);
        lockdownDuration = tagCompound.getInteger(Constants.NBT.ALTAR_LOCKDOWN_DURATION);
        accelerationUpgrades = tagCompound.getInteger(Constants.NBT.ALTAR_ACCELERATION_UPGRADES);
        demonBloodDuration = tagCompound.getInteger(Constants.NBT.ALTAR_DEMON_BLOOD_DURATION);
        cooldownAfterCrafting = tagCompound.getInteger(Constants.NBT.ALTAR_COOLDOWN_AFTER_CRAFTING);
        chargingRate = tagCompound.getInteger(Constants.NBT.ALTAR_CHARGE_RATE);
        chargingFrequency = tagCompound.getInteger(Constants.NBT.ALTAR_CHARGE_FREQUENCY);
        totalCharge = tagCompound.getInteger(Constants.NBT.ALTAR_TOTAL_CHARGE);
        maxCharge = tagCompound.getInteger(Constants.NBT.ALTAR_MAX_CHARGE);
        currentTierDisplayed = Enums.getIfPresent(EnumAltarTier.class, tagCompound.getString(Constants.NBT.ALTAR_CURRENT_TIER_DISPLAYED)).or(EnumAltarTier.ONE);
    }

    public void writeToNBT(NBTTagCompound tagCompound) {

        if (fluid != null)
            fluid.writeToNBT(tagCompound);
        else
            tagCompound.setString(Constants.NBT.EMPTY, "");

        if (fluidOutput != null)
            tagCompound.setInteger(Constants.NBT.OUTPUT_AMOUNT, fluidOutput.amount);

        if (fluidInput != null)
            tagCompound.setInteger(Constants.NBT.INPUT_AMOUNT, fluidInput.amount);

        tagCompound.setInteger("internalCounter", internalCounter);
        tagCompound.setString(Constants.NBT.ALTAR_TIER, altarTier.name());
        tagCompound.setBoolean(Constants.NBT.ALTAR_ACTIVE, isActive);
        tagCompound.setInteger(Constants.NBT.ALTAR_LIQUID_REQ, liquidRequired);
        tagCompound.setBoolean(Constants.NBT.ALTAR_FILLABLE, canBeFilled);
        tagCompound.setBoolean(Constants.NBT.ALTAR_UPGRADED, isUpgraded);
        tagCompound.setInteger(Constants.NBT.ALTAR_CONSUMPTION_RATE, consumptionRate);
        tagCompound.setInteger(Constants.NBT.ALTAR_DRAIN_RATE, drainRate);
        tagCompound.setFloat(Constants.NBT.ALTAR_CONSUMPTION_MULTIPLIER, consumptionMultiplier);
        tagCompound.setFloat(Constants.NBT.ALTAR_EFFICIENCY_MULTIPLIER, efficiencyMultiplier);
        tagCompound.setFloat(Constants.NBT.ALTAR_SACRIFICE_MULTIPLIER, sacrificeEfficiencyMultiplier);
        tagCompound.setFloat(Constants.NBT.ALTAR_SELF_SACRIFICE_MULTIPLIER, selfSacrificeEfficiencyMultiplier);
        tagCompound.setBoolean(Constants.NBT.ALTAR_IS_RESULT_BLOCK, isResultBlock);
        tagCompound.setFloat(Constants.NBT.ALTAR_CAPACITY_MULTIPLIER, capacityMultiplier);
        tagCompound.setFloat(Constants.NBT.ALTAR_ORB_CAPACITY_MULTIPLIER, orbCapacityMultiplier);
        tagCompound.setFloat(Constants.NBT.ALTAR_DISLOCATION_MULTIPLIER, dislocationMultiplier);
        tagCompound.setInteger(Constants.NBT.ALTAR_CAPACITY, capacity);
        tagCompound.setInteger(Constants.NBT.ALTAR_PROGRESS, progress);
        tagCompound.setInteger(Constants.NBT.ALTAR_BUFFER_CAPACITY, bufferCapacity);
        tagCompound.setInteger(Constants.NBT.ALTAR_LOCKDOWN_DURATION, lockdownDuration);
        tagCompound.setInteger(Constants.NBT.ALTAR_ACCELERATION_UPGRADES, accelerationUpgrades);
        tagCompound.setInteger(Constants.NBT.ALTAR_DEMON_BLOOD_DURATION, demonBloodDuration);
        tagCompound.setInteger(Constants.NBT.ALTAR_COOLDOWN_AFTER_CRAFTING, cooldownAfterCrafting);
        tagCompound.setInteger(Constants.NBT.ALTAR_CHARGE_RATE, chargingRate);
        tagCompound.setInteger(Constants.NBT.ALTAR_CHARGE_FREQUENCY, chargingFrequency);
        tagCompound.setInteger(Constants.NBT.ALTAR_TOTAL_CHARGE, totalCharge);
        tagCompound.setInteger(Constants.NBT.ALTAR_MAX_CHARGE, maxCharge);
        tagCompound.setString(Constants.NBT.ALTAR_CURRENT_TIER_DISPLAYED, currentTierDisplayed.name());
    }

    public void startCycle() {
        if (tileAltar.getWorld() != null)
            tileAltar.getWorld().notifyBlockUpdate(tileAltar.getPos(), tileAltar.getWorld().getBlockState(tileAltar.getPos()), tileAltar.getWorld().getBlockState(tileAltar.getPos()), 3);

        checkTier();

        if ((fluid == null || fluid.amount <= 0) && totalCharge <= 0)
            return;

        if (!isActive)
            progress = 0;

        ItemStack input = tileAltar.getStackInSlot(0);

        if (!input.isEmpty()) {
            // Do recipes
            RecipeBloodAltar recipe = BloodMagicAPI.INSTANCE.getRecipeRegistrar().getBloodAltar(input);
            if (recipe != null) {
                if (recipe.getMinimumTier().ordinal() <= altarTier.ordinal()) {
                    this.isActive = true;
                    this.recipe = recipe;
                    this.liquidRequired = recipe.getSyphon();
                    this.consumptionRate = recipe.getConsumeRate();
                    this.drainRate = recipe.getDrainRate();
                    this.canBeFilled = false;
                    return;
                }
            } else if (input.getItem() instanceof IBloodOrb) {
                this.isActive = true;
                this.canBeFilled = true;
                return;
            }
        }

        isActive = false;
    }

    public void update() {
        World world = tileAltar.getWorld();
        BlockPos pos = tileAltar.getPos();

        if (world.isRemote)
            return;

        // Used instead of the world time for checks that do not happen every tick
        internalCounter++;

        if (lockdownDuration > 0)
            lockdownDuration--;

        if (internalCounter % 20 == 0) {
            for (EnumFacing facing : EnumFacing.VALUES) {
                BlockPos newPos = pos.offset(facing);
                IBlockState block = world.getBlockState(newPos);
                block.getBlock().onNeighborChange(world, newPos, pos);
            }
        }
        if (internalCounter % (Math.max(20 - this.accelerationUpgrades, 1)) == 0) {
            int syphonMax = (int) (20 * this.dislocationMultiplier);
            int fluidInputted;
            int fluidOutputted;
            fluidInputted = Math.min(syphonMax, -this.fluid.amount + capacity);
            fluidInputted = Math.min(this.fluidInput.amount, fluidInputted);
            this.fluid.amount += fluidInputted;
            this.fluidInput.amount -= fluidInputted;
            fluidOutputted = Math.min(syphonMax, this.bufferCapacity - this.fluidOutput.amount);
            fluidOutputted = Math.min(this.fluid.amount, fluidOutputted);
            this.fluidOutput.amount += fluidOutputted;
            this.fluid.amount -= fluidOutputted;
            tileAltar.getWorld().notifyBlockUpdate(tileAltar.getPos(), tileAltar.getWorld().getBlockState(tileAltar.getPos()), tileAltar.getWorld().getBlockState(tileAltar.getPos()), 3);
        }

        if (internalCounter % this.getChargingFrequency() == 0 && !this.isActive) {
            int chargeInputted = Math.min(chargingRate, this.fluid.amount);
            chargeInputted = Math.min(chargeInputted, maxCharge - totalCharge);
            totalCharge += chargeInputted;
            this.fluid.amount -= chargeInputted;
            tileAltar.getWorld().notifyBlockUpdate(tileAltar.getPos(), tileAltar.getWorld().getBlockState(tileAltar.getPos()), tileAltar.getWorld().getBlockState(tileAltar.getPos()), 3);
        }

        if (internalCounter % 100 == 0 && (this.isActive || this.cooldownAfterCrafting <= 0))
            startCycle();

        updateAltar();
    }

    private void updateAltar() {
        if (!isActive) {
            if (cooldownAfterCrafting > 0)
                cooldownAfterCrafting--;
            return;
        }

        ItemStack input = tileAltar.getStackInSlot(0);

        if (input.isEmpty())
            return;

        World world = tileAltar.getWorld();
        BlockPos pos = tileAltar.getPos();

        if (world.isRemote)
            return;

        if (!canBeFilled) {
            boolean hasOperated = false;
            int stackSize = input.getCount();

            if (totalCharge > 0) {
                int chargeDrained = Math.min(liquidRequired * stackSize - progress, totalCharge);

                totalCharge -= chargeDrained;
                progress += chargeDrained;

                hasOperated = true;
            }
            if (fluid != null && fluid.amount >= 1) {
                int liquidDrained = Math.min((int) (altarTier.ordinal() >= 2 ? consumptionRate * (1 + consumptionMultiplier) : consumptionRate), fluid.amount);

                if (liquidDrained > (liquidRequired * stackSize - progress))
                    liquidDrained = liquidRequired * stackSize - progress;

                fluid.amount = fluid.amount - liquidDrained;
                progress += liquidDrained;

                hasOperated = true;

                if (internalCounter % 4 == 0 && world instanceof WorldServer) {
                    WorldServer server = (WorldServer) world;
                    server.spawnParticle(EnumParticleTypes.REDSTONE, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, 1, 0.2, 0, 0.2, 0);
                }

            } else if (!hasOperated && progress > 0) {
                progress -= (int) (efficiencyMultiplier * drainRate);

                if (internalCounter % 2 == 0 && world instanceof WorldServer) {
                    WorldServer server = (WorldServer) world;
                    server.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, 1, 0.1, 0, 0.1, 0);
                }
            }

            if (hasOperated) {
                if (progress >= liquidRequired * stackSize) {
                    ItemStack result = recipe.getOutput().copy();

                    BloodMagicCraftedEvent.Altar event = new BloodMagicCraftedEvent.Altar(recipe.getInput(), result);
                    MinecraftForge.EVENT_BUS.post(event);
                    tileAltar.setInventorySlotContents(0, event.getOutput());
                    progress = 0;

                    if (world instanceof WorldServer) {
                        WorldServer server = (WorldServer) world;
                        server.spawnParticle(EnumParticleTypes.REDSTONE, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, 40, 0.3, 0, 0.3, 0);
                    }

                    this.cooldownAfterCrafting = 30;
                    this.isActive = false;
                }
            }
        } else {
            ItemStack returnedItem = tileAltar.getStackInSlot(0);

            if (returnedItem.isEmpty() || !(returnedItem.getItem() instanceof IBloodOrb))
                return;

            IBloodOrb item = (IBloodOrb) (returnedItem.getItem());
            NBTTagCompound itemTag = returnedItem.getTagCompound();

            if (itemTag == null)
                return;

            String ownerUUID = itemTag.getString(Constants.NBT.OWNER_UUID);

            if (Strings.isNullOrEmpty(ownerUUID))
                return;

            if (fluid != null && fluid.amount >= 1) {
                int liquidDrained = Math.min((int) (altarTier.ordinal() >= 2 ? consumptionRate * (1 + consumptionMultiplier) : consumptionRate), fluid.amount);

                BloodOrb orb = item.getOrb(returnedItem);
                int drain = orb == null ? 0 : NetworkHelper.getSoulNetwork(ownerUUID).add(liquidDrained, (int) (orb.getCapacity() * this.orbCapacityMultiplier));
                fluid.amount = fluid.amount - drain;

                if (drain > 0 && internalCounter % 4 == 0 && world instanceof WorldServer) {
                    WorldServer server = (WorldServer) world;
                    server.spawnParticle(EnumParticleTypes.SPELL_WITCH, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, 1, 0, 0, 0, 0.001);
                }
            }
        }

        tileAltar.getWorld().notifyBlockUpdate(tileAltar.getPos(), tileAltar.getWorld().getBlockState(tileAltar.getPos()), tileAltar.getWorld().getBlockState(tileAltar.getPos()), 3);
    }

    public void checkTier() {
        EnumAltarTier tier = BloodAltar.getAltarTier(tileAltar.getWorld(), tileAltar.getPos());
        this.altarTier = tier;

        upgrade = BloodAltar.getUpgrades(tileAltar.getWorld(), tileAltar.getPos(), tier);

        if (tier.equals(currentTierDisplayed))
            currentTierDisplayed = EnumAltarTier.ONE;

        if (tier.equals(EnumAltarTier.ONE)) {
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
        } else if (!tier.equals(EnumAltarTier.ONE) && upgrade != null) {
            this.isUpgraded = true;
            this.accelerationUpgrades = upgrade.getAccelerationCount();
            this.consumptionMultiplier = (float) (0.20 * upgrade.getSpeedCount());
            this.efficiencyMultiplier = (float) Math.pow(0.85, upgrade.getEfficiencyCount());
            this.sacrificeEfficiencyMultiplier = (float) (0.10 * upgrade.getSacrificeCount());
            this.selfSacrificeEfficiencyMultiplier = (float) (0.10 * upgrade.getSelfSacrificeCount());
            this.capacityMultiplier = (float) ((1 * Math.pow(1.10, upgrade.getBetterCapacityCount()) + 0.20 * upgrade.getCapacityCount()));
            this.dislocationMultiplier = (float) (Math.pow(1.2, upgrade.getDisplacementCount()));
            this.orbCapacityMultiplier = (float) (1 + 0.02 * upgrade.getOrbCapacityCount());
            this.chargingFrequency = Math.max(20 - upgrade.getAccelerationCount(), 1);
            this.chargingRate = (int) (10 * upgrade.getChargingCount() * (1 + consumptionMultiplier / 2));
            this.maxCharge = (int) (Fluid.BUCKET_VOLUME * Math.max(0.5 * capacityMultiplier, 1) * upgrade.getChargingCount());
        }

        this.capacity = (int) (Fluid.BUCKET_VOLUME * 10 * capacityMultiplier);
        this.bufferCapacity = (int) (Fluid.BUCKET_VOLUME * 1 * capacityMultiplier);

        if (this.fluid.amount > this.capacity)
            this.fluid.amount = this.capacity;
        if (this.fluidOutput.amount > this.bufferCapacity)
            this.fluidOutput.amount = this.bufferCapacity;
        if (this.fluidInput.amount > this.bufferCapacity)
            this.fluidInput.amount = this.bufferCapacity;
        if (this.totalCharge > this.maxCharge)
            this.totalCharge = this.maxCharge;

        tileAltar.getWorld().notifyBlockUpdate(tileAltar.getPos(), tileAltar.getWorld().getBlockState(tileAltar.getPos()), tileAltar.getWorld().getBlockState(tileAltar.getPos()), 3);
    }

    public int fillMainTank(int amount) {
        int filledAmount = Math.min(capacity - fluid.amount, amount);
        fluid.amount += filledAmount;

        return filledAmount;
    }

    public void sacrificialDaggerCall(int amount, boolean isSacrifice) {
        if (this.lockdownDuration > 0) {
            int amt = (int) Math.min(bufferCapacity - fluidInput.amount, (isSacrifice ? 1 + sacrificeEfficiencyMultiplier : 1 + selfSacrificeEfficiencyMultiplier) * amount);
            fluidInput.amount += amt;
        } else {
            fluid.amount += Math.min(capacity - fluid.amount, (isSacrifice ? 1 + sacrificeEfficiencyMultiplier : 1 + selfSacrificeEfficiencyMultiplier) * amount);
        }
    }

    public void setMainFluid(FluidStack fluid) {
        this.fluid = fluid;
    }

    public void setOutputFluid(FluidStack fluid) {
        this.fluidOutput = fluid;
    }

    public void setInputFluid(FluidStack fluid) {
        this.fluidInput = fluid;
    }

    public AltarUpgrade getUpgrade() {
        return upgrade;
    }

    public void setUpgrade(AltarUpgrade upgrade) {
        this.upgrade = upgrade;
    }

    public int getCapacity() {
        return capacity;
    }

    public FluidStack getFluid() {
        return fluid;
    }

    public int getFluidAmount() {
        return fluid.amount;
    }

    public int getCurrentBlood() {
        return getFluidAmount();
    }

    public EnumAltarTier getTier() {
        return altarTier;
    }

    public void setTier(EnumAltarTier tier) {
        this.altarTier = tier;
    }

    public int getProgress() {
        return progress;
    }

    public float getSacrificeMultiplier() {
        return sacrificeEfficiencyMultiplier;
    }

    public float getSelfSacrificeMultiplier() {
        return selfSacrificeEfficiencyMultiplier;
    }

    public float getOrbMultiplier() {
        return orbCapacityMultiplier;
    }

    public float getDislocationMultiplier() {
        return dislocationMultiplier;
    }

    public float getConsumptionMultiplier() {
        return consumptionMultiplier;
    }

    public float getConsumptionRate() {
        return consumptionRate;
    }

    public int getLiquidRequired() {
        return liquidRequired;
    }

    public int getBufferCapacity() {
        return bufferCapacity;
    }

    public boolean setCurrentTierDisplayed(EnumAltarTier altarTier) {
        if (currentTierDisplayed == altarTier)
            return false;
        else
            currentTierDisplayed = altarTier;
        return true;
    }

    public void addToDemonBloodDuration(int dur) {
        this.demonBloodDuration += dur;
    }

    public boolean hasDemonBlood() {
        return this.demonBloodDuration > 0;
    }

    public void decrementDemonBlood() {
        this.demonBloodDuration = Math.max(0, this.demonBloodDuration - 1);
    }

    public void setActive() {
        if (tileAltar.getStackInSlot(0).isEmpty()) {
            isActive = false;
        }
    }

    public boolean isActive() {
        return isActive;
    }

    public void requestPauseAfterCrafting(int amount) {
        if (this.isActive) {
            this.cooldownAfterCrafting = amount;
        }
    }

    public int getChargingRate() {
        return chargingRate;
    }

    public int getTotalCharge() {
        return totalCharge;
    }

    public int getChargingFrequency() {
        return chargingFrequency == 0 ? 1 : chargingFrequency;
    }

    @Override
    public int fill(FluidStack resource, boolean doFill) {
        if (resource == null || resource.getFluid() != BlockLifeEssence.getLifeEssence()) {
            return 0;
        }

        if (!doFill) {
            if (fluidInput == null) {
                return Math.min(bufferCapacity, resource.amount);
            }

            if (!fluidInput.isFluidEqual(resource)) {
                return 0;
            }

            return Math.min(bufferCapacity - fluidInput.amount, resource.amount);
        }

        if (fluidInput == null) {
            fluidInput = new FluidStack(resource, Math.min(bufferCapacity, resource.amount));

            return fluidInput.amount;
        }

        if (!fluidInput.isFluidEqual(resource)) {
            return 0;
        }
        int filled = bufferCapacity - fluidInput.amount;

        if (resource.amount < filled) {
            fluidInput.amount += resource.amount;
            filled = resource.amount;
        } else {
            fluidInput.amount = bufferCapacity;
        }

        return filled;
    }

    @Override
    public FluidStack drain(FluidStack resource, boolean doDrain) {
        if (resource == null || !resource.isFluidEqual(fluidOutput)) {
            return null;
        }
        return drain(resource.amount, doDrain);
    }

    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {
        if (fluidOutput == null) {
            return null;
        }

        int drained = maxDrain;
        if (fluidOutput.amount < drained) {
            drained = fluidOutput.amount;
        }

        FluidStack stack = new FluidStack(fluidOutput, drained);
        if (doDrain) {
            fluidOutput.amount -= drained;
        }
        return stack;
    }

    @Override
    public IFluidTankProperties[] getTankProperties() {
        return new IFluidTankProperties[]{new FluidTankPropertiesWrapper(new FluidTank(fluid, capacity))};
    }

    public EnumAltarTier getCurrentTierDisplayed() {
        return currentTierDisplayed;
    }

    public static EnumAltarTier getAltarTier(World world, BlockPos pos) {
        for (int i = EnumAltarTier.MAXTIERS - 1; i >= 1; i--) {
            if (checkAltarIsValid(world, pos, i)) {
                return EnumAltarTier.values()[i];
            }
        }

        return EnumAltarTier.ONE;
    }

    public static boolean checkAltarIsValid(World world, BlockPos worldPos, int altarTier) {
        for (AltarComponent altarComponent : EnumAltarTier.values()[altarTier].getAltarComponents()) {
            BlockPos componentPos = worldPos.add(altarComponent.getOffset());
            IBlockState state = world.getBlockState(componentPos);

            if (altarComponent.getComponent() != EnumAltarComponent.NOTAIR) {
                if (state.getBlock() instanceof IAltarComponent) {
                    EnumAltarComponent component = ((IAltarComponent) state.getBlock()).getType(world, state, componentPos);
                    if (component == null || component != altarComponent.getComponent())
                        return false;
                }

                if (!BloodMagicAPI.INSTANCE.getComponentStates(altarComponent.getComponent()).contains(state))
                    return false;
            } else if (world.isAirBlock(componentPos))
                return false;
        }

        return true;
    }

    public static Pair<BlockPos, EnumAltarComponent> getAltarMissingBlock(World world, BlockPos worldPos, int altarTier) {
        if (altarTier >= EnumAltarTier.MAXTIERS)
            return null;

        for (AltarComponent altarComponent : EnumAltarTier.values()[altarTier].getAltarComponents()) {
            BlockPos componentPos = worldPos.add(altarComponent.getOffset());
            IBlockState state = world.getBlockState(componentPos);

            if (altarComponent.getComponent() != EnumAltarComponent.NOTAIR) {
                if (state.getBlock() instanceof IAltarComponent) {
                    EnumAltarComponent component = ((IAltarComponent) state.getBlock()).getType(world, state, componentPos);
                    if (component == null || component != altarComponent.getComponent())
                        return Pair.of(componentPos, altarComponent.getComponent());
                }

                if (!BloodMagicAPI.INSTANCE.getComponentStates(altarComponent.getComponent()).contains(state))
                    return Pair.of(componentPos, altarComponent.getComponent());
            } else if (world.isAirBlock(componentPos))
                return Pair.of(componentPos, altarComponent.getComponent());
        }

        return null;
    }

    public static AltarUpgrade getUpgrades(World world, BlockPos pos, EnumAltarTier altarTier) {
        if (world.isRemote) {
            return null;
        }

        AltarUpgrade upgrades = new AltarUpgrade();
        List<AltarComponent> list = altarTier.getAltarComponents();

        for (AltarComponent altarComponent : list) {
            BlockPos componentPos = pos.add(altarComponent.getOffset());

            if (altarComponent.isUpgradeSlot()) {
                BlockStack worldBlock = new BlockStack(world.getBlockState(componentPos).getBlock(), world.getBlockState(componentPos).getBlock().getMetaFromState(world.getBlockState(componentPos)));

                if (worldBlock.getBlock() instanceof BlockBloodRune) {
                    switch (((BlockBloodRune) worldBlock.getBlock()).getRuneEffect(worldBlock.getMeta())) {
                        case 1:
                            upgrades.addSpeed();
                            break;

                        case 2:
                            upgrades.addEfficiency();
                            break;

                        case 3:
                            upgrades.addSacrifice();
                            break;

                        case 4:
                            upgrades.addSelfSacrifice();
                            break;

                        case 5:
                            upgrades.addDisplacement();
                            break;

                        case 6:
                            upgrades.addCapacity();
                            break;

                        case 7:
                            upgrades.addBetterCapacity();
                            break;

                        case 8:
                            upgrades.addOrbCapacity();
                            break;

                        case 9:
                            upgrades.addAcceleration();
                            break;

                        case 10:
                            upgrades.addCharging();
                            break;
                    }
                }
            }
        }

        return upgrades;
    }
}
