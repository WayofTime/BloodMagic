package com.wayoftime.bloodmagic.guide.test.gui;

import com.google.common.collect.Maps;
import com.wayoftime.bloodmagic.guide.Guide;
import net.minecraft.client.gui.GuiScreen;

import java.util.Map;

public class GuiGuide extends GuiScreen {

    private static final Map<Guide, GuiGuide> SCREENS = Maps.newHashMap();

    private final Guide guide;
    private final Bookmark bookmark;

    public GuiGuide(Guide guide) {
        this.guide = guide;
        this.bookmark = new Bookmark();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void onGuiClosed() {
        // TODO update bookmark
        super.onGuiClosed();
    }

    public static GuiGuide getGui(Guide guide) {
        return SCREENS.compute(guide, (k, v) -> v == null ? new GuiGuide(guide) : v);
    }
}
