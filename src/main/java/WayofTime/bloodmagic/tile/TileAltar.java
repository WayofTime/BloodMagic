package WayofTime.bloodmagic.tile;

import WayofTime.bloodmagic.altar.BloodAltar;
import WayofTime.bloodmagic.api.BloodMagicAPI;
import WayofTime.bloodmagic.api.altar.AltarRecipe;
import WayofTime.bloodmagic.api.altar.AltarUpgrade;
import WayofTime.bloodmagic.api.altar.EnumAltarTier;
import WayofTime.bloodmagic.api.altar.IBloodAltar;
import WayofTime.bloodmagic.api.registry.AltarRecipeRegistry;
import WayofTime.bloodmagic.block.BlockLifeEssence;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.*;

public class TileAltar extends TileInventory implements IBloodAltar, IUpdatePlayerListBox, IFluidTank, IFluidHandler {
    private int tier;
    private AltarUpgrade upgrade = new AltarUpgrade();
    private FluidStack fluid = new FluidStack(BloodMagicAPI.getLifeEssence(), 0);
    protected FluidStack fluidOutput = new FluidStack(BlockLifeEssence.getLifeEssence(), 0);
    protected FluidStack fluidInput = new FluidStack(BlockLifeEssence.getLifeEssence(), 0);
    private int capacity;

    private int upgradeLevel;

    private int resultID;
    private int resultDamage;
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
    private int bufferCapacity;
    private int progress;

    public boolean isActive;

    private int lockdownDuration;
    private int demonBloodDuration;

    private int cooldownAfterCrafting = 500;

    public TileAltar() {
        super(1, "altar");

        this.capacity = FluidContainerRegistry.BUCKET_VOLUME * 10;
        this.bufferCapacity = FluidContainerRegistry.BUCKET_VOLUME;
    }

    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
        super.readFromNBT(par1NBTTagCompound);

        resultID = par1NBTTagCompound.getInteger("resultID");
        resultDamage = par1NBTTagCompound.getInteger("resultDamage");

        if (!par1NBTTagCompound.hasKey("Empty")) {
            FluidStack fluid = this.fluid.loadFluidStackFromNBT(par1NBTTagCompound);

            if (fluid != null) {
                setMainFluid(fluid);
            }

            FluidStack fluidOut = new FluidStack(BloodMagicAPI.getLifeEssence(), par1NBTTagCompound.getInteger("outputAmount"));

            if (fluidOut != null) {
                setOutputFluid(fluidOut);
            }

            FluidStack fluidIn = new FluidStack(BloodMagicAPI.getLifeEssence(), par1NBTTagCompound.getInteger("inputAmount"));

            if (fluidIn != null) {
                setInputFluid(fluidIn);
            }
        }

        upgradeLevel = par1NBTTagCompound.getInteger("upgradeLevel");
        isActive = par1NBTTagCompound.getBoolean("isActive");
        liquidRequired = par1NBTTagCompound.getInteger("liquidRequired");
        canBeFilled = par1NBTTagCompound.getBoolean("canBeFilled");
        isUpgraded = par1NBTTagCompound.getBoolean("isUpgraded");
        consumptionRate = par1NBTTagCompound.getInteger("consumptionRate");
        drainRate = par1NBTTagCompound.getInteger("drainRate");
        consumptionMultiplier = par1NBTTagCompound.getFloat("consumptionMultiplier");
        efficiencyMultiplier = par1NBTTagCompound.getFloat("efficiencyMultiplier");
        selfSacrificeEfficiencyMultiplier = par1NBTTagCompound.getFloat("selfSacrificeEfficiencyMultiplier");
        sacrificeEfficiencyMultiplier = par1NBTTagCompound.getFloat("sacrificeEfficiencyMultiplier");
        capacityMultiplier = par1NBTTagCompound.getFloat("capacityMultiplier");
        orbCapacityMultiplier = par1NBTTagCompound.getFloat("orbCapacityMultiplier");
        dislocationMultiplier = par1NBTTagCompound.getFloat("dislocationMultiplier");
        capacity = par1NBTTagCompound.getInteger("capacity");
        bufferCapacity = par1NBTTagCompound.getInteger("bufferCapacity");
        progress = par1NBTTagCompound.getInteger("progress");
        isResultBlock = par1NBTTagCompound.getBoolean("isResultBlock");
        lockdownDuration = par1NBTTagCompound.getInteger("lockdownDuration");
        accelerationUpgrades = par1NBTTagCompound.getInteger("accelerationUpgrades");
        demonBloodDuration = par1NBTTagCompound.getInteger("demonBloodDuration");
        cooldownAfterCrafting = par1NBTTagCompound.getInteger("cooldownAfterCrafting");
    }

    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
        super.writeToNBT(par1NBTTagCompound);

        par1NBTTagCompound.setInteger("resultID", resultID);
        par1NBTTagCompound.setInteger("resultDamage", resultDamage);

        if (fluid != null) {
            fluid.writeToNBT(par1NBTTagCompound);
        } else {
            par1NBTTagCompound.setString("Empty", "");
        }

        if (fluidOutput != null) {
            par1NBTTagCompound.setInteger("outputAmount", fluidOutput.amount);
        }

        if (fluidInput != null) {
            par1NBTTagCompound.setInteger("inputAmount", fluidInput.amount);
        }

        par1NBTTagCompound.setInteger("upgradeLevel", upgradeLevel);
        par1NBTTagCompound.setBoolean("isActive", isActive);
        par1NBTTagCompound.setInteger("liquidRequired", liquidRequired);
        par1NBTTagCompound.setBoolean("canBeFilled", canBeFilled);
        par1NBTTagCompound.setBoolean("isUpgraded", isUpgraded);
        par1NBTTagCompound.setInteger("consumptionRate", consumptionRate);
        par1NBTTagCompound.setInteger("drainRate", drainRate);
        par1NBTTagCompound.setFloat("consumptionMultiplier", consumptionMultiplier);
        par1NBTTagCompound.setFloat("efficiencyMultiplier", efficiencyMultiplier);
        par1NBTTagCompound.setFloat("sacrificeEfficiencyMultiplier", sacrificeEfficiencyMultiplier);
        par1NBTTagCompound.setFloat("selfSacrificeEfficiencyMultiplier", selfSacrificeEfficiencyMultiplier);
        par1NBTTagCompound.setBoolean("isResultBlock", isResultBlock);
        par1NBTTagCompound.setFloat("capacityMultiplier", capacityMultiplier);
        par1NBTTagCompound.setFloat("orbCapacityMultiplier", orbCapacityMultiplier);
        par1NBTTagCompound.setFloat("dislocationMultiplier", dislocationMultiplier);
        par1NBTTagCompound.setInteger("capacity", capacity);
        par1NBTTagCompound.setInteger("progress", progress);
        par1NBTTagCompound.setInteger("bufferCapacity", bufferCapacity);
        par1NBTTagCompound.setInteger("lockdownDuration", lockdownDuration);
        par1NBTTagCompound.setInteger("accelerationUpgrades", this.accelerationUpgrades);
        par1NBTTagCompound.setInteger("demonBloodDuration", demonBloodDuration);
        par1NBTTagCompound.setInteger("cooldownAfterCrafting", cooldownAfterCrafting);
    }

    @Override
    public void update() {
        if (getWorld().isRemote) {
            return;
        }

        if (getWorld().getTotalWorldTime() % (Math.max(20 - getUpgrade().getSpeedCount(), 1)) == 0) {
            everySecond();
        }

        if (getWorld().getTotalWorldTime() % 100 == 0) {
            everyFiveSeconds();
        }
    }

    private void everySecond() {
        startCycle();
    }

    private void everyFiveSeconds() {
        checkTier();
    }

    public void startCycle() {
        if (worldObj != null) {
            worldObj.markBlockForUpdate(pos);
        }

        if (fluid == null || fluid.amount <= 0) {
            return;
        }

        if (getStackInSlot(0) != null) {
            // Do recipes
            if (AltarRecipeRegistry.getRecipes().containsKey(getStackInSlot(0))) {
                AltarRecipe recipe = AltarRecipeRegistry.getRecipeForInput(getStackInSlot(0));

                if (tier >= recipe.getMinTier()) {
                    this.liquidRequired = recipe.getSyphon();
//                this.canBeFilled = recipe
                    this.consumptionRate = recipe.getConsumeRate();
                    this.drainRate = recipe.getDrainRate();
                    this.isActive = true;
                }
            }
        }
    }

    private void checkTier() {
        // TODO - Write checking for tier stuff

        EnumAltarTier tier = BloodAltar.getAltarTier(getWorld(), getPos());

        if (tier.equals(EnumAltarTier.ONE)) {
            upgrade = new AltarUpgrade();
        } else {
            upgrade = BloodAltar.getUpgrades(worldObj, pos, tier);
        }

        if (this.fluid.amount > this.capacity) {
            this.fluid.amount = this.capacity;
        }

        worldObj.markBlockForUpdate(pos);
    }

    public void sacrificialDaggerCall(int amount, boolean isSacrifice) {
        if (!isSacrifice && this.lockdownDuration > 0) {
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

    public TileAltar setUpgrade(AltarUpgrade upgrade) {
        this.upgrade = upgrade;
        return this;
    }

    public AltarUpgrade getUpgrade() {
        return upgrade;
    }

    public TileAltar setTier(int tier) {
        this.tier = tier;
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
    public int getTier() {
        return upgradeLevel;
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
