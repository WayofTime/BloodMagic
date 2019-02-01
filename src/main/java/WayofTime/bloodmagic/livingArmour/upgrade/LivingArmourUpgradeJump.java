package WayofTime.bloodmagic.livingArmour.upgrade;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.livingArmour.ILivingArmour;
import WayofTime.bloodmagic.livingArmour.LivingArmourUpgrade;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class LivingArmourUpgradeJump extends LivingArmourUpgrade {
    public static final int[] costs = new int[]{3, 6, 11, 23, 37, 50, 70, 100, 140, 200};
    public static final double[] jumpModifier = new double[]{0.10, 0.2, 0.3, 0.4, 0.5, 0.7, 0.9, 1.1, 1.3, 1.5};
    public static final double[] fallModifier = new double[]{0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.75, 0.8, 0.85};

    public LivingArmourUpgradeJump(int level) {
        super(level);
    }

    public double getJumpModifier() {
        return jumpModifier[this.level];
    }

    @Override
    public String getUniqueIdentifier() {
        return BloodMagic.MODID + ".upgrade.jump";
    }

    @Override
    public int getMaxTier() {
        return 10; // Set to here until I can add more upgrades to it.
    }

    @Override
    public void onTick(World world, EntityPlayer player, ILivingArmour livingArmour) {
        if (!world.isRemote) {
            double motionY = player.motionY;

            if (motionY < 0) {
                player.fallDistance = (float) Math.max(0, player.fallDistance + motionY * fallModifier[this.level]);
            }
        }
    }

    @Override
    public int getCostOfUpgrade() {
        return costs[this.level];
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        // EMPTY
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        // EMPTY
    }

    @Override
    public String getTranslationKey() {
        return tooltipBase + "jump";
    }
}
