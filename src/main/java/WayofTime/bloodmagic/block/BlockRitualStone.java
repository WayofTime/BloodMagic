package WayofTime.bloodmagic.block;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.ritual.EnumRuneType;
import WayofTime.bloodmagic.api.ritual.IRitualStone;
import WayofTime.bloodmagic.block.base.BlockString;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockRitualStone extends BlockString implements IRitualStone {

    public static final String[] names = {"blank", "water", "fire", "earth", "air", "dusk", "dawn"};

    public BlockRitualStone() {
        super(Material.iron, names);

        setUnlocalizedName(Constants.Mod.MODID + ".ritualStone.");
        setCreativeTab(BloodMagic.tabBloodMagic);
        setStepSound(soundTypeStone);
        setHardness(2.0F);
        setResistance(5.0F);
        setHarvestLevel("pickaxe", 2);
    }

    @Override
    public boolean isRuneType(World world, BlockPos pos, EnumRuneType runeType) {
        return runeType.toString().equals(names[getMetaFromState(world.getBlockState(pos))]);
    }
}
