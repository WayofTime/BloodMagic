package WayofTime.alchemicalWizardry.common.items.potion;

import java.util.List;

import org.lwjgl.input.Keyboard;

import WayofTime.alchemicalWizardry.common.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.alchemy.AlchemyRecipeRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

public class AlchemyReagent extends Item
{
    public AlchemyReagent(int id)
    {
        super(id);
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
        this.setMaxStackSize(64);
        // TODO Auto-generated constructor stub
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
        if (this.itemID == AlchemicalWizardry.incendium.itemID)
        {
            this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:Incendium");
            return;
        }

        if (this.itemID == AlchemicalWizardry.magicales.itemID)
        {
            this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:Magicales");
            return;
        }

        if (this.itemID == AlchemicalWizardry.sanctus.itemID)
        {
            this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:Sanctus");
            return;
        }

        if (this.itemID == AlchemicalWizardry.aether.itemID)
        {
            this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:Aether");
            return;
        }

        if (this.itemID == AlchemicalWizardry.simpleCatalyst.itemID)
        {
            this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:SimpleCatalyst");
            return;
        }

        if (this.itemID == AlchemicalWizardry.crepitous.itemID)
        {
            this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:Crepitous");
            return;
        }

        if (this.itemID == AlchemicalWizardry.crystallos.itemID)
        {
            this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:Crystallos");
            return;
        }

        if (this.itemID == AlchemicalWizardry.terrae.itemID)
        {
            this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:Terrae");
            return;
        }

        if (this.itemID == AlchemicalWizardry.aquasalus.itemID)
        {
            this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:Aquasalus");
            return;
        }

        if (this.itemID == AlchemicalWizardry.tennebrae.itemID)
        {
            this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:Tennebrae");
            return;
        }
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        par3List.add("Used in alchemy");

        if (Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
        {
            ItemStack[] recipe = AlchemyRecipeRegistry.getRecipeForItemStack(par1ItemStack);

            if (recipe != null)
            {
                par3List.add(EnumChatFormatting.BLUE + "Recipe:");

                for (ItemStack item: recipe)
                {
                    if (item != null)
                    {
                        par3List.add("" + item.getDisplayName());
                    }
                }
            }
        }
        else
        {
            par3List.add("-Press " + EnumChatFormatting.BLUE + "shift" + EnumChatFormatting.GRAY + " for Recipe-");
        }
    }
}
