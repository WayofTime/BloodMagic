package wayoftime.bloodmagic.util.helper;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.event.level.BlockEvent;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Helper class for safe block operations that respect protection mods like FTB Chunks.
 * <p>
 * Protection mods listen to BlockEvent.BreakEvent and BlockEvent.EntityPlaceEvent to
 * prevent unauthorized block modifications in claimed chunks. This helper fires these
 * events before performing block operations, allowing protection mods to cancel them.
 * <p>
 * Use these methods whenever Blood Magic needs to:
 * - Break or remove blocks (rituals, sigils, explosives)
 * - Place blocks (rituals, sigils, diviner)
 * - Modify blocks (changing block state, replacing fluids)
 */
public class BlockProtectionHelper {

    /**
     * Attempts to break a block, respecting protection events.
     * Fires BlockEvent.BreakEvent and returns false if cancelled.
     *
     * @param level  The level
     * @param pos    The block position to break
     * @param player The player responsible for the break (null for machine/ritual operations)
     * @return true if the block was successfully broken, false if cancelled or failed
     */
    public static boolean tryBreakBlock(Level level, BlockPos pos, @Nullable Player player) {
        if (level.isClientSide()) {
            return false;
        }

        BlockState state = level.getBlockState(pos);
        if (state.isAir()) {
            return true; // Already air, nothing to break
        }

        // Fire break event - protection mods can cancel this
        if (!fireBreakEvent(level, pos, state, player)) {
            return false;
        }

        // Perform the break
        return level.destroyBlock(pos, true, player);
    }

    /**
     * Attempts to break a block without dropping items, respecting protection events.
     *
     * @param level  The level
     * @param pos    The block position to break
     * @param player The player responsible for the break
     * @return true if the block was successfully broken, false if cancelled or failed
     */
    public static boolean tryBreakBlockNoDrops(Level level, BlockPos pos, @Nullable Player player) {
        if (level.isClientSide()) {
            return false;
        }

        BlockState state = level.getBlockState(pos);
        if (state.isAir()) {
            return true;
        }

        if (!fireBreakEvent(level, pos, state, player)) {
            return false;
        }

        return level.destroyBlock(pos, false, player);
    }

    /**
     * Attempts to remove a block (set to air) without breaking animation/drops.
     *
     * @param level  The level
     * @param pos    The block position
     * @param player The player responsible
     * @return true if successful
     */
    public static boolean tryRemoveBlock(Level level, BlockPos pos, @Nullable Player player) {
        if (level.isClientSide()) {
            return false;
        }

        BlockState state = level.getBlockState(pos);
        if (state.isAir()) {
            return true;
        }

        if (!fireBreakEvent(level, pos, state, player)) {
            return false;
        }

        return level.removeBlock(pos, false);
    }

    /**
     * Attempts to place a block, respecting protection events.
     * Fires BlockEvent.EntityPlaceEvent and returns false if cancelled.
     *
     * @param level    The level
     * @param pos      The block position
     * @param newState The block state to place
     * @param player   The player responsible (null for machine/ritual operations)
     * @return true if the block was successfully placed, false if cancelled
     */
    public static boolean tryPlaceBlock(Level level, BlockPos pos, BlockState newState, @Nullable Player player) {
        return tryPlaceBlock(level, pos, newState, player, Block.UPDATE_ALL);
    }

    /**
     * Attempts to place a block with custom update flags, respecting protection events.
     *
     * @param level       The level
     * @param pos         The block position
     * @param newState    The block state to place
     * @param player      The player responsible
     * @param updateFlags Block update flags
     * @return true if the block was successfully placed, false if cancelled
     */
    public static boolean tryPlaceBlock(Level level, BlockPos pos, BlockState newState, @Nullable Player player, int updateFlags) {
        if (level.isClientSide()) {
            return false;
        }

        BlockState oldState = level.getBlockState(pos);

        // Fire place event - protection mods can cancel this
        if (!firePlaceEvent(level, pos, oldState, newState, player)) {
            return false;
        }

        return level.setBlock(pos, newState, updateFlags);
    }

    /**
     * Attempts to replace a block (break old, place new), respecting protection events.
     * This is useful for operations like fluid replacement or block transformation.
     *
     * @param level    The level
     * @param pos      The block position
     * @param newState The new block state
     * @param player   The player responsible
     * @return true if successful
     */
    public static boolean tryReplaceBlock(Level level, BlockPos pos, BlockState newState, @Nullable Player player) {
        if (level.isClientSide()) {
            return false;
        }

        BlockState oldState = level.getBlockState(pos);

        // For replacement, we need to check both break and place permissions
        if (!oldState.isAir()) {
            if (!fireBreakEvent(level, pos, oldState, player)) {
                return false;
            }
        }

        if (!firePlaceEvent(level, pos, oldState, newState, player)) {
            return false;
        }

        return level.setBlock(pos, newState, Block.UPDATE_ALL);
    }

    /**
     * Fires a BlockEvent.BreakEvent and returns whether it was allowed (not cancelled).
     *
     * @param level  The level
     * @param pos    The block position
     * @param state  The current block state
     * @param player The player (can be null for non-player breaks)
     * @return true if the event was not cancelled
     */
    public static boolean fireBreakEvent(Level level, BlockPos pos, BlockState state, @Nullable Player player) {
        if (level.isClientSide() || !(level instanceof ServerLevel serverLevel)) {
            return true;
        }

        // If no player context, we can't fire the event properly
        // Some mods may still block based on chunk claims, but we can't simulate a player
        if (player == null) {
            // For non-player operations (rituals/machines), we still want protection
            // Use a fake player approach or just allow it
            // Most protection mods require a player to check permissions
            return true;
        }

        BlockEvent.BreakEvent event = new BlockEvent.BreakEvent(serverLevel, pos, state, player);
        MinecraftForge.EVENT_BUS.post(event);
        return !event.isCanceled();
    }

    /**
     * Fires a BlockEvent.EntityPlaceEvent and returns whether it was allowed (not cancelled).
     *
     * @param level    The level
     * @param pos      The block position
     * @param oldState The previous block state (before placement)
     * @param newState The new block state being placed
     * @param player   The player (can be null)
     * @return true if the event was not cancelled
     */
    public static boolean firePlaceEvent(Level level, BlockPos pos, BlockState oldState, BlockState newState, @Nullable Player player) {
        if (level.isClientSide() || !(level instanceof ServerLevel serverLevel)) {
            return true;
        }

        if (player == null) {
            return true;
        }

        // Create a snapshot of the old state before placement
        BlockSnapshot snapshot = BlockSnapshot.create(serverLevel.dimension(), serverLevel, pos);

        // Fire the place event
        BlockEvent.EntityPlaceEvent event = new BlockEvent.EntityPlaceEvent(snapshot, oldState, player);
        MinecraftForge.EVENT_BUS.post(event);
        return !event.isCanceled();
    }

    /**
     * Checks if a player can break a block at the given position without actually breaking it.
     * Useful for preview/validation before performing an operation.
     *
     * @param level  The level
     * @param pos    The block position
     * @param player The player
     * @return true if breaking is allowed
     */
    public static boolean canBreakBlock(Level level, BlockPos pos, @Nullable Player player) {
        if (level.isClientSide()) {
            return true; // Client-side always returns true, server will validate
        }

        BlockState state = level.getBlockState(pos);
        return fireBreakEvent(level, pos, state, player);
    }

    /**
     * Checks if a player can place a block at the given position without actually placing it.
     *
     * @param level    The level
     * @param pos      The block position
     * @param newState The block state to place
     * @param player   The player
     * @return true if placing is allowed
     */
    public static boolean canPlaceBlock(Level level, BlockPos pos, BlockState newState, @Nullable Player player) {
        if (level.isClientSide()) {
            return true;
        }

        BlockState oldState = level.getBlockState(pos);
        return firePlaceEvent(level, pos, oldState, newState, player);
    }

    /**
     * Helper to get a block's drops before breaking it.
     * This can be useful when you need to capture drops but want to check protection first.
     *
     * @param level  The server level
     * @param pos    The block position
     * @param player The player
     * @param tool   The tool being used to break (affects drops)
     * @return The drops, or empty list if break was denied
     */
    public static List<ItemStack> getDropsIfBreakAllowed(ServerLevel level, BlockPos pos, @Nullable Player player, ItemStack tool) {
        BlockState state = level.getBlockState(pos);

        if (!fireBreakEvent(level, pos, state, player)) {
            return Collections.emptyList();
        }

        BlockEntity be = level.getBlockEntity(pos);
        return Block.getDrops(state, level, pos, be, player, tool);
    }

    // ==================== UUID-based methods for rituals/machines ====================

    /**
     * Gets a player from their UUID if they are online.
     *
     * @param level     The level
     * @param ownerUUID The player's UUID
     * @return The player if online, null otherwise
     */
    @Nullable
    public static Player getPlayerFromUUID(Level level, @Nullable UUID ownerUUID) {
        if (ownerUUID == null || !(level instanceof ServerLevel serverLevel)) {
            return null;
        }
        return serverLevel.getServer().getPlayerList().getPlayer(ownerUUID);
    }

    /**
     * Attempts to break a block using owner UUID for protection checks.
     * If the owner is offline, the operation proceeds without protection checks.
     *
     * @param level     The level
     * @param pos       The block position
     * @param ownerUUID The UUID of the owner (e.g., ritual owner)
     * @return true if successful
     */
    public static boolean tryBreakBlock(Level level, BlockPos pos, @Nullable UUID ownerUUID) {
        return tryBreakBlock(level, pos, getPlayerFromUUID(level, ownerUUID));
    }

    /**
     * Attempts to break a block without drops, using owner UUID.
     *
     * @param level     The level
     * @param pos       The block position
     * @param ownerUUID The UUID of the owner
     * @return true if successful
     */
    public static boolean tryBreakBlockNoDrops(Level level, BlockPos pos, @Nullable UUID ownerUUID) {
        return tryBreakBlockNoDrops(level, pos, getPlayerFromUUID(level, ownerUUID));
    }

    /**
     * Attempts to remove a block, using owner UUID.
     *
     * @param level     The level
     * @param pos       The block position
     * @param ownerUUID The UUID of the owner
     * @return true if successful
     */
    public static boolean tryRemoveBlock(Level level, BlockPos pos, @Nullable UUID ownerUUID) {
        return tryRemoveBlock(level, pos, getPlayerFromUUID(level, ownerUUID));
    }

    /**
     * Attempts to place a block, using owner UUID.
     *
     * @param level     The level
     * @param pos       The block position
     * @param newState  The block state to place
     * @param ownerUUID The UUID of the owner
     * @return true if successful
     */
    public static boolean tryPlaceBlock(Level level, BlockPos pos, BlockState newState, @Nullable UUID ownerUUID) {
        return tryPlaceBlock(level, pos, newState, getPlayerFromUUID(level, ownerUUID));
    }

    /**
     * Attempts to replace a block, using owner UUID.
     *
     * @param level     The level
     * @param pos       The block position
     * @param newState  The new block state
     * @param ownerUUID The UUID of the owner
     * @return true if successful
     */
    public static boolean tryReplaceBlock(Level level, BlockPos pos, BlockState newState, @Nullable UUID ownerUUID) {
        return tryReplaceBlock(level, pos, newState, getPlayerFromUUID(level, ownerUUID));
    }

    /**
     * Checks if an owner can break a block at the given position.
     * If owner is offline, defaults to allowing the operation.
     * Use {@link #canBreakBlockStrict} if you want to deny when owner is offline.
     *
     * @param level     The level
     * @param pos       The block position
     * @param ownerUUID The owner's UUID
     * @return true if allowed (or owner is offline, which defaults to allowing)
     */
    public static boolean canBreakBlock(Level level, BlockPos pos, @Nullable UUID ownerUUID) {
        return canBreakBlock(level, pos, getPlayerFromUUID(level, ownerUUID));
    }

    /**
     * Checks if an owner can break a block at the given position (strict mode).
     * Returns false if the owner UUID is null or the owner is offline.
     * Use this for operations that should only work when the owner is online.
     *
     * @param level     The level
     * @param pos       The block position
     * @param ownerUUID The owner's UUID
     * @return true if allowed and owner is online, false otherwise
     */
    public static boolean canBreakBlockStrict(Level level, BlockPos pos, @Nullable UUID ownerUUID) {
        if (ownerUUID == null) {
            return false;
        }
        Player player = getPlayerFromUUID(level, ownerUUID);
        if (player == null) {
            return false; // Owner offline - deny operation
        }
        return canBreakBlock(level, pos, player);
    }

    /**
     * Checks if an owner can place a block at the given position.
     *
     * @param level     The level
     * @param pos       The block position
     * @param newState  The block state to place
     * @param ownerUUID The owner's UUID
     * @return true if allowed
     */
    public static boolean canPlaceBlock(Level level, BlockPos pos, BlockState newState, @Nullable UUID ownerUUID) {
        return canPlaceBlock(level, pos, newState, getPlayerFromUUID(level, ownerUUID));
    }

    // ==================== Entity-based methods for projectiles ====================

    /**
     * Gets a player from an entity - either directly if the entity is a player,
     * or from projectile owner if applicable.
     *
     * @param entity The entity
     * @return The player, or null if not a player
     */
    @Nullable
    public static Player getPlayerFromEntity(@Nullable Entity entity) {
        if (entity instanceof Player player) {
            return player;
        }
        return null;
    }

    /**
     * Attempts to place a block, using entity for protection checks.
     * If the entity is a player, uses that player for protection.
     * Otherwise, allows the operation (no protection check).
     *
     * @param level    The level
     * @param pos      The block position
     * @param newState The block state to place
     * @param entity   The entity responsible (player or projectile owner)
     * @return true if successful
     */
    public static boolean tryPlaceBlock(Level level, BlockPos pos, BlockState newState, @Nullable Entity entity) {
        return tryPlaceBlock(level, pos, newState, getPlayerFromEntity(entity));
    }

    /**
     * Attempts to remove a block, using entity for protection checks.
     *
     * @param level  The level
     * @param pos    The block position
     * @param entity The entity responsible
     * @return true if successful
     */
    public static boolean tryRemoveBlock(Level level, BlockPos pos, @Nullable Entity entity) {
        return tryRemoveBlock(level, pos, getPlayerFromEntity(entity));
    }

    /**
     * Checks if an entity can place a block at the given position.
     *
     * @param level  The level
     * @param pos    The block position
     * @param entity The entity
     * @return true if allowed
     */
    public static boolean canPlaceBlock(Level level, BlockPos pos, @Nullable Entity entity) {
        if (level.isClientSide()) {
            return true;
        }
        BlockState oldState = level.getBlockState(pos);
        return firePlaceEvent(level, pos, oldState, oldState, getPlayerFromEntity(entity));
    }

    // ==================== Convenience methods without BlockState ====================

    /**
     * Checks if a player can place a block at the given position (without specifying state).
     * Uses current state as both old and new state for event firing.
     *
     * @param level  The level
     * @param pos    The block position
     * @param player The player
     * @return true if allowed
     */
    public static boolean canPlaceBlock(Level level, BlockPos pos, @Nullable Player player) {
        if (level.isClientSide()) {
            return true;
        }
        BlockState oldState = level.getBlockState(pos);
        return firePlaceEvent(level, pos, oldState, oldState, player);
    }

    /**
     * Checks if an owner UUID can place a block at the given position (without specifying state).
     *
     * @param level     The level
     * @param pos       The block position
     * @param ownerUUID The owner's UUID
     * @return true if allowed
     */
    public static boolean canPlaceBlock(Level level, BlockPos pos, @Nullable UUID ownerUUID) {
        return canPlaceBlock(level, pos, getPlayerFromUUID(level, ownerUUID));
    }
}
