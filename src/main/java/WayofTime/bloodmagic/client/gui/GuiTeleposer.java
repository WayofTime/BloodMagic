package WayofTime.bloodmagic.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.tile.container.ContainerTeleposer;
import WayofTime.bloodmagic.util.helper.TextHelper;

@SideOnly(Side.CLIENT)
public class GuiTeleposer extends GuiContainer
{
    public GuiTeleposer(InventoryPlayer playerInventory, IInventory tileTeleposer)
    {
        super(new ContainerTeleposer(playerInventory, tileTeleposer));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        this.fontRendererObj.drawString(TextHelper.localize("tile.BloodMagic.teleposer.name"), 64, 23, 4210752);
        this.fontRendererObj.drawString(TextHelper.localize("container.inventory"), 8, 47, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        ResourceLocation teleposerGuiTextures = new ResourceLocation(Constants.Mod.MODID + ":textures/gui/teleposer.png");
        this.mc.getTextureManager().bindTexture(teleposerGuiTextures);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j + 18, 0, 0, this.xSize, this.ySize);
    }
}
