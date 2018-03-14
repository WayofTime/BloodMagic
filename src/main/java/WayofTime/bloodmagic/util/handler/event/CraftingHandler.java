package WayofTime.bloodmagic.util.handler.event;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.item.types.ComponentTypes;
import WayofTime.bloodmagic.util.Constants;
import WayofTime.bloodmagic.event.AltarCraftedEvent;
import WayofTime.bloodmagic.iface.IUpgradeTrainer;
import WayofTime.bloodmagic.livingArmour.LivingArmourUpgrade;
import WayofTime.bloodmagic.util.Utils;
import WayofTime.bloodmagic.util.helper.ItemHelper;
import WayofTime.bloodmagic.util.helper.NBTHelper;
import WayofTime.bloodmagic.block.BlockLifeEssence;
import WayofTime.bloodmagic.core.RegistrarBloodMagicItems;
import WayofTime.bloodmagic.item.ItemInscriptionTool;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemBanner;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = BloodMagic.MODID)
public class CraftingHandler {

    // Sets the uses of crafted Inscription Tools to 10
    @SubscribeEvent
    public static void onAltarCrafted(AltarCraftedEvent event) {
        if (event.getOutput() == null) {
            return;
        }

        if (event.getOutput().getItem() instanceof ItemInscriptionTool) {
            NBTHelper.checkNBT(event.getOutput());
            event.getOutput().getTagCompound().setInteger(Constants.NBT.USES, 10);
        }

        if (event.getOutput().getItem() == ForgeModContainer.getInstance().universalBucket && event.getAltarRecipe().getSyphon() == 1000) {
            NBTTagCompound bucketTags = FluidUtil.getFilledBucket(new FluidStack(BlockLifeEssence.getLifeEssence(), Fluid.BUCKET_VOLUME)).getTagCompound();
            event.getOutput().setTagCompound(bucketTags);
        }
    }

    // Handles crafting of: Revealing Upgrade Tome, Elytra Upgrade Tome, Combining Upgrade Tomes, Setting Upgrade for Trainer
    @SubscribeEvent
    public static void onAnvil(AnvilUpdateEvent event) {
        // TODO - Azanor come back :(
//        if (ConfigHandler.thaumcraftGogglesUpgrade) {
//            if (event.getLeft().getItem() == RegistrarBloodMagicItems.LIVING_ARMOUR_HELMET && event.getRight().getItem() == Constants.Compat.THAUMCRAFT_GOGGLES && !event.getRight().isItemDamaged()) {
//                ItemStack output = new ItemStack(RegistrarBloodMagicItems.UPGRADE_TOME);
//                output = NBTHelper.checkNBT(output);
//                ItemHelper.LivingUpgrades.setKey(output, BloodMagic.MODID + ".upgrade.revealing");
//                ItemHelper.LivingUpgrades.setLevel(output, 1);
//                event.setCost(1);
//
//                event.setOutput(output);
//
//                return;
//            }
//        }

        if (event.getLeft().getItem() == RegistrarBloodMagicItems.SIGIL_HOLDING) {
            if (event.getRight().getItem() == Items.NAME_TAG) {
                ItemStack output = event.getLeft().copy();
                if (!output.hasTagCompound())
                    output.setTagCompound(new NBTTagCompound());
                output.getTagCompound().setString(Constants.NBT.COLOR, event.getRight().getDisplayName());
                event.setCost(1);

                event.setOutput(output);

                return;
            }

            if (event.getRight().getItem() == Items.DYE) {
                EnumDyeColor dyeColor = ItemBanner.getBaseColor(event.getRight());
                ItemStack output = event.getLeft().copy();
                if (!output.hasTagCompound())
                    output.setTagCompound(new NBTTagCompound());
                output.getTagCompound().setString(Constants.NBT.COLOR, String.valueOf(Utils.DYE_COLOR_VALUES.getOrDefault(dyeColor, 0)));
                event.setCost(1);

                event.setOutput(output);

                return;
            }
        }

        if (event.getLeft().getItem() == Items.BOOK && event.getRight().getItem() == Items.ELYTRA && !event.getRight().isItemDamaged()) {
            ItemStack output = new ItemStack(RegistrarBloodMagicItems.UPGRADE_TOME);
            output = NBTHelper.checkNBT(output);
            ItemHelper.LivingUpgrades.setKey(output, BloodMagic.MODID + ".upgrade.elytra");
            ItemHelper.LivingUpgrades.setLevel(output, 1);
            event.setCost(30);

            event.setOutput(output);

            return;
        }

        if (event.getLeft().getItem() == RegistrarBloodMagicItems.UPGRADE_TOME && event.getRight().getItem() == RegistrarBloodMagicItems.UPGRADE_TOME) {
            LivingArmourUpgrade leftUpgrade = ItemHelper.LivingUpgrades.getUpgrade(event.getLeft());
            if (leftUpgrade != null && !leftUpgrade.isDowngrade() && ItemHelper.LivingUpgrades.getKey(event.getLeft()).equals(ItemHelper.LivingUpgrades.getKey(event.getRight()))) {
                int leftLevel = ItemHelper.LivingUpgrades.getLevel(event.getLeft());
                int rightLevel = ItemHelper.LivingUpgrades.getLevel(event.getRight());

                if (leftLevel == rightLevel && leftLevel < leftUpgrade.getMaxTier() - 1) {
                    ItemStack outputStack = event.getLeft().copy();
                    ItemHelper.LivingUpgrades.setLevel(outputStack, leftLevel + 1);
                    event.setCost(leftLevel + 2);

                    event.setOutput(outputStack);

                    return;
                }
            }
        }

        if (event.getLeft().getItem() instanceof IUpgradeTrainer && event.getRight().getItem() == RegistrarBloodMagicItems.UPGRADE_TOME) {
            LivingArmourUpgrade rightUpgrade = ItemHelper.LivingUpgrades.getUpgrade(event.getRight());
            if (rightUpgrade != null) {
                String key = ItemHelper.LivingUpgrades.getKey(event.getRight());
                ItemStack outputStack = event.getLeft().copy();
                List<String> keyList = new ArrayList<>();
                keyList.add(key);
                if (((IUpgradeTrainer) event.getLeft().getItem()).setTrainedUpgrades(outputStack, keyList)) {
                    event.setCost(1);

                    event.setOutput(outputStack);
                }
            }
        }
    }

    @SubscribeEvent
    public static void handleFuelLevel(FurnaceFuelBurnTimeEvent event) {
        if (ItemStack.areItemsEqual(event.getItemStack(), ComponentTypes.SAND_COAL.getStack()))
            event.setBurnTime(TileEntityFurnace.getItemBurnTime(new ItemStack(Items.COAL)));
    }
}
