package wayoftime.bloodmagic.client.screens;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.platform.GlStateManager.DestFactor;
import com.mojang.blaze3d.platform.GlStateManager.SourceFactor;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.network.AlchemyTableButtonPacket;
import wayoftime.bloodmagic.network.BloodMagicPacketHandler;
import wayoftime.bloodmagic.tile.TileAlchemyTable;
import wayoftime.bloodmagic.tile.container.ContainerAlchemyTable;

import net.minecraft.client.gui.components.Button.OnPress;

public class ScreenAlchemyTable extends ScreenBase<ContainerAlchemyTable>
{
	private static final ResourceLocation background = new ResourceLocation(BloodMagic.MODID, "textures/gui/alchemytable.png");
	private static final List<Component> orbError = new ArrayList<Component>();
	private static final List<Component> lpError = new ArrayList<Component>();
	public TileAlchemyTable tileTable;

	private int left, top;

	public ScreenAlchemyTable(ContainerAlchemyTable container, Inventory playerInventory, Component title)
	{
		super(container, playerInventory, title);
		tileTable = container.tileTable;
		this.imageWidth = 176;
		this.imageHeight = 205;

		orbError.clear();
		orbError.add(new TranslatableComponent("tooltip.bloodmagic.alchemytable.orberror.title").withStyle(ChatFormatting.RED));
		orbError.add(new TranslatableComponent("tooltip.bloodmagic.alchemytable.orberror.text").withStyle(ChatFormatting.GRAY));
		lpError.clear();
		lpError.add(new TranslatableComponent("tooltip.bloodmagic.alchemytable.lperror.title").withStyle(ChatFormatting.RED));
		lpError.add(new TranslatableComponent("tooltip.bloodmagic.alchemytable.lperror.text").withStyle(ChatFormatting.GRAY));
	}

	@Override
	public ResourceLocation getBackground()
	{
		return background;
	}

	@Override
	protected void renderLabels(PoseStack stack, int mouseX, int mouseY)
	{
		this.font.draw(stack, new TranslatableComponent("tile.bloodmagic.alchemytable.name"), 8, 5, 4210752);
		this.font.draw(stack, new TranslatableComponent("container.inventory"), 8, 111, 4210752);
	}

	@Override
	public void init()
	{
		super.init();
		left = (this.width - this.imageWidth) / 2;
		top = (this.height - this.imageHeight) / 2;

		this.buttons.clear();
//		this.buttons.add();
		this.addButton(new Button(left + 135, top + 52, 14, 14, new TextComponent("D"), new DirectionalPress(tileTable, Direction.DOWN)));
		this.addButton(new Button(left + 153, top + 52, 14, 14, new TextComponent("U"), new DirectionalPress(tileTable, Direction.UP)));
		this.addButton(new Button(left + 135, top + 70, 14, 14, new TextComponent("N"), new DirectionalPress(tileTable, Direction.NORTH)));
		this.addButton(new Button(left + 153, top + 70, 14, 14, new TextComponent("S"), new DirectionalPress(tileTable, Direction.SOUTH)));
		this.addButton(new Button(left + 135, top + 88, 14, 14, new TextComponent("W"), new DirectionalPress(tileTable, Direction.WEST)));
		this.addButton(new Button(left + 153, top + 88, 14, 14, new TextComponent("E"), new DirectionalPress(tileTable, Direction.EAST)));
	}

	@Override
	protected void renderBg(PoseStack stack, float partialTicks, int mouseX, int mouseY)
	{
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		getMinecraft().getTextureManager().bind(background);
		int i = (this.width - this.imageWidth) / 2;
		int j = (this.height - this.imageHeight) / 2;
		this.blit(stack, i, j, 0, 0, this.imageWidth, this.imageHeight);

		int l = this.getCookProgressScaled(90);
		this.blit(stack, i + 106, j + 14 + 90 - l, 176, 90 - l, 18, l);

		if (this.getOrbFlag())
		{
			this.blit(stack, i + 106, j + 24, 194, 55, 18, 18);
			if (mouseX >= i + 106 && mouseX < i + 106 + 18 && mouseY >= j + 24 && mouseY < j + 24 + 18)
				this.renderWrappedToolTip(stack, orbError, mouseX, mouseY, font);
		} else if (this.getLPFlag())
		{
			this.blit(stack, i + 106, j + 24, 194, 73, 18, 18);
			if (mouseX >= i + 106 && mouseX < i + 106 + 18 && mouseY >= j + 24 && mouseY < j + 24 + 18)
				this.renderWrappedToolTip(stack, lpError, mouseX, mouseY, font);
		}

		int slotId = tileTable.activeSlot;
		if (slotId != -1)
		{
			Slot slot = this.getMenu().getSlot(slotId);

			if (slotId == TileAlchemyTable.outputSlot)
			{
				this.blit(stack, i + slot.x, j + slot.y, 195, 37, 16, 16);
			} else
			{
				this.blit(stack, i + slot.x, j + slot.y, 195, 19, 16, 16);
			}

			for (int buttonId = 0; buttonId < 6; buttonId++)
			{
				int xOffset = (buttonId % 2) * 18 + 133;
				int yOffset = (buttonId / 2) * 18 + 50;
				if (tileTable.isSlotEnabled(slotId, Direction.from3DDataValue(buttonId)))
				{
					this.blit(stack, i + xOffset, j + yOffset, 212, 18, 18, 18);
				} else
				{
					this.blit(stack, i + xOffset, j + yOffset, 212, 0, 18, 18);
				}
			}
		}

//		for (int slotId = 0; slotId < 6; slotId++)
//		{
//			if (!((TileAlchemyTable) tileTable).isInputSlotAccessible(slotId))
//			{
//				Slot slot = this.getContainer().getSlot(slotId);
//
//				this.blit(stack, i + slot.xPos, j + slot.yPos, 195, 1, 16, 16);
//			}
//		}
	}

//	@Override
//	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton)
//	{
//		boolean superMouse = super.mouseClicked(mouseX, mouseY, mouseButton);
//		System.out.println("Last button clicked: " + mouseButton);
//		return superMouse;
//	}

//
	public int getCookProgressScaled(int scale)
	{
		double progress = ((TileAlchemyTable) tileTable).getProgressForGui();
//		if (tileSoulForge != null)
//		{
//			System.out.println("Tile is NOT null");
//		}
//		double progress = ((float) this.container.data.get(0)) / ((float) this.container.data.get(1));
//		System.out.println(this.container.data.get(0));
		return (int) (progress * scale);
	}

	public boolean getOrbFlag()
	{
		return ((TileAlchemyTable) tileTable).getOrbFlagForGui();
	}

	public boolean getLPFlag()
	{
		return ((TileAlchemyTable) tileTable).getLPFlagforGui();
	}

	public class DirectionalButton extends Button
	{
		private static final int BUTTON_TEX_X = 200, BUTTON_TEX_Y = 60;

		public DirectionalButton(int x, int y, int width, int height, Component title, OnPress pressedAction)
		{
			super(x, y, width, height, title, pressedAction);
		}

		@Override
		public void renderButton(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks)
		{
			Minecraft minecraft = Minecraft.getInstance();
			Font fontrenderer = minecraft.font;
			minecraft.getTextureManager().bind(WIDGETS_LOCATION);

			// Vanilla's method
//			RenderSystem.color4f(1.0F, 1.0F, 1.0F, this.alpha);
//			int i = this.getYImage(this.isHovered());
//			RenderSystem.enableBlend();
//			RenderSystem.defaultBlendFunc();
//			RenderSystem.enableDepthTest();
//			this.blit(matrixStack, this.x, this.y, 0, 46 + i * 20, this.width / 2, this.height);
//			this.blit(matrixStack, this.x + this.width / 2, this.y, 200 - this.width / 2, 46 + i * 20, this.width / 2, this.height);
//			this.renderBg(matrixStack, minecraft, mouseX, mouseY);
//			int j = getFGColor();
//			drawCenteredString(matrixStack, fontrenderer, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);

			// Mekanism's method
			int i = this.getYImage(this.isHovered());
			RenderSystem.enableBlend();
			RenderSystem.blendFuncSeparate(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA, SourceFactor.ONE, DestFactor.ZERO);
			RenderSystem.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);

			int width = this.width;
			int height = this.height;
			int halfWidthLeft = width / 2;
			int halfWidthRight = width % 2 == 0 ? halfWidthLeft : halfWidthLeft + 1;
			int halfHeightTop = height / 2;
			int halfHeightBottom = height % 2 == 0 ? halfHeightTop : halfHeightTop + 1;
			int position = i * 20;

			// Left Top Corner
			blit(matrixStack, x, y, 0, position, halfWidthLeft, halfHeightTop, BUTTON_TEX_X, BUTTON_TEX_Y);
			// Left Bottom Corner
			blit(matrixStack, x, y + halfHeightTop, 0, position + 20 - halfHeightBottom, halfWidthLeft, halfHeightBottom, BUTTON_TEX_X, BUTTON_TEX_Y);
			// Right Top Corner
			blit(matrixStack, x + halfWidthLeft, y, 200 - halfWidthRight, position, halfWidthRight, halfHeightTop, BUTTON_TEX_X, BUTTON_TEX_Y);
			// Right Bottom Corner
			blit(matrixStack, x + halfWidthLeft, y + halfHeightTop, 200 - halfWidthRight, position + 20 - halfHeightBottom, halfWidthRight, halfHeightBottom, BUTTON_TEX_X, BUTTON_TEX_Y);
			renderBg(matrixStack, minecraft, mouseX, mouseY);
			RenderSystem.disableBlend();

			if (this.isHovered())
			{
				this.renderToolTip(matrixStack, mouseX, mouseY);
			}
		}
	}

	public class DirectionalPress implements Button.OnPress
	{
		private final TileAlchemyTable table;
		private final Direction direction;

		public DirectionalPress(TileAlchemyTable table, Direction direction)
		{
			this.table = table;
			this.direction = direction;
		}

		@Override
		public void onPress(Button button)
		{
//			System.out.println("Pressing le " + direction.toString() + " button, beign oui!");
			int activeSlot = table.activeSlot;
			if (activeSlot != -1)
			{
				boolean enabled = table.isSlotEnabled(activeSlot, direction);
				table.setSlotEnabled(!enabled, activeSlot, direction);
				BloodMagicPacketHandler.INSTANCE.sendToServer(new AlchemyTableButtonPacket(table.getBlockPos(), activeSlot, direction, !enabled));
			}
////			if (button.visible)
//			{
//				button.visible = !button.visible;
//			}
		}
	}
}