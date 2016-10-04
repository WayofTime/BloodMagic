package WayofTime.bloodmagic.util.handler.event;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemBanner;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import WayofTime.bloodmagic.ConfigHandler;
import WayofTime.bloodmagic.annot.Handler;
import WayofTime.bloodmagic.api.BloodMagicAPI;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.event.AltarCraftedEvent;
import WayofTime.bloodmagic.api.iface.IUpgradeTrainer;
import WayofTime.bloodmagic.api.livingArmour.LivingArmourUpgrade;
import WayofTime.bloodmagic.api.util.helper.ItemHelper;
import WayofTime.bloodmagic.api.util.helper.NBTHelper;
import WayofTime.bloodmagic.item.ItemInscriptionTool;
import WayofTime.bloodmagic.registry.ModItems;

@Handler
public class CraftingHandler
{

    // Sets the uses of crafted Inscription Tools to 10
    @SubscribeEvent
    public void onAltarCrafted(AltarCraftedEvent event)
    {
        if (event.getOutput() == null)
        {
            return;
        }

        if (event.getOutput().getItem() instanceof ItemInscriptionTool)
        {
            NBTHelper.checkNBT(event.getOutput());
            event.getOutput().getTagCompound().setInteger(Constants.NBT.USES, 10);
        }

        if (event.getOutput().getItem() == ForgeModContainer.getInstance().universalBucket && event.getAltarRecipe().getSyphon() == 1000)
        {
            NBTTagCompound bucketTags = BloodMagicAPI.getLifeEssenceBucket().getTagCompound();
            event.getOutput().setTagCompound(bucketTags);
        }
    }

    // Handles crafting of: Revealing Upgrade Tome, Elytra Upgrade Tome, Combining Upgrade Tomes, Setting Upgrade for Trainer
    @SubscribeEvent
    public void onAnvil(AnvilUpdateEvent event)
    {
        if (ConfigHandler.thaumcraftGogglesUpgrade)
        {
            if (event.getLeft().getItem() == ModItems.LIVING_ARMOUR_HELMET && event.getRight().getItem() == Constants.Compat.THAUMCRAFT_GOGGLES && !event.getRight().isItemDamaged())
            {
                ItemStack output = new ItemStack(ModItems.UPGRADE_TOME);
                output = NBTHelper.checkNBT(output);
                ItemHelper.LivingUpgrades.setKey(output, Constants.Mod.MODID + ".upgrade.revealing");
                ItemHelper.LivingUpgrades.setLevel(output, 1);
                event.setCost(1);

                event.setOutput(output);

                return;
            }
        }

        if (event.getLeft().getItem() == ModItems.SIGIL_HOLDING)
        {
            if (event.getRight().getItem() == Items.NAME_TAG)
            {
                ItemStack output = event.getLeft().copy();
                if (!output.hasTagCompound())
                    output.setTagCompound(new NBTTagCompound());
                output.getTagCompound().setString(Constants.NBT.COLOR, event.getRight().getDisplayName());
                event.setCost(1);

                event.setOutput(output);

                return;
            }

            if (event.getRight().getItem() == Items.DYE)
            {
                EnumDyeColor dyeColor = ItemBanner.getBaseColor(event.getRight());
                ItemStack output = event.getLeft().copy();
                if (!output.hasTagCompound())
                    output.setTagCompound(new NBTTagCompound());
                output.getTagCompound().setString(Constants.NBT.COLOR, String.valueOf(dyeColor.getMapColor().colorValue));
                event.setCost(1);

                event.setOutput(output);

                return;
            }
        }

        if (event.getLeft().getItem() == Items.BOOK && event.getRight().getItem() == Items.ELYTRA && !event.getRight().isItemDamaged())
        {
            ItemStack output = new ItemStack(ModItems.UPGRADE_TOME);
            output = NBTHelper.checkNBT(output);
            ItemHelper.LivingUpgrades.setKey(output, Constants.Mod.MODID + ".upgrade.elytra");
            ItemHelper.LivingUpgrades.setLevel(output, 1);
            event.setCost(30);

            event.setOutput(output);

            return;
        }

        if (event.getLeft().getItem() == ModItems.UPGRADE_TOME && event.getRight().getItem() == ModItems.UPGRADE_TOME)
        {
            LivingArmourUpgrade leftUpgrade = ItemHelper.LivingUpgrades.getUpgrade(event.getLeft());
            if (leftUpgrade != null && ItemHelper.LivingUpgrades.getKey(event.getLeft()).equals(ItemHelper.LivingUpgrades.getKey(event.getRight())))
            {
                int leftLevel = ItemHelper.LivingUpgrades.getLevel(event.getLeft());
                int rightLevel = ItemHelper.LivingUpgrades.getLevel(event.getRight());

                if (leftLevel == rightLevel && leftLevel < leftUpgrade.getMaxTier() - 1)
                {
                    ItemStack outputStack = event.getLeft().copy();
                    ItemHelper.LivingUpgrades.setLevel(outputStack, leftLevel + 1);
                    event.setCost(leftLevel + 2);

                    event.setOutput(outputStack);

                    return;
                }
            }
        }

        if (event.getLeft().getItem() instanceof IUpgradeTrainer && event.getRight().getItem() == ModItems.UPGRADE_TOME)
        {
            LivingArmourUpgrade rightUpgrade = ItemHelper.LivingUpgrades.getUpgrade(event.getRight());
            if (rightUpgrade != null)
            {
                String key = ItemHelper.LivingUpgrades.getKey(event.getRight());
                ItemStack outputStack = event.getLeft().copy();
                List<String> keyList = new ArrayList<String>();
                keyList.add(key);
                if (((IUpgradeTrainer) event.getLeft().getItem()).setTrainedUpgrades(outputStack, keyList))
                {
                    event.setCost(1);

                    event.setOutput(outputStack);

                    return;
                }
            }
        }
    }
}
