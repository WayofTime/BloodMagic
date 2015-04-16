package WayofTime.alchemicalWizardry.common.block;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.tileEntity.TEOrientable;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockOrientable extends BlockContainer
{
    public BlockOrientable()
    {
        super(Material.rock);
        setHardness(2.0F);
        setResistance(5.0F);
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int dunno)
    {
        return new TEOrientable();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float what, float these, float are)
    {
        //Right-click orients the output face. Shift-right-click orients the input face.
        if (world.isRemote)
        {
            return false;
        }

        ForgeDirection sideClicked = ForgeDirection.getOrientation(side);
        TileEntity tile = world.getTileEntity(x, y, z);

        if (tile instanceof TEOrientable)
        {
            TEOrientable newTile = (TEOrientable) tile;
            if (player.isSneaking())
            {
                int nextSide = TEOrientable.getIntForForgeDirection(newTile.getInputDirection()) + 1;

                if (nextSide > 5)
                {
                    nextSide = 0;
                }
                if (ForgeDirection.getOrientation(nextSide) == newTile.getOutputDirection())
                {
                    nextSide++;
                    if (nextSide > 5)
                    {
                        nextSide = 0;
                    }
                }

                newTile.setInputDirection(ForgeDirection.getOrientation(nextSide));
            } else
            {
                int nextSide = TEOrientable.getIntForForgeDirection(newTile.getOutputDirection()) + 1;

                if (nextSide > 5)
                {
                    nextSide = 0;
                }
                if (ForgeDirection.getOrientation(nextSide) == newTile.getInputDirection())
                {
                    nextSide++;
                    if (nextSide > 5)
                    {
                        nextSide = 0;
                    }
                }

                newTile.setOutputDirection(ForgeDirection.getOrientation(nextSide));
            }
        }

        world.markBlockForUpdate(x, y, z);
        return true;
    }

    @Override
    public int damageDropped(int metadata)
    {
        return metadata;
    }
}
