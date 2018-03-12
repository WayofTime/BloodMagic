package WayofTime.bloodmagic.item;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.core.RegistrarBloodMagicItems;
import WayofTime.bloodmagic.core.data.Binding;
import WayofTime.bloodmagic.event.BoundToolEvent;
import WayofTime.bloodmagic.iface.IActivatable;
import WayofTime.bloodmagic.iface.IBindable;
import WayofTime.bloodmagic.util.Constants;
import WayofTime.bloodmagic.util.Utils;
import WayofTime.bloodmagic.util.helper.NetworkHelper;
import WayofTime.bloodmagic.util.helper.TextHelper;
import com.google.common.collect.*;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

public class ItemBoundTool extends ItemTool implements IBindable, IActivatable {
    public static final int MAX_CHARGE_TIME = 30;
    protected final String tooltipBase;
    private final String name;

    public ItemBoundTool(String name, float damage, Set<Block> effectiveBlocks) {
        super(damage, 1, RegistrarBloodMagicItems.BOUND_TOOL_MATERIAL, effectiveBlocks);
        setUnlocalizedName(BloodMagic.MODID + ".bound." + name);
        setCreativeTab(BloodMagic.TAB_BM);
        setHarvestLevel(name, 4);

        addPropertyOverride(new ResourceLocation(BloodMagic.MODID, "activated"), new IItemPropertyGetter()
        {
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn)
            {
                return getActivated(stack) ? 1F : 0F;
            }
        });

        addPropertyOverride(new ResourceLocation(BloodMagic.MODID, "charge"), new IItemPropertyGetter()
        {
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn)
            {
                return entityIn != null && getActivated(stack) && entityIn.isHandActive() && entityIn.getActiveItemStack() == stack ? MathHelper.clamp((float)Math.min(getCharge(stack), MAX_CHARGE_TIME) / (float)MAX_CHARGE_TIME, 0.0F, 1.0F) : 0;
            }
        });

        this.name = name;
        this.tooltipBase = "tooltip.bloodmagic.bound." + name + ".";
    }

    @Override
    public float getDestroySpeed(ItemStack stack, IBlockState state) {
        return getActivated(stack) ? toolMaterial.getEfficiency() : 1.0F;
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return slotChanged;
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
        if (!isInCreativeTab(tab))
            return;

        subItems.add(Utils.setUnbreakable(new ItemStack(this)));
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
        Binding binding = getBinding(stack);
        if (binding == null) {
            setActivatedState(stack, false);
            return;
        }

        if (entity instanceof EntityPlayer && getActivated(stack) && isSelected && stack == ((EntityPlayer) entity).getActiveItemStack()) {
            EntityPlayer player = (EntityPlayer) entity;
            if (world.isRemote)
                setCharge(stack, Math.min(72000 - player.getItemInUseCount(), MAX_CHARGE_TIME));
        }

        if (entity instanceof EntityPlayer && getActivated(stack) && world.getTotalWorldTime() % 80 == 0)
            NetworkHelper.getSoulNetwork(binding).syphonAndDamage((EntityPlayer) entity, 20);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (player.isSneaking())
            setActivatedState(stack, !getActivated(stack));

        if (!player.isSneaking() && getActivated(stack)) {
            BoundToolEvent.Charge event = new BoundToolEvent.Charge(player, stack);
            if (MinecraftForge.EVENT_BUS.post(event))
                return new ActionResult<>(EnumActionResult.FAIL, event.result);

            player.setActiveHand(hand);
            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        }

        return super.onItemRightClick(world, player, hand);
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
        if (entityLiving instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entityLiving;
            if (!player.isSneaking() && getActivated(stack)) {
                int i = this.getMaxItemUseDuration(stack) - timeLeft;
                BoundToolEvent.Release event = new BoundToolEvent.Release(player, stack, i);
                if (MinecraftForge.EVENT_BUS.post(event))
                    return;

                i = event.charge;

                onBoundRelease(stack, worldIn, player, Math.min(i, MAX_CHARGE_TIME));
                if (worldIn.isRemote)
                    setCharge(stack, 0);
            }
        }
    }

    protected void onBoundRelease(ItemStack stack, World world, EntityPlayer player, int charge) {

    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase entityLiving) {
        return stack;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.BOW;
    }

    @Override
    public int getItemEnchantability() {
        return 50;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
        if (TextHelper.canTranslate(tooltipBase + "desc"))
            tooltip.add(TextHelper.localizeEffect(tooltipBase + "desc"));

        tooltip.add(TextHelper.localize("tooltip.bloodmagic." + (getActivated(stack) ? "activated" : "deactivated")));

        if (!stack.hasTagCompound())
            return;

        Binding binding = getBinding(stack);
        if (binding != null)
            tooltip.add(TextHelper.localizeEffect("tooltip.bloodmagic.currentOwner", binding.getOwnerName()));

        super.addInformation(stack, world, tooltip, flag);
    }

    @Override
    public Set<String> getToolClasses(ItemStack stack) {
        return ImmutableSet.of(name);
    }

    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot) {
        return ArrayListMultimap.create(); // No-op
    }

    public String getTooltipBase() {
        return tooltipBase;
    }

    public String getName() {
        return name;
    }

    protected static void dropStacks(Multiset<ItemStack> drops, World world, BlockPos posToDrop) {
        for (Multiset.Entry<ItemStack> entry : drops.entrySet()) {
            int count = entry.getCount();
            ItemStack stack = entry.getElement();
            int maxStackSize = stack.getItem().getItemStackLimit(stack);

            while (count >= maxStackSize) {
                ItemStack s = ItemHandlerHelper.copyStackWithSize(stack, maxStackSize);
                world.spawnEntity(new EntityItem(world, posToDrop.getX(), posToDrop.getY(), posToDrop.getZ(), s));
                count -= maxStackSize;
            }

            if (count > 0) {
                ItemStack s = ItemHandlerHelper.copyStackWithSize(stack, count);
                world.spawnEntity(new EntityItem(world, posToDrop.getX(), posToDrop.getY(), posToDrop.getZ(), s));
            }
        }
    }

    protected int getCharge(ItemStack stack) {
        return !stack.isEmpty() && stack.hasTagCompound() ? stack.getTagCompound().getInteger(Constants.NBT.CHARGE) : 0;
    }

    protected void setCharge(ItemStack stack, int chargeTime) {
        if (!stack.isEmpty()) {
            if (!stack.hasTagCompound())
                stack.setTagCompound(new NBTTagCompound());

            stack.getTagCompound().setInteger(Constants.NBT.CHARGE, chargeTime);
        }
    }
}