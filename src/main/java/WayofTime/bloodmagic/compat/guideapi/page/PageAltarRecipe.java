package WayofTime.bloodmagic.compat.guideapi.page;

import WayofTime.bloodmagic.util.ItemStackWrapper;
import WayofTime.bloodmagic.core.registry.AltarRecipeRegistry;
import WayofTime.bloodmagic.util.helper.TextHelper;
import amerifrance.guideapi.api.impl.Book;
import amerifrance.guideapi.api.impl.Page;
import amerifrance.guideapi.api.impl.abstraction.CategoryAbstract;
import amerifrance.guideapi.api.impl.abstraction.EntryAbstract;
import amerifrance.guideapi.api.util.GuiHelper;
import amerifrance.guideapi.gui.GuiBase;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class PageAltarRecipe extends Page {

    public List<ItemStack> input;
    public ItemStack output;
    public int tier;
    public int bloodRequired;

    public PageAltarRecipe(AltarRecipeRegistry.AltarRecipe recipe) {
        this.input = ItemStackWrapper.toStackList(recipe.getInput());
        this.output = recipe.getOutput();
        this.tier = recipe.getMinTier().toInt();
        this.bloodRequired = recipe.getSyphon();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void draw(Book book, CategoryAbstract category, EntryAbstract entry, int guiLeft, int guiTop, int mouseX, int mouseY, GuiBase guiBase, FontRenderer fontRenderer) {
        Minecraft.getInstance().getTextureManager().bindTexture(new ResourceLocation("bloodmagicguide" + ":textures/gui/altar.png"));
        guiBase.drawTexturedModalRect(guiLeft + 42, guiTop + 53, 0, 0, 146, 104);

        guiBase.drawCenteredString(fontRenderer, TextHelper.localize("guide.bloodmagic.page.bloodAltar"), guiLeft + guiBase.xSize / 2, guiTop + 12, 0);

        int inputX = (1 + 1) * 20 + (guiLeft + guiBase.xSize / 7) + 1;
        int inputY = (20) + (guiTop + guiBase.ySize / 5) - 1; //1 * 20
        GuiHelper.drawItemStack(input.get(0), inputX, inputY);
        if (GuiHelper.isMouseBetween(mouseX, mouseY, inputX, inputY, 15, 15)) {
            guiBase.renderToolTip(input.get(0), mouseX, mouseY);
        }

        if (output.isEmpty()) {
            output = new ItemStack(Blocks.BARRIER);
        }
        int outputX = (5 * 20) + (guiLeft + guiBase.xSize / 7) + 1;
        int outputY = (20) + (guiTop + guiBase.xSize / 5) - 1; // 1 * 20
        GuiHelper.drawItemStack(output, outputX, outputY);
        if (GuiHelper.isMouseBetween(mouseX, mouseY, outputX, outputY, 15, 15)) {
            guiBase.renderToolTip(output, outputX, outputY);
        }

        if (output.getItem() == Item.getItemFromBlock(Blocks.BARRIER)) {
            guiBase.drawCenteredString(fontRenderer, TextHelper.localize("text.furnace.error"), guiLeft + guiBase.xSize / 2, guiTop + 4 * guiBase.ySize / 6, 0xED073D);
            guiBase.drawCenteredString(fontRenderer, TextHelper.localize("bm.string.tier") + ": " + String.valueOf(tier), guiLeft + guiBase.xSize / 2, guiTop + 4 * guiBase.ySize / 6 + 15, 0);
            guiBase.drawCenteredString(fontRenderer, "LP: " + String.valueOf(bloodRequired), guiLeft + guiBase.xSize / 2, guiTop + 4 * guiBase.ySize / 6 + 30, 0);
        }
        guiBase.drawCenteredString(fontRenderer, TextHelper.localize("guide.bloodmagic.page.tier", String.valueOf(tier)), guiLeft + guiBase.xSize / 2, guiTop + 4 * guiBase.ySize / 6, 0);
        guiBase.drawCenteredString(fontRenderer, TextHelper.localize("guide.bloodmagic.page.lp", String.valueOf(bloodRequired)), guiLeft + guiBase.xSize / 2, guiTop + 4 * guiBase.ySize / 6 + 15, 0);
    }
}
