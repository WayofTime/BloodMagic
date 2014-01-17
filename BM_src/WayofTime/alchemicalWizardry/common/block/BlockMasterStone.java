package WayofTime.alchemicalWizardry.common.block;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.items.ActivationCrystal;
import WayofTime.alchemicalWizardry.common.tileEntity.TEMasterStone;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockMasterStone extends BlockContainer
{
    public BlockMasterStone(int id)
    {
        super(id, Material.iron);
        setHardness(2.0F);
        setResistance(5.0F);
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
        setUnlocalizedName("blockMasterStone");
        // TODO Auto-generated constructor stub
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
        this.blockIcon = iconRegister.registerIcon("AlchemicalWizardry:MasterStone");
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int idk, float what, float these, float are)
    {
        TEMasterStone tileEntity = (TEMasterStone) world.getBlockTileEntity(x, y, z);

        if (tileEntity == null || player.isSneaking())
        {
            return false;
        }

        ItemStack playerItem = player.getCurrentEquippedItem();

        if (playerItem == null)
        {
            return false;
        }

        Item item = playerItem.getItem();

        if (!(item instanceof ActivationCrystal))
        {
            return false;
        }

        ActivationCrystal acItem = (ActivationCrystal) item;
        tileEntity.setOwner(acItem.getOwnerName(playerItem));
        tileEntity.activateRitual(world, acItem.getCrystalLevel(playerItem));
        world.markBlockForUpdate(x, y, z);
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world)
    {
        return new TEMasterStone();
    }
}
