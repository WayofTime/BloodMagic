package WayofTime.bloodmagic.client.gui;

import java.io.IOException;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.network.BloodMagicPacketHandler;
import WayofTime.bloodmagic.network.ItemRouterButtonPacketProcessor;
import WayofTime.bloodmagic.tile.container.ContainerItemRoutingNode;

@SideOnly(Side.CLIENT)
public class GuiItemRoutingNode extends GuiContainer
{
    private GuiButton downButton;
    private GuiButton upButton;
    private GuiButton northButton;
    private GuiButton southButton;
    private GuiButton westButton;
    private GuiButton eastButton;

    private TileEntity inventory;

    public GuiItemRoutingNode(InventoryPlayer playerInventory, IInventory tileRoutingNode)
    {
        super(new ContainerItemRoutingNode(playerInventory, tileRoutingNode));
        this.xSize = 176;
        this.ySize = 169;
        inventory = (TileEntity) tileRoutingNode;
    }

    @Override
    public void initGui()
    {
        super.initGui();
        this.buttonList.clear();
        this.buttonList.add(this.downButton = new GuiButton(0, (this.width - this.xSize) / 2 + 133, (this.height - this.ySize) / 2 + 50, 18, 18, "D"));
        this.buttonList.add(this.upButton = new GuiButton(1, (this.width - this.xSize) / 2 + 133, (this.height - this.ySize) / 2 + 14, 18, 18, "U"));
        this.buttonList.add(this.northButton = new GuiButton(2, (this.width - this.xSize) / 2 + 151, (this.height - this.ySize) / 2 + 14, 18, 18, "N"));
        this.buttonList.add(this.southButton = new GuiButton(3, (this.width - this.xSize) / 2 + 151, (this.height - this.ySize) / 2 + 50, 18, 18, "S"));
        this.buttonList.add(this.westButton = new GuiButton(4, (this.width - this.xSize) / 2 + 133, (this.height - this.ySize) / 2 + 32, 18, 18, "W"));
        this.buttonList.add(this.eastButton = new GuiButton(5, (this.width - this.xSize) / 2 + 151, (this.height - this.ySize) / 2 + 32, 18, 18, "E"));
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
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
//        this.fontRendererObj.drawString(TextHelper.localize("tile.BloodMagic.soulForge.name"), 8, 5, 4210752);
//        this.fontRendererObj.drawString(TextHelper.localize("container.inventory"), 8, 111, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        ResourceLocation soulForgeGuiTextures = new ResourceLocation(Constants.Mod.MODID + ":textures/gui/routingNode.png");
        this.mc.getTextureManager().bindTexture(soulForgeGuiTextures);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
    }
}
