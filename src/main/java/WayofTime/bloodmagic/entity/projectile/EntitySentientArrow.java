package WayofTime.bloodmagic.entity.projectile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import WayofTime.bloodmagic.api.soul.EnumDemonWillType;
import WayofTime.bloodmagic.api.soul.PlayerDemonWillHandler;

public class EntitySentientArrow extends EntityArrow
{
    public double reimbursedAmountOnHit = 0;

    public EntitySentientArrow(World worldIn)
    {
        super(worldIn);
    }

    public EntitySentientArrow(World worldIn, double x, double y, double z)
    {
        super(worldIn, x, y, z);
    }

    public EntitySentientArrow(World worldIn, EntityLivingBase shooter, EntityLivingBase p_i1755_3_, float p_i1755_4_, float p_i1755_5_)
    {
        super(worldIn, shooter, p_i1755_3_, p_i1755_4_, p_i1755_5_);

    }

    public EntitySentientArrow(World worldIn, EntityLivingBase shooter, float velocity, double reimbursement)
    {
        super(worldIn, shooter, velocity);
        this.reimbursedAmountOnHit = reimbursement;
    }

    public void reimbursePlayer()
    {
        if (this.shootingEntity instanceof EntityPlayer)
        {
            PlayerDemonWillHandler.addDemonWill(EnumDemonWillType.DEFAULT, (EntityPlayer) this.shootingEntity, reimbursedAmountOnHit);
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tag)
    {
        super.writeEntityToNBT(tag);

        tag.setDouble("reimbursement", reimbursedAmountOnHit);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tag)
    {
        super.readEntityFromNBT(tag);

        reimbursedAmountOnHit = tag.getDouble("reimbursement");
    }
}
