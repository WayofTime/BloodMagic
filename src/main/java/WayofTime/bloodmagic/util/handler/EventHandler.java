package WayofTime.bloodmagic.util.handler;

import WayofTime.bloodmagic.api.util.helper.PlayerHelper;
import WayofTime.bloodmagic.item.gear.ItemPackSacrifice;
import WayofTime.bloodmagic.registry.ModBlocks;
import WayofTime.bloodmagic.registry.ModItems;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventHandler {

    @SubscribeEvent
    public void onEntityDeath(LivingHurtEvent event) {

        int chestIndex = 2;

        if (event.source.getEntity() instanceof EntityPlayer && !PlayerHelper.isFakePlayer((EntityPlayer) event.source.getEntity())) {
            EntityPlayer player = (EntityPlayer) event.source.getEntity();

            if (player.getCurrentArmor(chestIndex) != null && player.getCurrentArmor(chestIndex).getItem() instanceof ItemPackSacrifice) {
                ItemPackSacrifice pack = (ItemPackSacrifice) player.getCurrentArmor(chestIndex).getItem();

                boolean shouldSyphon = pack.getStoredLP(player.getCurrentArmor(chestIndex)) < pack.CAPACITY;
                int totalLP = Math.round(event.ammount * pack.CONVERSION);

                if (shouldSyphon)
                    pack.addLP(player.getCurrentArmor(chestIndex), totalLP);
            }
        }
    }

    @SubscribeEvent
    public void onBucketFill(FillBucketEvent event) {
        if (event.current.getItem() != Items.bucket)
            return;

        ItemStack result = null;

        Block block = event.world.getBlockState(event.target.getBlockPos()).getBlock();

        if (block != null && (block.equals(ModBlocks.lifeEssence)) && block.getMetaFromState(event.world.getBlockState(event.target.getBlockPos())) == 0) {
            event.world.setBlockToAir(event.target.getBlockPos());
            result = new ItemStack(ModItems.bucketEssence);
        }

        if (result == null)
            return;

        event.result = result;
        event.setResult(Event.Result.ALLOW);
    }
}
