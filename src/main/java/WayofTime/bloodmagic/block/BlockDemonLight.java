package WayofTime.bloodmagic.block;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.block.base.BlockEnum;
import WayofTime.bloodmagic.block.enums.EnumSubWillType;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockDemonLight extends BlockEnum<EnumSubWillType> {
    public BlockDemonLight() {
        super(Material.ROCK, EnumSubWillType.class);

        setTranslationKey(BloodMagic.MODID + ".demonlight.");
        setCreativeTab(BloodMagic.TAB_BM);
        setHardness(2.0F);
        setResistance(5.0F);
        setSoundType(SoundType.STONE);
        setHarvestLevel("pickaxe", 2);
        setLightLevel(1);
    }
}
