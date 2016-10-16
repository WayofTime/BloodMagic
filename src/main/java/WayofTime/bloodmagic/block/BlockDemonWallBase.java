package WayofTime.bloodmagic.block;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.IStringSerializable;

import org.apache.commons.lang3.tuple.Pair;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.block.base.BlockEnumWall;
import WayofTime.bloodmagic.client.IVariantProvider;

public class BlockDemonWallBase<E extends Enum<E> & IStringSerializable> extends BlockEnumWall<E> implements IVariantProvider
{
    public BlockDemonWallBase(String baseName, Material materialIn, Class<E> enumClass)
    {
        super(materialIn, enumClass);

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

        for (int i = 0; i < this.getTypes().length; i++)
            ret.add(Pair.of(i, "east=true,north=false,south=false,type=" + this.getTypes()[i] + ",up=true,west=true"));

        return ret;
    }
}