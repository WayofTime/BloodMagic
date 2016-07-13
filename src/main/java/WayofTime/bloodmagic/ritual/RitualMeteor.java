package WayofTime.bloodmagic.ritual;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.ritual.AreaDescriptor;
import WayofTime.bloodmagic.api.ritual.EnumRuneType;
import WayofTime.bloodmagic.api.ritual.IMasterRitualStone;
import WayofTime.bloodmagic.api.ritual.Ritual;
import WayofTime.bloodmagic.api.ritual.RitualComponent;
import WayofTime.bloodmagic.entity.projectile.EntityMeteor;
import WayofTime.bloodmagic.meteor.MeteorRegistry;

public class RitualMeteor extends Ritual
{
    public static final String ITEM_RANGE = "itemRange";

    public RitualMeteor()
    {
        super("ritualMeteor", 0, 1000000, "ritual." + Constants.Mod.MODID + ".meteorRitual");
        addBlockRange(ITEM_RANGE, new AreaDescriptor.Rectangle(new BlockPos(0, 1, 0), 1));
        setMaximumVolumeAndDistanceOfRange(ITEM_RANGE, 0, 10, 10);
    }

    @Override
    public void performRitual(IMasterRitualStone masterRitualStone)
    {
        World world = masterRitualStone.getWorldObj();

        BlockPos pos = masterRitualStone.getBlockPos();

        AreaDescriptor itemDetectionRange = getBlockRange(ITEM_RANGE);
        List<EntityItem> itemList = world.getEntitiesWithinAABB(EntityItem.class, itemDetectionRange.getAABB(pos));

        for (EntityItem entityItem : itemList)
        {
            ItemStack stack = entityItem.getEntityItem();
            if (MeteorRegistry.hasMeteorForItem(stack))
            {
                EntityMeteor meteor = new EntityMeteor(world, pos.getX(), 260, pos.getZ(), 0, -0.1, 0);
                meteor.setMeteorStack(stack.copy());
                world.spawnEntityInWorld(meteor);

                entityItem.setDead();

                masterRitualStone.setActive(false);
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

        this.addParallelRunes(components, 2, 0, EnumRuneType.FIRE);
        this.addOffsetRunes(components, 3, 1, 0, EnumRuneType.AIR);
        this.addOffsetRunes(components, 4, 2, 0, EnumRuneType.AIR);
        this.addOffsetRunes(components, 5, 3, 0, EnumRuneType.DUSK);
        this.addCornerRunes(components, 4, 0, EnumRuneType.DUSK);

        for (int i = 4; i <= 6; i++)
        {
            this.addParallelRunes(components, 4, 0, EnumRuneType.EARTH);
        }

        this.addParallelRunes(components, 8, 0, EnumRuneType.EARTH);
        this.addParallelRunes(components, 8, 1, EnumRuneType.EARTH);
        this.addParallelRunes(components, 7, 1, EnumRuneType.EARTH);
        this.addParallelRunes(components, 7, 2, EnumRuneType.EARTH);
        this.addParallelRunes(components, 6, 2, EnumRuneType.FIRE);
        this.addParallelRunes(components, 6, 3, EnumRuneType.WATER);
        this.addParallelRunes(components, 5, 3, EnumRuneType.WATER);
        this.addParallelRunes(components, 5, 4, EnumRuneType.AIR);

        this.addOffsetRunes(components, 1, 4, 4, EnumRuneType.AIR);
        this.addParallelRunes(components, 4, 4, EnumRuneType.AIR);

        this.addOffsetRunes(components, 2, 4, 4, EnumRuneType.WATER);
        this.addOffsetRunes(components, 2, 3, 4, EnumRuneType.FIRE);
        this.addCornerRunes(components, 3, 4, EnumRuneType.FIRE);

        return components;
    }

    @Override
    public Ritual getNewCopy()
    {
        return new RitualMeteor();
    }
}
