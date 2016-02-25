package WayofTime.bloodmagic.item;

import net.minecraft.init.Items;
import net.minecraft.item.ItemBucket;
import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.registry.ModBlocks;

public class ItemBucketEssence extends ItemBucket
{
    public ItemBucketEssence()
    {
        super(ModBlocks.lifeEssence);

        setUnlocalizedName(Constants.Mod.MODID + ".bucket.lifeEssence");
        setRegistryName(Constants.BloodMagicItem.BUCKET_ESSENCE.getRegName());
        setContainerItem(Items.bucket);
        setCreativeTab(BloodMagic.tabBloodMagic);
    }
}
