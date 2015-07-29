package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.wind;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.api.spell.MeleeSpellCenteredWorldEffect;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;

public class MeleeEnvironmentalWind extends MeleeSpellCenteredWorldEffect
{
    public MeleeEnvironmentalWind(int power, int potency, int cost)
    {
        super(power, potency, cost);
        this.setRange(5 * power + 5);
    }

    @Override
    public void onCenteredWorldEffect(EntityPlayer player, World world, BlockPos pos)
    {
        int radius = 5 * this.potencyUpgrades + 3;

        List<Entity> entities = SpellHelper.getEntitiesInRange(world, pos.getX(), pos.getY(), pos.getZ(), radius, radius);

        for (Entity entity : entities)
        {
            if (entity instanceof EntityItem)
            {
                ((EntityItem) entity).setPickupDelay(0);
                entity.onCollideWithPlayer((EntityPlayer) player);
            }
        }
    }
}
