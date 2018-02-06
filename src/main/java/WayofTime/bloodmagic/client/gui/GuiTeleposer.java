package WayofTime.bloodmagic.client.gui;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.tile.container.ContainerTeleposer;
import WayofTime.bloodmagic.util.helper.TextHelper;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiTeleposer extends GuiContainer {
    public GuiTeleposer(InventoryPlayer playerInventory, IInventory tileTeleposer) {
        super(new ContainerTeleposer(playerInventory, tileTeleposer));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.fontRenderer.drawString(TextHelper.localize("tile.bloodmagic.teleposer.name"), 64, 23, 4210752);
        this.fontRenderer.drawString(TextHelper.localize("container.inventory"), 8, 47, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        ResourceLocation teleposerGuiTextures = new ResourceLocation(BloodMagic.MODID + ":textures/gui/teleposer.png");
        this.mc.getTextureManager().bindTexture(teleposerGuiTextures);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j + 18, 0, 0, this.xSize, this.ySize);
    }
}
