package wayoftime.bloodmagic.common.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;
import wayoftime.bloodmagic.common.datacomponent.BMDataComponents;
import wayoftime.bloodmagic.common.datacomponent.Binding;
import wayoftime.bloodmagic.common.datamap.BMDataMaps;
import wayoftime.bloodmagic.common.datamap.BloodOrb;
import wayoftime.bloodmagic.common.event.BloodMagicCraftedEvent;
import wayoftime.bloodmagic.common.fluid.BMFluids;
import wayoftime.bloodmagic.common.recipe.BMRecipes;
import wayoftime.bloodmagic.common.recipe.bloodaltar.BloodAltarInput;
import wayoftime.bloodmagic.common.recipe.bloodaltar.BloodAltarRecipe;
import wayoftime.bloodmagic.common.tag.BMTags;
import wayoftime.bloodmagic.util.EnumRuneType;
import wayoftime.bloodmagic.util.AltarUtil;
import wayoftime.bloodmagic.util.SoulTicket;
import wayoftime.bloodmagic.util.helper.SoulNetworkHelper;

import java.util.Map;
import java.util.Optional;

public class BloodAltarTile extends BaseTile implements IFluidHandler {

    public boolean isActive = false;
    public boolean canFill = false;
    public BloodAltarRecipe currentRecipe = null;
    public int cooldownAfterCrafting = 0;
    public int progress = 0;
    public int tier = 0;
    public int ticks;
    public int inputTank = 0;
    public int outputTank = 0;
    public int mainTank = 0;
    public int chargingTank = 0;
    public boolean isSignaling = false;
    public ItemStackHandler inv = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            setChanged();
        }
    };

    private float capacityMod = 1;
    private int tickRate = 20;
    private float consumptionMod = 1;
    private float sacrificeMod = 1;
    private float selfSacMod = 1;
    private float dislocationMod = 1;
    private float orbCapMod = 1;
    private float chargeAmountMod = 1;
    private float chargeCapMod = 1;
    private float efficiencyMod = 1;

    public BloodAltarTile(BlockPos pos, BlockState blockState) {
        super(BMTiles.BLOOD_ALTAR_TYPE.get(), pos, blockState);
    }

    public void calculateStats(Map<EnumRuneType, Integer> upgrades) {
        capacityMod = (float) ((1D + 0.2D * upgrades.getOrDefault(EnumRuneType.CAPACITY, 0) * Math.pow(1.075, upgrades.getOrDefault(EnumRuneType.AUGMENTED_CAPACITY, 0))));
        tickRate = Math.max(1, 20 - upgrades.getOrDefault(EnumRuneType.ACCELERATION, 0));
        consumptionMod = 0.2F * upgrades.getOrDefault(EnumRuneType.SPEED, 0);
        sacrificeMod = 0.1F * upgrades.getOrDefault(EnumRuneType.SACRIFICE, 0);
        selfSacMod = 0.1F * upgrades.getOrDefault(EnumRuneType.SELF_SACRIFICE, 0);
        dislocationMod = (float) Math.pow(1.2, upgrades.getOrDefault(EnumRuneType.DISPLACEMENT, 0));
        orbCapMod = 0.2F * upgrades.getOrDefault(EnumRuneType.ORB, 0);
        chargeAmountMod = (10 * upgrades.getOrDefault(EnumRuneType.CHARGING, 0) * (1 + consumptionMod/2));
        chargeCapMod = (float) Math.max(0.5 * capacityMod, 1) * upgrades.getOrDefault(EnumRuneType.CHARGING, 0);
        efficiencyMod = (float) Math.pow(0.85, upgrades.getOrDefault(EnumRuneType.EFFICIENCY, 0));
    }

    public static void tick(Level level, BlockPos pos, BlockState state, BloodAltarTile tile) {
        if (level.isClientSide) {
            return;
        }

        if (tile.isSignaling) {
            tile.isSignaling = false;
        }

        tile.ticks++;
        if (tile.ticks % (20 * 5) == 0) {
            int newTier = AltarUtil.getTier(level, pos);
            Map<EnumRuneType, Integer> newUpgrades = AltarUtil.getUpgrades(newTier, level, pos);
            tile.calculateStats(newUpgrades);
            tile.setChanged();
            if (tile.isActive || tile.cooldownAfterCrafting <= 0) {
                tile.checkAction();
            }
        }

        if (tile.ticks % Math.max(tile.tickRate, 1) == 0) {
            float ioAmount = 20F * tile.dislocationMod;
            int input = (int) Math.min(tile.inputTank, ioAmount);
            input = (int) Math.min(input, tile.getMainCapacity() - tile.mainTank);
            tile.inputTank -= input;
            tile.mainTank += input;

            int output = (int) Math.min(tile.mainTank, ioAmount);
            output = (int) Math.min(output, tile.getIOCapacity() - tile.outputTank);
            tile.mainTank -= output;
            tile.outputTank += output;

            if (!tile.isActive) {
                tile.progress = 0;
                int charge = (int) Math.min(tile.mainTank, tile.chargeAmountMod);
                charge = (int) Math.min(charge, tile.getChargingCapacity() - tile.chargingTank);
                tile.mainTank -= charge;
                tile.chargingTank += charge;
            }
        }

        if (!tile.isActive && tile.cooldownAfterCrafting > 0) {
            tile.cooldownAfterCrafting--;
            if (tile.cooldownAfterCrafting <= 0) {
                tile.checkAction();
            }
            return;
        }

        if (!tile.canFill && tile.currentRecipe == null) {
            tile.checkAction();
            return;
        }

        ItemStack inputStack = tile.inv.getStackInSlot(0);
        if (inputStack.isEmpty()) {
            return;
        }

        if (!tile.canFill) {
            boolean hasOperated = false;
            int inputSize = inputStack.getCount();
            if (tile.chargingTank > 0) {
                int chargeDrained = Math.min(tile.currentRecipe.totalBlood * inputSize - tile.progress, tile.chargingTank);
                tile.chargingTank -= chargeDrained;
                tile.progress += chargeDrained;
                hasOperated = true;
            }
            if (tile.mainTank > 0) {
                int drained = Math.min(tile.mainTank, (int) (tile.currentRecipe.craftSpeed * (1 + tile.consumptionMod)));
                drained = Math.min(drained, tile.currentRecipe.totalBlood * inputSize - tile.progress);
                tile.mainTank -= drained;
                tile.progress += drained;
                hasOperated = true;

                if (tile.ticks % 4 == 0) {
                    ((ServerLevel) level).sendParticles(DustParticleOptions.REDSTONE, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, 1, 0.2, 1.0, 0.2, 0);
                }
            } else if (!hasOperated && tile.progress > 0) {
                tile.progress -= (int) (tile.currentRecipe.drainSpeed * (1 + tile.efficiencyMod));
                if (tile.progress < 0) {
                    tile.progress = 0;
                }
                if (tile.ticks % 2 == 0) {
                    ((ServerLevel) level).sendParticles(ParticleTypes.LARGE_SMOKE, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, 1, 0.1, 1.0, 0.1, 0);
                }
            }

            if (hasOperated && tile.progress >= tile.currentRecipe.totalBlood * inputSize) {
                ItemStack result = tile.currentRecipe.getResult().copyWithCount(inputSize);
                BloodMagicCraftedEvent.Altar event = new BloodMagicCraftedEvent.Altar(result, inputStack);
                NeoForge.EVENT_BUS.post(event);
                tile.inv.setStackInSlot(0, event.getOutput());
                if (level.getBlockState(pos.below()).is(BMTags.Blocks.PULSE_ON_CRAFTING)) {
                    tile.isSignaling = true;
                }
                tile.progress = 0;
                tile.cooldownAfterCrafting = 30;
                tile.isActive = false;
                tile.currentRecipe = null;
                ((ServerLevel) level).sendParticles(DustParticleOptions.REDSTONE, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, 40, 0.3, 0.0, 0.3, 0);
            }
        } else {
            Binding binding = inputStack.getOrDefault(BMDataComponents.BINDING, Binding.EMPTY);
            BloodOrb orb = inputStack.getItemHolder().getData(BMDataMaps.BLOOD_ORB_STATS);
            if (binding.isEmpty() || orb == null) {
                return;
            }
            if (tile.mainTank > 0) {
                int available = Math.min(tile.mainTank, (int) (orb.fillRate() * (1 + tile.consumptionMod)));
                int drained = SoulNetworkHelper.getSoulNetwork(binding.uuid()).add(SoulTicket.block(level, pos, available), (int) (orb.capacity() * (1 + tile.orbCapMod)));
                tile.mainTank -= drained;
                if (drained > 0) {
                    ((ServerLevel) level).sendParticles(ParticleTypes.WITCH, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, 1, 0, 0, 0, 0.001);
                }
            }
        }

        tile.setChanged();
    }

    public void sacrificialDaggerCall(int lpAdded, boolean isSacrifice) {
        mainTank = mainTank + Math.min((getMainCapacity() - mainTank), (int) ((isSacrifice ? 1 + sacrificeMod : 1 + selfSacMod) * lpAdded));
        setChanged();
    }

    public void checkAction() {
        if (!isActive) {
            progress = 0;
        }

        ItemStack inputStack = inv.getStackInSlot(0);
        Binding inputBinding = inputStack.get(BMDataComponents.BINDING);
        Optional<RecipeHolder<BloodAltarRecipe>> optionalHolder = level.getRecipeManager().getRecipeFor(BMRecipes.BLOOD_ALTAR_TYPE.get(), new BloodAltarInput(inputStack, tier), level);
        if (!(inputBinding == null || inputBinding.isEmpty())) {
            canFill = true;
            isActive = true;
            currentRecipe = null;
            return;
        } else if (optionalHolder.isPresent()) {
            currentRecipe = optionalHolder.get().value();
            isActive = true;
            canFill = false;
            return;
        }
        isActive = false;
    }

    public int analogSignal() {
        if (level.getBlockState(getBlockPos().below()).is(BMTags.Blocks.SOUL_NETWORK_COMPARATOR)) {
            ItemStack content = inv.getStackInSlot(0);
            Binding binding = content.getOrDefault(BMDataComponents.BINDING, Binding.EMPTY);
            BloodOrb orb = content.getItemHolder().getData(BMDataMaps.BLOOD_ORB_STATS);
            if (binding.isEmpty() || orb == null) {
                return 0;
            }
            float current = SoulNetworkHelper.getSoulNetwork(binding).getCurrentEssence();
            float max = (int) ((float) orb.capacity() * (1 + orbCapMod));
            return Mth.lerpDiscrete(current / max, 0, 15);
        }

        return Mth.lerpDiscrete((float) mainTank / (float) getMainCapacity(), 0, 15);
    }

    public int getMainCapacity() {
        return (int) ((float) FluidType.BUCKET_VOLUME * 10F * capacityMod);
    }

    public int getIOCapacity() {
        return (int) ((float) FluidType.BUCKET_VOLUME * 1F * capacityMod);
    }

    public int getChargingCapacity() {
        return (int) ((float) FluidType.BUCKET_VOLUME * chargeCapMod);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        CompoundTag stats = tag.getCompound("stats");
        tickRate = stats.getInt("tickrate");
        ticks = stats.getInt("ticks");
        capacityMod = stats.getFloat("capacity");
        consumptionMod = stats.getFloat("consumption");
        efficiencyMod = stats.getFloat("efficiency");
        sacrificeMod = stats.getFloat("sacrifice");
        selfSacMod = stats.getFloat("selfsacrifice");
        dislocationMod = stats.getFloat("dislocation");
        orbCapMod = stats.getFloat("orb");
        chargeAmountMod = stats.getFloat("chargeamount");
        chargeCapMod = stats.getFloat("chargecap");

        CompoundTag tanks = tag.getCompound("tanks");

        inputTank = tanks.getInt("input");
        outputTank = tanks.getInt("output");
        mainTank = tanks.getInt("main");
        chargingTank = tanks.getInt("charging");
        progress = tanks.getInt("progress");

        inv.deserializeNBT(registries, tag.getCompound("inventory"));

        this.isSignaling = tag.getBoolean("signal");

        this.tier = tag.getInt("tier");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        CompoundTag stats = new CompoundTag();
        stats.putInt("tickrate", tickRate);
        stats.putInt("ticks", ticks % 2048);
        stats.putFloat("capacity", capacityMod);
        stats.putFloat("consumption", consumptionMod);
        stats.putFloat("efficiency", efficiencyMod);
        stats.putFloat("sacrifice", sacrificeMod);
        stats.putFloat("selfsacrifice", selfSacMod);
        stats.putFloat("dislocation", dislocationMod);
        stats.putFloat("orb", orbCapMod);
        stats.putFloat("chargeamount", chargeAmountMod);
        stats.putFloat("chargecap", chargeCapMod);

        CompoundTag tanks = new CompoundTag();
        tanks.putInt("input", inputTank);
        tanks.putInt("output", outputTank);
        tanks.putInt("main", mainTank);
        tanks.putInt("charging", chargingTank);
        tanks.putInt("progress", progress);

        CompoundTag inventory = inv.serializeNBT(registries);

        tag.put("tanks", tanks);

        tag.put("inventory", inventory);

        tag.put("stats", stats);
        tag.putInt("tier", this.tier);
        tag.putBoolean("signal", isSignaling);
    }

    @Override
    public void setChanged() {
        super.setChanged();
        this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), Block.UPDATE_ALL); // send to client
    }

    @Override
    public int getTanks() {
        return 3;
    }

    @Override
    public FluidStack getFluidInTank(int tank) {
        return switch (tank) {
            case 0 -> new FluidStack(BMFluids.LIFE_ESSENCE_SOURCE, mainTank);
            case 1 -> new FluidStack(BMFluids.LIFE_ESSENCE_SOURCE, inputTank);
            case 2 -> new FluidStack(BMFluids.LIFE_ESSENCE_SOURCE, outputTank);
            default -> FluidStack.EMPTY;
        };
    }

    @Override
    public int getTankCapacity(int tank) {
        return switch (tank) {
          case 0 -> getMainCapacity();
          case 1,2 -> getIOCapacity();
          default -> 0;
        };
    }

    @Override
    public boolean isFluidValid(int tank, FluidStack stack) {
        return stack.is(BMFluids.LIFE_ESSENCE_TYPE.get());
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        if (!isFluidValid(0, resource)) {
            return 0;
        }
        int canFill = Math.min(getIOCapacity() - inputTank, 0);
        canFill = Math.max(canFill, resource.getAmount());

        if (action.execute()) {
            inputTank += canFill;
            this.setChanged();
        }

        return canFill;
    }

    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        if (!isFluidValid(0, resource)) {
            return FluidStack.EMPTY;
        }

        return drain(resource.getAmount(), action);
    }

    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        int toDrain = Math.min(outputTank, maxDrain);

        if (action.execute()) {
            outputTank -= toDrain;
            this.setChanged();
        }

        return new FluidStack(BMFluids.LIFE_ESSENCE_SOURCE, toDrain);
    }
}
