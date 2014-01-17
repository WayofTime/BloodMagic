package WayofTime.alchemicalWizardry.common.items;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.ModItems;
import WayofTime.alchemicalWizardry.common.ArmourUpgrade;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import thaumcraft.api.IGoggles;
import thaumcraft.api.IVisDiscounter;
import thaumcraft.api.nodes.IRevealer;

import java.util.List;

public class ItemSanguineArmour extends ItemArmor implements IRevealer, ArmourUpgrade, IGoggles, IVisDiscounter
{
    private static Icon helmetIcon;

    public ItemSanguineArmour(int par1)
    {
        super(par1, AlchemicalWizardry.sanguineArmourArmourMaterial, 4, 0);
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
        //this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:SheathedItem");
        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:SanguineHelmet");
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, int layer)
    {
        //if(AlchemicalWizardry.isThaumcraftLoaded)
        {
            if (itemID == ModItems.sanguineHelmet.itemID)
            {
                return "alchemicalwizardry:models/armor/sanguineArmour_layer_1.png";
            }
        }
        return null;
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        par3List.add("A pair of goggles imbued with power");
        par3List.add("Vis discount: " + this.getVisDiscount() + "%");
    }

    @Override
    public boolean showNodes(ItemStack itemstack, EntityLivingBase player)
    {
        return true;
    }

    @Override
    public void onArmourUpdate(World world, EntityPlayer player, ItemStack thisItemStack)
    {
        return;
    }

    @Override
    public boolean isUpgrade()
    {
        return true;
    }

    @Override
    public int getEnergyForTenSeconds()
    {
        return 0;
    }

    @Override
    public boolean showIngamePopups(ItemStack itemstack, EntityLivingBase player)
    {
        return true;
    }

    @Override
    public int getVisDiscount()
    {
        return 10;
    }
}
