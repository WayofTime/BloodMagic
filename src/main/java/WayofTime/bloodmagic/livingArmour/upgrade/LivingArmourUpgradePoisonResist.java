package WayofTime.bloodmagic.livingArmour.upgrade;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.util.Constants;
import WayofTime.bloodmagic.livingArmour.ILivingArmour;
import WayofTime.bloodmagic.livingArmour.LivingArmourUpgrade;
import WayofTime.bloodmagic.util.helper.TextHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class LivingArmourUpgradePoisonResist extends LivingArmourUpgrade {
    public static final int[] costs = new int[]{2, 6, 14, 25, 40};
    public static final int[] poisonCooldownTime = new int[]{1200, 800, 600, 300, 100};
    public static final int[] poisonMaxCure = new int[]{0, 1, 2, 2, 3};

    public int poisonCooldown = 0;

    public LivingArmourUpgradePoisonResist(int level) {
        super(level);
    }

    @Override
    public void onTick(World world, EntityPlayer player, ILivingArmour livingArmour) {
        if (player.isPotionActive(MobEffects.POISON) && poisonCooldown <= 0) {
            PotionEffect eff = player.getActivePotionEffect(MobEffects.POISON);
            if (eff.getAmplifier() <= poisonMaxCure[this.level]) {
                player.removePotionEffect(MobEffects.POISON);
                poisonCooldown = poisonCooldownTime[this.level];

                player.sendStatusMessage(new TextComponentString(TextHelper.localize(chatBase + "poisonRemove")), true);
            }
        } else if (poisonCooldown > 0) {
            poisonCooldown--;
        }
    }

    @Override
    public String getUniqueIdentifier() {
        return BloodMagic.MODID + ".upgrade.poisonResist";
    }

    @Override
    public int getMaxTier() {
        return 5; // Set to here until I can add more upgrades to it.
    }

    @Override
    public int getCostOfUpgrade() {
        return costs[this.level];
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        tag.setInteger(Constants.NBT.UPGRADE_POISON_TIMER, poisonCooldown);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        poisonCooldown = tag.getInteger(Constants.NBT.UPGRADE_POISON_TIMER);
    }

    @Override
    public String getTranslationKey() {
        return tooltipBase + "poisonResist";
    }
}