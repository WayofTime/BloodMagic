package wayoftime.bloodmagic.common.tile;

import com.sun.jna.platform.win32.WinDef;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.LevelData;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.block.BlockInversionPillarEnd;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.common.block.type.PillarCapType;
import wayoftime.bloodmagic.common.tile.base.TileBase;
import wayoftime.bloodmagic.structures.DungeonRoomLoader;
import wayoftime.bloodmagic.structures.DungeonRoomRegistry;
import wayoftime.bloodmagic.structures.DungeonSynthesizer;
import wayoftime.bloodmagic.structures.ModDungeons;
import wayoftime.bloodmagic.util.BMLog;
import wayoftime.bloodmagic.util.ChatUtil;
import wayoftime.bloodmagic.util.Constants;

import java.util.UUID;

public class TileInversionPillar extends TileBase implements CommandSource
{
	protected BlockPos teleportPos = BlockPos.ZERO;
	protected ResourceKey<Level> destinationKey;

	public TileInversionPillar(BlockEntityType<?> type, BlockPos pos, BlockState state)
	{
		super(type, pos, state);
	}

	public TileInversionPillar(BlockPos pos, BlockState state)
	{
		this(BloodMagicTileEntities.INVERSION_PILLAR_TYPE.get(), pos, state);
	}

	public void setDestination(Level destinationWorld, BlockPos destinationPos)
	{
        BMLog.DEFAULT.info("Pillar at {} set to {} in {}", getBlockPos(), teleportPos, destinationKey);
		this.destinationKey = destinationWorld.dimension();
		this.teleportPos = destinationPos;
	}

    public boolean hasDestination() {
        return teleportPos != BlockPos.ZERO;
    }

	@Override
	public void deserialize(CompoundTag tag)
	{
		super.deserialize(tag);

		CompoundTag positionTag = tag.getCompound(Constants.NBT.DUNGEON_TELEPORT_POS);
		teleportPos = new BlockPos(positionTag.getInt(Constants.NBT.X_COORD), positionTag.getInt(Constants.NBT.Y_COORD), positionTag.getInt(Constants.NBT.Z_COORD));

		if (tag.contains(Constants.NBT.DUNGEON_TELEPORT_KEY))
		{
			String key = tag.getString(Constants.NBT.DUNGEON_TELEPORT_KEY);
//			System.out.println("Deserialized key: " + key);
			destinationKey = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(key));
		}
	}

	@Override
	public CompoundTag serialize(CompoundTag tag)
	{
		super.serialize(tag);

		CompoundTag positionTag = new CompoundTag();
		positionTag.putInt(Constants.NBT.X_COORD, teleportPos.getX());
		positionTag.putInt(Constants.NBT.Y_COORD, teleportPos.getY());
		positionTag.putInt(Constants.NBT.Z_COORD, teleportPos.getZ());
		tag.put(Constants.NBT.DUNGEON_TELEPORT_POS, positionTag);

		if (destinationKey != null)
			tag.putString(Constants.NBT.DUNGEON_TELEPORT_KEY, destinationKey.location().toString());

		return tag;
	}

	public void handlePlayerInteraction(ServerPlayer player)
	{
        BlockPos pillarPos = this.getBlockPos();
        BlockPos controllerPos = pillarPos.above(2);
        // this could probably be cheated relatively easily but I dont really know how to do it better. Even adding more blocks doesnt prevent a sufficiently motivated person from just moving it.
        // potentially solvable by making seals not drop anything and making them immovable by pistons
        boolean isEndless = level.getBlockState(pillarPos.below().north(10)).is(BloodMagicBlocks.DUNGEON_SEAL.get());

        // no controller block above pillar? probably used to be the ritual side so skipping check
        if (level.getBlockState(controllerPos).is(BloodMagicBlocks.DUNGEON_CONTROLLER.get())) {
            boolean regen = false;
            if (level.getBlockEntity(controllerPos) instanceof TileDungeonController controller) {
                if (controller.dungeon == null // not like it should ever be null. like once it saves it fills this if it is null. but BCL doesnt care I guess...
                        || controller.dungeon.availableDoorMasterMap.isEmpty()) {
                    BMLog.DEFAULT.warn("Trouble generating dungeon detected, attempting to fix");
                    regen = true;
                }
            }

            if (regen) {
                DungeonSynthesizer dungeon = new DungeonSynthesizer();
                dungeon.generateInitialRoom(BloodMagic.rl(isEndless ? "room_pools/entrances/standard_dungeon_entrances" : "room_pools/entrances/mini_dungeon_entrances"), level.random, (ServerLevel) level, controllerPos);

                // dungeon nbt file has regular pillars here, fix that
                level.setBlockAndUpdate(pillarPos, BloodMagicBlocks.INVERSION_PILLAR.get().defaultBlockState());
                level.setBlockAndUpdate(pillarPos.relative(Direction.DOWN), BloodMagicBlocks.INVERSION_PILLAR_CAP.get().defaultBlockState().setValue(BlockInversionPillarEnd.TYPE, PillarCapType.BOTTOM));
                level.setBlockAndUpdate(pillarPos.relative(Direction.UP), BloodMagicBlocks.INVERSION_PILLAR_CAP.get().defaultBlockState().setValue(BlockInversionPillarEnd.TYPE, PillarCapType.TOP));

                if (level.getBlockEntity(pillarPos) instanceof TileInversionPillar tilePillar && player.getPersistentData().contains(Constants.NBT.DUNGEON_EXIT)) {
                    CompoundTag exit = player.getPersistentData().getCompound(Constants.NBT.DUNGEON_EXIT);
                    BlockPos exitPos = new BlockPos(exit.getInt("xCoord"), exit.getInt("yCoord"), exit.getInt("zCoord"));
                    ResourceKey<Level> exitDim = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(exit.getString("dimension_key")));
                    tilePillar.setDestination(level.getServer().getLevel(exitDim), exitPos);
                    tilePillar.setChanged();
                    ChatUtil.sendNoSpam(player, Component.translatable("chat.bloodmagic.dungeon.position_fix", tilePillar.teleportPos, tilePillar.destinationKey).withStyle(ChatFormatting.LIGHT_PURPLE));
                }

                ChatUtil.sendNoSpam(player, Component.translatable("chat.bloodmagic.dungeon.controller_fix").withStyle(ChatFormatting.DARK_GREEN));
                return;
            }
        }

		if (teleportPos.equals(BlockPos.ZERO))
		{
            CompoundTag exit = player.getPersistentData().getCompound(Constants.NBT.DUNGEON_EXIT);
            this.teleportPos = new BlockPos(exit.getInt("xCoord"), exit.getInt("yCoord"), exit.getInt("zCoord"));
            this.destinationKey = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(exit.getString("dimension_key")));
            this.setChanged();
            ChatUtil.sendNoSpam(player, Component.translatable("chat.bloodmagic.dungeon.position_fix", teleportPos, destinationKey).withStyle(ChatFormatting.DARK_PURPLE));
			return;
		}

		teleportPlayerToLocation((ServerLevel) level, player, destinationKey, teleportPos);
	}

	public CommandSourceStack getCommandSource(ServerLevel world)
	{
		return new CommandSourceStack(this, new Vec3(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ()), Vec2.ZERO, world, 2, "Inversion Pillar", Component.literal("Inversion Pillar"), world.getServer(), (Entity) null);
	}

	public void teleportPlayerToLocation(ServerLevel serverWorld, Player player, ResourceKey<Level> destination, BlockPos destinationPos)
	{
//		System.out.println("Key: " + destination.getLocation());
//		String command = "execute in bloodmagic:dungeon run teleport Dev 0 100 0";
		String command = getTextCommandForTeleport(destination, player, destinationPos.getX() + 0.5, destinationPos.getY(), destinationPos.getZ() + 0.5);
		MinecraftServer mcServer = serverWorld.getServer();
		mcServer.getCommands().performPrefixedCommand(getCommandSource(serverWorld), command);
	}

	public String getTextCommandForTeleport(ResourceKey<Level> destination, Player player, double posX, double posY, double posZ)
	{
		String playerName = player.getName().getString();
//		System.out.println("Potential player name: " + playerName);
		return "execute in " + destination.location().toString() + " run teleport " + playerName + " " + posX + " " + posY + " " + posZ;
	}

	@Override
	public void sendSystemMessage(Component component) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean acceptsSuccess()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean acceptsFailure()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean shouldInformAdmins()
	{
		// TODO Auto-generated method stub
		return false;
	}
}
