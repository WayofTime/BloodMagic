package WayofTime.bloodmagic.block.base;

import java.util.List;

import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import WayofTime.bloodmagic.block.property.UnlistedPropertyInteger;

/**
 * Creates a block that has multiple meta-based states.
 * 
 * These states will be numbered 0 through {@code maxMeta}.
 * 
 * For {@link net.minecraft.tileentity.TileEntity}'s, use
 * {@link BlockIntegerContainer}.
 */
@Getter
public class BlockInteger extends Block
{
    private final int maxMeta;
    private final PropertyInteger metaProp;
    private final IUnlistedProperty unlistedMetaProp;
    private final BlockState realBlockState;

    public BlockInteger(Material material, int maxMeta, String propName)
    {
        super(material);

        this.maxMeta = maxMeta;

        this.metaProp = PropertyInteger.create(propName, 0, maxMeta);
        this.unlistedMetaProp = new UnlistedPropertyInteger(maxMeta, propName);
        this.realBlockState = createRealBlockState();
        setupStates();
    }

    public BlockInteger(Material material, int maxMeta)
    {
        this(material, maxMeta, "meta");
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return getBlockState().getBaseState().withProperty(metaProp, meta);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return (Integer) state.getValue(metaProp);
    }

    @Override
    public int damageDropped(IBlockState state)
    {
        return getMetaFromState(state);
    }

    @Override
    public BlockState getBlockState()
    {
        return this.realBlockState;
    }

    @Override
    public BlockState createBlockState()
    {
        return Blocks.air.getBlockState();
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, BlockPos pos, EntityPlayer player)
    {
        return new ItemStack(this, 1, this.getMetaFromState(world.getBlockState(pos)));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs creativeTabs, List<ItemStack> list)
    {
        for (int i = 0; i < maxMeta + 1; i++)
            list.add(new ItemStack(this, 1, i));
    }

    private void setupStates()
    {
        this.setDefaultState(getExtendedBlockState().withProperty(unlistedMetaProp, 0).withProperty(metaProp, 0));
    }

    public ExtendedBlockState getBaseExtendedState()
    {
        return (ExtendedBlockState) this.getBlockState();
    }

    public IExtendedBlockState getExtendedBlockState()
    {
        return (IExtendedBlockState) this.getBaseExtendedState().getBaseState();
    }

    private BlockState createRealBlockState()
    {
        return new ExtendedBlockState(this, new IProperty[] { metaProp }, new IUnlistedProperty[] { unlistedMetaProp });
    }
}
