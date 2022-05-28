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
		if (world.isRemote)
		{
			return;
		}

		int currentInput = getWorld().getStrongPower(pos);
//		System.out.println("Input: " + currentInput);

		if (previousInput == 0 && currentInput != 0)
		{
//            initiateTeleport();
//			getNetwork().syphon(SoulTicket.block(getWorld(), getPos(), 10));
//			System.out.println("High edge!");
			initiateTeleport();
		}

		previousInput = currentInput;
	}

	public void initiateTeleport()
	{
		if (!(world instanceof ServerWorld))
		{
			return;
		}

		if (!canTeleport())
		{
			return;
		}

		ServerWorld serverWorld = (ServerWorld) world;

		ItemStack focusStack = getStackInSlot(FOCUS_SLOT);
		ITeleposerFocus focusItem = (ITeleposerFocus) focusStack.getItem();

		World linkedWorld = focusItem.getStoredWorld(focusStack, world);
		BlockPos linkedPos = focusItem.getStoredPos(focusStack);
		if (linkedWorld == null || linkedPos.equals(pos))
		{
			return;
		}

		RegistryKey<World> linkedKey = linkedWorld.getDimensionKey();

		TileEntity boundTile = linkedWorld.getTileEntity(linkedPos);
		if (boundTile instanceof TileTeleposer)
		{
			AxisAlignedBB entityRangeOffsetBB = focusItem.getEntityRangeOffset(linkedWorld, getPos());
			if (entityRangeOffsetBB == null)
			{
				return;
			}

//			System.out.println("Area: " + entityRangeOffsetBB);

			// Teleports players from current teleposer

			AxisAlignedBB originalBB = entityRangeOffsetBB.offset(getPos());
			AxisAlignedBB focusBB = entityRangeOffsetBB.offset(linkedPos);

			List<Entity> originalEntities = world.getEntitiesWithinAABB(Entity.class, originalBB);
			List<Entity> focusEntities = world.getEntitiesWithinAABB(Entity.class, focusBB);

			for (Entity entity : originalEntities)
			{
				Vector3d newPosVec = entity.getPositionVec().subtract(pos.getX(), pos.getY(), pos.getZ()).add(linkedPos.getX(), linkedPos.getY(), linkedPos.getZ());

				if (entity instanceof PlayerEntity)
				{
					teleportPlayerToLocation(serverWorld, (PlayerEntity) entity, linkedKey, newPosVec.x, newPosVec.y, newPosVec.z);
				} else
				{
					entity.setPositionAndUpdate(newPosVec.x, newPosVec.y, newPosVec.z);
					entity.setWorld(linkedWorld);
				}
			}

			for (Entity entity : focusEntities)
			{
				Vector3d newPosVec = entity.getPositionVec().add(pos.getX(), pos.getY(), pos.getZ()).subtract(linkedPos.getX(), linkedPos.getY(), linkedPos.getZ());

				if (entity instanceof PlayerEntity)
				{
					teleportPlayerToLocation(serverWorld, (PlayerEntity) entity, world.getDimensionKey(), newPosVec.x, newPosVec.y, newPosVec.z);
				} else
				{
					entity.setPositionAndUpdate(newPosVec.x, newPosVec.y, newPosVec.z);
					entity.setWorld(world);
				}
			}

			List<BlockPos> offsetList = focusItem.getBlockListOffset(world);
			for (BlockPos offsetPos : offsetList)
			{
				BlockPos originalPos = pos.add(offsetPos);
				BlockPos focusPos = linkedPos.add(offsetPos);

				Utils.swapLocations(world, originalPos, linkedWorld, focusPos, false);
			}

			world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.BLOCKS, 1, 1);
			linkedWorld.playSound(null, linkedPos.getX(), linkedPos.getY(), linkedPos.getZ(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.BLOCKS, 1, 1);
		}
	}

	public boolean canTeleport()
	{
		SoulNetwork network = getNetwork();

		return network != null;
	}

	private SoulNetwork getNetwork()
	{
		ItemStack focusStack = this.getStackInSlot(FOCUS_SLOT);
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
		assert world != null;
		return new ContainerTeleposer(this, p_createMenu_1_, p_createMenu_2_);
	}

	@Override
	public ITextComponent getDisplayName()
	{
		return new StringTextComponent("Teleposer");
	}

	public CommandSource getCommandSource(ServerWorld world)
	{
		return new CommandSource(this, new Vector3d(pos.getX(), pos.getY(), pos.getZ()), Vector2f.ZERO, world, 2, "Teleposer", new StringTextComponent("Teleposer"), world.getServer(), (Entity) null);
	}

	public void teleportPlayerToLocation(ServerWorld serverWorld, PlayerEntity player, RegistryKey<World> destination, double x, double y, double z)
	{
//		System.out.println("Key: " + destination.getLocation());
//		String command = "execute in bloodmagic:dungeon run teleport Dev 0 100 0";
		String command = getTextCommandForTeleport(destination, player, x, y, z);
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