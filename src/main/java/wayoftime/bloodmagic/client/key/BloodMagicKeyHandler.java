package wayoftime.bloodmagic.client.key;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.glfw.GLFW;

import java.util.BitSet;

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

	public static boolean isKeyDown(KeyMapping keyBinding)
	{
		InputConstants.Key key = keyBinding.getKey();
		int keyCode = key.getValue();
		if (keyCode != InputConstants.UNKNOWN.getValue())
		{
			long windowHandle = Minecraft.getInstance().getWindow().getWindow();
			try
			{
				if (key.getType() == InputConstants.Type.KEYSYM)
				{
					return InputConstants.isKeyDown(windowHandle, keyCode);
				} else if (key.getType() == InputConstants.Type.MOUSE)
				{
					return GLFW.glfwGetMouseButton(windowHandle, keyCode) == GLFW.GLFW_PRESS;
				}
			} catch (Exception ignored)
			{
			}
		}
		return false;
	}

	public void keyTick(InputEvent.Key event)
	{
//		System.out.println("Pressing the key handlers");
		for (int i = 0; i < KeyBindings.values().length; i++)
		{
			KeyBindings keyBindings = KeyBindings.values()[i];
			KeyMapping keyBinding = keyBindings.getKey();
			boolean state = keyBinding.isDown();
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
