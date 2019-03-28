package WayofTime.bloodmagic.client.gui;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.network.BloodMagicPacketHandler;
import WayofTime.bloodmagic.network.ItemRouterAmountPacketProcessor;
import WayofTime.bloodmagic.network.ItemRouterButtonPacketProcessor;
import WayofTime.bloodmagic.tile.container.ContainerItemRoutingNode;
import WayofTime.bloodmagic.tile.routing.TileFilteredRoutingNode;
import WayofTime.bloodmagic.util.GhostItemHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

@SideOnly(Side.CLIENT)
public class GuiItemRoutingNode extends GuiContainer {
    private GuiTextField textBox;

    private TileFilteredRoutingNode inventory;
    private ContainerItemRoutingNode container;

    private int left, top;

    public GuiItemRoutingNode(InventoryPlayer playerInventory, IInventory tileRoutingNode) {
        super(new ContainerItemRoutingNode(playerInventory, tileRoutingNode));
        this.xSize = 201;
        this.ySize = 169;
        inventory = (TileFilteredRoutingNode) tileRoutingNode;
        container = (ContainerItemRoutingNode) this.inventorySlots;
    }

    private int getCurrentActiveSlotPriority() {
        EnumFacing direction = EnumFacing.byIndex(inventory.currentActiveSlot);
        if (direction != null) {
            return inventory.getPriority(direction);
        }

        return 0;
    }

    @Override
    public void initGui() {
        super.initGui();
        left = (this.width - this.xSize) / 2;
        top = (this.height - this.ySize) / 2;

        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, left + 176, top + 14, 18, 18, "D"));
        this.buttonList.add(new GuiButton(1, left + 176, top + 32, 18, 18, "U"));
        this.buttonList.add(new GuiButton(2, left + 176, top + 50, 18, 18, "N"));
        this.buttonList.add(new GuiButton(3, left + 176, top + 68, 18, 18, "S"));
        this.buttonList.add(new GuiButton(4, left + 176, top + 86, 18, 18, "W"));
        this.buttonList.add(new GuiButton(5, left + 176, top + 104, 18, 18, "E"));
        this.buttonList.add(new GuiButton(6, left + 160, top + 50, 10, 18, ">"));
        this.buttonList.add(new GuiButton(7, left + 132, top + 50, 10, 18, "<"));
        disableDirectionalButton(inventory.currentActiveSlot);

        this.textBox = new GuiTextField(0, this.fontRenderer, left + 94, top + 37, 70, 12);
        this.textBox.setEnableBackgroundDrawing(false);
        this.textBox.setText("");
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (this.textBox.textboxKeyTyped(typedChar, keyCode)) {
            if (container.lastGhostSlotClicked != -1) {
//              this.renameItem();
                String str = this.textBox.getText();
                int amount = 0;

                if (!str.isEmpty()) {
                    try {
                        Integer testVal = Integer.decode(str);
                        if (testVal != null) {
                            amount = testVal;
                        }
                    } catch (NumberFormatException d) {
                    }
                }

//                inventory.setGhostItemAmount(container.lastGhostSlotClicked, amount);
                setValueOfGhostItemInSlot(container.lastGhostSlotClicked, amount);
            }
        } else {
            super.keyTyped(typedChar, keyCode);
        }
    }

    private void setValueOfGhostItemInSlot(int ghostItemSlot, int amount) {
        BloodMagicPacketHandler.INSTANCE.sendToServer(new ItemRouterAmountPacketProcessor(ghostItemSlot, amount, inventory.getPos(), inventory.getWorld()));
    }

    /**
     * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
     */
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.textBox.mouseClicked(mouseX, mouseY, mouseButton);
        if (container.lastGhostSlotClicked != -1) {
            Slot slot = container.getSlot(container.lastGhostSlotClicked + 1);
            ItemStack stack = slot.getStack();
            if (!stack.isEmpty()) {
                int amount = GhostItemHelper.getItemGhostAmount(stack);
                this.textBox.setText("" + amount);
            } else {
                this.textBox.setText("");
            }
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);

        Minecraft.getMinecraft().fontRenderer.drawString(inventory.getName(), xSize, ySize / 4, 4210752);
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed
     * for buttons)
     */
    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.enabled) {
            BloodMagicPacketHandler.INSTANCE.sendToServer(new ItemRouterButtonPacketProcessor(button.id, inventory.getPos(), inventory.getWorld()));
            if (button.id < 6) {
                inventory.currentActiveSlot = button.id;
                enableAllDirectionalButtons();
                button.enabled = false;
            }
        }
    }

    private void enableAllDirectionalButtons() {
        for (GuiButton button : this.buttonList) {
            button.enabled = true;
        }
    }

    private void disableDirectionalButton(int id) {
        this.buttonList.get(id).enabled = false;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.fontRenderer.drawString("" + getCurrentActiveSlotPriority(), 143 + 5, 51 + 4, 0xFFFFFF);
        String s = "";
        if (container.lastGhostSlotClicked != -1) {
            ItemStack clickedStack = inventorySlots.getSlot(1 + container.lastGhostSlotClicked).getStack();
            if (!clickedStack.isEmpty()) {
                s = clickedStack.getDisplayName();
            }
        }

        this.fontRenderer.drawStringWithShadow(s.substring(0, Math.min(16, s.length())), 81, 19, 0xFFFFFF);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        ResourceLocation soulForgeGuiTextures = new ResourceLocation(BloodMagic.MODID + ":textures/gui/routingNode.png");
        this.mc.getTextureManager().bindTexture(soulForgeGuiTextures);
        this.drawTexturedModalRect(left, top, 0, 0, this.xSize, this.ySize);
        GlStateManager.disableLighting();
        GlStateManager.disableBlend();
        this.textBox.drawTextBox();
    }

//    @Override
//    public void sendSlotContents(Container containerToSend, int slotInd, ItemStack stack)
//    {
//        if (slotInd == 0)
//        {
//            this.nameField.setText(stack == null ? "" : stack.getOwnerName());
//            this.nameField.setEnabled(stack != null);
//
//            if (stack != null)
//            {
//                this.renameItem();
//            }
//        }
//    }
}
