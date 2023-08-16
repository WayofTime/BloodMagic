package wayoftime.bloodmagic.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.container.item.ContainerTrainingBracelet;
import wayoftime.bloodmagic.common.item.ItemLivingTrainer;
import wayoftime.bloodmagic.core.living.ILivingContainer;
import wayoftime.bloodmagic.core.living.LivingStats;
import wayoftime.bloodmagic.core.living.LivingUpgrade;
import wayoftime.bloodmagic.network.LivingTrainerPacket;
import wayoftime.bloodmagic.network.LivingTrainerWhitelistPacket;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;

public class ScreenTrainingBracelet extends ScreenBase<ContainerTrainingBracelet>
{
	private static final ResourceLocation background = BloodMagic.rl("textures/gui/trainingbracelet.png");
	public Container trainerInventory;
	private Player player;
	private int left, top;

	private List<String> buttonKeyList = new ArrayList<String>();

	private int whitelistButtonPosX = 24;
	private int whitelistButtonPosY = 55;

	private int numberHoverPosX = 16;
	private int numberHoverPosY = 34;
	private int numberHoverWidth = 36;
	private int numberHoverHeight = 20;

	protected boolean isWhitelist = false;

	public ScreenTrainingBracelet(ContainerTrainingBracelet container, Inventory playerInventory, Component title)
	{
		super(container, playerInventory, title);
		trainerInventory = container.inventoryTrainer;
		imageWidth = 176;
		imageHeight = 187;
		this.player = playerInventory.player;
		this.isWhitelist = ((ItemLivingTrainer) container.trainerStack.getItem()).getIsWhitelist(container.trainerStack);
	}

	@Override
	public void init()
	{
		super.init();
		left = (this.width - this.imageWidth) / 2;
		top = (this.height - this.imageHeight) / 2;

		buttonKeyList.clear();

		ItemStack filterStack = this.container.trainerStack;

		this.addRenderableWidget(Button.builder( Component.literal(">"), new IncrementPress(this, 0)).pos(left + 62 - 18, top + 34).size( 8, 20).build());
		this.addRenderableWidget(Button.builder( Component.literal("<"), new IncrementPress(this, 1)).pos(left + 34 - 18, top + 34).size( 8, 20).build());

		this.addRenderableWidget(Button.builder( Component.literal(""), new WhitelistTogglePress(this)).pos(left + whitelistButtonPosX, top + whitelistButtonPosY).size( 20, 20).build());

//		if (filterStack.getItem() instanceof IItemFilterProvider)
//		{
//			IItemFilterProvider provider = (IItemFilterProvider) filterStack.getItem();
//			List<Pair<String, Button.IPressable>> buttonActionList = provider.getButtonAction(this.container);
//
//			for (Pair<String, Button.IPressable> pair : buttonActionList)
//			{
//				if (buttonKeyList.contains(pair.getKey()))
//				{
//					continue;
//				}
//				buttonKeyList.add(pair.getKey());
//				Pair<Integer, Integer> buttonLocation = getButtonLocation(numberOfAddedButtons);
//				Button addedButton = new Button(left + buttonLocation.getLeft(), top + buttonLocation.getRight(), 20, 20, new StringTextComponent(""), pair.getRight());
//
//				if (!provider.isButtonGlobal(filterStack, pair.getKey()))
//				{
//					addedButton.active = false;
//				}
//
//				this.addWidget(addedButton);
//				numberOfAddedButtons++;
//			}
//		}
	}

	private int getCurrentActiveSlotUpgradeLevel()
	{
		if (this.container.lastGhostSlotClicked == -1)
		{
			return 0;
		}

		ItemStack stack = this.container.getSlot(container.lastGhostSlotClicked).getItem();
		return getStackUpgradeLevel(stack);
	}

	private int setCurrentActiveSlotUpgradeLevel(int level)
	{
		if (this.container.lastGhostSlotClicked == -1)
		{
			return 0;
		}

		int slotClicked = container.lastGhostSlotClicked;
		ItemStack stack = container.getSlot(slotClicked).getItem();
		level = Math.max(0, Math.min(level, getMaxUpgradeLevel(stack)));

		if (stack.getItem() instanceof ILivingContainer)
		{
			LivingStats stats = ((ILivingContainer) stack.getItem()).getLivingStats(stack);
			if (stats != null)
			{
				LivingStats newStats = new LivingStats();
				for (Entry<LivingUpgrade, Double> entry : stats.getUpgrades().entrySet())
				{
					double exp = level == 0 ? 0.01 : entry.getKey().getLevelExp(level);

					newStats.addExperience(entry.getKey().getKey(), exp);
//					return entry.getKey().getLevel(entry.getValue().intValue());
				}

				((ILivingContainer) stack.getItem()).updateLivingStats(stack, newStats);
				container.getSlot(slotClicked).set(stack);
				container.inventoryTrainer.setItem(slotClicked, stack);
				((ItemLivingTrainer) container.trainerStack.getItem()).setTomeLevel(container.trainerStack, slotClicked, level);

//				System.out.println("Changing level on client side");
//				((ILivingContainer) stack.getItem())

				return level;
			}
		}

		return 0;
	}

	private int getStackUpgradeLevel(ItemStack stack)
	{
		if (stack.getItem() instanceof ILivingContainer)
		{
			LivingStats stats = ((ILivingContainer) stack.getItem()).getLivingStats(stack);
			if (stats != null)
			{
				for (Entry<LivingUpgrade, Double> entry : stats.getUpgrades().entrySet())
				{
					return entry.getKey().getLevel(entry.getValue().intValue());
				}
			}
		}

		return 0;
	}

	private int getMaxUpgradeLevel(ItemStack stack)
	{
		if (stack.getItem() instanceof ILivingContainer)
		{
			LivingStats stats = ((ILivingContainer) stack.getItem()).getLivingStats(stack);
			if (stats != null)
			{
				for (Entry<LivingUpgrade, Double> entry : stats.getUpgrades().entrySet())
				{
					return entry.getKey().getLevel(Integer.MAX_VALUE);
				}
			}
		}

		return 0;
	}

	@Override
	public ResourceLocation getBackground()
	{
		return background;
	}

	@Override
	protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY)
	{
		String textEntry = "" + getCurrentActiveSlotUpgradeLevel();
		int offset = -3 * textEntry.length();
		guiGraphics.drawString(this.font, Component.literal(textEntry), 45 - 18 + offset + 7, 37 + 3, 0xFFFFFF, false);
		guiGraphics.drawString(this.font, Component.translatable("container.inventory"), 8, 93, 4210752, false);
		guiGraphics.drawString(this.font, container.trainerStack.getHoverName(), 8, 4, 4210752, false);

		int w = 20;
		int h = 20;

		int xl = whitelistButtonPosX;
		int yl = whitelistButtonPosY;

		guiGraphics.blit(background, +xl, +yl, 176, isWhitelist ? 0 : 20, w, h);
	}

	@Override
	protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY)
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
//        this.mc.getTextureManager().bindForSetupTexture(texture);
		int x = (width - imageWidth) / 2;
		int y = (height - imageHeight) / 2;
		guiGraphics.blit(background, x, y, 0, 0, imageWidth, imageHeight);
		ItemStack held = player.getItemInHand(InteractionHand.MAIN_HAND);
		if (container.lastGhostSlotClicked >= 0)
		{
//            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			guiGraphics.blit(background, 85 + x + 21 * (container.lastGhostSlotClicked % 4), y + 11 + 21 * (container.lastGhostSlotClicked / 4), 0, 187, 24, 24);
		}

	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks)
	{
		super.render(guiGraphics, mouseX, mouseY, partialTicks);

		List<Component> tooltip = new ArrayList<>();

		// TODO: Add hover text for button

		int w = 20;
		int h = 20;

		int x = this.leftPos + whitelistButtonPosX;
		int y = this.topPos + whitelistButtonPosY;

		if (mouseX >= x && mouseX < x + w && mouseY >= y && mouseY < y + h)
		{
			List<Component> components = getHoverTextForWhitelistButton();
			if (components != null && !components.isEmpty())
				tooltip.addAll(components);
		}

		w = +numberHoverWidth;
		h = numberHoverHeight;

		x = this.leftPos + numberHoverPosX;
		y = this.topPos + numberHoverPosY;

		if (mouseX >= x && mouseX < x + w && mouseY >= y && mouseY < y + h)
		{
			List<Component> components = getHoverTextForNumber();
			if (components != null && !components.isEmpty())
				tooltip.addAll(components);
		}

//		if (container.trainerStack.getItem() instanceof IItemFilterProvider)
//		{
//			for (int i = 0; i < numberOfAddedButtons; i++)
//			{
//				Pair<Integer, Integer> buttonLocation = getButtonLocation(i);

//			}
//		}

		if (!tooltip.isEmpty())
			guiGraphics.renderTooltip(this.font, tooltip, Optional.empty(), mouseX, mouseY);
	}

	private List<Component> getHoverTextForWhitelistButton()
	{
		List<Component> components = new ArrayList<>();

		if (isWhitelist)
		{
			components.add(Component.translatable("trainer.bloodmagic.whitelist"));
		} else
		{
			components.add(Component.translatable("trainer.bloodmagic.blacklist"));
		}

		return components;
	}

	private List<Component> getHoverTextForNumber()
	{
		List<Component> components = new ArrayList<>();

		if (this.container.lastGhostSlotClicked == -1)
		{
			return components;
		}

		int slotClicked = container.lastGhostSlotClicked;
		ItemStack stack = container.getSlot(slotClicked).getItem();

		if (stack.getItem() instanceof ILivingContainer)
		{
			LivingStats stats = ((ILivingContainer) stack.getItem()).getLivingStats(stack);
			if (stats != null)
			{
				for (Entry<LivingUpgrade, Double> entry : stats.getUpgrades().entrySet())
				{
					int level = entry.getKey().getLevel(entry.getValue().intValue());

					if (level > 0)
						components.add(Component.translatable("trainer.bloodmagic.allowupgrade", Component.translatable(entry.getKey().getTranslationKey()), Component.translatable("enchantment.level." + level)));
					else
						components.add(Component.translatable("trainer.bloodmagic.blockupgrade", Component.translatable(entry.getKey().getTranslationKey())));
				}
			}
		}

		return components;
	}

	public class IncrementPress implements Button.OnPress
	{
		private final ScreenTrainingBracelet screen;
		private final int id;

		public IncrementPress(ScreenTrainingBracelet screen, int id)
		{
			this.screen = screen;
			this.id = id;
		}

		@Override
		public void onPress(Button button)
		{
			if (screen.container.lastGhostSlotClicked == -1)
			{
				return;
			}
			if (id == 0)
			{
				// Increment
				int currentLevel = getCurrentActiveSlotUpgradeLevel();
				int newLevel = setCurrentActiveSlotUpgradeLevel(currentLevel + 1);
//				System.out.println("Sending incrementation packet with new level of: " + newLevel);
				BloodMagic.packetHandler.sendToServer(new LivingTrainerPacket(player.getInventory().selected, screen.container.lastGhostSlotClicked, newLevel));
			} else if (id == 1)
			{
				int currentLevel = getCurrentActiveSlotUpgradeLevel();
				int newLevel = setCurrentActiveSlotUpgradeLevel(currentLevel - 1);
				BloodMagic.packetHandler.sendToServer(new LivingTrainerPacket(player.getInventory().selected, screen.container.lastGhostSlotClicked, newLevel));
			}
		}
	}

	public class WhitelistTogglePress implements Button.OnPress
	{
		private final ScreenTrainingBracelet screen;

		public WhitelistTogglePress(ScreenTrainingBracelet screen)
		{
			this.screen = screen;
		}

		@Override
		public void onPress(Button button)
		{
			boolean newWhitelistState = !screen.isWhitelist;

			screen.isWhitelist = newWhitelistState;
			((ItemLivingTrainer) screen.container.trainerStack.getItem()).setIsWhitelist(screen.container.trainerStack, newWhitelistState);
			BloodMagic.packetHandler.sendToServer(new LivingTrainerWhitelistPacket(player.getInventory().selected, newWhitelistState));

		}
	}
}
