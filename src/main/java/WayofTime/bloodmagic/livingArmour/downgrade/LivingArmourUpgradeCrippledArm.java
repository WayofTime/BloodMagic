package WayofTime.bloodmagic.livingArmour.downgrade;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.livingArmour.ILivingArmour;
import WayofTime.bloodmagic.livingArmour.LivingArmourUpgrade;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;

public class LivingArmourUpgradeCrippledArm extends LivingArmourUpgrade {
    public static final int[] costs = new int[]{-150};

    public LivingArmourUpgradeCrippledArm(int level) {
        super(level);
    }

    @Override
    public void onTick(World world, PlayerEntity player, ILivingArmour livingArmour) {

    }

    @Override
    public String getUniqueIdentifier() {
        return BloodMagic.MODID + ".upgrade.crippledArm";
    }

    @Override
    public int getMaxTier() {
        return 1;
    }

    @Override
    public int getCostOfUpgrade() {
        return costs[this.level];
    }

    @Override
    public void writeToNBT(CompoundNBT tag) {
    }

    @Override
    public void readFromNBT(CompoundNBT tag) {
    }

    @Override
    public String getTranslationKey() {
        return tooltipBase + "crippledArm";
    }

    @Override
    public boolean isDowngrade() {
        return true;
    }
}