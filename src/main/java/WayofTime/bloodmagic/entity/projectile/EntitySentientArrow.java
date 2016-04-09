package WayofTime.bloodmagic.entity.projectile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.soul.EnumDemonWillType;
import WayofTime.bloodmagic.api.soul.PlayerDemonWillHandler;

public class EntitySentientArrow extends EntityTippedArrow
{
    public double reimbursedAmountOnHit = 0;
    public EnumDemonWillType type = EnumDemonWillType.DEFAULT;

    public EntitySentientArrow(World worldIn)
    {
        super(worldIn);
    }

    public EntitySentientArrow(World worldIn, double x, double y, double z)
    {
        super(worldIn, x, y, z);
    }

    public EntitySentientArrow(World worldIn, EntityLivingBase shooter, EnumDemonWillType type)
    {
        super(worldIn, shooter);
        this.reimbursedAmountOnHit = 0;
        this.type = type;
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
        tag.setString(Constants.NBT.WILL_TYPE, type.toString());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tag)
    {
        super.readEntityFromNBT(tag);

        reimbursedAmountOnHit = tag.getDouble("reimbursement");
        type = EnumDemonWillType.valueOf(tag.getString(Constants.NBT.WILL_TYPE));
    }

    @Override
    protected ItemStack getArrowStack()
    {
        return new ItemStack(Items.arrow);
    }
}
