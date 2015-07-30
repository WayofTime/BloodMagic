package WayofTime.alchemicalWizardry.common.entity.mob;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIFollowOwner;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIOwnerHurtByTarget;
import net.minecraft.entity.ai.EntityAIOwnerHurtTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.EntityAITargetAggro;
import WayofTime.alchemicalWizardry.common.entity.projectile.HolyProjectile;

public class EntityShade extends EntityDemon
{
    private EntityAIAttackOnCollide aiAttackOnCollide = new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.2D, false);

    public EntityShade(World par1World)
    {
        super(par1World, AlchemicalWizardry.entityShadeID);
        this.setSize(0.8F, 2.0F);
        ((PathNavigateGround)this.getNavigator()).func_179690_a(true);
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, this.aiSit);
        this.tasks.addTask(3, new EntityAIAttackOnCollide(this, 1.0D, true));
        this.tasks.addTask(4, new EntityAIFollowOwner(this, 1.0D, 10.0F, 2.0F));
        this.tasks.addTask(5, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIOwnerHurtByTarget(this));
        this.targetTasks.addTask(2, new EntityAIOwnerHurtTarget(this));
        this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, true));
        this.targetTasks.addTask(4, new EntityAITargetAggro(this, EntityPlayer.class, 0, false));
        this.setAggro(false);
        this.setTamed(false);

        if (par1World != null && !par1World.isRemote)
        {
            this.setCombatTask();
        }
    }

    @Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        //This line affects the speed of the monster
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.30000001192092896D);
        float maxTamedHealth = 50.0F;
        float maxUntamedHealth = 100.0F;

        //My guess is that this will alter the max health
        if (this.isTamed())
        {
            this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(maxTamedHealth);
        } else
        {
            this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(maxUntamedHealth);
        }
    }

    public void attackEntityWithRangedAttack(EntityLivingBase par1EntityLivingBase, float par2)
    {
        HolyProjectile hol = new HolyProjectile(worldObj, this, par1EntityLivingBase, 1.8f, 0f, 5, 600);
        this.worldObj.spawnEntityInWorld(hol);
    }

    /**
     * sets this entity's combat AI.
     */
    public void setCombatTask()
    {
        this.tasks.removeTask(this.aiAttackOnCollide);
        //this.tasks.removeTask(this.aiArrowAttack);
        this.tasks.addTask(4, this.aiAttackOnCollide);
    }
}