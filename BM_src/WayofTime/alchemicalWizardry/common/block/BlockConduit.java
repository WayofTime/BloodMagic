package WayofTime.alchemicalWizardry.common.block;

import WayofTime.alchemicalWizardry.common.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.tileEntity.TEConduit;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public class BlockConduit extends BlockContainer {
    @SideOnly(Side.CLIENT)
    private static Icon topIcon;
    @SideOnly(Side.CLIENT)
    private static Icon sideIcon1;
    @SideOnly(Side.CLIENT)
    private static Icon sideIcon2;
    @SideOnly(Side.CLIENT)
    private static Icon bottomIcon;

    public BlockConduit(int id)
    {
        super(id, Material.rock);
        setHardness(2.0F);
        setResistance(5.0F);
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
        setUnlocalizedName("blockConduit");
        //func_111022_d("AlchemicalWizardry:blocks");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
        this.topIcon = iconRegister.registerIcon("AlchemicalWizardry:BloodAltar_Top");
        this.sideIcon1 = iconRegister.registerIcon("AlchemicalWizardry:BloodAltar_SideType1");
        this.sideIcon2 = iconRegister.registerIcon("AlchemicalWizardry:BloodAltar_SideType2");
        this.bottomIcon = iconRegister.registerIcon("AlchemicalWizardry:BloodAltar_Bottom");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int side, int meta)
    {
        switch (side)
        {
            case 0:
                return bottomIcon;

            case 1:
                return topIcon;

            //case 2: return sideIcon1;
            //case 3: return sideIcon1;
            //case 4: return sideIcon2;
            //case 5: return sideIcon2;
            default:
                return sideIcon2;
        }
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float what, float these, float are)
    {
        if (world.isRemote)
        {
            return false;
        }

        ForgeDirection sideClicked = ForgeDirection.getOrientation(side);
        TileEntity tile = world.getBlockTileEntity(x, y, z);

        if (tile instanceof TEConduit)
        {
            //TODO NEEDS WORK
            if (((TEConduit) tile).getInputDirection().equals(sideClicked))
            {
                ((TEConduit) tile).setInputDirection(((TEConduit) tile).getOutputDirection());
                ((TEConduit) tile).setOutputDirection(sideClicked);
            } else if (((TEConduit) tile).getOutputDirection().equals(sideClicked))
            {
                ((TEConduit) tile).setOutputDirection(((TEConduit) tile).getInputDirection());
                ((TEConduit) tile).setInputDirection(sideClicked);
            } else
            {
                if (!player.isSneaking())
                {
                    ((TEConduit) tile).setOutputDirection(sideClicked);
                } else
                {
                    ((TEConduit) tile).setOutputDirection(sideClicked.getOpposite());
                }
            }
        }

        world.markBlockForUpdate(x, y, z);
        return true;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, int par5, int par6)
    {
        //dropItems(world, x, y, z);
        super.breakBlock(world, x, y, z, par5, par6);
    }

    @Override
    public TileEntity createNewTileEntity(World world)
    {
        return new TEConduit();
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
}
