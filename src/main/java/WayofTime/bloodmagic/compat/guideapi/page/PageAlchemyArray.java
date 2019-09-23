package WayofTime.bloodmagic.compat.guideapi.page;

import WayofTime.bloodmagic.util.helper.TextHelper;
import amerifrance.guideapi.api.impl.Book;
import amerifrance.guideapi.api.impl.Page;
import amerifrance.guideapi.api.impl.abstraction.CategoryAbstract;
import amerifrance.guideapi.api.impl.abstraction.EntryAbstract;
import amerifrance.guideapi.api.util.GuiHelper;
import amerifrance.guideapi.gui.GuiBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PageAlchemyArray extends Page {
    public static final double scale = 58d / 256d;
    public final ItemStack inputStack;
    public final ItemStack catalystStack;
    public final ItemStack outputStack;
    public List<ResourceLocation> arrayResources = new ArrayList<>();

    public PageAlchemyArray(List<ResourceLocation> arrayResources, ItemStack inputStack, ItemStack catalystStack, ItemStack outputStack) {
        this.arrayResources = arrayResources;
        this.inputStack = inputStack;
        this.catalystStack = catalystStack;
        this.outputStack = outputStack;
    }

    public PageAlchemyArray(List<ResourceLocation> resources, ItemStack inputStack, ItemStack catalystStack) {
        this(resources, inputStack, catalystStack, ItemStack.EMPTY);
    }

    public PageAlchemyArray(ResourceLocation resource, ItemStack inputStack, ItemStack catalystStack, ItemStack outputStack) {
        this(Collections.singletonList(resource), inputStack, catalystStack, outputStack);
    }

    public PageAlchemyArray(ResourceLocation resource, ItemStack inputStack, ItemStack catalystStack) {
        this(Collections.singletonList(resource), inputStack, catalystStack);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void draw(Book book, CategoryAbstract category, EntryAbstract entry, int guiLeft, int guiTop, int mouseX, int mouseY, GuiBase guiBase, FontRenderer fontRenderer) {
        int x = guiLeft + 65;
        int y = guiTop + 30;

        Minecraft.getInstance().getTextureManager().bindTexture(new ResourceLocation("bloodmagicguide" + ":textures/gui/alchemyArrayCrafting.png"));
        guiBase.drawTexturedModalRect(x, y, 0, 0, 62, 88 + (outputStack.isEmpty() ? 0 : 26));

        guiBase.drawCenteredString(fontRenderer, TextHelper.localize("guide.bloodmagic.page.alchemyArray"), guiLeft + guiBase.xSize / 2, guiTop + 12, 0);

        for (ResourceLocation arrayResource : arrayResources) {
            Minecraft.getInstance().getTextureManager().bindTexture(arrayResource);
            GlStateManager.pushMatrix();
            GlStateManager.translate(x + 2, y + 28, 0);
            GlStateManager.scale(scale, scale, scale);
            guiBase.drawTexturedModalRect(0, 0, 0, 0, 256, 256);
            GlStateManager.popMatrix();
        }

        int inputX = x + 3;
        int inputY = y + 3;
        GuiHelper.drawItemStack(inputStack, inputX, inputY);

        int catalystX = x + 43;
        int catalystY = y + 3;
        GuiHelper.drawItemStack(catalystStack, catalystX, catalystY);

        if (GuiHelper.isMouseBetween(mouseX, mouseY, inputX, inputY, 15, 15)) {
            guiBase.renderToolTip(inputStack, mouseX, mouseY);
        }

        if (GuiHelper.isMouseBetween(mouseX, mouseY, catalystX, catalystY, 15, 15)) {
            guiBase.renderToolTip(catalystStack, mouseX, mouseY);
        }

        if (!outputStack.isEmpty()) {
            int outputX = x + 43;
            int outputY = y + 95;

            GuiHelper.drawItemStack(outputStack, outputX, outputY);
            if (GuiHelper.isMouseBetween(mouseX, mouseY, outputX, outputY, 15, 15)) {
                guiBase.renderToolTip(outputStack, mouseX, mouseY);
            }
        }
    }
}