package WayofTime.bloodmagic.ritual.types;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.altar.BloodAltar;
import WayofTime.bloodmagic.block.BlockLifeEssence;
import WayofTime.bloodmagic.core.RegistrarBloodMagic;
import WayofTime.bloodmagic.ritual.*;
import WayofTime.bloodmagic.tile.TileAltar;
import WayofTime.bloodmagic.util.helper.NetworkHelper;
import WayofTime.bloodmagic.util.helper.PlayerHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

@RitualRegister("eternal_soul")
public class RitualEternalSoul extends Ritual {
    public static final String ALTAR_RANGE = "altar";

    private BlockPos altarOffsetPos = new BlockPos(0, 0, 0);

    public RitualEternalSoul() {
        super("ritualEternalSoul", 2, 2000000, "ritual." + BloodMagic.MODID + ".eternalSoulRitual");
        addBlockRange(ALTAR_RANGE, new AreaDescriptor.Rectangle(new BlockPos(-5, -10, -5), 11, 21, 11));

        setMaximumVolumeAndDistanceOfRange(ALTAR_RANGE, 0, 10, 15);
    }

    @Override
    public void performRitual(IMasterRitualStone masterRitualStone) {
        UUID owner = masterRitualStone.getOwner();
        int currentEssence = NetworkHelper.getSoulNetwork(owner).getCurrentEssence();
        World world = masterRitualStone.getWorldObj();
        BlockPos pos = masterRitualStone.getBlockPos();
        BlockPos altarPos = pos.add(altarOffsetPos);

        TileEntity tile = world.getTileEntity(altarPos);
        AreaDescriptor altarRange = masterRitualStone.getBlockRange(ALTAR_RANGE);

        if (!altarRange.isWithinArea(altarOffsetPos) || !(tile instanceof TileAltar)) {
            for (BlockPos newPos : altarRange.getContainedPositions(pos)) {
                TileEntity nextTile = world.getTileEntity(newPos);
                if (nextTile instanceof TileAltar) {
                    tile = nextTile;
                    altarOffsetPos = newPos.subtract(pos);

                    altarRange.resetCache();
                    break;
                }
            }
        }

        if (!(tile instanceof TileAltar)) {
            return;
        }

        BloodAltar altar = (BloodAltar) tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);

        int horizontalRange = 15;
        int verticalRange = 20;

        List<EntityPlayer> list = world.getEntitiesWithinAABB(EntityPlayer.class,
                new AxisAlignedBB(pos.getX() - 0.5f, pos.getY() - 0.5f, pos.getZ() - 0.5f,
                        pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f)
                        .expand(horizontalRange, verticalRange, horizontalRange).expand(0, -verticalRange, 0));

        EntityPlayer entityOwner = PlayerHelper.getPlayerFromUUID(owner);

        int fillAmount = Math.min(currentEssence / 2, altar.fill(new FluidStack(BlockLifeEssence.getLifeEssence(), 10000), false));

        altar.fill(new FluidStack(BlockLifeEssence.getLifeEssence(), fillAmount), true);

        if (entityOwner != null && list.contains(entityOwner) && entityOwner.getHealth() > 2.0f && fillAmount != 0)
            entityOwner.setHealth(2.0f);

        for (EntityPlayer player : list)
            player.addPotionEffect(new PotionEffect(RegistrarBloodMagic.SOUL_FRAY, 100));

        masterRitualStone.getOwnerNetwork().syphon(masterRitualStone.ticket(fillAmount * 2));

    }


    @Override
    public int getRefreshCost() {
        return 0;
    }

    @Override
    public int getRefreshTime() {
        return 1;
    }

    @Override
    public void gatherComponents(Consumer<RitualComponent> components) {
        addCornerRunes(components, 1, 0, EnumRuneType.FIRE);

        for (int i = 0; i < 4; i++) {
            addCornerRunes(components, 2, i, EnumRuneType.AIR);
        }

        addCornerRunes(components, 4, 1, EnumRuneType.EARTH);

        addOffsetRunes(components, 3, 4, 1, EnumRuneType.EARTH);


        for (int i = 0; i < 2; i++) {
            addCornerRunes(components, 4, i + 2, EnumRuneType.WATER);
        }

        addCornerRunes(components, 4, 4, EnumRuneType.DUSK);

        addOffsetRunes(components, 6, 5, 0, EnumRuneType.FIRE);


        for (int i = 0; i < 2; i++) {
            addCornerRunes(components, 6, i, EnumRuneType.FIRE);
        }

        for (int i = 0; i < 3; i++) {
            addCornerRunes(components, 6, i + 2, EnumRuneType.BLANK);
        }

        addCornerRunes(components, 6, 5, EnumRuneType.DUSK);
    }

    @Override
    public Ritual getNewCopy() {
        return new RitualEternalSoul();
    }
}
