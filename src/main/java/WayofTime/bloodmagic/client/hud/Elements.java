package WayofTime.bloodmagic.client.hud;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.client.Sprite;
import WayofTime.bloodmagic.tile.TileAltar;
import WayofTime.bloodmagic.tile.TileIncenseAltar;
import WayofTime.bloodmagic.tile.TileInversionPillar;
import WayofTime.bloodmagic.util.helper.NumeralHelper;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.function.Function;

public class Elements {

    public static void createHUDElements() {
        new HUDElementHolding();
        new HUDElementDemonWillAura();
        // Blood Altar with Divination Sigil
        new HUDElementCornerTile.DivinedView<TileAltar>(TileAltar.class, true) {
            @Override
            protected void addInformation(List<Pair<Sprite, Function<TileAltar, String>>> information) {
                // Current tier
                information.add(Pair.of(
                        new Sprite(new ResourceLocation(BloodMagic.MODID, "textures/gui/widgets.png"), 0, 46, 16, 16),
                        altar -> NumeralHelper.toRoman(altar.getTier().toInt())
                ));
                // Stored/Capacity
                information.add(Pair.of(
                        new Sprite(new ResourceLocation(BloodMagic.MODID, "textures/gui/widgets.png"), 16, 46, 16, 16),
                        altar -> String.format("%d/%d", altar.getCurrentBlood(), altar.getCapacity())
                ));
            }
        };
        // Blood Altar with Seers Sigil
        new HUDElementCornerTile.DivinedView<TileAltar>(TileAltar.class, false) {
            @Override
            protected void addInformation(List<Pair<Sprite, Function<TileAltar, String>>> information) {
                // Current tier
                information.add(Pair.of(
                        new Sprite(new ResourceLocation(BloodMagic.MODID, "textures/gui/widgets.png"), 0, 46, 16, 16),
                        altar -> NumeralHelper.toRoman(altar.getTier().toInt())
                ));
                // Stored/Capacity
                information.add(Pair.of(
                        new Sprite(new ResourceLocation(BloodMagic.MODID, "textures/gui/widgets.png"), 16, 46, 16, 16),
                        altar -> String.format("%d/%d", altar.getCurrentBlood(), altar.getCapacity())
                ));
                // Crafting progress/Crafting requirement
                information.add(Pair.of(
                        new Sprite(new ResourceLocation(BloodMagic.MODID, "textures/gui/widgets.png"), 32, 46, 16, 16),
                        altar -> {
                            if (!altar.isActive())
                                return "Inactive"; // FIXME localize
                            int progress = altar.getProgress();
                            int totalLiquidRequired = altar.getLiquidRequired() * altar.getStackInSlot(0).getCount();
                            return String.format("%d/%d", progress, totalLiquidRequired);
                        }
                ));
                // Consumption rate
                information.add(Pair.of(
                        new Sprite(new ResourceLocation(BloodMagic.MODID, "textures/gui/widgets.png"), 48, 46, 16, 16),
                        altar -> String.valueOf((int) (altar.getConsumptionRate() * (altar.getConsumptionMultiplier() + 1)))
                ));
                // Total charge
                information.add(Pair.of(
                        new Sprite(new ResourceLocation(BloodMagic.MODID, "textures/gui/widgets.png"), 64, 46, 16, 16),
                        altar -> String.valueOf(altar.getTotalCharge())
                ));
            }
        };
        // Incense Altar
        new HUDElementCornerTile.DivinedView<TileIncenseAltar>(TileIncenseAltar.class, true) {
            @Override
            protected void addInformation(List<Pair<Sprite, Function<TileIncenseAltar, String>>> information) {
                // Current tranquility
                information.add(Pair.of(
                        new Sprite(new ResourceLocation(BloodMagic.MODID, "textures/gui/widgets.png"), 80, 46, 16, 16),
                        incense -> String.valueOf((int) ((100D * (int) (100 * incense.tranquility)) / 100D))
                ));
                // Sacrifice bonus
                information.add(Pair.of(
                        new Sprite(new ResourceLocation(BloodMagic.MODID, "textures/gui/widgets.png"), 96, 46, 16, 16),
                        incense -> String.valueOf((int) (100 * incense.incenseAddition))
                ));
            }
        };
        // Inversion Pillar
        new HUDElementCornerTile.DivinedView<TileInversionPillar>(TileInversionPillar.class, true) {
            @Override
            protected void addInformation(List<Pair<Sprite, Function<TileInversionPillar, String>>> information) {
                // Current inversion
                information.add(Pair.of(
                        new Sprite(new ResourceLocation(BloodMagic.MODID, "textures/gui/widgets.png"), 112, 46, 16, 16),
                        pillar -> String.valueOf(((int) (10 * pillar.getCurrentInversion())) / 10D)
                ));
            }
        };
    }
}
