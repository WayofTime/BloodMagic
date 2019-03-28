package WayofTime.bloodmagic.block;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.block.base.BlockEnum;
import WayofTime.bloodmagic.block.enums.BloodRuneType;
import WayofTime.bloodmagic.iface.IBloodRune;
import WayofTime.bloodmagic.util.helper.TextHelper;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class BlockBloodRune extends BlockEnum<BloodRuneType> implements IBloodRune {

    public BlockBloodRune() {
        super(Material.ROCK, BloodRuneType.class);

        setTranslationKey(BloodMagic.MODID + ".rune.");
        setCreativeTab(BloodMagic.TAB_BM);
        setSoundType(SoundType.STONE);
        setHardness(2.0F);
        setResistance(5.0F);
        setHarvestLevel("pickaxe", 2);
    }

    @Nullable
    @Override
    public BloodRuneType getBloodRune(IBlockAccess world, BlockPos pos, IBlockState state) {
        return state.getValue(getProperty());
    }

    @Override
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag tooltipFlag) {
        tooltip.add(TextHelper.localizeEffect("tooltip.bloodmagic.decoration.safe"));
        super.addInformation(stack, world, tooltip, tooltipFlag);
    }
}
