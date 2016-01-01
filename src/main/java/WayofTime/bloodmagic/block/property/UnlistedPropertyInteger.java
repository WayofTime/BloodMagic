package WayofTime.bloodmagic.block.property;

import net.minecraftforge.common.property.IUnlistedProperty;

public class UnlistedPropertyInteger implements IUnlistedProperty<Integer>
{
    private int maxMeta;
    private String propName;

    public UnlistedPropertyInteger(int maxMeta, String propName)
    {
        this.maxMeta = maxMeta;
        this.propName = propName;
    }

    @Override
    public String getName()
    {
        return propName;
    }

    @Override
    public boolean isValid(Integer value)
    {
        return value <= maxMeta;
    }

    @Override
    public Class<Integer> getType()
    {
        return Integer.class;
    }

    @Override
    public String valueToString(Integer value)
    {
        return value.toString();
    }
}
