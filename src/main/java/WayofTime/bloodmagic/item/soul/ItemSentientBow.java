package WayofTime.bloodmagic.item.soul;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.client.IVariantProvider;
import WayofTime.bloodmagic.core.RegistrarBloodMagicItems;
import WayofTime.bloodmagic.entity.mob.EntitySentientSpecter;
import WayofTime.bloodmagic.entity.projectile.EntitySentientArrow;
import WayofTime.bloodmagic.iface.IMultiWillTool;
import WayofTime.bloodmagic.iface.ISentientTool;
import WayofTime.bloodmagic.soul.EnumDemonWillType;
import WayofTime.bloodmagic.soul.PlayerDemonWillHandler;
import WayofTime.bloodmagic.util.Constants;
import WayofTime.bloodmagic.util.helper.NBTHelper;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Locale;

public class ItemSentientBow extends ItemBow implements IMultiWillTool, ISentientTool, IVariantProvider//, IMeshProvider
{
    public static int[] soulBracket = new int[] { 16, 60, 200, 400, 1000, 2000, 4000 };
    public static double[] defaultDamageAdded = new double[] { 0.25, 0.5, 0.75, 1, 1.25, 1.5, 1.75 };
    public static float[] velocityAdded = new float[] { 0.25f, 0.5f, 0.75f, 1, 1.25f, 1.5f, 1.75f };
    public static double[] soulDrainPerSwing = new double[] { 0.05, 0.1, 0.2, 0.4, 0.75, 1, 1.5 }; //TODO
    public static double[] soulDrop = new double[] { 2, 4, 7, 10, 13, 16, 24 };
    public static double[] staticDrop = new double[] { 1, 1, 2, 3, 3, 3, 4 };

    public ItemSentientBow()
    {
        super();
        setUnlocalizedName(BloodMagic.MODID + ".sentientBow");
        setCreativeTab(BloodMagic.TAB_BM);
        this.addPropertyOverride(new ResourceLocation("pull"), new IItemPropertyGetter()
        {
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, World world, EntityLivingBase entityIn)
            {
                if (entityIn == null)
                {
                    return 0.0F;
                } else
                {
                    ItemStack itemstack = entityIn.getActiveItemStack();
                    return !itemstack.isEmpty() && itemstack.getItem() == RegistrarBloodMagicItems.SENTIENT_BOW ? (float) (stack.getMaxItemUseDuration() - entityIn.getItemInUseCount()) / 20.0F : 0.0F;
                }
            }
        });
        this.addPropertyOverride(new ResourceLocation("pulling"), new IItemPropertyGetter()
        {
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, World world, EntityLivingBase entityIn)
            {
                return entityIn != null && entityIn.isHandActive() && entityIn.getActiveItemStack() == stack ? 1.0F : 0.0F;
            }
        });
        this.addPropertyOverride(new ResourceLocation("type"), new IItemPropertyGetter()
        {
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, World world, EntityLivingBase entityIn)
            {
                return ((ItemSentientBow) RegistrarBloodMagicItems.SENTIENT_BOW).getCurrentType(stack).ordinal();
            }
        });
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
    {
        return RegistrarBloodMagicItems.ITEM_DEMON_CRYSTAL == repair.getItem() || super.getIsRepairable(toRepair, repair);
    }

    public void recalculatePowers(ItemStack stack, World world, EntityPlayer player)
    {
        EnumDemonWillType type = PlayerDemonWillHandler.getLargestWillType(player);
        double soulsRemaining = PlayerDemonWillHandler.getTotalDemonWill(type, player);
        recalculatePowers(stack, type, soulsRemaining);
    }

    public void recalculatePowers(ItemStack stack, EnumDemonWillType type, double will)
    {
        this.setCurrentType(stack, will > 0 ? type : EnumDemonWillType.DEFAULT);
        int level = getLevel(will);
//
        double drain = level >= 0 ? soulDrainPerSwing[level] : 0;

        setDrainOfActivatedBow(stack, drain);
        setStaticDropOfActivatedBow(stack, level >= 0 ? staticDrop[level] : 1);
        setDropOfActivatedBow(stack, level >= 0 ? soulDrop[level] : 0);
//        double drain = level >= 0 ? soulDrainPerSwing[level] : 0;
//        double extraDamage = level >= 0 ? damageAdded[level] : 0;
//
//        setDrainOfActivatedSword(stack, drain);
//        setDamageOfActivatedSword(stack, 7 + extraDamage);
//        setStaticDropOfActivatedSword(stack, level >= 0 ? staticDrop[level] : 1);
//        setDropOfActivatedSword(stack, level >= 0 ? soulDrop[level] : 0);

        setVelocityOfArrow(stack, level >= 0 ? 3 + getVelocityModifier(type, level) : 0);
        setDamageAdded(stack, level >= 0 ? getDamageModifier(type, level) : 0);
    }

    private int getLevel(double soulsRemaining)
    {
        int lvl = -1;
        for (int i = 0; i < soulBracket.length; i++)
        {
            if (soulsRemaining >= soulBracket[i])
            {
                lvl = i;
            }
        }

        return lvl;
    }

    @Override
    public EnumDemonWillType getCurrentType(ItemStack stack)
    {
        NBTHelper.checkNBT(stack);

        NBTTagCompound tag = stack.getTagCompound();

        if (!tag.hasKey(Constants.NBT.WILL_TYPE))
        {
            return EnumDemonWillType.DEFAULT;
        }

        return EnumDemonWillType.valueOf(tag.getString(Constants.NBT.WILL_TYPE).toUpperCase(Locale.ENGLISH));
    }

    public double getDamageModifier(EnumDemonWillType type, int willBracket)
    {
        switch (type)
        {
        case VENGEFUL:
            return 0;
        case DEFAULT:
        case CORROSIVE:
        case DESTRUCTIVE:
        case STEADFAST:
            return defaultDamageAdded[willBracket];
        }

        return 0;
    }

    public float getVelocityModifier(EnumDemonWillType type, int willBracket)
    {
        switch (type)
        {
        case VENGEFUL:
            return velocityAdded[willBracket];
        default:
            return 0;
        }
    }

    public void setDamageAdded(ItemStack stack, double damage)
    {
        NBTHelper.checkNBT(stack);

        NBTTagCompound tag = stack.getTagCompound();

        tag.setDouble("damage", damage);
    }

    public double getDamageAdded(ItemStack stack)
    {
        NBTHelper.checkNBT(stack);

        NBTTagCompound tag = stack.getTagCompound();

        return tag.getDouble("damage");
    }

    public void setVelocityOfArrow(ItemStack stack, float velocity)
    {
        NBTHelper.checkNBT(stack);

        NBTTagCompound tag = stack.getTagCompound();

        tag.setFloat("velocity", velocity);
    }

    public float getVelocityOfArrow(ItemStack stack)
    {
        NBTHelper.checkNBT(stack);

        NBTTagCompound tag = stack.getTagCompound();

        if (tag.hasKey("velocity"))
        {
            return tag.getFloat("velocity");
        }

        return 3;
    }

    public void setCurrentType(ItemStack stack, EnumDemonWillType type)
    {
        NBTHelper.checkNBT(stack);

        NBTTagCompound tag = stack.getTagCompound();

        tag.setString(Constants.NBT.WILL_TYPE, type.toString());
    }

    public double getDrainOfActivatedBow(ItemStack stack)
    {
        NBTHelper.checkNBT(stack);

        NBTTagCompound tag = stack.getTagCompound();
        return tag.getDouble(Constants.NBT.SOUL_SWORD_ACTIVE_DRAIN);
    }

    public void setDrainOfActivatedBow(ItemStack stack, double drain)
    {
        NBTHelper.checkNBT(stack);

        NBTTagCompound tag = stack.getTagCompound();

        tag.setDouble(Constants.NBT.SOUL_SWORD_ACTIVE_DRAIN, drain);
    }

    public double getStaticDropOfActivatedBow(ItemStack stack)
    {
        NBTHelper.checkNBT(stack);

        NBTTagCompound tag = stack.getTagCompound();
        return tag.getDouble(Constants.NBT.SOUL_SWORD_STATIC_DROP);
    }

    public void setStaticDropOfActivatedBow(ItemStack stack, double drop)
    {
        NBTHelper.checkNBT(stack);

        NBTTagCompound tag = stack.getTagCompound();

        tag.setDouble(Constants.NBT.SOUL_SWORD_STATIC_DROP, drop);
    }

    public double getDropOfActivatedBow(ItemStack stack)
    {
        NBTHelper.checkNBT(stack);

        NBTTagCompound tag = stack.getTagCompound();
        return tag.getDouble(Constants.NBT.SOUL_SWORD_DROP);
    }

    public void setDropOfActivatedBow(ItemStack stack, double drop)
    {
        NBTHelper.checkNBT(stack);

        NBTTagCompound tag = stack.getTagCompound();

        tag.setDouble(Constants.NBT.SOUL_SWORD_DROP, drop);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
    {
        ItemStack stack = player.getHeldItem(hand);
        this.recalculatePowers(stack, world, player);
        return super.onItemRightClick(world, player, hand);
    }

    @Override
    public void gatherVariants(@Nonnull Int2ObjectMap<String> variants)
    {
        variants.put(0, "inventory");
    }

    public EntityTippedArrow getArrowEntity(World world, ItemStack stack, EntityLivingBase target, EntityLivingBase user, float velocity)
    {
        EnumDemonWillType type = this.getCurrentType(stack);

        double amount = user instanceof EntityPlayer ? (this.getDropOfActivatedBow(stack) * world.rand.nextDouble() + this.getStaticDropOfActivatedBow(stack)) : 0;

        float newArrowVelocity = velocity * getVelocityOfArrow(stack);
        double soulsRemaining = user instanceof EntityPlayer ? (PlayerDemonWillHandler.getTotalDemonWill(type, (EntityPlayer) user)) : 0;
        EntitySentientArrow entityArrow = new EntitySentientArrow(world, user, type, amount, getLevel(soulsRemaining));

        double d0 = target.posX - user.posX;
        double d1 = target.getEntityBoundingBox().minY + (double) (target.height / 3.0F) - entityArrow.posY;
        double d2 = target.posZ - user.posZ;
        double d3 = (double) MathHelper.sqrt(d0 * d0 + d2 * d2);
        entityArrow.shoot(d0, d1 + d3 * 0.05, d2, newArrowVelocity, 0);

        if (newArrowVelocity == 0)
        {
            world.playSound(null, user.getPosition(), SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.NEUTRAL, 0.4F, 1.0F);
            return null;
        }

        if (velocity == 1.0F)
        {
            entityArrow.setIsCritical(true);
        }

        int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);

        entityArrow.setDamage(entityArrow.getDamage() + this.getDamageAdded(stack) + (j > 0 ? j * 0.5 + 0.5 : 0));

        int k = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack);

        if (k > 0)
        {
            entityArrow.setKnockbackStrength(k);
        }

        if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, stack) > 0)
        {
            entityArrow.setFire(100);
        }

        entityArrow.pickupStatus = EntityArrow.PickupStatus.CREATIVE_ONLY;

        return entityArrow;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase entityLiving, int timeLeft)
    {
        if (entityLiving instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer) entityLiving;
            boolean flag = player.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack) > 0;
            ItemStack itemstack = this.getFiredArrow(player);

            int i = this.getMaxItemUseDuration(stack) - timeLeft;
            i = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(stack, world, (EntityPlayer) entityLiving, i, itemstack != null || flag);
            if (i < 0)
                return;

            if (itemstack != null || flag)
            {
                if (itemstack == null)
                {
                    itemstack = new ItemStack(Items.ARROW);
                }

                float arrowVelocity = getArrowVelocity(i);

                if ((double) arrowVelocity >= 0.1D)
                {
                    boolean flag1 = flag && itemstack.getItem() == Items.ARROW; //Forge: Fix consuming custom arrows.

                    if (!world.isRemote)
                    {
                        this.recalculatePowers(stack, world, player);
                        EnumDemonWillType type = this.getCurrentType(stack);

                        //Need to do some stuffs
//                        ItemArrow itemarrow = ((ItemArrow) (itemstack.getItem() instanceof ItemArrow ? itemstack.getItem() : Items.arrow));
//                        EntityArrow entityArrow = itemarrow.createArrow(world, itemstack, player);

                        double amount = (this.getDropOfActivatedBow(stack) * world.rand.nextDouble() + this.getStaticDropOfActivatedBow(stack));

                        float newArrowVelocity = arrowVelocity * getVelocityOfArrow(stack);
                        double soulsRemaining = PlayerDemonWillHandler.getTotalDemonWill(type, player);
                        EntitySentientArrow entityArrow = new EntitySentientArrow(world, entityLiving, type, amount, getLevel(soulsRemaining));
                        entityArrow.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, newArrowVelocity, 1.0F);

                        if (newArrowVelocity == 0)
                        {
                            world.playSound(null, player.getPosition(), SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.NEUTRAL, 0.4F, 1.0F);
                            return;
                        }

                        if (arrowVelocity == 1.0F)
                        {
                            entityArrow.setIsCritical(true);
                        }

                        int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);

                        entityArrow.setDamage(entityArrow.getDamage() + this.getDamageAdded(stack) + (j > 0 ? j * 0.5 + 0.5 : 0));

                        int k = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack);

                        if (k > 0)
                        {
                            entityArrow.setKnockbackStrength(k);
                        }

                        if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, stack) > 0)
                        {
                            entityArrow.setFire(100);
                        }

                        stack.damageItem(1, player);

                        if (flag1)
                        {
                            entityArrow.pickupStatus = EntityArrow.PickupStatus.CREATIVE_ONLY;
                        }

                        world.spawnEntity(entityArrow);
                    }

                    world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.NEUTRAL, 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + arrowVelocity * 0.5F);

                    if (!flag1)
                    {
                        itemstack.shrink(1);

                        if (itemstack.isEmpty())
                        {
                            player.inventory.deleteStack(itemstack);
                        }
                    }

                    player.addStat(StatList.getObjectUseStats(this));
                }
            }
        }
    }

    protected ItemStack getFiredArrow(EntityPlayer player)
    {
        if (this.isArrow(player.getHeldItem(EnumHand.OFF_HAND)))
        {
            return player.getHeldItem(EnumHand.OFF_HAND);
        } else if (this.isArrow(player.getHeldItem(EnumHand.MAIN_HAND)))
        {
            return player.getHeldItem(EnumHand.MAIN_HAND);
        } else
        {
            for (int i = 0; i < player.inventory.getSizeInventory(); ++i)
            {
                ItemStack itemstack = player.inventory.getStackInSlot(i);

                if (this.isArrow(itemstack))
                {
                    return itemstack;
                }
            }

            return null;
        }
    }

    @Override
    public boolean spawnSentientEntityOnDrop(ItemStack droppedStack, EntityPlayer player)
    {
        World world = player.getEntityWorld();
        if (!world.isRemote)
        {
            this.recalculatePowers(droppedStack, world, player);

            EnumDemonWillType type = this.getCurrentType(droppedStack);
            double soulsRemaining = PlayerDemonWillHandler.getTotalDemonWill(type, player);
            if (soulsRemaining < 1024)
            {
                return false;
            }

            PlayerDemonWillHandler.consumeDemonWill(type, player, 100);

            EntitySentientSpecter specterEntity = new EntitySentientSpecter(world);
            specterEntity.setPosition(player.posX, player.posY, player.posZ);
            world.spawnEntity(specterEntity);

            specterEntity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, droppedStack.copy());

            specterEntity.setType(this.getCurrentType(droppedStack));
            specterEntity.setOwner(player);
            specterEntity.setTamed(true);

            return true;
        }

        return false;
    }
}
