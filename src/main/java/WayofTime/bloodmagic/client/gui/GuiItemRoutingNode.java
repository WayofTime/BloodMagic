package WayofTime.bloodmagic.client.gui;

import io.netty.buffer.Unpooled;

import java.io.IOException;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;
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

    private GuiTextField textBox;

    private TileFilteredRoutingNode inventory;
    private ContainerItemRoutingNode container;

    private int left, top;

    public GuiItemRoutingNode(InventoryPlayer playerInventory, IInventory tileRoutingNode)
    {
        super(new ContainerItemRoutingNode(playerInventory, tileRoutingNode));
        this.xSize = 201;
        this.ySize = 169;
        inventory = (TileFilteredRoutingNode) tileRoutingNode;
        container = (ContainerItemRoutingNode) this.inventorySlots;
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
        this.buttonList.add(this.downButton = new GuiButton(0, left + 176, top + 14, 18, 18, "D"));
        this.buttonList.add(this.upButton = new GuiButton(1, left + 176, top + 32, 18, 18, "U"));
        this.buttonList.add(this.northButton = new GuiButton(2, left + 176, top + 50, 18, 18, "N"));
        this.buttonList.add(this.southButton = new GuiButton(3, left + 176, top + 68, 18, 18, "S"));
        this.buttonList.add(this.westButton = new GuiButton(4, left + 176, top + 86, 18, 18, "W"));
        this.buttonList.add(this.eastButton = new GuiButton(5, left + 176, top + 104, 18, 18, "E"));
        this.buttonList.add(this.incrementButton = new GuiButton(6, left + 97, top + 14, 18, 17, "^"));
        this.buttonList.add(this.decrementButton = new GuiButton(7, left + 97, top + 50, 18, 17, "v"));
        disableDirectionalButton(inventory.currentActiveSlot);

        this.textBox = new GuiTextField(0, this.fontRendererObj, left + 90, top + 73, 64, 12);
        this.textBox.setEnableBackgroundDrawing(false);
        this.textBox.setText("Test");
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        if (this.textBox.textboxKeyTyped(typedChar, keyCode))
        {
//            System.out.println(typedChar + ", " + keyCode);
//            this.renameItem();
        } else
        {
            super.keyTyped(typedChar, keyCode);
        }
    }

    private void renameItem()
    {
        String s = this.textBox.getText();
        Slot slot = this.container.getSlot(1);

        if (slot != null && slot.getHasStack() && !slot.getStack().hasDisplayName() && s.equals(slot.getStack().getDisplayName()))
        {
            s = "";
        }

//        this.container.updateItemName(s);
        this.mc.thePlayer.connection.sendPacket(new CPacketCustomPayload("MC|ItemName", (new PacketBuffer(Unpooled.buffer())).writeString(s)));
    }

    /**
     * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
     */
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.textBox.mouseClicked(mouseX, mouseY, mouseButton);
    }

    /**
     * Draws the screen and all the components in it.
     */
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        super.drawScreen(mouseX, mouseY, partialTicks);

        GlStateManager.disableLighting();
        GlStateManager.disableBlend();
        this.textBox.drawTextBox();
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
                inventory.currentActiveSlot = button.id;
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
        String s = "";
        if (container.lastGhostSlotClicked != -1)
        {
            ItemStack clickedStack = inventorySlots.getSlot(1 + container.lastGhostSlotClicked).getStack();
            if (clickedStack != null)
            {
                s = clickedStack.getDisplayName();
            }
        }

        this.fontRendererObj.drawStringWithShadow(s, 9, 73, 0xFFFFFF);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        ResourceLocation soulForgeGuiTextures = new ResourceLocation(Constants.Mod.MODID + ":textures/gui/routingNode.png");
        this.mc.getTextureManager().bindTexture(soulForgeGuiTextures);
        this.drawTexturedModalRect(left, top, 0, 0, this.xSize, this.ySize);
    }

//    @Override
//    public void sendSlotContents(Container containerToSend, int slotInd, ItemStack stack)
//    {
//        if (slotInd == 0)
//        {
//            this.nameField.setText(stack == null ? "" : stack.getDisplayName());
//            this.nameField.setEnabled(stack != null);
//
//            if (stack != null)
//            {
//                this.renameItem();
//            }
//        }
//    }
}
