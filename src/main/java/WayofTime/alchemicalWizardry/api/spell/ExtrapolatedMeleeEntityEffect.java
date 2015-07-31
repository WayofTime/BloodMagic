package WayofTime.alchemicalWizardry.api.spell;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.util.List;

public abstract class ExtrapolatedMeleeEntityEffect implements IMeleeSpellEntityEffect
{
    protected float range;
    protected float radius;
    protected int powerUpgrades;
    protected int potencyUpgrades;
    protected int costUpgrades;
    protected int maxHit;

    public ExtrapolatedMeleeEntityEffect(int power, int potency, int cost)
    {
        this.powerUpgrades = power;
        this.potencyUpgrades = potency;
        this.costUpgrades = cost;
        this.range = 0;
        this.radius = 0;
        this.maxHit = 1;
    }

    @Override
    public void onEntityImpact(World world, EntityPlayer entityPlayer)
    {
        Vec3 lookVec = entityPlayer.getLook(range);
        double x = entityPlayer.posX + lookVec.xCoord;
        double y = entityPlayer.posY + entityPlayer.getEyeHeight() + lookVec.yCoord;
        double z = entityPlayer.posZ + lookVec.zCoord;

        List<Entity> entities = world.getEntitiesWithinAABB(Entity.class, AxisAlignedBB.getBoundingBox(x - 0.5f, y - 0.5f, z - 0.5f, x + 0.5f, y + 0.5f, z + 0.5f).expand(radius, radius, radius));
        int hit = 0;

        if (entities != null)
        {
            for (Entity entity : entities)
            {
                if (hit < maxHit && !entity.equals(entityPlayer))
                {
                    if (this.entityEffect(world, entity, entityPlayer))
                    {
                        hit++;
                    }
                }
            }
        }
    }

    protected abstract boolean entityEffect(World world, Entity entity, EntityPlayer player);

    public void setRange(float range)
    {
        this.range = range;
    }

    public void setRadius(float radius)
    {
        this.radius = radius;
    }

    public void setMaxNumberHit(int maxHit)
    {
        this.maxHit = maxHit;
    }
}
