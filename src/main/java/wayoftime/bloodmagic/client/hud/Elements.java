package wayoftime.bloodmagic.client.hud;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector2f;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.ConfigManager;
import wayoftime.bloodmagic.client.hud.element.ElementDemonAura;

public class Elements
{
	public static void registerElements()
	{
		ElementRegistry.registerHandler(new ResourceLocation(BloodMagic.MODID, "demon_will_aura"), new ElementDemonAura(), new Vector2f(ConfigManager.CLIENT.demonWillGaugeX.get().floatValue(), ConfigManager.CLIENT.demonWillGaugeY.get().floatValue()));
	}
}
