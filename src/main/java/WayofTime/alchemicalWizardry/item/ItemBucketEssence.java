package WayofTime.alchemicalWizardry.item;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.registry.ModBlocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBucket;

public class ItemBucketEssence extends ItemBucket {

    public ItemBucketEssence() {
        super(ModBlocks.lifeEssence);

        setUnlocalizedName(AlchemicalWizardry.MODID + ".bucket.lifeEssence");
        setContainerItem(Items.bucket);
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
    }
}
