package wayoftime.bloodmagic.client.screens;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager.DestFactor;
import com.mojang.blaze3d.platform.GlStateManager.SourceFactor;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.network.AlchemyTableButtonPacket;
import wayoftime.bloodmagic.network.BloodMagicPacketHandler;
import wayoftime.bloodmagic.tile.TileAlchemyTable;
import wayoftime.bloodmagic.tile.container.ContainerAlchemyTable;

public class ScreenAlchemyTable extends ScreenBase<ContainerAlchemyTable>
{
	private static final ResourceLocation background = new ResourceLocation(BloodMagic.MODID, "textures/gui/alchemytable.png");
	private static final List<ITextComponent> orbError = new ArrayList<ITextComponent>();
	private static final List<ITextComponent> lpError = new ArrayList<ITextComponent>();
	public TileAlchemyTable tileTable;

	private int left, top;

	public ScreenAlchemyTable(ContainerAlchemyTable container, PlayerInventory playerInventory, ITextComponent title)
	{
		super(container, playerInventory, title);
		tileTable = container.tileTable;
		this.xSize = 176;
		this.ySize = 205;

		orbError.clear();
		orbError.add(new TranslationTextComponent("tooltip.bloodmagic.alchemytable.orberror.title").mergeStyle(TextFormatting.RED));
		orbError.add(new TranslationTextComponent("tooltip.bloodmagic.alchemytable.orberror.text").mergeStyle(TextFormatting.GRAY));
		lpError.clear();
		lpError.add(new TranslationTextComponent("tooltip.bloodmagic.alchemytable.lperror.title").mergeStyle(TextFormatting.RED));
		lpError.add(new TranslationTextComponent("tooltip.bloodmagic.alchemytable.lperror.text").mergeStyle(TextFormatting.GRAY));
	}

	@Override
	public ResourceLocation getBackground()
	{
		return background;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(MatrixStack stack, int mouseX, int mouseY)
	{
		this.font.func_243248_b(stack, new TranslationTextComponent("tile.bloodmagic.alchemytable.name"), 8, 5, 4210752);
		this.font.func_243248_b(stack, new TranslationTextComponent("container.inventory"), 8, 111, 4210752);
	}

	@Override
	public void init()
	{
		super.init();
		left = (this.width - this.xSize) / 2;
		top = (this.height - this.ySize) / 2;

		this.buttons.clear();
//		this.buttons.add();
		this.addButton(new Button(left + 135, top + 52, 14, 14, new StringTextComponent("D"), new DirectionalPress(tileTable, Direction.DOWN)));
		this.addButton(new Button(left + 153, top + 52, 14, 14, new StringTextComponent("U"), new DirectionalPress(tileTable, Direction.UP)));
		this.addButton(new Button(left + 135, top + 70, 14, 14, new StringTextComponent("N"), new DirectionalPress(tileTable, Direction.NORTH)));
		this.addButton(new Button(left + 153, top + 70, 14, 14, new StringTextComponent("S"), new DirectionalPress(tileTable, Direction.SOUTH)));
		this.addButton(new Button(left + 135, top + 88, 14, 14, new StringTextComponent("W"), new DirectionalPress(tileTable, Direction.WEST)));
		this.addButton(new Button(left + 153, top + 88, 14, 14, new StringTextComponent("E"), new DirectionalPress(tileTable, Direction.EAST)));
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(MatrixStack stack, float partialTicks, int mouseX, int mouseY)
	{
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		getMinecraft().getTextureManager().bindTexture(background);
		int i = (this.width - this.xSize) / 2;
		int j = (this.height - this.ySize) / 2;
		this.blit(stack, i, j, 0, 0, this.xSize, this.ySize);

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
			Slot slot = this.getContainer().getSlot(slotId);

			if (slotId == TileAlchemyTable.outputSlot)
			{
				this.blit(stack, i + slot.xPos, j + slot.yPos, 195, 37, 16, 16);
			} else
			{
				this.blit(stack, i + slot.xPos, j + slot.yPos, 195, 19, 16, 16);
			}

			for (int buttonId = 0; buttonId < 6; buttonId++)
			{
				int xOffset = (buttonId % 2) * 18 + 133;
				int yOffset = (buttonId / 2) * 18 + 50;
				if (tileTable.isSlotEnabled(slotId, Direction.byIndex(buttonId)))
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

		public DirectionalButton(int x, int y, int width, int height, ITextComponent title, IPressable pressedAction)
		{
			super(x, y, width, height, title, pressedAction);
		}

		@Override
		public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
		{
			Minecraft minecraft = Minecraft.getInstance();
			FontRenderer fontrenderer = minecraft.fontRenderer;
			minecraft.getTextureManager().bindTexture(WIDGETS_LOCATION);

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

	public class DirectionalPress implements Button.IPressable
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
				BloodMagicPacketHandler.INSTANCE.sendToServer(new AlchemyTableButtonPacket(table.getPos(), activeSlot, direction, !enabled));
			}
////			if (button.visible)
//			{
//				button.visible = !button.visible;
//			}
		}
	}
}