package WayofTime.bloodmagic.block;

import WayofTime.bloodmagic.client.IVariantProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.ritual.EnumRuneType;
import WayofTime.bloodmagic.api.ritual.IRitualStone;
import WayofTime.bloodmagic.block.base.BlockString;
import WayofTime.bloodmagic.registry.ModBlocks;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class BlockRitualStone extends BlockString implements IRitualStone, IVariantProvider
{
    public static final String[] names = { "blank", "water", "fire", "earth", "air", "dusk", "dawn" };

    public BlockRitualStone()
    {
        super(Material.iron, names);

        setUnlocalizedName(Constants.Mod.MODID + ".ritualStone.");
        setRegistryName(Constants.BloodMagicBlock.RITUAL_STONE.getRegName());
        setCreativeTab(BloodMagic.tabBloodMagic);
        setStepSound(soundTypeStone);
        setHardness(2.0F);
        setResistance(5.0F);
        setHarvestLevel("pickaxe", 2);
    }

    @Override
    public int damageDropped(IBlockState state)
    {
        return 0;
    }

    @Override
    public boolean isRuneType(World world, BlockPos pos, EnumRuneType runeType)
    {
        return runeType.toString().equals(names[getMetaFromState(world.getBlockState(pos))]);
    }

    @Override
    public void setRuneType(World world, BlockPos pos, EnumRuneType runeType)
    {
        int meta = runeType.ordinal();
        IBlockState newState = ModBlocks.ritualStone.getStateFromMeta(meta);
        world.setBlockState(pos, newState);
    }

    @Override
    public List<Pair<Integer, String>> getVariants()
    {
        List<Pair<Integer, String>> ret = new ArrayList<Pair<Integer, String>>();
        for (int i = 0; i < names.length; i++)
            ret.add(new ImmutablePair<Integer, String>(i, "type=" + names[i]));
        return ret;
    }
}
