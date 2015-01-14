package WayofTime.alchemicalWizardry.client;

import net.minecraft.client.settings.KeyBinding;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.registry.ClientRegistry;

public class KeyBindings 
{
	public static KeyBinding omegaTest;
	
	public static void init()
	{
		omegaTest = new KeyBinding("key.ping", Keyboard.KEY_O, "key.categories.alchemicalwizardry");
		
		ClientRegistry.registerKeyBinding(omegaTest);
	}
}
