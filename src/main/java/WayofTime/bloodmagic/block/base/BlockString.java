package WayofTime.bloodmagic.block.base;

import java.util.List;

import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import WayofTime.bloodmagic.block.property.PropertyString;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.ArrayUtils;

/**
 * Creates a block that has multiple meta-based states.
 * 
 * These states will be named after the given string array. Somewhere along the
 * way, each value is {@code toLowerCase()}'ed, so the blockstate JSON needs all
 * values to be lowercase.
 */
@Getter
public class BlockString extends Block
{
    private final int maxMeta;
    private final String[] types;
    private final PropertyString property;
    private final BlockStateContainer realStateContainer;

    public BlockString(Material material, String[] values, String propName)
    {
        super(material);

        this.maxMeta = values.length;
        this.types = values;

        this.property = PropertyString.create(propName, values);
        this.realStateContainer = createStateContainer();
        setDefaultState(getBlockState().getBaseState());
    }

    public BlockString(Material material, String[] values)
    {
        this(material, values, "type");
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
        return ArrayUtils.indexOf(types, state.getValue(property));
    }

    @Override
    public int damageDropped(IBlockState state)
    {
        return getMetaFromState(state);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(Item item, CreativeTabs tab, List<ItemStack> subBlocks)
    {
        for (int i = 0; i < maxMeta; i++)
            subBlocks.add(new ItemStack(item, 1, i));
    }

    protected BlockStateContainer createStateContainer()
    {
        System.out.println("");
        BlockStateContainer ctn = new BlockStateContainer.Builder(this).add(property).build();
        System.out.println("Number of states: " + ctn.getValidStates().size());
        return ctn;
    }
}
