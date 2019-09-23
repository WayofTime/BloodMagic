package WayofTime.bloodmagic.livingArmour.upgrade;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.livingArmour.ILivingArmour;
import WayofTime.bloodmagic.livingArmour.LivingArmourUpgrade;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;

public class LivingArmourUpgradeRepairing extends LivingArmourUpgrade {
    public static final int[] costs = new int[]{15};
    public static final int[] repairDelay = new int[]{200};

    int maxRepair = 1;

    int delay = 0;

    public LivingArmourUpgradeRepairing(int level) {
        super(level);
    }

    @Override
    public void onTick(World world, PlayerEntity player, ILivingArmour livingArmour) {
        if (delay <= 0) {
            delay = repairDelay[this.level];

            EquipmentSlotType randomSlot = EquipmentSlotType.values()[2 + world.rand.nextInt(4)];
            ItemStack repairStack = player.getItemStackFromSlot(randomSlot);
            if (!repairStack.isEmpty()) {
                if (repairStack.isItemStackDamageable() && repairStack.isItemDamaged()) {
                    int toRepair = Math.min(maxRepair, repairStack.getItemDamage());
                    if (toRepair > 0) {
                        repairStack.setItemDamage(repairStack.getItemDamage() - toRepair);
                    }
                }
            }
        } else {
            delay--;
        }
    }

    @Override
    public String getUniqueIdentifier() {
        return BloodMagic.MODID + ".upgrade.repair";
    }

    @Override
    public int getMaxTier() {
        return 1; // Set to here until I can add more upgrades to it.
    }

    @Override
    public int getCostOfUpgrade() {
        return costs[this.level];
    }

    @Override
    public void writeToNBT(CompoundNBT tag) {
        tag.putInt("repairingDelay", delay);
    }

    @Override
    public void readFromNBT(CompoundNBT tag) {
        delay = tag.getInt("repairingDelay");
    }

    @Override
    public String getTranslationKey() {
        return tooltipBase + "repair";
    }
}