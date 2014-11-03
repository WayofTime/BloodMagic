package bloodutils.api.compact;

import net.minecraft.item.ItemStack;
import bloodutils.api.enums.EnumType;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class Category {
	public Category(String name, ItemStack iconStack, EnumType type){
		this.name = name;
		this.iconStack = iconStack;
		this.type = type;
	}
	public String name;
	public ItemStack iconStack;
	
	public EnumType type;
}