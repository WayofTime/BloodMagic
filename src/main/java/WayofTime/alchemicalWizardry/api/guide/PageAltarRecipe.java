package WayofTime.alchemicalWizardry.api.guide;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import WayofTime.alchemicalWizardry.api.altarRecipeRegistry.AltarRecipe;
import amerifrance.guideapi.api.abstraction.CategoryAbstract;
import amerifrance.guideapi.api.abstraction.EntryAbstract;
import amerifrance.guideapi.api.base.Book;
import amerifrance.guideapi.api.base.PageBase;
import amerifrance.guideapi.api.util.GuiHelper;
import amerifrance.guideapi.gui.GuiBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PageAltarRecipe extends PageBase {

    public ItemStack input;
    public ItemStack output;
    public int tier;
    public int bloodRequired;

    public PageAltarRecipe(AltarRecipe recipe) {
        this.input = recipe.getRequiredItem();
        this.output = recipe.getResult();
        this.tier = recipe.getMinTier();
        this.bloodRequired = recipe.getLiquidRequired();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void draw(Book book, CategoryAbstract category, EntryAbstract entry, int guiLeft, int guiTop, int mouseX, int mouseY, GuiBase guiBase, FontRenderer fontRenderer) {

        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("alchemicalwizardry" + ":textures/gui/guide/altar.png"));
        guiBase.drawTexturedModalRect(guiLeft + 42, guiTop + 53, 0, 87, 146, 104);

        guiBase.drawCenteredString(fontRenderer, StatCollector.translateToLocal("text.recipe.altar"), guiLeft + guiBase.xSize / 2, guiTop + 12, 0);

        int inputX = (1 + 1) * 20 + (guiLeft + guiBase.xSize / 7);
        int inputY = (1 * 20) + (guiTop + guiBase.ySize / 5);
        GuiHelper.drawItemStack(input, inputX, inputY);
        if (GuiHelper.isMouseBetween(mouseX, mouseY, inputX, inputY, 15, 15)) {
            guiBase.renderToolTip(input, mouseX, mouseY);
        }

        if (output == null) {
            output = new ItemStack(Blocks.fire);
        }
        int outputX = (5 * 20) + (guiLeft + guiBase.xSize / 7);
        int outputY = (1 * 20) + (guiTop + guiBase.xSize / 5);
        GuiHelper.drawItemStack(output, outputX, outputY);
        if (GuiHelper.isMouseBetween(mouseX, mouseY, outputX, outputY, 15, 15)) {
            guiBase.renderToolTip(output, outputX, outputY);
        }

        if (output.getItem() == Item.getItemFromBlock(Blocks.fire)) {
            guiBase.drawCenteredString(fontRenderer, StatCollector.translateToLocal("text.furnace.error"), guiLeft + guiBase.xSize / 2, guiTop + 4 * guiBase.ySize / 6, 0xED073D);
            guiBase.drawCenteredString(fontRenderer, StatCollector.translateToLocal("bm.string.tier") + ": " + String.valueOf(tier), guiLeft + guiBase.xSize / 2, guiTop + 4 * guiBase.ySize / 6 + 15, 0);
            guiBase.drawCenteredString(fontRenderer, "LP: " + String.valueOf(bloodRequired), guiLeft + guiBase.xSize / 2, guiTop + 4 * guiBase.ySize / 6 + 30, 0);
        }
        guiBase.drawCenteredString(fontRenderer, String.format(StatCollector.translateToLocal("text.recipe.altar.tier"), String.valueOf(tier)), guiLeft + guiBase.xSize / 2, guiTop + 4 * guiBase.ySize / 6, 0);
        guiBase.drawCenteredString(fontRenderer, String.format(StatCollector.translateToLocal("text.recipe.altar.bloodRequired"), String.valueOf(bloodRequired)), guiLeft + guiBase.xSize / 2, guiTop + 4 * guiBase.ySize / 6 + 15, 0);
    }
}
