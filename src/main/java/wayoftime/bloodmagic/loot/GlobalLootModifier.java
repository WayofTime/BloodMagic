package wayoftime.bloodmagic.loot;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.anointment.AnointmentHolder;
import wayoftime.bloodmagic.common.tags.BloodMagicTags;
import wayoftime.bloodmagic.core.AnointmentRegistrar;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.function.Supplier;

public class GlobalLootModifier
{
//	public static final DeferredRegister<GlobalLootModifierSerializer<?>> GLM = DeferredRegister.create(ForgeRegistries.LOOT_MODIFIER_SERIALIZERS, BloodMagic.MODID);
	public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> GLM = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, BloodMagic.MODID);
	public static final RegistryObject<Codec<SilkTouchTestModifier>> SILKTOUCH = GLM.register("silk_touch_bamboo", SilkTouchTestModifier.CODEC);
	public static final RegistryObject<Codec<FortuneModifier>> FORTUNE = GLM.register("fortune", FortuneModifier.CODEC);
	public static final RegistryObject<Codec<LootingModifier>> LOOTING = GLM.register("looting", LootingModifier.CODEC);
	public static final RegistryObject<Codec<SmeltingModifier>> SMELT = GLM.register("smelt", SmeltingModifier.CODEC);
	public static final RegistryObject<Codec<VoidingModifier>> VOID = GLM.register("voiding", VoidingModifier.CODEC);

	private static class SilkTouchTestModifier extends LootModifier
	{
		public static final Supplier<Codec<SilkTouchTestModifier>> CODEC = Suppliers.memoize(() -> RecordCodecBuilder.create(inst -> codecStart(inst).apply(inst, SilkTouchTestModifier::new)));
		public SilkTouchTestModifier(LootItemCondition[] conditionsIn)
		{
			super(conditionsIn);
//			System.out.println("Registering silk touch modifier");
		}

		@Nonnull
		@Override
		protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
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
			LootParams.Builder builder = new LootParams.Builder(context.getLevel());
			builder.withParameter(LootContextParams.TOOL, fakeTool);
			LootParams ctx = builder.create(LootContextParamSets.EMPTY);
			LootTable loottable = context.getLevel().getServer().getLootData().getLootTable(context.getParamOrNull(LootContextParams.BLOCK_STATE).getBlock().getLootTable());
			return loottable.getRandomItems(ctx);
		}

		@Override
		public Codec<? extends IGlobalLootModifier> codec() {
			return CODEC.get();
		}
	}

	private static class FortuneModifier extends LootModifier
	{
		public static final Supplier<Codec<FortuneModifier>> CODEC = Suppliers.memoize(() -> RecordCodecBuilder.create(inst -> codecStart(inst).apply(inst, FortuneModifier::new)));
		public FortuneModifier(LootItemCondition[] conditionsIn)
		{
			super(conditionsIn);
//			System.out.println("Registering silk touch modifier");
		}

//		List<ItemStack> bufferList = new ArrayList<ItemStack>();

		@Nonnull
		@Override
		protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
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

			LootParams.Builder builder = new LootParams.Builder(context.getLevel());
			builder.withParameter(LootContextParams.TOOL, fakeTool);
			LootParams ctx = builder.create(LootContextParamSets.EMPTY);
			LootTable loottable = context.getLevel().getServer().getLootData().getLootTable(context.getParamOrNull(LootContextParams.BLOCK_STATE).getBlock().getLootTable());
			return loottable.getRandomItems(ctx);
		}

		@Override
		public Codec<? extends IGlobalLootModifier> codec() {
			return CODEC.get();
		}
	}

	private static class LootingModifier extends LootModifier
	{
		public static final Supplier<Codec<LootingModifier>> CODEC = Suppliers.memoize(() -> RecordCodecBuilder.create(inst -> codecStart(inst).apply(inst, LootingModifier::new)));
		public LootingModifier(LootItemCondition[] conditionsIn)
		{
			super(conditionsIn);
		}

		@Nonnull
		@Override
		protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
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

		@Override
		public Codec<? extends IGlobalLootModifier> codec() {
			return CODEC.get();
		}
	}

	private static class SmeltingModifier extends LootModifier
	{
		public static final Supplier<Codec<SmeltingModifier>> CODEC = Suppliers.memoize(() -> RecordCodecBuilder.create(inst -> codecStart(inst).apply(inst, SmeltingModifier::new)));
		public SmeltingModifier(LootItemCondition[] conditionsIn)
		{
			super(conditionsIn);
		}

		@Nonnull
		@Override
		protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
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

			ObjectArrayList<ItemStack> ret = new ObjectArrayList<>();
			generatedLoot.forEach((stack) -> ret.add(smelt(stack, context)));
			return ret;
		}

		private static ItemStack smelt(ItemStack stack, LootContext context)
		{
			return context.getLevel().getRecipeManager().getRecipeFor(RecipeType.SMELTING, new SimpleContainer(stack), context.getLevel())
					.map((a) -> a.getResultItem(context.getLevel().registryAccess()))
					.filter(itemStack -> !itemStack.isEmpty())
					.map(itemStack -> ItemHandlerHelper.copyStackWithSize(itemStack, stack.getCount() * itemStack.getCount()))
					.orElse(stack);
		}

		@Override
		public Codec<? extends IGlobalLootModifier> codec() {
			return CODEC.get();
		}
	}

	private static class VoidingModifier extends LootModifier
	{
		public static final Supplier<Codec<VoidingModifier>> CODEC = Suppliers.memoize(() -> RecordCodecBuilder.create(inst -> codecStart(inst).apply(inst, VoidingModifier::new)));
		public VoidingModifier(LootItemCondition[] conditionsIn)
		{
			super(conditionsIn);
		}

		@Nonnull
		@Override
		protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
			BlockState blockState = context.getParamOrNull(LootContextParams.BLOCK_STATE);
			if (blockState == null)
			{
				return generatedLoot;
			}

			if (!blockState.is(BloodMagicTags.Blocks.MUNDANE_BLOCK))
			{
				return generatedLoot;
			}

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

			int voidingLevel = holder.getAnointmentLevel(AnointmentRegistrar.ANOINTMENT_VOIDING.get());
			if (voidingLevel <= 0)
			{
				return generatedLoot;
			}

			ObjectArrayList<ItemStack> ret = new ObjectArrayList<>();
//			generatedLoot.forEach((stack) -> ret.add(smelt(stack, context)));
			return ret;
		}

		@Override
		public Codec<? extends IGlobalLootModifier> codec() {
			return CODEC.get();
		}
	}
}
