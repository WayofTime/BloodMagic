package wayoftime.bloodmagic.client.screen;

import it.unimi.dsi.fastutil.Pair;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.client.widgets.MultiIconButton;
import wayoftime.bloodmagic.common.menu.AbstractGhostMenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractGhostScreen<T extends AbstractGhostMenu<?>> extends AbstractContainerScreen<T> {
    public static final ResourceLocation SELECTED = BloodMagic.rl("container/ghost_selected");

    public AbstractGhostScreen(T menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        ContainerListener buttonListener = new ContainerListener() {
            @Override
            public void slotChanged(AbstractContainerMenu containerToSend, int dataSlotIndex, ItemStack stack) {
            }

            @Override
            public void dataChanged(AbstractContainerMenu containerMenu, int dataSlotIndex, int value) {
                if (updateButtons.containsKey(dataSlotIndex)) {
                    updateButtons.get(dataSlotIndex).setState(value);
                }
            }
        };
        menu.addSlotListener(buttonListener);
    }

    @Override
    protected void init() {
        super.init();
        this.inventoryLabelY = this.imageHeight - 94; // auto recalc
    }

    private final Map<Integer, MultiIconButton> updateButtons = new HashMap<>();
    public void addMultiIconButton(int dataIndex, MultiIconButton button) {
        updateButtons.put(dataIndex, button);
        addRenderableWidget(button);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int x, int y) {
        super.renderTooltip(guiGraphics, x, y);
        updateButtons.forEach((key, button) -> {
            if (button.isHovered()) {
                guiGraphics.renderTooltip(this.font, button.getHoverText(), x, y);
            }
        });
    }


    public abstract ResourceLocation background();
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
