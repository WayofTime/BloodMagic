package WayofTime.bloodmagic.entity.projectile;

import java.util.Locale;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import WayofTime.bloodmagic.soul.EnumDemonWillType;
import WayofTime.bloodmagic.soul.PlayerDemonWillHandler;
import WayofTime.bloodmagic.util.Constants;

public class EntitySentientArrow extends EntityTippedArrow
{
    public double reimbursedAmountOnHit = 0;
    public EnumDemonWillType type = EnumDemonWillType.DEFAULT;
    public int currentLevel = 0;
    public float[] destructiveExplosionRadius = { 0.5f, 1, 1.5f, 2, 2.5f, 3, 3.5f };
    public int[] poisonDuration = { 50, 100, 150, 80, 120, 160, 200 };
    public int[] poisonLevel = { 0, 0, 0, 1, 1, 1, 1 };
    public int[] levitationDuration = { 20, 40, 60, 80, 100, 120, 160 };
    public int[] levitationLevel = { 0, 0, 0, 1, 1, 1, 2 };
    public int[] slownessDuration = { 40, 60, 100, 150, 200, 250, 300 };
    public int[] slownessLevel = { 0, 0, 0, 1, 1, 1, 2 };

    public EntitySentientArrow(World worldIn)
    {
        super(worldIn);
    }

    public EntitySentientArrow(World worldIn, double x, double y, double z)
    {
        super(worldIn, x, y, z);
    }

    public EntitySentientArrow(World worldIn, EntityLivingBase shooter, EnumDemonWillType type, double reinburseAmount, int currentLevel)
    {
        super(worldIn, shooter);
        this.reimbursedAmountOnHit = reinburseAmount;
        this.type = type;
        this.currentLevel = currentLevel;
    }

    public void reimbursePlayer(EntityLivingBase hitEntity, float damage)
    {
        if (this.shootingEntity instanceof EntityPlayer)
        {
            if (hitEntity.getEntityWorld().getDifficulty() != EnumDifficulty.PEACEFUL && !(hitEntity instanceof IMob))
            {
                return;
            }

            PlayerDemonWillHandler.addDemonWill(type, (EntityPlayer) this.shootingEntity, reimbursedAmountOnHit * damage / 20f);
        }
    }

    @Override
    protected void arrowHit(EntityLivingBase living)
    {
        super.arrowHit(living);

        switch (type)
        {
        case CORROSIVE:
            living.addPotionEffect(new PotionEffect(MobEffects.POISON, currentLevel >= 0 ? poisonDuration[currentLevel] : 0, currentLevel >= 0 ? poisonLevel[currentLevel] : 0));
            break;
        case DEFAULT:
            break;
        case DESTRUCTIVE:
            this.world.createExplosion(this, this.posX, this.posY, this.posZ, currentLevel >= 0 ? destructiveExplosionRadius[currentLevel] : 0, false);
            break;
        case STEADFAST:
            living.addPotionEffect(new PotionEffect(MobEffects.LEVITATION, currentLevel >= 0 ? levitationDuration[currentLevel] : 0, currentLevel >= 0 ? levitationLevel[currentLevel] : 0));
            break;
        case VENGEFUL:
            living.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, currentLevel >= 0 ? slownessDuration[currentLevel] : 0, currentLevel >= 0 ? slownessLevel[currentLevel] : 0));
            break;
        default:
            break;
        }
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();

        if (!this.world.isRemote && this.inGround && this.timeInGround > 0)
        {
            switch (type)
            {
            case DESTRUCTIVE:
                this.world.createExplosion(this, this.posX, this.posY, this.posZ, currentLevel >= 0 ? destructiveExplosionRadius[currentLevel] : 0, false);
                this.setDead();
                break;
            case CORROSIVE:
                break;
            case DEFAULT:
                break;
            case STEADFAST:
                break;
            case VENGEFUL:
                break;
            default:
                break;
            }
        }
//        else if (this.inGround && this.timeInGround != 0 && !this.customPotionEffects.isEmpty() && this.timeInGround >= 600)
//        {
//            this.world.setEntityState(this, (byte)0);
//            this.potion = PotionTypes.EMPTY;
//            this.customPotionEffects.clear();
//            this.dataManager.set(COLOR, Integer.valueOf(-1));
//        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tag)
    {
        super.writeEntityToNBT(tag);

        tag.setDouble("reimbursement", reimbursedAmountOnHit);
        tag.setInteger("currentLevel", currentLevel);
        tag.setString(Constants.NBT.WILL_TYPE, type.toString());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tag)
    {
        super.readEntityFromNBT(tag);

        reimbursedAmountOnHit = tag.getDouble("reimbursement");
        type = EnumDemonWillType.valueOf(tag.getString(Constants.NBT.WILL_TYPE).toUpperCase(Locale.ENGLISH));
        currentLevel = tag.getInteger("currentLevel");
    }

    @Override
    protected ItemStack getArrowStack()
    {
        return new ItemStack(Items.ARROW);
    }
}
