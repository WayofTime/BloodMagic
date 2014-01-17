package WayofTime.alchemicalWizardry.common;

import WayofTime.alchemicalWizardry.ModItems;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingDropsEvent;

public class ModLivingDropsEvent
{
    public static double rand;

    @ForgeSubscribe
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
                            event.entityLiving.dropItem(ModItems.weakBloodShard.itemID, 1);
                        }
                }
            }
        }
    }
}