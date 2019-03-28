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

public class LivingArmourUpgradeFireResist extends LivingArmourUpgrade {
    public static final int[] costs = new int[]{2, 6, 14, 25, 40};
    public static final int[] fireCooldownTime = new int[]{5 * 60 * 20, 5 * 60 * 20, 4 * 60 * 20, 3 * 60 * 20, 2 * 60 * 20};
    public static final int[] fireResistDuration = new int[]{30 * 20, 30 * 20, 40 * 20, 50 * 20, 60 * 20};

    public int fireCooldown = 0;

    public LivingArmourUpgradeFireResist(int level) {
        super(level);
    }

    @Override
    public void onTick(World world, EntityPlayer player, ILivingArmour livingArmour) {
        if (player.isBurning() && fireCooldown <= 0) {

            player.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, fireResistDuration[this.level]));
            fireCooldown = fireCooldownTime[this.level];

            player.sendStatusMessage(new TextComponentString(TextHelper.localizeEffect(chatBase + "fireRemove")), true);

        } else if (fireCooldown > 0) {
            fireCooldown--;
        }
    }

    @Override
    public String getUniqueIdentifier() {
        return BloodMagic.MODID + ".upgrade.fireResist";
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
        tag.setInteger(Constants.NBT.UPGRADE_FIRE_TIMER, fireCooldown);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        fireCooldown = tag.getInteger(Constants.NBT.UPGRADE_FIRE_TIMER);
    }

    @Override
    public String getTranslationKey() {
        return tooltipBase + "fireResist";
    }
}