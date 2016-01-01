package WayofTime.bloodmagic.block.property;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import net.minecraft.block.properties.PropertyHelper;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

public class PropertyString extends PropertyHelper
{
    private final ImmutableSet allowedValues;

    protected PropertyString(String name, String[] values)
    {
        super(name, String.class);

        HashSet<String> hashSet = Sets.newHashSet();
        hashSet.addAll(Arrays.asList(values));
        allowedValues = ImmutableSet.copyOf(hashSet);
    }

    public static PropertyString create(String name, String[] values)
    {
        return new PropertyString(name, values);
    }

    @Override
    public Collection getAllowedValues()
    {
        return allowedValues;
    }

    public String getName0(String value)
    {
        return value;
    }

    @Override
    public String getName(Comparable value)
    {
        return this.getName0(value.toString());
    }
}
