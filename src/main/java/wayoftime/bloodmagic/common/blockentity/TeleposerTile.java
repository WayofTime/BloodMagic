package wayoftime.bloodmagic.common.blockentity;

import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.items.ItemStackHandler;
import wayoftime.bloodmagic.common.datacomponent.Binding;
import wayoftime.bloodmagic.common.datacomponent.BMDataComponents;
import wayoftime.bloodmagic.common.datacomponent.SoulNetwork;
import wayoftime.bloodmagic.common.item.ITeleposerFocus;
import wayoftime.bloodmagic.common.menu.TeleposerMenu;
import wayoftime.bloodmagic.common.tag.BMTags;
import wayoftime.bloodmagic.util.SoulTicket;
import wayoftime.bloodmagic.util.helper.SoulNetworkHelper;

import java.util.List;

public class TeleposerTile extends BaseTile implements MenuProvider, CommandSource {
    public static final int FOCUS_SLOT = 0;
    public static final int MAX_UNIT_COST = 1000;
    public static final int MAX_TOTAL_COST = 10000;

    private int previousInput = 0;

    public ItemStackHandler inv = new ItemStackHandler(1) {
        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return stack.getItem() instanceof ITeleposerFocus;
        }

        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            setChanged();
        }
    };

    public TeleposerTile(BlockPos pos, BlockState state) {
        super(BMTiles.TELEPOSER_TYPE.get(), pos, state);
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (this.level != null) {
            this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), Block.UPDATE_ALL);
        }
    }

    public void tick() {
        if (level == null || level.isClientSide) {
            return;
        }

        int currentInput = getLevel().getDirectSignalTo(worldPosition);

        if (previousInput == 0 && currentInput != 0) {
            previousInput = currentInput;
            initiateTeleport();
        } else {
            previousInput = currentInput;
        }
    }

    public void initiateTeleport() {
        if (!(level instanceof ServerLevel serverWorld)) {
            return;
        }

        if (!canTeleport()) {
            return;
        }

        ItemStack focusStack = inv.getStackInSlot(FOCUS_SLOT);
        if (!(focusStack.getItem() instanceof ITeleposerFocus focusItem)) {
            return;
        }

        Level linkedWorld = focusItem.getStoredWorld(focusStack, level);
        BlockPos linkedPos = focusItem.getStoredPos(focusStack);
        if (linkedWorld == null || linkedPos.equals(worldPosition)) {
            return;
        }

        if (!(linkedWorld.getBlockEntity(linkedPos) instanceof TeleposerTile)) {
            return;
        }

        AABB entityRangeOffsetBB = focusItem.getEntityRangeOffset(linkedWorld, getBlockPos());
        if (entityRangeOffsetBB == null) {
            return;
        }

        double transportCost = Math.min(0.5 * Math.sqrt(linkedPos.distSqr(worldPosition)), MAX_UNIT_COST);
        if (!linkedWorld.equals(level)) {
            transportCost = MAX_UNIT_COST;
        }

        AABB originalBB = entityRangeOffsetBB.move(getBlockPos());
        AABB focusBB = entityRangeOffsetBB.move(linkedPos);

        List<Entity> originalEntities = level.getEntitiesOfClass(Entity.class, originalBB);
        List<Entity> focusEntities = linkedWorld.getEntitiesOfClass(Entity.class, focusBB);

        List<BlockPos> offsetList = focusItem.getBlockListOffset(level);

        int uses = 0;
        int maxUses = offsetList.size() + originalEntities.size() + focusEntities.size();

        int maxDrain = Math.min((int) (transportCost * maxUses), MAX_TOTAL_COST);
        SoulNetwork network = getNetwork();
        if (network == null || network.getCurrentEssence() < maxDrain) {
            return;
        }

        ResourceKey<Level> linkedKey = linkedWorld.dimension();

        for (Entity entity : originalEntities) {
            if (entity.getType().is(BMTags.Entities.TELEPOSE_BLACKLIST)) {
                continue;
            }

            Vec3 newPosVec = entity.position().subtract(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ()).add(linkedPos.getX(), linkedPos.getY(), linkedPos.getZ());

            if (entity instanceof Player && !linkedWorld.equals(level)) {
                teleportPlayerToLocation(serverWorld, (Player) entity, linkedKey, newPosVec.x, newPosVec.y, newPosVec.z);
            } else {
                entity.teleportTo(newPosVec.x, newPosVec.y, newPosVec.z);
            }

            uses++;
        }

        for (Entity entity : focusEntities) {
            if (entity.getType().is(BMTags.Entities.TELEPOSE_BLACKLIST)) {
                continue;
            }

            Vec3 newPosVec = entity.position().add(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ()).subtract(linkedPos.getX(), linkedPos.getY(), linkedPos.getZ());

            if (entity instanceof Player && !linkedWorld.equals(level)) {
                teleportPlayerToLocation(serverWorld, (Player) entity, level.dimension(), newPosVec.x, newPosVec.y, newPosVec.z);
            } else {
                entity.teleportTo(newPosVec.x, newPosVec.y, newPosVec.z);
            }

            uses++;
        }

        level.playSound(null, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.BLOCKS, 1, 1);
        linkedWorld.playSound(null, linkedPos.getX(), linkedPos.getY(), linkedPos.getZ(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.BLOCKS, 1, 1);

        network.syphon(SoulTicket.block(level, worldPosition, Math.min((int) (uses * transportCost), MAX_TOTAL_COST)));
    }

    public boolean canTeleport() {
        return getNetwork() != null;
    }

    private SoulNetwork getNetwork() {
        ItemStack focusStack = this.inv.getStackInSlot(FOCUS_SLOT);
        if (!focusStack.isEmpty() && focusStack.getItem() instanceof ITeleposerFocus) {
            Binding binding = focusStack.get(BMDataComponents.BINDING);
            if (binding != null && !binding.isEmpty()) {
                return SoulNetworkHelper.getSoulNetwork(binding);
            }
        }
        return null;
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        inv.deserializeNBT(registries, tag.getCompound("inventory"));
        this.previousInput = tag.getInt("redstone");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("inventory", inv.serializeNBT(registries));
        tag.putInt("redstone", previousInput);
    }

    public void dropItems() {
        if (level != null && !level.isClientSide) {
            for (int i = 0; i < inv.getSlots(); i++) {
                ItemStack stack = inv.getStackInSlot(i);
                if (!stack.isEmpty()) {
                    Containers.dropItemStack(level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), stack);
                }
            }
        }
    }

    public boolean stillValid(Player player) {
        if (this.level.getBlockEntity(this.worldPosition) != this) {
            return false;
        }
        return player.distanceToSqr(this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 0.5, this.worldPosition.getZ() + 0.5) <= 64.0;
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new TeleposerMenu(containerId, playerInventory, this);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.bloodmagic.teleposer");
    }

    public CommandSourceStack getCommandSource(ServerLevel world) {
        return new CommandSourceStack(this, new Vec3(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ()), Vec2.ZERO, world, 2, "Teleposer", Component.literal("Teleposer"), world.getServer(), null);
    }

    public void teleportPlayerToLocation(ServerLevel serverWorld, Player player, ResourceKey<Level> destination, double x, double y, double z) {
        String command = getTextCommandForTeleport(destination, player, x, y, z);
        MinecraftServer mcServer = serverWorld.getServer();
        mcServer.getCommands().performPrefixedCommand(getCommandSource(serverWorld), command);
    }

    public String getTextCommandForTeleport(ResourceKey<Level> destination, Player player, double posX, double posY, double posZ) {
        String playerName = player.getName().getString();
        return "execute in " + destination.location().toString() + " run teleport " + playerName + " " + posX + " " + posY + " " + posZ;
    }

    @Override
    public void sendSystemMessage(Component component) {
    }

    @Override
    public boolean acceptsSuccess() {
        return false;
    }

    @Override
    public boolean acceptsFailure() {
        return false;
    }

    @Override
    public boolean shouldInformAdmins() {
        return false;
    }
}
