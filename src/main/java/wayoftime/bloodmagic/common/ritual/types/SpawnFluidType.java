package wayoftime.bloodmagic.common.ritual.types;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidActionResult;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.FluidUtil;
import wayoftime.bloodmagic.api.datacomponent.Binding;
import wayoftime.bloodmagic.api.helper.SoulNetworkHelper;
import wayoftime.bloodmagic.api.ritual.Range;
import wayoftime.bloodmagic.api.ritual.Ritual;
import wayoftime.bloodmagic.api.soulnetwork.SoulTicket;
import wayoftime.bloodmagic.common.blockentity.MasterRitualTile;
import wayoftime.bloodmagic.common.datacomponent.SoulNetwork;
import wayoftime.bloodmagic.common.ritual.ranges.RectangleRange;

import java.util.HashMap;
import java.util.Map;

public record SpawnFluidType(Fluid fluid, int tankFillAmount, int essencePerBucket, int tickRate, int activationCost) implements Ritual {

    public static final String FLUID = "fluid";
    public static final String TANK = "tank";

    public static final MapCodec<SpawnFluidType> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            BuiltInRegistries.FLUID.byNameCodec().fieldOf("fluid_type").forGetter(SpawnFluidType::fluid),
            Codec.INT.fieldOf("tank_fill_amount").forGetter(SpawnFluidType::tankFillAmount),
            Codec.INT.fieldOf("essence_per_bucket").forGetter(SpawnFluidType::essencePerBucket),
            Codec.INT.fieldOf("tick_rate").forGetter(SpawnFluidType::tickRate),
            Codec.INT.fieldOf("activation_cost").forGetter(SpawnFluidType::getActivationCost)
    ).apply(builder, SpawnFluidType::new));

    @Override
    public int getRefreshTicks(MasterRitualTile mrs) {
        return tickRate;
    }

    @Override
    public int getActivationCost() {
        return activationCost;
    }

    @Override
    public boolean perform(MasterRitualTile mrs) {
        Binding owner = mrs.getBinding();
        SoulNetwork ownerNetwork = SoulNetworkHelper.getSoulNetwork(owner);
        Level level = mrs.getLevel();
        if (ownerNetwork.getCurrentEssence() >= essencePerBucket) {
            Range fluidRange = mrs.getConfig().ranges().get(FLUID);
            BlockPos targetPos = fluidRange.getFirst();
            if (!level.getFluidState(targetPos).is(fluid)) {
                FluidActionResult result = FluidUtil.tryPlaceFluid(null, level, InteractionHand.MAIN_HAND, targetPos, fluid.getBucket().getDefaultInstance(), new FluidStack(fluid, FluidType.BUCKET_VOLUME));
                if (result.isSuccess()) {
                    ownerNetwork.syphon(SoulTicket.block(level, mrs.getBlockPos(), essencePerBucket));
                }
            }
        }
        return true;
    }

    @Override
    public HashMap<String, Range> getDefaultRanges(MasterRitualTile mrs) {
        return new HashMap<>(Map.of(
                // position where fluid is placed
                FLUID, new RectangleRange(new BlockPos(0, 1, 0), new BlockPos(0, 1, 0), 1, 1),
                // TODO actually implement ranges
                // all surrounded positions will be attempted to fill with fluid if applicable
                TANK, new RectangleRange(new BlockPos(0, 0, 0), new BlockPos(0, 0, 0), 1, 1)
        ));
    }

    @Override
    public MapCodec<? extends Ritual> codec() {
        return CODEC;
    }
}
