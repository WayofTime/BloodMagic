package wayoftime.bloodmagic.client.screen;

import it.unimi.dsi.fastutil.Pair;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.client.widgets.MultiIconButton;
import wayoftime.bloodmagic.common.menu.AbstractGhostMenu;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractGhostScreen<T extends AbstractGhostMenu<?>> extends AbstractContainerScreen<T> {
    public static final ResourceLocation SELECTED = BloodMagic.rl("container/ghost_selected");

    public AbstractGhostScreen(T menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void init() {
        super.init();
        this.inventoryLabelY = this.imageHeight - 94; // auto recalc
    }

    private final List<Pair<MultiIconButton, Integer>> updateButtons = new ArrayList<>();
    public void addMultiIconButton(int dataIndex, MultiIconButton button) {
        updateButtons.add(Pair.of(button, dataIndex));
        addRenderableWidget(button);
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        updateButtons.forEach(pair -> pair.left().setState(this.menu.getData(pair.right())));
    }

    public abstract ResourceLocation background();

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int x, int y) {
        super.renderTooltip(guiGraphics, x, y);
        updateButtons.forEach(pair -> {
            MultiIconButton button = pair.left();
            if (button.isHovered()) {
                guiGraphics.renderTooltip(this.font, button.getHoverText(), x, y);
            }
        });
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blit(this.background(), leftPos, topPos, 0, 0, imageWidth, imageHeight);
        int lastIdx = this.menu.getLastGhostSlotClicked();
        if (lastIdx >= 0) {
            Slot lastSlot = this.menu.getSlot(lastIdx);
            guiGraphics.blitSprite(SELECTED, leftPos + lastSlot.x - 4, topPos + lastSlot.y - 4, 24, 24);
        }
    }
}
