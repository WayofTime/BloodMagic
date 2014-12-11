package WayofTime.alchemicalWizardry.common.items.sigil;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.items.interfaces.ArmourUpgrade;
import WayofTime.alchemicalWizardry.common.items.EnergyItems;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import WayofTime.alchemicalWizardry.common.spell.simple.SpellTeleport;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemSigilOfTheAssassin extends EnergyItems implements ArmourUpgrade
{
    public ItemSigilOfTheAssassin()
    {
        super();
        this.maxStackSize = 1;
        setEnergyUsed(100);
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
    }

    @Override
    public void onCreated(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        if (par1ItemStack.stackTagCompound == null)
        {
            par1ItemStack.setTagCompound(new NBTTagCompound());
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:WaterSigil");
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack)
    {
        ItemStack copiedStack = itemStack.copy();
        copiedStack.setItemDamage(copiedStack.getItemDamage() + 1);
        copiedStack.stackSize = 1;
        return copiedStack;
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        par3List.add("Time to stay stealthy...");

        if (!(par1ItemStack.stackTagCompound == null))
        {
            par3List.add("Current owner: " + par1ItemStack.stackTagCompound.getString("ownerName"));
        }
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        EnergyItems.checkAndSetItemOwner(par1ItemStack, par3EntityPlayer);

        if (par3EntityPlayer.isSneaking())
        {
            return par1ItemStack;
        }
        


        float f = 10.0F;

        MovingObjectPosition movingobjectposition = SpellHelper.raytraceFromEntity(par2World, par3EntityPlayer, false, f);

        if (movingobjectposition == null)
        {
        	AlchemicalWizardry.logger.info("I saw nothing.");
            return par1ItemStack;
        } else
        {
        	AlchemicalWizardry.logger.info("Got something! Type of hit: " + movingobjectposition.typeOfHit);

            if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY)
            {
            	Entity hitEntity = movingobjectposition.entityHit;
                double x = hitEntity.posX;
                double y = hitEntity.posY;
                double z = hitEntity.posZ;

                if(hitEntity instanceof EntityLivingBase)
                {
                	AlchemicalWizardry.logger.info("It's a living entity!");

                	
                	teleportTo(par3EntityPlayer, x, y, z, par3EntityPlayer.posX, par3EntityPlayer.posY, par3EntityPlayer.posZ);
                }
            }

            return par1ItemStack;
        }
    }
    
    protected static boolean teleportTo(EntityLivingBase entityLiving, double par1, double par3, double par5, double lastX, double lastY, double lastZ)
    {
        EnderTeleportEvent event = new EnderTeleportEvent(entityLiving, par1, par3, par5, 0);

        if (MinecraftForge.EVENT_BUS.post(event))
        {
            return false;
        }

        double d3 = lastX;
        double d4 = lastY;
        double d5 = lastZ;
        SpellTeleport.moveEntityViaTeleport(entityLiving, event.targetX, event.targetY, event.targetZ);
        boolean flag = false;
        int i = MathHelper.floor_double(entityLiving.posX);
        int j = MathHelper.floor_double(entityLiving.posY);
        int k = MathHelper.floor_double(entityLiving.posZ);
        Block l;

        if (entityLiving.worldObj.blockExists(i, j, k))
        {
            boolean flag1 = false;

            while (!flag1 && j > 0)
            {
                l = entityLiving.worldObj.getBlock(i, j - 1, k);

                if (l != null && l.getMaterial().blocksMovement())
                {
                    flag1 = true;
                } else
                {
                    --entityLiving.posY;
                    --j;
                }
            }

            if (flag1)
            {
                SpellTeleport.moveEntityViaTeleport(entityLiving, entityLiving.posX, entityLiving.posY, entityLiving.posZ);

                if (entityLiving.worldObj.getCollidingBoundingBoxes(entityLiving, entityLiving.boundingBox).isEmpty() && !entityLiving.worldObj.isAnyLiquid(entityLiving.boundingBox))
                {
                    flag = true;
                }
            }
        }

        if (!flag)
        {
            SpellTeleport.moveEntityViaTeleport(entityLiving, d3, d4, d5);
            return false;
        } else
        {
            short short1 = 128;

            for (j = 0; j < short1; ++j)
            {
                double d6 = (double) j / ((double) short1 - 1.0D);
                float f = (entityLiving.worldObj.rand.nextFloat() - 0.5F) * 0.2F;
                float f1 = (entityLiving.worldObj.rand.nextFloat() - 0.5F) * 0.2F;
                float f2 = (entityLiving.worldObj.rand.nextFloat() - 0.5F) * 0.2F;
                double d7 = d3 + (entityLiving.posX - d3) * d6 + (entityLiving.worldObj.rand.nextDouble() - 0.5D) * (double) entityLiving.width * 2.0D;
                double d8 = d4 + (entityLiving.posY - d4) * d6 + entityLiving.worldObj.rand.nextDouble() * (double) entityLiving.height;
                double d9 = d5 + (entityLiving.posZ - d5) * d6 + (entityLiving.worldObj.rand.nextDouble() - 0.5D) * (double) entityLiving.width * 2.0D;
                entityLiving.worldObj.spawnParticle("portal", d7, d8, d9, (double) f, (double) f1, (double) f2);
            }
            return true;
        }
    }
    
    public MovingObjectPosition getMovingObjectPositionFromPlayer(World p_77621_1_, EntityPlayer p_77621_2_, boolean p_77621_3_, float reach)
    {
        float f = 1;
        float f1 = p_77621_2_.prevRotationPitch + (p_77621_2_.rotationPitch - p_77621_2_.prevRotationPitch) * f;
        float f2 = p_77621_2_.prevRotationYaw + (p_77621_2_.rotationYaw - p_77621_2_.prevRotationYaw) * f;
        double d0 = p_77621_2_.prevPosX + (p_77621_2_.posX - p_77621_2_.prevPosX) * (double)f;
        double d1 = p_77621_2_.prevPosY + (p_77621_2_.posY - p_77621_2_.prevPosY) * (double)f + (double)(p_77621_1_.isRemote ? p_77621_2_.getEyeHeight() - p_77621_2_.getDefaultEyeHeight() : p_77621_2_.getEyeHeight()); // isRemote check to revert changes to ray trace position due to adding the eye height clientside and player yOffset differences
        double d2 = p_77621_2_.prevPosZ + (p_77621_2_.posZ - p_77621_2_.prevPosZ) * (double)f;
        Vec3 vec3 = Vec3.createVectorHelper(d0, d1, d2);
        float f3 = MathHelper.cos(-f2 * 0.017453292F - (float)Math.PI);
        float f4 = MathHelper.sin(-f2 * 0.017453292F - (float)Math.PI);
        float f5 = -MathHelper.cos(-f1 * 0.017453292F);
        float f6 = MathHelper.sin(-f1 * 0.017453292F);
        float f7 = f4 * f5;
        float f8 = f3 * f5;
        double d3 = 500.0D;
        if (p_77621_2_ instanceof EntityPlayerMP)
        {
//            d3 = ((EntityPlayerMP)p_77621_2_).theItemInWorldManager.getBlockReachDistance();
        }
        Vec3 vec31 = vec3.addVector((double)f7 * d3, (double)f6 * d3, (double)f8 * d3);
        return p_77621_1_.func_147447_a(vec3, vec31, p_77621_3_, !p_77621_3_, false);
    }
    
//    public MovingObjectPosition movingObjectPositiongdsa(WOrld worldObj, int posX, int posY, int posZ)
//    {
//    	Vec3 var17 = Vec3.createVectorHelper(posX, posY, posZ);
//        Vec3 var3 = Vec3.createVectorHelper(posX + motionX, posY + motionY, posZ + motionZ);
//        MovingObjectPosition var4 = worldObj.func_147447_a(var17, var3, true, false, false);
//        var17 = Vec3.createVectorHelper(posX, posY, posZ);
//        var3 = Vec3.createVectorHelper(posX + motionX, posY + motionY, posZ + motionZ);
//
//        if (var4 != null)
//        {
//            var3 = Vec3.createVectorHelper(var4.hitVec.xCoord, var4.hitVec.yCoord, var4.hitVec.zCoord);
//        }
//
//        Entity var5 = null;
//        List var6 = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.addCoord(motionX, motionY, motionZ).expand(1.0D, 1.0D, 1.0D));
//        double var7 = 0.0D;
//        Iterator var9 = var6.iterator();
//        float var11;
//
//        while (var9.hasNext())
//        {
//            Entity var10 = (Entity) var9.next();
//
//            if (var10.canBeCollidedWith() && (var10 != shootingEntity || ticksInAir >= 5))
//            {
//                var11 = 0.3F;
//                AxisAlignedBB var12 = var10.boundingBox.expand(var11, var11, var11);
//                MovingObjectPosition var13 = var12.calculateIntercept(var17, var3);
//
//                if (var13 != null)
//                {
//                    double var14 = var17.distanceTo(var13.hitVec);
//
//                    if (var14 < var7 || var7 == 0.0D)
//                    {
//                        var5 = var10;
//                        var7 = var14;
//                    }
//                }
//            }
//        }
//
//        if (var5 != null)
//        {
//            var4 = new MovingObjectPosition(var5);
//        }
//
//        if (var4 != null)
//        {
//            this.onImpact(var4);
//        }
//    }

    @Override
    public void onArmourUpdate(World world, EntityPlayer player, ItemStack thisItemStack)
    {
        player.addPotionEffect(new PotionEffect(Potion.waterBreathing.id, 2, 9, true));
    }

    @Override
    public boolean isUpgrade()
    {
        return true;
    }

    @Override
    public int getEnergyForTenSeconds()
    {
        return 50;
    }
}
