package wayoftime.bloodmagic.tile;

import java.util.UUID;

import net.minecraft.command.CommandSource;
import net.minecraft.command.ICommandSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.ObjectHolder;
import wayoftime.bloodmagic.tile.base.TileBase;
import wayoftime.bloodmagic.util.Constants;

public class TileInversionPillar extends TileBase implements ICommandSource
{
	@ObjectHolder("bloodmagic:inversion_pillar")
	public static TileEntityType<TileInversionPillar> TYPE;

	protected BlockPos teleportPos = BlockPos.ZERO;
	protected RegistryKey<World> destinationKey;

	public TileInversionPillar(TileEntityType<?> type)
	{
		super(type);
	}

	public TileInversionPillar()
	{
		this(TYPE);
	}

	public void setDestination(World destinationWorld, BlockPos destinationPos)
	{
		this.destinationKey = destinationWorld.getDimensionKey();
		this.teleportPos = destinationPos;
	}

	@Override
	public void deserialize(CompoundNBT tag)
	{
		super.deserialize(tag);

		CompoundNBT positionTag = tag.getCompound(Constants.NBT.DUNGEON_TELEPORT_POS);
		teleportPos = new BlockPos(positionTag.getInt(Constants.NBT.X_COORD), positionTag.getInt(Constants.NBT.Y_COORD), positionTag.getInt(Constants.NBT.Z_COORD));

		if (tag.contains(Constants.NBT.DUNGEON_TELEPORT_KEY))
		{
			String key = tag.getString(Constants.NBT.DUNGEON_TELEPORT_KEY);
//			System.out.println("Deserialized key: " + key);
			destinationKey = RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation(key));
		}
	}

	@Override
	public CompoundNBT serialize(CompoundNBT tag)
	{
		super.serialize(tag);

		CompoundNBT positionTag = new CompoundNBT();
		positionTag.putInt(Constants.NBT.X_COORD, teleportPos.getX());
		positionTag.putInt(Constants.NBT.Y_COORD, teleportPos.getY());
		positionTag.putInt(Constants.NBT.Z_COORD, teleportPos.getZ());
		tag.put(Constants.NBT.DUNGEON_TELEPORT_POS, positionTag);

		if (destinationKey != null)
			tag.putString(Constants.NBT.DUNGEON_TELEPORT_KEY, destinationKey.getLocation().toString());

		return tag;
	}

	public void handlePlayerInteraction(PlayerEntity player)
	{
//		RegistryKey<World> key = RegistryKey.getOrCreateKey(Registry.WORLD_KEY, BloodMagic.rl("dungeon"));
		if (teleportPos.equals(BlockPos.ZERO))
		{
			return;
		}

		teleportPlayerToLocation((ServerWorld) world, player, destinationKey, teleportPos);
	}

	public CommandSource getCommandSource(ServerWorld world)
	{
		return new CommandSource(this, new Vector3d(pos.getX(), pos.getY(), pos.getZ()), Vector2f.ZERO, world, 2, "Inversion Pillar", new StringTextComponent("Inversion Pillar"), world.getServer(), (Entity) null);
	}

	public void teleportPlayerToLocation(ServerWorld serverWorld, PlayerEntity player, RegistryKey<World> destination, BlockPos destinationPos)
	{
//		System.out.println("Key: " + destination.getLocation());
//		String command = "execute in bloodmagic:dungeon run teleport Dev 0 100 0";
		String command = getTextCommandForTeleport(destination, player, destinationPos.getX() + 0.5, destinationPos.getY(), destinationPos.getZ() + 0.5);
		MinecraftServer mcServer = serverWorld.getServer();
		mcServer.getCommandManager().handleCommand(getCommandSource(serverWorld), command);
	}

	public String getTextCommandForTeleport(RegistryKey<World> destination, PlayerEntity player, double posX, double posY, double posZ)
	{
		String playerName = player.getName().getString();
//		System.out.println("Potential player name: " + playerName);
		return "execute in " + destination.getLocation().toString() + " run teleport " + playerName + " " + posX + " " + posY + " " + posZ;
	}

	@Override
	public void sendMessage(ITextComponent component, UUID senderUUID)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public boolean shouldReceiveFeedback()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean shouldReceiveErrors()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean allowLogging()
	{
		// TODO Auto-generated method stub
		return false;
	}
}
