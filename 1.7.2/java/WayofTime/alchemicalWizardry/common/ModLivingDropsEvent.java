package WayofTime.alchemicalWizardry.common;

import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import WayofTime.alchemicalWizardry.ModItems;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class ModLivingDropsEvent
{
    public static double rand;

    @SubscribeEvent
    public void onEntityDrop(LivingDropsEvent event)
    {
        if (event.source.getDamageType().equals("player"))
        {
            rand = Math.random();

            if (!(event.entityLiving instanceof EntityAnimal))
            {
                PotionEffect effect = event.entityLiving.getActivePotionEffect(Potion.weakness);

                if (effect != null)
                {
                    if (effect.getAmplifier() >= 2)
                        if (rand < 0.50d)
                        {
                            event.entityLiving.dropItem(ModItems.weakBloodShard, 1);
                        }
                }
            }
        }
    }
}