package WayofTime.bloodmagic.ritual;

import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.ritual.*;
import WayofTime.bloodmagic.item.armour.ItemLivingArmour;
import WayofTime.bloodmagic.livingArmour.LivingArmour;
import com.google.common.collect.Iterables;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class RitualArmourEvolve extends Ritual
{
    public static final String CHECK_RANGE = "fillRange";

    public RitualArmourEvolve()
    {
        super("ritualArmourEvolve", 0, 50000, "ritual." + Constants.Mod.MODID + ".armourEvolveRitual");
        addBlockRange(CHECK_RANGE, new AreaDescriptor.Rectangle(new BlockPos(0, 1, 0), 1, 2, 1));
    }

    @Override
    public void performRitual(IMasterRitualStone masterRitualStone)
    {
        World world = masterRitualStone.getWorldObj();

        if (world.isRemote)
        {
            return;
        }

        BlockPos pos = masterRitualStone.getBlockPos();

        AreaDescriptor checkRange = getBlockRange(CHECK_RANGE);

        List<EntityPlayer> playerList = world.getEntitiesWithinAABB(EntityPlayer.class, checkRange.getAABB(pos));

        for (EntityPlayer player : playerList)
        {
            if (LivingArmour.hasFullSet(player))
            {
                ItemStack chestStack = Iterables.toArray(player.getArmorInventoryList(), ItemStack.class)[2];
                LivingArmour armour = ItemLivingArmour.getLivingArmour(chestStack);
                if (armour != null)
                {
                    if (armour.maxUpgradePoints < 300)
                    {
                        armour.maxUpgradePoints = 300;
                        ((ItemLivingArmour) chestStack.getItem()).setLivingArmour(chestStack, armour, true);

                        masterRitualStone.setActive(false);

                        world.spawnEntityInWorld(new EntityLightningBolt(world, pos.getX(), pos.getY() - 1, pos.getZ(), true));
                    }
                }
            }
        }
    }

    @Override
    public int getRefreshTime()
    {
        return 1;
    }

    @Override
    public int getRefreshCost()
    {
        return 0;
    }

    @Override
    public ArrayList<RitualComponent> getComponents()
    {
        ArrayList<RitualComponent> components = new ArrayList<RitualComponent>();

        this.addCornerRunes(components, 1, 0, EnumRuneType.DUSK);
        this.addCornerRunes(components, 2, 0, EnumRuneType.FIRE);
        this.addOffsetRunes(components, 1, 2, 0, EnumRuneType.FIRE);
        this.addCornerRunes(components, 1, 1, EnumRuneType.DUSK);
        this.addParallelRunes(components, 4, 0, EnumRuneType.EARTH);
        this.addCornerRunes(components, 1, 3, EnumRuneType.DUSK);
        this.addParallelRunes(components, 1, 4, EnumRuneType.EARTH);

        for (int i = 0; i < 4; i++)
        {
            this.addCornerRunes(components, 3, i, EnumRuneType.EARTH);
        }

        return components;
    }

    @Override
    public Ritual getNewCopy()
    {
        return new RitualArmourEvolve();
    }
}
