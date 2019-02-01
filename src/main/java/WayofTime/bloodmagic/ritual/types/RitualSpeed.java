package WayofTime.bloodmagic.ritual.types;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.ritual.*;
import WayofTime.bloodmagic.soul.EnumDemonWillType;
import WayofTime.bloodmagic.demonAura.WorldDemonWillHandler;
import WayofTime.bloodmagic.util.Utils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.Consumer;

@RitualRegister("speed")
public class RitualSpeed extends Ritual {
    public static final String SPEED_RANGE = "sanicRange";

    public static final double vengefulWillDrain = 0.05;
    public static final double destructiveWillDrain = 0.05;
    public static final double rawWillDrain = 0.1;

    public RitualSpeed() {
        super("ritualSpeed", 0, 1000, "ritual." + BloodMagic.MODID + ".speedRitual");
        addBlockRange(SPEED_RANGE, new AreaDescriptor.Rectangle(new BlockPos(-2, 1, -2), new BlockPos(2, 5, 2)));
        setMaximumVolumeAndDistanceOfRange(SPEED_RANGE, 0, 2, 5);
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

        List<EnumDemonWillType> willConfig = masterRitualStone.getActiveWillConfig();

        double corrosiveWill = this.getWillRespectingConfig(world, pos, EnumDemonWillType.CORROSIVE, willConfig);
        double destructiveWill = this.getWillRespectingConfig(world, pos, EnumDemonWillType.DESTRUCTIVE, willConfig);
        double rawWill = this.getWillRespectingConfig(world, pos, EnumDemonWillType.DEFAULT, willConfig);
        double steadfastWill = this.getWillRespectingConfig(world, pos, EnumDemonWillType.STEADFAST, willConfig);
        double vengefulWill = this.getWillRespectingConfig(world, pos, EnumDemonWillType.VENGEFUL, willConfig);

        AreaDescriptor speedRange = masterRitualStone.getBlockRange(SPEED_RANGE);

        double vengefulDrain = 0;
        double destructiveDrain = 0;
        double rawDrain = 0;

        if (rawWill < rawWillDrain) {
            rawWill = 0; //Simplifies later calculations
        }

        for (EntityLivingBase entity : world.getEntitiesWithinAABB(EntityLivingBase.class, speedRange.getAABB(masterRitualStone.getBlockPos()))) {
            if (entity.isSneaking())
                continue;

            boolean transportChildren = destructiveWill < destructiveWillDrain;
            boolean transportAdults = vengefulWill < vengefulWillDrain;

            if ((entity.isChild() && !transportChildren) || (!entity.isChild() && !transportAdults)) {
                continue;
            }

            if (entity instanceof EntityPlayer && (transportChildren ^ transportAdults)) {
                continue;
            }

            if (!transportChildren) {
                destructiveWill -= destructiveWillDrain;
                destructiveDrain += destructiveWillDrain;
            }

            if (!transportAdults) {
                vengefulWill -= vengefulWillDrain;
                vengefulDrain += vengefulWillDrain;
            }

            double motionY = getVerticalSpeedForWill(rawWill);
            double speed = getHorizontalSpeedForWill(rawWill);
            EnumFacing direction = masterRitualStone.getDirection();

            if (rawWill >= rawWillDrain) {
                rawWill -= rawWillDrain;
                rawDrain += rawWillDrain;
            }

            entity.motionY = motionY;
            entity.fallDistance = 0;

            switch (direction) {
                case NORTH:
                    entity.motionX = 0;
                    entity.motionY = motionY;
                    entity.motionZ = -speed;
                    break;

                case SOUTH:
                    entity.motionX = 0;
                    entity.motionY = motionY;
                    entity.motionZ = speed;
                    break;

                case WEST:
                    entity.motionX = -speed;
                    entity.motionY = motionY;
                    entity.motionZ = 0;
                    break;

                case EAST:
                    entity.motionX = speed;
                    entity.motionY = motionY;
                    entity.motionZ = 0;
                    break;
                default:
                    break;
            }

            if (entity instanceof EntityPlayer) {
                Utils.setPlayerSpeedFromServer((EntityPlayer) entity, entity.motionX, entity.motionY, entity.motionZ);
            }
        }

        if (rawDrain > 0) {
            WorldDemonWillHandler.drainWill(world, pos, EnumDemonWillType.DEFAULT, rawDrain, true);
        }

        if (vengefulDrain > 0) {
            WorldDemonWillHandler.drainWill(world, pos, EnumDemonWillType.VENGEFUL, vengefulDrain, true);
        }

        if (destructiveDrain > 0) {
            WorldDemonWillHandler.drainWill(world, pos, EnumDemonWillType.DESTRUCTIVE, destructiveDrain, true);
        }
    }

    @Override
    public int getRefreshTime() {
        return 1;
    }

    @Override
    public int getRefreshCost() {
        return 5;
    }

    @Override
    public void gatherComponents(Consumer<RitualComponent> components) {
        addRune(components, 0, 0, -2, EnumRuneType.DUSK);
        addRune(components, 1, 0, -1, EnumRuneType.AIR);
        addRune(components, -1, 0, -1, EnumRuneType.AIR);
        for (int i = 0; i < 3; i++) {
            addRune(components, 2, 0, i, EnumRuneType.AIR);
            addRune(components, -2, 0, i, EnumRuneType.AIR);
        }
    }

    @Override
    public Ritual getNewCopy() {
        return new RitualSpeed();
    }

    @Override
    public ITextComponent[] provideInformationOfRitualToPlayer(EntityPlayer player) {
        return new ITextComponent[]{new TextComponentTranslation(this.getTranslationKey() + ".info"), new TextComponentTranslation(this.getTranslationKey() + ".default.info"), new TextComponentTranslation(this.getTranslationKey() + ".corrosive.info"), new TextComponentTranslation(this.getTranslationKey() + ".steadfast.info"), new TextComponentTranslation(this.getTranslationKey() + ".destructive.info"), new TextComponentTranslation(this.getTranslationKey() + ".vengeful.info")};
    }

    public double getVerticalSpeedForWill(double rawWill) {
        return 1.2 + rawWill / 200;
    }

    public double getHorizontalSpeedForWill(double rawWill) {
        return 3 + rawWill / 40;
    }
}
