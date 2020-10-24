package wayoftime.bloodmagic.common.registration;

import java.util.function.Supplier;

import javax.annotation.Nonnull;

import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class WrappedRegistryObject<T extends IForgeRegistryEntry<? super T>> implements Supplier<T>, INamedEntry
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