package wayoftime.bloodmagic.common.block;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.SimpleFluidContent;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.Nullable;
import wayoftime.bloodmagic.common.blockentity.BloodTankTile;
import wayoftime.bloodmagic.common.datacomponent.BMDataComponents;
import wayoftime.bloodmagic.util.helper.BlockEntityHelper;

import java.util.List;

public class BloodTankBlock extends Block implements EntityBlock {
    protected static final VoxelShape BOX = Block.box(3, 0, 3, 13, 14, 13);

    public BloodTankBlock() {
        super(BlockBehaviour.Properties.of()
                .requiresCorrectToolForDrops()
                .strength(2.0F, 5.0F)
                .sound(SoundType.GLASS)
                .forceSolidOn() // This prevents fluids from wiping it away
        );
    }

    @Override
    public boolean hasDynamicLightEmission(BlockState state) {
        return true;
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        BlockEntity entity = level.getBlockEntity(pos);
        if (entity instanceof BloodTankTile tank) {
            return tank.getFluidContained().getFluidType().getLightLevel();
        }

        return 0;
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> components, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, components, tooltipFlag);

        int tier = stack.getOrDefault(BMDataComponents.CONTAINER_TIER, 0);
        if (tier == 0) {
            components.add(Component.translatable("tooltip.bloodmagic.container_tier_missing").withStyle(ChatFormatting.RED, ChatFormatting.BOLD));
        } else {
            components.add(BlockEntityHelper.translatableHover("tooltip.bloodmagic.container_tier", tier));
        }

        FluidStack fluidStack = stack.getOrDefault(BMDataComponents.FLUID_CONTENT, SimpleFluidContent.EMPTY).copy();
        if (fluidStack.isEmpty()) {
            components.add(BlockEntityHelper.translatableHover("tooltip.bloodmagic.fluid_content_empty"));
        } else {
            components.add(BlockEntityHelper.translatableHover("tooltip.bloodmagic.fluid_content", fluidStack.getAmount(), fluidStack.getHoverName()));
        }
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return BOX;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        IFluidHandler tank = level.getCapability(Capabilities.FluidHandler.BLOCK, pos, null);
        if (tank == null) {
            return ItemInteractionResult.CONSUME;
        }
        if (!FluidUtil.interactWithFluidHandler(player, hand, tank)) {
            return ItemInteractionResult.CONSUME;
        }
        player.getInventory().setChanged();
        level.sendBlockUpdated(pos, state, state, UPDATE_ALL);
        return ItemInteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BloodTankTile(blockPos, blockState);
    }
}
