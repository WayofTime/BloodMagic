package wayoftime.bloodmagic.client.screens;

import org.apache.commons.lang3.tuple.Pair;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.network.BloodMagicPacketHandler;
import wayoftime.bloodmagic.network.ItemRoutingNodeButtonPacket;
import wayoftime.bloodmagic.tile.container.ContainerItemRoutingNode;
import wayoftime.bloodmagic.tile.routing.TileFilteredRoutingNode;

public class ScreenItemRoutingNode extends ScreenBase<ContainerItemRoutingNode>
{
	private static final ResourceLocation background = new ResourceLocation(BloodMagic.MODID, "textures/gui/routingnode.png");
	public TileFilteredRoutingNode tileNode;

	private Direction playerFacing;
	private Direction horizontalFacing;

	private int left, top;

	public ScreenItemRoutingNode(ContainerItemRoutingNode container, PlayerInventory playerInventory, ITextComponent title)
	{
		super(container, playerInventory, title);
		tileNode = container.tileNode;
		this.xSize = 176;
		this.ySize = 187;

		Vector3d facingVec = Vector3d.fromPitchYaw(playerInventory.player.getPitchYaw());
		this.playerFacing = Direction.getFacingFromVector(facingVec.x, facingVec.y, facingVec.z);
		this.horizontalFacing = playerInventory.player.getHorizontalFacing();

	}

	@Override
	public void init()
	{
		super.init();
		left = (this.width - this.xSize) / 2;
		top = (this.height - this.ySize) / 2;

		this.buttons.clear();

		for (int i = 0; i < 6; i++)
		{
			Direction dir = getFilterDirectionForButton(i);
			Pair<Integer, Integer> buttonLocation = getButtonLocation(i);
			String dirName = getStringForDirection(dir);
			if (!tileNode.getWorld().isAirBlock(tileNode.getBlockPos().offset(dir)))
			{
				dirName = "";
			}
			this.addButton(new Button(left + buttonLocation.getLeft(), top + buttonLocation.getRight(), 20, 20, new StringTextComponent(dirName), new DirectionalPress(this, tileNode, i, dir)));

			if (dir.ordinal() == tileNode.getCurrentActiveSlot())
			{
				disableDirectionalButton(i);
			}
		}

		this.addButton(new Button(left + 89, top + 50, 8, 20, new StringTextComponent(">"), new IncrementPress(tileNode, 6)));
		this.addButton(new Button(left + 61, top + 50, 8, 20, new StringTextComponent("<"), new IncrementPress(tileNode, 7)));
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
				return horizontalFacing.rotateYCCW();
			case 3:
				return horizontalFacing.rotateY();
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
				return horizontalFacing.rotateYCCW();
			case 3:
				return horizontalFacing.rotateY();
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
				return horizontalFacing.rotateYCCW();
			case 3:
				return horizontalFacing.rotateY();
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
	protected void drawGuiContainerForegroundLayer(MatrixStack stack, int mouseX, int mouseY)
	{
		this.font.func_243248_b(stack, new StringTextComponent("" + getCurrentActiveSlotPriority()), 71 + 5, 51 + 5, 0xFFFFFF);
//		this.font.func_243248_b(stack, new TranslationTextComponent("tile.bloodmagic.routingnode.name"), 8, 5, 4210752);
//		this.font.func_243248_b(stack, new TranslationTextComponent("container.inventory"), 8, 111, 4210752);

		BlockPos tilePos = tileNode.getBlockPos();
		World world = tileNode.getWorld();

		for (int i = 0; i < 6; i++)
		{
			Direction dir = getFilterDirectionForButton(i);
			Pair<Integer, Integer> buttonLocation = getButtonLocation(i);

			BlockState blockState = world.getBlockState(tilePos.offset(dir));
			Block block = blockState.getBlock();
			if (block != null)
			{
				ItemStack itemStack = new ItemStack(block);
				this.drawItemStack(itemStack, buttonLocation.getLeft() + 2, buttonLocation.getRight() + 2, getStringForDirection(dir));
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
		Direction direction = Direction.byIndex(tileNode.getCurrentActiveSlot());
		if (direction != null)
		{
			return tileNode.getPriority(direction);
		}

		return 0;
	}

	private void enableAllDirectionalButtons()
	{
		for (Widget button : this.buttons)
		{
			button.active = true;
		}
	}

	private void disableDirectionalButton(int id)
	{
		this.buttons.get(id).active = false;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(MatrixStack stack, float partialTicks, int mouseX, int mouseY)
	{
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		getMinecraft().getTextureManager().bindTexture(background);
		int i = (this.width - this.xSize) / 2;
		int j = (this.height - this.ySize) / 2;
		this.blit(stack, i, j, 0, 0, this.xSize, this.ySize);

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
	private void drawItemStack(ItemStack stack, int x, int y, String altText)
	{
		RenderSystem.translatef(0.0F, 0.0F, 32.0F);
//		this.getbl
		this.setBlitOffset(1);
		this.itemRenderer.zLevel = 1;
		net.minecraft.client.gui.FontRenderer font = stack.getItem().getFontRenderer(stack);
		if (font == null)
			font = this.font;
		this.itemRenderer.renderItemAndEffectIntoGUI(stack, x, y);
		int offset = 8; // (this.draggedStack.isEmpty() ? 0 : 8)
		this.itemRenderer.renderItemOverlayIntoGUI(font, stack, x, y - offset, altText);
		this.setBlitOffset(0);
		this.itemRenderer.zLevel = 0.0F;
	}

	public class DirectionalPress implements Button.IPressable
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
				BloodMagicPacketHandler.INSTANCE.sendToServer(new ItemRoutingNodeButtonPacket(node.getPos(), direction.ordinal()));

				if (id < 6)
				{
					tileNode.setCurrentActiveSlot(direction.ordinal());
					this.screen.enableAllDirectionalButtons();
					this.screen.disableDirectionalButton(id);
				}
			}
		}
	}

	public class IncrementPress implements Button.IPressable
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
				BloodMagicPacketHandler.INSTANCE.sendToServer(new ItemRoutingNodeButtonPacket(node.getPos(), id));
			}
		}
	}
}
