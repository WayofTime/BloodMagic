package WayofTime.bloodmagic.client.gui;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.tile.TileAlchemyTable;
import WayofTime.bloodmagic.tile.container.ContainerAlchemyTable;
import WayofTime.bloodmagic.util.helper.TextHelper;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiAlchemyTable extends GuiContainer {
    public IInventory tileTable;

    public GuiAlchemyTable(InventoryPlayer playerInventory, IInventory tileTable) {
        super(new ContainerAlchemyTable(playerInventory, tileTable));
        this.tileTable = tileTable;
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
        this.fontRenderer.drawString(TextHelper.localize("tile.bloodmagic.alchemyTable.name"), 8, 5, 4210752);
        this.fontRenderer.drawString(TextHelper.localize("container.inventory"), 8, 111, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        ResourceLocation soulForgeGuiTextures = new ResourceLocation(BloodMagic.MODID + ":textures/gui/alchemyTable.png");
        this.mc.getTextureManager().bindTexture(soulForgeGuiTextures);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);

        int l = this.getCookProgressScaled(90);
        this.drawTexturedModalRect(i + 115, j + 14 + 90 - l, 176, 90 - l, 18, l);

        for (int slotId = 0; slotId < 6; slotId++) {
            if (!((TileAlchemyTable) tileTable).isInputSlotAccessible(slotId)) {
                Slot slot = this.inventorySlots.getSlot(slotId);

                this.drawTexturedModalRect(i + slot.xPos, j + slot.yPos, 195, 1, 16, 16);
            }
        }
    }

    public int getCookProgressScaled(int scale) {
        double progress = ((TileAlchemyTable) tileTable).getProgressForGui();
        return (int) (progress * scale);
    }
}
