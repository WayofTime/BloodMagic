package com.wayoftime.bloodmagic.item.sigil;

import com.wayoftime.bloodmagic.BloodMagic;
import com.wayoftime.bloodmagic.core.network.Binding;
import com.wayoftime.bloodmagic.core.network.SoulNetwork;
import com.wayoftime.bloodmagic.core.network.NetworkInteraction;
import com.wayoftime.bloodmagic.core.util.BooleanResult;
import com.wayoftime.bloodmagic.core.util.register.IVariantProvider;
import com.wayoftime.bloodmagic.item.IBindable;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

/*
 * TODO - See checklist
 * - [-] Sigils
 *  - [x] Divination
 *  - [x] Air
 *  - [x] Fast Miner
 *  - [-] Haste
 *  - [ ] Water
 *  - [ ] Lava
 *  - [ ] Void
 *  - [ ] Green Grove
 *  - [ ] Blood Light
 *  - [ ] Elemental Affinity
 *  - [ ] Magnetism
 *  - [ ] Suppression
 *  - [ ] Seer
 *   - Perhaps this could be modified to show network interaction history instead of additional altar data?
 *  - [ ] Ender Severance
 *  - [ ] Whirlwind
 *  - [ ] Phantom Bridge
 *  - [ ] Compression
 *  - [ ] Holding
 *  - [ ] Teleposition
 *  - [ ] Transposition
 *  - [ ] Claw
 *  - [ ] Bounce
 *  - [ ] Frost
 */
public class ItemSigil extends Item implements IBindable, IVariantProvider {

    private final ISigil sigil;

    public ItemSigil(ISigil sigil, String name) {
        this.sigil = sigil;

        setCreativeTab(BloodMagic.TAB_BM);
        setTranslationKey(BloodMagic.MODID + ":sigil_" + name);
        setRegistryName("sigil_" + name);
        setMaxStackSize(1);

        if (sigil instanceof ISigil.Toggle) {
            addPropertyOverride(new ResourceLocation(BloodMagic.MODID, "toggled"), new IItemPropertyGetter() {
                @SideOnly(Side.CLIENT)
                @Override
                public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {
                    return isActive(stack) ? 1.0F : 0.0F;
                }
            });
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        if (player instanceof FakePlayer)
            return ActionResult.newResult(EnumActionResult.FAIL, player.getHeldItem(hand));

        ItemStack sigilStack = getSigilStack(player, hand);
        Binding binding = getBinding(sigilStack);
        if (binding == null || binding.getOwnerId() == null)
            return ActionResult.newResult(EnumActionResult.FAIL, player.getHeldItem(hand));

        if (sigil instanceof ISigil.Toggle && player.isSneaking()) {
            boolean newState = toggleState(sigilStack);
            ((ISigil.Toggle) sigil).onToggle(newState, sigilStack, player, world, hand, binding);
            return ActionResult.newResult(EnumActionResult.SUCCESS, player.getHeldItem(hand));
        }

        EnumActionResult result = sigil.onRightClick(sigilStack, player, world, hand, binding);
        if (result == EnumActionResult.SUCCESS) {
            BooleanResult<Integer> syphonResult = SoulNetwork.get(binding.getOwnerId()).submitInteraction(NetworkInteraction.asItemInfo(sigilStack, world, player, sigil.getCost()).syphon());
            if (!syphonResult.isSuccess())
                player.attackEntityFrom(SoulNetwork.WEAK_SOUL, 2.0F);
        }

        return ActionResult.newResult(result, player.getHeldItem(hand));
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (player instanceof FakePlayer)
            return EnumActionResult.FAIL;

        ItemStack stack = getSigilStack(player, hand);
        Binding binding = getBinding(stack);
        if (binding == null)
            return EnumActionResult.FAIL;

        return sigil.onInteract(stack, player, world, pos, facing, hand, binding);
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
        if (!(entity instanceof EntityPlayer) || entity instanceof FakePlayer)
            return;

        ItemStack sigilStack = getSigilStack(stack);
        if (sigil instanceof ISigil.Toggle) {
            Binding binding = getBinding(sigilStack);
            if (binding != null && isActive(sigilStack)) {
                ((ISigil.Toggle) sigil).onUpdate(sigilStack, (EntityPlayer) entity, world, itemSlot, isSelected, binding);
                if (world.getTotalWorldTime() % 100 == 0) {
                    BooleanResult<Integer> result = SoulNetwork.get(binding.getOwnerId()).submitInteraction(NetworkInteraction.asItemInfo(sigilStack, world, entity, sigil.getCost()).syphon());
                    if (!result.isSuccess())
                        entity.attackEntityFrom(SoulNetwork.WEAK_SOUL, 1.0F);
                }
            }
        }
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        IBindable.appendTooltip(getBinding(stack), tooltip);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return slotChanged;
    }

    public ISigil getSigil() {
        return sigil;
    }

    public ItemStack getSigilStack(EntityPlayer player, EnumHand hand) {
        return getSigilStack(player.getHeldItem(hand));
    }

    public ItemStack getSigilStack(ItemStack sigilStack) {
        if (sigil instanceof ISigil.Holding) {
            ISigil.Holding holding = (ISigil.Holding) sigil;
            int current = holding.getEquippedSigil(sigilStack);
            return holding.getHeldSigils(sigilStack).get(current);
        }

        return sigilStack;
    }

    public boolean toggleState(ItemStack stack) {
        if (!stack.hasTagCompound())
            return false;

        boolean newState = !isActive(stack);
        stack.getTagCompound().setBoolean("active", newState);
        return newState;
    }

    public boolean isActive(ItemStack stack) {
        return stack.hasTagCompound() && stack.getTagCompound().getBoolean("active");
    }
}
