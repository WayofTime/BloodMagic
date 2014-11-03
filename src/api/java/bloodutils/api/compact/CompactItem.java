package bloodutils.api.compact;

import net.minecraft.item.Item;

public class CompactItem {
	public CompactItem(Item item1, Item item2){
		this.item1 = item1;
		this.item2 = item2;
	}
	public Item item1;
	public Item item2;
	
	@Override
	public boolean equals(Object obj){
		if(obj instanceof CompactItem){
			CompactItem ci = (CompactItem)obj;
			return ci.item1 == this.item1 && ci.item2 == this.item2;
		}
		return false;
	}
}