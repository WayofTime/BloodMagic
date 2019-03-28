package WayofTime.bloodmagic.block;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.block.base.BlockEnum;
import WayofTime.bloodmagic.client.IVariantProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.IStringSerializable;

public class BlockDemonBase<E extends Enum<E> & IStringSerializable> extends BlockEnum<E> implements IVariantProvider {
    public BlockDemonBase(String baseName, Class<E> enumClass) {
        super(Material.ROCK, enumClass);

        setTranslationKey(BloodMagic.MODID + "." + baseName + ".");
        setCreativeTab(BloodMagic.TAB_BM);
        setHardness(2.0F);
        setResistance(5.0F);
        setSoundType(SoundType.STONE);
        setHarvestLevel("pickaxe", 2);
    }
}
