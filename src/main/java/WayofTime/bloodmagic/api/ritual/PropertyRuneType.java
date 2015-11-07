package WayofTime.bloodmagic.api.ritual;

import java.util.Collection;

import net.minecraft.block.properties.PropertyEnum;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

public class PropertyRuneType extends PropertyEnum
{
    protected PropertyRuneType(String name, Collection values)
    {
        super(name, EnumRuneType.class, values);
    }

    /**
     * Create a new PropertyRuneType with the given name
     */
    public static PropertyRuneType create(String name)
    {
        /**
         * Create a new PropertyRuneType with all directions that match the given Predicate
         */
        return create(name, Predicates.alwaysTrue());
    }

    /**
     * Create a new PropertyRuneType with all directions that match the given Predicate
     */
    public static PropertyRuneType create(String name, Predicate filter)
    {
        /**
         * Create a new PropertyRuneType for the given direction values
         */
        return create(name, Collections2.filter(Lists.newArrayList(EnumRuneType.values()), filter));
    }

    /**
     * Create a new PropertyRuneType for the given direction values
     */
    public static PropertyRuneType create(String name, Collection values)
    {
        return new PropertyRuneType(name, values);
    }
}