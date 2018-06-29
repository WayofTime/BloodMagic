package WayofTime.bloodmagic.client.hud.element;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;

public class ElementItemStack extends HUDElement {
    private ItemStack stack;

    public ElementItemStack(ItemStack stack) {
        super(16, 16);

        this.stack = stack;
    }

    @Override
    public void draw(ScaledResolution resolution, float partialTicks, int drawX, int drawY) {
        RenderHelper.enableGUIStandardItemLighting();
        Minecraft.getMinecraft().getRenderItem().renderItemIntoGUI(stack, drawX, drawY);
        RenderHelper.disableStandardItemLighting();
    }
}
