package wayoftime.bloodmagic.common.tile;

import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import wayoftime.bloodmagic.common.tile.base.TileBase;
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
		this.destinationKey = destinationWorld.dimension();
		this.teleportPos = destinationPos;
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

	public void handlePlayerInteraction(Player player)
	{
//		RegistryKey<World> key = RegistryKey.getOrCreateKey(Registry.WORLD_KEY, BloodMagic.rl("dungeon"));
		if (teleportPos.equals(BlockPos.ZERO))
		{
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
