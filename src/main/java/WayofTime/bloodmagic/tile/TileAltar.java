package WayofTime.bloodmagic.tile;

import WayofTime.bloodmagic.altar.BloodAltar;
import WayofTime.bloodmagic.api.BloodMagicAPI;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.altar.AltarRecipe;
import WayofTime.bloodmagic.api.altar.AltarUpgrade;
import WayofTime.bloodmagic.api.altar.EnumAltarTier;
import WayofTime.bloodmagic.api.altar.IBloodAltar;
import WayofTime.bloodmagic.api.orb.IBloodOrb;
import WayofTime.bloodmagic.api.registry.AltarRecipeRegistry;
import WayofTime.bloodmagic.api.util.helper.NetworkHelper;
import WayofTime.bloodmagic.block.BlockLifeEssence;
import com.google.common.base.Enums;
import com.google.common.base.Strings;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraftforge.fluids.*;

public class TileAltar extends TileInventory implements IBloodAltar, ITickable, IFluidTank, IFluidHandler {

    public boolean isActive;
    protected FluidStack fluidOutput = new FluidStack(BlockLifeEssence.getLifeEssence(), 0);
    protected FluidStack fluidInput = new FluidStack(BlockLifeEssence.getLifeEssence(), 0);
    private EnumAltarTier altarTier = EnumAltarTier.ONE;
    private AltarUpgrade upgrade;
    private int capacity = FluidContainerRegistry.BUCKET_VOLUME * 10;
    private FluidStack fluid = new FluidStack(BloodMagicAPI.getLifeEssence(), 0);
    private int liquidRequired; //mB
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
    private int bufferCapacity = FluidContainerRegistry.BUCKET_VOLUME;
    private int progress;
    private int lockdownDuration;
    private int demonBloodDuration;

    private int cooldownAfterCrafting = 500;

    private ItemStack result;

    public TileAltar() {
        super(1, "altar");
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);

        if (!tagCompound.hasKey(Constants.NBT.EMPTY)) {
            FluidStack fluid = FluidStack.loadFluidStackFromNBT(tagCompound);

            if (fluid != null)
                setMainFluid(fluid);

            FluidStack fluidOut = new FluidStack(BloodMagicAPI.getLifeEssence(), tagCompound.getInteger(Constants.NBT.OUTPUT_AMOUNT));
            setOutputFluid(fluidOut);

            FluidStack fluidIn = new FluidStack(BloodMagicAPI.getLifeEssence(), tagCompound.getInteger(Constants.NBT.INPUT_AMOUNT));
            setInputFluid(fluidIn);
        }

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
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);

        if (fluid != null)
            fluid.writeToNBT(tagCompound);
        else
            tagCompound.setString(Constants.NBT.EMPTY, "");

        if (fluidOutput != null)
            tagCompound.setInteger(Constants.NBT.OUTPUT_AMOUNT, fluidOutput.amount);

        if (fluidInput != null)
            tagCompound.setInteger(Constants.NBT.INPUT_AMOUNT, fluidInput.amount);

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
    }

    @Override
    public void update() {
        if (getWorld().isRemote)
            return;

        this.decrementDemonBlood();

        if (lockdownDuration > 0)
            lockdownDuration--;

        if (!getWorld().isRemote && getWorld().getWorldTime() % 20 == 0) {
            IBlockState block = getWorld().getBlockState(new BlockPos(this.pos.getX() + 1, this.pos.getY(), this.pos.getZ()));
            block.getBlock().onNeighborBlockChange(getWorld(), new BlockPos(this.pos.getX() + 1, this.pos.getY(), this.pos.getZ()), block, block.getBlock());
            block = getWorld().getBlockState(new BlockPos(this.pos.getX() + 1, this.pos.getY(), this.pos.getZ()));
            block.getBlock().onNeighborBlockChange(getWorld(), new BlockPos(this.pos.getX() - 1, this.pos.getY(), this.pos.getZ()), block, block.getBlock());
            block = getWorld().getBlockState(new BlockPos(this.pos.getX(), this.pos.getY() + 1, this.pos.getZ()));
            block.getBlock().onNeighborBlockChange(getWorld(), new BlockPos(this.pos.getX(), this.pos.getY() + 1, this.pos.getZ()), block, block.getBlock());
            block = getWorld().getBlockState(new BlockPos(this.pos.getX(), this.pos.getY() - 1, this.pos.getZ()));
            block.getBlock().onNeighborBlockChange(getWorld(), new BlockPos(this.pos.getX(), this.pos.getY() - 1, this.pos.getZ()), block, block.getBlock());
            block = getWorld().getBlockState(new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ() + 1));
            block.getBlock().onNeighborBlockChange(getWorld(), new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ() + 1), block, block.getBlock());
            block = getWorld().getBlockState(new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ() - 1));
            block.getBlock().onNeighborBlockChange(getWorld(), new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ() - 1), block, block.getBlock());
        }
        if (getWorld().getTotalWorldTime() % (Math.max(20 - this.accelerationUpgrades, 1)) == 0)
            everySecond();

        if (getWorld().getTotalWorldTime() % 100 == 0 && (this.isActive || this.cooldownAfterCrafting <= 0))
            everyFiveSeconds();

        updat();
    }

    private void everySecond() {
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
    }

    private void everyFiveSeconds() {
        startCycle();
    }

    @Override
    public void startCycle() {
        if (getWorld() != null)
            getWorld().markBlockForUpdate(pos);

        checkTier();

        if (fluid == null || fluid.amount <= 0)
            return;

        if (!isActive)
            progress = 0;

        if (getStackInSlot(0) != null) {
            // Do recipes
            for (ItemStack itemStack : AltarRecipeRegistry.getRecipes().keySet()) {
                if (getStackInSlot(0).getIsItemStackEqual(AltarRecipeRegistry.getRecipes().get(itemStack).getInput())) {
                    AltarRecipe recipe = AltarRecipeRegistry.getRecipeForInput(itemStack);

                    if (altarTier.ordinal() >= recipe.getMinTier().ordinal()) {
                        this.isActive = true;
                        this.result = new ItemStack(recipe.getOutput().getItem(), 1, recipe.getOutput().getMetadata());
                        this.liquidRequired = recipe.getSyphon();
                        this.canBeFilled = recipe.isUseTag();
                        this.consumptionRate = recipe.getConsumeRate();
                        this.drainRate = recipe.getDrainRate();
                        return;
                    }
                }
            }
        }

        isActive = false;
    }

    private void updat() {
        if (!isActive) {
            if (cooldownAfterCrafting > 0)
                cooldownAfterCrafting--;
            return;
        }

        if (getStackInSlot(0) == null)
            return;

        int worldTime = (int) (getWorld().getWorldTime() % 24000);

        if (getWorld().isRemote)
            return;

        float f = 1.0F;
        float f1 = f * 0.6F + 0.4F;
        float f2 = f * f * 0.7F - 0.5F;
        float f3 = f * f * 0.6F - 0.7F;

        if (!canBeFilled) {
            if (fluid != null && fluid.amount >= 1) {
                int stackSize = getStackInSlot(0).stackSize;
                int liquidDrained = Math.min((int) (altarTier.ordinal() >= 2 ? consumptionRate * (1 + consumptionMultiplier) : consumptionRate), fluid.amount);

                if (liquidDrained > (liquidRequired * stackSize - progress))
                    liquidDrained = liquidRequired * stackSize - progress;

                fluid.amount = fluid.amount - liquidDrained;
                progress += liquidDrained;

                if (worldTime % 4 == 0)
                    getWorld().spawnParticle(EnumParticleTypes.REDSTONE, this.pos.getX() + Math.random() - Math.random(), this.pos.getY() + Math.random() - Math.random(), this.pos.getZ() + Math.random() - Math.random(), f1, f2, f3);

                if (progress >= liquidRequired * stackSize) {
                    ItemStack result = this.result;

                    if (result != null)
                        result.stackSize *= stackSize;

                    setInventorySlotContents(0, result);
                    progress = 0;

                    for (int i = 0; i < 8; i++)
                        getWorld().spawnParticle(EnumParticleTypes.REDSTONE, this.pos.getX() + Math.random() - Math.random(), this.pos.getY() + Math.random() - Math.random(), this.pos.getZ() + Math.random() - Math.random(), f1, f2, f3);

                    this.isActive = false;
                }
            } else if (progress > 0) {
                progress -= (int) (efficiencyMultiplier * drainRate);

                if (worldTime % 2 == 0)
                    getWorld().spawnParticle(EnumParticleTypes.REDSTONE, this.pos.getX() + Math.random() - Math.random(), this.pos.getY() + Math.random() - Math.random(), this.pos.getZ() + Math.random() - Math.random(), f1, f2, f3);
            }
        } else {
            ItemStack returnedItem = getStackInSlot(0);

            if (!(returnedItem.getItem() instanceof IBloodOrb))
                return;

            IBloodOrb item = (IBloodOrb) (returnedItem.getItem());
            NBTTagCompound itemTag = returnedItem.getTagCompound();

            if (itemTag == null)
                return;

            String ownerName = itemTag.getString(Constants.NBT.OWNER_NAME);

            if (Strings.isNullOrEmpty(ownerName))
                return;

            if (fluid != null && fluid.amount >= 1) {
                int liquidDrained = Math.min((int) (altarTier.ordinal() >= 2 ? consumptionRate * (1 + consumptionMultiplier) : consumptionRate), fluid.amount);

                int drain = NetworkHelper.getSoulNetwork(ownerName, getWorld()).addLifeEssence(liquidDrained, (int) (item.getMaxEssence(returnedItem.getMetadata()) * this.orbCapacityMultiplier));

                fluid.amount = fluid.amount - drain;

                if (worldTime % 4 == 0)
                    getWorld().spawnParticle(EnumParticleTypes.REDSTONE, this.pos.getX() + Math.random() - Math.random(), this.pos.getY() + Math.random() - Math.random(), this.pos.getZ() + Math.random() - Math.random(), f1, f2, f3);
            }
        }
        if (getWorld() != null)
            getWorld().markBlockForUpdate(pos);
    }

    private void checkTier() {
        // TODO - Write checking for tier stuff

        EnumAltarTier tier = BloodAltar.getAltarTier(getWorld(), getPos());
        this.altarTier = tier;

        upgrade = BloodAltar.getUpgrades(getWorld(), pos, tier);

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
            return;
        }
        else if (!tier.equals(EnumAltarTier.ONE) && upgrade != null) {
            this.isUpgraded = true;
            this.consumptionMultiplier = (float) (0.20 * upgrade.getSpeedCount());
            this.efficiencyMultiplier = (float) Math.pow(0.85, upgrade.getEfficiencyCount());
            this.sacrificeEfficiencyMultiplier = (float) (0.10 * upgrade.getSacrificeCount());
            this.selfSacrificeEfficiencyMultiplier = (float) (0.10 * upgrade.getSelfSacrificeCount());
            this.capacityMultiplier = (float) ((1 * Math.pow(1.10, upgrade.getBetterCapacityCount()) + 0.20 * upgrade.getCapacityCount()));
            this.dislocationMultiplier = (float) (Math.pow(1.2, upgrade.getDisplacementCount()));
            this.orbCapacityMultiplier = (float) (1 + 0.02 * upgrade.getOrbCapacityCount());
            this.capacity = (int) (FluidContainerRegistry.BUCKET_VOLUME * 10 * capacityMultiplier);
            this.bufferCapacity = (int) (FluidContainerRegistry.BUCKET_VOLUME * 1 * capacityMultiplier);
            this.accelerationUpgrades = upgrade.getAccelerationCount();
        }

        if (this.fluid.amount > this.capacity) this.fluid.amount = this.capacity;
        if (this.fluidOutput.amount > this.bufferCapacity) this.fluidOutput.amount = this.bufferCapacity;
        if (this.fluidInput.amount > this.bufferCapacity) this.fluidInput.amount = this.bufferCapacity;

        worldObj.markBlockForUpdate(pos);
    }

    public int fillMainTank(int amount) {
        int filledAmount = Math.min(capacity - fluid.amount, amount);
        fluid.amount += filledAmount;

        return filledAmount;
    }

    @Override
    public void sacrificialDaggerCall(int amount, boolean isSacrifice) {
        if (this.lockdownDuration > 0) {
            int amt = (int) Math.min(bufferCapacity - fluidInput.amount, (isSacrifice ? 1 + sacrificeEfficiencyMultiplier : 1 + selfSacrificeEfficiencyMultiplier) * amount);
            fluidInput.amount += amt;
        } else {
            fluid.amount += Math.min(capacity - fluid.amount, (isSacrifice ? 1 + sacrificeEfficiencyMultiplier : 1 + selfSacrificeEfficiencyMultiplier) * amount);
        }
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack itemstack) {
        return slot == 0;
    }

    public AltarUpgrade getUpgrade() {
        return upgrade;
    }

    public TileAltar setUpgrade(AltarUpgrade upgrade) {
        this.upgrade = upgrade;
        return this;
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    @Override
    public int getCurrentBlood() {
        return getFluidAmount();
    }

    @Override
    public EnumAltarTier getTier() {
        return altarTier;
    }

    public TileAltar setTier(EnumAltarTier tier) {
        this.altarTier = tier;
        return this;
    }

    @Override
    public int getProgress() {
        return progress;
    }

    @Override
    public float getSacrificeMultiplier() {
        return sacrificeEfficiencyMultiplier;
    }

    @Override
    public float getSelfSacrificeMultiplier() {
        return selfSacrificeEfficiencyMultiplier;
    }

    @Override
    public float getOrbMultiplier() {
        return orbCapacityMultiplier;
    }

    @Override
    public float getDislocationMultiplier() {
        return dislocationMultiplier;
    }

    @Override
    public int getBufferCapacity() {
        return bufferCapacity;
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
        isActive = false;
    }

    public boolean isActive() {
        return isActive;
    }

    @Override
    public void requestPauseAfterCrafting(int amount) {
        if (this.isActive) {
            this.cooldownAfterCrafting = amount;
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

    // IFluidHandler

    @Override
    public FluidTankInfo[] getTankInfo(EnumFacing from) {
        return new FluidTankInfo[0];
    }

    @Override
    public int fill(EnumFacing from, FluidStack resource, boolean doFill) {
        return 0;
    }

    @Override
    public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain) {
        return null;
    }

    @Override
    public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain) {
        return null;
    }

    @Override
    public boolean canFill(EnumFacing from, Fluid fluid) {
        return false;
    }

    @Override
    public boolean canDrain(EnumFacing from, Fluid fluid) {
        return false;
    }

    // IFluidTank

    @Override
    public FluidStack getFluid() {
        return fluid;
    }

    @Override
    public int getFluidAmount() {
        return fluid.amount;
    }

    @Override
    public FluidTankInfo getInfo() {
        return new FluidTankInfo(this);
    }

    @Override
    public int fill(FluidStack resource, boolean doFill) {
        return 0;
    }

    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {
        return null;
    }
}