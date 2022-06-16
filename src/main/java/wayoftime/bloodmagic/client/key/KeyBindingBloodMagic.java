package wayoftime.bloodmagic.client.key;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ClientRegistry;

@OnlyIn(Dist.CLIENT)
public class KeyBindingBloodMagic extends KeyMapping
{
	public KeyBindingBloodMagic(KeyBindings key)
	{
		super(key.getDescription(), -1, "key.bloodmagic.category");

		ClientRegistry.registerKeyBinding(this);
	}
}
