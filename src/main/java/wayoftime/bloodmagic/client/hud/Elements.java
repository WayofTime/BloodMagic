package wayoftime.bloodmagic.client.hud;

import java.util.function.Consumer;
import java.util.function.Function;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec2;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.client.Sprite;
import wayoftime.bloodmagic.client.hud.element.ElementDemonAura;
import wayoftime.bloodmagic.client.hud.element.ElementDivinedInformation;
import wayoftime.bloodmagic.client.hud.element.ElementHolding;
import wayoftime.bloodmagic.common.tile.TileAltar;
import wayoftime.bloodmagic.common.tile.TileIncenseAltar;
import wayoftime.bloodmagic.util.helper.NumeralHelper;

public class Elements
{
	public static void registerElements()
	{
		ElementRegistry.registerHandler(new ResourceLocation(BloodMagic.MODID, "demon_will_aura"), new ElementDemonAura(), new Vec2(0.01f, 0.01f));

		ElementRegistry.registerHandler(BloodMagic.rl("blood_altar"), new ElementDivinedInformation<TileAltar>(2, true, TileAltar.class)
		{
			@Override
			public void gatherInformation(Consumer<Pair<Sprite, Function<TileAltar, String>>> information)
			{
				// Current tier
				information.accept(Pair.of(new Sprite(new ResourceLocation(BloodMagic.MODID, "textures/gui/widgets.png"), 0, 46, 16, 16), altar -> altar == null
						? "IV"
						: NumeralHelper.toRoman(altar.getTier())));
				// Stored/Capacity
				information.accept(Pair.of(new Sprite(new ResourceLocation(BloodMagic.MODID, "textures/gui/widgets.png"), 16, 46, 16, 16), altar -> String.format("%d/%d", altar == null
						? 0
						: altar.getCurrentBlood(), altar == null ? 10000 : altar.getCapacity())));
			}
		}, new Vec2(0.01f, 0.01f));

		ElementRegistry.registerHandler(new ResourceLocation(BloodMagic.MODID, "blood_altar_adv"), new ElementDivinedInformation<TileAltar>(5, false, TileAltar.class)
		{
			@Override
			public void gatherInformation(Consumer<Pair<Sprite, Function<TileAltar, String>>> information)
			{
				// Current tier
				information.accept(Pair.of(new Sprite(new ResourceLocation(BloodMagic.MODID, "textures/gui/widgets.png"), 0, 46, 16, 16), altar -> altar == null
						? "IV"
						: NumeralHelper.toRoman(altar.getTier())));
				// Stored/Capacity
				information.accept(Pair.of(new Sprite(new ResourceLocation(BloodMagic.MODID, "textures/gui/widgets.png"), 16, 46, 16, 16), altar -> String.format("%d/%d", altar == null
						? 0
						: altar.getCurrentBlood(), altar == null ? 10000 : altar.getCapacity())));
				// Crafting progress/Crafting requirement
				information.accept(Pair.of(new Sprite(new ResourceLocation(BloodMagic.MODID, "textures/gui/widgets.png"), 32, 46, 16, 16), altar -> {
					if (altar == null || !altar.isActive())
						return I18n.get("hud.bloodmagic.inactive");
					int progress = altar.getProgress();
					int totalLiquidRequired = altar.getLiquidRequired() * altar.getItem(0).getCount();
					return String.format("%d/%d", progress, totalLiquidRequired);
				}));
				// Consumption rate
				information.accept(Pair.of(new Sprite(new ResourceLocation(BloodMagic.MODID, "textures/gui/widgets.png"), 48, 46, 16, 16), altar -> altar == null
						? "0"
						: String.valueOf((int) (altar.getConsumptionRate() * (altar.getConsumptionMultiplier() + 1)))));
				// Total charge
				information.accept(Pair.of(new Sprite(new ResourceLocation(BloodMagic.MODID, "textures/gui/widgets.png"), 64, 46, 16, 16), altar -> altar == null
						? "0"
						: String.valueOf(altar.getTotalCharge())));
			}
		}, new Vec2(0.01f, 0.01f));

		ElementRegistry.registerHandler(new ResourceLocation(BloodMagic.MODID, "incense_altar"), new ElementDivinedInformation<TileIncenseAltar>(2, true, TileIncenseAltar.class)
		{
			@Override
			public void gatherInformation(Consumer<Pair<Sprite, Function<TileIncenseAltar, String>>> information)
			{
				// Current tranquility
				information.accept(Pair.of(new Sprite(new ResourceLocation(BloodMagic.MODID, "textures/gui/widgets.png"), 80, 46, 16, 16), incense -> incense == null
						? "0"
						: String.valueOf((int) ((100D * (int) (100 * incense.tranquility)) / 100D))));
				// Sacrifice bonus
				information.accept(Pair.of(new Sprite(new ResourceLocation(BloodMagic.MODID, "textures/gui/widgets.png"), 96, 46, 16, 16), incense -> incense == null
						? "0"
						: String.valueOf((int) (100 * incense.incenseAddition))));
			}
		}, new Vec2(0.01f, 0.01f));

		ElementRegistry.registerHandler(new ResourceLocation(BloodMagic.MODID, "holding"), new ElementHolding(), new Vec2(0.72F, 0.9F));
	}
}
