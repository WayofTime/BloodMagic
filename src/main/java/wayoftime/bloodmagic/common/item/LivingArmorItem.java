package wayoftime.bloodmagic.common.item;

import net.minecraft.world.item.ArmorItem;
import wayoftime.bloodmagic.api.item.UpgradeHolderBase;
import wayoftime.bloodmagic.common.datacomponent.BMDataComponents;

public class LivingArmorItem extends ArmorItem implements UpgradeHolderBase {

    public LivingArmorItem() {
        super(
                BMMaterialsAndTiers.LIVING_ARMOR_MATERIAL,
                Type.CHESTPLATE,
                new Properties()
                        .durability(Type.CHESTPLATE.getDurability(33))
                        .component(BMDataComponents.IS_EVOLVED, false)
                );
    }
}
