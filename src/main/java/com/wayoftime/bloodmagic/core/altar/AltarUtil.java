package com.wayoftime.bloodmagic.core.altar;

import com.google.common.collect.Maps;
import com.wayoftime.bloodmagic.api.impl.BloodMagicAPI;
import com.wayoftime.bloodmagic.block.BlockBloodRune;
import com.wayoftime.bloodmagic.core.RegistrarBloodMagic;
import com.wayoftime.bloodmagic.core.RegistrarBloodMagicBlocks;
import com.wayoftime.bloodmagic.core.type.ComponentType;
import com.wayoftime.bloodmagic.core.util.BooleanResult;
import com.wayoftime.bloodmagic.event.SacrificeEvent;
import com.wayoftime.bloodmagic.tile.TileBloodAltar;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.EnumMap;
import java.util.List;

public class AltarUtil {

    private static final EnumMap<ComponentType, IBlockState> COMPONENT_DEFAULT_STATES;
    static {
        COMPONENT_DEFAULT_STATES = Maps.newEnumMap(ComponentType.class);
        COMPONENT_DEFAULT_STATES.put(ComponentType.GLOWSTONE, Blocks.GLOWSTONE.getDefaultState());
        COMPONENT_DEFAULT_STATES.put(ComponentType.BLOODSTONE, RegistrarBloodMagicBlocks.BLOODSTONE_BRICK.getDefaultState());
        COMPONENT_DEFAULT_STATES.put(ComponentType.BEACON, Blocks.BEACON.getDefaultState());
        COMPONENT_DEFAULT_STATES.put(ComponentType.BLOOD_RUNE, RegistrarBloodMagicBlocks.BLOOD_RUNE_BLANK.getDefaultState());
        COMPONENT_DEFAULT_STATES.put(ComponentType.CRYSTAL, Blocks.BEDROCK.getDefaultState()); // FIXME - Crystal
        COMPONENT_DEFAULT_STATES.put(ComponentType.NOT_AIR, Blocks.STONEBRICK.getDefaultState());
    }

    public static BooleanResult<Integer> handleSacrifice(EntityLivingBase living, int baseAmount) {
        TileBloodAltar altar = findNearestAltar(living.getEntityWorld(), new BlockPos(living), 2);
        if (altar == null)
            return new BooleanResult<>(0, false);

        boolean isPlayer = living instanceof EntityPlayer;
        AltarUpgrades upgrades = altar.getUpgrades();
        int modifiedAmount = (int) (baseAmount * (1 + (isPlayer ? upgrades.getSelfSacrificeModifier() : upgrades.getSacrificeModifier())));

        SacrificeEvent event = isPlayer ? new SacrificeEvent.SelfSacrifice(living, baseAmount, modifiedAmount) : new SacrificeEvent(living, baseAmount, modifiedAmount);
        MinecraftForge.EVENT_BUS.post(event);
        modifiedAmount = event.getModifiedAmount();

        int filled = altar.getTank().fill(new FluidStack(RegistrarBloodMagic.FLUID_LIFE_ESSENCE, modifiedAmount), true);
        if (filled != 0)
            altar.markDirty();

        return new BooleanResult<>(filled, true);
    }

    public static BooleanResult<Integer> handleSacrifice(EntityLivingBase living) {
        boolean isPlayer = living instanceof EntityPlayer;
        int baseAmount = isPlayer ? 200 : BloodMagicAPI.INSTANCE.getValueManager().getSacrificial().getOrDefault(EntityList.getKey(living), 200);
        return handleSacrifice(living, baseAmount);
    }

    @Nonnull
    public static AltarTier getTier(World world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);
        if (!(tile instanceof TileBloodAltar))
            return AltarTier.ONE;

        AltarTier lastCheck = AltarTier.ONE;
        for (AltarTier tier : AltarTier.VALUES) {
            for (AltarComponent component : tier.getComponents()) {
                List<IBlockState> validStates = BloodMagicAPI.INSTANCE.getComponentStates(component.getType());
                IBlockState worldState = world.getBlockState(pos.add(component.getOffset()));
                if (component.getType() == ComponentType.NOT_AIR && worldState.getMaterial() != Material.AIR)
                    continue;

                if (!validStates.contains(worldState))
                    return lastCheck;
            }

            lastCheck = tier;
        }

        return lastCheck;
    }

    @Nonnull
    public static AltarUpgrades getUpgrades(World world, BlockPos pos, AltarTier currentTier) {
        AltarUpgrades upgrades = new AltarUpgrades();

        for (AltarTier tier : AltarTier.VALUES) {
            if (tier.ordinal() > currentTier.ordinal())
                break;

            for (AltarComponent component : tier.getComponents()) {
                if (!component.isUpgradeSlot() || component.getType() != ComponentType.BLOOD_RUNE)
                    continue;

                IBlockState state = world.getBlockState(pos.add(component.getOffset()));
                if (state.getBlock() instanceof BlockBloodRune)
                    upgrades.upgrade(((BlockBloodRune) state.getBlock()).getRune());
            }
        }

        return upgrades;
    }

    // FIXME - This does not search properly. It may provide an altar that isn't the closest
    @Nullable
    public static TileBloodAltar findNearestAltar(World world, BlockPos pos, int searchRadius) {
        BlockPos.MutableBlockPos offsetPos = new BlockPos.MutableBlockPos(pos);
        for (int x = -searchRadius; x < searchRadius; x++) {
            for (int y = -searchRadius; y < searchRadius; y++) {
                for (int z = -searchRadius; z < searchRadius; z++) {
                    TileEntity tile = world.getTileEntity(offsetPos.setPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z));
                    if (tile instanceof TileBloodAltar)
                        return (TileBloodAltar) tile;
                }
            }
        }

        return null;
    }

    public static void constructAltar(World world, BlockPos altarPos, AltarTier buildTo) {
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
        for (AltarTier tier : AltarTier.VALUES) {
            if (tier.ordinal() > buildTo.ordinal())
                return;

            for (AltarComponent component : tier.getComponents()) {
                mutablePos.setPos(altarPos.getX() + component.getOffset().getX(), altarPos.getY() + component.getOffset().getY(), altarPos.getZ() + component.getOffset().getZ());
                world.setBlockState(mutablePos, COMPONENT_DEFAULT_STATES.get(component.getType()));
            }
        }
    }

    public static void destroyAltar(World world, BlockPos altarPos) {
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
        for (AltarTier tier : AltarTier.VALUES) {
            for (AltarComponent component : tier.getComponents()) {
                mutablePos.setPos(altarPos.getX() + component.getOffset().getX(), altarPos.getY() + component.getOffset().getY(), altarPos.getZ() + component.getOffset().getZ());
                world.setBlockToAir(mutablePos);
            }
        }
    }
}
