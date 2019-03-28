package WayofTime.bloodmagic.livingArmour.upgrade;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.livingArmour.LivingArmourUpgrade;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.nbt.NBTTagCompound;
import org.apache.commons.codec.binary.StringUtils;

import java.util.UUID;

public class LivingArmourUpgradeKnockbackResist extends LivingArmourUpgrade {
    public static final int[] costs = new int[]{3, 7, 13, 26, 42};
    public static final double[] kbModifier = new double[]{0.2, 0.4, 0.6, 0.8, 1.0};
    public static final int[] healthModifier = new int[]{0, 0, 0, 4, 10};

    public LivingArmourUpgradeKnockbackResist(int level) {
        super(level);
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers() {
        Multimap<String, AttributeModifier> modifierMap = HashMultimap.create();

        String name = getUniqueIdentifier() + "-KnockbackModifier1";
        modifierMap.put(SharedMonsterAttributes.KNOCKBACK_RESISTANCE.getName(), new AttributeModifier(UUID.nameUUIDFromBytes(StringUtils.getBytesUtf8(name)), "KnockbackModifier1", kbModifier[this.level], 0));

        if (healthModifier[this.level] > 0) {
            name = getUniqueIdentifier() + "-HealthModifier1";
            modifierMap.put(SharedMonsterAttributes.MAX_HEALTH.getName(), new AttributeModifier(UUID.nameUUIDFromBytes(StringUtils.getBytesUtf8(name)), "HealthModifier1", healthModifier[this.level], 0));
        }

        return modifierMap;
    }

    @Override
    public String getUniqueIdentifier() {
        return BloodMagic.MODID + ".upgrade.knockback";
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
        // EMPTY
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        // EMPTY
    }

    @Override
    public String getTranslationKey() {
        return tooltipBase + "knockback";
    }
}
