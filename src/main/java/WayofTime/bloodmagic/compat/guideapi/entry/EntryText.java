package WayofTime.bloodmagic.compat.guideapi.entry;

import amerifrance.guideapi.api.IPage;
import amerifrance.guideapi.api.impl.Book;
import amerifrance.guideapi.api.impl.abstraction.CategoryAbstract;
import amerifrance.guideapi.api.util.GuiHelper;
import amerifrance.guideapi.entry.EntryResourceLocation;
import amerifrance.guideapi.gui.GuiBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class EntryText extends EntryResourceLocation {

    public EntryText(List<IPage> pageList, String unlocEntryName, boolean unicode) {
        super(pageList, unlocEntryName, new ResourceLocation("bloodmagicguide", "textures/gui/bullet_point.png"), unicode);
    }

    public EntryText(List<IPage> pageList, String unlocEntryName) {
        this(pageList, unlocEntryName, false);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void drawExtras(Book book, CategoryAbstract category, int entryX, int entryY, int entryWidth, int entryHeight, int mouseX, int mouseY, GuiBase guiBase, FontRenderer fontRendererObj) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(image);
        GuiHelper.drawSizedIconWithoutColor(entryX + 4, entryY + 2, 8, 8, 1F);
    }
}
