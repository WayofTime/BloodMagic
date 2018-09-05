package com.wayoftime.bloodmagic.tile;

import com.wayoftime.bloodmagic.api.event.BloodMagicCraftedEvent;
import com.wayoftime.bloodmagic.api.impl.BloodMagicAPI;
import com.wayoftime.bloodmagic.api.impl.recipe.RecipeBloodAltar;
import com.wayoftime.bloodmagic.core.RegistrarBloodMagic;
import com.wayoftime.bloodmagic.core.altar.AltarTier;
import com.wayoftime.bloodmagic.core.altar.AltarUpgrades;
import com.wayoftime.bloodmagic.core.altar.AltarUtil;
import com.wayoftime.bloodmagic.core.network.*;
import com.wayoftime.bloodmagic.item.IBindable;
import com.wayoftime.bloodmagic.tile.base.TileBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

/*
 * TODO - See checklist
 * - [-] Upgrades
 *  - [x] Self Sacrifice
 *  - [x] Sacrifice
 *  - [x] Capacity
 *  - [x] Augmented Capacity
 *  - [ ] Speed
 *  - [ ] Displacement
 *  - [ ] Orb
 *  - [ ] Acceleration
 *  - [ ] Charging
 * - [-] Tanks
 *  - [x] Main tank keeps buffer filled
 *  - [ ] Buffer fills main tank
 */
public class TileBloodAltar extends TileBase implements ITickable {

    private FluidTank tank = new FluidTank(new FluidStack(RegistrarBloodMagic.FLUID_LIFE_ESSENCE, 0), 10000);
    private FluidTank buffer = new FluidTank(new FluidStack(RegistrarBloodMagic.FLUID_LIFE_ESSENCE, 0), 1000); // Buffer has 10% the capacity of the tank
    private ItemStackHandler itemHandler = new ItemStackHandler(1);
    private AltarTier currentTier = AltarTier.ONE;
    private AltarUpgrades upgrades = new AltarUpgrades();
    private float progress;
    private RecipeBloodAltar recipe = null;
    private int drained;
    private long lastCompletionTime;

    public TileBloodAltar() {
        tank.setTileEntity(this);
    }

    @Override
    public void update() {
        if (getWorld().getTotalWorldTime() % 20 == 0) {
            currentTier = AltarUtil.getTier(getWorld(), getPos());
            upgrades = AltarUtil.getUpgrades(getWorld(), pos, currentTier);
            handleTankUpdates();
        }

        if (getWorld().getTotalWorldTime() - lastCompletionTime > 20)
            handleRecipe();
    }

    protected void handleTankUpdates() {
        tank.setCapacity((int) (Fluid.BUCKET_VOLUME * 10 * upgrades.getCapacityModifier()));
        if (tank.getCapacity() < tank.getFluidAmount())
            tank.setFluid(new FluidStack(RegistrarBloodMagic.FLUID_LIFE_ESSENCE, tank.getCapacity()));

        buffer.setCapacity((int) (tank.getCapacity() * 0.1D));
        if (buffer.getCapacity() < buffer.getFluidAmount())
            buffer.setFluid(new FluidStack(RegistrarBloodMagic.FLUID_LIFE_ESSENCE, buffer.getCapacity()));

        if (buffer.getCapacity() > buffer.getFluidAmount() && tank.getFluidAmount() > 0) {
            FluidStack drained = tank.drain(100, true);
            if (drained != null)
                buffer.fill(drained, true);
        }
    }

    protected void handleRecipe() {
        ItemStack inputItem = itemHandler.getStackInSlot(0);
        if (inputItem.isEmpty())
            return;

        // Only look for a recipe if the altar currently has blood in it as well as the current recipe is null or the item doesn't match the recipe input
        if (tank.getFluidAmount() > 0 && (recipe == null || !recipe.getInput().apply(inputItem))) {
            RecipeBloodAltar newRecipe = BloodMagicAPI.INSTANCE.getRecipeRegistrar().getBloodAltar(inputItem);
            if (newRecipe != null && newRecipe.getMinimumTier().ordinal() <= currentTier.ordinal())
                recipe = newRecipe;
        }

        // Match orbs
        if (recipe == null) {
            if (inputItem.getItem() instanceof IBloodOrb && inputItem.getItem() instanceof IBindable) {
                BloodOrb orb = ((IBloodOrb) inputItem.getItem()).getOrb(inputItem);
                Binding binding = ((IBindable) inputItem.getItem()).getBinding(inputItem);
                if (orb != null && binding != null) {
                    if (currentTier.ordinal() < orb.getTier())
                        return;

                    SoulNetwork network = SoulNetwork.get(binding.getOwnerId());
                    if (network.getEssence() < orb.getCapacity()) {
                        FluidStack drained = tank.drain(new FluidStack(RegistrarBloodMagic.FLUID_LIFE_ESSENCE, orb.getFillRate()), true);
                        if (drained != null)
                            network.submitInteraction(NetworkInteraction.asItemInfo(inputItem, world, pos, drained.amount));
                    }
                }
            }

            return;
        }

        // Check tier
        if (recipe.getMinimumTier().ordinal() > currentTier.ordinal())
            return;

        // Check and handle progress loss
        if (tank.getFluidAmount() <= 0) {
            progress = Math.max(0, progress - (recipe.getDrainRate() / (recipe.getSyphon() * inputItem.getCount())));
            if (getWorld() instanceof WorldServer)
                ((WorldServer) getWorld()).spawnParticle(EnumParticleTypes.SMOKE_NORMAL, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, 1, 0.1, 0, 0.1, 0);
            return;
        }

        FluidStack tankDrain = tank.drain(new FluidStack(RegistrarBloodMagic.FLUID_LIFE_ESSENCE, recipe.getConsumeRate()), true);
        if (tankDrain == null)
            return;

        if (getWorld() instanceof WorldServer)
            ((WorldServer) getWorld()).spawnParticle(EnumParticleTypes.REDSTONE, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, 1, 0.2, 0, 0.2, 0);

        drained += tankDrain.amount;
        progress = (float) drained / (Math.max(recipe.getSyphon() * inputItem.getCount(), 1));

        if (progress >= 1.0F) {
            BloodMagicCraftedEvent.Altar event = new BloodMagicCraftedEvent.Altar(recipe.getOutput(), inputItem);
            MinecraftForge.EVENT_BUS.post(event);

            if (getWorld() instanceof WorldServer)
                ((WorldServer) getWorld()).spawnParticle(EnumParticleTypes.REDSTONE, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, 40, 0.3, 0, 0.3, 0);

            itemHandler.setStackInSlot(0, event.getOutput());
            lastCompletionTime = getWorld().getTotalWorldTime();
            resetProgress();
        }
    }

    @Override
    public void deserialize(NBTTagCompound tag) {
        tank.readFromNBT(tag.getCompoundTag("tank"));
        tank.setTileEntity(this);
        itemHandler.deserializeNBT(tag.getCompoundTag("inventory"));
        progress = tag.getFloat("progress");
        currentTier = AltarTier.VALUES[tag.getInteger("tier")];
        drained = tag.getInteger("drained");
        lastCompletionTime = tag.getLong("lastCompletionTime");
    }

    @Override
    public void serialize(NBTTagCompound tag) {
        tag.setTag("tank", tank.writeToNBT(new NBTTagCompound()));
        tag.setTag("inventory", itemHandler.serializeNBT());
        tag.setFloat("progress", progress);
        tag.setInteger("tier", currentTier.ordinal());
        tag.setInteger("drained", drained);
        tag.setLong("lastCompletionTime", lastCompletionTime);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return super.hasCapability(capability, facing) || capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return facing == null ? (T) tank : (T) buffer;

        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return (T) itemHandler;

        return super.getCapability(capability, facing);
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound tag = new NBTTagCompound();
        writeToNBT(tag);
        ItemStack contained = itemHandler.getStackInSlot(0);
        if (!contained.isEmpty()) {
            NBTTagCompound itemTag = new NBTTagCompound();
            itemTag.setString("item", contained.getItem().getRegistryName().toString());
            itemTag.setInteger("count", contained.getCount());
            itemTag.setInteger("data", contained.getMetadata());
            NBTTagCompound shareTag = contained.getItem().getNBTShareTag(contained);
            if (shareTag != null)
                itemTag.setTag("nbt", shareTag);

            tag.setTag("item", itemTag);
        }

        tag.setTag("tank", tank.writeToNBT(new NBTTagCompound()));
        tag.setFloat("progress", progress);
        return tag;
    }

    @Override
    public void handleUpdateTag(NBTTagCompound tag) {
        readCurrentPos(tag);
        if (tag.hasKey("item")) {
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(tag.getString("item")));
            int count = tag.getInteger("count");
            int meta = tag.getInteger("data");
            NBTTagCompound nbt = null;
            if (tag.hasKey("nbt"))
                nbt = tag.getCompoundTag("nbt");

            ItemStack stack = new ItemStack(item, count, meta);
            if (nbt != null)
                stack.deserializeNBT(nbt);
            itemHandler.setStackInSlot(0, stack);
        } else {
            itemHandler.setStackInSlot(0, ItemStack.EMPTY);
        }
        tank.readFromNBT(tag.getCompoundTag("tank"));
        progress = tag.getFloat("progress");
    }

    public void resetProgress() {
        recipe = null;
        drained = 0;
        progress = 0.0F;
    }

    public FluidTank getTank() {
        return tank;
    }

    public boolean isCrafting() {
        return recipe != null;
    }

    public AltarTier getCurrentTier() {
        return currentTier;
    }

    public float getProgress() {
        return progress;
    }

    public AltarUpgrades getUpgrades() {
        return upgrades;
    }
}
