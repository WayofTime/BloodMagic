package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.ice;

import WayofTime.alchemicalWizardry.api.spell.ProjectileImpactEffect;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class ProjectileDefaultIce extends ProjectileImpactEffect
{
    public ProjectileDefaultIce(int power, int potency, int cost)
    {
        super(power, potency, cost);
    }

    @Override
    public void onEntityImpact(Entity mop, Entity proj)
    {

    }

    @Override
    public void onTileImpact(World world, MovingObjectPosition mop)
    {
        ForgeDirection sideHit = ForgeDirection.getOrientation(mop.sideHit);

        int posX = mop.blockX + sideHit.offsetX;
        int posY = mop.blockY + sideHit.offsetY;
        int posZ = mop.blockZ + sideHit.offsetZ;

        if (world.isAirBlock(posX, posY, posZ))
        {
            world.setBlock(posX, posY, posZ, Blocks.ice);
        }
    }
}
