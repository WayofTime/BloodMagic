package WayofTime.bloodmagic.item;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.iface.IUpgradeTrainer;
import WayofTime.bloodmagic.livingArmour.LivingArmourHandler;
import WayofTime.bloodmagic.livingArmour.LivingArmourUpgrade;
import WayofTime.bloodmagic.util.helper.ItemHelper.LivingUpgrades;
import WayofTime.bloodmagic.client.IVariantProvider;
import WayofTime.bloodmagic.util.helper.TextHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public class ItemUpgradeTrainer extends Item implements IUpgradeTrainer, IVariantProvider {
    public ItemUpgradeTrainer() {
        super();

        setCreativeTab(BloodMagic.TAB_TOMES);
        setUnlocalizedName(BloodMagic.MODID + ".upgradeTrainer");
        setHasSubtypes(true);
        setMaxStackSize(1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(CreativeTabs creativeTab, NonNullList<ItemStack> list) {
        if (!isInCreativeTab(creativeTab))
            return;

        list.add(new ItemStack(this));
        for (Entry<String, Integer> entry : LivingArmourHandler.upgradeMaxLevelMap.entrySet()) {
            String key = entry.getKey();
            ItemStack stack = new ItemStack(this);
            LivingUpgrades.setKey(stack, key);
            list.add(stack);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
//        tooltip.addAll(Arrays.asList(TextHelper.cutLongString(TextHelper.localizeEffect("tooltip.bloodmagic.livingArmour"))));
        if (!stack.hasTagCompound())
            return;
        LivingArmourUpgrade upgrade = LivingUpgrades.getUpgrade(stack);
        if (upgrade != null) {
            tooltip.add(TextHelper.localize(upgrade.getUnlocalizedName()));
        }
    }

    @Override
    public List<String> getTrainedUpgrades(ItemStack stack) {
        List<String> keyList = new ArrayList<>();
        String key = LivingUpgrades.getKey(stack);

        if (!key.isEmpty()) {
            keyList.add(key);
        }

        return keyList;
    }

    @Override
    public boolean setTrainedUpgrades(ItemStack stack, List<String> keys) {
        if (keys.isEmpty()) {
            return false;
        }

        LivingUpgrades.setKey(stack, keys.get(0));
        return true;
    }

    @Override
    public List<Pair<Integer, String>> getVariants() {
        List<Pair<Integer, String>> ret = new ArrayList<>();
        ret.add(new ImmutablePair<>(0, "type=upgradetrainer"));
        return ret;
    }
}
