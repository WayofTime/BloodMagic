package WayofTime.alchemicalWizardry.common.block;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.items.BlankSpell;
import WayofTime.alchemicalWizardry.common.tileEntity.TEHomHeart;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockSpellTable extends BlockContainer
{
    @SideOnly(Side.CLIENT)
    private IIcon bottomIcon;
    @SideOnly(Side.CLIENT)
    private IIcon topIcon;
    @SideOnly(Side.CLIENT)
    private IIcon sideIcon;

    public BlockSpellTable()
    {
        super(Material.rock);
        setHardness(2.0F);
        setResistance(5.0F);
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
        this.setBlockName("blockHomHeart");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        this.topIcon = iconRegister.registerIcon("AlchemicalWizardry:HomHeart_top");
        this.bottomIcon = iconRegister.registerIcon("AlchemicalWizardry:HomHeart_bottom");
        this.sideIcon = iconRegister.registerIcon("AlchemicalWizardry:HomHeart_side");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta)
    {
        switch (side)
        {
            case 0:
                return bottomIcon;
            case 1:
                return topIcon;
            default:
                return sideIcon;
        }
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int idk, float what, float these, float are)
    {
        TEHomHeart tileEntity = (TEHomHeart) world.getTileEntity(x, y, z);

        if (tileEntity == null || player.isSneaking())
        {
            return false;
        }

        ItemStack playerItem = player.getCurrentEquippedItem();

        if (playerItem != null)
        {
            if (playerItem.getItem() instanceof BlankSpell)
            {
                if (playerItem.getTagCompound() == null)
                {
                    playerItem.setTagCompound(new NBTTagCompound());
                }

                NBTTagCompound itemTag = playerItem.getTagCompound();
                itemTag.setInteger("xCoord", x);
                itemTag.setInteger("yCoord", y);
                itemTag.setInteger("zCoord", z);
                itemTag.setInteger("dimensionId", world.provider.getDimensionId());
                return true;
            }
        }

        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metaMaybe)
    {
        return new TEHomHeart();
    }
}
