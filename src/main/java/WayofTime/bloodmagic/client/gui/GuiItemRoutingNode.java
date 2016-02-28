package WayofTime.bloodmagic.client.gui;

import java.io.IOException;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.network.BloodMagicPacketHandler;
import WayofTime.bloodmagic.network.ItemRouterButtonPacketProcessor;
import WayofTime.bloodmagic.tile.container.ContainerItemRoutingNode;
import WayofTime.bloodmagic.tile.routing.TileFilteredRoutingNode;

@SideOnly(Side.CLIENT)
public class GuiItemRoutingNode extends GuiContainer
{
    private GuiButton downButton;
    private GuiButton upButton;
    private GuiButton northButton;
    private GuiButton southButton;
    private GuiButton westButton;
    private GuiButton eastButton;
    private GuiButton incrementButton;
    private GuiButton decrementButton;

    private TileFilteredRoutingNode inventory;

    private int left, top;

    public GuiItemRoutingNode(InventoryPlayer playerInventory, IInventory tileRoutingNode)
    {
        super(new ContainerItemRoutingNode(playerInventory, tileRoutingNode));
        this.xSize = 176;
        this.ySize = 169;
        inventory = (TileFilteredRoutingNode) tileRoutingNode;
    }

    private int getCurrentActiveSlotPriority()
    {
        EnumFacing direction = EnumFacing.getFront(inventory.currentActiveSlot);
        if (direction != null)
        {
            return inventory.getPriority(direction);
        }

        return 0;
    }

    @Override
    public void initGui()
    {
        super.initGui();
        left = (this.width - this.xSize) / 2;
        top = (this.height - this.ySize) / 2;

        this.buttonList.clear();
        this.buttonList.add(this.downButton = new GuiButton(0, left + 133, top + 50, 18, 17, "D"));
        this.buttonList.add(this.upButton = new GuiButton(1, left + 133, top + 14, 18, 18, "U"));
        this.buttonList.add(this.northButton = new GuiButton(2, left + 150, top + 14, 18, 18, "N"));
        this.buttonList.add(this.southButton = new GuiButton(3, left + 150, top + 50, 18, 17, "S"));
        this.buttonList.add(this.westButton = new GuiButton(4, left + 133, top + 32, 18, 18, "W"));
        this.buttonList.add(this.eastButton = new GuiButton(5, left + 150, top + 32, 18, 18, "E"));
        this.buttonList.add(this.incrementButton = new GuiButton(6, left + 97, top + 14, 18, 17, "^"));
        this.buttonList.add(this.decrementButton = new GuiButton(7, left + 97, top + 50, 18, 17, "v"));
        disableDirectionalButton(inventory.currentActiveSlot);
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed
     * for buttons)
     */
    @Override
    protected void actionPerformed(GuiButton button) throws IOException
    {
        if (button.enabled)
        {
            BloodMagicPacketHandler.INSTANCE.sendToServer(new ItemRouterButtonPacketProcessor(button.id, inventory.getPos(), inventory.getWorld()));
            if (button.id < 6)
            {
                enableAllDirectionalButtons();
                button.enabled = false;
            }
        }
    }

    private void enableAllDirectionalButtons()
    {
        for (GuiButton button : this.buttonList)
        {
            button.enabled = true;
        }
    }

    private void disableDirectionalButton(int id)
    {
        this.buttonList.get(id).enabled = false;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        this.fontRendererObj.drawString("" + getCurrentActiveSlotPriority(), 98 + 5, 33 + 4, 0xFFFFFF);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        ResourceLocation soulForgeGuiTextures = new ResourceLocation(Constants.Mod.MODID + ":textures/gui/routingNode.png");
        this.mc.getTextureManager().bindTexture(soulForgeGuiTextures);
        this.drawTexturedModalRect(left, top, 0, 0, this.xSize, this.ySize);
    }
}
