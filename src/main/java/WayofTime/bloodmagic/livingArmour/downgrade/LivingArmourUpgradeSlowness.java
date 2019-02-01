package WayofTime.bloodmagic.livingArmour.downgrade;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.livingArmour.ILivingArmour;
import WayofTime.bloodmagic.livingArmour.LivingArmourUpgrade;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import org.apache.commons.codec.binary.StringUtils;

import java.util.UUID;

public class LivingArmourUpgradeSlowness extends LivingArmourUpgrade {
    public static final int[] costs = new int[]{-10, -17, -23, -35, -48, -60, -80, -110, -160, -200};
    public static final double[] speedModifier = new double[]{-0.1, -0.2, -0.3, -0.4, -0.45, -0.5, -0.55, -0.6, -0.65, -0.7};

    public LivingArmourUpgradeSlowness(int level) {
        super(level);
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers() {
        Multimap<String, AttributeModifier> modifierMap = HashMultimap.create();

        String name = getUniqueIdentifier() + "-SpeedModifier1";
        modifierMap.put(SharedMonsterAttributes.MOVEMENT_SPEED.getName(), new AttributeModifier(UUID.nameUUIDFromBytes(StringUtils.getBytesUtf8(name)), "SpeedModifier1", speedModifier[this.level], 1));

        return modifierMap;
    }

    @Override
    public void onTick(World world, EntityPlayer player, ILivingArmour livingArmour) {

    }

    @Override
    public String getUniqueIdentifier() {
        return BloodMagic.MODID + ".upgrade.slowness";
    }

    @Override
    public int getMaxTier() {
        return 10;
    }

    @Override
    public int getCostOfUpgrade() {
        return costs[this.level];
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
    }

    @Override
    public String getTranslationKey() {
        return tooltipBase + "slowness";
    }

    @Override
    public boolean isDowngrade() {
        return true;
    }
}