package WayofTime.bloodmagic.block;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.block.base.BlockEnum;
import WayofTime.bloodmagic.block.enums.EnumPath;
import WayofTime.bloodmagic.incense.IIncensePath;
import WayofTime.bloodmagic.util.helper.TextHelper;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class BlockPath extends BlockEnum<EnumPath> implements IIncensePath {

    public BlockPath() {
        super(Material.ROCK, EnumPath.class);

        setTranslationKey(BloodMagic.MODID + ".path.");
        setCreativeTab(BloodMagic.TAB_BM);
        setHardness(2.0F);
        setResistance(5.0F);
        setSoundType(SoundType.STONE);

        setHarvestLevel("axe", 0, getDefaultState().withProperty(getProperty(), EnumPath.WOOD));
        setHarvestLevel("axe", 0, getDefaultState().withProperty(getProperty(), EnumPath.WOODTILE));
        setHarvestLevel("pickaxe", 0, getDefaultState().withProperty(getProperty(), EnumPath.STONE));
        setHarvestLevel("pickaxe", 0, getDefaultState().withProperty(getProperty(), EnumPath.STONETILE));
        setHarvestLevel("pickaxe", 0, getDefaultState().withProperty(getProperty(), EnumPath.WORNSTONE));
        setHarvestLevel("pickaxe", 0, getDefaultState().withProperty(getProperty(), EnumPath.WORNSTONETILE));
        setHarvestLevel("pickaxe", 3, getDefaultState().withProperty(getProperty(), EnumPath.OBSIDIAN));
        setHarvestLevel("pickaxe", 3, getDefaultState().withProperty(getProperty(), EnumPath.OBSIDIANTILE));
    }

    @Override
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag tooltipFlag) {
        tooltip.add(TextHelper.localizeEffect("tooltip.bloodmagic.decoration.safe"));
        super.addInformation(stack, world, tooltip, tooltipFlag);
    }

    @Override
    public Material getMaterial(IBlockState state) {
        EnumPath path = state.getValue(getProperty());
        if (path.equals(EnumPath.WOOD) || path.equals(EnumPath.WOODTILE))
            return Material.WOOD;
        else
            return Material.ROCK;
    }

    @Override
    public SoundType getSoundType(IBlockState state, World world, BlockPos pos, @Nullable Entity entity) {
        EnumPath path = state.getValue(getProperty());
        if (path.equals(EnumPath.WOOD) || path.equals(EnumPath.WOODTILE))
            return SoundType.WOOD;
        else
            return super.getSoundType();
    }

    @Override
    public int getLevelOfPath(World world, BlockPos pos, IBlockState state) {
        switch (this.getMetaFromState(state)) {
            case 0:
            case 1:
                return 2;
            case 2:
            case 3:
                return 4;
            case 4:
            case 5:
                return 6;
            case 6:
            case 7:
                return 8;
            default:
                return 0;
        }
    }
}
