package WayofTime.bloodmagic.entity.projectile;

import WayofTime.bloodmagic.util.Constants;
import WayofTime.bloodmagic.soul.EnumDemonWillType;
import WayofTime.bloodmagic.soul.PlayerDemonWillHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

import java.util.Locale;

public class EntitySentientArrow extends EntityTippedArrow {
    public double reimbursedAmountOnHit = 0;
    public EnumDemonWillType type = EnumDemonWillType.DEFAULT;

    public EntitySentientArrow(World worldIn) {
        super(worldIn);
    }

    public EntitySentientArrow(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    public EntitySentientArrow(World worldIn, EntityLivingBase shooter, EnumDemonWillType type, double reinburseAmount) {
        super(worldIn, shooter);
        this.reimbursedAmountOnHit = reinburseAmount;
        this.type = type;
    }

    public void reimbursePlayer(EntityLivingBase hitEntity, float damage) {
        if (this.shootingEntity instanceof EntityPlayer) {
            if (hitEntity.getEntityWorld().getDifficulty() != EnumDifficulty.PEACEFUL && !(hitEntity instanceof IMob)) {
                return;
            }

            PlayerDemonWillHandler.addDemonWill(type, (EntityPlayer) this.shootingEntity, reimbursedAmountOnHit * damage / 20f);
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tag) {
        super.writeEntityToNBT(tag);

        tag.setDouble("reimbursement", reimbursedAmountOnHit);
        tag.setString(Constants.NBT.WILL_TYPE, type.toString());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tag) {
        super.readEntityFromNBT(tag);

        reimbursedAmountOnHit = tag.getDouble("reimbursement");
        type = EnumDemonWillType.valueOf(tag.getString(Constants.NBT.WILL_TYPE).toUpperCase(Locale.ENGLISH));
    }

    @Override
    protected ItemStack getArrowStack() {
        return new ItemStack(Items.ARROW);
    }
}
