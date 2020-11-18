package wayoftime.bloodmagic.tile;

import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.PotionUtils;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelDataManager;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.registries.ObjectHolder;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.util.ChatUtil;
import wayoftime.bloodmagic.util.Utils;

public class TileMimic extends TileInventory
{
	@ObjectHolder("bloodmagic:mimic")
	public static TileEntityType<TileMimic> TYPE;

	public static final ModelProperty<BlockState> MIMIC = new ModelProperty<>();

	private BlockState mimic;

	public boolean dropItemsOnBreak = true;
	public CompoundNBT tileTag = new CompoundNBT();
	public TileEntity mimicedTile = null;

	public int playerCheckRadius = 5;
	public int potionSpawnRadius = 5;
	public int potionSpawnInterval = 40;

	private int internalCounter = 0;

	public TileMimic(TileEntityType<?> type)
	{
		super(type, 2, "mimic");
	}

	public TileMimic()
	{
		this(TYPE);
	}

	public boolean onBlockActivated(World world, BlockPos pos, BlockState state, PlayerEntity player, Hand hand, ItemStack heldItem, Direction side)
	{
		if (!heldItem.isEmpty() && player.isCreative())
		{
			List<EffectInstance> list = PotionUtils.getEffectsFromStack(heldItem);
			if (!list.isEmpty())
			{
				if (!world.isRemote)
				{
					setInventorySlotContents(1, heldItem.copy());
					world.notifyBlockUpdate(pos, state, state, 3);
					ChatUtil.sendNoSpam(player, new TranslationTextComponent("chat.bloodmagic.mimic.potionSet"));
				}
				return true;
			}
//			} else if (heldItem.getItem() == RegistrarBloodMagicItems.POTION_FLASK)
//			{
//				// The potion flask is empty, therefore we have to reset the stored potion.
//				if (!world.isRemote)
//				{
//					setInventorySlotContents(1, ItemStack.EMPTY);
//					world.notifyBlockUpdate(pos, state, state, 3);
//					ChatUtil.sendNoSpam(player, new TranslationTextComponent("chat.bloodmagic.mimic.potionRemove"));
//				}
//				return true;
//			}
		}

		if (performSpecialAbility(player, side))
		{
			return true;
		}

		if (player.isSneaking())
			return false;

		if (!player.getHeldItem(hand).isEmpty() && player.getHeldItem(hand).getItem() == new ItemStack(BloodMagicBlocks.MIMIC.get()).getItem())
			return false;

		if (!getStackInSlot(0).isEmpty() && !player.getHeldItem(hand).isEmpty())
			return false;

		if (!dropItemsOnBreak && !player.isCreative())
			return false;

		Utils.insertItemToTile(this, player, 0);
		ItemStack stack = getStackInSlot(0);
		if (mimic == null || mimic == Blocks.AIR.getDefaultState())
		{
			if (!stack.isEmpty() && stack.getItem() instanceof BlockItem && !world.isRemote)
			{
				Block block = ((BlockItem) stack.getItem()).getBlock();
				this.setMimic(block.getDefaultState());
//				mimic = block.getDefaultState();
//				markDirty();
			}
		}
		this.refreshTileEntity();

		if (player.isCreative())
		{
			dropItemsOnBreak = getStackInSlot(0).isEmpty();
		}

//		world.notifyBlockUpdate(pos, state, state, 3);
		return true;
	}

	public boolean performSpecialAbility(PlayerEntity player, Direction sideHit)
	{
		if (!player.isCreative())
		{
			return false;
		}

		if (player.getActiveItemStack().isEmpty() && !getStackInSlot(1).isEmpty())
		{
			switch (sideHit)
			{
			case EAST: // When the block is clicked on the EAST or WEST side, potionSpawnRadius is
						// edited.
			case WEST:
				if (player.isSneaking())
				{
					potionSpawnRadius = Math.max(potionSpawnRadius - 1, 0);
					ChatUtil.sendNoSpam(player, new TranslationTextComponent("chat.bloodmagic.mimic.potionSpawnRadius.down", potionSpawnRadius));
				} else
				{
					potionSpawnRadius++;
					ChatUtil.sendNoSpam(player, new TranslationTextComponent("chat.bloodmagic.mimic.potionSpawnRadius.up", potionSpawnRadius));
				}
				break;
			case NORTH: // When the block is clicked on the NORTH or SOUTH side, detectRadius is edited.
			case SOUTH:
				if (player.isSneaking())
				{
					playerCheckRadius = Math.max(playerCheckRadius - 1, 0);
					ChatUtil.sendNoSpam(player, new TranslationTextComponent("chat.bloodmagic.mimic.detectRadius.down", playerCheckRadius));
				} else
				{
					playerCheckRadius++;
					ChatUtil.sendNoSpam(player, new TranslationTextComponent("chat.bloodmagic.mimic.detectRadius.up", playerCheckRadius));
				}
				break;
			case UP: // When the block is clicked on the UP or DOWN side, potionSpawnInterval is
						// edited.
			case DOWN:
				if (player.isSneaking())
				{
					potionSpawnInterval = Math.max(potionSpawnInterval - 1, 1);
					ChatUtil.sendNoSpam(player, new TranslationTextComponent("chat.bloodmagic.mimic.potionInterval.down", potionSpawnInterval));
				} else
				{
					potionSpawnInterval++;
					ChatUtil.sendNoSpam(player, new TranslationTextComponent("chat.bloodmagic.mimic.potionInterval.up", potionSpawnInterval));
				}
				break;
			default:
				break;

			}

			return true;
		}

		return false;
	}

	public void refreshTileEntity()
	{
		if (mimicedTile != null)
		{
			dropMimicedTileInventory();
		}
		mimicedTile = getTileFromStackWithTag(getWorld(), pos, getStackInSlot(0), tileTag, mimic);
	}

	public void dropMimicedTileInventory()
	{
		if (!getWorld().isRemote && mimicedTile instanceof IInventory)
		{
			InventoryHelper.dropInventoryItems(getWorld(), getPos(), (IInventory) mimicedTile);
		}
	}

	@Nullable
	public static TileEntity getTileFromStackWithTag(World world, BlockPos pos, ItemStack stack, @Nullable CompoundNBT tag, BlockState replacementState)
	{
		if (!stack.isEmpty() && stack.getItem() instanceof BlockItem)
		{
			Block block = ((BlockItem) stack.getItem()).getBlock();
			BlockState state = replacementState;
			if (block.hasTileEntity(state))
			{
				TileEntity tile = block.createTileEntity(state, world);

				if (tile == null)
					return null;

				if (tag != null)
				{
					CompoundNBT copyTag = tag.copy();
					copyTag.putInt("x", pos.getX());
					copyTag.putInt("y", pos.getY());
					copyTag.putInt("z", pos.getZ());
					tile.deserializeNBT(copyTag);
				}

				tile.setWorldAndPos(world, pos);

				return tile;
			}
		}

		return null;
	}

	public void setMimic(BlockState mimic)
	{
		this.mimic = mimic;
		markDirty();
		world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
	}

	public BlockState getMimic()
	{
		return mimic;
	}

	// The getUpdateTag()/handleUpdateTag() pair is called whenever the client
	// receives a new chunk
	// it hasn't seen before. i.e. the chunk is loaded

	@Override
	public CompoundNBT getUpdateTag()
	{
		CompoundNBT tag = super.getUpdateTag();
		writeMimic(tag);
		return tag;
	}

	// The getUpdatePacket()/onDataPacket() pair is used when a block update happens
	// on the client
	// (a blockstate change or an explicit notificiation of a block update from the
	// server). It's
	// easiest to implement them based on getUpdateTag()/handleUpdateTag()

	@Nullable
	@Override
	public SUpdateTileEntityPacket getUpdatePacket()
	{
		return new SUpdateTileEntityPacket(pos, 1, getUpdateTag());
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
	{
		BlockState oldMimic = mimic;
		CompoundNBT tag = pkt.getNbtCompound();
		deserialize(tag);
		if (!Objects.equals(oldMimic, mimic))
		{
			ModelDataManager.requestModelDataRefresh(this);
			world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
		}
	}

	@Nonnull
	@Override
	public IModelData getModelData()
	{
		return new ModelDataMap.Builder().withInitial(MIMIC, mimic).build();
	}

	@Override
	public void deserialize(CompoundNBT tag)
	{
		super.deserialize(tag);

		dropItemsOnBreak = tag.getBoolean("dropItemsOnBreak");
		tileTag = tag.getCompound("tileTag");
//        stateOfReplacedBlock = StateUtil.parseState(tag.getString("stateOfReplacedBlock"));
		readMimic(tag);
		mimicedTile = getTileFromStackWithTag(getWorld(), pos, getStackInSlot(0), tileTag, mimic);
		playerCheckRadius = tag.getInt("playerCheckRadius");
		potionSpawnRadius = tag.getInt("potionSpawnRadius");
		potionSpawnInterval = Math.max(1, tag.getInt("potionSpawnInterval"));
	}

	private void readMimic(CompoundNBT tag)
	{
		if (tag.contains("mimic"))
		{
			mimic = NBTUtil.readBlockState(tag.getCompound("mimic"));
		}
	}

	@Override
	public CompoundNBT serialize(CompoundNBT tag)
	{
		tag.putBoolean("dropItemsOnBreak", dropItemsOnBreak);
		tag.put("tileTag", tileTag);
		tag.putInt("playerCheckRadius", playerCheckRadius);
		tag.putInt("potionSpawnRadius", potionSpawnRadius);
		tag.putInt("potionSpawnInterval", potionSpawnInterval);
//        tag.putString("stateOfReplacedBlock", stateOfReplacedBlock.toString());
		writeMimic(tag);
		return super.serialize(tag);
	}

	private void writeMimic(CompoundNBT tag)
	{
		if (mimic != null)
		{
			tag.put("mimic", NBTUtil.writeBlockState(mimic));
		}
	}

	@Override
	public void dropItems()
	{
		if (dropItemsOnBreak)
		{
			InventoryHelper.dropInventoryItems(getWorld(), getPos(), this);
		}

		dropMimicedTileInventory();
	}
}