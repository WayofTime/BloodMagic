package WayofTime.bloodmagic.item.soul;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.iface.IMultiWillTool;
import WayofTime.bloodmagic.api.soul.EnumDemonWillType;
import WayofTime.bloodmagic.api.soul.PlayerDemonWillHandler;
import WayofTime.bloodmagic.api.util.helper.NBTHelper;
import WayofTime.bloodmagic.entity.projectile.EntitySentientArrow;
import WayofTime.bloodmagic.registry.ModItems;

public class ItemSentientBow extends ItemBow implements IMultiWillTool//, IMeshProvider
{
    public static int[] soulBracket = new int[] { 16, 60, 200, 400, 1000 };
    public static double[] defaultDamageAdded = new double[] { 0.25, 0.5, 0.75, 1, 1.25 };
    public static float[] velocityAdded = new float[] { 0.25f, 0.5f, 0.75f, 1, 1.25f };

    public ItemSentientBow()
    {
        super();
        setUnlocalizedName(Constants.Mod.MODID + ".sentientBow");
        setCreativeTab(BloodMagic.tabBloodMagic);
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
                    return itemstack != null && itemstack.getItem() == ModItems.sentientBow ? (float) (stack.getMaxItemUseDuration() - entityIn.getItemInUseCount()) / 20.0F : 0.0F;
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
                return ((ItemSentientBow) ModItems.sentientBow).getCurrentType(stack).ordinal();
            }
        });
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
    {
        return ModItems.itemDemonCrystal == repair.getItem() ? true : super.getIsRepairable(toRepair, repair);
    }

    public void recalculatePowers(ItemStack stack, World world, EntityPlayer player)
    {
        EnumDemonWillType type = PlayerDemonWillHandler.getLargestWillType(player);
        double soulsRemaining = PlayerDemonWillHandler.getTotalDemonWill(type, player);
        this.setCurrentType(stack, soulsRemaining > 0 ? type : EnumDemonWillType.DEFAULT);
        int level = getLevel(stack, soulsRemaining);
//
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

    private int getLevel(ItemStack stack, double soulsRemaining)
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

        return EnumDemonWillType.valueOf(tag.getString(Constants.NBT.WILL_TYPE));
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

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand)
    {
        this.recalculatePowers(stack, world, player);
        return super.onItemRightClick(stack, world, player, hand);
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
                    boolean flag1 = flag && itemstack.getItem() instanceof ItemArrow; //Forge: Fix consuming custom arrows.

                    if (!world.isRemote)
                    {
                        this.recalculatePowers(stack, world, player);
                        EnumDemonWillType type = this.getCurrentType(stack);

                        //Need to do some stuffs
//                        ItemArrow itemarrow = ((ItemArrow) (itemstack.getItem() instanceof ItemArrow ? itemstack.getItem() : Items.arrow));
//                        EntityArrow entityArrow = itemarrow.createArrow(world, itemstack, player);
                        EntityArrow entityArrow = new EntitySentientArrow(world, entityLiving, type);
                        entityArrow.setAim(player, player.rotationPitch, player.rotationYaw, 0.0F, arrowVelocity * getVelocityOfArrow(stack), 1.0F);

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

                        world.spawnEntityInWorld(entityArrow);
                    }

                    world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.NEUTRAL, 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + arrowVelocity * 0.5F);

                    if (!flag1)
                    {
                        --itemstack.stackSize;

                        if (itemstack.stackSize == 0)
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
}
