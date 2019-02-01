package WayofTime.bloodmagic.ritual.types;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.core.data.SoulNetwork;
import WayofTime.bloodmagic.demonAura.WorldDemonWillHandler;
import WayofTime.bloodmagic.entity.projectile.EntityMeteor;
import WayofTime.bloodmagic.meteor.Meteor;
import WayofTime.bloodmagic.meteor.MeteorRegistry;
import WayofTime.bloodmagic.ritual.*;
import WayofTime.bloodmagic.soul.EnumDemonWillType;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.Consumer;

@RitualRegister("meteor")
public class RitualMeteor extends Ritual {
    public static final String ITEM_RANGE = "itemRange";
    public static final double destructiveWillDrain = 50;

    public RitualMeteor() {
        super("ritualMeteor", 2, 0, "ritual." + BloodMagic.MODID + ".meteorRitual");

        addBlockRange(ITEM_RANGE, new AreaDescriptor.Rectangle(new BlockPos(0, 1, 0), 1));
        setMaximumVolumeAndDistanceOfRange(ITEM_RANGE, 0, 10, 10);
    }

    @Override
    public void performRitual(IMasterRitualStone masterRitualStone) {
        World world = masterRitualStone.getWorldObj();
        int currentEssence = masterRitualStone.getOwnerNetwork().getCurrentEssence();

        BlockPos pos = masterRitualStone.getBlockPos();

        List<EnumDemonWillType> willConfig = masterRitualStone.getActiveWillConfig();

        double corrosiveWill = this.getWillRespectingConfig(world, pos, EnumDemonWillType.CORROSIVE, willConfig);
        double destructiveWill = this.getWillRespectingConfig(world, pos, EnumDemonWillType.DESTRUCTIVE, willConfig);
        double rawWill = this.getWillRespectingConfig(world, pos, EnumDemonWillType.DEFAULT, willConfig);
        double steadfastWill = this.getWillRespectingConfig(world, pos, EnumDemonWillType.STEADFAST, willConfig);
        double vengefulWill = this.getWillRespectingConfig(world, pos, EnumDemonWillType.VENGEFUL, willConfig);

        AreaDescriptor itemDetectionRange = masterRitualStone.getBlockRange(ITEM_RANGE);
        List<EntityItem> itemList = world.getEntitiesWithinAABB(EntityItem.class, itemDetectionRange.getAABB(pos));

        double radiusModifier = getRadiusModifier(rawWill);
        double explosionModifier = getExplosionModifier(steadfastWill);
        double fillerChance = getFillerChance(corrosiveWill);

        boolean successful = false;

        for (EntityItem entityItem : itemList) {
            ItemStack stack = entityItem.getItem();
            Meteor meteor = MeteorRegistry.getMeteorForItem(stack);

            if (meteor != null) {
                SoulNetwork network = masterRitualStone.getOwnerNetwork();
                int cost = meteor.getCost();
                if (currentEssence < cost) {
                    network.causeNausea();
                    break;
                } else {
                    network.syphon(masterRitualStone.ticket(cost));
                    EntityMeteor entityMeteor = new EntityMeteor(world, pos.getX(), 260, pos.getZ(), 0, -0.1, 0, radiusModifier, explosionModifier, fillerChance);
                    entityMeteor.setMeteorStack(stack.copy());
                    world.spawnEntity(entityMeteor);

                    entityItem.setDead();

                }

                if (destructiveWill >= destructiveWillDrain && currentEssence >= 1000000000) {
                    network.syphon(masterRitualStone.ticket(cost / 10));
                } else {
                    masterRitualStone.setActive(false);
                }
                successful = true;
                break;
            }
        }

        if (successful) {
            if (rawWill > 0) {
                WorldDemonWillHandler.drainWill(world, pos, EnumDemonWillType.DEFAULT, rawWill, true);
            }

            if (corrosiveWill > 0) {
                WorldDemonWillHandler.drainWill(world, pos, EnumDemonWillType.CORROSIVE, corrosiveWill, true);
            }

            if (steadfastWill > 0) {
                WorldDemonWillHandler.drainWill(world, pos, EnumDemonWillType.STEADFAST, steadfastWill, true);
            }
        }
    }

    @Override
    public int getRefreshTime() {
        return 1;
    }

    @Override
    public int getRefreshCost() {
        return 0;
    }

    @Override
    public void gatherComponents(Consumer<RitualComponent> components) {
        addParallelRunes(components, 2, 0, EnumRuneType.FIRE);
        addOffsetRunes(components, 3, 1, 0, EnumRuneType.AIR);
        addOffsetRunes(components, 4, 2, 0, EnumRuneType.AIR);
        addOffsetRunes(components, 5, 3, 0, EnumRuneType.DUSK);
        addCornerRunes(components, 4, 0, EnumRuneType.DUSK);

        for (int i = 4; i <= 6; i++) {
            addParallelRunes(components, 4, 0, EnumRuneType.EARTH);
        }

        addParallelRunes(components, 8, 0, EnumRuneType.EARTH);
        addParallelRunes(components, 8, 1, EnumRuneType.EARTH);
        addParallelRunes(components, 7, 1, EnumRuneType.EARTH);
        addParallelRunes(components, 7, 2, EnumRuneType.EARTH);
        addParallelRunes(components, 6, 2, EnumRuneType.FIRE);
        addParallelRunes(components, 6, 3, EnumRuneType.WATER);
        addParallelRunes(components, 5, 3, EnumRuneType.WATER);
        addParallelRunes(components, 5, 4, EnumRuneType.AIR);

        addOffsetRunes(components, 1, 4, 4, EnumRuneType.AIR);
        addParallelRunes(components, 4, 4, EnumRuneType.AIR);

        addOffsetRunes(components, 2, 4, 4, EnumRuneType.WATER);
        addOffsetRunes(components, 2, 3, 4, EnumRuneType.FIRE);
        addCornerRunes(components, 3, 4, EnumRuneType.FIRE);
    }

    @Override
    public Ritual getNewCopy() {
        return new RitualMeteor();
    }

    public double getRadiusModifier(double rawWill) {
        return Math.pow(1 + rawWill / 100, 1 / 3);
    }

    public double getFillerChance(double corrosiveWill) {
        return corrosiveWill / 200;
    }

    public double getExplosionModifier(double steadfastWill) {
        return Math.max(Math.pow(0.4, steadfastWill / 100), 1);
    }
}
