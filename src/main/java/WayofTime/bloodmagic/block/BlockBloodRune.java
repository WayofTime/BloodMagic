package WayofTime.bloodmagic.block;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.block.base.BlockEnum;
import WayofTime.bloodmagic.block.enums.EnumBloodRune;
import WayofTime.bloodmagic.util.helper.TextHelper;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

public class BlockBloodRune extends BlockEnum<EnumBloodRune> {
    public BlockBloodRune() {
        super(Material.ROCK, EnumBloodRune.class);

        setUnlocalizedName(BloodMagic.MODID + ".rune.");
        setCreativeTab(BloodMagic.TAB_BM);
        setSoundType(SoundType.STONE);
        setHardness(2.0F);
        setResistance(5.0F);
        setHarvestLevel("pickaxe", 2);
    }

    public int getRuneEffect(int meta) {
        return meta;
    }

    @Override
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag tooltipFlag) {
        tooltip.add(TextHelper.localizeEffect("tooltip.bloodmagic.decoration.safe"));
        super.addInformation(stack, world, tooltip, tooltipFlag);
    }
}
