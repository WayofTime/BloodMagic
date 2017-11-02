package WayofTime.bloodmagic.item;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.ItemStackWrapper;
import WayofTime.bloodmagic.api.event.BoundToolEvent;
import WayofTime.bloodmagic.api.iface.IActivatable;
import WayofTime.bloodmagic.api.iface.IBindable;
import WayofTime.bloodmagic.api.util.helper.NBTHelper;
import WayofTime.bloodmagic.api.util.helper.NetworkHelper;
import WayofTime.bloodmagic.api.util.helper.PlayerHelper;
import WayofTime.bloodmagic.client.IMeshProvider;
import WayofTime.bloodmagic.client.mesh.CustomMeshDefinitionActivatable;
import WayofTime.bloodmagic.core.RegistrarBloodMagicItems;
import WayofTime.bloodmagic.util.Utils;
import WayofTime.bloodmagic.util.helper.TextHelper;
import com.google.common.base.Strings;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multiset;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ItemBoundTool extends ItemTool implements IBindable, IActivatable, IMeshProvider {
    public final int chargeTime = 30;
    protected final String tooltipBase;
    private final String name;
    public Map<ItemStack, Boolean> heldDownMap = new HashMap<ItemStack, Boolean>();
    public Map<ItemStack, Integer> heldDownCountMap = new HashMap<ItemStack, Integer>();

    public ItemBoundTool(String name, float damage, Set<Block> effectiveBlocks) {
        super(damage, 1, RegistrarBloodMagicItems.BOUND_TOOL_MATERIAL, effectiveBlocks);
        setUnlocalizedName(BloodMagic.MODID + ".bound." + name);
        setCreativeTab(BloodMagic.TAB_BM);
        setHarvestLevel(name, 4);

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
        if (Strings.isNullOrEmpty(getOwnerUUID(stack))) {
            setActivatedState(stack, false);
            return;
        }

        if (entity instanceof EntityPlayer && getActivated(stack) && isSelected && getBeingHeldDown(stack) && stack == ((EntityPlayer) entity).getActiveItemStack()) {
            EntityPlayer player = (EntityPlayer) entity;
            setHeldDownCount(stack, Math.min(player.getItemInUseCount(), chargeTime));
        } else if (!isSelected) {
            setBeingHeldDown(stack, false);
        }

        if (entity instanceof EntityPlayer && getActivated(stack) && world.getTotalWorldTime() % 80 == 0)
            NetworkHelper.getSoulNetwork(getOwnerUUID(stack)).syphonAndDamage((EntityPlayer) entity, 20);
    }

    protected int getHeldDownCount(ItemStack stack) {
        if (!heldDownCountMap.containsKey(stack))
            return 0;

        return heldDownCountMap.get(stack);
    }

    protected void setHeldDownCount(ItemStack stack, int count) {
        heldDownCountMap.put(stack, count);
    }

    protected boolean getBeingHeldDown(ItemStack stack) {
        if (!heldDownMap.containsKey(stack))
            return false;

        return heldDownMap.get(stack);
    }

    protected void setBeingHeldDown(ItemStack stack, boolean heldDown) {
        heldDownMap.put(stack, heldDown);
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

                onBoundRelease(stack, worldIn, player, Math.min(i, chargeTime));
                setBeingHeldDown(stack, false);
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

        if (!Strings.isNullOrEmpty(getOwnerUUID(stack)))
            tooltip.add(TextHelper.localizeEffect("tooltip.bloodmagic.currentOwner", PlayerHelper.getUsernameFromStack(stack)));

        super.addInformation(stack, world, tooltip, flag);
    }

    @Override
    public Set<String> getToolClasses(ItemStack stack) {
        return ImmutableSet.of(name);
    }

    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot) {
        return ArrayListMultimap.create(); // No-op
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return getActivated(stack) && getBeingHeldDown(stack);
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        return ((double) -Math.min(getHeldDownCount(stack), chargeTime) / chargeTime) + 1;
    }

    @Override
    public String getOwnerName(ItemStack stack) {
        return stack != null ? NBTHelper.checkNBT(stack).getTagCompound().getString(Constants.NBT.OWNER_NAME) : null;
    }

    // IBindable

    @Override
    public String getOwnerUUID(ItemStack stack) {
        return stack != null ? NBTHelper.checkNBT(stack).getTagCompound().getString(Constants.NBT.OWNER_UUID) : null;
    }

    @Override
    public boolean onBind(EntityPlayer player, ItemStack stack) {
        return true;
    }

    @Override
    public boolean getActivated(ItemStack stack) {
        return stack != null && NBTHelper.checkNBT(stack).getTagCompound().getBoolean(Constants.NBT.ACTIVATED);
    }

    // IActivatable

    @Override
    public ItemStack setActivatedState(ItemStack stack, boolean activated) {
        if (stack != null) {
            NBTHelper.checkNBT(stack).getTagCompound().setBoolean(Constants.NBT.ACTIVATED, activated);
            return stack;
        }

        return null;
    }

    // IMeshProvider

    @Override
    @SideOnly(Side.CLIENT)
    public ItemMeshDefinition getMeshDefinition() {
        return new CustomMeshDefinitionActivatable("bound_" + name);
    }

    @Override
    public void populateVariants(List<String> variants) {
        variants.add("active=true");
        variants.add("active=false");
    }

    public String getTooltipBase() {
        return tooltipBase;
    }

    public String getName() {
        return name;
    }

    public Map<ItemStack, Boolean> getHeldDownMap() {
        return heldDownMap;
    }

    public Map<ItemStack, Integer> getHeldDownCountMap() {
        return heldDownCountMap;
    }

    public int getChargeTime() {
        return chargeTime;
    }

    protected static void dropStacks(Multiset<ItemStackWrapper> drops, World world, BlockPos posToDrop) {
        for (Multiset.Entry<ItemStackWrapper> entry : drops.entrySet()) {
            int count = entry.getCount();
            ItemStackWrapper stack = entry.getElement();
            int maxStackSize = stack.item.getItemStackLimit(stack.toStack(1));

            while (count >= maxStackSize) {
                world.spawnEntity(new EntityItem(world, posToDrop.getX(), posToDrop.getY(), posToDrop.getZ(), stack.toStack(maxStackSize)));
                count -= maxStackSize;
            }

            if (count > 0)
                world.spawnEntity(new EntityItem(world, posToDrop.getX(), posToDrop.getY(), posToDrop.getZ(), stack.toStack(count)));
        }
    }
}
