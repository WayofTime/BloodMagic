package wayoftime.bloodmagic.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.registries.ForgeRegistries;
import org.joml.Matrix4f;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.container.tile.ContainerAlchemicalReactionChamber;
import wayoftime.bloodmagic.common.tile.TileAlchemicalReactionChamber;
import wayoftime.bloodmagic.util.handler.event.ClientHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ScreenAlchemicalReactionChamber extends ScreenBase<ContainerAlchemicalReactionChamber>
{
	private static final ResourceLocation background = new ResourceLocation(BloodMagic.MODID, "textures/gui/arc_gui.png");
	public TileAlchemicalReactionChamber tileARC;

	public ScreenAlchemicalReactionChamber(ContainerAlchemicalReactionChamber container, Inventory playerInventory, Component title)
	{
		super(container, playerInventory, title);
		tileARC = container.tileARC;
		this.imageWidth = 176;
		this.imageHeight = 205;
	}

	@Override
	public ResourceLocation getBackground()
	{
		return background;
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks)
	{
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
		List<Component> tooltip = new ArrayList<>();
//		FluidTank inputTank = new FluidTank(FluidAttributes.BUCKET_VOLUME * 2);
//		inputTank.fill(new FluidStack(Fluids.WATER, 1000), FluidAction.EXECUTE);

		ClientHandler.handleGuiTank(guiGraphics, tileARC.inputTank, this.leftPos + 8, this.topPos + 40, 16, 63, 194, 1, 16, 63, mouseX, mouseY, background, tooltip);
		ClientHandler.handleGuiTank(guiGraphics, tileARC.outputTank, this.leftPos + 152, this.topPos + 15, 16, 63, 194, 1, 16, 63, mouseX, mouseY, background, tooltip);

		if (!tooltip.isEmpty())
			guiGraphics.renderTooltip(this.font, tooltip, Optional.empty(), mouseX, mouseY);
	}

//
	@Override
	protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY)
	{
		guiGraphics.drawString(this.font, Component.translatable("tile.bloodmagic.arc.name"), 8, 5, 4210752, false);
		guiGraphics.drawString(this.font, Component.translatable("container.inventory"), 8, 111, 4210752, false);
	}

//
	@Override
	protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY)
	{
		int i = (this.width - this.imageWidth) / 2;
		int j = (this.height - this.imageHeight) / 2;
		guiGraphics.blit(background, i, j, 0, 0, this.imageWidth, this.imageHeight);

//		FluidTank inputTank = new FluidTank(FluidAttributes.BUCKET_VOLUME * 2);
//		inputTank.fill(new FluidStack(Fluids.WATER, 1000), FluidAction.EXECUTE);

		ClientHandler.handleGuiTank(guiGraphics, tileARC.inputTank, this.leftPos + 8, this.topPos + 40, 16, 63, 194, 1, 16, 63, mouseX, mouseY, background, null);
		ClientHandler.handleGuiTank(guiGraphics, tileARC.outputTank, this.leftPos + 152, this.topPos + 15, 16, 63, 194, 1, 16, 63, mouseX, mouseY, background, null);

		int w = this.getCookProgressScaled(38);
		guiGraphics.blit(background, i + 63, j + 44, 176, 90, w, 23);
	}

////
	public int getCookProgressScaled(int scale)
	{
		double progress =  tileARC.getProgressForGui();
		return (int) (progress * scale);
	}

}