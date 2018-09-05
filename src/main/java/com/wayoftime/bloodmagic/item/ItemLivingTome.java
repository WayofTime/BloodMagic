package com.wayoftime.bloodmagic.item;

import com.wayoftime.bloodmagic.core.RegistrarBloodMagicLivingArmor;
import com.wayoftime.bloodmagic.core.living.ILivingContainer;
import com.wayoftime.bloodmagic.core.living.LivingStats;
import com.wayoftime.bloodmagic.core.living.LivingUtil;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemLivingTome extends ItemMundane implements ILivingContainer {

    public ItemLivingTome() {
        super("living_tome");
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand hand) {
        ItemStack held = player.getHeldItem(hand);

        LivingStats armorStats = LivingStats.fromPlayer(player, true);
        if (armorStats == null)
            return ActionResult.newResult(EnumActionResult.PASS, held);

        LivingStats tomeStats = getLivingStats(held);
        if (tomeStats == null)
            return ActionResult.newResult(EnumActionResult.PASS, held);

        tomeStats.getUpgrades().forEach((k, v) -> LivingUtil.applyNewExperience(player, k, v));
//        LivingStats.toPlayer(player, armorStats);
        held.shrink(1);
        return ActionResult.newResult(EnumActionResult.SUCCESS, held);
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (!isInCreativeTab(tab))
            return;

        RegistrarBloodMagicLivingArmor.UPGRADES.values().forEach(upgrade -> {
            int exp = 0;
            while ((exp = upgrade.getNextRequirement(exp)) != 0) {
                ItemStack tome = new ItemStack(this);
                updateLivingStates(tome, new LivingStats().setMaxPoints(upgrade.getLevelCost(exp)).addExperience(upgrade.getKey(), exp));
                items.add(tome);
            }
        });
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        ILivingContainer.appendLivingTooltip(getLivingStats(stack), tooltip, false);
    }
}
