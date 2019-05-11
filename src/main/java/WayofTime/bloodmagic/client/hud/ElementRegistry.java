package WayofTime.bloodmagic.client.hud;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.client.hud.element.HUDElement;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.vecmath.Vector2f;
import java.awt.Color;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.BiFunction;

@Mod.EventBusSubscriber(modid = BloodMagic.MODID, value = Side.CLIENT)
public class ElementRegistry {

    private static final File CONFIG = new File(Loader.instance().getConfigDir(), BloodMagic.MODID + "/hud_elements.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Map<ResourceLocation, HUDElement> HUD_ELEMENTS = Maps.newLinkedHashMap();
    private static final Map<HUDElement, ResourceLocation> REVERSE = Maps.newHashMap();
    private static final Map<ResourceLocation, ElementInfo> ELEMENT_INFO = Maps.newHashMap();

    public static void registerHandler(ResourceLocation key, HUDElement element, Vector2f defaultPosition) {
        HUD_ELEMENTS.put(key, element);
        REVERSE.put(element, key);


        ELEMENT_INFO.put(key, new ElementInfo(defaultPosition, getRandomColor()));
    }

    public static void resetPos() {
        ELEMENT_INFO.values().forEach(ElementInfo::resetPosition);
    }

    public static List<HUDElement> getElements() {
        return ImmutableList.copyOf(HUD_ELEMENTS.values());
    }

    public static ResourceLocation getKey(HUDElement element) {
        return REVERSE.get(element);
    }

    public static int getColor(ResourceLocation element) {
        return ELEMENT_INFO.getOrDefault(element, ElementInfo.DUMMY).getBoxColor();
    }

    public static Vector2f getPosition(ResourceLocation element) {
        return ELEMENT_INFO.get(element).getPosition();
    }

    public static void setPosition(ResourceLocation element, Vector2f point) {
        ELEMENT_INFO.compute(element, (resourceLocation, elementInfo) -> {
            if (elementInfo == null)
                return new ElementInfo(point, getRandomColor());

            elementInfo.setPosition(point);
            return elementInfo;
        });
    }

    public static void save(Map<ResourceLocation, Vector2f> newLocations) {
        newLocations.forEach((k, v) -> {
            ElementInfo info = ELEMENT_INFO.get(k);
            if (info != null)
                info.setPosition(v);
        });

        Map<String, Vector2f> toWrite = Maps.newHashMap();
        for (Map.Entry<ResourceLocation, ElementInfo> entry : ELEMENT_INFO.entrySet())
            toWrite.put(entry.getKey().toString(), entry.getValue().getPosition());

        String json = GSON.toJson(toWrite);
        try (FileWriter writer = new FileWriter(CONFIG)) {
            writer.write(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void readConfig() {
        if (!CONFIG.exists())
            return;

        try (FileReader reader = new FileReader(CONFIG)) {
            Map<String, Vector2f> toLoad = GSON.fromJson(reader, new TypeToken<Map<String, Vector2f>>() {
            }.getType());
            for (Map.Entry<String, Vector2f> entry : toLoad.entrySet()) {
                ElementInfo info = ELEMENT_INFO.get(new ResourceLocation(entry.getKey()));
                if (info != null)
                    info.setPosition(entry.getValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void onRenderOverlay(RenderGameOverlayEvent.Pre event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR) {
            ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());

            for (HUDElement element : HUD_ELEMENTS.values()) {
                if (!element.shouldRender(Minecraft.getMinecraft()))
                    continue;

                Vector2f position = ELEMENT_INFO.get(getKey(element)).getPosition();
                int xPos = (int) (resolution.getScaledWidth_double() * position.x);
                if (xPos - element.getWidth() < 0)
                    xPos *= 2;
                if (xPos + element.getWidth() > resolution.getScaledWidth())
                    xPos -= element.getWidth();

                int yPos = (int) (resolution.getScaledHeight_double() * position.y);
                if (yPos - element.getHeight() < 0)
                    yPos *= 2;
                if (yPos + element.getHeight() > resolution.getScaledHeight())
                    yPos -= element.getHeight();

                element.draw(event.getResolution(), event.getPartialTicks(), xPos, yPos);
            }
        }
    }

    public static int getRandomColor() {
        Random rand = new Random();
        float r = rand.nextFloat() / 2F + 0.5F;
        float g = rand.nextFloat() / 2F + 0.5F;
        float b = rand.nextFloat() / 2F + 0.5F;
        float a = 0.5F;
        return new Color(r, g, b, a).getRGB();
    }
}
