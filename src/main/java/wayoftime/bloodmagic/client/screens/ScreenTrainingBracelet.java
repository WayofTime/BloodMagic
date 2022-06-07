package wayoftime.bloodmagic.client.screens;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.client.gui.GuiUtils;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.item.ItemLivingTrainer;
import wayoftime.bloodmagic.common.item.inventory.ContainerTrainingBracelet;
import wayoftime.bloodmagic.core.living.ILivingContainer;
import wayoftime.bloodmagic.core.living.LivingStats;
import wayoftime.bloodmagic.core.living.LivingUpgrade;
import wayoftime.bloodmagic.network.LivingTrainerPacket;
import wayoftime.bloodmagic.network.LivingTrainerWhitelistPacket;

public class ScreenTrainingBracelet extends ScreenBase<ContainerTrainingBracelet>
{
	private static final ResourceLocation background = BloodMagic.rl("textures/gui/trainingbracelet.png");
	public IInventory trainerInventory;
	private PlayerEntity player;
	private int left, top;

	private List<String> buttonKeyList = new ArrayList<String>();

	private int whitelistButtonPosX = 24;
	private int whitelistButtonPosY = 55;

	private int numberHoverPosX = 16;
	private int numberHoverPosY = 34;
	private int numberHoverWidth = 36;
	private int numberHoverHeight = 20;

	protected boolean isWhitelist = false;

	public ScreenTrainingBracelet(ContainerTrainingBracelet container, PlayerInventory playerInventory, ITextComponent title)
	{
		super(container, playerInventory, title);
		trainerInventory = container.inventoryTrainer;
		xSize = 176;
		ySize = 187;
		this.player = playerInventory.player;
		this.isWhitelist = ((ItemLivingTrainer) container.trainerStack.getItem()).getIsWhitelist(container.trainerStack);
	}

	@Override
	public void init()
	{
		super.init();
		left = (this.width - this.xSize) / 2;
		top = (this.height - this.ySize) / 2;

		buttonKeyList.clear();

		ItemStack filterStack = this.container.trainerStack;

		this.addButton(new Button(left + 62 - 18, top + 34, 8, 20, new StringTextComponent(">"), new IncrementPress(this, 0)));
		this.addButton(new Button(left + 34 - 18, top + 34, 8, 20, new StringTextComponent("<"), new IncrementPress(this, 1)));

		this.addButton(new Button(left + whitelistButtonPosX, top + whitelistButtonPosY, 20, 20, new StringTextComponent(""), new WhitelistTogglePress(this)));

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
//				this.addButton(addedButton);
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

		ItemStack stack = this.container.getSlot(container.lastGhostSlotClicked).getStack();
		return getStackUpgradeLevel(stack);
	}

	private int setCurrentActiveSlotUpgradeLevel(int level)
	{
		if (this.container.lastGhostSlotClicked == -1)
		{
			return 0;
		}

		int slotClicked = container.lastGhostSlotClicked;
		ItemStack stack = container.getSlot(slotClicked).getStack();
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
				container.getSlot(slotClicked).putStack(stack);
				container.inventoryTrainer.setInventorySlotContents(slotClicked, stack);
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
	protected void drawGuiContainerForegroundLayer(MatrixStack stack, int mouseX, int mouseY)
	{
		String textEntry = "" + getCurrentActiveSlotUpgradeLevel();
		int offset = -3 * textEntry.length();
		this.font.func_243248_b(stack, new StringTextComponent(textEntry), 45 - 18 + offset + 7.5f, 37 + 3, 0xFFFFFF);
//		this.font.func_243248_b(stack, new TranslationTextComponent("tile.bloodmagic.alchemytable.name"), 8, 5, 4210752);
		this.font.func_243248_b(stack, new TranslationTextComponent("container.inventory"), 8, 93, 4210752);
		this.font.func_243248_b(stack, container.trainerStack.getDisplayName(), 8, 4, 4210752);

		int w = 20;
		int h = 20;

		int xl = whitelistButtonPosX;
		int yl = whitelistButtonPosY;

		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		getMinecraft().getTextureManager().bindTexture(background);
		this.blit(stack, +xl, +yl, 176, isWhitelist ? 0 : 20, w, h);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(MatrixStack stack, float partialTicks, int mouseX, int mouseY)
	{
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		getMinecraft().getTextureManager().bindTexture(background);
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
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		this.blit(stack, x, y, 0, 0, xSize, ySize);
		ItemStack held = player.getHeldItem(Hand.MAIN_HAND);
		if (container.lastGhostSlotClicked >= 0)
		{
//            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
			this.blit(stack, 85 + x + 21 * (container.lastGhostSlotClicked % 4), y + 11 + 21 * (container.lastGhostSlotClicked / 4), 0, 187, 24, 24);
		}

	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
	{
		super.render(matrixStack, mouseX, mouseY, partialTicks);

		List<ITextComponent> tooltip = new ArrayList<>();

		// TODO: Add hover text for button

		int w = 20;
		int h = 20;

		int x = this.guiLeft + whitelistButtonPosX;
		int y = this.guiTop + whitelistButtonPosY;

		if (mouseX >= x && mouseX < x + w && mouseY >= y && mouseY < y + h)
		{
			List<ITextComponent> components = getHoverTextForWhitelistButton();
			if (components != null && !components.isEmpty())
				tooltip.addAll(components);
		}

		w = +numberHoverWidth;
		h = numberHoverHeight;

		x = this.guiLeft + numberHoverPosX;
		y = this.guiTop + numberHoverPosY;

		if (mouseX >= x && mouseX < x + w && mouseY >= y && mouseY < y + h)
		{
			List<ITextComponent> components = getHoverTextForNumber();
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
			GuiUtils.drawHoveringText(matrixStack, tooltip, mouseX, mouseY, width, height, -1, font);
	}

	private List<ITextComponent> getHoverTextForWhitelistButton()
	{
		List<ITextComponent> components = new ArrayList<>();

		if (isWhitelist)
		{
			components.add(new TranslationTextComponent("trainer.bloodmagic.whitelist"));
		} else
		{
			components.add(new TranslationTextComponent("trainer.bloodmagic.blacklist"));
		}

		return components;
	}

	private List<ITextComponent> getHoverTextForNumber()
	{
		List<ITextComponent> components = new ArrayList<>();

		if (this.container.lastGhostSlotClicked == -1)
		{
			return components;
		}

		int slotClicked = container.lastGhostSlotClicked;
		ItemStack stack = container.getSlot(slotClicked).getStack();

		if (stack.getItem() instanceof ILivingContainer)
		{
			LivingStats stats = ((ILivingContainer) stack.getItem()).getLivingStats(stack);
			if (stats != null)
			{
				for (Entry<LivingUpgrade, Double> entry : stats.getUpgrades().entrySet())
				{
					int level = entry.getKey().getLevel(entry.getValue().intValue());

					if (level > 0)
						components.add(new TranslationTextComponent("trainer.bloodmagic.allowupgrade", new TranslationTextComponent(entry.getKey().getTranslationKey()), new TranslationTextComponent("enchantment.level." + level)));
					else
						components.add(new TranslationTextComponent("trainer.bloodmagic.blockupgrade", new TranslationTextComponent(entry.getKey().getTranslationKey())));
				}
			}
		}

		return components;
	}

	public class IncrementPress implements Button.IPressable
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
				BloodMagic.packetHandler.sendToServer(new LivingTrainerPacket(player.inventory.currentItem, screen.container.lastGhostSlotClicked, newLevel));
			} else if (id == 1)
			{
				int currentLevel = getCurrentActiveSlotUpgradeLevel();
				int newLevel = setCurrentActiveSlotUpgradeLevel(currentLevel - 1);
				BloodMagic.packetHandler.sendToServer(new LivingTrainerPacket(player.inventory.currentItem, screen.container.lastGhostSlotClicked, newLevel));
			}
		}
	}

	public class WhitelistTogglePress implements Button.IPressable
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
			BloodMagic.packetHandler.sendToServer(new LivingTrainerWhitelistPacket(player.inventory.currentItem, newWhitelistState));

		}
	}
}
