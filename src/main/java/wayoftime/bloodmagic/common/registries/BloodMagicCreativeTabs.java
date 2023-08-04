package wayoftime.bloodmagic.common.registries;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;

import java.util.Map;

public class BloodMagicCreativeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, BloodMagic.MODID);
    public static final RegistryObject<CreativeModeTab> TAB = CREATIVE_TABS.register("bloodmagictab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.bloodmagictab"))
            .icon(() -> BloodMagicBlocks.BLOOD_ALTAR.get().asItem().getDefaultInstance())
            .displayItems((params, output) -> {
                for (Map.Entry<ResourceKey<Item>, Item> entry : ForgeRegistries.ITEMS.getEntries()) {
                    if (entry.getKey().location().getNamespace().equals(BloodMagic.MODID)) {
                        output.accept(entry.getValue().getDefaultInstance());
                    }
                }
            })
            .build());
}
