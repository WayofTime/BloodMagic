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
import WayofTime.bloodmagic.block.base.BlockStringWall;
import WayofTime.bloodmagic.client.IVariantProvider;

public class BlockDemonWallBase extends BlockStringWall implements IVariantProvider
{
    public final String[] names;

    public BlockDemonWallBase(String baseName, Material materialIn, String[] names)
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

        for (int i = 0; i < 32; i++)
        {
            boolean up = (i & 1) == 0;
            boolean north = (i & 2) == 2;
            boolean south = (i & 4) == 4;
            boolean east = (i & 8) == 8;
            boolean west = (i & 16) == 16;
            for (int j = 0; j < names.length; j++)
            {
                ret.add(new ImmutablePair<Integer, String>(i * names.length + j, "up=" + (up ? "true" : "false") + ",north=" + (north ? "true" : "false") + ",south=" + (south ? "true" : "false") + ",east=" + (east ? "true" : "false") + ",west=" + (west ? "true" : "false") + ",type=" + names[j]));
            }
        }

        return ret;
    }
}