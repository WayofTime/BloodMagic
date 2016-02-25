package WayofTime.bloodmagic.block.property;

import java.util.Arrays;
import java.util.List;

import net.minecraftforge.common.property.IUnlistedProperty;

public class UnlistedPropertyString implements IUnlistedProperty<String>
{
    private List values;
    private String propName;

    public UnlistedPropertyString(String[] values, String propName)
    {
        this.values = Arrays.asList(values);
        this.propName = propName;
    }

    @Override
    public String getName()
    {
        return propName;
    }

    @Override
    public boolean isValid(String value)
    {
        return values.contains(value);
    }

    @Override
    public Class<String> getType()
    {
        return String.class;
    }

    @Override
    public String valueToString(String value)
    {
        return value;
    }
}
