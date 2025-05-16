package wayoftime.bloodmagic.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.client.menu.ARCMenu;

public class ARCScreen extends AbstractContainerScreen<ARCMenu> {
    private final ResourceLocation background = ResourceLocation.fromNamespaceAndPath(BloodMagic.MODID, "textures/gui/arc_gui.png");
    public ARCScreen(ARCMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 176;
        this.imageHeight = 205;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, 8, 5, 4210752, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, 8, 111, 4210752, false);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1) {
        int left = (this.width - this.imageWidth) / 2;
        int top = (this.height - this.imageHeight) / 2;
        guiGraphics.blit(background, left, top, 0, 0, imageWidth, imageHeight);
        guiGraphics.blit(background, left + 63, top + 44, 176, 90, menu.tile.getProgressForGui(), 23);
    }
}
