package com.wayoftime.bloodmagic.item;

import com.wayoftime.bloodmagic.core.network.*;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemBloodOrb extends ItemBindable implements IBloodOrb {

    private final BloodOrb orb;

    public ItemBloodOrb(BloodOrb orb) {
        super("blood_orb_" + orb.getName().getPath());

        this.orb = orb;

        setMaxStackSize(1);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack held = player.getHeldItem(hand);
        Binding binding = getBinding(held);
        if (binding != null) {
            SoulNetwork network = SoulNetwork.get(binding.getOwnerId());
            if (player.capabilities.isCreativeMode || player.attackEntityFrom(SoulNetwork.WEAK_SOUL, 1.0F))
                network.submitInteraction(NetworkInteraction.asItemInfo(held, world, player, 200));
        }

        return super.onItemRightClick(world, player, hand);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
        super.addInformation(stack, world, tooltip, flag);

        BloodOrb orb = getOrb(stack);
        if (orb != null && flag.isAdvanced())
            tooltip.add(I18n.format("tooltip.bloodmagic:object_owner", orb.getName().getNamespace()));
    }

    @Nullable
    @Override
    public BloodOrb getOrb(@Nonnull ItemStack stack) {
        return orb;
    }
}
