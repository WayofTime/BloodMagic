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

public class LivingArmourUpgradeMeleeDecrease extends LivingArmourUpgrade {
    public static final int[] costs = new int[]{-10, -17, -28, -42, -60, -80, -100, -125, -160, -200};
    public static final double[] meleeDamage = new double[]{-0.1, -0.2, -0.25, -0.3, -0.35, -0.4, -0.5, -0.6, -0.7, -0.8};

    public LivingArmourUpgradeMeleeDecrease(int level) {
        super(level);
    }

    @Override
    public void onTick(World world, EntityPlayer player, ILivingArmour livingArmour) {

    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers() {
        Multimap<String, AttributeModifier> modifierMap = HashMultimap.create();

        String name = getUniqueIdentifier() + "-DamageModifier1";
        modifierMap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(UUID.nameUUIDFromBytes(StringUtils.getBytesUtf8(name)), "DamageModifier1", meleeDamage[this.level], 1));

        return modifierMap;
    }

    @Override
    public String getUniqueIdentifier() {
        return BloodMagic.MODID + ".upgrade.meleeDecrease";
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
        // EMPTY
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        // EMPTY
    }

    @Override
    public String getTranslationKey() {
        return tooltipBase + "meleeDecrease";
    }

    @Override
    public boolean isDowngrade() {
        return true;
    }
}