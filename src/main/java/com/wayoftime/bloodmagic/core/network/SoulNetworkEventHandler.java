package com.wayoftime.bloodmagic.core.network;

import com.wayoftime.bloodmagic.BloodMagic;
import com.wayoftime.bloodmagic.event.BindingEvent;
import com.wayoftime.bloodmagic.item.IBindable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = BloodMagic.MODID)
public class SoulNetworkEventHandler {

    @SubscribeEvent
    public static void onItemUse(PlayerInteractEvent.RightClickItem event) {
        if (event.getWorld().isRemote)
            return;

        EntityPlayer player = event.getEntityPlayer();
        ItemStack clicked = event.getItemStack();
        if (player instanceof FakePlayer)
            return;

        if (clicked.getItem() instanceof IBindable) {
            IBindable bindable = (IBindable) clicked.getItem();
            Binding binding = bindable.getBinding(clicked);
            if (binding == null && bindable.onBind(player, clicked)) {
                binding = new Binding(player);
                BindingEvent bindingEvent = new BindingEvent(clicked, binding, player);
                if (!MinecraftForge.EVENT_BUS.post(bindingEvent))
                    IBindable.applyBinding(clicked, binding);
            }
        }

        if (clicked.getItem() instanceof IBloodOrb) {
            BloodOrb orb = ((IBloodOrb) clicked.getItem()).getOrb(clicked);
            if (orb == null)
                return;

            SoulNetwork network = SoulNetwork.get(player.getGameProfile().getId());
            if (orb.getTier() > network.getTier())
                network.setTier(orb.getTier(), orb.getCapacity());
        }
    }
}
