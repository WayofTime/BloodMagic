package WayofTime.bloodmagic.item;

import WayofTime.bloodmagic.item.types.AlchemicVialType;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;

public class ItemAlchemicVial extends ItemEnum.Variant<AlchemicVialType>
{
    public ItemAlchemicVial()
    {
        super(AlchemicVialType.class, "alchemic_vial");
    }

    @Override
    public void gatherVariants(Int2ObjectMap<String> variants)
    {
        for (AlchemicVialType type : types)
            variants.put(type.ordinal(), "type=normal");
    }
}