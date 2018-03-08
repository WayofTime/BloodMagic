package WayofTime.bloodmagic.block;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.block.base.BlockEnumPillar;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;

import javax.annotation.Nonnull;

public class BlockDemonPillarBase<E extends Enum<E> & IStringSerializable> extends BlockEnumPillar<E> {
    public BlockDemonPillarBase(String baseName, Material materialIn, Class<E> enumClass) {
        super(materialIn, enumClass);

        setUnlocalizedName(BloodMagic.MODID + "." + baseName + ".");
        setCreativeTab(BloodMagic.TAB_BM);
        setHardness(2.0F);
        setResistance(5.0F);
        setSoundType(SoundType.STONE);
        setHarvestLevel("pickaxe", 2);
    }

    @Override
    public void gatherVariants(@Nonnull Int2ObjectMap<String> variants) {
        EnumFacing.Axis[] axis = new EnumFacing.Axis[]{EnumFacing.Axis.Y, EnumFacing.Axis.X, EnumFacing.Axis.Z};

        for (int i = 0; i < 3; i++)
            for (int j = 0; j < this.getTypes().length; j++)
                variants.put(i * 5 + j, "axis=" + axis[i] + ",type=" + this.getTypes()[j]);
    }
}