package wayoftime.bloodmagic.common.item;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import wayoftime.bloodmagic.BloodMagic;

import java.util.List;

public class BMMaterialsAndTiers {
    public static final DeferredRegister<ArmorMaterial> ARMOUR_MATERIALS = DeferredRegister.create(BuiltInRegistries.ARMOR_MATERIAL, BloodMagic.MODID);

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> LIVING_ARMOUR_MATERIAL = ARMOUR_MATERIALS.register("living", () -> new ArmorMaterial(
            ArmorMaterials.IRON.value().defense(), ArmorMaterials.IRON.value().enchantmentValue(),
            ArmorMaterials.IRON.value().equipSound(), () -> Ingredient.of(BMItems.RAW_WILL.get()),
            List.of(new ArmorMaterial.Layer(bm("living"))), 0, 0
    ));

    private static ResourceLocation bm(String path) {
        return ResourceLocation.fromNamespaceAndPath(BloodMagic.MODID, path);
    }

    public static void register(IEventBus modBus) {
        ARMOUR_MATERIALS.register(modBus);
    }
}
