package wayoftime.bloodmagic.common.blockentity;

import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.RangedWrapper;
import org.jetbrains.annotations.Nullable;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.menu.LivingStationMenu;
import wayoftime.bloodmagic.common.datacomponent.BMDataComponents;
import wayoftime.bloodmagic.common.datacomponent.UpgradeTome;
import wayoftime.bloodmagic.common.item.BMItems;
import wayoftime.bloodmagic.common.living.LivingHelper;
import wayoftime.bloodmagic.common.living.LivingUpgrade;
import wayoftime.bloodmagic.common.tag.BMTags;
import wayoftime.bloodmagic.common.tag.TagsCache;

import java.util.ArrayList;
import java.util.List;

public class LivingStationTile extends BaseTile implements MenuProvider {

    public Object2FloatOpenHashMap<Holder<LivingUpgrade>> upgradeData;
    public int storedScrap;
    public LivingStationTile(BlockPos pos, BlockState blockState) {
        super(BMTiles.LIVING_STATION_TYPE.get(), pos, blockState);
        upgradeData = new Object2FloatOpenHashMap<>();
    }

    public final ItemStackHandler inv = new ItemStackHandler(3 + TagsCache.getUpgradeTooltipOrder().size()) {
        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            UpgradeTome tome = stack.get(BMDataComponents.UPGRADE_TOME_DATA);
            int scrap = stack.getOrDefault(BMDataComponents.UPGRADE_SCRAP, -1);
            return switch (slot) {
                case 0 -> stack.is(BMItems.UPGRADE_TOME)
                        && tome != null
                        && TagsCache.getUpgradeTooltipOrder().contains(tome.upgrade());
                case 1 ->
                        scrap >= 0 || (stack.is(BMItems.UPGRADE_TOME) && tome != null && tome.upgrade().is(BMTags.Living.IS_SCRAPPABLE));
                default -> false;
            };
        }

        // inserting from slots. aand when set on client
        @Override
        public void setStackInSlot(int slot, ItemStack stack) {
            if (level.isClientSide && slot >= 2) { // assuming server version is also triggered here
                super.setStackInSlot(slot, stack); // this *should* set up the internal list with the server provided stacks
                return;
            }

            UpgradeTome tome = stack.get(BMDataComponents.UPGRADE_TOME_DATA);
            if (slot == 0) {
                if (tome != null) {
                    upgradeData.computeFloat(tome.upgrade(), (holder, exp) -> {
                        double existing = exp == null ? 0 : exp;
                        double added = tome.exp();
                        double result = existing + added;
                        return result > Float.MAX_VALUE ? Float.MAX_VALUE : (float) result;
                    });
                }
            } else if (slot == 1) {
                long scrap = 0;
                if (tome != null) {
                    Pair<Integer, Float> tomeScrap = LivingHelper.scrapFromTome(tome);
                    scrap += tomeScrap.left();
                    float expUsed = tomeScrap.right();
                    upgradeData.computeFloat(tome.upgrade(), (holder, exp) -> exp == null ? tome.exp() - expUsed : exp + tome.exp() - expUsed);
                }
                scrap += (long) stack.getOrDefault(BMDataComponents.UPGRADE_SCRAP, 0) * stack.getCount();
                storedScrap = Math.toIntExact(Math.min(((long) storedScrap + scrap), Integer.MAX_VALUE));
            } else {
                BloodMagic.LOGGER.info("something called setStackInSlot({}, {}) client: {}", slot, stack, level == null ? "null" : level.isClientSide);
            }
            onContentsChanged(slot);
        }

        @Override
        public ItemStack getStackInSlot(int slot) {
            if (slot < 2) {
                return ItemStack.EMPTY;
            }
            if (level.isClientSide) {
                return super.getStackInSlot(slot);
            }
            ItemStack ret;
            if (slot == 2) {
                ret = new ItemStack(BMItems.UPGRADE_SCRAP);
                ret.set(BMDataComponents.UPGRADE_SCRAP, storedScrap);
            } else {
                ret = new ItemStack(BMItems.UPGRADE_TOME);
                Holder<LivingUpgrade> upgrade = TagsCache.getUpgradeTooltipOrder().get(slot - 3); // should be fine, we're never here if its client side
                ret.set(BMDataComponents.UPGRADE_TOME_DATA, new UpgradeTome(upgrade, upgradeData.getOrDefault(upgrade, 0)));
            }

            return ret;
        }

        // probably automation
        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            BloodMagic.LOGGER.info("insertItem({}, {}, {}) client: {}", slot, stack, simulate, level == null ? "null" : level.isClientSide);
            return stack;
        }

        // probably also automation... but slots as well here
        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            if (level.isClientSide) {
                return super.extractItem(slot, amount, simulate);
            }

            ItemStack ret = getStackInSlot(slot); // do this *before* removing the values *could* actually help with it not giving 0
            if (!simulate) {
                if (slot == 2) {
                    storedScrap = 0;
                } else {
                    upgradeData.removeFloat(TagsCache.getUpgradeTooltipOrder().get(slot - 3));
                }

                onContentsChanged(slot);
            }

            return ret;
        }

        @Override
        protected void onContentsChanged(int slot) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
            setChanged();
        }
    };

    public ItemStackHandler itemCap = new ItemStackHandler() {
        @Override
        public int getSlots() {
            return 2;
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            UpgradeTome tome = stack.get(BMDataComponents.UPGRADE_TOME_DATA);
            if (slot == 0) {
                return stack.is(BMItems.UPGRADE_TOME) && (tome != null && tome.upgrade().is(BMTags.Living.TOOLTIP_ORDER));
            } else if (slot == 1) {
                return stack.is(BMItems.SYNTHETIC_POINT)
                        || stack.is(BMItems.UPGRADE_SCRAP)
                        || (stack.is(BMItems.UPGRADE_TOME) && (tome != null && tome.upgrade().is(BMTags.Living.IS_SCRAPPABLE)));
            }

            return false;
        }

        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            return inv.extractItem(slot, amount, simulate);
        }

        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            if (!isItemValid(slot, stack)) {
                return stack;
            }
            BloodMagic.LOGGER.info("{}, {}, {}", slot, stack, simulate);

            if (!simulate) {
                inv.setStackInSlot(slot, stack);
            }

            return ItemStack.EMPTY;
        }

        @Override
        public void setStackInSlot(int slot, ItemStack stack) {
            inv.setStackInSlot(slot, stack);
        }

        @Override
        public ItemStack getStackInSlot(int slot) {
            return inv.getStackInSlot(slot);
        }
    };

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        storedScrap = tag.getInt("stored_scrap");
        CompoundTag data = tag.getCompound("upgrade_data");
        TagsCache.getUpgradeTooltipOrder().forEach(holder -> {
            upgradeData.put(holder, data.getFloat(holder.getKey().location().toString()));
        });
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("stored_scrap", storedScrap);
        CompoundTag data = new CompoundTag();
        upgradeData.forEach((holder, exp) -> {
            data.putFloat(holder.getKey().location().toString(), exp);
        });
        tag.put("upgrade_data", data);
    }

    @Override
    protected void applyImplicitComponents(DataComponentInput input) {
        upgradeData = input.getOrDefault(BMDataComponents.STORED_UPGRADES, LivingHelper.EMPTY_UPGRADE_MAP);
        storedScrap = input.getOrDefault(BMDataComponents.UPGRADE_SCRAP, 0);
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder builder) {
        builder.set(BMDataComponents.STORED_UPGRADES, upgradeData);
        builder.set(BMDataComponents.UPGRADE_SCRAP, storedScrap);
    }

    @Override
    public void removeComponentsFromTag(CompoundTag tag) {
        tag.remove("upgrade_data");
        tag.remove("stored_scrap");
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Living Tinker Station");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new LivingStationMenu(containerId, playerInventory, this.inv);
    }
}
