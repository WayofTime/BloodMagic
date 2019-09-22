package WayofTime.bloodmagic.item;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.client.IMeshProvider;
import WayofTime.bloodmagic.client.mesh.CustomMeshDefinitionActivatable;
import WayofTime.bloodmagic.core.RegistrarBloodMagicItems;
import WayofTime.bloodmagic.core.data.Binding;
import WayofTime.bloodmagic.core.data.SoulTicket;
import WayofTime.bloodmagic.iface.IActivatable;
import WayofTime.bloodmagic.iface.IBindable;
import WayofTime.bloodmagic.util.Utils;
import WayofTime.bloodmagic.util.helper.NetworkHelper;
import WayofTime.bloodmagic.util.helper.TextHelper;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemGroup;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.function.Consumer;

public class ItemBoundSword extends SwordItem implements IBindable, IActivatable, IMeshProvider {
    public ItemBoundSword() {
        super(RegistrarBloodMagicItems.BOUND_TOOL_MATERIAL);

        setTranslationKey(BloodMagic.MODID + ".bound.sword");
        setCreativeTab(BloodMagic.TAB_BM);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (player.isSneaking())
            setActivatedState(stack, !getActivated(stack));

//        if (!player.isSneaking() && getActivated(stack))
//        {
//            BoundToolEvent.Charge event = new BoundToolEvent.Charge(player, stack);
//            if (MinecraftForge.EVENT_BUS.post(event))
//                return new ActionResult<ItemStack>(EnumActionResult.FAIL, event.result);
//
//            player.setActiveHand(hand);
//            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
//        }

        return super.onItemRightClick(world, player, hand);
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
        Binding binding = getBinding(stack);
        if (binding == null) {
            setActivatedState(stack, false);
            return;
        }

        if (entity instanceof PlayerEntity && getActivated(stack) && world.getTotalWorldTime() % 80 == 0)
            NetworkHelper.getSoulNetwork(binding).syphonAndDamage((PlayerEntity) entity, SoulTicket.item(stack, world, entity, 20));
    }

    @Override
    public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        return true;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World world, BlockState block, BlockPos pos, LivingEntity entityLiving) {
        return true;
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return oldStack.getItem() != newStack.getItem();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(ItemGroup tab, NonNullList<ItemStack> subItems) {
        if (!isInCreativeTab(tab))
            return;

        subItems.add(Utils.setUnbreakable(new ItemStack(this)));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
        if (!stack.hasTagCompound())
            return;

        if (TextHelper.canTranslate("tooltip.bloodmagic.bound.sword.desc"))
            tooltip.add(TextHelper.localizeEffect("tooltip.bloodmagic.bound.sword.desc"));

        tooltip.add(TextHelper.localize("tooltip.bloodmagic." + (getActivated(stack) ? "activated" : "deactivated")));

        Binding binding = getBinding(stack);
        if (binding != null)
            tooltip.add(TextHelper.localizeEffect("tooltip.bloodmagic.currentOwner", binding.getOwnerName()));
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EquipmentSlotType equipmentSlot, ItemStack stack) {
        Multimap<String, AttributeModifier> multimap = HashMultimap.create();
        if (equipmentSlot == EquipmentSlotType.MAINHAND) {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", getActivated(stack) ? 8 : 2, 0));
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -2.4, 0));
        }
        return multimap;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ItemMeshDefinition getMeshDefinition() {
        return new CustomMeshDefinitionActivatable("bound_sword");
    }

    @Override
    public void gatherVariants(Consumer<String> variants) {
        variants.accept("active=true");
        variants.accept("active=false");
    }
}
