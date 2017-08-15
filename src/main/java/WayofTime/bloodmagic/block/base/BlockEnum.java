package WayofTime.bloodmagic.block.base;

import WayofTime.bloodmagic.block.IBMBlock;
import WayofTime.bloodmagic.client.IVariantProvider;
import WayofTime.bloodmagic.item.block.base.ItemBlockEnum;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class BlockEnum<E extends Enum<E> & IStringSerializable> extends Block implements IBMBlock, IVariantProvider
{
    private final E[] types;
    private final PropertyEnum<E> property;
    private final BlockStateContainer realStateContainer;

    public BlockEnum(Material material, Class<E> enumClass, String propName)
    {
        super(material);

        this.types = enumClass.getEnumConstants();
        this.property = PropertyEnum.create(propName, enumClass);
        this.realStateContainer = createStateContainer();
        setDefaultState(getBlockState().getBaseState());
    }

    public BlockEnum(Material material, Class<E> enumClass)
    {
        this(material, enumClass, "type");
    }

    @Override
    protected final BlockStateContainer createBlockState()
    {
        return new BlockStateContainer.Builder(this).build(); // Blank to avoid crashes
    }

    @Override
    public final BlockStateContainer getBlockState()
    {
        return realStateContainer;
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return getDefaultState().withProperty(property, types[meta]);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(property).ordinal();
    }

    @Override
    public int damageDropped(IBlockState state)
    {
        return getMetaFromState(state);
    }

    @Override
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> subBlocks)
    {
        for (E type : types)
            subBlocks.add(new ItemStack(this, 1, type.ordinal()));
    }

    protected BlockStateContainer createStateContainer()
    {
        return new BlockStateContainer.Builder(this).add(property).build();
    }

    @Override
    public ItemBlock getItem() {
        return new ItemBlockEnum<>(this);
    }

    @Override
    public List<Pair<Integer, String>> getVariants() {
        List<Pair<Integer, String>> variants = Lists.newArrayList();
        if (getItem() == null)
            return variants;

        for (int i = 0; i < types.length; i++)
            variants.add(Pair.of(i, getProperty().getName() + "=" + types[i].name()));

        return variants;
    }

    public E[] getTypes() {
        return types;
    }

    public PropertyEnum<E> getProperty() {
        return property;
    }

    public BlockStateContainer getRealStateContainer() {
        return realStateContainer;
    }
}
