package wayoftime.bloodmagic.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import wayoftime.bloodmagic.api.BMIdentifiers;
import wayoftime.bloodmagic.common.datamap.BMDataMaps;
import wayoftime.bloodmagic.api.ritual.ImperfectRitualEffect;
import wayoftime.bloodmagic.api.helper.RitualUtil;
import wayoftime.bloodmagic.api.helper.SoulNetworkHelper;

import java.util.Optional;

public class ImperfectRitualBlock extends Block {
    public ImperfectRitualBlock() {
        super(
                BlockBehaviour.Properties.of()
                        .requiresCorrectToolForDrops()
                        .strength(2, 5)
                        .sound(SoundType.STONE)
        );
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        ResourceLocation type = level.getBlockState(pos.above()).getBlockHolder().getData(BMDataMaps.IMPERFECT_RITUAL_CATALYST);
        if (type == null) {
            return super.useWithoutItem(state, level, pos, player, hitResult);
        }
        Optional<ImperfectRitualEffect> optionalEffect = level.registryAccess().registryOrThrow(BMIdentifiers.RegistryKeys.IMPERFECT_RITUALS).getOptional(type);
        if (optionalEffect.isEmpty()) {
            return super.useWithoutItem(state, level, pos, player, hitResult);
        }

        ImperfectRitualEffect ritual = optionalEffect.get();
        if (!level.isClientSide) {
            RitualUtil.spawnLightning((ServerLevel) level, pos, true);
            SoulNetworkHelper.getSoulNetwork(player).hurtPlayer(player, ritual.getCost());
            ritual.perform(player, (ServerLevel) level, pos);
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
