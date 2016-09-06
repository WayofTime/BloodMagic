package WayofTime.bloodmagic.block;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.block.base.BlockStringStairs;
import WayofTime.bloodmagic.client.IVariantProvider;

public class BlockDemonStairsBase extends BlockStringStairs implements IVariantProvider
{
    public final String[] names;

    public BlockDemonStairsBase(String baseName, Material materialIn, String[] names)
    {
        super(materialIn, names);
        this.names = names;

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

        for (int i = 0; i < names.length; i++)
        {
            ret.add(new ImmutablePair<Integer, String>(i, "facing=south,half=bottom,shape=straight,type=" + names[i]));
        }

        return ret;
    }
}