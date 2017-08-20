package WayofTime.bloodmagic.core;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.soul.EnumDemonWillType;
import WayofTime.bloodmagic.item.ItemDemonCrystal;
import WayofTime.bloodmagic.item.soul.ItemSoulGem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.ShapelessOreRecipe;

@Mod.EventBusSubscriber(modid = BloodMagic.MODID)
public class RegistrarBloodMagicRecipes {

    @SubscribeEvent
    public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        for (int i = 0; i < ItemSoulGem.names.length; i++) {
            for (int j = 0; j < ItemDemonCrystal.NAMES.size(); j++) {
                ItemStack baseGemStack = new ItemStack(RegistrarBloodMagicItems.SOUL_GEM, 1, i);
                ItemStack newGemStack = new ItemStack(RegistrarBloodMagicItems.SOUL_GEM, 1, i);

                ItemStack crystalStack = new ItemStack(RegistrarBloodMagicItems.ITEM_DEMON_CRYSTAL, 1, j);

                EnumDemonWillType willType = ((ItemDemonCrystal) RegistrarBloodMagicItems.ITEM_DEMON_CRYSTAL).getType(crystalStack);
                ((ItemSoulGem) RegistrarBloodMagicItems.SOUL_GEM).setCurrentType(willType, newGemStack);
                ShapelessOreRecipe shapeless  = new ShapelessOreRecipe(new ResourceLocation(BloodMagic.MODID, "soul_gem"), newGemStack, baseGemStack, crystalStack);
                event.getRegistry().register(shapeless.setRegistryName("soul_gem_" + willType.getName()));
            }
        }
    }
}
