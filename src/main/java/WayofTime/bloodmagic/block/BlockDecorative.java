package WayofTime.bloodmagic.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.block.base.BlockEnum;
import WayofTime.bloodmagic.block.enums.EnumDecorative;

public class BlockDecorative extends BlockEnum<EnumDecorative>
{
    public BlockDecorative()
    {
        super(Material.ROCK, EnumDecorative.class);

        setUnlocalizedName(BloodMagic.MODID + ".");
        setCreativeTab(BloodMagic.TAB_BM);
        setHardness(2.0F);
        setResistance(5.0F);
        setSoundType(SoundType.STONE);
        setHarvestLevel("pickaxe", 2);
    }
}
