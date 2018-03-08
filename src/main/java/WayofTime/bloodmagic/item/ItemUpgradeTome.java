package WayofTime.bloodmagic.item;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.client.IVariantProvider;
import WayofTime.bloodmagic.item.armour.ItemLivingArmour;
import WayofTime.bloodmagic.livingArmour.LivingArmour;
import WayofTime.bloodmagic.livingArmour.LivingArmourHandler;
import WayofTime.bloodmagic.livingArmour.LivingArmourUpgrade;
import WayofTime.bloodmagic.util.helper.ItemHelper.LivingUpgrades;
import WayofTime.bloodmagic.util.helper.TextHelper;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map.Entry;

public class ItemUpgradeTome extends Item implements IVariantProvider {
    public ItemUpgradeTome() {
        super();

        setCreativeTab(BloodMagic.TAB_TOMES);
        setUnlocalizedName(BloodMagic.MODID + ".upgradeTome");
        setHasSubtypes(true);
        setMaxStackSize(1);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (world.isRemote) {
            return super.onItemRightClick(world, player, hand);
        }

        LivingArmourUpgrade upgrade = LivingUpgrades.getUpgrade(stack);
        if (upgrade == null) {
            return super.onItemRightClick(world, player, hand);
        }

        ItemStack chestStack = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
        if (chestStack.getItem() instanceof ItemLivingArmour) {
            LivingArmour armour = ItemLivingArmour.getLivingArmourFromStack(chestStack);
            if (armour == null) {
                return super.onItemRightClick(world, player, hand);
            }

            if (armour.upgradeArmour(player, upgrade)) {
                ItemLivingArmour.setLivingArmour(chestStack, armour);
//                ((ItemLivingArmour) chestStack.getItem()).setLivingArmour(stack, armour, false);
                stack.shrink(1);
            }
        }
        return super.onItemRightClick(world, player, hand);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        if (!stack.hasTagCompound())
            return super.getUnlocalizedName(stack);

        LivingArmourUpgrade upgrade = LivingUpgrades.getUpgrade(stack);
        if (upgrade != null && upgrade.isDowngrade())
            return "item." + BloodMagic.MODID + ".downgradeTome";

        return super.getUnlocalizedName(stack);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(CreativeTabs creativeTab, NonNullList<ItemStack> list) {
        if (!isInCreativeTab(creativeTab))
            return;

        for (Entry<String, Integer> entry : LivingArmourHandler.upgradeMaxLevelMap.entrySet()) {
            String key = entry.getKey();
            int maxLevel = entry.getValue();
            for (int i = 0; i < maxLevel; i++) {
                ItemStack stack = new ItemStack(this);
                LivingUpgrades.setKey(stack, key);
                LivingUpgrades.setLevel(stack, i);
                list.add(stack);
            }
        }
    }

    @Override
    public void gatherVariants(@Nonnull Int2ObjectMap<String> variants) {
        variants.put(0, "type=upgradetome");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
        if (!stack.hasTagCompound())
            return;
        LivingArmourUpgrade upgrade = LivingUpgrades.getUpgrade(stack);
        if (upgrade != null) {
            tooltip.add(TextHelper.localizeEffect("tooltip.bloodmagic.livingArmour.upgrade.level", TextHelper.localize(upgrade.getUnlocalizedName()), upgrade.getUpgradeLevel() + 1));
        }
    }
}
