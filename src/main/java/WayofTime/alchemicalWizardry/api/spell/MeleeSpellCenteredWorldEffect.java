package WayofTime.alchemicalWizardry.api.spell;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
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
        
        int x = MathHelper.floor_double(entityPlayer.posX + lookVec.xCoord * range);
        int y = MathHelper.floor_double(entityPlayer.posY + entityPlayer.getEyeHeight() + lookVec.yCoord * range);
        int z = MathHelper.floor_double(entityPlayer.posZ + lookVec.zCoord * range);

        this.onCenteredWorldEffect(entityPlayer, world, new BlockPos(x, y, z));
    }

    public void setRange(float range)
    {
        this.range = range;
    }

    public abstract void onCenteredWorldEffect(EntityPlayer player, World world, BlockPos pos);
}
