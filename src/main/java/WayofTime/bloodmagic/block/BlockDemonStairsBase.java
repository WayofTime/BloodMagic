package WayofTime.bloodmagic.block;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.block.base.BlockEnumStairs;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.IStringSerializable;

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
    public void gatherVariants(Int2ObjectMap<String> variants) {
        for (int i = 0; i < this.getTypes().length; i++)
            variants.put(i, "facing=south,half=bottom,shape=straight,type=" + this.getTypes()[i]);
    }
}