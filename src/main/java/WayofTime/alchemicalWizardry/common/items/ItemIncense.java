package WayofTime.alchemicalWizardry.common.items;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.sacrifice.IIncense;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemIncense extends Item implements IIncense
{
<<<<<<< HEAD
    private static final String[] ITEM_NAMES = new String[]{"Woodash"};
=======
    private static final String[] ITEM_NAMES = new String[]{"Woodash", "Cloves"};
>>>>>>> origin/master

    @SideOnly(Side.CLIENT)
    private IIcon[] icons;

    public static int minValue;
    public static int maxValue;
    public static int itemDuration;
    
    public ItemIncense()
    {
        super();
        this.maxStackSize = 64;
        this.setCreativeTab(AlchemicalWizardry.tabBloodMagic);
        this.hasSubtypes = true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
        icons = new IIcon[ITEM_NAMES.length];

        for (int i = 0; i < ITEM_NAMES.length; ++i)
        {
            icons[i] = iconRegister.registerIcon("AlchemicalWizardry:" + "baseIncenseItem" + ITEM_NAMES[i]);
        }
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4)
    {
        list.add(StatCollector.translateToLocal("tooltip.alchemy.usedinincense"));
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack)
    {
        int meta = MathHelper.clamp_int(itemStack.getItemDamage(), 0, ITEM_NAMES.length - 1);
        return ("" + "item.bloodMagicIncenseItem." + ITEM_NAMES[meta]);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int meta)
    {
        int j = MathHelper.clamp_int(meta, 0, ITEM_NAMES.length - 1);
        return icons[j];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item id, CreativeTabs creativeTab, List list)
    {
        for (int meta = 0; meta < ITEM_NAMES.length; ++meta)
        {
            list.add(new ItemStack(id, 1, meta));
        }
    }

	@Override
	public int getMinLevel(ItemStack stack) 
	{
<<<<<<< HEAD
=======
		switch(stack.getItemDamage())
		{
		case 0:
			return 0;
		case 1:
			return 200;
		}
>>>>>>> origin/master
		return 0;
	}

	@Override
	public int getMaxLevel(ItemStack stack) 
	{
<<<<<<< HEAD
=======
		switch(stack.getItemDamage())
		{
		case 0:
			return 200;
		case 1:
			return 500;
		}
>>>>>>> origin/master
		return 100;
	}

	@Override
	public int getIncenseDuration(ItemStack stack) 
	{
		return 200;
	}
	
	@Override
	public float getTickRate(ItemStack stack)
	{
<<<<<<< HEAD
=======
		switch(stack.getItemDamage())
		{
		case 0:
			return 1.0f;
		case 1:
			return 0.5f;
		}
>>>>>>> origin/master
		return 1.0f;
	}

	@Override
	public float getRedColour(ItemStack stack) 
	{
		return 1.0f;
	}

	@Override
	public float getGreenColour(ItemStack stack) 
	{
		return 0;
	}

	@Override
	public float getBlueColour(ItemStack stack) 
	{
		return 0;
	}
}
