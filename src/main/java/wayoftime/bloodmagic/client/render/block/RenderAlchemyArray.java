package wayoftime.bloodmagic.client.render.block;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.world.item.ItemStack;
import wayoftime.bloodmagic.client.render.alchemyarray.AlchemyArrayRenderer;
import wayoftime.bloodmagic.core.registry.AlchemyArrayRendererRegistry;
import wayoftime.bloodmagic.tile.TileAlchemyArray;

public class RenderAlchemyArray extends BlockEntityRenderer<TileAlchemyArray>
{
	public static final AlchemyArrayRenderer arrayRenderer = new AlchemyArrayRenderer();

	public RenderAlchemyArray(BlockEntityRenderDispatcher rendererDispatcherIn)
	{
		super(rendererDispatcherIn);
	}

	@Override
	public void render(TileAlchemyArray tileArray, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int combinedLightIn, int combinedOverlayIn)
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
