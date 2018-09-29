package com.wayoftime.bloodmagic.core.will;

import com.wayoftime.bloodmagic.BloodMagic;
import com.wayoftime.bloodmagic.core.RegistrarBloodMagicItems;
import com.wayoftime.bloodmagic.core.util.InventoryUtil;
import com.wayoftime.bloodmagic.event.DemonicWillEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.EnumDifficulty;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import java.util.function.Predicate;

@Mod.EventBusSubscriber(modid = BloodMagic.MODID)
public class DemonicWillEventHandler {

    @SubscribeEvent
    public static void gatherDrops(LivingDropsEvent event) {
        if (!(event.getEntityLiving() instanceof IMob) && event.getEntityLiving().getEntityWorld().getDifficulty() != EnumDifficulty.PEACEFUL)
            return;

        Entity sourceEntity = event.getSource().getTrueSource();
        if (!(sourceEntity instanceof EntityPlayer) || sourceEntity instanceof FakePlayer)
            return;

        EntityPlayer player = (EntityPlayer) sourceEntity;
        ItemStack held = player.getHeldItem(EnumHand.MAIN_HAND);
        if (held.isEmpty() || !(held.getItem() instanceof ISentientEquipment))
            return;

        DemonWillHolder willDrop = ((ISentientEquipment) held.getItem()).getWillDrop(player, event.getEntityLiving(), held, event.getLootingLevel());
        if (willDrop == null)
            return;

        DemonicWillEvent.GatherWillDrops willDropEvent = new DemonicWillEvent.GatherWillDrops(player, event.getEntityLiving(), held, event.getLootingLevel(), willDrop.getAmount());
        if (MinecraftForge.EVENT_BUS.post(willDropEvent))
            return;

        willDrop.setAmount(willDropEvent.getDroppedWill());
        IItemHandler inventory = player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        if (inventory == null)
            return; // shut up intellij this is never going to happen

        InventoryUtil.findMatchingItems(inventory, InventoryUtil.NO_EMPTY.and(new WillContainerFilter(willDrop.getType()))).forEach(stack -> {
            if (willDrop.getAmount() <= 0)
                return;

            IWillContainer container = (IWillContainer) stack.getItem();
            DemonWillHolder holder = container.getDemonWill(stack);
            if (holder == null)
                holder = new DemonWillHolder(willDrop.getType(), 0);

            holder.modifyAmount(Math.min(willDrop.getAmount(), container.getMaxContained(stack)));
            willDrop.modifyAmount(-holder.getAmount());
        });

        if (willDrop.getAmount() >= 0) {
            ItemStack drop = new ItemStack(RegistrarBloodMagicItems.MONSTER_SOUL);
            ((IWillContainer) drop.getItem()).applyDemonWill(drop, willDrop);
            event.getDrops().add(new EntityItem(player.getEntityWorld(), event.getEntity().posX, event.getEntity().posY, event.getEntity().posZ, drop));
        }
    }

    private static class WillContainerFilter implements Predicate<ItemStack> {

        private final DemonWill type;

        public WillContainerFilter(DemonWill type) {
            this.type = type;
        }

        @Override
        public boolean test(ItemStack stack) {
            if (stack.getItem() instanceof IWillContainer) {
                IWillContainer container = (IWillContainer) stack.getItem();
                double max = container.getMaxContained(stack);
                DemonWillHolder holder = container.getDemonWill(stack);
                if (holder == null)
                    return true;

                if (holder.getType() != type)
                    return false;

                return holder.getAmount() < max;
            }

            return false;
        }
    }
}
