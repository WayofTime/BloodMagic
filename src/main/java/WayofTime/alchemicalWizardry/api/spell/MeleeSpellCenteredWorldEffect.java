package WayofTime.alchemicalWizardry.api.spell;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public abstract class MeleeSpellCenteredWorldEffect extends MeleeSpellWorldEffect
{
    protected float range;

    public MeleeSpellCenteredWorldEffect(int power, int potency, int cost)
    {
        super(power, potency, cost);
    }

    @Override
    public void onWorldEffect(World world, EntityPlayer entityPlayer)
    {
        Vec3 lookVec = entityPlayer.getLook(range).normalize();
        int x = (int) (entityPlayer.posX + lookVec.xCoord * range);
        int y = (int) (entityPlayer.posY + entityPlayer.getEyeHeight() + lookVec.yCoord * range);
        int z = (int) (entityPlayer.posZ + lookVec.zCoord * range);

        this.onCenteredWorldEffect(entityPlayer, world, x, y, z);
    }

    public void setRange(float range)
    {
        this.range = range;
    }

    public abstract void onCenteredWorldEffect(EntityPlayer player, World world, int posX, int posY, int posZ);
}
