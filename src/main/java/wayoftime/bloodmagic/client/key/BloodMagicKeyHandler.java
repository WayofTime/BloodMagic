package wayoftime.bloodmagic.client.key;

import java.util.BitSet;

import org.lwjgl.glfw.GLFW;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.MinecraftForge;

public class BloodMagicKeyHandler
{

	private final BitSet keyDown;

	private final BitSet repeatings;

	public BloodMagicKeyHandler()
	{
		this.keyDown = new BitSet();
		this.repeatings = new BitSet();
		MinecraftForge.EVENT_BUS.addListener(this::keyTick);
	}

	public static boolean isKeyDown(KeyBinding keyBinding)
	{
		InputMappings.Input key = keyBinding.getKey();
		int keyCode = key.getKeyCode();
		if (keyCode != InputMappings.INPUT_INVALID.getKeyCode())
		{
			long windowHandle = Minecraft.getInstance().getMainWindow().getHandle();
			try
			{
				if (key.getType() == InputMappings.Type.KEYSYM)
				{
					return InputMappings.isKeyDown(windowHandle, keyCode);
				} else if (key.getType() == InputMappings.Type.MOUSE)
				{
					return GLFW.glfwGetMouseButton(windowHandle, keyCode) == GLFW.GLFW_PRESS;
				}
			} catch (Exception ignored)
			{
			}
		}
		return false;
	}

	public void keyTick(InputEvent.KeyInputEvent event)
	{
//		System.out.println("Pressing the key handlers");
		for (int i = 0; i < KeyBindings.values().length; i++)
		{
			KeyBindings keyBindings = KeyBindings.values()[i];
			KeyBinding keyBinding = keyBindings.getKey();
			boolean state = keyBinding.isKeyDown();
			boolean lastState = keyDown.get(i);
			if (state != lastState || (state && repeatings.get(i)))
			{
				if (state)
				{
					keyDown(keyBindings, lastState);
				} else
				{
					keyUp(keyBindings);
				}
				keyDown.set(i, state);
			}
		}
	}

	public void keyDown(KeyBindings kb, boolean isRepeat)
	{
		kb.handleKeybind();
	}

	public void keyUp(KeyBindings kb)
	{

	}
}
