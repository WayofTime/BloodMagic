package WayofTime.bloodmagic.livingArmour.upgrade;

import java.util.UUID;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.nbt.NBTTagCompound;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.livingArmour.LivingArmourUpgrade;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public class LivingArmourUpgradeKnockbackResist extends LivingArmourUpgrade
{
    public static final int[] costs = new int[] { 3, 7, 13, 26, 42 };
    public static final double[] kbModifier = new double[] { 0.2, 0.4, 0.6, 0.8, 1.0 };
    public static final int[] healthModifier = new int[] { 0, 0, 0, 4, 10 };

    public LivingArmourUpgradeKnockbackResist(int level)
    {
        super(level);
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers()
    {
        Multimap<String, AttributeModifier> modifierMap = HashMultimap.<String, AttributeModifier>create();

        modifierMap.put(SharedMonsterAttributes.knockbackResistance.getAttributeUnlocalizedName(), new AttributeModifier(new UUID(895132, 1), "Knockback modifier" + 1, kbModifier[this.level], 0));

        if (healthModifier[this.level] > 0)
        {
            modifierMap.put(SharedMonsterAttributes.maxHealth.getAttributeUnlocalizedName(), new AttributeModifier(new UUID(952142, 1), "Health modifier" + 1, healthModifier[this.level], 0));
        }

        return modifierMap;
    }

    @Override
    public String getUniqueIdentifier()
    {
        return Constants.Mod.MODID + ".upgrade.knockback";
    }

    @Override
    public int getMaxTier()
    {
        return 5;
    }

    @Override
    public int getCostOfUpgrade()
    {
        return costs[this.level];
    }

    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
        // EMPTY
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        // EMPTY
    }

    @Override
    public String getUnlocalizedName()
    {
        return tooltipBase + "knockback";
    }
}
