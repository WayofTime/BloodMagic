package WayofTime.bloodmagic.item;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.livingArmour.LivingArmourHandler;
import WayofTime.bloodmagic.api.livingArmour.LivingArmourUpgrade;
import WayofTime.bloodmagic.api.util.helper.ItemHelper.LivingUpgrades;
import WayofTime.bloodmagic.client.IVariantProvider;
import WayofTime.bloodmagic.item.armour.ItemLivingArmour;
import WayofTime.bloodmagic.livingArmour.LivingArmour;
import WayofTime.bloodmagic.util.helper.TextHelper;

public class ItemUpgradeTome extends Item implements IVariantProvider
{
    public ItemUpgradeTome()
    {
        super();

        setCreativeTab(BloodMagic.tabUpgradeTome);
        setUnlocalizedName(Constants.Mod.MODID + ".upgradeTome");
        setHasSubtypes(true);
        setMaxStackSize(1);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand)
    {
        if (world.isRemote)
        {
            return super.onItemRightClick(stack, world, player, hand);
        }

        LivingArmourUpgrade upgrade = LivingUpgrades.getUpgrade(stack);
        if (upgrade == null)
        {
            return super.onItemRightClick(stack, world, player, hand);
        }

        ItemStack chestStack = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
        if (chestStack != null && chestStack.getItem() instanceof ItemLivingArmour)
        {
            LivingArmour armour = ItemLivingArmour.getLivingArmourFromStack(chestStack);
            if (armour == null)
            {
                return super.onItemRightClick(stack, world, player, hand);
            }

            if (armour.upgradeArmour(player, upgrade))
            {
                ItemLivingArmour.setLivingArmour(chestStack, armour);
//                ((ItemLivingArmour) chestStack.getItem()).setLivingArmour(stack, armour, false);
                stack.stackSize--;
            }
        }
        return super.onItemRightClick(stack, world, player, hand);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        if (!stack.hasTagCompound())
            return super.getUnlocalizedName(stack);

        LivingArmourUpgrade upgrade = LivingUpgrades.getUpgrade(stack);
        if (upgrade != null && upgrade.isDowngrade())
            return "item." + Constants.Mod.MODID + ".downgradeTome";

        return super.getUnlocalizedName(stack);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item id, CreativeTabs creativeTab, List<ItemStack> list)
    {
        for (Entry<String, Integer> entry : LivingArmourHandler.upgradeMaxLevelMap.entrySet())
        {
            String key = entry.getKey();
            int maxLevel = entry.getValue();
            for (int i = 0; i < maxLevel; i++)
            {
                ItemStack stack = new ItemStack(this);
                LivingUpgrades.setKey(stack, key);
                LivingUpgrades.setLevel(stack, i);
                list.add(stack);
            }
        }
    }

    @Override
    public List<Pair<Integer, String>> getVariants()
    {
        List<Pair<Integer, String>> ret = new ArrayList<Pair<Integer, String>>();
        ret.add(new ImmutablePair<Integer, String>(0, "type=upgradetome"));
        return ret;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced)
    {
//        tooltip.addAll(Arrays.asList(TextHelper.cutLongString(TextHelper.localizeEffect("tooltip.BloodMagic.livingArmour"))));
        if (!stack.hasTagCompound())
            return;
        LivingArmourUpgrade upgrade = LivingUpgrades.getUpgrade(stack);
        if (upgrade != null)
        {
            tooltip.add(TextHelper.localizeEffect("tooltip.BloodMagic.livingArmour.upgrade.level", TextHelper.localize(upgrade.getUnlocalizedName()), upgrade.getUpgradeLevel() + 1));
        }
    }
}
