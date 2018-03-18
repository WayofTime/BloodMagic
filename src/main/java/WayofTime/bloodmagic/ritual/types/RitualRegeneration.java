package WayofTime.bloodmagic.ritual.types;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.ritual.*;
import WayofTime.bloodmagic.util.DamageSourceBloodMagic;
import WayofTime.bloodmagic.soul.EnumDemonWillType;
import WayofTime.bloodmagic.demonAura.WorldDemonWillHandler;
import WayofTime.bloodmagic.util.Utils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class RitualRegeneration extends Ritual {
    public static final String HEAL_RANGE = "heal";
    public static final String VAMPIRE_RANGE = "vampire";

    public static final int SACRIFICE_AMOUNT = 100;

    public static final double corrosiveWillDrain = 0.04;

    public RitualRegeneration() {
        super("ritualRegeneration", 0, 25000, "ritual." + BloodMagic.MODID + ".regenerationRitual");
        addBlockRange(HEAL_RANGE, new AreaDescriptor.Rectangle(new BlockPos(-15, -15, -15), 31));
        addBlockRange(VAMPIRE_RANGE, new AreaDescriptor.Rectangle(new BlockPos(-15, -15, -15), 31));

        setMaximumVolumeAndDistanceOfRange(HEAL_RANGE, 0, 20, 20);
        setMaximumVolumeAndDistanceOfRange(VAMPIRE_RANGE, 0, 20, 20);
    }

    @Override
    public void performRitual(IMasterRitualStone masterRitualStone) {
        World world = masterRitualStone.getWorldObj();
        int currentEssence = masterRitualStone.getOwnerNetwork().getCurrentEssence();

        if (currentEssence < getRefreshCost()) {
            masterRitualStone.getOwnerNetwork().causeNausea();
            return;
        }

        BlockPos pos = masterRitualStone.getBlockPos();

        int maxEffects = currentEssence / getRefreshCost();
        int totalEffects = 0;

        int totalCost = 0;

        List<EnumDemonWillType> willConfig = masterRitualStone.getActiveWillConfig();

        double rawWill = this.getWillRespectingConfig(world, pos, EnumDemonWillType.DEFAULT, willConfig);
        double steadfastWill = this.getWillRespectingConfig(world, pos, EnumDemonWillType.STEADFAST, willConfig);
        double corrosiveWill = this.getWillRespectingConfig(world, pos, EnumDemonWillType.CORROSIVE, willConfig);
        double destructiveWill = this.getWillRespectingConfig(world, pos, EnumDemonWillType.DESTRUCTIVE, willConfig);
        double vengefulWill = this.getWillRespectingConfig(world, pos, EnumDemonWillType.VENGEFUL, willConfig);

        double vengefulDrain = 0;
        double steadfastDrain = 0;
        double destructiveDrain = 0;
        double corrosiveDrain = 0;

        boolean syphonHealth = corrosiveWill >= corrosiveWillDrain;
        boolean applyAbsorption = false;
        float absorptionRate = 1;
        int maxAbsorption = 20;

        AreaDescriptor healArea = getBlockRange(HEAL_RANGE);
        AxisAlignedBB healRange = healArea.getAABB(pos);

        AreaDescriptor damageArea = getBlockRange(VAMPIRE_RANGE);
        AxisAlignedBB damageRange = damageArea.getAABB(pos);

        List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, healRange);
        List<EntityPlayer> players = world.getEntitiesWithinAABB(EntityPlayer.class, healRange);
        List<EntityLivingBase> damagedEntities = world.getEntitiesWithinAABB(EntityLivingBase.class, damageRange);

        if (syphonHealth) {
            for (EntityPlayer player : players) {
                if (player.getHealth() <= player.getMaxHealth() - 1) {
                    float syphonedHealthAmount = getSyphonAmountForWill(corrosiveWill);
                    Collections.shuffle(damagedEntities);
                    for (EntityLivingBase damagedEntity : damagedEntities) {
                        if (damagedEntity instanceof EntityPlayer) {
                            continue;
                        }

                        float currentHealth = damagedEntity.getHealth();

                        damagedEntity.attackEntityFrom(DamageSourceBloodMagic.INSTANCE, Math.min(player.getMaxHealth() - player.getHealth(), syphonedHealthAmount));

                        float healthDifference = currentHealth - damagedEntity.getHealth();
                        if (healthDifference > 0) {
                            corrosiveDrain += corrosiveWillDrain;
                            corrosiveWill -= corrosiveWillDrain;
                            player.heal(healthDifference);
                        }

                        break;
                    }
                }
            }
        }

        for (EntityLivingBase entity : entities) {
            float health = entity.getHealth();
            if (health <= entity.getMaxHealth() - 1) {
                if (entity.isPotionApplicable(new PotionEffect(MobEffects.REGENERATION))) {
                    if (entity instanceof EntityPlayer) {
                        totalCost += getRefreshCost();
                        currentEssence -= getRefreshCost();
                    } else {
                        totalCost += getRefreshCost() / 10;
                        currentEssence -= getRefreshCost() / 10;
                    }

                    entity.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 50, 0, false, false));

                    totalEffects++;

                    if (totalEffects >= maxEffects) {
                        break;
                    }
                }
            }
            if (applyAbsorption && entity instanceof EntityPlayer) {
                if (applyAbsorption) {
                    float added = Utils.addAbsorptionToMaximum(entity, absorptionRate, maxAbsorption, 1000);
                }
            }
        }

        if (corrosiveDrain > 0) {
            WorldDemonWillHandler.drainWill(world, pos, EnumDemonWillType.CORROSIVE, corrosiveDrain, true);
        }

        masterRitualStone.getOwnerNetwork().syphon(totalCost);
    }

    @Override
    public int getRefreshTime() {
        return 50;
    }

    @Override
    public int getRefreshCost() {
        return SACRIFICE_AMOUNT;
    }

    @Override
    public void gatherComponents(Consumer<RitualComponent> components) {
        components.accept(new RitualComponent(new BlockPos(4, 0, 0), EnumRuneType.AIR));
        components.accept(new RitualComponent(new BlockPos(5, 0, -1), EnumRuneType.AIR));
        components.accept(new RitualComponent(new BlockPos(5, 0, 1), EnumRuneType.AIR));
        components.accept(new RitualComponent(new BlockPos(-4, 0, 0), EnumRuneType.AIR));
        components.accept(new RitualComponent(new BlockPos(-5, 0, -1), EnumRuneType.AIR));
        components.accept(new RitualComponent(new BlockPos(-5, 0, 1), EnumRuneType.AIR));
        components.accept(new RitualComponent(new BlockPos(0, 0, 4), EnumRuneType.FIRE));
        components.accept(new RitualComponent(new BlockPos(1, 0, 5), EnumRuneType.FIRE));
        components.accept(new RitualComponent(new BlockPos(-1, 0, 5), EnumRuneType.FIRE));
        components.accept(new RitualComponent(new BlockPos(0, 0, -4), EnumRuneType.FIRE));
        components.accept(new RitualComponent(new BlockPos(1, 0, -5), EnumRuneType.FIRE));
        components.accept(new RitualComponent(new BlockPos(-1, 0, -5), EnumRuneType.FIRE));
        addOffsetRunes(components, 3, 5, 0, EnumRuneType.WATER);
        addCornerRunes(components, 3, 0, EnumRuneType.DUSK);
        addOffsetRunes(components, 4, 5, 0, EnumRuneType.EARTH);
        addOffsetRunes(components, 4, 5, -1, EnumRuneType.EARTH);
        addCornerRunes(components, 5, 0, EnumRuneType.EARTH);
    }

    @Override
    public Ritual getNewCopy() {
        return new RitualRegeneration();
    }

    public float getSyphonAmountForWill(double corrosiveWill) {
        return 1;
    }
}
