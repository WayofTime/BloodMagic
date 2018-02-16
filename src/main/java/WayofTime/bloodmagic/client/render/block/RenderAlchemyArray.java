package WayofTime.bloodmagic.client.render.block;

import WayofTime.bloodmagic.api.impl.BloodMagicAPI;
import WayofTime.bloodmagic.api.impl.recipe.RecipeAlchemyArray;
import WayofTime.bloodmagic.alchemyArray.AlchemyCircleRenderer;
import WayofTime.bloodmagic.core.registry.AlchemyArrayRecipeRegistry;
import WayofTime.bloodmagic.tile.TileAlchemyArray;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;

public class RenderAlchemyArray extends TileEntitySpecialRenderer<TileAlchemyArray> {
    @Override
    public void render(TileAlchemyArray alchemyArray, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        ItemStack inputStack = alchemyArray.getStackInSlot(0);
        ItemStack catalystStack = alchemyArray.getStackInSlot(1);
        int craftTime = alchemyArray.activeCounter;
        AlchemyCircleRenderer renderer = AlchemyArrayRecipeRegistry.getAlchemyCircleRenderer(inputStack, catalystStack);
        if (renderer == AlchemyArrayRecipeRegistry.DEFAULT_RENDERER) {
            RecipeAlchemyArray recipe = BloodMagicAPI.INSTANCE.getRecipeRegistrar().getAlchemyArray(inputStack, catalystStack);
            if (recipe != null)
                renderer = new AlchemyCircleRenderer(recipe.getCircleTexture());
        }

        renderer.renderAt(alchemyArray, x, y, z, (craftTime > 0 ? craftTime + partialTicks : 0));
    }
}
