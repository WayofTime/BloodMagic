package wayoftime.bloodmagic.client.hud.element;

import java.awt.Color;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import wayoftime.bloodmagic.client.Sprite;

public abstract class ElementTileInformation<T extends TileEntity> extends HUDElement
{

	protected final Class<T> tileClass;
	private final List<Pair<Sprite, Function<T, String>>> information;

	public ElementTileInformation(int width, int lines, Class<T> tileClass)
	{
		super(width, 18 * lines - 2);

		this.tileClass = tileClass;
		this.information = Lists.newArrayList();
		gatherInformation(information::add);
	}

	public abstract void gatherInformation(Consumer<Pair<Sprite, Function<T, String>>> information);

	@SuppressWarnings("unchecked")
	@Override
	public void draw(MatrixStack matrixStack, float partialTicks, int drawX, int drawY)
	{
		RayTraceResult trace = Minecraft.getInstance().hitResult;
		if (trace == null || trace.getType() != RayTraceResult.Type.BLOCK)
			return;

		T tile = (T) Minecraft.getInstance().level.getBlockEntity(((BlockRayTraceResult) trace).getBlockPos());

		int yOffset = 0;
		for (Pair<Sprite, Function<T, String>> sprite : information)
		{
			sprite.getLeft().draw(matrixStack, drawX, drawY + yOffset);
			int textY = drawY + yOffset + (sprite.getLeft().getTextureHeight() / 4);
			Minecraft.getInstance().font.drawShadow(matrixStack, (tile != null && tile.getClass() == tileClass)
					? sprite.getRight().apply(tile)
					: "?", drawX + sprite.getLeft().getTextureWidth() + 2, textY, Color.WHITE.getRGB());
			yOffset += sprite.getLeft().getTextureHeight() + 2;
		}
	}

	@Override
	public boolean shouldRender(Minecraft minecraft)
	{
		RayTraceResult trace = Minecraft.getInstance().hitResult;
		if (trace == null || trace.getType() != RayTraceResult.Type.BLOCK)
			return false;

		TileEntity tile = Minecraft.getInstance().level.getBlockEntity(((BlockRayTraceResult) trace).getBlockPos());
		if (tile == null || !tileClass.isAssignableFrom(tile.getClass()))
			return false;

		return true;
	}
}
