package WayofTime.bloodmagic.livingArmour.downgrade;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.livingArmour.ILivingArmour;
import WayofTime.bloodmagic.livingArmour.LivingArmourUpgrade;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class LivingArmourUpgradeQuenched extends LivingArmourUpgrade {
    public static final int[] costs = new int[]{-100};

    public LivingArmourUpgradeQuenched(int level) {
        super(level);
    }

    @Override
    public void onTick(World world, EntityPlayer player, ILivingArmour livingArmour) {

    }

    @Override
    public String getUniqueIdentifier() {
        return BloodMagic.MODID + ".upgrade.quenched";
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
    public void writeToNBT(NBTTagCompound tag) {
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
    }

    @Override
    public String getTranslationKey() {
        return tooltipBase + "quenched";
    }

    @Override
    public boolean isDowngrade() {
        return true;
    }
}