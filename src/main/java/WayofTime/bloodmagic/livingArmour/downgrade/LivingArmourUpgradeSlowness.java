package WayofTime.bloodmagic.livingArmour.downgrade;

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

public class LivingArmourUpgradeSlowness extends LivingArmourUpgrade
{
    public static final int[] costs = new int[] { -10, -17, -23, -35, -48, -60, -80, -110, -160, -200 };
    public static final double[] speedModifier = new double[] { -0.1, -0.2, -0.3, -0.4, -0.45, -0.5, -0.55, -0.6, -0.65, -0.7 };

    public LivingArmourUpgradeSlowness(int level)
    {
        super(level);
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers()
    {
        Multimap<String, AttributeModifier> modifierMap = HashMultimap.<String, AttributeModifier>create();

        modifierMap.put(SharedMonsterAttributes.MOVEMENT_SPEED.getAttributeUnlocalizedName(), new AttributeModifier(new UUID(85472, 8502), "speed modifier" + 2, speedModifier[this.level], 1));

        return modifierMap;
    }

    @Override
    public void onTick(World world, EntityPlayer player, ILivingArmour livingArmour)
    {

    }

    @Override
    public String getUniqueIdentifier()
    {
        return Constants.Mod.MODID + ".upgrade.slowness";
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
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
    }

    @Override
    public String getUnlocalizedName()
    {
        return tooltipBase + "slowness";
    }
}