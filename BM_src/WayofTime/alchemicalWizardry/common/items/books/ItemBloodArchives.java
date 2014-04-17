package WayofTime.alchemicalWizardry.common.items.books;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemEditableBook;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.client.GuiManual;
import cpw.mods.fml.client.FMLClientHandler;

public class ItemBloodArchives extends ItemEditableBook
{
	public static ItemStack book1Stack;
	
	public ItemBloodArchives(int par1) 
	{
		super(par1);
		setCreativeTab(AlchemicalWizardry.tabBloodMagic);
	}
	
	public static void initBooks()
	{
		ItemStack tomeStack = new ItemStack(Item.writableBook);
		NBTTagList bookPages = new NBTTagList("pages");
		bookPages.appendTag(new NBTTagString("1", "Insert text here."));
		bookPages.appendTag(new NBTTagString("2", "Insert moar text here."));
		tomeStack.setTagInfo("pages", bookPages);
		tomeStack.setTagInfo("author", new NBTTagString("author", "WayofTime"));
		tomeStack.setTagInfo("title", new NBTTagString("title", "Blood Book"));
		tomeStack.itemID = Item.writtenBook.itemID;
		
		book1Stack = tomeStack.copy();
	}
	
	@Override
    public ItemStack onItemRightClick (ItemStack stack, World world, EntityPlayer player)
    {
        player.openGui(AlchemicalWizardry.instance, 2, world, 0, 0, 0);
        //FMLClientHandler.instance().displayGuiScreen(player, new GuiManual(stack, getData(stack)));
        /*Side side = FMLCommonHandler.instance().getEffectiveSide();
        if (side.isClient())
        	FMLClientHandler.instance().displayGuiScreen(player, new GuiManual(player.getCurrentEquippedItem(), getManualFromStack(stack)));*/
        return stack;
    }
	
//	@Override
//    @SideOnly(Side.CLIENT)
//    public void getSubItems(int id, CreativeTabs creativeTab, List list)
//    {
//        list.add(book1Stack);
//    }
	
//	private BookData getData (ItemStack stack)
//    {
//        switch (stack.getItemDamage())
//        {
//        case 0:
//            return TProxyClient.manualData.beginner;
//        case 1:
//            return TProxyClient.manualData.toolStation;
//        case 2:
//            return TProxyClient.manualData.smeltery;
//        default:
//            return TProxyClient.manualData.diary;
//        }
//    }

}
