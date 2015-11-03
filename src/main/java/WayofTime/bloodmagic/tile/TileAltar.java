package WayofTime.bloodmagic.tile;

import WayofTime.bloodmagic.altar.BloodAltar;
import WayofTime.bloodmagic.api.BloodMagicAPI;
import WayofTime.bloodmagic.api.altar.AltarRecipe;
import WayofTime.bloodmagic.api.altar.AltarUpgrade;
import WayofTime.bloodmagic.api.altar.EnumAltarTier;
import WayofTime.bloodmagic.api.registry.AltarRecipeRegistry;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.*;

public class TileAltar extends TileInventory implements IUpdatePlayerListBox, IFluidTank, IFluidHandler
{
    private int tier;
    private AltarUpgrade upgrade = new AltarUpgrade();
    private FluidStack fluid = new FluidStack(BloodMagicAPI.getLifeEssence(), 0);
    private int capacity;

    public TileAltar()
    {
        super(1, "altar");

        this.capacity = 10000;
    }

    @Override
    public void update()
    {
        if (getWorld().isRemote)
        {
            return;
        }

        if (getWorld().getTotalWorldTime() % (Math.max(20 - getUpgrade().getSpeedCount(), 1)) == 0)
        {
            everySecond();
        }

        if (getWorld().getTotalWorldTime() % 100 == 0)
        {
            everyFiveSeconds();
        }
    }

    private void everySecond()
    {
        // Do recipes
        if (AltarRecipeRegistry.getRecipes().containsKey(getStackInSlot(0)))
        {
            AltarRecipe recipe = AltarRecipeRegistry.getRecipeForInput(getStackInSlot(0));

            if (!(tier >= recipe.minTier))
            {
                return;
            }
        }
    }

    private void everyFiveSeconds()
    {
        checkTier();
    }

    private void checkTier()
    {
        // TODO - Write checking for tier stuff

        EnumAltarTier tier = BloodAltar.getAltarTier(getWorld(), getPos());

        if (tier.equals(EnumAltarTier.ONE))
        {
            upgrade = new AltarUpgrade();
            return;
        }
    }

    public TileAltar setUpgrade(AltarUpgrade upgrade)
    {
        this.upgrade = upgrade;
        return this;
    }

    public AltarUpgrade getUpgrade()
    {
        return upgrade;
    }

    public TileAltar setTier(int tier)
    {
        this.tier = tier;
        return this;
    }

    public int getTier() {
        return tier;
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
    public int getCapacity() {
        return capacity;
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
