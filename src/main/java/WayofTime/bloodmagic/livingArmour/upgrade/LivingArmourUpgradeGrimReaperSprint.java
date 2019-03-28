package WayofTime.bloodmagic.livingArmour.upgrade;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.livingArmour.ILivingArmour;
import WayofTime.bloodmagic.livingArmour.LivingArmourUpgrade;
import WayofTime.bloodmagic.util.helper.TextHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class LivingArmourUpgradeGrimReaperSprint extends LivingArmourUpgrade {
    public static final int[] costs = new int[]{20, 50, 130, 270, 450, 580, 700, 800, 900, 1000};
    public static final int[] rebirthDelay = new int[]{20 * 60 * 60, 20 * 60 * 50, 20 * 60 * 45, 20 * 60 * 40, 20 * 60 * 30, 20 * 60 * 25, 20 * 60 * 15, 20 * 60 * 10, 20 * 60 * 5, 20 * 60};
    public static final int[] strengthDuration = new int[]{0, 0, 100, 100, 200, 200, 200, 300, 300, 400};
    public static final int[] strengthValue = new int[]{0, 0, 0, 0, 0, 1, 1, 2, 2, 3};
    public static final int[] resistanceDuration = new int[]{100, 100, 100, 100, 100, 100, 100, 100, 100, 100};
    public static final int[] resistanceValue = new int[]{0, 0, 0, 0, 0, 1, 1, 2, 2, 3};
    public static final float[] healthOnRevive = new float[]{0.2f, 0.2f, 0.3f, 0.3f, 0.4f, 0.4f, 0.5f, 0.6f, 0.7f, 0.8f};

    public int deathTimer = 0;

    public LivingArmourUpgradeGrimReaperSprint(int level) {
        super(level);
    }

    @Override
    public void onTick(World world, EntityPlayer player, ILivingArmour livingArmour) {
        if (deathTimer > 0) {
            deathTimer--;
        }
    }

    @Override
    public String getUniqueIdentifier() {
        return BloodMagic.MODID + ".upgrade.grimReaper";
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
    public void readFromNBT(NBTTagCompound tag) {
        deathTimer = tag.getInteger(BloodMagic.MODID + ".tracker.grimReaper");
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        tag.setInteger(BloodMagic.MODID + ".tracker.grimReaper", deathTimer);
    }

    @Override
    public String getTranslationKey() {
        return tooltipBase + "grimReaper";
    }

    public void applyEffectOnRebirth(EntityPlayer player) {
        player.setHealth(player.getMaxHealth() * healthOnRevive[this.level]);

        int strDur = strengthDuration[this.level];
        if (strDur > 0) {
            player.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, strDur, strengthValue[this.level]));
        }

        int resDur = resistanceDuration[this.level];
        if (resDur > 0) {
            player.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, resDur, resistanceValue[this.level]));
        }

        deathTimer = rebirthDelay[this.level];
        player.sendStatusMessage(new TextComponentString(TextHelper.localizeEffect(chatBase + "grimReaper")), true);
    }

    public boolean canSavePlayer(EntityPlayer player) {
        return deathTimer <= 0;
    }
}
