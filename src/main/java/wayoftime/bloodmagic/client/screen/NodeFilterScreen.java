package wayoftime.bloodmagic.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.menu.NodeFilterMenu;

public class NodeFilterScreen extends AbstractContainerScreen<NodeFilterMenu> {

    private static final ResourceLocation background = BloodMagic.rl("textures/gui/container/node_filer.png");
    public NodeFilterScreen(NodeFilterMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void init() {
        addRenderableWidget(Button.builder(Component.literal("<"), button -> sendButtonClick(1))
                .pos(leftPos + 16, topPos + 34)
                .size(8, 20)
                .build()
        );
        addRenderableWidget(Button.builder(Component.literal(">"), button -> sendButtonClick(2))
                .pos(leftPos + 44, topPos + 34)
                .size(8, 20)
                .build()
        );
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blit(background, leftPos, topPos, 0, 0, imageWidth, imageHeight);
    }

    private void sendButtonClick(int id) {
        this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, id);
    }
}
