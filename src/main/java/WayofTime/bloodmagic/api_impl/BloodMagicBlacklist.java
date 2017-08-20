package WayofTime.bloodmagic.api_impl;

import WayofTime.bloodmagic.apiv2.IBloodMagicBlacklist;
import com.google.common.base.Preconditions;
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
        Preconditions.checkNotNull(state, "state cannot be null.");

        if (!teleposer.contains(state))
            teleposer.add(state);
    }

    @Override
    public void addTeleposer(@Nonnull Block block) {
        Preconditions.checkNotNull(block, "block cannot be null.");

        for (IBlockState state : block.getBlockState().getValidStates())
            addTeleposer(state);
    }

    @Override
    public void addTeleposer(@Nonnull ResourceLocation entityId) {
        Preconditions.checkNotNull(entityId, "entityId cannot be null.");

        if (!teleposerEntities.contains(entityId))
            teleposerEntities.add(entityId);
    }

    @Override
    public void addTransposition(@Nonnull IBlockState state) {
        Preconditions.checkNotNull(state, "state cannot be null.");

        if (!transposition.contains(state))
            transposition.add(state);
    }

    @Override
    public void addTransposition(@Nonnull Block block) {
        Preconditions.checkNotNull(block, "block cannot be null.");

        for (IBlockState state : block.getBlockState().getValidStates())
            addTransposition(state);
    }

    @Override
    public void addGreenGrove(@Nonnull IBlockState state) {
        Preconditions.checkNotNull(state, "state cannot be null.");

        if (!greenGrove.contains(state))
            greenGrove.add(state);
    }

    @Override
    public void addGreenGrove(@Nonnull Block block) {
        Preconditions.checkNotNull(block, "block cannot be null.");

        for (IBlockState state : block.getBlockState().getValidStates())
            addGreenGrove(state);
    }

    @Override
    public void addWellOfSuffering(@Nonnull ResourceLocation entityId) {
        Preconditions.checkNotNull(entityId, "entityId cannot be null.");

        if (!sacrifice.contains(entityId))
            sacrifice.add(entityId);
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
