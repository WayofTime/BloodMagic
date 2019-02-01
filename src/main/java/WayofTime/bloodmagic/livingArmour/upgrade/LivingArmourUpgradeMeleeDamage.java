package WayofTime.bloodmagic.livingArmour.upgrade;

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

public class LivingArmourUpgradeMeleeDamage extends LivingArmourUpgrade {
    public static final int[] costs = new int[]{5, 12, 20, 35, 49, 78, 110, 160, 215, 320};
    public static final double[] meleeDamage = new double[]{0.5, 1, 1.5, 2, 2.5, 3, 4, 5, 6, 7};

    public LivingArmourUpgradeMeleeDamage(int level) {
        super(level);
    }

    @Override
    public void onTick(World world, EntityPlayer player, ILivingArmour livingArmour) {

    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers() {
        Multimap<String, AttributeModifier> modifierMap = HashMultimap.create();

        String name = getUniqueIdentifier() + "-DamageModifier1";
        modifierMap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(UUID.nameUUIDFromBytes(StringUtils.getBytesUtf8(name)), "DamageModifier1", meleeDamage[this.level], 0));

        return modifierMap;
    }

    @Override
    public String getUniqueIdentifier() {
        return BloodMagic.MODID + ".upgrade.meleeDamage";
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
        return tooltipBase + "meleeDamage";
    }
}