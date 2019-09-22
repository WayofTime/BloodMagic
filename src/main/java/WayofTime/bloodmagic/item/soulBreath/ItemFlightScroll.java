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
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.Effects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
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
    public static Map<PlayerEntity, Map<LivingEntity, Vector3d>> floatMap = new HashMap<PlayerEntity, Map<LivingEntity, Vector3d>>();
    public static Map<PlayerEntity, LivingEntity> heldEntityMap = new HashMap<PlayerEntity, LivingEntity>();
    public static Map<PlayerEntity, Double> heldEntityOffsetMap = new HashMap<PlayerEntity, Double>();

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
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
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
    public boolean itemInteractionForEntity(ItemStack stack, PlayerEntity player, LivingEntity entity, Hand hand) {
        if (entity.world.isRemote) {
            return false;
        }

        //TODO: Do check to see if the entity is levitating - will only "ensnare" a mob that is levitating.

        if (player.isSneaking()) {
            //TODO: Release entity completely?
            removeEntity(player, entity);
        } else {
            LivingEntity heldEntity = getHeldEntity(player);
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
        if (!world.isRemote && entity instanceof ServerPlayerEntity && getActivated(stack)) {
            if (entity.ticksExisted % 20 == 0) {
                double drainNeeded = getBreathCostPerSecond(stack);
                if (this.drainBreath(stack, drainNeeded, false) >= drainNeeded) {
                    this.drainBreath(stack, drainNeeded, true);
                } else {
                    this.setActivatedState(stack, false);
                }
            }

            onEffectUpdate(stack, world, (PlayerEntity) entity, itemSlot, isSelected);
        }

        if (!world.isRemote) {
            if (entity instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) entity;
                updateHeldEntityPosition(player);
                if (floatMap.containsKey(player)) {
                    Map<LivingEntity, Vector3d> entityMap = floatMap.get(player);
                    if (entityMap == null) {
                        return;
                    }

                    List<LivingEntity> removalList = new ArrayList<LivingEntity>();

                    for (Entry<LivingEntity, Vector3d> entry : entityMap.entrySet()) {
                        LivingEntity floatingEntity = entry.getKey();
                        if (floatingEntity == null || floatingEntity.isDead || floatingEntity.dimension != player.dimension) {
                            removalList.add(floatingEntity);
                        }

                        followOwner(player, floatingEntity, entry.getValue());
                    }

                    for (LivingEntity livingEntity : removalList) {
                        entityMap.remove(livingEntity);
                    }

                    if (entityMap.isEmpty()) {
                        floatMap.remove(player);
                    }

                }
            }
        }
    }

    public static boolean updateEntityOffset(PlayerEntity player, LivingEntity living, Vector3d updatedOffset) {
        //TODO: Check if this entity is contained in another player's map to prevent weird things.
        if (floatMap.containsKey(player)) {
            Map<LivingEntity, Vector3d> entityMap = floatMap.get(player);
            entityMap.put(living, updatedOffset);
            return true;
        } else {
            Map<LivingEntity, Vector3d> entityMap = new HashMap<LivingEntity, Vector3d>();
            entityMap.put(living, updatedOffset);
            floatMap.put(player, entityMap);
            return true;
        }
    }

    @Nullable
    public static LivingEntity getHeldEntity(PlayerEntity player) {
        if (heldEntityMap.containsKey(player)) {
            return heldEntityMap.get(player);
        }

        return null;
    }

    public static double getHeldEntityOffset(PlayerEntity player) {
        if (heldEntityMap.containsKey(player)) {
            return heldEntityOffsetMap.get(player);
        }

        return 1;
    }

    public static void holdEntity(PlayerEntity player, LivingEntity entityLiving) {
        float distance = player.getDistance(entityLiving);
        Vec3d lookVec = player.getLookVec();
        heldEntityMap.put(player, entityLiving);
        heldEntityOffsetMap.put(player, (double) distance);
        updateEntityOffset(player, entityLiving, new Vector3d(lookVec.x * distance, lookVec.y * distance, lookVec.z * distance));
    }

    public static void updateHeldEntityPosition(PlayerEntity player) {
        LivingEntity entityLiving = getHeldEntity(player);
        if (entityLiving != null) {
            double offset = getHeldEntityOffset(player);
            Vec3d lookVec = player.getLookVec();
            updateEntityOffset(player, entityLiving, new Vector3d(lookVec.x * offset, lookVec.y * offset, lookVec.z * offset));
        }
    }

    public static void removeEntity(PlayerEntity player, LivingEntity living) {
        if (living == null) {
            return;
        }

        if (floatMap.containsKey(player)) {
            Map<LivingEntity, Vector3d> entityMap = floatMap.get(player);
            if (entityMap.containsKey(living)) {
                entityMap.remove(living);
            }

            if (entityMap.isEmpty()) {
                floatMap.remove(player);
            }
        }
    }

    public void followOwner(PlayerEntity owner, LivingEntity livingEntity, Vector3d offset) {
        double offsetX = offset.x;
        double offsetY = offset.y;
        double offsetZ = offset.z;
        double ownerSpeed = Math.sqrt(owner.motionX * owner.motionX + owner.motionY * owner.motionY + owner.motionZ * owner.motionZ);
//        double ownerSpeed = Math.sqrt((owner.posX - owner.prevPosX) * (owner.posX - owner.prevPosX) + (owner.posY - owner.prevPosY) * (owner.posY - owner.prevPosY) + (owner.posZ - owner.prevPosZ) * (owner.posZ - owner.prevPosZ));

        double speed = Math.max(ownerSpeed * 20, 2); //May just want to call it a day and set this to "2"

        livingEntity.addPotionEffect(new EffectInstance(Effects.LEVITATION, 20, 0, false, true));

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

    public void onEffectUpdate(ItemStack stack, World world, PlayerEntity player, int itemSlot, boolean isSelected) {
        player.addPotionEffect(new EffectInstance(RegistrarBloodMagic.FLIGHT, 2, 0));
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
