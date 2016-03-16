package WayofTime.bloodmagic.ritual;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
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
import WayofTime.bloodmagic.item.ItemComponent;
import WayofTime.bloodmagic.tile.TileAlchemyArray;

public class RitualCobblestone extends Ritual
{

    public static final String COBBLESTONE_RANGE = "cobblestoneRange";

    public RitualCobblestone()
    {
        super("ritualCobblestone", 0, 500, "ritual." + Constants.Mod.MODID + ".cobblestoneRitual");
        addBlockRange(COBBLESTONE_RANGE, new AreaDescriptor.Cross(new BlockPos(0, 1, 0), 1));
    }

    @Override
    public void performRitual(IMasterRitualStone masterRitualStone)
    {
        World world = masterRitualStone.getWorldObj();
        SoulNetwork network = NetworkHelper.getSoulNetwork(masterRitualStone.getOwner());
        int currentEssence = network.getCurrentEssence();
        TileEntity tileEntity = world.getTileEntity(masterRitualStone.getBlockPos().up());
        Block block = Blocks.cobblestone;

        if (currentEssence < getRefreshCost())
        {
            network.causeNauseaToPlayer();
            return;
        }

        int maxEffects = currentEssence / getRefreshCost();
        int totalEffects = 0;

        AreaDescriptor cobblestoneRange = getBlockRange(COBBLESTONE_RANGE);

        if (tileEntity != null && tileEntity instanceof TileAlchemyArray)
        {
            TileAlchemyArray alchemyArray = (TileAlchemyArray) tileEntity;
            if (alchemyArray.getStackInSlot(0) != null && alchemyArray.getStackInSlot(0).getItem() instanceof ItemComponent)
            {
                switch (alchemyArray.getStackInSlot(0).getItemDamage())
                {
                case 0:
                    block = Blocks.obsidian;
                    alchemyArray.decrStackSize(0, 1);
                    world.setBlockToAir(alchemyArray.getPos());
                    break;
                case 1:
                    block = Blocks.netherrack;
                    alchemyArray.decrStackSize(0, 1);
                    world.setBlockToAir(alchemyArray.getPos());
                    break;
                /*
                 * case 4: block = Blocks.end_stone;
                 * alchemyArray.decrStackSize(0, 1);
                 * world.setBlockToAir(alchemyArray.getPos()); break;
                 */
                default:
                    break;
                }
            }
        }

        for (BlockPos blockPos : cobblestoneRange.getContainedPositions(masterRitualStone.getBlockPos()))
        {
            if (world.isAirBlock(blockPos))
            {
                world.setBlockState(blockPos, block.getDefaultState());
                totalEffects++;
            }

            if (totalEffects >= maxEffects)
            {
                break;
            }
        }

        network.syphon(getRefreshCost() * totalEffects);
    }

    @Override
    public int getRefreshCost()
    {
        return 25;
    }

    @Override
    public ArrayList<RitualComponent> getComponents()
    {
        ArrayList<RitualComponent> components = new ArrayList<RitualComponent>();

        this.addCornerRunes(components, 1, 1, EnumRuneType.FIRE);
        this.addParallelRunes(components, 1, 0, EnumRuneType.WATER);

        return components;
    }

    @Override
    public Ritual getNewCopy()
    {
        return new RitualCobblestone();
    }
}
