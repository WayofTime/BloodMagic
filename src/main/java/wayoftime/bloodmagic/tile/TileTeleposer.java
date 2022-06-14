package wayoftime.bloodmagic.tile;

import java.util.List;
import java.util.UUID;

import net.minecraft.command.CommandSource;
import net.minecraft.command.ICommandSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.ObjectHolder;
import wayoftime.bloodmagic.common.item.ITeleposerFocus;
import wayoftime.bloodmagic.core.data.SoulNetwork;
import wayoftime.bloodmagic.core.data.SoulTicket;
import wayoftime.bloodmagic.tile.container.ContainerTeleposer;
import wayoftime.bloodmagic.util.Constants;
import wayoftime.bloodmagic.util.Utils;
import wayoftime.bloodmagic.util.helper.NetworkHelper;

public class TileTeleposer extends TileInventory implements ITickableTileEntity, INamedContainerProvider, ICommandSource
{
	@ObjectHolder("bloodmagic:teleposer")
	public static TileEntityType<TileTeleposer> TYPE;

	int previousInput = 0;

	public static final int FOCUS_SLOT = 0;

	public static final int MAX_UNIT_COST = 1000;
	public static final int MAX_TOTAL_COST = 10000;

	public TileTeleposer(TileEntityType<?> type)
	{
		super(type, 1, "teleposer");
	}

	public TileTeleposer()
	{
		this(TYPE);
	}

	@Override
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
		if (!(level instanceof ServerWorld))
		{
			return;
		}

		if (!canTeleport())
		{
			return;
		}

		ServerWorld serverWorld = (ServerWorld) level;

		ItemStack focusStack = getItem(FOCUS_SLOT);
		ITeleposerFocus focusItem = (ITeleposerFocus) focusStack.getItem();

		World linkedWorld = focusItem.getStoredWorld(focusStack, level);
		BlockPos linkedPos = focusItem.getStoredPos(focusStack);
		if (linkedWorld == null || linkedPos.equals(worldPosition))
		{
			return;
		}

		RegistryKey<World> linkedKey = linkedWorld.dimension();

		TileEntity boundTile = linkedWorld.getBlockEntity(linkedPos);
		if (boundTile instanceof TileTeleposer)
		{
			AxisAlignedBB entityRangeOffsetBB = focusItem.getEntityRangeOffset(linkedWorld, getBlockPos());
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

			AxisAlignedBB originalBB = entityRangeOffsetBB.move(getBlockPos());
			AxisAlignedBB focusBB = entityRangeOffsetBB.move(linkedPos);

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
				Vector3d newPosVec = entity.position().subtract(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ()).add(linkedPos.getX(), linkedPos.getY(), linkedPos.getZ());

				if (entity instanceof PlayerEntity && !(linkedWorld.equals(level)))
				{
					teleportPlayerToLocation(serverWorld, (PlayerEntity) entity, linkedKey, newPosVec.x, newPosVec.y, newPosVec.z);
				} else
				{
					entity.teleportTo(newPosVec.x, newPosVec.y, newPosVec.z);
					entity.setLevel(linkedWorld);
				}

				uses++;
			}

			for (Entity entity : focusEntities)
			{
				Vector3d newPosVec = entity.position().add(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ()).subtract(linkedPos.getX(), linkedPos.getY(), linkedPos.getZ());

				if (entity instanceof PlayerEntity && !(linkedWorld.equals(level)))
				{
					teleportPlayerToLocation(serverWorld, (PlayerEntity) entity, level.dimension(), newPosVec.x, newPosVec.y, newPosVec.z);
				} else
				{
					entity.teleportTo(newPosVec.x, newPosVec.y, newPosVec.z);
					entity.setLevel(level);
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

			level.playSound(null, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), SoundEvents.ENDERMAN_TELEPORT, SoundCategory.BLOCKS, 1, 1);
			linkedWorld.playSound(null, linkedPos.getX(), linkedPos.getY(), linkedPos.getZ(), SoundEvents.ENDERMAN_TELEPORT, SoundCategory.BLOCKS, 1, 1);

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
		if (!focusStack.isEmpty() && focusStack.getItem() instanceof ITeleposerFocus)
		{
			return NetworkHelper.getSoulNetwork(((ITeleposerFocus) focusStack.getItem()).getBinding(focusStack));
		}

		return null;
	}

	@Override
	public void deserialize(CompoundNBT tagCompound)
	{
		super.deserialize(tagCompound);

		this.previousInput = tagCompound.getInt(Constants.NBT.REDSTONE);
	}

	@Override
	public CompoundNBT serialize(CompoundNBT tagCompound)
	{
		super.serialize(tagCompound);

		tagCompound.putInt(Constants.NBT.REDSTONE, previousInput);

		return tagCompound;
	}

	@Override
	public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_)
	{
		assert level != null;
		return new ContainerTeleposer(this, p_createMenu_1_, p_createMenu_2_);
	}

	@Override
	public ITextComponent getDisplayName()
	{
		return new StringTextComponent("Teleposer");
	}

	public CommandSource getCommandSource(ServerWorld world)
	{
		return new CommandSource(this, new Vector3d(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ()), Vector2f.ZERO, world, 2, "Teleposer", new StringTextComponent("Teleposer"), world.getServer(), (Entity) null);
	}

	public void teleportPlayerToLocation(ServerWorld serverWorld, PlayerEntity player, RegistryKey<World> destination, double x, double y, double z)
	{
//		System.out.println("Key: " + destination.getLocation());
//		String command = "execute in bloodmagic:dungeon run teleport Dev 0 100 0";
		String command = getTextCommandForTeleport(destination, player, x, y, z);
		MinecraftServer mcServer = serverWorld.getServer();
		mcServer.getCommands().performCommand(getCommandSource(serverWorld), command);
	}

	public String getTextCommandForTeleport(RegistryKey<World> destination, PlayerEntity player, double posX, double posY, double posZ)
	{
		String playerName = player.getName().getString();
//		System.out.println("Potential player name: " + playerName);
		return "execute in " + destination.location().toString() + " run teleport " + playerName + " " + posX + " " + posY + " " + posZ;
	}

	@Override
	public void sendMessage(ITextComponent component, UUID senderUUID)
	{
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