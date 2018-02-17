package WayofTime.bloodmagic.api.impl;

import WayofTime.bloodmagic.api.IBloodMagicBlacklist;
import WayofTime.bloodmagic.util.BMLog;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.Set;

public class BloodMagicBlacklist implements IBloodMagicBlacklist {

    private final Set<IBlockState> teleposer;
    private final Set<ResourceLocation> teleposerEntities;
    private final Set<IBlockState> transposition;
    private final Set<IBlockState> greenGrove;
    private final Set<ResourceLocation> sacrifice;

    public BloodMagicBlacklist() {
        this.teleposer = Sets.newHashSet();
        this.teleposerEntities = Sets.newHashSet();
        this.transposition = Sets.newHashSet();
        this.greenGrove = Sets.newHashSet();
        this.sacrifice = Sets.newHashSet();
    }

    @Override
    public void addTeleposer(@Nonnull IBlockState state) {
        if (!teleposer.contains(state)) {
            BMLog.API_VERBOSE.info("Blacklist: Added {} to the Teleposer blacklist.", state);
            teleposer.add(state);
        }
    }

    public void addTeleposer(@Nonnull Block block) {
        for (IBlockState state : block.getBlockState().getValidStates())
            addTeleposer(state);
    }

    @Override
    public void addTeleposer(@Nonnull ResourceLocation entityId) {
        if (!teleposerEntities.contains(entityId)) {
            BMLog.API_VERBOSE.info("Blacklist: Added {} to the Teleposer blacklist.", entityId);
            teleposerEntities.add(entityId);
        }
    }

    @Override
    public void addTransposition(@Nonnull IBlockState state) {
        if (!transposition.contains(state)) {
            BMLog.API_VERBOSE.info("Blacklist: Added {} to the Transposition blacklist.", state);
            transposition.add(state);
        }
    }

    public void addTransposition(@Nonnull Block block) {
        for (IBlockState state : block.getBlockState().getValidStates())
            addTransposition(state);
    }

    @Override
    public void addGreenGrove(@Nonnull IBlockState state) {
        if (!greenGrove.contains(state)) {
            BMLog.API_VERBOSE.info("Blacklist: Added {} to the Green Grove blacklist.", state);
            greenGrove.add(state);
        }
    }

    public void addGreenGrove(@Nonnull Block block) {
        for (IBlockState state : block.getBlockState().getValidStates())
            addGreenGrove(state);
    }

    @Override
    public void addWellOfSuffering(@Nonnull ResourceLocation entityId) {
        if (!sacrifice.contains(entityId)) {
            BMLog.API_VERBOSE.info("Blacklist: Added {} to the Well of Suffering blacklist.", entityId);
            sacrifice.add(entityId);
        }
    }

    // Internal use getters

    public Set<IBlockState> getTeleposer() {
        return ImmutableSet.copyOf(teleposer);
    }

    public Set<ResourceLocation> getTeleposerEntities() {
        return ImmutableSet.copyOf(teleposerEntities);
    }

    public Set<IBlockState> getTransposition() {
        return ImmutableSet.copyOf(transposition);
    }

    public Set<IBlockState> getGreenGrove() {
        return ImmutableSet.copyOf(greenGrove);
    }

    public Set<ResourceLocation> getSacrifice() {
        return ImmutableSet.copyOf(sacrifice);
    }
}
