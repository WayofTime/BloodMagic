package wayoftime.bloodmagic.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageWidget;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.menu.ARCMenu;
import wayoftime.bloodmagic.util.helper.RenderHelper;

public class ARCScreen extends AbstractContainerScreen<ARCMenu> {
    private final ResourceLocation background = ResourceLocation.fromNamespaceAndPath(BloodMagic.MODID, "textures/gui/container/arc_gui.png");
    private final ResourceLocation progress = ResourceLocation.fromNamespaceAndPath(BloodMagic.MODID, "container/arc/progress");
    private final ResourceLocation gauge = ResourceLocation.fromNamespaceAndPath(BloodMagic.MODID, "container/arc/gauge");
    public ARCScreen(ARCMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 176;
        this.imageHeight = 208;
        this.inventoryLabelY = imageHeight - 94;
    }

    private int inputX;
    private int inputY;
    private int outputX;
    private int outputY;
    @Override
    protected void init() {
        super.init();
        this.inputX = leftPos + 8;
        this.inputY = topPos + 43;
        this.outputX = leftPos + 152;
        this.outputY = topPos + 18;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        // fluids in tank
        if (!this.menu.tile.inputTank.isEmpty()) {
            int fluidHeight = 63 * this.menu.tile.inputTank.getFluidAmount() / this.menu.tile.inputTank.getCapacity();
            RenderHelper.renderGuiFluid(guiGraphics, this.menu.tile.inputTank.getFluid().getFluid(), inputX, inputY + (63 - fluidHeight), 16, fluidHeight);
        }
        if (!this.menu.tile.outputTank.isEmpty()) {
            int fluidHeight = 63 * this.menu.tile.outputTank.getFluidAmount() / this.menu.tile.outputTank.getCapacity();
            RenderHelper.renderGuiFluid(guiGraphics, this.menu.tile.outputTank.getFluid().getFluid(), outputX, outputY + (63 - fluidHeight), 16, fluidHeight);
        }
        // the little red indicator lines
        guiGraphics.blitSprite(gauge, inputX, inputY, 16, 57);
        guiGraphics.blitSprite(gauge, outputX, outputY, 16, 57);

        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int x, int y) {
        super.renderTooltip(guiGraphics, x, y);

        if (!this.menu.tile.inputTank.isEmpty()
                && x > inputX && x < inputX + 16 && y > inputY && y < inputY + 63) {
            guiGraphics.renderTooltip(this.font, this.menu.tile.inputTank.getFluid().getHoverName(), x, y);
        }

        if (!this.menu.tile.outputTank.isEmpty()
                && x > outputX && x < outputX + 16 && y > outputY && y < outputY + 63) {
            guiGraphics.renderTooltip(this.font, this.menu.tile.outputTank.getFluid().getHoverName(), x, y);
        }
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1) {
        guiGraphics.blit(background, leftPos, topPos, 0, 0, imageWidth, imageHeight);
        guiGraphics.blitSprite(progress, leftPos + 63, topPos + 47, menu.tile.getProgressForGui(), 23);
    }
}
