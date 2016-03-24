package WayofTime.bloodmagic.ritual;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.network.SoulNetwork;
import WayofTime.bloodmagic.api.ritual.AreaDescriptor;
import WayofTime.bloodmagic.api.ritual.EnumRuneType;
import WayofTime.bloodmagic.api.ritual.IMasterRitualStone;
import WayofTime.bloodmagic.api.ritual.Ritual;
import WayofTime.bloodmagic.api.ritual.RitualComponent;
import WayofTime.bloodmagic.api.util.helper.NetworkHelper;
import WayofTime.bloodmagic.tile.TileDemonCrystal;

public class RitualForsakenSoul extends Ritual
{
    public static final String CRYSTAL_RANGE = "altar";
    public static final String DAMAGE_RANGE = "damage";

    public RitualForsakenSoul()
    {
        super("ritualForsakenSoul", 0, 40000, "ritual." + Constants.Mod.MODID + ".forsakenSoulRitual");
        addBlockRange(CRYSTAL_RANGE, new AreaDescriptor.Rectangle(new BlockPos(-1, -1, -1), 3));
        addBlockRange(DAMAGE_RANGE, new AreaDescriptor.Rectangle(new BlockPos(-10, -10, -10), 21));
    }

    @Override
    public void performRitual(IMasterRitualStone masterRitualStone)
    {
        World world = masterRitualStone.getWorldObj();
        SoulNetwork network = NetworkHelper.getSoulNetwork(masterRitualStone.getOwner());
        int currentEssence = network.getCurrentEssence();
        BlockPos pos = masterRitualStone.getBlockPos();

        if (currentEssence < getRefreshCost())
        {
            network.causeNauseaToPlayer();
            return;
        }

        int maxEffects = 100;
        int totalEffects = 0;

        List<TileDemonCrystal> crystalList = new ArrayList<TileDemonCrystal>();

        AreaDescriptor crystalRange = getBlockRange(CRYSTAL_RANGE);

        crystalRange.resetIterator();
        while (crystalRange.hasNext())
        {
            BlockPos nextPos = crystalRange.next().add(pos);
            TileEntity tile = world.getTileEntity(nextPos);
            if (tile instanceof TileDemonCrystal)
            {
                crystalList.add((TileDemonCrystal) tile);
            }
        }

        if (crystalList.size() > 0)
        {
            TileDemonCrystal chosenCrystal = crystalList.get(world.rand.nextInt(crystalList.size()));
            chosenCrystal.growCrystalWithWillAmount(40, 1);
        }
//        if (tile instanceof TileAltar)
//        {
//            TileAltar tileAltar = (TileAltar) tile;
//
//            AreaDescriptor damageRange = getBlockRange(DAMAGE_RANGE);
//            AxisAlignedBB range = damageRange.getAABB(pos);
//
//            List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, range);
//
//            for (EntityLivingBase entity : entities)
//            {
//                if (!ConfigHandler.wellOfSufferingBlacklist.contains(entity.getClass().getSimpleName()))
//                {
//                    if (entity.isEntityAlive() && !(entity instanceof EntityPlayer))
//                    {
//                        if (entity.attackEntityFrom(DamageSource.outOfWorld, 1))
//                        {
//                            tileAltar.sacrificialDaggerCall(SACRIFICE_AMOUNT, true);
//
//                            totalEffects++;
//
//                            if (totalEffects >= maxEffects)
//                            {
//                                break;
//                            }
//                        }
//                    }
//                }
//            }
//        }

        network.syphon(getRefreshCost() * totalEffects);
    }

    @Override
    public int getRefreshTime()
    {
        return 25;
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
        this.addCornerRunes(components, 2, -1, EnumRuneType.DUSK);
        this.addParallelRunes(components, 2, -1, EnumRuneType.EARTH);
        this.addCornerRunes(components, -3, -1, EnumRuneType.DUSK);
        this.addOffsetRunes(components, 2, 4, -1, EnumRuneType.WATER);
        this.addOffsetRunes(components, 1, 4, 0, EnumRuneType.WATER);
        this.addParallelRunes(components, 4, 1, EnumRuneType.AIR);

        return components;
    }

    @Override
    public Ritual getNewCopy()
    {
        return new RitualForsakenSoul();
    }
}
