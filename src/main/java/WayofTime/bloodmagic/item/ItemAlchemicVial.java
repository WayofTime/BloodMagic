package WayofTime.bloodmagic.item;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;

import javax.annotation.Nonnull;

import WayofTime.bloodmagic.item.types.AlchemicTypes;
import WayofTime.bloodmagic.item.types.ISubItem;

public class ItemAlchemicVial<T extends Enum<T> & ISubItem> extends ItemEnum.Variant<T>
{
    public ItemAlchemicVial()
    {
        super((Class<T>) AlchemicTypes.class, "alchemic_vial");
    }

    @Override
    public void gatherVariants(@Nonnull Int2ObjectMap<String> variants)
    {
        variants.put(0, "type=normal");
    }
}