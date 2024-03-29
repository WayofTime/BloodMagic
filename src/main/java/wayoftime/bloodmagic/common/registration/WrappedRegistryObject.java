package wayoftime.bloodmagic.common.registration;

import java.util.function.Supplier;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraftforge.registries.RegistryObject;

//Copied from Mekanism
@ParametersAreNonnullByDefault
public class WrappedRegistryObject<T> implements Supplier<T>, INamedEntry
{
	protected RegistryObject<T> registryObject;

	protected WrappedRegistryObject(RegistryObject<T> registryObject)
	{
		this.registryObject = registryObject;
	}

	@Nonnull
	@Override
	public T get()
	{
		return registryObject.get();
	}

	@Override
	public String getInternalRegistryName()
	{
		return registryObject.getId().getPath();
	}
}