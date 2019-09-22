package WayofTime.bloodmagic.item;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.core.RegistrarBloodMagicItems;
import WayofTime.bloodmagic.core.data.Binding;
import WayofTime.bloodmagic.core.data.SoulTicket;
import WayofTime.bloodmagic.event.BoundToolEvent;
import WayofTime.bloodmagic.iface.IActivatable;
import WayofTime.bloodmagic.iface.IBindable;
import WayofTime.bloodmagic.util.Utils;
import WayofTime.bloodmagic.util.helper.NetworkHelper;
import WayofTime.bloodmagic.util.helper.TextHelper;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.ToolItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ItemBoundTool extends ToolItem implements IBindable, IActivatable {
    public final int chargeTime = 30;
    protected final String tooltipBase;
    private final String name;
    public Map<ItemStack, Boolean> heldDownMap = new HashMap<>();
    public Map<ItemStack, Integer> heldDownCountMap = new HashMap<>();

    public ItemBoundTool(String name, float damage, Set<Block> effectiveBlocks) {
        super(damage, 1, RegistrarBloodMagicItems.BOUND_TOOL_MATERIAL, effectiveBlocks);
        setTranslationKey(BloodMagic.MODID + ".bound." + name);
        setCreativeTab(BloodMagic.TAB_BM);
        setHarvestLevel(name, 4);

        this.name = name;
        this.tooltipBase = "tooltip.bloodmagic.bound." + name + ".";
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        return getActivated(stack) ? toolMaterial.getEfficiency() : 1.0F;
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return slotChanged;
    }

    @Override
    public void getSubItems(ItemGroup tab, NonNullList<ItemStack> subItems) {
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

        if (entity instanceof PlayerEntity && getActivated(stack) && isSelected && getBeingHeldDown(stack) && stack == ((PlayerEntity) entity).getActiveItemStack()) {
            PlayerEntity player = (PlayerEntity) entity;
            setHeldDownCount(stack, Math.min(player.getItemInUseCount(), chargeTime));
        } else if (!isSelected) {
            setBeingHeldDown(stack, false);
        }

        if (entity instanceof PlayerEntity && getActivated(stack) && world.getTotalWorldTime() % 80 == 0)
            NetworkHelper.getSoulNetwork(binding).syphonAndDamage((PlayerEntity) entity, SoulTicket.item(stack, world, entity, 20));
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
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (player.isSneaking())
            setActivatedState(stack, !getActivated(stack));

        if (!player.isSneaking() && getActivated(stack)) {
            BoundToolEvent.Charge event = new BoundToolEvent.Charge(player, stack);
            if (MinecraftForge.EVENT_BUS.post(event))
                return new ActionResult<>(ActionResultType.FAIL, event.result);

            player.setActiveHand(hand);
            return new ActionResult<>(ActionResultType.SUCCESS, stack);
        }

        return super.onItemRightClick(world, player, hand);
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {
        if (entityLiving instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entityLiving;
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

    protected void onBoundRelease(ItemStack stack, World world, PlayerEntity player, int charge) {

    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World world, LivingEntity entityLiving) {
        return stack;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public UseAction getItemUseAction(ItemStack stack) {
        return UseAction.BOW;
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

    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EquipmentSlotType equipmentSlot) {
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

    protected void sharedHarvest(ItemStack stack, World world, PlayerEntity player, BlockPos blockPos, BlockState blockState, boolean silkTouch, int fortuneLvl) {

        if (blockState.getBlockHardness(world, blockPos) != -1.0F) {
            float strengthVsBlock = getDestroySpeed(stack, blockState);

            if (strengthVsBlock > 1.1F && world.canMineBlockBody(player, blockPos)) {
                if (!player.isCreative())
                    if (silkTouch && blockState.getBlock().canSilkHarvest(world, blockPos, world.getBlockState(blockPos), player))
                        ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(Item.getItemFromBlock(blockState.getBlock())));
                    else {
                        NonNullList<ItemStack> itemDrops = NonNullList.create();
                        blockState.getBlock().getDrops(itemDrops, world, blockPos, world.getBlockState(blockPos), fortuneLvl);
                        for (ItemStack stacks : itemDrops)
                            ItemHandlerHelper.giveItemToPlayer(player, stacks);
                    }
                blockState.getBlock().removedByPlayer(world.getBlockState(blockPos), world, blockPos, player, false);
            }
        }
    }
}
