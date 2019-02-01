package WayofTime.bloodmagic.ritual.types;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.altar.IBloodAltar;
import WayofTime.bloodmagic.block.BlockLifeEssence;
import WayofTime.bloodmagic.ritual.*;
import WayofTime.bloodmagic.util.helper.NetworkHelper;
import WayofTime.bloodmagic.util.helper.PlayerHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

@RitualRegister("eternal_soul")
public class RitualEternalSoul extends Ritual {


    private IBloodAltar altar = null;

    public RitualEternalSoul() {
        super("ritualEternalSoul", 2, 2000000, "ritual." + BloodMagic.MODID + ".eternalSoulRitual");
    }

    @Override
    public void performRitual(IMasterRitualStone masterRitualStone) {
        UUID owner = masterRitualStone.getOwner();
        int currentEssence = NetworkHelper.getSoulNetwork(owner).getCurrentEssence();
        World world = masterRitualStone.getWorldObj();
        BlockPos pos = masterRitualStone.getBlockPos();

        if (this.altar == null) {
            for (int i = -5; i <= 5; i++) {
                for (int j = -5; j <= 5; j++) {
                    for (int k = -10; k <= 10; k++) {
                        if (world.getTileEntity(new BlockPos(pos.getX() + i, pos.getY() + j, pos.getZ() + k)) instanceof IBloodAltar) {
                            this.altar = (IBloodAltar) world.getTileEntity(new BlockPos(pos.getX() + i, pos.getY() + j, pos.getZ() + k));
                        }
                    }
                }
            }
        }
        if (!(this.altar instanceof IFluidHandler))
            return;

        int horizontalRange = 15;
        int verticalRange = 20;

        List<EntityPlayer> list = world.getEntitiesWithinAABB(EntityPlayer.class,
                new AxisAlignedBB(pos.getX() - 0.5f, pos.getY() - 0.5f, pos.getZ() - 0.5f,
                        pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f)
                        .expand(horizontalRange, verticalRange, horizontalRange));

        EntityPlayer entityOwner = null;
        for (EntityPlayer player : list) {
            if (PlayerHelper.getUUIDFromPlayer(player) == owner)
                entityOwner = player;
        }

        int fillAmount = Math.min(currentEssence / 2, ((IFluidHandler) this.altar).fill(new FluidStack(BlockLifeEssence.getLifeEssence(), 10000), false));

        ((IFluidHandler) this.altar).fill(new FluidStack(BlockLifeEssence.getLifeEssence(), fillAmount), true);

        if (entityOwner != null && entityOwner.getHealth() > 2.0f && fillAmount != 0)
            entityOwner.setHealth(2.0f);

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
        addCornerRunes(components, 0, 1, EnumRuneType.FIRE);

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
