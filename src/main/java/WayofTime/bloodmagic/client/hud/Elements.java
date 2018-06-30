package WayofTime.bloodmagic.client.hud;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.client.Sprite;
import WayofTime.bloodmagic.client.hud.element.ElementDemonAura;
import WayofTime.bloodmagic.client.hud.element.ElementDivinedInformation;
import WayofTime.bloodmagic.client.hud.element.ElementHolding;
import WayofTime.bloodmagic.tile.TileAltar;
import WayofTime.bloodmagic.tile.TileIncenseAltar;
import WayofTime.bloodmagic.tile.TileInversionPillar;
import WayofTime.bloodmagic.util.helper.NumeralHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.tuple.Pair;

import javax.vecmath.Vector2f;
import java.awt.Point;
import java.util.function.Consumer;
import java.util.function.Function;

public class Elements {

    public static void registerElements() {
        ElementRegistry.registerHandler(
                new ResourceLocation(BloodMagic.MODID, "blood_altar"),
                new ElementDivinedInformation<TileAltar>(2, true, TileAltar.class) {
                    @Override
                    public void gatherInformation(Consumer<Pair<Sprite, Function<TileAltar, String>>> information) {
                        // Current tier
                        information.accept(Pair.of(
                                new Sprite(new ResourceLocation(BloodMagic.MODID, "textures/gui/widgets.png"), 0, 46, 16, 16),
                                altar -> altar == null ? "IV" : NumeralHelper.toRoman(altar.getTier().toInt())
                        ));
                        // Stored/Capacity
                        information.accept(Pair.of(
                                new Sprite(new ResourceLocation(BloodMagic.MODID, "textures/gui/widgets.png"), 16, 46, 16, 16),
                                altar -> String.format("%d/%d", altar == null ? 0 : altar.getCurrentBlood(), altar == null ? 10000 : altar.getCapacity())
                        ));
                    }
                },
                new Vector2f(0.01F, 0.01F)
        );

        ElementRegistry.registerHandler(
                new ResourceLocation(BloodMagic.MODID, "blood_altar_adv"),
                new ElementDivinedInformation<TileAltar>(5, false, TileAltar.class) {
                    @Override
                    public void gatherInformation(Consumer<Pair<Sprite, Function<TileAltar, String>>> information) {
                        // Current tier
                        information.accept(Pair.of(
                                new Sprite(new ResourceLocation(BloodMagic.MODID, "textures/gui/widgets.png"), 0, 46, 16, 16),
                                altar -> altar == null ? "IV" : NumeralHelper.toRoman(altar.getTier().toInt())
                        ));
                        // Stored/Capacity
                        information.accept(Pair.of(
                                new Sprite(new ResourceLocation(BloodMagic.MODID, "textures/gui/widgets.png"), 16, 46, 16, 16),
                                altar -> String.format("%d/%d", altar == null ? 0 : altar.getCurrentBlood(), altar == null ? 10000 : altar.getCapacity())
                        ));
                        // Crafting progress/Crafting requirement
                        information.accept(Pair.of(
                                new Sprite(new ResourceLocation(BloodMagic.MODID, "textures/gui/widgets.png"), 32, 46, 16, 16),
                                altar -> {
                                    if (altar == null || !altar.isActive())
                                        return I18n.format("hud.bloodmagic.inactive");
                                    int progress = altar.getProgress();
                                    int totalLiquidRequired = altar.getLiquidRequired() * altar.getStackInSlot(0).getCount();
                                    return String.format("%d/%d", progress, totalLiquidRequired);
                                }
                        ));
                        // Consumption rate
                        information.accept(Pair.of(
                                new Sprite(new ResourceLocation(BloodMagic.MODID, "textures/gui/widgets.png"), 48, 46, 16, 16),
                                altar -> altar == null ? "0" : String.valueOf((int) (altar.getConsumptionRate() * (altar.getConsumptionMultiplier() + 1)))
                        ));
                        // Total charge
                        information.accept(Pair.of(
                                new Sprite(new ResourceLocation(BloodMagic.MODID, "textures/gui/widgets.png"), 64, 46, 16, 16),
                                altar -> altar == null ? "0" : String.valueOf(altar.getTotalCharge())
                        ));
                    }
                },
                new Vector2f(0.01F, 0.01F)
        );

        ElementRegistry.registerHandler(
                new ResourceLocation(BloodMagic.MODID, "incense_altar"),
                new ElementDivinedInformation<TileIncenseAltar>(2, true, TileIncenseAltar.class) {
                    @Override
                    public void gatherInformation(Consumer<Pair<Sprite, Function<TileIncenseAltar, String>>> information) {
                        // Current tranquility
                        information.accept(Pair.of(
                                new Sprite(new ResourceLocation(BloodMagic.MODID, "textures/gui/widgets.png"), 80, 46, 16, 16),
                                incense -> incense == null ? "0" : String.valueOf((int) ((100D * (int) (100 * incense.tranquility)) / 100D))
                        ));
                        // Sacrifice bonus
                        information.accept(Pair.of(
                                new Sprite(new ResourceLocation(BloodMagic.MODID, "textures/gui/widgets.png"), 96, 46, 16, 16),
                                incense -> incense == null ? "0" : String.valueOf((int) (100 * incense.incenseAddition))
                        ));
                    }
                },
                new Vector2f(0.01F, 0.01F)
        );

        ElementRegistry.registerHandler(
                new ResourceLocation(BloodMagic.MODID, "inversion_pillar"),
                new ElementDivinedInformation<TileInversionPillar>(1, true, TileInversionPillar.class) {
                    @Override
                    public void gatherInformation(Consumer<Pair<Sprite, Function<TileInversionPillar, String>>> information) {
                        // Current inversion
                        information.accept(Pair.of(
                                new Sprite(new ResourceLocation(BloodMagic.MODID, "textures/gui/widgets.png"), 112, 46, 16, 16),
                                pillar -> pillar == null ? "0" : String.valueOf(((int) (10 * pillar.getCurrentInversion())) / 10D)
                        ));
                    }
                },
                new Vector2f(0.01F, 0.01F)
        );

        ElementRegistry.registerHandler(
                new ResourceLocation(BloodMagic.MODID, "demon_will_aura"),
                new ElementDemonAura(),
                new Vector2f(0.01F, 0.01F)
        );

        ElementRegistry.registerHandler(
                new ResourceLocation(BloodMagic.MODID, "holding"),
                new ElementHolding(),
                new Vector2f(0.72F, 1.0F)
        );

        ElementRegistry.readConfig();
    }
}
