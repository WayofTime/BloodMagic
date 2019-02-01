package WayofTime.bloodmagic.livingArmour.downgrade;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.livingArmour.ILivingArmour;
import WayofTime.bloodmagic.livingArmour.LivingArmourUpgrade;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class LivingArmourUpgradeDisoriented extends LivingArmourUpgrade {
    public static final int[] costs = new int[]{-10, -20, -30, -40, -70, -80, -100, -140, -180, -220};
    public static final double[] chance = new double[]{0.001, 0.002, 0.003, 0.004, 0.005, 0.006, 0.008, 0.010, 0.012, 0.014};

    public LivingArmourUpgradeDisoriented(int level) {
        super(level);
    }

    @Override
    public void onTick(World world, EntityPlayer player, ILivingArmour livingArmour) {
        if (world.isRemote && player.ticksExisted % 20 == 0 && world.rand.nextDouble() <= chance[this.level]) {
            player.rotationYaw = world.rand.nextFloat() * 360;
            player.rotationPitch = world.rand.nextFloat() * 180 - 90;
        }
    }

    @Override
    public boolean runOnClient() {
        return true;
    }

    @Override
    public String getUniqueIdentifier() {
        return BloodMagic.MODID + ".upgrade.disoriented";
    }

    @Override
    public int getMaxTier() {
        return 10;
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
        return tooltipBase + "disoriented";
    }

    @Override
    public boolean isDowngrade() {
        return true;
    }
}