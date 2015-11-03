package WayofTime.bloodmagic.tile;

import WayofTime.bloodmagic.altar.BloodAltar;
import WayofTime.bloodmagic.api.BloodMagicAPI;
import WayofTime.bloodmagic.api.NBTHolder;
import WayofTime.bloodmagic.api.altar.AltarRecipe;
import WayofTime.bloodmagic.api.altar.AltarUpgrade;
import WayofTime.bloodmagic.api.altar.EnumAltarTier;
import WayofTime.bloodmagic.api.altar.IBloodAltar;
import WayofTime.bloodmagic.api.orb.IBloodOrb;
import WayofTime.bloodmagic.api.registry.AltarRecipeRegistry;
import WayofTime.bloodmagic.api.util.helper.SoulNetworkHelper;
import WayofTime.bloodmagic.block.BlockLifeEssence;
import com.google.common.base.Enums;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.*;

public class TileAltar extends TileInventory implements IBloodAltar, IUpdatePlayerListBox, IFluidTank, IFluidHandler {

    private EnumAltarTier altarTier = EnumAltarTier.ONE;
    private AltarUpgrade upgrade = new AltarUpgrade();
    private int capacity = FluidContainerRegistry.BUCKET_VOLUME * 10;

    private FluidStack fluid = new FluidStack(BloodMagicAPI.getLifeEssence(), 0);
    protected FluidStack fluidOutput = new FluidStack(BlockLifeEssence.getLifeEssence(), 0);
    protected FluidStack fluidInput = new FluidStack(BlockLifeEssence.getLifeEssence(), 0);

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
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);

        if (!tagCompound.hasKey("Empty")) {
            FluidStack fluid = FluidStack.loadFluidStackFromNBT(tagCompound);

            if (fluid != null)
                setMainFluid(fluid);

            FluidStack fluidOut = new FluidStack(BloodMagicAPI.getLifeEssence(), tagCompound.getInteger("outputAmount"));
            setOutputFluid(fluidOut);

            FluidStack fluidIn = new FluidStack(BloodMagicAPI.getLifeEssence(), tagCompound.getInteger("inputAmount"));
            setInputFluid(fluidIn);
        }

        altarTier = Enums.getIfPresent(EnumAltarTier.class, tagCompound.getString(NBTHolder.NBT_ALTAR_TIER)).get();
        isActive = tagCompound.getBoolean(NBTHolder.NBT_ALTAR_ACTIVE);
        liquidRequired = tagCompound.getInteger(NBTHolder.NBT_ALTAR_LIQUID_REQ);
        canBeFilled = tagCompound.getBoolean(NBTHolder.NBT_ALTAR_FILLABLE);
        isUpgraded = tagCompound.getBoolean("isUpgraded");
        consumptionRate = tagCompound.getInteger("consumptionRate");
        drainRate = tagCompound.getInteger("drainRate");
        consumptionMultiplier = tagCompound.getFloat("consumptionMultiplier");
        efficiencyMultiplier = tagCompound.getFloat("efficiencyMultiplier");
        selfSacrificeEfficiencyMultiplier = tagCompound.getFloat("selfSacrificeEfficiencyMultiplier");
        sacrificeEfficiencyMultiplier = tagCompound.getFloat("sacrificeEfficiencyMultiplier");
        capacityMultiplier = tagCompound.getFloat("capacityMultiplier");
        orbCapacityMultiplier = tagCompound.getFloat("orbCapacityMultiplier");
        dislocationMultiplier = tagCompound.getFloat("dislocationMultiplier");
        capacity = tagCompound.getInteger("capacity");
        bufferCapacity = tagCompound.getInteger("bufferCapacity");
        progress = tagCompound.getInteger("progress");
        isResultBlock = tagCompound.getBoolean("isResultBlock");
        lockdownDuration = tagCompound.getInteger("lockdownDuration");
        accelerationUpgrades = tagCompound.getInteger("accelerationUpgrades");
        demonBloodDuration = tagCompound.getInteger("demonBloodDuration");
        cooldownAfterCrafting = tagCompound.getInteger("cooldownAfterCrafting");
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);

        if (fluid != null)
            fluid.writeToNBT(tagCompound);
        else
            tagCompound.setString("Empty", "");


        if (fluidOutput != null)
            tagCompound.setInteger("outputAmount", fluidOutput.amount);

        if (fluidInput != null)
            tagCompound.setInteger("inputAmount", fluidInput.amount);

        tagCompound.setString("upgradeLevel", altarTier.name());
        tagCompound.setBoolean("isActive", isActive);
        tagCompound.setInteger("liquidRequired", liquidRequired);
        tagCompound.setBoolean("canBeFilled", canBeFilled);
        tagCompound.setBoolean("isUpgraded", isUpgraded);
        tagCompound.setInteger("consumptionRate", consumptionRate);
        tagCompound.setInteger("drainRate", drainRate);
        tagCompound.setFloat("consumptionMultiplier", consumptionMultiplier);
        tagCompound.setFloat("efficiencyMultiplier", efficiencyMultiplier);
        tagCompound.setFloat("sacrificeEfficiencyMultiplier", sacrificeEfficiencyMultiplier);
        tagCompound.setFloat("selfSacrificeEfficiencyMultiplier", selfSacrificeEfficiencyMultiplier);
        tagCompound.setBoolean("isResultBlock", isResultBlock);
        tagCompound.setFloat("capacityMultiplier", capacityMultiplier);
        tagCompound.setFloat("orbCapacityMultiplier", orbCapacityMultiplier);
        tagCompound.setFloat("dislocationMultiplier", dislocationMultiplier);
        tagCompound.setInteger("capacity", capacity);
        tagCompound.setInteger("progress", progress);
        tagCompound.setInteger("bufferCapacity", bufferCapacity);
        tagCompound.setInteger("lockdownDuration", lockdownDuration);
        tagCompound.setInteger("accelerationUpgrades", this.accelerationUpgrades);
        tagCompound.setInteger("demonBloodDuration", demonBloodDuration);
        tagCompound.setInteger("cooldownAfterCrafting", cooldownAfterCrafting);
    }

    @Override
    public void update() {
        if (getWorld().isRemote)
            return;

        if (getWorld().getTotalWorldTime() % (Math.max(20 - getUpgrade().getSpeedCount(), 1)) == 0)
            everySecond();

        if (getWorld().getTotalWorldTime() % 100 == 0)
            everyFiveSeconds();
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
        updat();
    }

    public void startCycle() {
        if (worldObj != null)
            worldObj.markBlockForUpdate(pos);

        checkTier();

        if (fluid == null || fluid.amount <= 0)
            return;

        if (!isActive) {
            progress = 0;
        }

        if (getStackInSlot(0) != null) {
            // Do recipes
            if (AltarRecipeRegistry.getRecipes().containsKey(getStackInSlot(0))) {
                AltarRecipe recipe = AltarRecipeRegistry.getRecipeForInput(getStackInSlot(0));

                if (altarTier.ordinal() >= recipe.getMinTier().ordinal()) {
                    this.liquidRequired = recipe.getSyphon();
                    this.canBeFilled = recipe.isUseTag();
                    this.consumptionRate = recipe.getConsumeRate();
                    this.drainRate = recipe.getDrainRate();
                    this.isActive = true;
                }
            }
        }

        isActive = false;
    }

    private void updat() {
        if (!isActive) {
            if (cooldownAfterCrafting > 0) {
                cooldownAfterCrafting--;
            }
        }

        if (getStackInSlot(0) == null) {
            return;
        }

        int worldTime = (int) (worldObj.getWorldTime() % 24000);

        if (worldObj.isRemote) {
            return;
        }

        if (!canBeFilled) {
            if (fluid != null && fluid.amount >= 1) {
                int stackSize = getStackInSlot(0).stackSize;
                int liquidDrained = Math.min((int) (altarTier.ordinal() >= 2 ? consumptionRate * (1 + consumptionMultiplier) : consumptionRate), fluid.amount);

                if (liquidDrained > (liquidRequired * stackSize - progress)) {
                    liquidDrained = liquidRequired * stackSize - progress;
                }

                fluid.amount = fluid.amount - liquidDrained;
                progress += liquidDrained;

                if (worldTime % 4 == 0) {
//                    SpellHelper.sendIndexedParticleToAllAround(worldObj, xCoord, yCoord, zCoord, 20, worldObj.provider.dimensionId, 1, xCoord, yCoord, zCoord);
                }

                if (progress >= liquidRequired * stackSize) {
                    ItemStack result = AltarRecipeRegistry.getRecipeForInput(getStackInSlot(0)).getOutput();
                    if (result != null) {
                        result.stackSize *= stackSize;
                    }

                    setInventorySlotContents(0, result);
                    progress = 0;

                    for (int i = 0; i < 8; i++) {
//                        SpellHelper.sendIndexedParticleToAllAround(worldObj, xCoord, yCoord, zCoord, 20, worldObj.provider.dimensionId, 4, xCoord + 0.5f, yCoord + 1.0f, zCoord + 0.5f);
                    }
                    this.isActive = false;
                }
            } else if (progress > 0) {
                progress -= (int) (efficiencyMultiplier * drainRate);

                if (worldTime % 2 == 0) {
//                    SpellHelper.sendIndexedParticleToAllAround(worldObj, xCoord, yCoord, zCoord, 20, worldObj.provider.dimensionId, 2, xCoord, yCoord, zCoord);
                }
            }
        } else {
            ItemStack returnedItem = getStackInSlot(0);

            if (!(returnedItem.getItem() instanceof IBloodOrb)) {
                return;
            }

            IBloodOrb item = (IBloodOrb) (returnedItem.getItem());
            NBTTagCompound itemTag = returnedItem.getTagCompound();

            if (itemTag == null) {
                return;
            }

            String ownerName = itemTag.getString("ownerName");

            if (ownerName.equals("")) {
                return;
            }

            if (fluid != null && fluid.amount >= 1) {
                int liquidDrained = Math.min((int) (altarTier.ordinal() >= 2 ? consumptionRate * (1 + consumptionMultiplier) : consumptionRate), fluid.amount);

                int drain = SoulNetworkHelper.addCurrentEssenceToMaximum(ownerName, liquidDrained, (int) (item.getMaxEssence(returnedItem.getMetadata()) * this.orbCapacityMultiplier));

                fluid.amount = fluid.amount - drain;

                if (worldTime % 4 == 0) {
//                    SpellHelper.sendIndexedParticleToAllAround(worldObj, xCoord, yCoord, zCoord, 20, worldObj.provider.dimensionId, 3, xCoord, yCoord, zCoord);
                }
            }
        }
        if (worldObj != null) {
            worldObj.markBlockForUpdate(pos);
        }
    }

    private void checkTier() {
        // TODO - Write checking for tier stuff

        EnumAltarTier tier = BloodAltar.getAltarTier(getWorld(), getPos());

        if (tier.equals(EnumAltarTier.ONE))
            upgrade = new AltarUpgrade();
        else
            upgrade = BloodAltar.getUpgrades(worldObj, pos, tier);

        if (this.fluid.amount > this.capacity)
            this.fluid.amount = this.capacity;

        worldObj.markBlockForUpdate(pos);
    }

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

    public TileAltar setUpgrade(AltarUpgrade upgrade) {
        this.upgrade = upgrade;
        return this;
    }

    public AltarUpgrade getUpgrade() {
        return upgrade;
    }

    public TileAltar setTier(EnumAltarTier tier) {
        this.altarTier = tier;
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
