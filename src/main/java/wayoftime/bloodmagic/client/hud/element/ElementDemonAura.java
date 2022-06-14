package wayoftime.bloodmagic.client.hud.element;

import java.util.List;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.util.Utils;
import wayoftime.bloodmagic.util.handler.event.ClientHandler;
import wayoftime.bloodmagic.api.compat.EnumDemonWillType;

public class ElementDemonAura extends HUDElement
{

	private static final ResourceLocation BAR_LOCATION = new ResourceLocation(BloodMagic.MODID, "textures/hud/bars.png");

	private final List<EnumDemonWillType> orderedTypes = Lists.newArrayList(EnumDemonWillType.DEFAULT, EnumDemonWillType.CORROSIVE, EnumDemonWillType.STEADFAST, EnumDemonWillType.DESTRUCTIVE, EnumDemonWillType.VENGEFUL);

	public ElementDemonAura()
	{
		super(80, 46);
	}

	@Override
	public void draw(MatrixStack matrixStack, float partialTicks, int drawX, int drawY)
	{
		Minecraft minecraft = Minecraft.getInstance();
		PlayerEntity player = minecraft.player;

		minecraft.getTextureManager().bind(BAR_LOCATION);
//		GlStateManager.color(1.0F, 1.0F, 1.0F);
		this.blit(matrixStack, drawX, drawY, 0, 210, 80, 46);

		double maxAmount = Utils.getDemonWillResolution(player);

		int i = 0;
		for (EnumDemonWillType type : orderedTypes)
		{
			i++;
//			GlStateManager.color(1.0F, 1.0F, 1.0F);
			minecraft.getTextureManager().bind(BAR_LOCATION);
			int textureXOffset = (i > 3) ? (i - 3) : (3 - i);
			int maxBarSize = 30 - 2 * textureXOffset;

			double amount = ClientHandler.currentAura == null ? 0 : ClientHandler.currentAura.getWill(type);
			double ratio = Math.max(Math.min(amount / maxAmount, 1), 0);

//			double amount = 50;
//			double ratio = 0.5;

			double width = maxBarSize * ratio * 2;
			double height = 2;
			double x = drawX + 2 * textureXOffset + 10;
			double y = drawY + 4 * i + 10;

			double textureX = 2 * textureXOffset + 2 * 42;
			double textureY = 4 * i + 220;

			this.blit(matrixStack, (int) x, (int) y, (int) textureX, (int) textureY, (int) width, (int) height);

			if (player.isShiftKeyDown())
			{
				matrixStack.pushPose();
				matrixStack.translate(x - 2 * textureXOffset + 70, (y - 2), 0);
				matrixStack.scale(0.5f, 0.5f, 1f);
				minecraft.font.drawShadow(matrixStack, String.valueOf((int) amount), 0, 2, 0xffffffff);
				RenderSystem.clearTexGen();
				matrixStack.popPose();
			}
		}
	}

	@Override
	public boolean shouldRender(Minecraft minecraft)
	{
		return Utils.canPlayerSeeDemonWill(Minecraft.getInstance().player);
	}
}