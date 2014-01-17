package WayofTime.alchemicalWizardry.common.block;

import WayofTime.alchemicalWizardry.common.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.items.BlankSpell;
import WayofTime.alchemicalWizardry.common.tileEntity.TEHomHeart;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

public class BlockHomHeart extends BlockContainer
{
    public Icon bottomIcon;
    public Icon topIcon;
    public Icon sideIcon;

    public BlockHomHeart(int id)
    {
        super(id, Material.rock);
        setHardness(2.0F);
        setResistance(5.0F);
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
        setUnlocalizedName("blockHomHeart");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
        this.topIcon = iconRegister.registerIcon("AlchemicalWizardry:HomHeart_top");
        this.bottomIcon = iconRegister.registerIcon("AlchemicalWizardry:HomHeart_bottom");
        this.sideIcon = iconRegister.registerIcon("AlchemicalWizardry:HomHeart_side");
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
                return sideIcon;
        }
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int idk, float what, float these, float are)
    {
        TEHomHeart tileEntity = (TEHomHeart) world.getBlockTileEntity(x, y, z);

        if (tileEntity == null || player.isSneaking())
        {
            return false;
        }

        BlockGrass d;
        ItemStack playerItem = player.getCurrentEquippedItem();

        if (playerItem != null)
        {
            if (playerItem.getItem() instanceof BlankSpell)
            {
                if (playerItem.stackTagCompound == null)
                {
                    playerItem.setTagCompound(new NBTTagCompound());
                }

                NBTTagCompound itemTag = playerItem.stackTagCompound;
                itemTag.setInteger("xCoord", x);
                itemTag.setInteger("yCoord", y);
                itemTag.setInteger("zCoord", z);
                itemTag.setInteger("dimensionId", world.provider.dimensionId);
                return true;
            }
        }

        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World world)
    {
        return new TEHomHeart();
    }
}
