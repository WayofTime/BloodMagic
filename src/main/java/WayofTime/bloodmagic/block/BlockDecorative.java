package WayofTime.bloodmagic.block;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.ConfigHandler;
import WayofTime.bloodmagic.block.base.BlockEnum;
import WayofTime.bloodmagic.block.enums.EnumDecorative;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class BlockDecorative extends BlockEnum<EnumDecorative> {
    public BlockDecorative() {
        super(Material.ROCK, EnumDecorative.class);

        setTranslationKey(BloodMagic.MODID + ".");
        setCreativeTab(BloodMagic.TAB_BM);
        setHardness(2.0F);
        setResistance(5.0F);
        setSoundType(SoundType.STONE);
        setHarvestLevel("pickaxe", 2);
    }

    @Override
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> subBlocks) {
        for (EnumDecorative type : EnumDecorative.values()) {
            if (!ConfigHandler.general.enableTierSixEvenThoughThereIsNoContent && (type == EnumDecorative.CRYSTAL_TILE || type == EnumDecorative.CRYSTAL_BRICK))
                continue;
            subBlocks.add(new ItemStack(this, 1, type.ordinal()));
        }
    }
}
