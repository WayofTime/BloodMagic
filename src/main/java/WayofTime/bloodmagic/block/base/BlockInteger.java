package WayofTime.bloodmagic.block.base;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

/**
 * Creates a block that has multiple meta-based states.
 * <p>
 * These states will be numbered 0 through {@code maxMeta}.
 */
public class BlockInteger extends Block {
    private final int maxMeta;
    private final PropertyInteger property;
    private final BlockStateContainer realStateContainer;

    public BlockInteger(Material material, int maxMeta, String propName) {
        super(material);

        this.maxMeta = maxMeta;
        this.property = PropertyInteger.create(propName, 0, maxMeta);
        this.realStateContainer = createStateContainer();
        setDefaultState(getBlockState().getBaseState());
    }

    public BlockInteger(Material material, int maxMeta) {
        this(material, maxMeta, "meta");
    }

    @Override
    protected final BlockStateContainer createBlockState() {
        return new BlockStateContainer.Builder(this).build(); // Blank to avoid crashes
    }

    @Override
    public final BlockStateContainer getBlockState() {
        return realStateContainer;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(property, meta);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(property);
    }

    @Override
    public int damageDropped(IBlockState state) {
        return getMetaFromState(state);
    }

    @Override
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> subBlocks) {
        for (int i = 0; i < maxMeta; i++)
            subBlocks.add(new ItemStack(this, 1, i));
    }

    protected BlockStateContainer createStateContainer() {
        return new BlockStateContainer.Builder(this).add(property).build();
    }

    public int getMaxMeta() {
        return maxMeta;
    }

    public PropertyInteger getProperty() {
        return property;
    }

    public BlockStateContainer getRealStateContainer() {
        return realStateContainer;
    }
}
