package WayofTime.bloodmagic.client.helper;

import WayofTime.bloodmagic.api.Constants;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 *  Courtesy of williewillus <a href="https://github.com/williewillus/Botania/blob/MC18/"></a>
 */
public class MiscIcons
{
    public static final MiscIcons INSTANCE = new MiscIcons();

    public TextureAtlasSprite
            ritualStoneBlankIcon,
            ritualStoneWaterIcon,
            ritualStoneFireIcon,
            ritualStoneEarthIcon,
            ritualStoneAirIcon,
            ritualStoneDawnIcon,
            ritualStoneDuskIcon;

    private MiscIcons() {}

    @SubscribeEvent
    public void onTextureStitch(TextureStitchEvent.Pre event)
    {
        final String BLOCKS = "blocks";

        ritualStoneBlankIcon = forName(event.map, "RitualStone", BLOCKS);
        ritualStoneWaterIcon = forName(event.map, "WaterRitualStone", BLOCKS);
        ritualStoneFireIcon = forName(event.map, "FireRitualStone", BLOCKS);
        ritualStoneEarthIcon = forName(event.map, "EarthRitualStone", BLOCKS);
        ritualStoneAirIcon = forName(event.map, "AirRitualStone", BLOCKS);
        ritualStoneDawnIcon = forName(event.map, "LightRitualStone", BLOCKS);
        ritualStoneDuskIcon = forName(event.map, "DuskRitualStone", BLOCKS);
    }

    public static TextureAtlasSprite forName(TextureMap textureMap, String name)
    {
        return textureMap.registerSprite(new ResourceLocation(Constants.Mod.DOMAIN + name));
    }

    public static TextureAtlasSprite forName(TextureMap textureMap, String name, String dir)
    {
        return textureMap.registerSprite(new ResourceLocation(Constants.Mod.DOMAIN + dir + "/" + name));
    }
}
