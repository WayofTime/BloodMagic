package WayofTime.bloodmagic.livingArmour.downgrade;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.livingArmour.ILivingArmour;
import WayofTime.bloodmagic.livingArmour.LivingArmourUpgrade;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class LivingArmourUpgradeStormTrooper extends LivingArmourUpgrade {
    public static final int[] costs = new int[]{-10, -25, -40, -65, -90};
    public static final float[] inaccuracy = new float[]{0.04f, 0.08f, 0.12f, 0.16f, 0.2f};

    public LivingArmourUpgradeStormTrooper(int level) {
        super(level);
    }

    @Override
    public void onTick(World world, EntityPlayer player, ILivingArmour livingArmour) {

    }

    public float getArrowJiggle(EntityPlayer player) {
        return inaccuracy[this.level];
    }

    @Override
    public String getUniqueIdentifier() {
        return BloodMagic.MODID + ".upgrade.stormTrooper";
    }

    @Override
    public int getMaxTier() {
        return 5;
    }

    @Override
    public int getCostOfUpgrade() {
        return costs[this.level];
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
    }

    @Override
    public String getTranslationKey() {
        return tooltipBase + "stormTrooper";
    }

    @Override
    public boolean isDowngrade() {
        return true;
    }
}