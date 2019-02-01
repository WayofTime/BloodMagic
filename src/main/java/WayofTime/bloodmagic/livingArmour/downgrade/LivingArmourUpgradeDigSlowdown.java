package WayofTime.bloodmagic.livingArmour.downgrade;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.livingArmour.ILivingArmour;
import WayofTime.bloodmagic.livingArmour.LivingArmourUpgrade;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.HashMap;

public class LivingArmourUpgradeDigSlowdown extends LivingArmourUpgrade {
    public static final int[] costs = new int[]{-10, -17, -28, -42, -60, -80, -100, -125, -160, -200};
    public static final double[] digSpeedModifier = new double[]{0.9, 0.8, 0.7, 0.6, 0.55, 0.5, 0.4, 0.35, 0.3, 0.2};
    public static HashMap<ILivingArmour, Boolean> changeMap = new HashMap<>();

    public LivingArmourUpgradeDigSlowdown(int level) {
        super(level);
    }

    @Override
    public double getMiningSpeedModifier(EntityPlayer player) {
        return digSpeedModifier[this.level];
    }

    @Override
    public void onTick(World world, EntityPlayer player, ILivingArmour livingArmour) {

    }

    @Override
    public String getUniqueIdentifier() {
        return BloodMagic.MODID + ".upgrade.digSlowdown";
    }

    @Override
    public int getMaxTier() {
        return 10; // Set to here until I can add more upgrades to it.
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
        return tooltipBase + "digSlowdown";
    }

    @Override
    public boolean isDowngrade() {
        return true;
    }
}
