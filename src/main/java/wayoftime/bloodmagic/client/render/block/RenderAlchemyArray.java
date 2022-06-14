package wayoftime.bloodmagic.client.render.block;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import wayoftime.bloodmagic.client.render.alchemyarray.AlchemyArrayRenderer;
import wayoftime.bloodmagic.core.registry.AlchemyArrayRendererRegistry;
import wayoftime.bloodmagic.tile.TileAlchemyArray;

public class RenderAlchemyArray extends TileEntityRenderer<TileAlchemyArray>
{
	public static final AlchemyArrayRenderer arrayRenderer = new AlchemyArrayRenderer();

	public RenderAlchemyArray(TileEntityRendererDispatcher rendererDispatcherIn)
	{
		super(rendererDispatcherIn);
	}

	@Override
	public void render(TileAlchemyArray tileArray, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLightIn, int combinedOverlayIn)
	{
		ItemStack inputStack = tileArray.getItem(0);
		ItemStack catalystStack = tileArray.getItem(1);
//		arrayRenderer.renderAt(tileArray, 0, 0, 0, tileArray.activeCounter
//				+ partialTicks, matrixStack, buffer, combinedLightIn, combinedOverlayIn);

		AlchemyArrayRenderer renderer = AlchemyArrayRendererRegistry.getRenderer(tileArray.getLevel(), inputStack, catalystStack);
		if (renderer == null)
		{
			renderer = AlchemyArrayRendererRegistry.DEFAULT_RENDERER;
		}

		renderer.renderAt(tileArray, 0, 0, 0, tileArray.activeCounter
				+ partialTicks, matrixStack, buffer, combinedLightIn, combinedOverlayIn);
//		arrayRenderer.renderAt(tileArray, 0, 0, 0, 0, matrixStack, buffer, combinedLightIn, combinedOverlayIn);

//		if (tileAltar.getCurrentTierDisplayed() != AltarTier.ONE)
//			renderHologram(tileAltar, tileAltar.getCurrentTierDisplayed(), partialTicks);
	}
}
