package wayoftime.bloodmagic.client.screens;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.apache.commons.lang3.tuple.Pair;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.container.item.ContainerFilter;
import wayoftime.bloodmagic.common.item.inventory.InventoryFilter;
import wayoftime.bloodmagic.common.item.routing.IItemFilterProvider;
import wayoftime.bloodmagic.common.item.routing.ItemRouterFilter;
import wayoftime.bloodmagic.common.item.routing.ItemTagFilter;
import wayoftime.bloodmagic.network.FilterGhostSlotPacket;
import wayoftime.bloodmagic.util.BMLog;
import wayoftime.bloodmagic.util.Constants;
import wayoftime.bloodmagic.util.GhostItemHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class ScreenFilter extends ScreenBase<ContainerFilter>
{
	private static final ResourceLocation background = BloodMagic.rl("textures/gui/routingfilter.png");
	public InventoryFilter filterInventory;

	private EditBox textBox;

	public ScreenFilter(ContainerFilter container, Inventory playerInventory, Component title)
	{
		super(container, playerInventory, title);
		filterInventory = container.inventoryFilter;
		imageWidth = 176;
		imageHeight = 187;
	}

    private void sendButtonClick(int buttonId) {
        Minecraft.getInstance().gameMode.handleInventoryButtonClick(container.containerId, buttonId);
    }

	@Override
	public void init()
    {
        super.init();
        this.inventoryLabelY = this.imageHeight - 94;

        this.textBox = new EditBox(Minecraft.getInstance().font, leftPos + 23, topPos + 19, 70, 12, Component.literal("itemGroup.search"));
        this.textBox.setBordered(false);
        this.textBox.setMaxLength(50);
        this.textBox.setVisible(true);
        this.textBox.setTextColor(16777215);
        this.textBox.setValue("");

        addRenderableWidget(
                Button.builder(Component.literal(""), button -> sendButtonClick(ItemRouterFilter.BUTTON_BWLIST))
                        .pos(leftPos + 7, topPos + 32)
                        .size(20, 20)
                        .build()
        );

        if (this.container.isTag) {
            addRenderableWidget(
                    Button.builder(Component.literal(""), button -> sendButtonClick(ItemRouterFilter.BUTTON_TAG))
                            .pos(leftPos + 27, topPos + 32)
                            .size(20, 20)
                            .build()
            );
        } else if (this.container.isEnchant) {
            addRenderableWidget(
                    Button.builder(Component.literal(""), button -> sendButtonClick(ItemRouterFilter.BUTTON_ENCHANT_KIND))
                            .pos(leftPos + 27, topPos + 32)
                            .size(20, 20)
                            .build()
            );
            addRenderableWidget(
                    Button.builder(Component.literal(""), button -> sendButtonClick(ItemRouterFilter.BUTTON_ENCHANT_LEVEL))
                            .pos(leftPos + 47, topPos + 32)
                            .size(20, 20)
                            .build()
            );
        }
    }

	@Override
	protected void containerTick()
	{
		super.containerTick();
		this.textBox.tick();
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers)
	{
		if (this.textBox.isFocused())
		{
			if ((keyCode == 259 || keyCode == 261) && container.getData(ItemRouterFilter.DATA_SLOT) != -1)
			{
				String str = this.textBox.getValue();

				if (str != null && str.length() > 0)
				{
					str = str.substring(0, str.length() - 1);
					this.textBox.setValue(str);
					int amount = 0;
					if (str.length() > 0)
					{
						try
						{
							Integer testVal = Integer.decode(str);
							if (testVal != null)
							{
								amount = testVal;
							}
						} catch (NumberFormatException d)
						{
						}
					}

					setValueOfGhostItemInSlot(container.getData(ItemRouterFilter.DATA_SLOT), amount);
				}
			}
		}

		return super.keyPressed(keyCode, scanCode, modifiers);
	}

    public void setValueOfGhostItemInSlot(int slot, int amount) {
        ItemStack stack = container.getSlot(slot).getItem();
        GhostItemHelper.setItemGhostAmount(stack, amount);
        BloodMagic.packetHandler.sendToServer(new FilterGhostSlotPacket(slot, stack));
    }

	@Override
	public boolean charTyped(char typedChar, int keyCode)
	{
		try
		{
			Integer charVal = Integer.decode("" + typedChar);
			if (charVal != null)
			{
				if (this.textBox.charTyped(typedChar, keyCode))
				{
					if (container.getData(ItemRouterFilter.DATA_SLOT) != -1)
					{
						String str = this.textBox.getValue();
						int amount = 0;

						if (!str.isEmpty())
						{
							try
							{
								Integer testVal = Integer.decode(str);
								if (testVal != null)
								{
									amount = testVal;
								}
							} catch (NumberFormatException d)
							{
							}
						}

						setValueOfGhostItemInSlot(container.getData(ItemRouterFilter.DATA_SLOT), amount);
					}
					return true;
				} else
				{
					return super.charTyped(typedChar, keyCode);
				}
			}

		} catch (NumberFormatException d)
		{
		}

		return super.charTyped(typedChar, keyCode);
	}

	/**
	 * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
	 */
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton)
	{
		boolean testBool = super.mouseClicked(mouseX, mouseY, mouseButton);

		if (container.getData(ItemRouterFilter.DATA_SLOT) != -1) { // Text box only selectable if a ghost slot has been clicked.
			if (this.textBox.mouseClicked(mouseX, mouseY, mouseButton)) { // Left-Clicked
				this.textBox.setFocused(true);
				return true;
			}
			if (this.textBox.isMouseOver(mouseX, mouseY) && mouseButton == 1) // Right-Clicked
			{
				this.textBox.setValue("");
				setValueOfGhostItemInSlot(container.getData(ItemRouterFilter.DATA_SLOT), 0);
				this.textBox.setFocused(true);
				return true;
			}
		}
		this.textBox.setFocused(false);

		if (container.getData(ItemRouterFilter.DATA_SLOT) != -1)
		{
			Slot slot = container.getSlot(container.getData(ItemRouterFilter.DATA_SLOT));
			ItemStack stack = slot.getItem();
			if (!stack.isEmpty())
			{
				int amount = GhostItemHelper.getItemGhostAmount(stack);
				if (amount == 0)
				{
					this.textBox.setValue("");
				} else
				{
					this.textBox.setValue("" + amount);
				}
			} else
			{
				this.textBox.setValue("");
			}
		}

		return true;
	}

	@Override
	public ResourceLocation getBackground()
	{
		return background;
	}

	@Override
	protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY)
    {
        super.renderLabels(guiGraphics, mouseX, mouseY);

        int bwState = container.getData(ItemRouterFilter.DATA_BWLIST);
        guiGraphics.blit(background, 7, 32, 176, bwState == 0 ? 0 : 20, 20, 20);

        int offset = container.getData(ItemRouterFilter.DATA_SLOT);
        if (container.isTag) {
            int tagState = container.getData(ItemRouterFilter.DATA_TAG + offset);
            guiGraphics.blit(background, 27, 32, 196, tagState == 0 ? 20 : 0, 20, 20);
        }

        if (container.isEnchant) {
            int enchantState = container.getData(ItemRouterFilter.DATA_ENCHANT + offset);
            int enchantButton = enchantState == 0 ? 0 : enchantState == 1 ? 20 : 40; // could inline but its quite long
            guiGraphics.blit(background, 27, 32, 216, enchantButton, 20, 20);

            int levelState = container.getData(ItemRouterFilter.DATA_ENCHANT_LVL + offset);
            guiGraphics.blit(background, 47, 32, 236, levelState == 0 ? 0 : 20, 20, 20);
        }
    }

	@Override
	protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY)
	{
		guiGraphics.blit(background, leftPos, topPos, 0, 0, imageWidth, imageHeight);
        int ghostSlot = container.getData(ItemRouterFilter.DATA_SLOT);
		if (ghostSlot >= 0)
		{
			guiGraphics.blit(background, 106 + leftPos + 21 * (ghostSlot % 3), topPos + 11 + 21 * (ghostSlot / 3), 0, 187, 24, 24);
		}
	}

    private static Component translate(String name) {
        return Component.translatable("filter.bloodmagic." + name);
    }

    private List<Component> getTagText() {
        int index = container.getData(ItemRouterFilter.DATA_SLOT);
        int buttonState = container.getData(ItemRouterFilter.DATA_TAG + index);
        if (index == -1) {
            return List.of(translate("novalidtag"));
        }
        ItemStack ghostStack = container.getSlot(index).getItem();
        if (ghostStack.isEmpty()) {
            return List.of(translate("novalidtag"));
        }

        List<Component> componentList = new ArrayList<>();
        if (buttonState == 0) {
            Stream<TagKey<Item>> stream = ghostStack.getTags();

            List<ResourceLocation> locations = new ArrayList<>();
            stream.forEach(a -> {
                locations.add(a.location());
            });

            if (!locations.isEmpty()) {
                componentList.add(Component.translatable("filter.bloodmagic.anytag"));
                for (ResourceLocation rl : locations) {
                    componentList.add(Component.literal(rl.toString()));
                }
            } else {
                componentList.add(Component.translatable("filter.bloodmagic.novalidtag"));
            }
        } else { // could do the same thing as for enchant BUT idk if tag order is consistent in like any way, so we let the server put it on and just read it out here
            CompoundTag ghostTag = ghostStack.getOrCreateTag();
            String tagname = ghostTag.getString(Constants.NBT.TAG);
            ResourceLocation rl = new ResourceLocation(tagname);
            componentList.add(Component.translatable("filter.bloodmagic.specifiedtag"));
            componentList.add(Component.literal(rl.toString()));
        }

        return componentList;
    }

    private List<Component> getEnchantText() {
        int index = container.getData(ItemRouterFilter.DATA_SLOT);
        int state = container.getData(ItemRouterFilter.DATA_ENCHANT + index);
        if (index == -1) {
            return List.of(translate("noenchant"));
        }
        ItemStack ghostStack = container.getSlot(index).getItem();
        if (ghostStack.isEmpty()) {
            return List.of(translate("noenchant"));
        }

        List<Component> componentList = new ArrayList<>();
        Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(ghostStack);
        if (enchants.isEmpty()) {
            return List.of(translate("noenchant"));
        }

        if (state == 0 || state == 1) {
            if (state == 0) {
                componentList.add(Component.translatable("filter.bloodmagic.anyenchant"));
            } else {
                componentList.add(Component.translatable("filter.bloodmagic.allenchant"));
            }
            for (Map.Entry<Enchantment, Integer> entry : enchants.entrySet()) {
                componentList.add(entry.getKey().getFullname(entry.getValue()));
            }
        } else { // this is... not ideal. I think. Enchantments do have fixed lists, so I guess still fine? golang this wouldnt work
            List<Map.Entry<Enchantment, Integer>> enchantList = new ArrayList<>(enchants.entrySet());
            Map.Entry<Enchantment, Integer> selected = enchantList.get(state - 2);
            componentList.add(selected.getKey().getFullname(selected.getValue()));
        }

        return componentList;
    }

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks)
    {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.textBox.render(guiGraphics, mouseX, mouseY, partialTicks);

        List<Component> tooltip = new ArrayList<>();
        if (mouseY >= topPos + 32 && mouseY < topPos + 52) { // height range of buttons
            if (mouseX >= leftPos + 7 && mouseX < leftPos + 27) { // black/white list button
                tooltip.add(translate(container.getData(ItemRouterFilter.DATA_BWLIST) == 0 ? "whitelist" : "blacklist"));
            }
            if (mouseX >= leftPos + 27 && mouseX < leftPos + 47) {
                if (container.isTag) {
                    tooltip.addAll(getTagText());
                }
                if (container.isEnchant) {
                    tooltip.addAll(getEnchantText());
                }
            }
            if (container.isEnchant && mouseX >= leftPos + 47 && mouseX < leftPos + 67) {
                int state = container.getData(ItemRouterFilter.DATA_ENCHANT_LVL + container.getData(ItemRouterFilter.DATA_SLOT)); // its per slot. maybe dont render only for slot 0
                tooltip.add(translate(state == 0 ? "enchantnotfuzzy" : "enchantfuzzy"));
            }
        }

        if (!tooltip.isEmpty()) {
            // this should be in renderLabels for sure, but who's to judge. pretty sure it works anyways
            guiGraphics.renderTooltip(this.font, tooltip, Optional.empty(), mouseX, mouseY);
        }
    }
}
