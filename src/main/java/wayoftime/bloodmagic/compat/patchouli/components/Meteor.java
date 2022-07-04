package wayoftime.bloodmagic.compat.patchouli.components;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.UnaryOperator;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import vazkii.patchouli.api.IComponentRenderContext;
import vazkii.patchouli.api.ICustomComponent;
import vazkii.patchouli.api.IVariable;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.meteor.MeteorLayer;
import wayoftime.bloodmagic.common.meteor.RandomBlockContainer;
import wayoftime.bloodmagic.common.recipe.BloodMagicRecipeType;
import wayoftime.bloodmagic.recipe.RecipeMeteor;
import wayoftime.bloodmagic.util.helper.TextHelper;

public class Meteor implements ICustomComponent
{
	private final transient int ITEMSPERROW = 6;
	private final transient int ROWHEIGHT = 31;
	private final transient ResourceLocation METEORBOXESTEXTURE = new ResourceLocation(BloodMagic.MODID, "textures/gui/patchouli_book/meteor_grids.png");
	private final transient Minecraft mc = Minecraft.getInstance();

	private transient World world = mc.world;
	private transient RecipeMeteor recipeMeteor;

	private transient int syphon, totalLayerCount;
	private transient Ingredient input;
	private transient List<Integer> layerRadius = new ArrayList<Integer>();
	private transient List<List<Pair<List<ItemStack>, String>>> outputList = new ArrayList<List<Pair<List<ItemStack>, String>>>();
	private transient List<List<Integer>> layerRowSizes = new ArrayList<List<Integer>>();

	@Override
	public void onVariablesAvailable(UnaryOperator<IVariable> lookup)
	{
		Optional<? extends IRecipe<?>> recipeHandler = world.getRecipeManager().getRecipe(new ResourceLocation(lookup.apply(IVariable.wrap("#recipe")).asString()));
		if (recipeHandler.isPresent())
		{
			IRecipe<?> recipe = recipeHandler.get();
			if (recipe.getType().equals(BloodMagicRecipeType.METEOR))
			{
				this.recipeMeteor = (RecipeMeteor) recipe;
			}
		}
		if (this.recipeMeteor == null)
			LogManager.getLogger().warn("Buidebook missing Meteor recipe {}:, id");
	}

	@Override
	public void build(int componentX, int componentY, int pageNum)
	{
		if (recipeMeteor != null)
		{
			this.input = recipeMeteor.getInput();
			this.syphon = recipeMeteor.getSyphon();

			for (MeteorLayer layer : recipeMeteor.getLayerList())
			{
				this.totalLayerCount++;
				this.layerRadius.add(layer.layerRadius);

				List<Pair<List<ItemStack>, Integer>> layerWeightList = new ArrayList<Pair<List<ItemStack>, Integer>>();
				List<Pair<List<ItemStack>, String>> layerOutputList = new ArrayList<Pair<List<ItemStack>, String>>();
				int cellsNeeded = 0;
				int sumEntryWeights = 0;
				int fillWeight = 0;
				List<Block> shellBlocks = layer.shellBlock != null ? layer.shellBlock.getAllBlocks() : null;
				if (shellBlocks != null)
				{
					cellsNeeded++;
					layerWeightList.add(Pair.of(itemStacksFromBlocks(shellBlocks), -1)); // -1 indicates Shell.
				}

				for (Pair<RandomBlockContainer, Integer> pair : layer.weightList)
				{ // remove empty entries for this Instance, convert to a list of ItemStacks, and
					// sum used Weights.
					List<Block> blockList = pair.getLeft().getAllBlocks(); // this is crashing on servers.
					if (blockList != null)
					{
						cellsNeeded++;
						int weight = pair.getRight();
						sumEntryWeights += weight;
						layerWeightList.add(Pair.of(itemStacksFromBlocks(blockList), weight));
					}
				}

				int totalWeight = Math.max(sumEntryWeights + layer.additionalTotalWeight, layer.minWeight);

				// 10000 weight -> 100.00% of the meteor. 01234 -> 12.3%.
				for (int i = 0; i < layerWeightList.size(); i++)
				{
					int weight = layerWeightList.get(i).getRight();
					if (weight >= 0) // skip -1 for Shell
					{
						weight = weight * 10000 / totalWeight;
						layerWeightList.set(i, Pair.of(layerWeightList.get(i).getLeft(), weight));
					}
				}

				if (totalWeight != 0)
					fillWeight = 10000 - ((sumEntryWeights * 10000) / totalWeight);
				else
				{
					fillWeight = 10000; // all fill.
				}
				if (fillWeight > 0)
				{
					layerWeightList.add(Pair.of(itemStacksFromBlocks(layer.fillBlock.getAllBlocks()), fillWeight));
					cellsNeeded++;
				}

				for (int i = 0; i < layerWeightList.size(); i++)
				{
					String outputWeight = "";
					int weight = layerWeightList.get(i).getRight();
					if (weight == -1)
						outputWeight = TextHelper.localize("guide.patchouli.bloodmagic.meteor.shell");
					else if (weight == 10000)
						outputWeight = "100%";
					else
						outputWeight = String.format("%4.1f%%", (float) weight / 100);

					layerOutputList.add(Pair.of(layerWeightList.get(i).getLeft(), outputWeight));
				}
				outputList.add(layerOutputList);

				List<Integer> rowSizes = new ArrayList<Integer>();
				while (cellsNeeded / ITEMSPERROW > 0) // 6 or more items
				{
					rowSizes.add(ITEMSPERROW);
					cellsNeeded -= ITEMSPERROW;
				}
				if (cellsNeeded > 0) // any remainder for last row.
					rowSizes.add(cellsNeeded);
				this.layerRowSizes.add(rowSizes);
			}
		}
	}

	@Override
	public void render(MatrixStack ms, IComponentRenderContext context, float pticks, int mouseX, int mouseY)
	{
		if (recipeMeteor != null)
		{
			// TODO: Texture Alpha.

			int y = -10;
			int x = 2;
			FontRenderer font = mc.fontRenderer;
			ITextComponent textRender;

			context.renderIngredient(ms, 2, -3, mouseX, mouseY, this.input);
			RenderSystem.enableBlend();
			RenderSystem.color3f(1F, 1F, 1F);
			mc.textureManager.bindTexture(new ResourceLocation(BloodMagic.MODID, "textures/gui/patchouli_book/crafting.png"));
			AbstractGui.blit(ms, -1, -6, 71, 15, 22, 22, 128, 256);

			textRender = new StringTextComponent(TextHelper.localize("guide.patchouli.bloodmagic.meteor.syphon", syphon)).mergeStyle(context.getFont());
			font.func_238422_b_(ms, textRender.func_241878_f(), 23, 7, 0);

			y = -14;
			for (int i = 0; i < this.layerRowSizes.size(); i++)
			{
				textRender = new StringTextComponent(TextHelper.localize("guide.patchouli.bloodmagic.meteor.radius", layerRadius.get(i))).mergeStyle(context.getFont());
				font.func_238422_b_(ms, textRender.func_241878_f(), 0, y + 31, 0);
				y += 7;
				for (int j : this.layerRowSizes.get(i))
				{
					rowBlit(ms, y += ROWHEIGHT, j);
				}
			}
			y = -11;
			for (List<Pair<List<ItemStack>, String>> outputPair : outputList)
			{
				y += 7;
				for (int i = 0; i < outputPair.size(); i++)
				{
					if (i % ITEMSPERROW == 0)
					{
						x = -17;
						y += ROWHEIGHT;
					}
					Pair<List<ItemStack>, String> output = outputPair.get(i);
					context.renderItemStack(ms, x += 19, y + 1, mouseX, mouseY, output.getLeft().get(context.getTicksInBook() / 20 % output.getLeft().size()));
					String text = output.getRight();
					textRender = new StringTextComponent(text).mergeStyle(context.getFont());
					if (text.equals("100%"))
						font.func_238422_b_(ms, textRender.func_241878_f(), (float) (x + 1.5), y + 18, 0);
					else
						font.func_238422_b_(ms, textRender.func_241878_f(), (float) (x - 0.5), y + 18, 0);
				}
			}

		}
	}

	private List<ItemStack> itemStacksFromBlocks(List<Block> blocks)
	{
		List<ItemStack> items = new ArrayList<ItemStack>();
		for (Block block : blocks) items.add(new ItemStack(block));
		return items;
	}

	private void rowBlit(MatrixStack ms, int x, int y, int blocks)
	{
		RenderSystem.enableBlend();
		RenderSystem.color3f(1F, 1F, 1F);
		mc.textureManager.bindTexture(METEORBOXESTEXTURE);
		AbstractGui.blit(ms, x, y, 0, blocks * 32, 128, 32, 128, 256);
	}

	private void rowBlit(MatrixStack ms, int y, int blocks)
	{
		rowBlit(ms, -2, y, blocks);
	}
}