package wayoftime.bloodmagic.client.screen;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.menu.NodeFilterMenu;
import wayoftime.bloodmagic.util.helper.FilterHelper;

public class NodeFilterScreen extends AbstractContainerScreen<NodeFilterMenu> {

    private static final ResourceLocation background = BloodMagic.rl("textures/gui/container/node_filter.png");
    private static final ResourceLocation selected = BloodMagic.rl("container/ghost_selected");
    // TODO name these better. up means leftmost or topmost...
    private static final int upLeft = 109 + 1;
    private static final int upTop = 11 + 1;
    private static final int midLeft = upLeft + 20;
    private static final int midTop = upTop + 20;
    private static final int bottomLeft = midLeft + 20;
    private static final int bottomTop = midTop + 20;
    private final Direction down;
    private final Pair<Integer, Integer> downLocation = Pair.of(midLeft, bottomTop);
    private final Direction up;
    private final Pair<Integer, Integer> upLocation = Pair.of(midLeft, upTop);
    private final Direction front;
    private final Pair<Integer, Integer> frontLocation = Pair.of(midLeft, midTop);
    private final Direction back;
    private final Pair<Integer, Integer> backLocation = Pair.of(bottomLeft, bottomTop);
    private final Direction left;
    private final Pair<Integer, Integer> leftLocation = Pair.of(upLeft, midTop);
    private final Direction right;
    private final Pair<Integer, Integer> rightLocation = Pair.of(bottomLeft, midTop);

    public NodeFilterScreen(NodeFilterMenu menu, Inventory playerInv, Component title) {
        super(menu, playerInv, title);
        imageWidth = 176;
        imageHeight = 187;

        Vec3 rot = Vec3.directionFromRotation(playerInv.player.getRotationVector());
        Direction playerFacing = Direction.getNearest(rot.x, rot.y, rot.z);
        Direction horizontalFacing = playerInv.player.getDirection();
        back = playerFacing;
        front = playerFacing.getOpposite();
        left = horizontalFacing.getCounterClockWise();
        right = horizontalFacing.getClockWise();
        if (playerFacing.getAxis().isVertical()) {
            up = playerFacing == Direction.DOWN ? horizontalFacing : horizontalFacing.getOpposite();
            down = playerFacing == Direction.DOWN ? horizontalFacing.getOpposite() : horizontalFacing;
        } else {
            up = Direction.UP;
            down = Direction.DOWN;
        }
    }

    @Override
    protected void init() {
        super.init();

        addRenderableWidget(directionButton(down, downLocation));
        addRenderableWidget(directionButton(up, upLocation));
        addRenderableWidget(directionButton(front, frontLocation));
        addRenderableWidget(directionButton(back, backLocation));
        addRenderableWidget(directionButton(left, leftLocation));
        addRenderableWidget(directionButton(right, rightLocation));

        addRenderableWidget(Button.builder(Component.literal("<"), button -> sendButtonClick(NodeFilterMenu.BUTTON_PRIO_DOWN))
                .pos(leftPos + 61, topPos + 50)
                .size(8, 20)
                .build()
        );
        addRenderableWidget(Button.builder(Component.literal(">"), button -> sendButtonClick(NodeFilterMenu.BUTTON_PRIO_UP))
                .pos(leftPos + 89, topPos + 50)
                .size(8, 20)
                .build()
        );

        addRenderableWidget(Button.builder(FilterHelper.translate("edit"), button -> sendButtonClick(NodeFilterMenu.BUTTON_EDIT))
                .pos(leftPos + 8, topPos + 31)
                .size(50, 20)
                .build()
        );

        // sendButtonClick(front.get3DDataValue()); // not sure whether to always focus front or to keep track of last selected
    }

    private Button directionButton(Direction dir, Pair<Integer, Integer> pos) {
        return Button.builder(Component.empty(), getPress(dir))
                .pos(leftPos + pos.getFirst(), topPos + pos.getSecond())
                .size(18, 18)
                .build();
    }

    private Button.OnPress getPress(Direction dir) {
        return button -> {
            if(!(menu.getData(6) == dir.get3DDataValue())) {
                sendButtonClick(dir.get3DDataValue());
            }
        };
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blit(background, leftPos, topPos, 0, 0, imageWidth, imageHeight);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        super.renderLabels(guiGraphics, mouseX, mouseY);
        // TODO figure out why its 71 + 5 and 51 + 5 instead of 76 and 56
        guiGraphics.drawString(this.font, Component.literal("" + menu.getData(menu.getData(6))), 71 + 5, 51 + 5, 0xFFFFFF, false);

        drawBlock(guiGraphics, up, upLocation.getFirst(), upLocation.getSecond());
        drawBlock(guiGraphics, down, downLocation.getFirst(), downLocation.getSecond());
        drawBlock(guiGraphics, left, leftLocation.getFirst(), leftLocation.getSecond());
        drawBlock(guiGraphics, right, rightLocation.getFirst(), rightLocation.getSecond());
        drawBlock(guiGraphics, front, frontLocation.getFirst(), frontLocation.getSecond());
        drawBlock(guiGraphics, back, backLocation.getFirst(), backLocation.getSecond());

        Pair<Integer, Integer> activePos = getPosForDir(Direction.from3DDataValue(menu.getData(6)));
        guiGraphics.blitSprite(selected, activePos.getFirst() - 3, activePos.getSecond() - 3, 24, 24);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        // TODO implement E/esc/backspace to "go back" one layer
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false; // we handle esc ourselves, so ignore in super
    }

    private Pair<Integer, Integer> getPosForDir(Direction dir) {
        if (dir == up) {
            return upLocation;
        }
        if (dir == down) {
            return downLocation;
        }
        if (dir == front) {
            return frontLocation;
        }
        if (dir == back) {
            return backLocation;
        }
        if (dir == left) {
            return leftLocation;
        }
        if (dir == right) {
            return rightLocation;
        }

        return Pair.of(0, 0);
    }

    private void drawBlock(GuiGraphics guiGraphics, Direction dir, int x, int y) {
        BlockState blockState = this.menu.player.level().getBlockState(this.menu.nodePos.relative(dir));
        Block block = blockState.getBlock();
        ItemStack stack = new ItemStack(block);
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(x + 3, y + 3, 0);
        guiGraphics.pose().scale(0.75f, 0.75f, 0.75f);
        guiGraphics.renderItem(stack, 0, 0);
        guiGraphics.pose().popPose();
    }

    private void sendButtonClick(int id) {
        Minecraft.getInstance().gameMode.handleInventoryButtonClick(this.menu.containerId, id);
    }
}
