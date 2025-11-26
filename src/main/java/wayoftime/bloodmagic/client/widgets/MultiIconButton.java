package wayoftime.bloodmagic.client.widgets;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import wayoftime.bloodmagic.BloodMagic;

import java.util.function.Function;
import java.util.function.Supplier;

public class MultiIconButton extends AbstractButton {

    private final ResourceLocation[] icons;
    private final Component[] tooltips;
    private final OnPress onPress;
    private final Function<Integer, Integer> stateGetter;
    public MultiIconButton(int x, int y, int width, int height, Component[] tooltips, ResourceLocation[] icons, OnPress onPress, Function<Integer, Integer> stateGetter) {
        super(x, y, width, height, Component.literal(""));
        this.onPress = onPress;
        this.icons = icons;
        this.tooltips = tooltips;
        this.stateGetter = stateGetter;
    }

    public MultiIconButton(Builder builder) {
        this(builder.x, builder.y, builder.width, builder.height, builder.tooltips, builder.icons, builder.onPress, builder.stateGetter);
    }

    private int state = 0;
    public int getState() {
        return this.stateGetter.apply(state);
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public void onPress() {
        onPress.onPress(this);
    }

    public Component getHoverText() {
        return tooltips.length > 0 ? tooltips[stateGetter.apply(state) % tooltips.length] : CommonComponents.EMPTY;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);
        int index = stateGetter.apply(state);
        ResourceLocation sprite = icons[index % icons.length];
        // BloodMagic.LOGGER.info("got state {}, index {} and res loc {}", state, index, sprite);
        guiGraphics.blitSprite(sprite, this.getX(), this.getY(), this.getWidth(), this.getHeight());
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        this.defaultButtonNarrationText(narrationElementOutput);
    }

    public static Builder builder(OnPress onPress) {
        return new Builder(onPress);
    }

    public static class Builder {
        private final OnPress onPress;
        private int x;
        private int y;
        private int width = 150;
        private int height = 20;
        private ResourceLocation[] icons;
        private Component[] tooltips;
        private Function<Integer, Integer> stateGetter = Function.identity();

        public Builder(OnPress onPress) {
            this.onPress = onPress;
        }

        public Builder pos(int x, int y) {
            this.x = x;
            this.y = y;
            return this;
        }

        public Builder width(int width) {
            this.width = width;
            return this;
        }

        public Builder size(int width, int height) {
            this.width = width;
            this.height = height;
            return this;
        }

        public Builder bounds(int x, int y, int width, int height) {
            return this.pos(x, y).size(width, height);
        }

        public Builder tooltips(Component... tooltips) {
            this.tooltips = tooltips;
            return this;
        }

        public Builder icons(ResourceLocation... icons) {
            this.icons = icons;
            return this;
        }

        public Builder stateGetter(Function<Integer, Integer> stateGetter) {
            this.stateGetter = stateGetter;
            return this;
        }

        public MultiIconButton build() {
            return build(MultiIconButton::new);
        }

        public MultiIconButton build(java.util.function.Function<Builder, MultiIconButton> builder) {
            return builder.apply(this);
        }
    }

    @FunctionalInterface
    public interface OnPress {
        void onPress(MultiIconButton button);
    }
}
