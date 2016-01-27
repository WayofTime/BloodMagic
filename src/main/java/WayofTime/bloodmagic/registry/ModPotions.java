package WayofTime.bloodmagic.registry;

import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import WayofTime.bloodmagic.api.util.helper.PlayerSacrificeHelper;
import WayofTime.bloodmagic.potion.PotionBloodMagic;
import WayofTime.bloodmagic.potion.PotionEventHandlers;

public class ModPotions
{
    public static Potion drowning;
    public static Potion boost;
    public static Potion heavyHeart;
    public static Potion whirlwind;
    public static Potion planarBinding;
    public static Potion soulSnare;
    public static Potion soulFray;

    public static void init()
    {
        new PotionEventHandlers();

        // TODO FUTURE MAKE POTION TEXTURES

        // final String resourceLocation = Constants.Mod.MODID +
        // ":textures/potions/";

        // drowning = new PotionBloodMagic("Drowning", new
        // ResourceLocation(resourceLocation +
        // drowning.getName().toLowerCase()), true, 0, 0, 0);
        boost = new PotionBloodMagic("Boost", new ResourceLocation("boost")
        // new ResourceLocation(resourceLocation +
        // boost.getName().toLowerCase())
                , false, 0, 0, 0);
        whirlwind = new PotionBloodMagic("Whirlwind", new ResourceLocation("whirlwind"), false, 0, 0, 0);
        planarBinding = new PotionBloodMagic("Planar Binding", new ResourceLocation("planarBinding"), false, 0, 0, 0);
        soulSnare = new PotionBloodMagic("Soul Snare", new ResourceLocation("soulSnare"), false, 0xFFFFFF, 0, 0);
        soulFray = new PotionBloodMagic("Soul Fray", new ResourceLocation("soulFray"), true, 0xFFFFFF, 0, 0);
        PlayerSacrificeHelper.soulFrayId = soulFray;
        // heavyHeart = new PotionBloodMagic("Heavy Heart", new
        // ResourceLocation(resourceLocation +
        // heavyHeart.getName().toLowerCase()), true, 0, 0, 0);
    }
}
