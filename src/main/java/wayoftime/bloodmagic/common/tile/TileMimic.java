package wayoftime.bloodmagic.common.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.util.ChatUtil;
import wayoftime.bloodmagic.util.Utils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public class TileMimic extends TileInventory
{
	public static final ModelProperty<BlockState> MIMIC = new ModelProperty<>();

	private BlockState mimic;

	public boolean dropItemsOnBreak = true;
	public CompoundTag tileTag = new CompoundTag();
	public BlockEntity mimicedTile = null;

	public int playerCheckRadius = 5;
	public int potionSpawnRadius = 5;
	public int potionSpawnInterval = 40;

	private int internalCounter = 0;

	public TileMimic(BlockEntityType<?> type, BlockPos pos, BlockState state)
	{
		super(type, 2, "mimic", pos, state);
	}

	public TileMimic(BlockPos pos, BlockState state)
	{
		this(BloodMagicTileEntities.MIMIC_TYPE.get(), pos, state);
	}

	public boolean onBlockActivated(Level world, BlockPos pos, BlockState state, Player player, InteractionHand hand, ItemStack heldItem, Direction side)
	{
		if (!heldItem.isEmpty() && player.isCreative())
		{
			List<MobEffectInstance> list = PotionUtils.getMobEffects(heldItem);
			if (!list.isEmpty())
			{
				if (!world.isClientSide)
				{
					setItem(1, heldItem.copy());
					world.sendBlockUpdated(pos, state, state, 3);
					ChatUtil.sendNoSpam(player, Component.translatable("chat.bloodmagic.mimic.potionSet"));
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

		if (player.isShiftKeyDown())
			return false;

		if (!player.getItemInHand(hand).isEmpty() && player.getItemInHand(hand).getItem() == new ItemStack(BloodMagicBlocks.MIMIC.get()).getItem())
			return false;

		if (!getItem(0).isEmpty() && !player.getItemInHand(hand).isEmpty())
			return false;

		if (!dropItemsOnBreak && !player.isCreative())
			return false;

		Utils.insertItemToTile(this, player, 0);
		ItemStack stack = getItem(0);
		if (mimic == null || mimic == Blocks.AIR.defaultBlockState())
		{
			if (!stack.isEmpty() && stack.getItem() instanceof BlockItem && !world.isClientSide)
			{
				Block block = ((BlockItem) stack.getItem()).getBlock();
				this.setMimic(block.defaultBlockState());
//				mimic = block.getDefaultState();
//				markDirty();
			}
		}
		this.refreshTileEntity();

		if (player.isCreative())
		{
			dropItemsOnBreak = getItem(0).isEmpty();
		}

//		world.notifyBlockUpdate(pos, state, state, 3);
		return true;
	}

	public boolean performSpecialAbility(Player player, Direction sideHit)
	{
		if (!player.isCreative())
		{
			return false;
		}

		if (player.getUseItem().isEmpty() && !getItem(1).isEmpty())
		{
			switch (sideHit)
			{
			case EAST: // When the block is clicked on the EAST or WEST side, potionSpawnRadius is
						// edited.
			case WEST:
				if (player.isShiftKeyDown())
				{
					potionSpawnRadius = Math.max(potionSpawnRadius - 1, 0);
					ChatUtil.sendNoSpam(player, Component.translatable("chat.bloodmagic.mimic.potionSpawnRadius.down", potionSpawnRadius));
				} else
				{
					potionSpawnRadius++;
					ChatUtil.sendNoSpam(player, Component.translatable("chat.bloodmagic.mimic.potionSpawnRadius.up", potionSpawnRadius));
				}
				break;
			case NORTH: // When the block is clicked on the NORTH or SOUTH side, detectRadius is edited.
			case SOUTH:
				if (player.isShiftKeyDown())
				{
					playerCheckRadius = Math.max(playerCheckRadius - 1, 0);
					ChatUtil.sendNoSpam(player, Component.translatable("chat.bloodmagic.mimic.detectRadius.down", playerCheckRadius));
				} else
				{
					playerCheckRadius++;
					ChatUtil.sendNoSpam(player, Component.translatable("chat.bloodmagic.mimic.detectRadius.up", playerCheckRadius));
				}
				break;
			case UP: // When the block is clicked on the UP or DOWN side, potionSpawnInterval is
						// edited.
			case DOWN:
				if (player.isShiftKeyDown())
				{
					potionSpawnInterval = Math.max(potionSpawnInterval - 1, 1);
					ChatUtil.sendNoSpam(player, Component.translatable("chat.bloodmagic.mimic.potionInterval.down", potionSpawnInterval));
				} else
				{
					potionSpawnInterval++;
					ChatUtil.sendNoSpam(player, Component.translatable("chat.bloodmagic.mimic.potionInterval.up", potionSpawnInterval));
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
		mimicedTile = getTileFromStackWithTag(getLevel(), worldPosition, getItem(0), tileTag, mimic);
	}

	public void dropMimicedTileInventory()
	{
		if (!getLevel().isClientSide && mimicedTile instanceof Container)
		{
			Containers.dropContents(getLevel(), getBlockPos(), (Container) mimicedTile);
		}
	}

	@Nullable
	public static BlockEntity getTileFromStackWithTag(Level world, BlockPos pos, ItemStack stack, @Nullable CompoundTag tag, BlockState replacementState)
	{
		if (!stack.isEmpty() && stack.getItem() instanceof BlockItem)
		{
			Block block = ((BlockItem) stack.getItem()).getBlock();
			BlockState state = replacementState;
			if (block instanceof EntityBlock)
			{
				BlockEntity tile = ((EntityBlock) block).newBlockEntity(pos, state);

				if (tile == null)
					return null;

				if (tag != null)
				{
					CompoundTag copyTag = tag.copy();
					copyTag.putInt("x", pos.getX());
					copyTag.putInt("y", pos.getY());
					copyTag.putInt("z", pos.getZ());
					tile.deserializeNBT(copyTag);
				}

				tile.setLevel(world);
//				tile.setLevelAndPosition(world, pos);

				return tile;
			}
		}

		return null;
	}

	public void setMimic(BlockState mimic)
	{
		this.mimic = mimic;
		setChanged();
		level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
	}

	public BlockState getMimic()
	{
		return mimic;
	}

	// The getUpdateTag()/handleUpdateTag() pair is called whenever the client
	// receives a new chunk
	// it hasn't seen before. i.e. the chunk is loaded

	@Override
	public CompoundTag getUpdateTag()
	{
		CompoundTag tag = super.getUpdateTag();
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
	public ClientboundBlockEntityDataPacket getUpdatePacket()
	{
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt)
	{
		BlockState oldMimic = mimic;
		CompoundTag tag = pkt.getTag();
		deserialize(tag);
		if (!Objects.equals(oldMimic, mimic))
		{
			requestModelDataUpdate();
			level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
		}
	}

	@Nonnull
	@Override
	public ModelData getModelData()
	{
		return ModelData.builder().with(MIMIC, mimic).build();
	}

	@Override
	public void deserialize(CompoundTag tag)
	{
		super.deserialize(tag);

		dropItemsOnBreak = tag.getBoolean("dropItemsOnBreak");
		tileTag = tag.getCompound("tileTag");
//        stateOfReplacedBlock = StateUtil.parseState(tag.getString("stateOfReplacedBlock"));
		readMimic(tag);
		mimicedTile = getTileFromStackWithTag(getLevel(), worldPosition, getItem(0), tileTag, mimic);
		playerCheckRadius = tag.getInt("playerCheckRadius");
		potionSpawnRadius = tag.getInt("potionSpawnRadius");
		potionSpawnInterval = Math.max(1, tag.getInt("potionSpawnInterval"));
	}

	private void readMimic(CompoundTag tag)
	{
		if (tag.contains("mimic"))
		{
			mimic = NbtUtils.readBlockState(this.level.holderLookup(Registries.BLOCK),tag.getCompound("mimic"));
		}
	}

	@Override
	public CompoundTag serialize(CompoundTag tag)
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

	private void writeMimic(CompoundTag tag)
	{
		if (mimic != null)
		{
			tag.put("mimic", NbtUtils.writeBlockState(mimic));
		}
	}

	@Override
	public void dropItems()
	{
		if (dropItemsOnBreak)
		{
			Containers.dropContents(getLevel(), getBlockPos(), this);
		}

		dropMimicedTileInventory();
	}
}