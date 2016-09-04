package WayofTime.bloodmagic.block;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.EnumFacing;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.block.base.BlockStringPillar;
import WayofTime.bloodmagic.client.IVariantProvider;

public class BlockDemonPillar extends BlockStringPillar implements IVariantProvider
{
    public static final String[] names = new String[] { "raw", "corrosive" };

    public BlockDemonPillar(String baseName, Material materialIn)
    {
        super(materialIn, names);

        setUnlocalizedName(Constants.Mod.MODID + "." + baseName + ".");
        setCreativeTab(BloodMagic.tabBloodMagic);
        setHardness(2.0F);
        setResistance(5.0F);
        setSoundType(SoundType.STONE);
        setHarvestLevel("pickaxe", 2);
    }

    @Override
    public List<Pair<Integer, String>> getVariants()
    {
        List<Pair<Integer, String>> ret = new ArrayList<Pair<Integer, String>>();
//        for (int i = 0; i < 3; i++)
//        {
//            ret.add(new ImmutablePair<Integer, String>(i, "axis=" + EnumFacing.Axis.values()[i]));
//        }
//
//        for (int i = 0; i < names.length; i++)
//        {
//            ret.add(new ImmutablePair<Integer, String>(i + 3, "type=" + names[i]));
//        }

        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < names.length; j++)
            {
                ret.add(new ImmutablePair<Integer, String>(j * 3 + i, "axis=" + EnumFacing.Axis.values()[i] + ",type=" + names[j]));
            }
        }

        return ret;
    }
}