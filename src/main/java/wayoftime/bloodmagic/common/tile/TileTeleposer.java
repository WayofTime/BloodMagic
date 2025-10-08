package wayoftime.bloodmagic.common.tile;

import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import wayoftime.bloodmagic.common.container.tile.ContainerTeleposer;
import wayoftime.bloodmagic.common.item.ITeleposerFocus;
import wayoftime.bloodmagic.core.data.SoulNetwork;
import wayoftime.bloodmagic.core.data.SoulTicket;
import wayoftime.bloodmagic.util.Constants;
import wayoftime.bloodmagic.util.Utils;
import wayoftime.bloodmagic.util.helper.NetworkHelper;

import java.util.List;
import java.util.UUID;

public class TileTeleposer extends TileInventory implements MenuProvider, CommandSource
{
	int previousInput = 0;

	public static final int FOCUS_SLOT = 0;

	public static final int MAX_UNIT_COST = 1000;
	public static final int MAX_TOTAL_COST = 10000;

	public TileTeleposer(BlockEntityType<?> type, BlockPos pos, BlockState state)
	{
		super(type, 1, "teleposer", pos, state);
	}

	public TileTeleposer(BlockPos pos, BlockState state)
	{
		this(BloodMagicTileEntities.TELEPOSER_TYPE.get(), pos, state);
	}

	public void tick()
	{
		if (level.isClientSide)
		{
			return;
		}

		int currentInput = getLevel().getDirectSignalTo(worldPosition);

		if (previousInput == 0 && currentInput != 0)
		{
			previousInput = currentInput;

			initiateTeleport();
		} else
		{
			previousInput = currentInput;
		}
	}

	public void initiateTeleport()
	{
		if (!(level instanceof ServerLevel))
		{
			return;
		}

		if (!canTeleport())
		{
			return;
		}

		ServerLevel serverWorld = (ServerLevel) level;

		ItemStack focusStack = getItem(FOCUS_SLOT);
		ITeleposerFocus focusItem = (ITeleposerFocus) focusStack.getItem();

		Level linkedWorld = focusItem.getStoredWorld(focusStack, level);
		BlockPos linkedPos = focusItem.getStoredPos(focusStack);
		if (linkedWorld == null || linkedPos.equals(worldPosition))
		{
			return;
		}

		ResourceKey<Level> linkedKey = linkedWorld.dimension();

		BlockEntity boundTile = linkedWorld.getBlockEntity(linkedPos);
		if (boundTile instanceof TileTeleposer)
		{
			AABB entityRangeOffsetBB = focusItem.getEntityRangeOffset(linkedWorld, getBlockPos());
			if (entityRangeOffsetBB == null)
			{
				return;
			}

			double transportCost = Math.min(0.5 * Math.sqrt(linkedPos.distSqr(worldPosition)), MAX_UNIT_COST);
			if (!linkedWorld.equals(level))
			{
				transportCost = MAX_UNIT_COST;
			}

//			System.out.println("Area: " + entityRangeOffsetBB);

			// Teleports players from current teleposer

			AABB originalBB = entityRangeOffsetBB.move(getBlockPos());
			AABB focusBB = entityRangeOffsetBB.move(linkedPos);

			List<Entity> originalEntities = level.getEntitiesOfClass(Entity.class, originalBB);
			List<Entity> focusEntities = level.getEntitiesOfClass(Entity.class, focusBB);

			List<BlockPos> offsetList = focusItem.getBlockListOffset(level);

			int uses = 0;
			int maxUses = offsetList.size() + originalEntities.size() + focusEntities.size();

			int maxDrain = Math.min((int) (transportCost * maxUses), MAX_TOTAL_COST);
			SoulNetwork network = getNetwork();
			if (network.getCurrentEssence() < maxDrain)
			{

				return;
			}

			for (Entity entity : originalEntities)
			{
				Vec3 newPosVec = entity.position().subtract(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ()).add(linkedPos.getX(), linkedPos.getY(), linkedPos.getZ());

				if (entity instanceof Player && !(linkedWorld.equals(level)))
				{
					teleportPlayerToLocation(serverWorld, (Player) entity, linkedKey, newPosVec.x, newPosVec.y, newPosVec.z);
				} else
				{
					entity.teleportTo(newPosVec.x, newPosVec.y, newPosVec.z);
				}

				uses++;
			}

			for (Entity entity : focusEntities)
			{
				Vec3 newPosVec = entity.position().add(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ()).subtract(linkedPos.getX(), linkedPos.getY(), linkedPos.getZ());

				if (entity instanceof Player && !(linkedWorld.equals(level)))
				{
					teleportPlayerToLocation(serverWorld, (Player) entity, level.dimension(), newPosVec.x, newPosVec.y, newPosVec.z);
				} else
				{
					entity.teleportTo(newPosVec.x, newPosVec.y, newPosVec.z);
//					entity.setLevel(level);
				}

				uses++;
			}

			for (BlockPos offsetPos : offsetList)
			{
				BlockPos originalPos = worldPosition.offset(offsetPos);
				BlockPos focusPos = linkedPos.offset(offsetPos);

				if (Utils.swapLocations(level, originalPos, linkedWorld, focusPos, false))
				{
					uses++;
				}
			}

			level.playSound(null, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.BLOCKS, 1, 1);
			linkedWorld.playSound(null, linkedPos.getX(), linkedPos.getY(), linkedPos.getZ(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.BLOCKS, 1, 1);

			network.syphon(SoulTicket.block(level, worldPosition, Math.min((int) (uses * transportCost), MAX_TOTAL_COST)));
		}
	}

	public boolean canTeleport()
	{
		SoulNetwork network = getNetwork();

		return network != null;
	}

	private SoulNetwork getNetwork()
	{
		ItemStack focusStack = this.getItem(FOCUS_SLOT);
		if (!focusStack.isEmpty() && focusStack.getItem() instanceof ITeleposerFocus focus)
		{
			if (focus.getBinding(focusStack) != null)
			{
				return NetworkHelper.getSoulNetwork(focus.getBinding(focusStack));
			}
		}

		return null;
	}

	@Override
	public void deserialize(CompoundTag tagCompound)
	{
		super.deserialize(tagCompound);

		this.previousInput = tagCompound.getInt(Constants.NBT.REDSTONE);
	}

	@Override
	public CompoundTag serialize(CompoundTag tagCompound)
	{
		super.serialize(tagCompound);

		tagCompound.putInt(Constants.NBT.REDSTONE, previousInput);

		return tagCompound;
	}

	@Override
	public AbstractContainerMenu createMenu(int p_createMenu_1_, Inventory p_createMenu_2_, Player p_createMenu_3_)
	{
		assert level != null;
		return new ContainerTeleposer(this, p_createMenu_1_, p_createMenu_2_);
	}

	@Override
	public Component getDisplayName()
	{
		return Component.literal("Teleposer");
	}

	public CommandSourceStack getCommandSource(ServerLevel world)
	{
		return new CommandSourceStack(this, new Vec3(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ()), Vec2.ZERO, world, 2, "Teleposer", Component.literal("Teleposer"), world.getServer(), (Entity) null);
	}

	public void teleportPlayerToLocation(ServerLevel serverWorld, Player player, ResourceKey<Level> destination, double x, double y, double z)
	{
//		System.out.println("Key: " + destination.getLocation());
//		String command = "execute in bloodmagic:dungeon run teleport Dev 0 100 0";
		String command = getTextCommandForTeleport(destination, player, x, y, z);
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