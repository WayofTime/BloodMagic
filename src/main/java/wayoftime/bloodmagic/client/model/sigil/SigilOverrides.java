package wayoftime.bloodmagic.client.model.sigil;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import wayoftime.bloodmagic.api.BMIdentifiers;
import wayoftime.bloodmagic.common.datacomponent.BMDataComponents;

public class SigilOverrides extends ItemOverrides {

    @Override
    public @Nullable BakedModel resolve(BakedModel model, ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed) {
        BakedModel endModel = Minecraft.getInstance().getModelManager().getMissingModel();
        if (stack.has(BMDataComponents.SIGIL_TYPE)) {
            ModelResourceLocation modelLoc = BMIdentifiers.ModelLocations.fromSigilKey(stack.get(BMDataComponents.SIGIL_TYPE));
            endModel = Minecraft.getInstance().getModelManager().getModel(modelLoc);
        }

        return endModel;
    }
}
