package WayofTime.bloodmagic.block;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

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

        for (int i = 0; i < names.length; i++)
            ret.add(Pair.of(i, "east=true,north=false,south=false,type=" + names[i] + ",up=true,west=true"));

        return ret;
    }
}