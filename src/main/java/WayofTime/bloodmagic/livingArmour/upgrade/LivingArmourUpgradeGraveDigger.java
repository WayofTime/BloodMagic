package WayofTime.bloodmagic.livingArmour.upgrade;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.livingArmour.LivingArmourUpgrade;

public class LivingArmourUpgradeGraveDigger extends LivingArmourUpgrade
{
    public static final int[] costs = new int[] { 5, 12, 20, 35, 49, 78, 110, 160, 215, 320 };
    public static final double[] damageBoost = new double[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };

    public LivingArmourUpgradeGraveDigger(int level)
    {
        super(level);
    }

    @Override
    public double getAdditionalDamageOnHit(double damage, EntityPlayer wearer, EntityLivingBase hitEntity, ItemStack weapon)
    {
        if (weapon != null && weapon.getItem() instanceof ItemSpade)
        {
            return getDamageModifier();
        }

        return 0;
    }

    public double getDamageModifier()
    {
        return damageBoost[this.level];
    }

    @Override
    public String getUniqueIdentifier()
    {
        return Constants.Mod.MODID + ".upgrade.graveDigger";
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
        return tooltipBase + "graveDigger";
    }
}