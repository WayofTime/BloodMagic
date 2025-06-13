package wayoftime.bloodmagic.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.client.widgets.MultiIconButton;
import wayoftime.bloodmagic.common.menu.TrainerMenu;

public class TrainerScreen extends AbstractGhostScreen<TrainerMenu> {

    public TrainerScreen(TrainerMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 176;
        this.imageHeight = 187;
    }

    private static final Component allowTooltip = Component.translatable("trainer.bloodmagic.allow_others");
    private static final Component denyTooltip = Component.translatable("trainer.bloodmagic.deny_others");
    @Override
    protected void init() {
        super.init();

        addRenderableWidget(Button.builder(Component.literal("<"), button -> sendButtonClick(1))
                .pos(leftPos + 16, topPos + 34)
                .size(8, 20)
                .build()
        );
        addRenderableWidget(Button.builder(Component.literal(">"), button -> sendButtonClick(2))
                .pos(leftPos + 44, topPos + 34)
                .size(8, 20)
                .build()
        );

        addMultiIconButton(1, MultiIconButton.builder(button -> sendButtonClick(3))
                .icons(allow, deny)
                .tooltips(allowTooltip, denyTooltip)
                .pos(leftPos + 24, topPos + 55)
                .size(20, 20)
                .build()
        );

        addRenderableWidget(Button.builder(Component.translatable("trainer.bloodmagic.save"), button -> sendButtonClick(4))
                .pos(leftPos + 50, topPos + 55)
                .size(30, 20)
                .build()
        );
    }

    private void sendButtonClick(int buttonId) {
        this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, buttonId);
    }

    public String getLevelString() {
        return "" + getMenu().getData(3 + this.menu.getLastGhostSlotClicked());
    }

    private static final ResourceLocation allow = ResourceLocation.fromNamespaceAndPath(BloodMagic.MODID, "container/trainer/allow_others");
    private static final ResourceLocation deny = ResourceLocation.fromNamespaceAndPath(BloodMagic.MODID, "container/trainer/deny_others");
    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        super.renderLabels(guiGraphics, mouseX, mouseY);
        String level = getLevelString();
        if (!level.isEmpty()) {
            int xOff = -3 * level.length();
            guiGraphics.drawString(this.font, Component.literal(level), 34 + xOff, 40, 0xFFFFFF /*4210752*/, false);
        }
    }

    @Override
    public ResourceLocation background() {
        return BloodMagic.rl("textures/gui/container/training_bracelet.png");
    }
}
