package WayofTime.alchemicalWizardry.common.items;

import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.spell.complex.SpellParadigm;
import WayofTime.alchemicalWizardry.common.spell.complex.SpellParadigmSelf;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellEffectFire;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemComplexSpellCrystal extends EnergyItems 
{

	public ItemComplexSpellCrystal(int id) 
	{
		super(id);
		this.maxStackSize = 1;
        //setMaxDamage(1000);
        setEnergyUsed(50);
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
	}
	
	@Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        par3List.add("I feel lighter already...");

        if (!(par1ItemStack.stackTagCompound == null))
        {
            par3List.add("Current owner: " + par1ItemStack.stackTagCompound.getString("ownerName"));
        }
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:AirSigil");
    }

    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        EnergyItems.checkAndSetItemOwner(par1ItemStack, par3EntityPlayer);

        if (par3EntityPlayer.isSneaking())
        {
            return par1ItemStack;
        }

        SpellParadigm parad = new SpellParadigmSelf();
        parad.addBufferedEffect(new SpellEffectFire());
        parad.castSpell(par2World, par3EntityPlayer, par1ItemStack);

        return par1ItemStack;
    }
}
