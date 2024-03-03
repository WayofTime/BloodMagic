package wayoftime.bloodmagic.compat;

import java.util.List;

import net.minecraft.world.level.block.CropBlock;

import wayoftime.bloodmagic.impl.BloodMagicAPI;
import wayoftime.bloodmagic.ritual.harvest.HarvestRegistry;
import com.blakebr0.mysticalagriculture.api.MysticalAgricultureAPI;
import com.blakebr0.mysticalagriculture.api.registry.ICropRegistry;
import com.blakebr0.mysticalagriculture.api.crop.Crop;

public class MysticalAgricultureCompat 
{
    public MysticalAgricultureCompat()
    {
        ICropRegistry registry = MysticalAgricultureAPI.getCropRegistry();
        List<Crop> crops = registry.getCrops();

        HarvestRegistry api = BloodMagicAPI.INSTANCE.getHarvestRegistry();
        for (Crop maCrop : crops)
        {
            CropBlock crop = (CropBlock) maCrop.getCropBlock();
            api.registerStandardCrop(crop, crop.getMaxAge());
        }
    }
}
