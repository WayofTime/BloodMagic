package WayofTime.bloodmagic.livingArmour.upgrade;

import java.util.UUID;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.livingArmour.ILivingArmour;
import WayofTime.bloodmagic.api.livingArmour.LivingArmourUpgrade;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public class LivingArmourUpgradeSpeed extends LivingArmourUpgrade
{
    public static final int[] costs = new int[] { 3, 7, 13, 26, 42, 60, 90, 130, 180, 250 };
    public static final double[] speedModifier = new double[] { 0.1, 0.2, 0.3, 0.4, 0.5, 0.7, 0.9, 1.1, 1.3, 1.5 };
    public static final int[] sprintSpeedTime = new int[] { 0, 0, 0, 0, 0, 20, 60, 60, 100, 200 };
    public static final int[] sprintSpeedLevel = new int[] { 0, 0, 0, 0, 0, 0, 0, 1, 1, 2 };
    public static final int[] healthModifier = new int[] { 0, 0, 0, 0, 0, 0, 0, 4, 10, 20 };
    public static final int[] sprintRegenTime = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 25 };

    public LivingArmourUpgradeSpeed(int level)
    {
        super(level);
    }

    public double getSpeedModifier()
    {
        return speedModifier[this.level];
    }

    @Override
    public void onTick(World world, EntityPlayer player, ILivingArmour livingArmour)
    {
        if (player.isSprinting())
        {
            if (sprintSpeedTime[this.level] > 0)
            {
                player.addPotionEffect(new PotionEffect(Potion.moveSpeed.id, sprintSpeedTime[this.level], sprintSpeedLevel[this.level], false, false));
            }

            if (sprintRegenTime[this.level] > 0 && !player.isPotionActive(Potion.regeneration))
            {
                player.addPotionEffect(new PotionEffect(Potion.regeneration.id, sprintRegenTime[this.level], 0, false, false));
            }
        }
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers()
    {
        Multimap<String, AttributeModifier> modifierMap = HashMultimap.<String, AttributeModifier>create();

//        modifierMap.put(SharedMonsterAttributes.movementSpeed.getAttributeUnlocalizedName(), new AttributeModifier(new UUID(895132, 1), "Speed modifier" + 1, speedModifier[this.level], 1));

        if (healthModifier[this.level] > 0)
        {
            modifierMap.put(SharedMonsterAttributes.maxHealth.getAttributeUnlocalizedName(), new AttributeModifier(new UUID(952142, 1), "Health modifier" + 1, healthModifier[this.level], 0));
        }

        return modifierMap;
    }

    @Override
    public String getUniqueIdentifier()
    {
        return Constants.Mod.MODID + ".upgrade.movement";
    }

    @Override
    public int getMaxTier()
    {
        return 10;
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
        return tooltipBase + "speed";
    }
}
