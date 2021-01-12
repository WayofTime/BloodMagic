package wayoftime.bloodmagic.loot;

import java.util.List;

import javax.annotation.Nonnull;

import com.google.gson.JsonObject;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.anointment.AnointmentHolder;
import wayoftime.bloodmagic.core.AnointmentRegistrar;

public class GlobalLootModifier
{
	public static final DeferredRegister<GlobalLootModifierSerializer<?>> GLM = DeferredRegister.create(ForgeRegistries.LOOT_MODIFIER_SERIALIZERS, BloodMagic.MODID);
	public static final RegistryObject<SilkTouchTestModifier.Serializer> SILKTOUCH = GLM.register("silk_touch_bamboo", SilkTouchTestModifier.Serializer::new);
	public static final RegistryObject<FortuneModifier.Serializer> FORTUNE = GLM.register("fortune", FortuneModifier.Serializer::new);

	private static class SilkTouchTestModifier extends LootModifier
	{
		public SilkTouchTestModifier(ILootCondition[] conditionsIn)
		{
			super(conditionsIn);
//			System.out.println("Registering silk touch modifier");
		}

		@Nonnull
		@Override
		public List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context)
		{
//			System.out.println("Testing to see if we gotta check it~");
			ItemStack ctxTool = context.get(LootParameters.TOOL);
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
			fakeTool.addEnchantment(Enchantments.SILK_TOUCH, 1);
			LootContext.Builder builder = new LootContext.Builder(context);
			builder.withParameter(LootParameters.TOOL, fakeTool);
			LootContext ctx = builder.build(LootParameterSets.BLOCK);
			LootTable loottable = context.getWorld().getServer().getLootTableManager().getLootTableFromLocation(context.get(LootParameters.BLOCK_STATE).getBlock().getLootTable());
			return loottable.generate(ctx);
		}

		private static class Serializer extends GlobalLootModifierSerializer<SilkTouchTestModifier>
		{
			@Override
			public SilkTouchTestModifier read(ResourceLocation name, JsonObject json, ILootCondition[] conditionsIn)
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
		public FortuneModifier(ILootCondition[] conditionsIn)
		{
			super(conditionsIn);
//			System.out.println("Registering silk touch modifier");
		}

//		List<ItemStack> bufferList = new ArrayList<ItemStack>();

		@Nonnull
		@Override
		public List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context)
		{
			ItemStack ctxTool = context.get(LootParameters.TOOL);
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

			if (holder.getAnointmentLevel(AnointmentRegistrar.ANOINTMENT_SILK_TOUCH.get()) > 0)
			{
				return generatedLoot;
			}

			ItemStack fakeTool = ctxTool.copy();
			fakeTool.getOrCreateTag().putBoolean("bloodmagic:checked_fortune", true);
			int baseFortuneLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, ctxTool);

			fakeTool.addEnchantment(Enchantments.FORTUNE, baseFortuneLevel + additionalFortune);
			LootContext.Builder builder = new LootContext.Builder(context);
			builder.withParameter(LootParameters.TOOL, fakeTool);
			LootContext ctx = builder.build(LootParameterSets.BLOCK);
			LootTable loottable = context.getWorld().getServer().getLootTableManager().getLootTableFromLocation(context.get(LootParameters.BLOCK_STATE).getBlock().getLootTable());
			return loottable.generate(ctx);
		}

		private static class Serializer extends GlobalLootModifierSerializer<FortuneModifier>
		{
			@Override
			public FortuneModifier read(ResourceLocation name, JsonObject json, ILootCondition[] conditionsIn)
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
}
