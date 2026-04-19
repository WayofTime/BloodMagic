package wayoftime.bloodmagic.client.screen;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.menu.AlchemyTableMenu;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static wayoftime.bloodmagic.common.blockentity.AlchemyTableTile.*;

public class AlchemyTableScreen extends AbstractContainerScreen<AlchemyTableMenu> {

    public static final ResourceLocation background_rl = BloodMagic.rl("textures/gui/container/alchemy_table.png");
    public static final ResourceLocation progress_rl = BloodMagic.rl("container/alchemy_table/progress");
    public static final ResourceLocation err_essence_rl = BloodMagic.rl("container/alchemy_table/err_essence");
    public static final ResourceLocation err_orb_rl = BloodMagic.rl("container/alchemy_table/err_orb");

    public static final List<Component> essenceError = new ArrayList<>();
    public static final List<Component> orbError = new ArrayList<>();
    static {
        essenceError.add(Component.translatable("tooltip.bloodmagic.alchemy_table.essence_error.title").withStyle(ChatFormatting.RED));
        essenceError.add(Component.translatable("tooltip.bloodmagic.alchemy_table.essence_error.text").withStyle(ChatFormatting.GRAY));
        orbError.add(Component.translatable("tooltip.bloodmagic.alchemy_table.orb_error.title").withStyle(ChatFormatting.RED));
        orbError.add(Component.translatable("tooltip.bloodmagic.alchemy_table.orb_error.text").withStyle(ChatFormatting.GRAY));
    }

    public AlchemyTableScreen(AlchemyTableMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        imageWidth = 176;
        imageHeight = 205;
    }

    private Component all = Component.literal("64");
    private Component one = Component.literal("1");
    private Button stackLimitToggle;

    @Override
    protected void init() {
        super.init();
        this.inventoryLabelY = this.imageHeight - 94; // auto recalc
        stackLimitToggle = Button.builder(all, button -> minecraft.gameMode.handleInventoryButtonClick(menu.containerId, 0))
                .size(20, 20)
                .pos(leftPos + 141, topPos + 49)
                .build();
        addRenderableWidget(stackLimitToggle);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int x, int y) {
        super.renderTooltip(guiGraphics, x, y);
        if (stackLimitToggle.isHovered()) {
            guiGraphics.renderTooltip(font, Component.translatable("tooltip.bloodmagic.alchemy_table.stack_limit_toggle"), x, y);
        }
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        stackLimitToggle.setMessage(menu.getData(STACK_LIMIT) == 0 ? all : one);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blit(background_rl, leftPos, topPos, 0, 0, imageWidth, imageHeight);
        int progress = menu.getData(PROGRESS);
        guiGraphics.blitSprite(progress_rl, leftPos + 106, topPos + 14 + 90 - progress, 18, progress);
        int errorFlag = menu.getData(ERROR_FLAG);
        if (errorFlag == ERR_ORB) {
            guiGraphics.blitSprite(err_orb_rl, leftPos + 106, topPos + 24, 18, 18);
            if (mouseX > leftPos + 106 && mouseX < leftPos + 106 + 18 && mouseY > topPos + 24 && mouseY < topPos + 24 + 18) {
                guiGraphics.renderTooltip(font, orbError, Optional.empty(), mouseX, mouseY);
            }
        }
        if (errorFlag == ERR_ESSENCE) {
            guiGraphics.blitSprite(err_essence_rl, leftPos + 106, topPos + 24, 18, 18);
            if (mouseX > leftPos + 106 && mouseX < leftPos + 106 + 18 && mouseY > topPos + 24 && mouseY < topPos + 24 + 18) {
                guiGraphics.renderTooltip(font, orbError, Optional.empty(), mouseX, mouseY);
            }
        }
    }
}
