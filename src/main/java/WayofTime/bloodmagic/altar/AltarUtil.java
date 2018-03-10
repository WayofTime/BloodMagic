package WayofTime.bloodmagic.altar;

import WayofTime.bloodmagic.api.impl.BloodMagicAPI;
import WayofTime.bloodmagic.block.BlockBloodRune;
import WayofTime.bloodmagic.tile.TileAltar;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class AltarUtil {

    @Nonnull
    public static AltarTier getTier(World world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);
        if (!(tile instanceof TileAltar))
            return AltarTier.ONE;

        AltarTier lastCheck = AltarTier.ONE;
        for (AltarTier tier : AltarTier.values()) {
            for (AltarComponent component : tier.getAltarComponents()) {
                BlockPos componentPos = pos.add(component.getOffset());
                IBlockState worldState = world.getBlockState(componentPos);

                if (worldState.getBlock() instanceof IAltarComponent)
                    if (((IAltarComponent) worldState.getBlock()).getType(world, worldState, componentPos) == component.getComponent())
                        continue;

                if (component.getComponent() == ComponentType.NOTAIR && worldState.getMaterial() != Material.AIR && !worldState.getMaterial().isLiquid())
                    continue;

                List<IBlockState> validStates = BloodMagicAPI.INSTANCE.getComponentStates(component.getComponent());
                if (!validStates.contains(worldState))
                    return lastCheck;
            }

            lastCheck = tier;
        }

        return lastCheck;
    }

    @Nonnull
    public static AltarUpgrade getUpgrades(World world, BlockPos pos, AltarTier currentTier) {
        AltarUpgrade upgrades = new AltarUpgrade();

        for (AltarTier tier : AltarTier.values()) {
            if (tier.ordinal() > currentTier.ordinal())
                return upgrades;

            for (AltarComponent component : tier.getAltarComponents()) {
                if (!component.isUpgradeSlot() || component.getComponent() != ComponentType.BLOODRUNE)
                    continue;

                BlockPos componentPos = pos.add(component.getOffset());
                IBlockState state = world.getBlockState(componentPos);
                if (state.getBlock() instanceof BlockBloodRune)
                    upgrades.upgrade(((BlockBloodRune) state.getBlock()).getBloodRune(world, componentPos, state));
            }
        }

        return upgrades;
    }

    @Nullable
    public static Pair<BlockPos, ComponentType> getFirstMissingComponent(World world, BlockPos pos, int altarTier) {
        if (altarTier >= AltarTier.MAXTIERS)
            return null;

        for (AltarTier tier : AltarTier.values()) {
            for (AltarComponent component : tier.getAltarComponents()) {
                BlockPos componentPos = pos.add(component.getOffset());
                IBlockState worldState = world.getBlockState(componentPos);
                if (component.getComponent() == ComponentType.NOTAIR && worldState.getMaterial() != Material.AIR && !worldState.getMaterial().isLiquid())
                    continue;

                List<IBlockState> validStates = BloodMagicAPI.INSTANCE.getComponentStates(component.getComponent());
                if (!validStates.contains(worldState))
                    return Pair.of(componentPos, component.getComponent());
            }
        }

        return null;
    }
}
