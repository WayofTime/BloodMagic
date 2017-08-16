package WayofTime.bloodmagic.block;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.block.base.BlockEnumStairs;
import com.google.common.collect.Lists;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.IStringSerializable;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class BlockDemonStairsBase<E extends Enum<E> & IStringSerializable> extends BlockEnumStairs<E> {
    public BlockDemonStairsBase(String baseName, Material materialIn, Class<E> enumClass) {
        super(materialIn, enumClass);

        setUnlocalizedName(BloodMagic.MODID + "." + baseName + ".");
        setCreativeTab(BloodMagic.TAB_BM);
        setHardness(2.0F);
        setResistance(5.0F);
        setSoundType(SoundType.STONE);
        setHarvestLevel("pickaxe", 2);
    }

    @Override
    public List<Pair<Integer, String>> getVariants() {
        List<Pair<Integer, String>> ret = Lists.newArrayList();

        for (int i = 0; i < this.getTypes().length; i++)
            ret.add(Pair.of(i, "facing=south,half=bottom,shape=straight,type=" + this.getTypes()[i]));

        return ret;
    }
}