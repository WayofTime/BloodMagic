package wayoftime.bloodmagic.client.screens;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.tuple.Pair;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.container.item.ContainerFilter;
import wayoftime.bloodmagic.common.item.routing.IRoutingFilterProvider;
import wayoftime.bloodmagic.network.RouterFilterPacket;
import wayoftime.bloodmagic.util.GhostItemHelper;

public class ScreenFilter extends ScreenBase<ContainerFilter>
{
	private static final ResourceLocation background = BloodMagic.rl("textures/gui/routingfilter.png");
	public Container filterInventory;
	private Player player;
	private int left, top;

	private EditBox textBox;

	private int numberOfAddedButtons = 0;
	private List<String> buttonKeyList = new ArrayList<String>();

	private List<Button> buttonList = new ArrayList<>();

	public ScreenFilter(ContainerFilter container, Inventory playerInventory, Component title)
	{
		super(container, playerInventory, title);
		filterInventory = container.inventoryFilter;
		imageWidth = 176;
		imageHeight = 187;
		this.player = playerInventory.player;
	}

	@Override
	public void init()
	{
		super.init();
		left = (this.width - this.imageWidth) / 2;
		top = (this.height - this.imageHeight) / 2;

		this.textBox = new EditBox(Minecraft.getInstance().font, left + 23, top + 19, 70, 12, new TextComponent("itemGroup.search"));
		this.textBox.setBordered(false);
//		this.textBox.setText("");
		this.textBox.setMaxLength(50);
		this.textBox.setVisible(true);
		this.textBox.setTextColor(16777215);
		this.textBox.setValue("");

		numberOfAddedButtons = 0;
		buttonKeyList.clear();

		ItemStack filterStack = this.container.filterStack;

		if (filterStack.getItem() instanceof IRoutingFilterProvider)
		{
			IRoutingFilterProvider provider = (IRoutingFilterProvider) filterStack.getItem();
			List<Pair<String, Button.OnPress>> buttonActionList = provider.getButtonAction(this.container);

			for (Pair<String, Button.OnPress> pair : buttonActionList)
			{
				if (buttonKeyList.contains(pair.getKey()))
				{
					continue;
				}
				buttonKeyList.add(pair.getKey());
				Pair<Integer, Integer> buttonLocation = getButtonLocation(numberOfAddedButtons);
				Button addedButton = new Button(left + buttonLocation.getLeft(), top + buttonLocation.getRight(), 20, 20, new TextComponent(""), pair.getRight());

				if (!provider.isButtonGlobal(filterStack, pair.getKey()))
				{
					addedButton.active = false;
				}

				this.addRenderableWidget(addedButton);
				buttonList.add(addedButton);
				numberOfAddedButtons++;
			}
		}
	}

	public Pair<Integer, Integer> getButtonLocation(int addedButton)
	{
		int x = 7;
		int y = 32;

		x = x + addedButton * 20;

		return Pair.of(x, y);
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
			if ((keyCode == 259 || keyCode == 261) && container.lastGhostSlotClicked != -1)
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

					setValueOfGhostItemInSlot(container.lastGhostSlotClicked, amount);
				}
			}
		}

		return super.keyPressed(keyCode, scanCode, modifiers);
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
					if (container.lastGhostSlotClicked != -1)
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

						setValueOfGhostItemInSlot(container.lastGhostSlotClicked, amount);
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

	private void setValueOfGhostItemInSlot(int ghostItemSlot, int amount)
	{
		Slot slot = container.getSlot(ghostItemSlot);
		ItemStack ghostStack = slot.getItem();
//		ItemStack ghostStack = container.inventoryFilter.getStackInSlot(ghostItemSlot);
		if (!ghostStack.isEmpty())
		{
			GhostItemHelper.setItemGhostAmount(ghostStack, amount);
			GhostItemHelper.setItemGhostAmount(container.inventoryFilter.getItem(ghostItemSlot), amount);
			if (container.filterStack.getItem() instanceof IRoutingFilterProvider)
			{
				((IRoutingFilterProvider) container.filterStack.getItem()).setGhostItemAmount(container.filterStack, ghostItemSlot, amount);

			}
		}

		BloodMagic.packetHandler.sendToServer(new RouterFilterPacket(player.getInventory().selected, ghostItemSlot, amount));
	}

	/**
	 * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
	 */
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton)
	{
		boolean testBool = super.mouseClicked(mouseX, mouseY, mouseButton);

		if (this.textBox.mouseClicked(mouseX, mouseY, mouseButton))
		{
			return true;
		}

		if (container.lastGhostSlotClicked != -1)
		{
			enableAllButtons();
			Slot slot = container.getSlot(container.lastGhostSlotClicked);
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

	private void enableAllButtons()
	{
		for (AbstractWidget button : buttonList)
		{
			button.active = true;
		}
	}

	@Override
	public ResourceLocation getBackground()
	{
		return background;
	}

	@Override
	protected void renderLabels(PoseStack stack, int mouseX, int mouseY)
	{
//		this.font.draw(stack, new TranslationTextComponent("tile.bloodmagic.alchemytable.name"), 8, 5, 4210752);
		this.font.draw(stack, new TranslatableComponent("container.inventory"), 8, 93, 4210752);
		this.font.draw(stack, container.filterStack.getHoverName(), 8, 4, 4210752);

		if (container.filterStack.getItem() instanceof IRoutingFilterProvider)
		{
			for (int i = 0; i < numberOfAddedButtons; i++)
			{
				int currentButtonState = ((IRoutingFilterProvider) container.filterStack.getItem()).getCurrentButtonState(container.filterStack, buttonKeyList.get(i), container.lastGhostSlotClicked);
				Pair<Integer, Integer> buttonLocation = getButtonLocation(i);
				Pair<Integer, Integer> textureLocation = ((IRoutingFilterProvider) container.filterStack.getItem()).getTexturePositionForState(container.filterStack, buttonKeyList.get(i), currentButtonState);

				int w = 20;
				int h = 20;

				int xl = buttonLocation.getLeft();
				int yl = buttonLocation.getRight();

				RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
				RenderSystem.setShaderTexture(0, background);
				this.blit(stack, +xl, +yl, textureLocation.getLeft(), textureLocation.getRight(), w, h);
			}
		}
	}

	@Override
	protected void renderBg(PoseStack stack, float partialTicks, int mouseX, int mouseY)
	{
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, background);
//		int i = (this.width - this.xSize) / 2;
//		int j = (this.height - this.ySize) / 2;
//		this.blit(stack, i, j, 0, 0, this.xSize, this.ySize);
//
//		int l = this.getCookProgressScaled(90);
//		this.blit(stack, i + 115, j + 14 + 90 - l, 176, 90 - l, 18, l);
//
//		for (int slotId = 0; slotId < 6; slotId++)
//		{
//			if (!((TileAlchemyTable) filterInventory).isInputSlotAccessible(slotId))
//			{
//				Slot slot = this.getContainer().getSlot(slotId);
//
//				this.blit(stack, i + slot.xPos, j + slot.yPos, 195, 1, 16, 16);
//			}
//		}

		// draw your Gui here, only thing you need to change is the path
//        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
//        this.mc.getTextureManager().bindTexture(texture);
		int x = (width - imageWidth) / 2;
		int y = (height - imageHeight) / 2;
		this.blit(stack, x, y, 0, 0, imageWidth, imageHeight);
		ItemStack held = player.getItemInHand(InteractionHand.MAIN_HAND);
		if (container.lastGhostSlotClicked >= 0)
		{
//            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
			this.blit(stack, 106 + x + 21 * (container.lastGhostSlotClicked % 3), y + 11 + 21 * (container.lastGhostSlotClicked / 3), 0, 187, 24, 24);
		}

	}

	@Override
	public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks)
	{
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		{
			this.textBox.render(matrixStack, mouseX, mouseY, partialTicks);
		}

		List<Component> tooltip = new ArrayList<>();

		if (container.filterStack.getItem() instanceof IRoutingFilterProvider)
		{
			for (int i = 0; i < numberOfAddedButtons; i++)
			{
				Pair<Integer, Integer> buttonLocation = getButtonLocation(i);
				int w = 20;
				int h = 20;

				int x = this.leftPos + buttonLocation.getLeft();
				int y = this.topPos + buttonLocation.getRight();

				if (mouseX >= x && mouseX < x + w && mouseY >= y && mouseY < y + h)
				{
					List<Component> components = ((IRoutingFilterProvider) container.filterStack.getItem()).getTextForHoverItem(container.filterStack, buttonKeyList.get(i), container.lastGhostSlotClicked);
					if (components != null && !components.isEmpty())
						tooltip.addAll(components);
				}
			}
		}

		if (!tooltip.isEmpty())
			this.renderTooltip(matrixStack, tooltip, Optional.empty(), mouseX, mouseY, font);
//			GuiUtils.drawHoveringText(matrixStack, tooltip, mouseX, mouseY, width, height, -1, font);
	}

}
