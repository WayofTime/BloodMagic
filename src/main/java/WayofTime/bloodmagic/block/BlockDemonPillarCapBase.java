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
import WayofTime.bloodmagic.block.base.BlockStringPillarCap;
import WayofTime.bloodmagic.client.IVariantProvider;

public class BlockDemonPillarCapBase extends BlockStringPillarCap implements IVariantProvider
{
    public final String[] names;

    public BlockDemonPillarCapBase(String baseName, Material materialIn, String[] names)
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

        //This is done to make the ItemBlocks have the proper model

        for (int i = 0; i < EnumFacing.values().length; i++)
        {
            for (int j = 0; j < names.length; j++)
            {
                ret.add(new ImmutablePair<Integer, String>(i * 2 + j, "facing=" + EnumFacing.values()[i] + ",type=" + names[j]));
            }
        }

        return ret;
    }
}