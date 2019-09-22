package WayofTime.bloodmagic.block;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.block.base.BlockEnum;
import WayofTime.bloodmagic.block.enums.EnumInversionCap;
import WayofTime.bloodmagic.client.IVariantProvider;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockInversionPillarEnd extends BlockEnum<EnumInversionCap> implements IVariantProvider {
    public BlockInversionPillarEnd() {
        super(Material.ROCK, EnumInversionCap.class);

        setTranslationKey(BloodMagic.MODID + ".inversionpillarend.");
        setCreativeTab(BloodMagic.TAB_BM);
        setHardness(2.0F);
        setResistance(5.0F);
        setSoundType(SoundType.STONE);
        setHarvestLevel("pickaxe", 2);
    }

    @Override
    public boolean isOpaqueCube(BlockState state) {
        return false;
    }

    @Override
    public boolean isNormalCube(BlockState state, IBlockAccess world, BlockPos pos) {
        return false;
    }

    @Override
    public boolean isFullCube(BlockState state) {
        return false;
    }

    @Override
    public boolean causesSuffocation(BlockState state) {
        return false;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public void gatherVariants(Int2ObjectMap<String> variants) {
        for (int i = 0; i < this.getTypes().length; i++)
            variants.put(i, "type=" + this.getTypes()[i]);
    }
}
