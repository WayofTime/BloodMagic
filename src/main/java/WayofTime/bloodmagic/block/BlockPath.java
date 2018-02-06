package WayofTime.bloodmagic.block;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.apibutnotreally.incense.IIncensePath;
import WayofTime.bloodmagic.block.base.BlockEnum;
import WayofTime.bloodmagic.block.enums.EnumPath;
import WayofTime.bloodmagic.util.helper.TextHelper;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class BlockPath extends BlockEnum<EnumPath> implements IIncensePath {

    public BlockPath() {
        super(Material.ROCK, EnumPath.class);

        setUnlocalizedName(BloodMagic.MODID + ".path.");
        setCreativeTab(BloodMagic.TAB_BM);
        setHardness(2.0F);
        setResistance(5.0F);
        setSoundType(SoundType.STONE);

        setHarvestLevel("axe", 0, getStateFromMeta(0));
        setHarvestLevel("axe", 0, getStateFromMeta(1));
        setHarvestLevel("pickaxe", 0, getStateFromMeta(2));
        setHarvestLevel("pickaxe", 0, getStateFromMeta(3));
        setHarvestLevel("pickaxe", 0, getStateFromMeta(4));
        setHarvestLevel("pickaxe", 0, getStateFromMeta(5));
        setHarvestLevel("pickaxe", 3, getStateFromMeta(6));
        setHarvestLevel("pickaxe", 3, getStateFromMeta(7));
    }

    @Override
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag tooltipFlag) {
        tooltip.add(TextHelper.localizeEffect("tooltip.bloodmagic.decoration.safe"));
        super.addInformation(stack, world, tooltip, tooltipFlag);
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
