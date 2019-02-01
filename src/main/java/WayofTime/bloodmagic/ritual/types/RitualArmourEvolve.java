package WayofTime.bloodmagic.ritual.types;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.item.armour.ItemLivingArmour;
import WayofTime.bloodmagic.livingArmour.LivingArmour;
import WayofTime.bloodmagic.ritual.*;
import com.google.common.collect.Iterables;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.Consumer;

@RitualRegister("armour_evolve")
public class RitualArmourEvolve extends Ritual {
    public static final String CHECK_RANGE = "fillRange";

    public RitualArmourEvolve() {
        super("ritualArmourEvolve", 2, 50000, "ritual." + BloodMagic.MODID + ".armourEvolveRitual");
        addBlockRange(CHECK_RANGE, new AreaDescriptor.Rectangle(new BlockPos(0, 1, 0), 1, 2, 1));
    }

    @Override
    public void performRitual(IMasterRitualStone masterRitualStone) {
        World world = masterRitualStone.getWorldObj();

        if (world.isRemote) {
            return;
        }

        BlockPos pos = masterRitualStone.getBlockPos();

        AreaDescriptor checkRange = masterRitualStone.getBlockRange(CHECK_RANGE);

        List<EntityPlayer> playerList = world.getEntitiesWithinAABB(EntityPlayer.class, checkRange.getAABB(pos));

        for (EntityPlayer player : playerList) {
            if (LivingArmour.hasFullSet(player)) {
                ItemStack chestStack = Iterables.toArray(player.getArmorInventoryList(), ItemStack.class)[2];
                LivingArmour armour = ItemLivingArmour.getLivingArmour(chestStack);
                if (armour != null) {
                    if (armour.maxUpgradePoints < 300) {
                        armour.maxUpgradePoints = 300;
                        ((ItemLivingArmour) chestStack.getItem()).setLivingArmour(chestStack, armour, true);

                        masterRitualStone.setActive(false);

                        world.spawnEntity(new EntityLightningBolt(world, pos.getX(), pos.getY() - 1, pos.getZ(), true));
                    }
                }
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
        addCornerRunes(components, 1, 0, EnumRuneType.DUSK);
        addCornerRunes(components, 2, 0, EnumRuneType.FIRE);
        addOffsetRunes(components, 1, 2, 0, EnumRuneType.FIRE);
        addCornerRunes(components, 1, 1, EnumRuneType.DUSK);
        addParallelRunes(components, 4, 0, EnumRuneType.EARTH);
        addCornerRunes(components, 1, 3, EnumRuneType.DUSK);
        addParallelRunes(components, 1, 4, EnumRuneType.EARTH);

        for (int i = 0; i < 4; i++) {
            addCornerRunes(components, 3, i, EnumRuneType.EARTH);
        }
    }

    @Override
    public Ritual getNewCopy() {
        return new RitualArmourEvolve();
    }
}
