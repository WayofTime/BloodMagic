package WayofTime.bloodmagic.ritual.types;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.core.RegistrarBloodMagicItems;
import WayofTime.bloodmagic.ritual.*;
import WayofTime.bloodmagic.tile.TileAlchemyArray;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.function.Consumer;

@RitualRegister("cobblestone")
public class RitualCobblestone extends Ritual {

    public static final String COBBLESTONE_RANGE = "cobblestoneRange";

    public RitualCobblestone() {
        super("ritualCobblestone", 0, 500, "ritual." + BloodMagic.MODID + ".cobblestoneRitual");
        addBlockRange(COBBLESTONE_RANGE, new AreaDescriptor.Cross(new BlockPos(0, 1, 0), 1));
    }

    @Override
    public void performRitual(IMasterRitualStone masterRitualStone) {
        World world = masterRitualStone.getWorldObj();
        int currentEssence = masterRitualStone.getOwnerNetwork().getCurrentEssence();
        TileEntity tileEntity = world.getTileEntity(masterRitualStone.getBlockPos().up());
        Block block = Blocks.COBBLESTONE;

        if (currentEssence < getRefreshCost()) {
            masterRitualStone.getOwnerNetwork().causeNausea();
            return;
        }

        int maxEffects = currentEssence / getRefreshCost();
        int totalEffects = 0;

        AreaDescriptor cobblestoneRange = masterRitualStone.getBlockRange(COBBLESTONE_RANGE);

        if (tileEntity != null && tileEntity instanceof TileAlchemyArray) {
            TileAlchemyArray alchemyArray = (TileAlchemyArray) tileEntity;
            if (!alchemyArray.getStackInSlot(0).isEmpty() && alchemyArray.getStackInSlot(0).getItem() == RegistrarBloodMagicItems.COMPONENT) {
                switch (alchemyArray.getStackInSlot(0).getItemDamage()) {
                    case 0:
                        block = Blocks.OBSIDIAN;
                        alchemyArray.decrStackSize(0, 1);
                        world.setBlockToAir(alchemyArray.getPos());
                        break;
                    case 1:
                        block = Blocks.NETHERRACK;
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

        for (BlockPos blockPos : cobblestoneRange.getContainedPositions(masterRitualStone.getBlockPos())) {
            if (world.isAirBlock(blockPos)) {
                world.setBlockState(blockPos, block.getDefaultState());
                totalEffects++;
            }

            if (totalEffects >= maxEffects) {
                break;
            }
        }

        masterRitualStone.getOwnerNetwork().syphon(masterRitualStone.ticket(getRefreshCost() * totalEffects));
    }

    @Override
    public int getRefreshCost() {
        return 25;
    }

    @Override
    public void gatherComponents(Consumer<RitualComponent> components) {
        addCornerRunes(components, 1, 1, EnumRuneType.FIRE);
        addParallelRunes(components, 1, 0, EnumRuneType.WATER);
    }

    @Override
    public Ritual getNewCopy() {
        return new RitualCobblestone();
    }
}
