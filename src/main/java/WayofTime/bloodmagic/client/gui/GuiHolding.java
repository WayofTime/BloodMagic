package WayofTime.bloodmagic.client.gui;

import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.item.inventory.ContainerHolding;
import WayofTime.bloodmagic.item.inventory.InventoryHolding;
import WayofTime.bloodmagic.item.sigil.ItemSigilHolding;
import WayofTime.bloodmagic.util.helper.TextHelper;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiHolding extends GuiContainer
{
    private ResourceLocation texture = new ResourceLocation(Constants.Mod.MODID, "gui/SigilHolding.png");
    private EntityPlayer player;

    public GuiHolding(EntityPlayer player, InventoryHolding inventoryHolding)
    {
        super(new ContainerHolding(player, inventoryHolding));
        xSize = 176;
        ySize = 121;
        this.player = player;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int param1, int param2)
    {
        //the parameters for drawString are: string, x, y, color
        fontRendererObj.drawString(TextHelper.localize("item.BloodMagic.sigil.holding.name"), 53, 4, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        //draw your Gui here, only thing you need to change is the path
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(texture);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);

        if (player != null && player.getHeldItemMainhand() != null && player.getHeldItemMainhand().getItem() instanceof ItemSigilHolding)
        {
            if (ItemSigilHolding.getCurrentSigil(player.getHeldItemMainhand()) != null)
            {
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                this.mc.getTextureManager().bindTexture(texture);
                this.drawTexturedModalRect(4 + x + 36 * ItemSigilHolding.getCurrentItemOrdinal(player.getHeldItemMainhand()), y + 13, 0, 123, 24, 24);
            }
        }
    }
}
