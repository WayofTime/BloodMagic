package wayoftime.bloodmagic.client.render.block;

import com.mojang.blaze3d.vertex.PoseStack;

import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import wayoftime.bloodmagic.common.tile.TileDemonCrucible;

public class RenderDemonCrucible implements BlockEntityRenderer<TileDemonCrucible>
{
	public RenderDemonCrucible(BlockEntityRendererProvider.Context context)
	{

	}

	@Override
	public void render(TileDemonCrucible tileAltar, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int combinedLightIn, int combinedOverlayIn)
	{
		ItemStack inputStack = tileAltar.getItem(0);

		this.renderItem(inputStack, tileAltar, matrixStack, buffer, combinedLightIn, combinedOverlayIn);
	}

	private void renderItem(ItemStack stack, TileDemonCrucible tileAltar, PoseStack matrixStack, MultiBufferSource buffer, int combinedLightIn, int combinedOverlayIn)
	{
		matrixStack.pushPose();
		Minecraft mc = Minecraft.getInstance();
		ItemRenderer itemRenderer = mc.getItemRenderer();
		if (!stack.isEmpty())
		{
			matrixStack.translate(0.5, 1.5, 0.5);
			matrixStack.pushPose();

			float rotation = (float) (720.0 * (System.currentTimeMillis() & 0x3FFFL) / 0x3FFFL);

			matrixStack.mulPose(Axis.YP.rotationDegrees(rotation));
			matrixStack.scale(0.5F, 0.5F, 0.5F);
//			Lighting.turnBackOn();
			BakedModel ibakedmodel = itemRenderer.getModel(stack, tileAltar.getLevel(), (LivingEntity) null, 1);
			itemRenderer.render(stack, ItemDisplayContext.FIXED, true, matrixStack, buffer, combinedLightIn, combinedOverlayIn, ibakedmodel); // renderItem
//			Lighting.turnOff();

			matrixStack.popPose();
		}

		matrixStack.popPose();
	}
}
