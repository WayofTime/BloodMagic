package wayoftime.bloodmagic.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.tuple.Pair;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.container.tile.ContainerItemRoutingNode;
import wayoftime.bloodmagic.common.tile.routing.TileFilteredRoutingNode;
import wayoftime.bloodmagic.network.BloodMagicPacketHandler;
import wayoftime.bloodmagic.network.ItemRoutingNodeButtonPacket;

import java.util.ArrayList;
import java.util.List;

public class ScreenItemRoutingNode extends ScreenBase<ContainerItemRoutingNode>
{
	private static final ResourceLocation background = new ResourceLocation(BloodMagic.MODID, "textures/gui/routingnode.png");
	public TileFilteredRoutingNode tileNode;

	private Direction playerFacing;
	private Direction horizontalFacing;

	private int left, top;

	private List<Button> buttonList = new ArrayList<>();

	public ScreenItemRoutingNode(ContainerItemRoutingNode container, Inventory playerInventory, Component title)
	{
		super(container, playerInventory, title);
		tileNode = container.tileNode;
		this.imageWidth = 176;
		this.imageHeight = 187;

		Vec3 facingVec = Vec3.directionFromRotation(playerInventory.player.getRotationVector());
		this.playerFacing = Direction.getNearest(facingVec.x, facingVec.y, facingVec.z);
		this.horizontalFacing = playerInventory.player.getDirection();

	}

	@Override
	public void init()
	{
		super.init();
		left = (this.width - this.imageWidth) / 2;
		top = (this.height - this.imageHeight) / 2;

		this.clearWidgets();

		for (int i = 0; i < 6; i++)
		{
			Direction dir = getFilterDirectionForButton(i);
			Pair<Integer, Integer> buttonLocation = getButtonLocation(i);
			String dirName = getStringForDirection(dir);
			if (!tileNode.getLevel().isEmptyBlock(tileNode.getCurrentBlockPos().relative(dir)))
			{
				dirName = "";
			}
			Button button = Button.builder(Component.literal(dirName),  new DirectionalPress(this, tileNode, i, dir)).pos(left + buttonLocation.getLeft(), top + buttonLocation.getRight()).size(20,20).build();
			this.addRenderableWidget(button);
			this.buttonList.add(button);

			if (dir.ordinal() == tileNode.getCurrentActiveSlot())
			{
				disableDirectionalButton(i);
			}
		}

		this.addRenderableWidget(Button.builder(Component.literal(">"), new IncrementPress(tileNode, 6)).pos(left + 89, top + 50).size( 8, 20).build());
		this.addRenderableWidget(Button.builder(Component.literal("<"), new IncrementPress(tileNode, 7)).pos(left + 61, top + 50).size( 8, 20).build());
	}

	/**
	 * Gives the Filter Direction for the given button location.
	 */
	public Direction getFilterDirectionForButton(int button)
	{
		if (button == 2)
		{
			return playerFacing;
		} else if (button == 5)
		{
			return playerFacing.getOpposite();
		} else if (playerFacing == Direction.UP)
		{
			switch (button)
			{
			case 0:
				return horizontalFacing.getOpposite();
			case 4:
				return horizontalFacing;
			case 1:
				return horizontalFacing.getCounterClockWise();
			case 3:
				return horizontalFacing.getClockWise();
			}
		} else if (playerFacing == Direction.DOWN)
		{
			switch (button)
			{
			case 0:
				return horizontalFacing;
			case 4:
				return horizontalFacing.getOpposite();
			case 1:
				return horizontalFacing.getCounterClockWise();
			case 3:
				return horizontalFacing.getClockWise();
			}
		} else
		{
			switch (button)
			{
			case 0:
				return Direction.UP;
			case 4:
				return Direction.DOWN;
			case 1:
				return horizontalFacing.getCounterClockWise();
			case 3:
				return horizontalFacing.getClockWise();
			}
		}

		return Direction.UP;
	}

	public String getStringForDirection(Direction dir)
	{
		switch (dir)
		{
		case DOWN:
			return "D";
		case EAST:
			return "E";
		case NORTH:
			return "N";
		case SOUTH:
			return "S";
		case UP:
			return "U";
		case WEST:
			return "W";
		}

		return "";
	}

	@Override
	public ResourceLocation getBackground()
	{
		return background;
	}

	@Override
	protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY)
	{
		guiGraphics.drawString(this.font, Component.literal("" + getCurrentActiveSlotPriority()), 71 + 5, 51 + 5, 0xFFFFFF, false);
//		this.font.draw(stack, new TranslationTextComponent("tile.bloodmagic.routingnode.name"), 8, 5, 4210752);
//		this.font.draw(stack, new TranslationTextComponent("container.inventory"), 8, 111, 4210752);

		BlockPos tilePos = tileNode.getCurrentBlockPos();
		Level world = tileNode.getLevel();

		for (int i = 0; i < 6; i++)
		{
			Direction dir = getFilterDirectionForButton(i);
			Pair<Integer, Integer> buttonLocation = getButtonLocation(i);

			BlockState blockState = world.getBlockState(tilePos.relative(dir));
			Block block = blockState.getBlock();
			if (block != null)
			{
				ItemStack itemStack = new ItemStack(block);
				this.drawItemStack(guiGraphics ,itemStack, buttonLocation.getLeft() + 2, buttonLocation.getRight() + 2, getStringForDirection(dir));
			}
		}
	}

	public Pair<Integer, Integer> getButtonLocation(int button)
	{
		switch (button)
		{
		case 0:
			return Pair.of(129, 11);
		case 1:
			return Pair.of(109, 31);
		case 2:
			return Pair.of(129, 31);
		case 3:
			return Pair.of(149, 31);
		case 4:
			return Pair.of(129, 51);
		case 5:
			return Pair.of(149, 51);
		default:
			return Pair.of(0, 0);
		}
	}

	private int getCurrentActiveSlotPriority()
	{
		Direction direction = Direction.from3DDataValue(tileNode.getCurrentActiveSlot());
		if (direction != null)
		{
			return tileNode.getPriority(direction);
		}

		return 0;
	}

	private void enableAllDirectionalButtons()
	{
		for (AbstractWidget button : this.buttonList)
		{
			button.active = true;
		}
	}

	private void disableDirectionalButton(int id)
	{
		this.buttonList.get(id).active = false;
	}

	@Override
	protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY)
	{
		int i = (this.width - this.imageWidth) / 2;
		int j = (this.height - this.imageHeight) / 2;
		guiGraphics.blit(background, i, j, 0, 0, this.imageWidth, this.imageHeight);
	}

//	@Override
//	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton)
//	{
//		boolean superMouse = super.mouseClicked(mouseX, mouseY, mouseButton);
//		System.out.println("Last button clicked: " + mouseButton);
//		return superMouse;
//	}

//

	/**
	 * Draws an ItemStack.
	 * 
	 * The z index is increased by 32 (and not decreased afterwards), and the item
	 * is then rendered at z=200.
	 */
	private void drawItemStack(GuiGraphics guiGraphics, ItemStack stack, int x, int y, String altText)
	{
		guiGraphics.pose().pushPose();
		guiGraphics.pose().translate(0, 0, 1);
		Font font = null;
		if (font == null)
			font = this.font;
		guiGraphics.renderItem(stack, x, y);
		int offset = 8; // (this.draggedStack.isEmpty() ? 0 : 8)
		guiGraphics.renderItemDecorations(font, stack, x, y - offset, altText);
		guiGraphics.pose().popPose();
	}

	public class DirectionalPress implements Button.OnPress
	{
		private final ScreenItemRoutingNode screen;
		private final TileFilteredRoutingNode node;
		private final int id;
		private final Direction direction;

		public DirectionalPress(ScreenItemRoutingNode screen, TileFilteredRoutingNode node, int id, Direction direction)
		{
			this.screen = screen;
			this.node = node;
			this.id = id;
			this.direction = direction;
		}

		@Override
		public void onPress(Button button)
		{
			if (button.active)
			{
				BloodMagicPacketHandler.INSTANCE.sendToServer(new ItemRoutingNodeButtonPacket(node.getBlockPos(), direction.ordinal()));

				if (id < 6)
				{
					tileNode.setCurrentActiveSlot(direction.ordinal());
					this.screen.enableAllDirectionalButtons();
					this.screen.disableDirectionalButton(id);
				}
			}
		}
	}

	public class IncrementPress implements Button.OnPress
	{
		private final TileFilteredRoutingNode node;
		private final int id;

		public IncrementPress(TileFilteredRoutingNode node, int id)
		{
			this.node = node;
			this.id = id;
		}

		@Override
		public void onPress(Button button)
		{
			if (button.active)
			{
				BloodMagicPacketHandler.INSTANCE.sendToServer(new ItemRoutingNodeButtonPacket(node.getBlockPos(), id));
			}
		}
	}
}
