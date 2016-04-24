package WayofTime.bloodmagic.registry;

import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import WayofTime.bloodmagic.api.util.helper.PlayerSacrificeHelper;
import WayofTime.bloodmagic.potion.PotionBloodMagic;
import WayofTime.bloodmagic.potion.PotionEventHandlers;
import net.minecraftforge.fml.common.registry.GameRegistry;

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
        boost = registerPotion("Boost", new ResourceLocation("boost"), false, 0xFFFFFF, 0, 0);
        // new ResourceLocation(resourceLocation +
        // boost.getName().toLowerCase())

        whirlwind = registerPotion("Whirlwind", new ResourceLocation("whirlwind"), false, 0, 0, 0);
        planarBinding = registerPotion("Planar Binding", new ResourceLocation("planarBinding"), false, 0, 0, 0);
        soulSnare = registerPotion("Soul Snare", new ResourceLocation("soulSnare"), false, 0xFFFFFF, 0, 0);
        soulFray = registerPotion("Soul Fray", new ResourceLocation("soulFray"), true, 0xFFFFFF, 0, 0);
        PlayerSacrificeHelper.soulFrayId = soulFray;
        // heavyHeart = new PotionBloodMagic("Heavy Heart", new
        // ResourceLocation(resourceLocation +
        // heavyHeart.getName().toLowerCase()), true, 0, 0, 0);
    }

    protected static Potion registerPotion(String name, ResourceLocation location, boolean badEffect, int potionColour, int x, int y)
    {
        Potion potion = new PotionBloodMagic(name, location, badEffect, potionColour, x, y);
        GameRegistry.register(potion.setRegistryName(location));
        return potion;
    }
}
