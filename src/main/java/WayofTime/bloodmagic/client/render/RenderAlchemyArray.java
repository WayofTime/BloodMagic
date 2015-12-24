package WayofTime.bloodmagic.client.render;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import WayofTime.bloodmagic.api.alchemyCrafting.AlchemyCircleRenderer;
import WayofTime.bloodmagic.api.registry.AlchemyArrayRecipeRegistry;
import WayofTime.bloodmagic.tile.TileAlchemyArray;

public class RenderAlchemyArray extends TileEntitySpecialRenderer {
	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float partialTicks, int destroyStage) {
		if (tileEntity instanceof TileAlchemyArray) {
			ItemStack inputStack = ((TileAlchemyArray)tileEntity).getStackInSlot(0);

			AlchemyCircleRenderer renderer = AlchemyArrayRecipeRegistry.getAlchemyCircleRenderer(inputStack);

			renderer.renderAt(tileEntity, x, y, z);
		}
	}
}
