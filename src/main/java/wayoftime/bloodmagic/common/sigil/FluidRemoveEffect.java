package wayoftime.bloodmagic.common.sigil;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidActionResult;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import wayoftime.bloodmagic.api.sigil.SigilEffect;

public record FluidRemoveEffect(int removeAmount) implements SigilEffect {
    public static final MapCodec<FluidRemoveEffect> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            Codec.INT.fieldOf("remove_amount").forGetter(FluidRemoveEffect::removeAmount)
    ).apply(builder, FluidRemoveEffect::new));

    @Override
    public boolean useOnBlock(ItemStack sigil, Player player, UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        IFluidHandler handler = level.getCapability(Capabilities.FluidHandler.BLOCK, pos, context.getClickedFace());
        if (handler != null) {
            FluidStack drained = handler.drain(removeAmount, IFluidHandler.FluidAction.EXECUTE);
            if (drained.getAmount() > 0) {
                return true;
            }
        }

        FluidActionResult result = FluidUtil.tryPickUpFluid(Items.BUCKET.getDefaultInstance(), player, level, pos.relative(context.getClickedFace()), context.getClickedFace().getOpposite());
        return result.isSuccess();
    }

    @Override
    public boolean useOnAir(ItemStack sigil, Player player, InteractionHand usedHand) {
        BlockHitResult lookResult = Item.getPlayerPOVHitResult(player.level(), player, ClipContext.Fluid.SOURCE_ONLY);
        if (lookResult.getType() != HitResult.Type.BLOCK) {
            return false;
        }

        FluidActionResult fluidResult = FluidUtil.tryPickUpFluid(Items.BUCKET.getDefaultInstance(), player, player.level(), lookResult.getBlockPos(), lookResult.getDirection());
        return fluidResult.isSuccess();
    }

    @Override
    public MapCodec<? extends SigilEffect> codec() {
        return CODEC;
    }
}
