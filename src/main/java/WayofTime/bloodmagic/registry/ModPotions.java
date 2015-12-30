package WayofTime.bloodmagic.registry;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import WayofTime.bloodmagic.potion.PotionBloodMagic;
import WayofTime.bloodmagic.potion.PotionEventHandlers;

public class ModPotions
{

    public static Potion drowning;
    public static Potion boost;
    public static Potion heavyHeart;
    public static Potion whirlwind;
    public static Potion planarBinding;

    public static void init()
    {
        if (Potion.potionTypes.length < 256)
            extendPortionArray();

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
        // heavyHeart = new PotionBloodMagic("Heavy Heart", new
        // ResourceLocation(resourceLocation +
        // heavyHeart.getName().toLowerCase()), true, 0, 0, 0);
    }

    public static void extendPortionArray()
    {
        Potion[] potionTypes;

        for (Field f : Potion.class.getDeclaredFields())
        {
            f.setAccessible(true);
            try
            {
                if (f.getName().equals("potionTypes") || f.getName().equals("field_76425_a"))
                {
                    Field field = Field.class.getDeclaredField("modifiers");
                    field.setAccessible(true);
                    field.setInt(f, f.getModifiers() & ~Modifier.FINAL);

                    potionTypes = (Potion[]) f.get(null);
                    final Potion[] newPotionTypes = new Potion[256];
                    System.arraycopy(potionTypes, 0, newPotionTypes, 0, potionTypes.length);
                    f.set(null, newPotionTypes);
                }
            } catch (Exception e)
            {
                System.err.println(e);
            }
        }
    }
}
