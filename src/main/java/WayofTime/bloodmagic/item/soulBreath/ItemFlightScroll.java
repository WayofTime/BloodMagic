package WayofTime.bloodmagic.item.soulBreath;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;

import javax.annotation.Nullable;
import javax.vecmath.Vector3d;

import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.client.IMeshProvider;
import WayofTime.bloodmagic.client.mesh.CustomMeshDefinitionActivatable;
import WayofTime.bloodmagic.core.RegistrarBloodMagic;
import WayofTime.bloodmagic.iface.IActivatable;
import WayofTime.bloodmagic.util.Constants;
import WayofTime.bloodmagic.util.helper.NBTHelper;

public class ItemFlightScroll extends ItemSoulBreathContainer implements IMeshProvider, IActivatable {
    public static Map<EntityPlayer, Map<EntityLivingBase, Vector3d>> floatMap = new HashMap<EntityPlayer, Map<EntityLivingBase, Vector3d>>();
    public static Map<EntityPlayer, EntityLivingBase> heldEntityMap = new HashMap<EntityPlayer, EntityLivingBase>();
    public static Map<EntityPlayer, Double> heldEntityOffsetMap = new HashMap<EntityPlayer, Double>();

    //TODO: A lot of this stuff could be moved to a toggle-able variant
    public ItemFlightScroll() {
        super();
        setTranslationKey(BloodMagic.MODID + ".icarusScroll");
        setCreativeTab(BloodMagic.TAB_BM);
    }

    @Override
    public boolean getActivated(ItemStack stack) {
        return !stack.isEmpty() && NBTHelper.checkNBT(stack).getTagCompound().getBoolean(Constants.NBT.ACTIVATED);
    }

    @Override
    public ItemStack setActivatedState(ItemStack stack, boolean activated) {
        if (!stack.isEmpty()) {
            NBTHelper.checkNBT(stack).getTagCompound().setBoolean(Constants.NBT.ACTIVATED, activated);
            return stack;
        }

        return stack;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);

        if (!world.isRemote) {
            if (player.isSneaking()) {
                if (!getActivated(stack)) {
                    double drainNeeded = getBreathCostPerSecond(stack);
                    if (this.drainBreath(stack, drainNeeded, false) >= drainNeeded) {
                        setActivatedState(stack, true);
                    }
                } else {
                    setActivatedState(stack, false);
                }
            } else {
                //TODO: Add an effect where it "draws back" like a bow in order to cast Levitation on a mob.
                //Only Levitated mobs can be grabbed.
            }
        }

        return super.onItemRightClick(world, player, hand);
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase entity, EnumHand hand) {
        if (entity.world.isRemote) {
            return false;
        }

        //TODO: Do check to see if the entity is levitating - will only "ensnare" a mob that is levitating.

        if (player.isSneaking()) {
            //TODO: Release entity completely?
            removeEntity(player, entity);
        } else {
            EntityLivingBase heldEntity = getHeldEntity(player);
            if (heldEntity != null && heldEntity.equals(entity)) {
                heldEntityMap.remove(player);
            } else {
                holdEntity(player, entity); //Hold the entity so you can place it around yourself where needed.
            }
        }

        return true;
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
        if (!world.isRemote && entity instanceof EntityPlayerMP && getActivated(stack)) {
            if (entity.ticksExisted % 20 == 0) {
                double drainNeeded = getBreathCostPerSecond(stack);
                if (this.drainBreath(stack, drainNeeded, false) >= drainNeeded) {
                    this.drainBreath(stack, drainNeeded, true);
                } else {
                    this.setActivatedState(stack, false);
                }
            }

            onEffectUpdate(stack, world, (EntityPlayer) entity, itemSlot, isSelected);
        }

        if (!world.isRemote) {
            if (entity instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) entity;
                updateHeldEntityPosition(player);
                if (floatMap.containsKey(player)) {
                    Map<EntityLivingBase, Vector3d> entityMap = floatMap.get(player);
                    if (entityMap == null) {
                        return;
                    }

                    List<EntityLivingBase> removalList = new ArrayList<EntityLivingBase>();

                    for (Entry<EntityLivingBase, Vector3d> entry : entityMap.entrySet()) {
                        EntityLivingBase floatingEntity = entry.getKey();
                        if (floatingEntity == null || floatingEntity.isDead || floatingEntity.dimension != player.dimension) {
                            removalList.add(floatingEntity);
                        }

                        followOwner(player, floatingEntity, entry.getValue());
                    }

                    for (EntityLivingBase livingEntity : removalList) {
                        entityMap.remove(livingEntity);
                    }

                    if (entityMap.isEmpty()) {
                        floatMap.remove(player);
                    }

                }
            }
        }
    }

    public static boolean updateEntityOffset(EntityPlayer player, EntityLivingBase living, Vector3d updatedOffset) {
        //TODO: Check if this entity is contained in another player's map to prevent weird things.
        if (floatMap.containsKey(player)) {
            Map<EntityLivingBase, Vector3d> entityMap = floatMap.get(player);
            entityMap.put(living, updatedOffset);
            return true;
        } else {
            Map<EntityLivingBase, Vector3d> entityMap = new HashMap<EntityLivingBase, Vector3d>();
            entityMap.put(living, updatedOffset);
            floatMap.put(player, entityMap);
            return true;
        }
    }

    @Nullable
    public static EntityLivingBase getHeldEntity(EntityPlayer player) {
        if (heldEntityMap.containsKey(player)) {
            return heldEntityMap.get(player);
        }

        return null;
    }

    public static double getHeldEntityOffset(EntityPlayer player) {
        if (heldEntityMap.containsKey(player)) {
            return heldEntityOffsetMap.get(player);
        }

        return 1;
    }

    public static void holdEntity(EntityPlayer player, EntityLivingBase entityLiving) {
        float distance = player.getDistance(entityLiving);
        Vec3d lookVec = player.getLookVec();
        heldEntityMap.put(player, entityLiving);
        heldEntityOffsetMap.put(player, (double) distance);
        updateEntityOffset(player, entityLiving, new Vector3d(lookVec.x * distance, lookVec.y * distance, lookVec.z * distance));
    }

    public static void updateHeldEntityPosition(EntityPlayer player) {
        EntityLivingBase entityLiving = getHeldEntity(player);
        if (entityLiving != null) {
            double offset = getHeldEntityOffset(player);
            Vec3d lookVec = player.getLookVec();
            updateEntityOffset(player, entityLiving, new Vector3d(lookVec.x * offset, lookVec.y * offset, lookVec.z * offset));
        }
    }

    public static void removeEntity(EntityPlayer player, EntityLivingBase living) {
        if (living == null) {
            return;
        }

        if (floatMap.containsKey(player)) {
            Map<EntityLivingBase, Vector3d> entityMap = floatMap.get(player);
            if (entityMap.containsKey(living)) {
                entityMap.remove(living);
            }

            if (entityMap.isEmpty()) {
                floatMap.remove(player);
            }
        }
    }

    public void followOwner(EntityPlayer owner, EntityLivingBase livingEntity, Vector3d offset) {
        double offsetX = offset.x;
        double offsetY = offset.y;
        double offsetZ = offset.z;
        double ownerSpeed = Math.sqrt(owner.motionX * owner.motionX + owner.motionY * owner.motionY + owner.motionZ * owner.motionZ);
//        double ownerSpeed = Math.sqrt((owner.posX - owner.prevPosX) * (owner.posX - owner.prevPosX) + (owner.posY - owner.prevPosY) * (owner.posY - owner.prevPosY) + (owner.posZ - owner.prevPosZ) * (owner.posZ - owner.prevPosZ));

        double speed = Math.max(ownerSpeed * 20, 2); //May just want to call it a day and set this to "2"

        livingEntity.addPotionEffect(new PotionEffect(MobEffects.LEVITATION, 20, 0, false, true));

        double wantedX = owner.posX + offsetX;
        double wantedY = owner.posY + offsetY;
        double wantedZ = owner.posZ + offsetZ;

        Vector3d vec = new Vector3d(wantedX - livingEntity.posX, wantedY - livingEntity.posY, wantedZ - livingEntity.posZ);
        double vecDistance = Math.sqrt(vec.dot(vec));
        speed = Math.min(vecDistance, speed);

        vec.normalize();

        if (speed <= 0.00001) {
            return;
        }

        livingEntity.setVelocity(vec.x * speed, vec.y * speed, vec.z * speed);
    }

    public void onEffectUpdate(ItemStack stack, World world, EntityPlayer player, int itemSlot, boolean isSelected) {
        player.addPotionEffect(new PotionEffect(RegistrarBloodMagic.FLIGHT, 2, 0));
    }

    @Override
    public int getMaxBreath(ItemStack stack) {
        return 20;
    }

    public double getBreathCostPerSecond(ItemStack stack) {
        return 0.01;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ItemMeshDefinition getMeshDefinition() {
        return new CustomMeshDefinitionActivatable("icarus_scroll");
    }

    @Override
    public void gatherVariants(Consumer<String> variants) {
        variants.accept("active=false");
        variants.accept("active=true");
    }
}
