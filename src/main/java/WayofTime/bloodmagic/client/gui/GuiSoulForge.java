package WayofTime.bloodmagic.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.tile.container.ContainerSoulForge;
import WayofTime.bloodmagic.util.helper.TextHelper;

@SideOnly(Side.CLIENT)
public class GuiSoulForge extends GuiContainer
{
    public GuiSoulForge(InventoryPlayer playerInventory, IInventory tileTeleposer)
    {
        super(new ContainerSoulForge(playerInventory, tileTeleposer));
        this.xSize = 176;
        this.ySize = 157;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        this.fontRendererObj.drawString(TextHelper.localize("tile.BloodMagic.soulForge.name"), 64, 23, 4210752);
        this.fontRendererObj.drawString(TextHelper.localize("container.inventory"), 8, 47, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        ResourceLocation soulForgeGuiTextures = new ResourceLocation(Constants.Mod.MODID + ":textures/gui/soulForge.png");
        this.mc.getTextureManager().bindTexture(soulForgeGuiTextures);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);

        int l = this.getCookProgressScaled(36);
        this.drawTexturedModalRect(i + 79, j + 32, 176, 0, l, 18);
    }

    public int getCookProgressScaled(int scale)
    {
        double progress = 1;
        return (int) (progress * scale);
    }
}
