package WayofTime.bloodmagic.ritual;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.network.SoulNetwork;
import WayofTime.bloodmagic.api.ritual.AreaDescriptor;
import WayofTime.bloodmagic.api.ritual.EnumRuneType;
import WayofTime.bloodmagic.api.ritual.IMasterRitualStone;
import WayofTime.bloodmagic.api.ritual.Ritual;
import WayofTime.bloodmagic.api.ritual.RitualComponent;
import WayofTime.bloodmagic.api.util.helper.NetworkHelper;
import WayofTime.bloodmagic.tile.TileAltar;

public class RitualFeatheredKnife extends Ritual
{
    public static final String ALTAR_RANGE = "altar";
    public static final String DAMAGE_RANGE = "damage";

    public static final int SACRIFICE_AMOUNT = 100;

    public BlockPos altarOffsetPos = new BlockPos(0, 0, 0); //TODO: Save!

    public RitualFeatheredKnife()
    {
        super("ritualFeatheredKnife", 0, 25000, "ritual." + Constants.Mod.MODID + ".featheredKnifeRitual");
        addBlockRange(ALTAR_RANGE, new AreaDescriptor.Rectangle(new BlockPos(-5, -10, -5), 11, 21, 11));
        addBlockRange(DAMAGE_RANGE, new AreaDescriptor.Rectangle(new BlockPos(-15, -20, -15), 31, 41, 31));
    }

    @Override
    public void performRitual(IMasterRitualStone masterRitualStone)
    {
        World world = masterRitualStone.getWorldObj();
        SoulNetwork network = NetworkHelper.getSoulNetwork(masterRitualStone.getOwner());
        int currentEssence = network.getCurrentEssence();

        if (currentEssence < getRefreshCost())
        {
            network.causeNauseaToPlayer();
            return;
        }

        BlockPos pos = masterRitualStone.getBlockPos();

        int maxEffects = currentEssence / getRefreshCost();
        int totalEffects = 0;

        BlockPos altarPos = pos.add(altarOffsetPos);

        TileEntity tile = world.getTileEntity(altarPos);

        AreaDescriptor altarRange = getBlockRange(ALTAR_RANGE);

        if (!altarRange.isWithinArea(altarOffsetPos) || !(tile instanceof TileAltar))
        {
            for (BlockPos newPos : altarRange.getContainedPositions(pos))
            {
                TileEntity nextTile = world.getTileEntity(newPos);
                if (nextTile instanceof TileAltar)
                {
                    tile = nextTile;
                    altarOffsetPos = newPos.subtract(pos);

                    altarRange.resetCache();
                    break;
                }
            }
        }

        if (tile instanceof TileAltar)
        {
            TileAltar tileAltar = (TileAltar) tile;

            AreaDescriptor damageRange = getBlockRange(DAMAGE_RANGE);
            AxisAlignedBB range = damageRange.getAABB(pos);

            List<EntityPlayer> entities = world.getEntitiesWithinAABB(EntityPlayer.class, range);

            for (EntityLivingBase player : entities)
            {
                float health = player.getHealth();
                if (health > 6)
                {
                    player.setHealth(health - 1);

                    tileAltar.sacrificialDaggerCall(SACRIFICE_AMOUNT, false);

                    totalEffects++;

                    if (totalEffects >= maxEffects)
                    {
                        break;
                    }
                }
            }
        }

        network.syphon(getRefreshCost() * totalEffects);
    }

    @Override
    public int getRefreshTime()
    {
        return 20;
    }

    @Override
    public int getRefreshCost()
    {
        return 20;
    }

    @Override
    public ArrayList<RitualComponent> getComponents()
    {
        ArrayList<RitualComponent> components = new ArrayList<RitualComponent>();

        this.addParallelRunes(components, 1, 0, EnumRuneType.DUSK);
        this.addParallelRunes(components, 2, -1, EnumRuneType.WATER);
        this.addCornerRunes(components, 1, -1, EnumRuneType.AIR);
        this.addOffsetRunes(components, 2, 4, -1, EnumRuneType.FIRE);
        this.addOffsetRunes(components, 2, 4, 0, EnumRuneType.EARTH);
        this.addOffsetRunes(components, 4, 3, 0, EnumRuneType.EARTH);
        this.addCornerRunes(components, 3, 0, EnumRuneType.AIR);

        return components;
    }

    @Override
    public Ritual getNewCopy()
    {
        return new RitualFeatheredKnife();
    }
}
