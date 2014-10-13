package WayofTime.alchemicalWizardry.common.block;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.tileEntity.TEBellJar;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.ArrayList;

public class BlockBelljar extends BlockContainer
{
    public BlockBelljar()
    {
        super(Material.glass);
        setHardness(2.0F);
        setResistance(5.0F);
        this.setCreativeTab(AlchemicalWizardry.tabBloodMagic);
        this.setBlockName("crystalBelljar");
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack stack)
    {
        TileEntity tile = world.getTileEntity(x, y, z);

        if (tile instanceof TEBellJar)
        {
            NBTTagCompound tag = stack.getTagCompound();
            if (tag != null)
            {
                ((TEBellJar) tile).readTankNBTOnPlace(tag);
            }
        }
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TEBellJar();
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public int getRenderType()
    {
        return -1;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public boolean hasTileEntity()
    {
        return true;
    }

    @Override
    public boolean hasComparatorInputOverride()
    {
        return true;
    }

    @Override
    public int getComparatorInputOverride(World world, int x, int y, int z, int meta)
    {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof TEBellJar)
        {
            return ((TEBellJar) tile).getRSPowerOutput();
        }
        return 15;
    }

    @Override
    public void onBlockHarvested(World world, int x, int y, int z, int meta, EntityPlayer player)
    {
        this.dropBlockAsItem(world, x, y, z, meta, 0);
        super.onBlockHarvested(world, x, y, z, meta, player);
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune)
    {
        ArrayList<ItemStack> list = new ArrayList();

        TileEntity tile = world.getTileEntity(x, y, z);

        if (tile instanceof TEBellJar)
        {
            ItemStack drop = new ItemStack(this);
            NBTTagCompound tag = new NBTTagCompound();
            ((TEBellJar) tile).writeTankNBT(tag);
            drop.stackTagCompound = tag;

            list.add(drop);
        }

        return list;
    }
}
