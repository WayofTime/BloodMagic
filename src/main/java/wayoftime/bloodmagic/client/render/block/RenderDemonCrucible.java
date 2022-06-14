package wayoftime.bloodmagic.client.render.block;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3f;
import wayoftime.bloodmagic.tile.TileDemonCrucible;

public class RenderDemonCrucible extends TileEntityRenderer<TileDemonCrucible>
{
	public RenderDemonCrucible(TileEntityRendererDispatcher rendererDispatcherIn)
	{
		super(rendererDispatcherIn);
	}

	@Override
	public void render(TileDemonCrucible tileAltar, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLightIn, int combinedOverlayIn)
	{
		ItemStack inputStack = tileAltar.getItem(0);

		this.renderItem(inputStack, tileAltar, matrixStack, buffer, combinedLightIn, combinedOverlayIn);
	}

	private void renderItem(ItemStack stack, TileDemonCrucible tileAltar, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLightIn, int combinedOverlayIn)
	{
		matrixStack.pushPose();
		Minecraft mc = Minecraft.getInstance();
		ItemRenderer itemRenderer = mc.getItemRenderer();
		if (!stack.isEmpty())
		{
			matrixStack.translate(0.5, 1.5, 0.5);
			matrixStack.pushPose();

			float rotation = (float) (720.0 * (System.currentTimeMillis() & 0x3FFFL) / 0x3FFFL);

			matrixStack.mulPose(Vector3f.YP.rotationDegrees(rotation));
			matrixStack.scale(0.5F, 0.5F, 0.5F);
			RenderHelper.turnBackOn();
			IBakedModel ibakedmodel = itemRenderer.getModel(stack, tileAltar.getLevel(), (LivingEntity) null);
			itemRenderer.render(stack, ItemCameraTransforms.TransformType.FIXED, true, matrixStack, buffer, combinedLightIn, combinedOverlayIn, ibakedmodel); // renderItem
			RenderHelper.turnOff();

			matrixStack.popPose();
		}

		matrixStack.popPose();
	}
}
