package WayofTime.bloodmagic.alchemyArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import WayofTime.bloodmagic.api.impl.BloodMagicAPI;
import WayofTime.bloodmagic.api.impl.recipe.RecipeSacrificeCraft;
import WayofTime.bloodmagic.ritual.AreaDescriptor;
import WayofTime.bloodmagic.util.DamageSourceBloodMagic;
import WayofTime.bloodmagic.util.helper.PurificationHelper;

public class AlchemyArrayEffectMobSacrifice extends AlchemyArrayEffect {
    public static final AreaDescriptor itemDescriptor = new AreaDescriptor.Rectangle(new BlockPos(-5, -5, -5), 11);
    public static final AreaDescriptor mobDescriptor = new AreaDescriptor.Rectangle(new BlockPos(-5, -5, -5), 11);
    public int craftTime = 0;
    public static final int REQUIRED_CRAFT_TIME = 200;

    public AlchemyArrayEffectMobSacrifice(String key) {
        super(key);
    }

    @Override
    public boolean update(TileEntity tile, int ticksActive) {
        World world = tile.getWorld();
        if (world.isRemote && ticksActive < 200 && ticksActive > 40) {
            BlockPos pos = tile.getPos();
            Random rand = world.rand;

            for (int i = 0; i < 2; i++) {
                double d0 = (double) pos.getX() + 0.5D + (rand.nextDouble() - 0.5D) * 2.5D;
                double d1 = (double) pos.getY() + 0.2D + (rand.nextDouble() - 0.5D) * 0.2D;
                double d2 = (double) pos.getZ() + 0.5D + (rand.nextDouble() - 0.5D) * 2.5D;
                world.spawnParticle(EnumParticleTypes.SPELL_MOB, d0, d1, d2, 1D, 0.0D, 0.0D);
            }
        }

        //We need to do the check on both sides to correctly do particles.

        if (ticksActive >= 200) {
            BlockPos pos = tile.getPos();

            List<EntityItem> itemList = world.getEntitiesWithinAABB(EntityItem.class, itemDescriptor.getAABB(pos));

            List<ItemStack> inputList = new ArrayList<ItemStack>();

            for (EntityItem entityItem : itemList) {
                if (entityItem.isDead || entityItem.getItem().isEmpty()) {
                    continue;
                }

                inputList.add(entityItem.getItem().copy());
            }

            if (inputList.isEmpty()) {
                return false;
            }

            if (inputList.size() == 1) //TODO: Test if it is a something that can be filled with Soul Breath
            {

            }

            RecipeSacrificeCraft recipe = BloodMagicAPI.INSTANCE.getRecipeRegistrar().getSacrificeCraft(inputList);
            if (recipe != null) {
                double healthRequired = recipe.getHealthRequired();
                double healthAvailable = 0;

                List<EntityLivingBase> livingEntities = world.getEntitiesWithinAABB(EntityLivingBase.class, mobDescriptor.getAABB(pos));
                for (EntityLivingBase living : livingEntities) {
                    double health = getEffectiveHealth(living);
                    if (health > 0) {
                        healthAvailable += health;
                    }
                }

                if (healthAvailable < healthRequired) {
                    craftTime = 0;
                    return false;
                }

                craftTime++;

                if (craftTime >= REQUIRED_CRAFT_TIME) {
                    if (!world.isRemote) {
                        for (EntityLivingBase living : livingEntities) {
                            double health = getEffectiveHealth(living);
                            if (healthAvailable > 0 && health > 0) {
                                healthAvailable -= health;
                                living.getEntityWorld().playSound(null, living.posX, living.posY, living.posZ, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + (living.getEntityWorld().rand.nextFloat() - living.getEntityWorld().rand.nextFloat()) * 0.8F);
                                living.setHealth(-1);
                                living.onDeath(DamageSourceBloodMagic.INSTANCE);
                            }

                            if (healthAvailable <= 0) {
                                break;
                            }
                        }

                        for (EntityItem itemEntity : itemList) {
                            itemEntity.getItem().setCount(itemEntity.getItem().getCount() - 1);
                            if (itemEntity.getItem().isEmpty()) //TODO: Check container
                            {
                                itemEntity.setDead();
                            }
                        }

                        world.spawnEntity(new EntityItem(world, pos.getX() + 0.5, pos.getY() + 2.5, pos.getZ() + 0.5, recipe.getOutput()));
                        craftTime = 0;
                    }
                } else {
                    if (world.isRemote) {
                        Vec3d spawnPosition = new Vec3d(pos.getX() + 0.5, pos.getY() + 2.5, pos.getZ() + 0.5);
                        for (EntityItem itemEntity : itemList) {
                            ItemStack stack = itemEntity.getItem();
                            double velocityFactor = 0.1;

                            Vec3d itemPosition = new Vec3d(itemEntity.posX, itemEntity.posY + 0.5, itemEntity.posZ);
                            Vec3d velVec1 = new Vec3d((world.rand.nextDouble() - 0.5) * velocityFactor, (world.rand.nextDouble() - 0.5) * velocityFactor, (world.rand.nextDouble() - 0.5) * velocityFactor);
//                            Vec3d velVec2 = new Vec3d((world.rand.nextDouble() - 0.5) * velocityFactor, (world.rand.nextDouble()) * velocityFactor, (world.rand.nextDouble() - 0.5) * velocityFactor);

//                                vec3d1 = vec3d1.addVector(this.posX, this.posY + (double)this.getEyeHeight(), this.posZ);
//                                if (this.world instanceof WorldServer) //Forge: Fix MC-2518 spawnParticle is nooped on server, need to use server specific variant
//                                    ((WorldServer)this.world).spawnParticle(EnumParticleTypes.ITEM_CRACK, vec3d1.x, vec3d1.y, vec3d1.z, 0,  vec3d.x, vec3d.y + 0.05D, vec3d.z, 0.0D, Item.getIdFromItem(stack.getItem()), stack.getMetadata());
//                                else //Fix the fact that spawning ItemCrack uses TWO arguments.
                            world.spawnParticle(EnumParticleTypes.ITEM_CRACK, itemPosition.x + (spawnPosition.x - itemPosition.x) * craftTime / REQUIRED_CRAFT_TIME, itemPosition.y + (spawnPosition.y - itemPosition.y) * craftTime / REQUIRED_CRAFT_TIME, itemPosition.z + (spawnPosition.z - itemPosition.z) * craftTime / REQUIRED_CRAFT_TIME, velVec1.x, velVec1.y, velVec1.z, Item.getIdFromItem(stack.getItem()), stack.getMetadata());
//                            world.spawnParticle(EnumParticleTypes.ITEM_CRACK, spawnPosition.x, spawnPosition.y, spawnPosition.z, velVec2.x, velVec2.y, velVec2.z, Item.getIdFromItem(stack.getItem()), stack.getMetadata());
                        }

                        for (EntityLivingBase living : livingEntities) {
                            double health = getEffectiveHealth(living);
                            if (health <= 0) {
                                continue;
                            }
                            double d0 = (double) living.posX + (world.rand.nextDouble() - 0.5D) * 0.5D;
                            double d1 = (double) living.posY + 0.5D + (world.rand.nextDouble() - 0.5D) * 1D;
                            double d2 = (double) living.posZ + (world.rand.nextDouble() - 0.5D) * 0.5D;
                            world.spawnParticle(EnumParticleTypes.SPELL_MOB, d0, d1, d2, 1D, 0.0D, 0.0D);
                        }
                    }
                }
            }
        }

        return false;
    }

    //Future-proofing in case I want to make different mobs give different effective health
    public double getEffectiveHealth(EntityLivingBase living) {
        if (living == null)
            return 0;

        if (!living.isNonBoss())
            return 0;

        if (living instanceof EntityPlayer)
            return 0;

        if (living.isChild() && !(living instanceof IMob))
            return 0;

        if (living.isDead || living.getHealth() < 0.5F)
            return 0;

        EntityEntry entityEntry = EntityRegistry.getEntry(living.getClass());
        if (entityEntry == null)
            return 0;

        return living.getHealth();
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {

    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

    }

    @Override
    public AlchemyArrayEffect getNewCopy() {
        return new AlchemyArrayEffectMobSacrifice(key);
    }
}
