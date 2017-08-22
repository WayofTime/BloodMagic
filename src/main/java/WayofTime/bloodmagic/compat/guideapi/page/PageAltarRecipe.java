package WayofTime.bloodmagic.compat.guideapi.page;

import WayofTime.bloodmagic.api_impl.recipe.RecipeBloodAltar;
import WayofTime.bloodmagic.util.helper.TextHelper;
import amerifrance.guideapi.api.impl.Book;
import amerifrance.guideapi.api.impl.Page;
import amerifrance.guideapi.api.impl.abstraction.CategoryAbstract;
import amerifrance.guideapi.api.impl.abstraction.EntryAbstract;
import amerifrance.guideapi.api.util.GuiHelper;
import amerifrance.guideapi.gui.GuiBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class PageAltarRecipe extends Page {

    public final ItemStack[] input;
    public final ItemStack output;
    public final int tier;
    public final int bloodRequired;

    private long lastCycle = -1;
    private int cycleIdx = 0;
    private Random random = new Random();

    public PageAltarRecipe(RecipeBloodAltar recipe) {
        this.input = recipe.getInput().getMatchingStacks();
        this.output = recipe.getOutput();
        this.tier = recipe.getMinimumTier().toInt();
        this.bloodRequired = recipe.getSyphon();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void draw(Book book, CategoryAbstract category, EntryAbstract entry, int guiLeft, int guiTop, int mouseX, int mouseY, GuiBase guiBase, FontRenderer fontRenderer) {
        Minecraft mc = Minecraft.getMinecraft();
        mc.getTextureManager().bindTexture(new ResourceLocation("bloodmagicguide", "textures/gui/altar.png"));

        long time = mc.world.getTotalWorldTime();
        if (lastCycle < 0 || lastCycle < time - 20) {
            if (lastCycle > 0) {
                cycleIdx++;
                cycleIdx = Math.max(0, cycleIdx);
            }
            lastCycle = mc.world.getTotalWorldTime();
        }

        guiBase.drawTexturedModalRect(guiLeft + 42, guiTop + 53, 0, 0, 146, 104);

        guiBase.drawCenteredString(fontRenderer, TextHelper.localize("guide.bloodmagic.page.bloodAltar"), guiLeft + guiBase.xSize / 2, guiTop + 12, 0);

        int inputX = (1 + 1) * 20 + (guiLeft + guiBase.xSize / 7) + 1;
        int inputY = (20) + (guiTop + guiBase.ySize / 5) - 1; //1 * 20
        ItemStack inputStack = input[getRandomizedCycle(0, input.length)];
        GuiHelper.drawItemStack(inputStack, inputX, inputY);
        if (GuiHelper.isMouseBetween(mouseX, mouseY, inputX, inputY, 15, 15))
            guiBase.renderToolTip(inputStack, mouseX, mouseY);

        ItemStack outputStack = output;
        if (output.isEmpty())
            outputStack = new ItemStack(Blocks.BARRIER);

        int outputX = (5 * 20) + (guiLeft + guiBase.xSize / 7) + 1;
        int outputY = (20) + (guiTop + guiBase.xSize / 5) - 1; // 1 * 20
        GuiHelper.drawItemStack(outputStack, outputX, outputY);
        if (GuiHelper.isMouseBetween(mouseX, mouseY, outputX, outputY, 15, 15))
            guiBase.renderToolTip(outputStack, outputX, outputY);

        if (outputStack.getItem() == Item.getItemFromBlock(Blocks.BARRIER)) {
            guiBase.drawCenteredString(fontRenderer, TextHelper.localize("text.furnace.error"), guiLeft + guiBase.xSize / 2, guiTop + 4 * guiBase.ySize / 6, 0xED073D);
            guiBase.drawCenteredString(fontRenderer, TextHelper.localize("bm.string.tier") + ": " + String.valueOf(tier), guiLeft + guiBase.xSize / 2, guiTop + 4 * guiBase.ySize / 6 + 15, 0);
            guiBase.drawCenteredString(fontRenderer, "LP: " + String.valueOf(bloodRequired), guiLeft + guiBase.xSize / 2, guiTop + 4 * guiBase.ySize / 6 + 30, 0);
        }

        guiBase.drawCenteredString(fontRenderer, TextHelper.localize("guide.bloodmagic.page.tier", String.valueOf(tier)), guiLeft + guiBase.xSize / 2, guiTop + 4 * guiBase.ySize / 6, 0);
        guiBase.drawCenteredString(fontRenderer, TextHelper.localize("guide.bloodmagic.page.lp", String.valueOf(bloodRequired)), guiLeft + guiBase.xSize / 2, guiTop + 4 * guiBase.ySize / 6 + 15, 0);
    }

    protected ItemStack getNextItem(ItemStack stack, int position) {
        NonNullList<ItemStack> subItems = NonNullList.create();
        Item item = stack.getItem();

        item.getSubItems(CreativeTabs.SEARCH, subItems);
        return subItems.get(getRandomizedCycle(position, subItems.size()));
    }

    protected int getRandomizedCycle(int index, int max) {
        random.setSeed(index);
        return (index + random.nextInt(max) + cycleIdx) % max;
    }
}
