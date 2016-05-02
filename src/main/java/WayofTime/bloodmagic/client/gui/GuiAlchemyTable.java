package WayofTime.bloodmagic.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.tile.container.ContainerAlchemyTable;
import WayofTime.bloodmagic.util.helper.TextHelper;

@SideOnly(Side.CLIENT)
public class GuiAlchemyTable extends GuiContainer
{
    public IInventory tileTable;

    public GuiAlchemyTable(InventoryPlayer playerInventory, IInventory tileTable)
    {
        super(new ContainerAlchemyTable(playerInventory, tileTable));
        this.tileTable = tileTable;
        this.xSize = 176;
        this.ySize = 205;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        this.fontRendererObj.drawString(TextHelper.localize("tile.BloodMagic.alchemyTable.name"), 8, 5, 4210752);
        this.fontRendererObj.drawString(TextHelper.localize("container.inventory"), 8, 111, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        ResourceLocation soulForgeGuiTextures = new ResourceLocation(Constants.Mod.MODID + ":textures/gui/alchemyTable.png");
        this.mc.getTextureManager().bindTexture(soulForgeGuiTextures);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);

        int l = this.getCookProgressScaled(90);
        this.drawTexturedModalRect(i + 115, j + 14 + 90 - l, 176, 90 - l, 18, l);
    }

    public int getCookProgressScaled(int scale)
    {
//        double progress = ((TileAlchemyTable) tileTable).getProgressForGui();
//        return (int) (progress * scale);
        return scale / 2;
    }
}
