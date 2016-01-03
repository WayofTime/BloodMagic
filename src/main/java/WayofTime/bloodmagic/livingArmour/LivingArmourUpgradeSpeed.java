package WayofTime.bloodmagic.livingArmour;

import java.util.UUID;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.nbt.NBTTagCompound;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.livingArmour.LivingArmourUpgrade;

public class LivingArmourUpgradeSpeed extends LivingArmourUpgrade
{
    public LivingArmourUpgradeSpeed(int level)
    {
        super(level);
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers()
    {
        Multimap<String, AttributeModifier> modifierMap = HashMultimap.<String, AttributeModifier>create();

        // Adds 5% per level
        modifierMap.put(SharedMonsterAttributes.movementSpeed.getAttributeUnlocalizedName(), new AttributeModifier(new UUID(895132, 1), "Speed modifier" + 1, (this.level + 1) * 0.05, 1));

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
        return 3 * (this.level + 1) * (this.level + 1) * (this.level + 1);
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
}
