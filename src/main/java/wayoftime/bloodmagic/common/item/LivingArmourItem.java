package wayoftime.bloodmagic.common.item;

import net.minecraft.world.item.ArmorItem;
import wayoftime.bloodmagic.common.datacomponent.BMDataComponents;
import wayoftime.bloodmagic.common.tag.BMTags;

public class LivingArmourItem extends ArmorItem implements UpgradeHolderBase {

    public LivingArmourItem() {
        super(
                BMMaterialsAndTiers.LIVING_ARMOUR_MATERIAL,
                Type.CHESTPLATE,
                new Properties()
                        .durability(Type.CHESTPLATE.getDurability(33))
                        .component(BMDataComponents.REQUIRED_SET, BMTags.Items.LIVING_SET)
                        .component(BMDataComponents.CURRENT_UPGRADE_POINTS, 0)
                );
    }
}
