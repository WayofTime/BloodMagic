package WayofTime.bloodmagic.item;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.registry.ModBlocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBucket;

public class ItemBucketEssence extends ItemBucket
{
    public ItemBucketEssence()
    {
        super(ModBlocks.lifeEssence);

        setUnlocalizedName(Constants.Mod.MODID + ".bucket.lifeEssence");
        setContainerItem(Items.BUCKET);
        setCreativeTab(BloodMagic.tabBloodMagic);
    }
}
