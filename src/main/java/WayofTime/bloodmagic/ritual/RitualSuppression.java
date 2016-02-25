package WayofTime.bloodmagic.ritual;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
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
import WayofTime.bloodmagic.tile.TileSpectralBlock;
import WayofTime.bloodmagic.util.Utils;

public class RitualSuppression extends Ritual
{
    public static final String SUPPRESSION_RANGE = "suppressionRange";

    public RitualSuppression()
    {
        super("ritualSuppression", 0, 10000, "ritual." + Constants.Mod.MODID + ".suppressionRitual");
        addBlockRange(SUPPRESSION_RANGE, new AreaDescriptor.HemiSphere(new BlockPos(0, 0, 0), 10));
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

        final int refresh = 100;
        AreaDescriptor suppressionRange = getBlockRange(SUPPRESSION_RANGE);

        for (BlockPos blockPos : suppressionRange.getContainedPositions(masterRitualStone.getBlockPos()))
        {
            Block block = world.getBlockState(blockPos).getBlock();

            if (Utils.isBlockLiquid(block) && world.getTileEntity(blockPos) == null)
                TileSpectralBlock.createSpectralBlock(world, blockPos, refresh);
            else
            {
                TileEntity tile = world.getTileEntity(blockPos);
                if (tile instanceof TileSpectralBlock)
                    ((TileSpectralBlock) tile).resetDuration(refresh);
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
        return 2;
    }

    @Override
    public ArrayList<RitualComponent> getComponents()
    {
        ArrayList<RitualComponent> components = new ArrayList<RitualComponent>();

        this.addCornerRunes(components, 2, 0, EnumRuneType.WATER);
        this.addRune(components, -2, 0, -1, EnumRuneType.AIR);
        this.addRune(components, -1, 0, -2, EnumRuneType.AIR);
        this.addRune(components, -2, 0, 1, EnumRuneType.AIR);
        this.addRune(components, 1, 0, -2, EnumRuneType.AIR);
        this.addRune(components, 2, 0, 1, EnumRuneType.AIR);
        this.addRune(components, 1, 0, 2, EnumRuneType.AIR);
        this.addRune(components, 2, 0, -1, EnumRuneType.AIR);
        this.addRune(components, -1, 0, 2, EnumRuneType.AIR);

        return components;
    }

    @Override
    public Ritual getNewCopy()
    {
        return new RitualSuppression();
    }
}
