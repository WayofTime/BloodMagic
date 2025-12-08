package wayoftime.bloodmagic.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.client.widgets.MultiIconButton;
import wayoftime.bloodmagic.common.datacomponent.BMDataComponents;
import wayoftime.bloodmagic.common.menu.FilterMenu;
import wayoftime.bloodmagic.common.network.GhostAmountPacket;
import wayoftime.bloodmagic.util.helper.FilterHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static wayoftime.bloodmagic.util.helper.FilterHelper.*;

public class FilterScreen extends AbstractGhostScreen<FilterMenu> {
    public static final ResourceLocation background = BloodMagic.rl("textures/gui/container/filter_item.png");

    // sprites dont need .png I guess?
    public static final ResourceLocation whitelist = BloodMagic.rl("container/filter/whitelist");
    public static final ResourceLocation blacklist = BloodMagic.rl("container/filter/blacklist");
    public static final ResourceLocation tag = BloodMagic.rl("container/filter/tag");
    public static final ResourceLocation tag_all = BloodMagic.rl("container/filter/tag_all");
    public static final ResourceLocation enchant = BloodMagic.rl("container/filter/enchant");
    public static final ResourceLocation enchant_any = BloodMagic.rl("container/filter/enchant_any");
    public static final ResourceLocation enchant_every = BloodMagic.rl("container/filter/enchant_every");
    public static final ResourceLocation enchant_level_any = BloodMagic.rl("container/filter/enchant_level_any");
    public static final ResourceLocation enchant_level_exact = BloodMagic.rl("container/filter/enchant_level_exact");

    public FilterScreen(FilterMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        imageWidth = 176;
        imageHeight = 187;
    }

    private EditBox amountBox;
    @Override
    protected void init() {
        super.init();
        amountBox = new EditBox(font, Button.DEFAULT_WIDTH, Button.DEFAULT_HEIGHT, Component.literal("message?"));
        amountBox.setEditable(true);
        amountBox.setFilter(newValueString -> {
            if (newValueString.equalsIgnoreCase("")) {
                return true;
            }
            try {
                Integer.decode(newValueString);
                return true;
            } catch (final NumberFormatException e) {
                return false;
            }
        });
        amountBox.setResponder(newValueString -> {
            BloodMagic.LOGGER.info("responding to '{}'", newValueString);
            try {
                int amount = Integer.decode(newValueString);
                setGhostAmount(amount);
            } catch (NumberFormatException e) {
                // ignored
            }
        });
        amountBox.setTooltip(Tooltip.create(Component.translatable("filter.bloodmagic.amount_box")));
        amountBox.setPosition(leftPos + 20, topPos + 16);
        amountBox.setSize(77, 14);

        addRenderableWidget(amountBox);

        if (menu.heldSlot == -1) {
            addRenderableWidget(Button.builder(FilterHelper.translate("return"), button -> sendButtonClick(BUTTON_RETURN))
                    .pos(leftPos + 8, topPos + 55)
                    .size(50, 20)
                    .build());
        }

        addMultiIconButton(DATA_BWLIST, MultiIconButton.builder(button -> sendButtonClick(BUTTON_BWLIST))
                .icons(whitelist, blacklist)
                .tooltips(FilterHelper.translate("whitelist"), FilterHelper.translate("blacklist"))
                .pos(leftPos + 8, topPos + 32)
                .size(20, 20)
                .build()
        );

        if (menu.isTag) {
            addPerSlotButton(DATA_TAG, MultiIconButton.builder(button -> sendButtonClick(BUTTON_TAG))
                    .icons(tag_all, tag)
                    .stateGetter(state -> state == 0 ? 0 : 1)
                    .pos(leftPos + 28, topPos + 32)
                    .size(20, 20)
                    .build()
            );
        }

        if (menu.isEnchant) {
            addPerSlotButton(DATA_ENCHANT, MultiIconButton.builder(button -> sendButtonClick(BUTTON_ENCHANT))
                    .icons(enchant_every, enchant_any, enchant)
                    .stateGetter(state -> switch (state) {
                        case 0 -> 0;
                        case 1 -> 1;

                        default -> 2;
                    })
                    .pos(leftPos + 28, topPos + 32)
                    .size(20, 20)
                    .build()
            );

            addPerSlotButton(DATA_ENCHANT_LVL, MultiIconButton.builder(button -> sendButtonClick(BUTTON_ENCHANT_LVL))
                    .icons(enchant_level_exact, enchant_level_any)
                    .pos(leftPos + 48, topPos + 32)
                    .size(20, 20)
                    .build()
            );
        }
    }

    private Map<Integer, MultiIconButton> perSlotButton = new HashMap<>();
    @Override
    protected void containerTick() {
        super.containerTick();
        perSlotButton.forEach((index, button) -> {
            int slot = menu.getData(DATA_SLOT);
            int state = menu.getData(index + slot);
            button.setState(state);
        });
    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int x, int y) {
        super.renderTooltip(guiGraphics, x, y);
        perSlotButton.forEach((key, button) -> {
            if (button.isHovered()) {
                guiGraphics.renderTooltip(this.font, buttonHoverText(key), Optional.empty(), x, y);
            }
        });
    }

    private List<Component> buttonHoverText(int data) {
        int slot = menu.getData(DATA_SLOT);
        return switch (data) {
            case DATA_TAG -> FilterHelper.tagButtonText(menu.handler.getStackInSlot(slot));
            case DATA_ENCHANT -> FilterHelper.enchantButtonText(menu.handler.getStackInSlot(slot));
            case DATA_ENCHANT_LVL -> FilterHelper.enchantLevelButtonText(menu.handler.getStackInSlot(slot));

            default -> List.of(Component.literal("Huh. dont know button %d".formatted(data)));
        };
    }

    private void addPerSlotButton(int dataOffset, MultiIconButton button) {
        perSlotButton.put(dataOffset, button);
        addRenderableWidget(button);
    }

    private void setGhostAmount(int amount) {
        int slot = menu.getData(DATA_SLOT);
        ItemStack stack = menu.getSlot(slot).getItem();
        int oldAmount = stack.getOrDefault(BMDataComponents.GHOST_AMOUNT, 0);
        if (oldAmount == amount) {
            return;
        }

        GhostAmountPacket packet = new GhostAmountPacket(slot, amount);
        PacketDistributor.sendToServer(packet);
    }

    private void sendButtonClick(int buttonId) {
        this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, buttonId);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button); // otherwise screen dies

        if (menu.getData(DATA_SLOT) != -1 && amountBox.isMouseOver(mouseX, mouseY)) { // Text box only selectable if a ghost slot has been clicked.
            if (button == 1) {
                amountBox.setValue("");
                setGhostAmount(0);
                amountBox.setFocused(true);
            }
            if (button == 0) {
                amountBox.setFocused(true);
            }
            return true;
        }

        amountBox.setFocused(false);
        return true;
    }

    @Override
    public ResourceLocation background() {
        return background;
    }
}
