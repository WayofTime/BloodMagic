package WayofTime.bloodmagic.livingArmour.upgrade;

import java.util.UUID;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.livingArmour.ILivingArmour;
import WayofTime.bloodmagic.api.livingArmour.LivingArmourUpgrade;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public class LivingArmourUpgradeMeleeDamage extends LivingArmourUpgrade
{
    public static final int[] costs = new int[] { 5, 12, 20, 35, 49, 78, 110, 160, 215, 320 };
    public static final double[] meleeDamage = new double[] { 0.5, 1, 1.5, 2, 2.5, 3, 4, 5, 6, 7 };

    public LivingArmourUpgradeMeleeDamage(int level)
    {
        super(level);
    }

    @Override
    public void onTick(World world, EntityPlayer player, ILivingArmour livingArmour)
    {

    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers()
    {
        Multimap<String, AttributeModifier> modifierMap = HashMultimap.<String, AttributeModifier>create();

        modifierMap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(new UUID(9423688, 1), "damage modifier" + 1, meleeDamage[this.level], 0));

        return modifierMap;
    }

    @Override
    public String getUniqueIdentifier()
    {
        return Constants.Mod.MODID + ".upgrade.meleeDamage";
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
        return tooltipBase + "meleeDamage";
    }
}