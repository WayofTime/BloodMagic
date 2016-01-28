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

public class LivingArmourUpgradeHealthboost extends LivingArmourUpgrade
{
    public static final int[] costs = new int[] { 5, 12, 20, 35, 49, 78, 110, 160, 215, 320 };
    public static final int[] healthModifier = new int[] { 4, 8, 12, 16, 20, 26, 32, 38, 44, 50 };

    public LivingArmourUpgradeHealthboost(int level)
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

        modifierMap.put(SharedMonsterAttributes.maxHealth.getAttributeUnlocalizedName(), new AttributeModifier(new UUID(9423688, 1), "Health modifier" + 1, healthModifier[this.level], 0));

        return modifierMap;
    }

    @Override
    public String getUniqueIdentifier()
    {
        return Constants.Mod.MODID + ".upgrade.health";
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
        return tooltipBase + "health";
    }
}