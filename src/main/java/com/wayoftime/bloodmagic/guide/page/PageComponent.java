package com.wayoftime.bloodmagic.guide.page;

import com.wayoftime.bloodmagic.guide.Guide;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class PageComponent {

    private final int height;
    private IComponentSplitter splitter;
    private IViewingRequirement viewingRequirement;

    public PageComponent(int height) {
        this.height = height;
    }

    @SideOnly(Side.CLIENT)
    public void draw(Minecraft minecraft, ScaledResolution resolution, FontRenderer font) {

    }

    public int getHeight() {
        return height;
    }

    @Nullable
    public IComponentSplitter getSplitter() {
        return splitter;
    }

    public PageComponent withComponentSplitter(IComponentSplitter splitter) {
        this.splitter = splitter;
        return this;
    }

    public IViewingRequirement getViewingRequirement() {
        return viewingRequirement;
    }

    public PageComponent withViewingRequirement(IViewingRequirement viewingRequirement) {
        this.viewingRequirement = viewingRequirement;
        return this;
    }

    public interface IComponentSplitter {
        void split(Consumer<PageComponent> components, int currentPosition, int pageHeight);
    }

    public interface IViewingRequirement {
        boolean canView(EntityPlayer player, World world, ItemStack guideStack, Guide guide);
    }
}
