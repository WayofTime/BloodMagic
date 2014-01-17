package WayofTime.alchemicalWizardry.common;

import WayofTime.alchemicalWizardry.common.entity.mob.EntityElemental;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntityAirElemental extends EntityElemental implements IMob
{
    public EntityAirElemental(World world)
    {
        super(world, AlchemicalWizardry.entityAirElementalID);
    }

    public void inflictEffectOnEntity(Entity target)
    {
        if (target instanceof EntityPlayer)
        {
            PacketDispatcher.sendPacketToPlayer(PacketHandler.getPlayerVelocitySettingPacket(target.motionX, target.motionY + 3, target.motionZ), (Player)target);
            ((EntityLivingBase)target).addPotionEffect(new PotionEffect(AlchemicalWizardry.customPotionInhibit.id, 150, 0));
        }
        else if (target instanceof EntityLivingBase)
        {
            ((EntityLivingBase)target).motionY += 3.0D;
            ((EntityLivingBase)target).addPotionEffect(new PotionEffect(AlchemicalWizardry.customPotionInhibit.id, 150, 0));
        }
    }
}
