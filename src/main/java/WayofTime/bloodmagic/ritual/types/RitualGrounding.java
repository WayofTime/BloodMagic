package WayofTime.bloodmagic.ritual.types;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.core.RegistrarBloodMagic;
import WayofTime.bloodmagic.demonAura.WorldDemonWillHandler;
import WayofTime.bloodmagic.ritual.*;
import WayofTime.bloodmagic.soul.DemonWillHolder;
import WayofTime.bloodmagic.soul.EnumDemonWillType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.Consumer;

@RitualRegister("grounding")
public class RitualGrounding extends Ritual {

    public static final int willRefreshTime = 20;
    public static final String GROUNDING_RANGE = "groundingRange";
    public static final double willDrain = 0.5;

    public RitualGrounding() {
        super("ritualGrounding", 0, 5000, "ritual." + BloodMagic.MODID + ".groundingRitual");
        addBlockRange(GROUNDING_RANGE, new AreaDescriptor.Rectangle(new BlockPos(-10, 0, -10), 21, 30, 21));
        setMaximumVolumeAndDistanceOfRange(GROUNDING_RANGE, 0, 200, 200);
    }

    @Override
    public void performRitual(IMasterRitualStone masterRitualStone) {
        /* Default Ritual Stuff */
        World world = masterRitualStone.getWorldObj();
        int currentEssence = masterRitualStone.getOwnerNetwork().getCurrentEssence();
        BlockPos pos = masterRitualStone.getBlockPos();

        if (currentEssence < getRefreshCost()) {
            masterRitualStone.getOwnerNetwork().causeNausea();
            return;
        }

        int maxEffects = currentEssence / getRefreshCost();
        int totalEffects = 0;

        /* Default will augment stuff */
        List<EnumDemonWillType> willConfig = masterRitualStone.getActiveWillConfig();
        DemonWillHolder holder = WorldDemonWillHandler.getWillHolder(world, pos);

        double rawWill = this.getWillRespectingConfig(world, pos, EnumDemonWillType.DEFAULT, willConfig);
        double corrosiveWill = this.getWillRespectingConfig(world, pos, EnumDemonWillType.CORROSIVE, willConfig);
        double destructiveWill = this.getWillRespectingConfig(world, pos, EnumDemonWillType.DESTRUCTIVE, willConfig);
        double steadfastWill = this.getWillRespectingConfig(world, pos, EnumDemonWillType.STEADFAST, willConfig);
        double vengefulWill = this.getWillRespectingConfig(world, pos, EnumDemonWillType.VENGEFUL, willConfig);

        double rawDrained = 0;
        double corrosiveDrained = 0;
        double destructiveDrained = 0;
        double steadfastDrained = 0;
        double vengefulDrained = 0;

        /* Actual ritual stuff begins here */
        AreaDescriptor groundingRange = masterRitualStone.getBlockRange(GROUNDING_RANGE);
        List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, groundingRange.getAABB(pos));
        for (EntityLivingBase entity : entities) {
            if (totalEffects >= maxEffects) {
                break;
            }

            if (entity instanceof EntityPlayer && ((EntityPlayer) entity).isCreative())
                continue;

            totalEffects++;


            if (entity instanceof EntityPlayer) {
                /* Raw will effect: Affects players */
                if (world.getTotalWorldTime() % 10 == 0) {
                    if (rawWill >= willDrain) {

                        rawDrained += willDrain;

                        double[] drainagePlayer = sharedWillEffects(world, entity, corrosiveWill, destructiveWill, vengefulWill, corrosiveDrained, destructiveDrained, vengefulDrained);

                        corrosiveDrained += drainagePlayer[0];
                        destructiveDrained += drainagePlayer[1];
                        vengefulDrained += drainagePlayer[2];
                    }
                }
            } else if (entity.isNonBoss()) {
                if (world.getTotalWorldTime() % 10 == 0) {
                    double[] drainageEntity = sharedWillEffects(world, entity, corrosiveWill, destructiveWill, vengefulWill, corrosiveDrained, destructiveDrained, vengefulDrained);

                    corrosiveDrained += drainageEntity[0];
                    destructiveDrained += drainageEntity[1];
                    vengefulDrained += drainageEntity[2];
                }
            } else if (!entity.isNonBoss()) {
                /* Steadfast will effect: Affects bosses
                (some bosses, like the wither, have a restriction to motion modification,
                others, like the Ender Dragon, don't do potions) */
                if (steadfastWill >= willDrain) {
                    if (entity instanceof EntityWither || entity instanceof EntityDragon)
                        entity.move(MoverType.SELF, 0, -0.05, 0); // to work on Wither and EnderDragon without interfering with other mod author's decisions (looking at you, Vazkii)

                    steadfastDrained += willDrain / 10f;

                    double[] drainagePlayer = sharedWillEffects(world, entity, corrosiveWill, destructiveWill, vengefulWill, corrosiveDrained, destructiveDrained, vengefulDrained);

                    corrosiveDrained += drainagePlayer[0];
                    destructiveDrained += drainagePlayer[1];
                    vengefulDrained += drainagePlayer[2];
                }
            }
        }


        if (rawDrained > 0)
            WorldDemonWillHandler.drainWill(world, pos, EnumDemonWillType.DEFAULT, rawDrained, true);
        if (corrosiveDrained > 0)
            WorldDemonWillHandler.drainWill(world, pos, EnumDemonWillType.CORROSIVE, corrosiveDrained, true);
        if (destructiveDrained > 0)
            WorldDemonWillHandler.drainWill(world, pos, EnumDemonWillType.DESTRUCTIVE, destructiveDrained, true);
        if (steadfastDrained > 0)
            WorldDemonWillHandler.drainWill(world, pos, EnumDemonWillType.STEADFAST, steadfastDrained, true);
        if (vengefulDrained > 0)
            WorldDemonWillHandler.drainWill(world, pos, EnumDemonWillType.VENGEFUL, vengefulDrained, true);

        masterRitualStone.getOwnerNetwork().syphon(masterRitualStone.ticket(getRefreshCost() * totalEffects));
    }

    @Override
    public int getRefreshTime() {
        return 1;
    }

    @Override
    public int getRefreshCost() {
        return Math.max(1, getBlockRange(GROUNDING_RANGE).getVolume() / 10000);
    }

    @Override
    public void gatherComponents(Consumer<RitualComponent> components) {
        addParallelRunes(components, 1, 0, EnumRuneType.DUSK);
        addCornerRunes(components, 2, 2, EnumRuneType.EARTH);
        addCornerRunes(components, 3, 3, EnumRuneType.EARTH);
    }


    @Override
    public Ritual getNewCopy() {
        return new RitualGrounding();
    }

    public double[] sharedWillEffects(World world, EntityLivingBase entity, double corrosiveWill, double destructiveWill, double vengefulWill, double corrosiveDrained, double destructiveDrained, double vengefulDrained) {
        /* Combination of corrosive + vengeful will: Levitation */
        if (corrosiveWill >= willDrain && vengefulWill >= willDrain) {

            entity.addPotionEffect(new PotionEffect(MobEffects.LEVITATION, 20, 10));
            vengefulDrained += willDrain;
            corrosiveDrained += willDrain;

            /* Corrosive will effect: Suspension */
        } else if (corrosiveWill >= willDrain) {

            entity.addPotionEffect(new PotionEffect(RegistrarBloodMagic.SUSPENDED, 20, 0));
            corrosiveDrained += willDrain;

            /* Vengeful will effect: Stronger effect */
        } else if (vengefulWill >= willDrain) {

            vengefulDrained += willDrain;
            entity.addPotionEffect(new PotionEffect(RegistrarBloodMagic.GROUNDED, 40, 20));

        } else

            entity.addPotionEffect(new PotionEffect(RegistrarBloodMagic.GROUNDED, 20, 10));

        /* Destructive will effect: Increased fall damage */
        if (destructiveWill >= willDrain) {
            destructiveDrained += willDrain;

            /* Combination of destructive + vengeful will: stronger destructive effect */
            if (vengefulWill >= willDrain + vengefulDrained) {
                if (world.getTotalWorldTime() % 100 == 0) {
                    vengefulDrained += willDrain;
                    entity.addPotionEffect(new PotionEffect(RegistrarBloodMagic.HEAVY_HEART, 200, 2));
                }

            } else if (world.getTotalWorldTime() % 50 == 0)
                entity.addPotionEffect(new PotionEffect(RegistrarBloodMagic.HEAVY_HEART, 100, 1));
        }
        return new double[]{corrosiveDrained, destructiveDrained, vengefulDrained};
    }
}