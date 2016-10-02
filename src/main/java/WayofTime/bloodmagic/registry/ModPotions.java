package WayofTime.bloodmagic.registry;

import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
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
    public static Potion fireFuse;
    public static Potion constrict;
    public static Potion plantLeech;
    public static Potion deafness;

    public static Potion immuneSuppress;

    public static void init()
    {
        new PotionEventHandlers();

        // TODO FUTURE MAKE POTION TEXTURES

        // final String resourceLocation = Constants.Mod.MODID +
        // ":textures/potions/";

        // drowning = new PotionBloodMagic("Drowning", new
        // ResourceLocation(resourceLocation +
        // drowning.getName().toLowerCase()), true, 0, 0, 0);
        boost = registerPotion("Boost", new ResourceLocation("boost"), false, 0xFFFFFF, 0, 1);
        // new ResourceLocation(resourceLocation +
        // boost.getName().toLowerCase())

        whirlwind = registerPotion("Whirlwind", new ResourceLocation("whirlwind"), false, 0, 1, 0);
        planarBinding = registerPotion("Planar Binding", new ResourceLocation("planarBinding"), false, 0, 2, 0);
        soulSnare = registerPotion("Soul Snare", new ResourceLocation("soulSnare"), false, 0xFFFFFF, 3, 0);
        soulFray = registerPotion("Soul Fray", new ResourceLocation("soulFray"), true, 0xFFFFFF, 4, 0);
        PlayerSacrificeHelper.soulFrayId = soulFray;

        fireFuse = registerPotion("Fire Fuse", new ResourceLocation("fireFuse"), true, 0xFF3333, 5, 0);
        constrict = registerPotion("Constriction", new ResourceLocation("constrict"), true, 0x000000, 6, 0);
        plantLeech = registerPotion("Plant Leech", new ResourceLocation("plantLeech"), true, 0x000000, 7, 0);
        deafness = registerPotion("Deaf", new ResourceLocation("deafness"), true, 0x000000, 0, 1);
        immuneSuppress = registerPotion("(-) Immunity", new ResourceLocation("immuneSuppress"), true, 0x000000, 1, 1);
        // heavyHeart = new PotionBloodMagic("Heavy Heart", new
        // ResourceLocation(resourceLocation +
        // heavyHeart.getName().toLowerCase()), true, 0, 0, 0);
    }

    protected static Potion registerPotion(String name, ResourceLocation location, boolean badEffect, int potionColour, int x, int y)
    {
        Potion potion = new PotionBloodMagic(name, location, badEffect, potionColour, x, y);
        GameRegistry.register(potion.setRegistryName(location.getResourcePath()));
        return potion;
    }
}
