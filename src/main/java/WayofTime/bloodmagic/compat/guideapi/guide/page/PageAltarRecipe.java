package WayofTime.bloodmagic.compat.guideapi.guide.page;

import WayofTime.bloodmagic.api.altar.EnumAltarTier;
import WayofTime.bloodmagic.api.registry.AltarRecipeRegistry;
import WayofTime.bloodmagic.util.helper.TextHelper;
import amerifrance.guideapi.api.abstraction.CategoryAbstract;
import amerifrance.guideapi.api.abstraction.EntryAbstract;
import amerifrance.guideapi.api.base.Book;
import amerifrance.guideapi.api.base.PageBase;
import amerifrance.guideapi.api.util.GuiHelper;
import amerifrance.guideapi.gui.GuiBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PageAltarRecipe extends PageBase
{

    public ItemStack input;
    public ItemStack output;
    public EnumAltarTier tier;
    public int bloodRequired;

    public PageAltarRecipe(AltarRecipeRegistry.AltarRecipe recipe)
    {
        this.input = recipe.getInput();
        this.output = recipe.getOutput();
        this.tier = recipe.getMinTier();
        this.bloodRequired = recipe.getSyphon();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void draw(Book book, CategoryAbstract category, EntryAbstract entry, int guiLeft, int guiTop, int mouseX, int mouseY, GuiBase guiBase, FontRenderer fontRenderer)
    {
        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("bloodmagicguide" + ":textures/gui/altar.png"));
        guiBase.drawTexturedModalRect(guiLeft + 42, guiTop + 53, 0, 0, 146, 104);

        guiBase.drawCenteredString(fontRenderer, TextHelper.localize("guide.BloodMagic.page.bloodAltar"), guiLeft + guiBase.xSize / 2, guiTop + 12, 0);

        int inputX = (1 + 1) * 20 + (guiLeft + guiBase.xSize / 7) + 1;
        int inputY = (20) + (guiTop + guiBase.ySize / 5) - 1; //1 * 20
        GuiHelper.drawItemStack(input, inputX, inputY);

        if (output == null)
            output = new ItemStack(Blocks.barrier);

        int outputX = (5 * 20) + (guiLeft + guiBase.xSize / 7) + 1;
        int outputY = (20) + (guiTop + guiBase.xSize / 5) - 1; // 1 * 20
        GuiHelper.drawItemStack(output, outputX, outputY);

        if (GuiHelper.isMouseBetween(mouseX, mouseY, inputX, inputY, 15, 15))
            guiBase.renderToolTip(input, mouseX, mouseY);

        if (GuiHelper.isMouseBetween(mouseX, mouseY, outputX, outputY, 15, 15))
            guiBase.renderToolTip(output, mouseX, mouseY);

        if (output.getItem() == Item.getItemFromBlock(Blocks.barrier))
        {
            guiBase.drawCenteredString(fontRenderer, TextHelper.localize("text.furnace.error"), guiLeft + guiBase.xSize / 2, guiTop + 4 * guiBase.ySize / 6, 0xED073D);
            return;
        }

        guiBase.drawCenteredString(fontRenderer, TextHelper.localize("guide.BloodMagic.page.tier", tier.toInt()), guiLeft + guiBase.xSize / 2, guiTop + 4 * guiBase.ySize / 6, 0);
        guiBase.drawCenteredString(fontRenderer, TextHelper.localize("guide.BloodMagic.page.lp", bloodRequired), guiLeft + guiBase.xSize / 2, guiTop + 4 * guiBase.ySize / 6 + 15, 0);
    }
}