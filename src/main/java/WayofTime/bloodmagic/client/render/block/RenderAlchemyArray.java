package WayofTime.bloodmagic.client.render.block;

import WayofTime.bloodmagic.apibutnotreally.alchemyCrafting.AlchemyCircleRenderer;
import WayofTime.bloodmagic.apibutnotreally.registry.AlchemyArrayRecipeRegistry;
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

        renderer.renderAt(alchemyArray, x, y, z, (craftTime > 0 ? craftTime + partialTicks : 0));
    }
}
