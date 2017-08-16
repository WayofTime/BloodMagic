package WayofTime.bloodmagic.compat.guideapi.page;

import WayofTime.bloodmagic.api.recipe.TartaricForgeRecipe;
import WayofTime.bloodmagic.api.registry.OrbRegistry;
import WayofTime.bloodmagic.util.helper.TextHelper;
import amerifrance.guideapi.api.impl.Book;
import amerifrance.guideapi.api.impl.Page;
import amerifrance.guideapi.api.impl.abstraction.CategoryAbstract;
import amerifrance.guideapi.api.impl.abstraction.EntryAbstract;
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
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;
import java.util.Random;

public class PageTartaricForgeRecipe extends Page {
    public List<Object> input;
    public ItemStack output;
    public int tier;
    public double minimumWill;
    public double drainedWill;

    private int cycleIdx = 0;
    private Random rand = new Random();

    public PageTartaricForgeRecipe(TartaricForgeRecipe recipe) {
        this.input = recipe.getInput();
        this.output = recipe.getRecipeOutput();
        this.tier = 0;
        this.minimumWill = recipe.getMinimumSouls();
        this.drainedWill = recipe.getSoulsDrained();
    }

    @SuppressWarnings("unchecked")
    @Override
    @SideOnly(Side.CLIENT)
    public void draw(Book book, CategoryAbstract category, EntryAbstract entry, int guiLeft, int guiTop, int mouseX, int mouseY, GuiBase guiBase, FontRenderer fontRenderer) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("bloodmagicguide" + ":textures/gui/soulForge.png"));
        guiBase.drawTexturedModalRect(guiLeft + 42, guiTop + 53, 0, 0, 146, 104);

        guiBase.drawCenteredString(fontRenderer, TextHelper.localize("guide.bloodmagic.page.soulForge"), guiLeft + guiBase.xSize / 2, guiTop + 12, 0);

//        int inputX = (1 + 1) * 20 + (guiLeft + guiBase.xSize / 7) + 1;
//        int inputY = (20) + (guiTop + guiBase.ySize / 5) - 1; //1 * 20

        for (int y = 0; y < 2; y++) {
            for (int x = 0; x < 2; x++) {
                int stackX = (x + 1) * 20 + (guiLeft + guiBase.xSize / 7) + 1;
                int stackY = (y + 1) * 20 + (guiTop + guiBase.ySize / 5) - 1;
                Object component = input.size() > y * 2 + x ? input.get(y * 2 + x) : null;//recipe.getInput()[y * 2 + x];
                if (component != null) {
                    if (component instanceof ItemStack) {
                        ItemStack input = (ItemStack) component;
                        if (input.getItemDamage() == OreDictionary.WILDCARD_VALUE)
                            input.setItemDamage(0);

                        GuiHelper.drawItemStack((ItemStack) component, stackX, stackY);
                        if (GuiHelper.isMouseBetween(mouseX, mouseY, stackX, stackY, 15, 15)) {
//                            tooltips = GuiHelper.getTooltip((ItemStack) component);
                            guiBase.renderToolTip((ItemStack) component, mouseX, mouseY);
                        }
                    } else if (component instanceof Integer) {
                        List<ItemStack> list = OrbRegistry.getOrbsDownToTier((Integer) component);
                        if (!list.isEmpty()) {
                            ItemStack stack = list.get(getRandomizedCycle(x + (y * 2), list.size()));
                            GuiHelper.drawItemStack(stack, stackX, stackY);
                            if (GuiHelper.isMouseBetween(mouseX, mouseY, stackX, stackY, 15, 15)) {
//                                tooltips = GuiHelper.getTooltip(stack);
                                guiBase.renderToolTip(stack, mouseX, mouseY);
                            }
                        }
                    } else {
                        List<ItemStack> list = (List<ItemStack>) component;
                        if (!list.isEmpty()) {
                            ItemStack stack = list.get(getRandomizedCycle(x + (y * 2), list.size()));
                            GuiHelper.drawItemStack(stack, stackX, stackY);
                            if (GuiHelper.isMouseBetween(mouseX, mouseY, stackX, stackY, 15, 15)) {
//                                tooltips = GuiHelper.getTooltip(stack);
                                guiBase.renderToolTip(stack, mouseX, mouseY);
                            }
                        }
                    }
                }
            }
        }

//        GuiHelper.drawItemStack(input.get(0), inputX, inputY);
//        if (GuiHelper.isMouseBetween(mouseX, mouseY, inputX, inputY, 15, 15))
//        {
//            guiBase.renderToolTip(input.get(0), mouseX, mouseY);
//        }

        if (output == null) {
            output = new ItemStack(Blocks.BARRIER);
        }
        int outputX = (5 * 20) + (guiLeft + guiBase.xSize / 7) + 1;
        int outputY = (20) + (guiTop + guiBase.xSize / 5) + 10; // 1 * 20
        GuiHelper.drawItemStack(output, outputX, outputY);
        if (GuiHelper.isMouseBetween(mouseX, mouseY, outputX, outputY, 15, 15)) {
            guiBase.renderToolTip(output, outputX, outputY);
        }

        if (output.getItem() == Item.getItemFromBlock(Blocks.BARRIER)) {
            guiBase.drawCenteredString(fontRenderer, TextHelper.localize("text.furnace.error"), guiLeft + guiBase.xSize / 2, guiTop + 4 * guiBase.ySize / 6, 0xED073D);
            guiBase.drawCenteredString(fontRenderer, TextHelper.localize("bm.string.tier") + ": " + String.valueOf(tier), guiLeft + guiBase.xSize / 2, guiTop + 4 * guiBase.ySize / 6 + 15, 0);
//            guiBase.drawCenteredString(fontRenderer, "LP: " + String.valueOf(bloodRequired), guiLeft + guiBase.xSize / 2, guiTop + 4 * guiBase.ySize / 6 + 30, 0);
        }
        guiBase.drawCenteredString(fontRenderer, TextHelper.localize("guide.bloodmagic.page.minimumWill", String.valueOf(minimumWill)), guiLeft + guiBase.xSize / 2, guiTop + 4 * guiBase.ySize / 6 - 15, 0);
        guiBase.drawCenteredString(fontRenderer, TextHelper.localize("guide.bloodmagic.page.drainedWill", String.valueOf(drainedWill)), guiLeft + guiBase.xSize / 2, guiTop + 4 * guiBase.ySize / 6, 0);
    }

    protected int getRandomizedCycle(int index, int max) {
        rand.setSeed(index);
        return (index + rand.nextInt(max) + cycleIdx) % max;
    }
}
