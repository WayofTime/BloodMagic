package wayoftime.bloodmagic.client.hud;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLPaths;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.client.hud.element.HUDElement;

import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Mod.EventBusSubscriber(modid = BloodMagic.MODID, value = Dist.CLIENT)
public class ElementRegistry
{

//	private static final File CORNFIG = new File(Loader.instance().getConfigDir(), BloodMagic.MODID + "/hud_elements.json");
	private static final File CONFIG = FMLPaths.CONFIGDIR.get().resolve(BloodMagic.MODID).resolve("hud_elements.json").toFile();

	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final Map<ResourceLocation, HUDElement> HUD_ELEMENTS = Maps.newLinkedHashMap();
	private static final Map<HUDElement, ResourceLocation> REVERSE = Maps.newHashMap();
	private static final Map<ResourceLocation, ElementInfo> ELEMENT_INFO = Maps.newHashMap();

	public static void registerHandler(ResourceLocation key, HUDElement element, Vec2 defaultPosition)
	{
		HUD_ELEMENTS.put(key, element);
		REVERSE.put(element, key);

		ELEMENT_INFO.put(key, new ElementInfo(defaultPosition, getRandomColor()));
	}

	public static void resetPos()
	{
		ELEMENT_INFO.values().forEach(ElementInfo::resetPosition);
	}

	public static List<HUDElement> getElements()
	{
		return ImmutableList.copyOf(HUD_ELEMENTS.values());
	}

	public static ResourceLocation getKey(HUDElement element)
	{
		return REVERSE.get(element);
	}

	public static int getColor(ResourceLocation element)
	{
		return ELEMENT_INFO.getOrDefault(element, ElementInfo.DUMMY).getBoxColor();
	}

	public static Vec2 getPosition(ResourceLocation element)
	{
		return ELEMENT_INFO.get(element).getPosition();
	}

	public static void setPosition(ResourceLocation element, Vec2 point)
	{
		ELEMENT_INFO.compute(element, (resourceLocation, elementInfo) -> {
			if (elementInfo == null)
				return new ElementInfo(point, getRandomColor());

			elementInfo.setPosition(point);
			return elementInfo;
		});
	}

	public static void save(Map<ResourceLocation, Vec2> newLocations)
	{
		newLocations.forEach((k, v) -> {
			ElementInfo info = ELEMENT_INFO.get(k);
			if (info != null)
				info.setPosition(v);
		});

		Map<String, Vec2> toWrite = Maps.newHashMap();
		for (Map.Entry<ResourceLocation, ElementInfo> entry : ELEMENT_INFO.entrySet())
			toWrite.put(entry.getKey().toString(), entry.getValue().getPosition());

		String json = GSON.toJson(toWrite);
		try (FileWriter writer = new FileWriter(CONFIG))
		{
			writer.write(json);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void readConfig()
	{
		if (!CONFIG.exists())
			return;

		try (FileReader reader = new FileReader(CONFIG))
		{
			Map<String, Vec2> toLoad = GSON.fromJson(reader, new TypeToken<Map<String, Vec2>>()
			{
			}.getType());
			for (Map.Entry<String, Vec2> entry : toLoad.entrySet())
			{
				ElementInfo info = ELEMENT_INFO.get(new ResourceLocation(entry.getKey()));
				if (info != null)
					info.setPosition(entry.getValue());
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void onRenderOverlay(RenderGuiOverlayEvent.Pre event)
	{
		if (event.getOverlay() == VanillaGuiOverlay.CHAT_PANEL.type())
		{
			Window window = event.getWindow();

			for (HUDElement element : HUD_ELEMENTS.values())
			{
				if (!element.shouldRender(Minecraft.getInstance()))
					continue;

				Vec2 position = ELEMENT_INFO.get(getKey(element)).getPosition();
				int xPos = (int) (window.getGuiScaledWidth() * position.x);
				int yPos = (int) (window.getGuiScaledHeight() * position.y);
				element.draw(event.getGuiGraphics(), event.getPartialTick(), xPos, yPos);
			}
		}
	}

	public static int getRandomColor()
	{
		Random rand = new Random();
		float r = rand.nextFloat() / 2F + 0.5F;
		float g = rand.nextFloat() / 2F + 0.5F;
		float b = rand.nextFloat() / 2F + 0.5F;
		float a = 0.5F;
		return new Color(r, g, b, a).getRGB();
	}
}