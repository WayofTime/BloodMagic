package WayofTime.alchemicalWizardry.common.tileEntity.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import WayofTime.alchemicalWizardry.common.tileEntity.TETeleposer;
import WayofTime.alchemicalWizardry.common.tileEntity.container.ContainerTeleposer;

public class GuiTeleposer extends GuiContainer
{
	public GuiTeleposer(InventoryPlayer inventoryPlayer, TETeleposer tileEntity)
	{
		//the container is instanciated and passed to the superclass for handling
		super(new ContainerTeleposer(inventoryPlayer, tileEntity));
		xSize = 176;
		ySize = 222;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int param1, int param2)
	{
		//draw text and stuff here
		//the parameters for drawString are: string, x, y, color
		fontRenderer.drawString("Teleposer", 8, 6, 4210752);
		//draws "Inventory" or your regional equivalent
		fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, 130, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		//draw your Gui here, only thing you need to change is the path
		//ResourceLocation texture = mc.renderEngine.getTexture("/gui/trap.png");
		ResourceLocation test = new ResourceLocation("alchemicalwizardry", "gui/Teleposer.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(test);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
	}
}