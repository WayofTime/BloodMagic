package WayofTime.bloodmagic.block.property;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import net.minecraft.block.properties.PropertyHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collection;

public class PropertyString extends PropertyHelper<String> {
    private final ImmutableSet<String> allowedValues;

    protected PropertyString(String name, String[] values) {
        super(name, String.class);
        allowedValues = ImmutableSet.copyOf(values);
    }

    @SideOnly(Side.CLIENT)
    public Optional<String> parseValue(String value) {
        return allowedValues.contains(value) ? Optional.of(value) : Optional.<String>absent();
    }

    @Override
    public Collection<String> getAllowedValues() {
        return allowedValues;
    }

    public String getName0(String value) {
        return value;
    }

    @Override
    public String getName(String value) {
        return this.getName0(value);
    }

    public static PropertyString create(String name, String[] values) {
        return new PropertyString(name, values);
    }
}
