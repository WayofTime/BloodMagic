package WayofTime.alchemicalWizardry.common.book;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.entity.mob.BookEntityItem;

public class ItemBMBook extends Item
{
	public ItemBMBook() 
	{
		super();
		setMaxStackSize(1);
		this.setCreativeTab(AlchemicalWizardry.tabBloodMagic);
	}

	@Override
	public void registerIcons(IIconRegister ir)
	{
		itemIcon = ir.registerIcon("AlchemicalWizardry" + ":" + "guide");
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
	{
		player.openGui(AlchemicalWizardry.instance, 2, world, (int)player.posX, (int)player.posY, (int)player.posZ);
		return stack;
	}
	
	@Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int par4, boolean par5)
	{
    	super.onUpdate(stack, world, entity, par4, par5);
		if(!stack.hasTagCompound())
    		stack.setTagCompound(new NBTTagCompound());
    }

    @Override
    public void onCreated(ItemStack stack, World world, EntityPlayer player)
    {
    	super.onCreated(stack, world, player);
    	if(!stack.hasTagCompound())
    		stack.setTagCompound(new NBTTagCompound());
    }

    @Override
    public boolean hasCustomEntity(ItemStack stack)
	{
		return true;
	}

    @Override
	public Entity createEntity(World world, Entity location, ItemStack itemstack)
	{
		return new BookEntityItem(world, location, itemstack);
	}
}
