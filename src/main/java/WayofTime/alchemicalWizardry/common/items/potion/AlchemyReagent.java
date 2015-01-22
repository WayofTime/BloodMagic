package WayofTime.alchemicalWizardry.common.items.potion;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.ModItems;
import WayofTime.alchemicalWizardry.api.alchemy.AlchemyRecipeRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class AlchemyReagent extends Item
{
    public AlchemyReagent()
    {
        super();
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
        this.setMaxStackSize(64);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
        if (this == ModItems.incendium)
        {
            this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:Incendium");
            return;
        }
        if (this == ModItems.magicales)
        {
            this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:Magicales");
            return;
        }
        if (this == ModItems.sanctus)
        {
            this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:Sanctus");
            return;
        }
        if (this == ModItems.aether)
        {
            this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:Aether");
            return;
        }
        if (this == ModItems.simpleCatalyst)
        {
            this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:SimpleCatalyst");
            return;
        }
        if (this == ModItems.crepitous)
        {
            this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:Crepitous");
            return;
        }
        if (this == ModItems.crystallos)
        {
            this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:Crystallos");
            return;
        }
        if (this == ModItems.terrae)
        {
            this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:Terrae");
            return;
        }
        if (this == ModItems.aquasalus)
        {
            this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:Aquasalus");
            return;
        }
        if (this == ModItems.tennebrae)
        {
            this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:Tennebrae");
            return;
        }
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        par3List.add(StatCollector.translateToLocal("tooltip.alchemy.usedinalchemy"));

        if (Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
        {
            ItemStack[] recipe = AlchemyRecipeRegistry.getRecipeForItemStack(par1ItemStack);

            if (recipe != null)
            {
                par3List.add(EnumChatFormatting.BLUE + StatCollector.translateToLocal("tooltip.alchemy.recipe"));

                for (ItemStack item : recipe)
                {
                    if (item != null)
                    {
                        par3List.add("" + item.getDisplayName());
                    }
                }
            }
        } else
        {
            par3List.add("-" + StatCollector.translateToLocal("tooltip.alchemy.press") + " " + EnumChatFormatting.BLUE + StatCollector.translateToLocal("tooltip.alchemy.shift") + EnumChatFormatting.GRAY + " " + StatCollector.translateToLocal("tooltip.alchemy.forrecipe") + "-");
        }
    }
}
