package wayoftime.bloodmagic.loot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import com.google.gson.JsonObject;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.anointment.AnointmentHolder;
import wayoftime.bloodmagic.core.AnointmentRegistrar;

public class GlobalLootModifier
{
//	public static final DeferredRegister<GlobalLootModifierSerializer<?>> GLM = DeferredRegister.create(ForgeRegistries.LOOT_MODIFIER_SERIALIZERS, BloodMagic.MODID);
	public static final DeferredRegister<GlobalLootModifierSerializer<?>> GLM = DeferredRegister.create(ForgeRegistries.Keys.LOOT_MODIFIER_SERIALIZERS, BloodMagic.MODID);
	public static final RegistryObject<SilkTouchTestModifier.Serializer> SILKTOUCH = GLM.register("silk_touch_bamboo", SilkTouchTestModifier.Serializer::new);
	public static final RegistryObject<FortuneModifier.Serializer> FORTUNE = GLM.register("fortune", FortuneModifier.Serializer::new);
	public static final RegistryObject<LootingModifier.Serializer> LOOTING = GLM.register("looting", LootingModifier.Serializer::new);
	public static final RegistryObject<SmeltingModifier.Serializer> SMELT = GLM.register("smelt", SmeltingModifier.Serializer::new);

	private static class SilkTouchTestModifier extends LootModifier
	{
		public SilkTouchTestModifier(LootItemCondition[] conditionsIn)
		{
			super(conditionsIn);
//			System.out.println("Registering silk touch modifier");
		}

		@Nonnull
		@Override
		public List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context)
		{
			ItemStack ctxTool = context.getParamOrNull(LootContextParams.TOOL);
			// return early if silk-touch is already applied (otherwise we'll get stuck in
			// an infinite loop).
			if (EnchantmentHelper.getEnchantments(ctxTool).containsKey(Enchantments.SILK_TOUCH))
				return generatedLoot;
			AnointmentHolder holder = AnointmentHolder.fromItemStack(ctxTool);
			if (holder == null || holder.getAnointmentLevel(AnointmentRegistrar.ANOINTMENT_SILK_TOUCH.get()) <= 0)
			{
				return generatedLoot;
			}
			ItemStack fakeTool = ctxTool.copy();
			fakeTool.enchant(Enchantments.SILK_TOUCH, 1);
			LootContext.Builder builder = new LootContext.Builder(context);
			builder.withParameter(LootContextParams.TOOL, fakeTool);
			LootContext ctx = builder.create(LootContextParamSets.BLOCK);
			LootTable loottable = context.getLevel().getServer().getLootTables().get(context.getParamOrNull(LootContextParams.BLOCK_STATE).getBlock().getLootTable());
			return loottable.getRandomItems(ctx);
		}

		private static class Serializer extends GlobalLootModifierSerializer<SilkTouchTestModifier>
		{
			@Override
			public SilkTouchTestModifier read(ResourceLocation name, JsonObject json, LootItemCondition[] conditionsIn)
			{
				return new SilkTouchTestModifier(conditionsIn);
			}

			@Override
			public JsonObject write(SilkTouchTestModifier instance)
			{
				return makeConditions(instance.conditions);
			}
		}
	}

	private static class FortuneModifier extends LootModifier
	{
		public FortuneModifier(LootItemCondition[] conditionsIn)
		{
			super(conditionsIn);
//			System.out.println("Registering silk touch modifier");
		}

//		List<ItemStack> bufferList = new ArrayList<ItemStack>();

		@Nonnull
		@Override
		public List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context)
		{
			ItemStack ctxTool = context.getParamOrNull(LootContextParams.TOOL);
			// return early if silk-touch is already applied (otherwise we'll get stuck in
			// an infinite loop).
			if (ctxTool.getTag() != null && ctxTool.getTag().getBoolean("bloodmagic:checked_fortune"))
			{
				return generatedLoot;
			}

			if (EnchantmentHelper.getEnchantments(ctxTool).containsKey(Enchantments.SILK_TOUCH))
				return generatedLoot;
			AnointmentHolder holder = AnointmentHolder.fromItemStack(ctxTool);
			if (holder == null)
			{
				return generatedLoot;
			}

			int additionalFortune = holder.getAnointmentLevel(AnointmentRegistrar.ANOINTMENT_FORTUNE.get());
			if (additionalFortune <= 0)
			{
				return generatedLoot;
			}

			ItemStack fakeTool = ctxTool.copy();
			fakeTool.getOrCreateTag().putBoolean("bloodmagic:checked_fortune", true);
			int baseFortuneLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_FORTUNE, ctxTool);

			Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(fakeTool);
			enchants.put(Enchantments.BLOCK_FORTUNE, baseFortuneLevel + additionalFortune);
			EnchantmentHelper.setEnchantments(enchants, fakeTool);

//			EnchantmentHelper.setEnchantmentLevel(p_182441_, p_182442_);

			LootContext.Builder builder = new LootContext.Builder(context);
			builder.withParameter(LootContextParams.TOOL, fakeTool);
			LootContext ctx = builder.create(LootContextParamSets.BLOCK);
			LootTable loottable = context.getLevel().getServer().getLootTables().get(context.getParamOrNull(LootContextParams.BLOCK_STATE).getBlock().getLootTable());
			return loottable.getRandomItems(ctx);
		}

		private static class Serializer extends GlobalLootModifierSerializer<FortuneModifier>
		{
			@Override
			public FortuneModifier read(ResourceLocation name, JsonObject json, LootItemCondition[] conditionsIn)
			{
				return new FortuneModifier(conditionsIn);
			}

			@Override
			public JsonObject write(FortuneModifier instance)
			{
				return makeConditions(instance.conditions);
			}
		}
	}

	private static class LootingModifier extends LootModifier
	{
		public LootingModifier(LootItemCondition[] conditionsIn)
		{
			super(conditionsIn);
		}

		@Nonnull
		@Override
		public List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context)
		{
//			System.out.println("Checking for looting");
//			Entity killerEntity = context.get(LootParameters.KILLER_ENTITY);
//			if (!(killerEntity instanceof PlayerEntity))
//			{
			return generatedLoot;
//			}
//			Entity killedEntity = context.get(LootParameters.THIS_ENTITY);
//			if (!(killedEntity instanceof LivingEntity))
//			{
//				return generatedLoot;
//			}
//			ItemStack ctxTool = ((PlayerEntity) killerEntity).getHeldItemMainhand();
//			// return early if silk-touch is already applied (otherwise we'll get stuck in
//			// an infinite loop).
//
//			System.out.println("Checking looting. ItemStack context: ");
////			EndermanEntity d;
//			if (ctxTool.getTag() != null && ctxTool.getTag().getBoolean("bloodmagic:checked_looting"))
//			{
//				return generatedLoot;
//			}
//
//			if (EnchantmentHelper.getEnchantments(ctxTool).containsKey(Enchantments.SILK_TOUCH))
//				return generatedLoot;
//			AnointmentHolder holder = AnointmentHolder.fromItemStack(ctxTool);
//			if (holder == null)
//			{
//				return generatedLoot;
//			}
//
//			int additionalLooting = holder.getAnointmentLevel(AnointmentRegistrar.ANOINTMENT_LOOTING.get()) * 20;
//			if (additionalLooting <= 0)
//			{
//				return generatedLoot;
//			}
//
////			if (holder.getAnointmentLevel(AnointmentRegistrar.ANOINTMENT_SILK_TOUCH.get()) > 0)
////			{
////				return generatedLoot;
////			}
//
//			ItemStack fakeTool = ctxTool.copy();
//			fakeTool.getOrCreateTag().putBoolean("bloodmagic:checked_looting", true);
//			int baseLootingLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.LOOTING, ctxTool);
//
//			fakeTool.addEnchantment(Enchantments.LOOTING, baseLootingLevel + additionalLooting);
//			LootContext.Builder builder = new LootContext.Builder(context);
//			builder.withParameter(LootParameters.TOOL, fakeTool);
//			LootContext ctx = builder.build(LootParameterSets.ENTITY);
//			ResourceLocation resource = ((LivingEntity) killedEntity).getLootTableResourceLocation();
//			LootTable loottable = context.getWorld().getServer().getLootTableManager().getLootTableFromLocation(resource);
//			return loottable.generate(ctx);
		}

		private static class Serializer extends GlobalLootModifierSerializer<LootingModifier>
		{
			@Override
			public LootingModifier read(ResourceLocation name, JsonObject json, LootItemCondition[] conditionsIn)
			{
				return new LootingModifier(conditionsIn);
			}

			@Override
			public JsonObject write(LootingModifier instance)
			{
				return makeConditions(instance.conditions);
			}
		}

//		private static class ExtendedLootContext extends LootContext
//		{
//			public ExtendedLootContext()
//			{
//				
//			}
//		}
	}

	private static class SmeltingModifier extends LootModifier
	{
		public SmeltingModifier(LootItemCondition[] conditionsIn)
		{
			super(conditionsIn);
		}

		@Nonnull
		@Override
		public List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context)
		{
			ItemStack ctxTool = context.getParamOrNull(LootContextParams.TOOL);
			// return early if silk-touch is already applied (otherwise we'll get stuck in
			// an infinite loop).
			if (ctxTool.getTag() == null)
			{
				return generatedLoot;
			}

			AnointmentHolder holder = AnointmentHolder.fromItemStack(ctxTool);
			if (holder == null)
			{
				return generatedLoot;
			}

			int smeltingLevel = holder.getAnointmentLevel(AnointmentRegistrar.ANOINTMENT_SMELTING.get());
			if (smeltingLevel <= 0)
			{
				return generatedLoot;
			}

			ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
			generatedLoot.forEach((stack) -> ret.add(smelt(stack, context)));
			return ret;
		}

		private static ItemStack smelt(ItemStack stack, LootContext context)
		{
			return context.getLevel().getRecipeManager().getRecipeFor(RecipeType.SMELTING, new SimpleContainer(stack), context.getLevel()).map(SmeltingRecipe::getResultItem).filter(itemStack -> !itemStack.isEmpty()).map(itemStack -> ItemHandlerHelper.copyStackWithSize(itemStack, stack.getCount() * itemStack.getCount())).orElse(stack);
		}

		private static class Serializer extends GlobalLootModifierSerializer<SmeltingModifier>
		{
			@Override
			public SmeltingModifier read(ResourceLocation name, JsonObject json, LootItemCondition[] conditionsIn)
			{
				return new SmeltingModifier(conditionsIn);
			}

			@Override
			public JsonObject write(SmeltingModifier instance)
			{
				return makeConditions(instance.conditions);
			}
		}
	}
}
