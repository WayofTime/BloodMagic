package WayofTime.alchemicalWizardry.common.items.potion;

import net.minecraft.item.Item;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.alchemy.ICombinationalCatalyst;

public class CombinationalCatalyst extends Item implements ICombinationalCatalyst
{
    public CombinationalCatalyst()
    {
        super();
        this.setCreativeTab(AlchemicalWizardry.tabBloodMagic);
    }

}
