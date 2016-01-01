package WayofTime.bloodmagic.client.render;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import WayofTime.bloodmagic.api.alchemyCrafting.AlchemyCircleRenderer;
import WayofTime.bloodmagic.api.registry.AlchemyArrayRecipeRegistry;
import WayofTime.bloodmagic.tile.TileAlchemyArray;

public class RenderAlchemyArray extends TileEntitySpecialRenderer<TileAlchemyArray>
{
    @Override
    public void renderTileEntityAt(TileAlchemyArray alchemyArray, double x, double y, double z, float partialTicks, int destroyStage)
    {
        ItemStack inputStack = alchemyArray.getStackInSlot(0);
        int craftTime = alchemyArray.activeCounter;
        AlchemyCircleRenderer renderer = AlchemyArrayRecipeRegistry.getAlchemyCircleRenderer(inputStack);

        renderer.renderAt(alchemyArray, x, y, z, (craftTime > 0 ? craftTime + partialTicks : 0));
    }
}
