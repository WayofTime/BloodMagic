package wayoftime.bloodmagic.common.item.sigil;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import wayoftime.bloodmagic.common.tile.TileTeleposer;
import wayoftime.bloodmagic.core.data.Binding;
import wayoftime.bloodmagic.core.data.SoulTicket;
import wayoftime.bloodmagic.event.ItemBindEvent;
import wayoftime.bloodmagic.util.Constants;
import wayoftime.bloodmagic.util.helper.BindableHelper;
import wayoftime.bloodmagic.util.helper.NetworkHelper;
import wayoftime.bloodmagic.util.helper.PlayerHelper;
import wayoftime.bloodmagic.util.helper.TextHelper;

import java.util.List;

public class ItemSigilTeleposition extends ItemSigilBase
{
	public ItemSigilTeleposition()
	{
		super("teleposition", 1000);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand)
	{
		ItemStack stack = player.getItemInHand(hand);
		if (stack.getItem() instanceof ISigil.Holding)
			stack = ((Holding) stack.getItem()).getHeldItem(stack, player);
		if (PlayerHelper.isFakePlayer(player))
			return new InteractionResultHolder<>(InteractionResult.FAIL, stack);

		if (world.isClientSide || isUnusable(stack))
		{
			return new InteractionResultHolder<>(InteractionResult.CONSUME, stack);
		}

		Level storedWorld = getStoredWorld(stack, world);
		if (storedWorld != null && storedWorld instanceof ServerLevel)
		{
			ResourceKey<Level> key = getStoredKey(stack, world);
			BlockPos telePos = getStoredPos(stack);
			BlockEntity tile = storedWorld.getBlockEntity(telePos);
			if (tile instanceof TileTeleposer)
			{
				((TileTeleposer) tile).teleportPlayerToLocation((ServerLevel) storedWorld, player, key, telePos.getX() + 0.5, telePos.getY() + 1, telePos.getZ() + 0.5);
				if (!player.isCreative())
					this.setUnusable(stack, !NetworkHelper.getSoulNetwork(getBinding(stack)).syphonAndDamage(player, SoulTicket.item(stack, world, player, getLpUsed())).isSuccess());

				storedWorld.playSound(null, telePos.getX(), telePos.getY(), telePos.getZ(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.BLOCKS, 1, 1);
				world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.BLOCKS, 1, 1);

				return new InteractionResultHolder<>(InteractionResult.PASS, stack);
			}
		}

		return new InteractionResultHolder<>(InteractionResult.PASS, stack);
	}

	@Override
	public InteractionResult useOn(UseOnContext context)
	{
		ItemStack stack = context.getItemInHand();
		BlockPos pos = context.getClickedPos();
		Level world = context.getLevel();
		Player player = context.getPlayer();

		if (world.getBlockEntity(pos) instanceof TileTeleposer)
		{
			setStoredPos(stack, pos);
			setWorld(stack, world);

			Binding binding = getBinding(stack);
			if (binding == null)
			{ // If the binding is null, let's create one
				if (onBind(player, stack))
				{
					ItemBindEvent toPost = new ItemBindEvent(player, stack);
					if (!MinecraftForge.EVENT_BUS.post(toPost))
						BindableHelper.applyBinding(stack, player);
				}
			}
		}

		return InteractionResult.SUCCESS;
	}

	public void setStoredPos(ItemStack stack, BlockPos pos)
	{
		if (!stack.hasTag())
		{
			stack.setTag(new CompoundTag());
		}

		CompoundTag tag = stack.getTag();

		tag.putInt(Constants.NBT.X_COORD, pos.getX());
		tag.putInt(Constants.NBT.Y_COORD, pos.getY());
		tag.putInt(Constants.NBT.Z_COORD, pos.getZ());
	}

	public BlockPos getStoredPos(ItemStack stack)
	{
		if (!stack.hasTag())
		{
			stack.setTag(new CompoundTag());
		}

		CompoundTag tag = stack.getTag();

		return new BlockPos(tag.getInt(Constants.NBT.X_COORD), tag.getInt(Constants.NBT.Y_COORD), tag.getInt(Constants.NBT.Z_COORD));
	}

	public void setWorld(ItemStack stack, Level world)
	{
		String worldKey = world.dimension().location().toString();
		if (!stack.hasTag())
		{
			stack.setTag(new CompoundTag());
		}

		stack.getTag().putString(Constants.NBT.WORLD, worldKey);
	}

	public ResourceKey<Level> getStoredKey(ItemStack stack, Level world)
	{
		if (!stack.hasTag())
		{
			return null;
		}

		String worldKey = stack.getTag().getString(Constants.NBT.WORLD);
		ResourceKey<Level> registryKey = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(worldKey));
		return registryKey;
	}

	public Level getStoredWorld(ItemStack stack, Level world)
	{
		ResourceKey<Level> registryKey = getStoredKey(stack, world);
		if (registryKey == null || world.getServer() == null)
		{
			return null;
		}

		return world.getServer().getLevel(registryKey);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flag)
	{
		super.appendHoverText(stack, world, tooltip, flag);
		ResourceKey<Level> storedKey = getStoredKey(stack, world);
//		World storedWorld = getStoredWorld(stack, world);
		if (storedKey != null)
		{
			BlockPos storedPos = getStoredPos(stack);
			tooltip.add(Component.translatable(TextHelper.localizeEffect("tooltip.bloodmagic.telepositionfocus.coords", storedPos.getX(), storedPos.getY(), storedPos.getZ())).withStyle(ChatFormatting.GRAY));
			tooltip.add(Component.translatable("tooltip.bloodmagic.telepositionfocus.world", Component.translatable(storedKey.location().toString())).withStyle(ChatFormatting.GRAY));
		}
	}
}