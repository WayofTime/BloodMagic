package wayoftime.bloodmagic.common.event;

import com.mojang.authlib.GameProfile;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.datacomponent.BMDataComponents;
import wayoftime.bloodmagic.common.datacomponent.Binding;

import java.util.Objects;

@EventBusSubscriber(modid = BloodMagic.MODID)
public class CommonEventHandler {

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onInteract(PlayerInteractEvent.RightClickItem event) {
        Player player = event.getEntity();

        if (player instanceof FakePlayer)
            return;

        ItemStack held = event.getItemStack();
        if (held.isEmpty()) {
            return;
        }

        Binding binding = held.get(BMDataComponents.BINDING);
        if (binding == null) {
            BloodMagic.LOGGER.info("binding was null");
            return;
        }
        BloodMagic.LOGGER.info("binding: {}:{}", binding.name(), binding.uuid());
        GameProfile profile = event.getEntity().getGameProfile();
        if (binding.isEmpty()) {
            binding = new Binding(profile.getId(), profile.getName());
            if (NeoForge.EVENT_BUS.post(new ItemBindEvent(event.getEntity(), held)).isCanceled()) {
                return;
            }
            held.set(BMDataComponents.BINDING, binding);
        } else if (binding.uuid() == profile.getId() && !Objects.equals(binding.name(), profile.getName())) {
            binding = new Binding(profile.getId(), profile.getName());
            held.set(BMDataComponents.BINDING, binding);
        }
    }
}
