package WayofTime.bloodmagic.ritual.types;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.impl.BloodMagicAPI;
import WayofTime.bloodmagic.ritual.*;
import WayofTime.bloodmagic.tile.TileDemonCrystal;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@RitualRegister("forsaken_soul")
public class RitualForsakenSoul extends Ritual {
    public static final String CRYSTAL_RANGE = "crystal";
    public static final String DAMAGE_RANGE = "damage";
    public static final int MAX_UNIQUENESS = 10;

    public static final int HEALTH_THRESHOLD = 20;

    public double willBuffer = 0;
    public double crystalBuffer = 0;

    public List<Integer> keyList = new ArrayList<>();

    public RitualForsakenSoul() {
        super("ritualForsakenSoul", 0, 40000, "ritual." + BloodMagic.MODID + ".forsakenSoulRitual");
        addBlockRange(CRYSTAL_RANGE, new AreaDescriptor.Rectangle(new BlockPos(-3, 2, -3), 7, 5, 7));
        addBlockRange(DAMAGE_RANGE, new AreaDescriptor.Rectangle(new BlockPos(-10, -10, -10), 21));

        setMaximumVolumeAndDistanceOfRange(CRYSTAL_RANGE, 250, 5, 7);
        setMaximumVolumeAndDistanceOfRange(DAMAGE_RANGE, 0, 10, 10);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        willBuffer = tag.getDouble("willBuffer");
        crystalBuffer = tag.getDouble("crystalBuffer");

        keyList.clear();
        for (int i = 0; i < MAX_UNIQUENESS; i++) {
            String key = "uniq" + i;
            if (tag.hasKey(key)) {
                keyList.add(tag.getInteger(key));
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);

        tag.setDouble("willBuffer", willBuffer);
        tag.setDouble("crystalBuffer", crystalBuffer);

        for (int i = 0; i < Math.min(MAX_UNIQUENESS, keyList.size()); i++) {
            String key = "uniq" + i;
            tag.setInteger(key, keyList.get(i));
        }
    }

    @Override
    public void performRitual(IMasterRitualStone masterRitualStone) {
        World world = masterRitualStone.getWorldObj();
        int currentEssence = masterRitualStone.getOwnerNetwork().getCurrentEssence();
        BlockPos pos = masterRitualStone.getBlockPos();

        if (currentEssence < getRefreshCost()) {
            masterRitualStone.getOwnerNetwork().causeNausea();
            return;
        }

        int maxEffects = 100;
        int totalEffects = 0;

        List<TileDemonCrystal> crystalList = new ArrayList<>();

        AreaDescriptor crystalRange = masterRitualStone.getBlockRange(CRYSTAL_RANGE);

        crystalRange.resetIterator();
        while (crystalRange.hasNext()) {
            BlockPos nextPos = crystalRange.next().add(pos);
            TileEntity tile = world.getTileEntity(nextPos);
            if (tile instanceof TileDemonCrystal) {
                crystalList.add((TileDemonCrystal) tile);
            }
        }

        AreaDescriptor damageRange = masterRitualStone.getBlockRange(DAMAGE_RANGE);
        AxisAlignedBB range = damageRange.getAABB(pos);

        List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, range);

        for (EntityLivingBase entity : entities) {
            EntityEntry entityEntry = EntityRegistry.getEntry(entity.getClass());

            if (entityEntry == null || BloodMagicAPI.INSTANCE.getBlacklist().getSacrifice().contains(entityEntry.getRegistryName()))
                continue;

            if (entity.isEntityAlive() && !(entity instanceof EntityPlayer)) {
                if (entity.attackEntityFrom(RitualManager.RITUAL_DAMAGE, 1)) {
                    if (!entity.isEntityAlive()) {
                        int uniqueness = calculateUniqueness(entity);
                        double modifier = 1;
                        if (entity instanceof EntityAnimal && !((EntityAnimal) entity).collided) {
                            modifier = 4;
                        }

                        willBuffer += modifier * getWillForUniqueness(uniqueness) / HEALTH_THRESHOLD * entity.getMaxHealth();
                        crystalBuffer += modifier * entity.getMaxHealth() / HEALTH_THRESHOLD;

                        totalEffects++;
                        if (totalEffects >= maxEffects) {
                            break;
                        }
                    }
                }
            }
        }

        if (crystalList.size() > 0 && crystalBuffer > 0) {
            double growth = Math.min(crystalBuffer, 1);
            double willSyphonAmount = growth * willBuffer / crystalBuffer;
            TileDemonCrystal chosenCrystal = crystalList.get(world.rand.nextInt(crystalList.size()));
            double percentageGrowth = chosenCrystal.growCrystalWithWillAmount(growth * willBuffer / crystalBuffer, growth);
            if (percentageGrowth > 0) {
                crystalBuffer -= percentageGrowth;
                willBuffer -= percentageGrowth * willSyphonAmount;
            }
        }

        masterRitualStone.getOwnerNetwork().syphon(masterRitualStone.ticket(getRefreshCost() * totalEffects));
    }

    /**
     * @param mob
     * @return The amount of uniqueness to the last 10 mobs killed
     */
    public int calculateUniqueness(EntityLivingBase mob) {
        int key = mob.getClass().hashCode();
        keyList.add(key);
        if (keyList.size() > MAX_UNIQUENESS) {
            keyList.remove(0);
        }

        List<Integer> uniquenessList = new ArrayList<>();
        for (int value : keyList) {
            if (!uniquenessList.contains(value)) {
                uniquenessList.add(value);
            }
        }

        return Math.min(uniquenessList.size(), MAX_UNIQUENESS);
    }

    public double getWillForUniqueness(int uniqueness) {
        return Math.max(50 - 15 * Math.sqrt(uniqueness), 0);
    }

    @Override
    public int getRefreshTime() {
        return 25;
    }

    @Override
    public int getRefreshCost() {
        return 2;
    }

    @Override
    public void gatherComponents(Consumer<RitualComponent> components) {
        addCornerRunes(components, 1, 0, EnumRuneType.AIR);
        addParallelRunes(components, 1, -1, EnumRuneType.DUSK);
        addParallelRunes(components, 1, 1, EnumRuneType.FIRE);
        addParallelRunes(components, 2, 1, EnumRuneType.FIRE);
        addParallelRunes(components, 3, 1, EnumRuneType.FIRE);
        addOffsetRunes(components, 3, 1, 1, EnumRuneType.FIRE);
        addCornerRunes(components, 3, 1, EnumRuneType.EARTH);
        addCornerRunes(components, 3, 0, EnumRuneType.EARTH);
        addOffsetRunes(components, 3, 2, 0, EnumRuneType.EARTH);
    }

    @Override
    public Ritual getNewCopy() {
        return new RitualForsakenSoul();
    }
}
