package WayofTime.bloodmagic.client.gui;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.tile.TileSoulForge;
import WayofTime.bloodmagic.tile.container.ContainerSoulForge;
import WayofTime.bloodmagic.util.helper.TextHelper;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiSoulForge extends GuiContainer {
    public IInventory tileSoulForge;

    public GuiSoulForge(InventoryPlayer playerInventory, IInventory tileSoulForge) {
        super(new ContainerSoulForge(playerInventory, tileSoulForge));
        this.tileSoulForge = tileSoulForge;
        this.xSize = 176;
        this.ySize = 205;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.fontRenderer.drawString(TextHelper.localize("tile.bloodmagic.soulForge.name"), 8, 5, 4210752);
        this.fontRenderer.drawString(TextHelper.localize("container.inventory"), 8, 111, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        ResourceLocation soulForgeGuiTextures = new ResourceLocation(BloodMagic.MODID + ":textures/gui/soulForge.png");
        this.mc.getTextureManager().bindTexture(soulForgeGuiTextures);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);

        int l = this.getCookProgressScaled(90);
        this.drawTexturedModalRect(i + 115, j + 14 + 90 - l, 176, 90 - l, 18, l);
    }

    public int getCookProgressScaled(int scale) {
        double progress = ((TileSoulForge) tileSoulForge).getProgressForGui();
        return (int) (progress * scale);
    }
}
