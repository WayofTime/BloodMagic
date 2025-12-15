package wayoftime.bloodmagic.common.sigil;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidActionResult;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import wayoftime.bloodmagic.api.sigil.SigilEffect;

public record FluidPlaceEffect(Holder<Fluid> fluid, int tankFillAmount) implements SigilEffect {
    public static final MapCodec<FluidPlaceEffect> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            BuiltInRegistries.FLUID.holderByNameCodec().fieldOf("fluid_type").forGetter(FluidPlaceEffect::fluid),
            Codec.INT.fieldOf("tank_fill_amount").forGetter(FluidPlaceEffect::tankFillAmount)
    ).apply(builder, FluidPlaceEffect::new));

    @Override
    public boolean useOnBlock(ItemStack sigil, Player player, UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        FluidStack toFill = new FluidStack(fluid, tankFillAmount);
        IFluidHandler handler = level.getCapability(Capabilities.FluidHandler.BLOCK, pos, context.getClickedFace());
        if (handler != null) {
            int amount = handler.fill(toFill, IFluidHandler.FluidAction.EXECUTE);
            if (amount > 0) {
                return true;
            }
        }

        FluidActionResult result = FluidUtil.tryPlaceFluid(player, level, context.getHand(), pos.relative(context.getClickedFace()), fluid.value().getBucket().getDefaultInstance(), new FluidStack(fluid, FluidType.BUCKET_VOLUME));
        return result.isSuccess();
    }

    @Override
    public MapCodec<? extends SigilEffect> codec() {
        return CODEC;
    }
}
