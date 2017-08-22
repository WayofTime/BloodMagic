package WayofTime.bloodmagic.compat.guideapi.page;

import WayofTime.bloodmagic.api.registry.OrbRegistry;
import WayofTime.bloodmagic.api_impl.recipe.RecipeTartaricForge;
import WayofTime.bloodmagic.util.helper.TextHelper;
import amerifrance.guideapi.api.impl.Book;
import amerifrance.guideapi.api.impl.Page;
import amerifrance.guideapi.api.impl.abstraction.CategoryAbstract;
import amerifrance.guideapi.api.impl.abstraction.EntryAbstract;
import amerifrance.guideapi.api.util.GuiHelper;
import amerifrance.guideapi.gui.GuiBase;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;
import java.util.Random;

public class PageTartaricForgeRecipe extends Page {
    public final List<ItemStack[]> input;
    public final ItemStack output;
    public final int tier;
    public final double minimumWill;
    public final double drainedWill;

    private long lastCycle = -1;
    private int cycleIdx = 0;
    private Random random = new Random();

    public PageTartaricForgeRecipe(RecipeTartaricForge recipe) {
        input = Lists.newArrayList();
        for (Ingredient ingredient : recipe.getInput())
            input.add(ingredient.getMatchingStacks());

        this.output = recipe.getOutput();
        this.tier = 0;
        this.minimumWill = recipe.getMinimumSouls();
        this.drainedWill = recipe.getSoulDrain();
    }

    @SuppressWarnings("unchecked")
    @Override
    @SideOnly(Side.CLIENT)
    public void draw(Book book, CategoryAbstract category, EntryAbstract entry, int guiLeft, int guiTop, int mouseX, int mouseY, GuiBase guiBase, FontRenderer fontRenderer) {
        Minecraft mc = Minecraft.getMinecraft();
        mc.getTextureManager().bindTexture(new ResourceLocation("bloodmagicguide", "textures/gui/soulForge.png"));

        long time = mc.world.getTotalWorldTime();
        if (lastCycle < 0 || lastCycle < time - 20) {
            if (lastCycle > 0) {
                cycleIdx++;
                cycleIdx = Math.max(0, cycleIdx);
            }
            lastCycle = mc.world.getTotalWorldTime();
        }

        guiBase.drawTexturedModalRect(guiLeft + 42, guiTop + 53, 0, 0, 146, 104);
        guiBase.drawCenteredString(fontRenderer, TextHelper.localize("guide.bloodmagic.page.soulForge"), guiLeft + guiBase.xSize / 2, guiTop + 12, 0);

        for (int y = 0; y < 2; y++) {
            for (int x = 0; x < 2; x++) {
                int stackX = (x + 1) * 20 + (guiLeft + guiBase.xSize / 7) + 1;
                int stackY = (y + 1) * 20 + (guiTop + guiBase.ySize / 5) - 1;
                ItemStack[] component = input.size() > y * 2 + x ? input.get(y * 2 + x) : null;//recipe.getInput()[y * 2 + x];
                if (component != null) {
                    ItemStack drawStack = component[getRandomizedCycle(x + (y * 2), component.length)];
                    GuiHelper.drawItemStack(drawStack, stackX, stackY);
                    if (GuiHelper.isMouseBetween(mouseX, mouseY, stackX, stackY, 15, 15))
                        guiBase.renderToolTip(drawStack, mouseX, mouseY);
                }
            }
        }

        ItemStack outputStack = output;
        if (outputStack.isEmpty())
            outputStack = new ItemStack(Blocks.BARRIER);

        int outputX = (5 * 20) + (guiLeft + guiBase.xSize / 7) + 1;
        int outputY = (20) + (guiTop + guiBase.xSize / 5) + 10; // 1 * 20
        GuiHelper.drawItemStack(outputStack, outputX, outputY);
        if (GuiHelper.isMouseBetween(mouseX, mouseY, outputX, outputY, 15, 15)) {
            guiBase.renderToolTip(outputStack, outputX, outputY);
        }

        if (outputStack.getItem() == Item.getItemFromBlock(Blocks.BARRIER)) {
            guiBase.drawCenteredString(fontRenderer, TextHelper.localize("text.furnace.error"), guiLeft + guiBase.xSize / 2, guiTop + 4 * guiBase.ySize / 6, 0xED073D);
            guiBase.drawCenteredString(fontRenderer, TextHelper.localize("bm.string.tier") + ": " + String.valueOf(tier), guiLeft + guiBase.xSize / 2, guiTop + 4 * guiBase.ySize / 6 + 15, 0);
//            guiBase.drawCenteredString(fontRenderer, "LP: " + String.valueOf(bloodRequired), guiLeft + guiBase.xSize / 2, guiTop + 4 * guiBase.ySize / 6 + 30, 0);
        }
        guiBase.drawCenteredString(fontRenderer, TextHelper.localize("guide.bloodmagic.page.minimumWill", String.valueOf(minimumWill)), guiLeft + guiBase.xSize / 2, guiTop + 4 * guiBase.ySize / 6 - 15, 0);
        guiBase.drawCenteredString(fontRenderer, TextHelper.localize("guide.bloodmagic.page.drainedWill", String.valueOf(drainedWill)), guiLeft + guiBase.xSize / 2, guiTop + 4 * guiBase.ySize / 6, 0);
    }

    protected ItemStack getNextItem(ItemStack stack, int position) {
        NonNullList<ItemStack> subItems = NonNullList.create();
        Item item = stack.getItem();

        item.getSubItems(item.getCreativeTab(), subItems);
        return subItems.get(getRandomizedCycle(position, subItems.size()));
    }

    protected int getRandomizedCycle(int index, int max) {
        random.setSeed(index);
        return (index + random.nextInt(max) + cycleIdx) % max;
    }
}
