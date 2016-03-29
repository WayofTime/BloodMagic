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
import WayofTime.bloodmagic.registry.ModItems;

public class ItemSentientBow extends ItemBow implements IMultiWillTool//, IMeshProvider
{
    public ItemSentientBow()
    {
        super();
        setUnlocalizedName(Constants.Mod.MODID + ".sentientBow");
        setRegistryName(Constants.BloodMagicItem.SENTIENT_BOW.getRegName());
        setCreativeTab(BloodMagic.tabBloodMagic);
        this.addPropertyOverride(new ResourceLocation("pull"), new IItemPropertyGetter()
        {
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, World worldIn, EntityLivingBase entityIn)
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
            public float apply(ItemStack stack, World worldIn, EntityLivingBase entityIn)
            {
                return entityIn != null && entityIn.isHandActive() && entityIn.getActiveItemStack() == stack ? 1.0F : 0.0F;
            }
        });
        this.addPropertyOverride(new ResourceLocation("type"), new IItemPropertyGetter()
        {
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, World worldIn, EntityLivingBase entityIn)
            {
                return ((ItemSentientBow) ModItems.sentientBow).getCurrentType(stack).ordinal();
            }
        });
    }

//    @Override
//    @SideOnly(Side.CLIENT)
//    public ItemMeshDefinition getMeshDefinition()
//    {
//        return new ItemMeshDefinition()
//        {
//            @Override
//            public ModelResourceLocation getModelLocation(ItemStack stack)
//            {
//                assert getCustomLocation() != null;
//                EnumDemonWillType type = ((ItemSentientBow) ModItems.sentientBow).getCurrentType(stack);
//                String additional = type.getName().toLowerCase();
//                return new ModelResourceLocation(getCustomLocation(), "type=" + additional);
//            }
//        };
//    }
//
//    @Override
//    public ResourceLocation getCustomLocation()
//    {
//        return new ResourceLocation(Constants.Mod.MODID, "item/ItemSentientBow");
//    }
//
//    @Override
//    public List<String> getVariants()
//    {
//        List<String> ret = new ArrayList<String>();
//        for (EnumDemonWillType type : EnumDemonWillType.values())
//        {
//            String additional = type.getName().toLowerCase();
//
//            ret.add("type=" + additional);
//        }
//
//        return ret;
//    }

    public void recalculatePowers(ItemStack stack, World world, EntityPlayer player)
    {
        EnumDemonWillType type = PlayerDemonWillHandler.getLargestWillType(player);
//        double soulsRemaining = PlayerDemonWillHandler.getTotalDemonWill(type, player);
        this.setCurrentType(stack, type);
//        int level = getLevel(stack, soulsRemaining);
//
//        double drain = level >= 0 ? soulDrainPerSwing[level] : 0;
//        double extraDamage = level >= 0 ? damageAdded[level] : 0;
//
//        setDrainOfActivatedSword(stack, drain);
//        setDamageOfActivatedSword(stack, 7 + extraDamage);
//        setStaticDropOfActivatedSword(stack, level >= 0 ? staticDrop[level] : 1);
//        setDropOfActivatedSword(stack, level >= 0 ? soulDrop[level] : 0);
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
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft)
    {
        if (entityLiving instanceof EntityPlayer)
        {
            EntityPlayer entityplayer = (EntityPlayer) entityLiving;
            boolean flag = entityplayer.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantments.infinity, stack) > 0;
            ItemStack itemstack = this.getFiredArrow(entityplayer);

            int i = this.getMaxItemUseDuration(stack) - timeLeft;
            i = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(stack, worldIn, (EntityPlayer) entityLiving, i, itemstack != null || flag);
            if (i < 0)
                return;

            if (itemstack != null || flag)
            {
                if (itemstack == null)
                {
                    itemstack = new ItemStack(Items.arrow);
                }

                float arrowVelocity = getArrowVelocity(i);

                if ((double) arrowVelocity >= 0.1D)
                {
                    boolean flag1 = flag && itemstack.getItem() instanceof ItemArrow; //Forge: Fix consuming custom arrows.

                    if (!worldIn.isRemote)
                    {
                        //Need to do some stuffs
                        ItemArrow itemarrow = ((ItemArrow) (itemstack.getItem() instanceof ItemArrow ? itemstack.getItem() : Items.arrow));
                        EntityArrow entityarrow = itemarrow.createArrow(worldIn, itemstack, entityplayer);
                        entityarrow.func_184547_a(entityplayer, entityplayer.rotationPitch, entityplayer.rotationYaw, 0.0F, arrowVelocity * 3.0F, 1.0F);

                        if (arrowVelocity == 1.0F)
                        {
                            entityarrow.setIsCritical(true);
                        }

                        int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.power, stack);

                        if (j > 0)
                        {
                            entityarrow.setDamage(entityarrow.getDamage() + (double) j * 0.5D + 0.5D);
                        }

                        int k = EnchantmentHelper.getEnchantmentLevel(Enchantments.punch, stack);

                        if (k > 0)
                        {
                            entityarrow.setKnockbackStrength(k);
                        }

                        if (EnchantmentHelper.getEnchantmentLevel(Enchantments.flame, stack) > 0)
                        {
                            entityarrow.setFire(100);
                        }

                        stack.damageItem(1, entityplayer);

                        if (flag1)
                        {
                            entityarrow.canBePickedUp = EntityArrow.PickupStatus.CREATIVE_ONLY;
                        }

                        worldIn.spawnEntityInWorld(entityarrow);
                    }

                    worldIn.playSound(null, entityplayer.posX, entityplayer.posY, entityplayer.posZ, SoundEvents.entity_arrow_shoot, SoundCategory.NEUTRAL, 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + arrowVelocity * 0.5F);

                    if (!flag1)
                    {
                        --itemstack.stackSize;

                        if (itemstack.stackSize == 0)
                        {
                            entityplayer.inventory.deleteStack(itemstack);
                        }
                    }

                    entityplayer.addStat(StatList.getObjectUseStats(this));
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
