package WayofTime.alchemicalWizardry.common.tileEntity.gui;

import WayofTime.alchemicalWizardry.common.tileEntity.TETeleposer;
import WayofTime.alchemicalWizardry.common.tileEntity.TEWritingTable;
import WayofTime.alchemicalWizardry.common.tileEntity.container.ContainerTeleposer;
import WayofTime.alchemicalWizardry.common.tileEntity.container.ContainerWritingTable;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class GuiHandler implements IGuiHandler
{
    //returns an instance of the Container you made earlier
    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
    {
        TileEntity tileEntity;

        switch (id)
        {
            case 0:
                tileEntity = world.getTileEntity(x, y, z);

                if (tileEntity instanceof TEWritingTable)
                {
                    return new ContainerWritingTable(player.inventory, (TEWritingTable) tileEntity);
                }

            case 1:
                tileEntity = world.getTileEntity(x, y, z);

                if (tileEntity instanceof TETeleposer)
                {
                    return new ContainerTeleposer(player.inventory, (TETeleposer) tileEntity);
                }
        }

        return null;
    }

    //returns an instance of the Gui you made earlier
    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
    {
        TileEntity tileEntity;

        switch (id)
        {
            case 0:
                tileEntity = world.getTileEntity(x, y, z);

                if (tileEntity instanceof TEWritingTable)
                {
                    return new GuiWritingTable(player.inventory, (TEWritingTable) tileEntity);
                }

                break;

            case 1:
                tileEntity = world.getTileEntity(x, y, z);

                if (tileEntity instanceof TETeleposer)
                {
                    return new GuiTeleposer(player.inventory, (TETeleposer) tileEntity);
                }

                break;
        }

        return null;
    }
}