package wayoftime.bloodmagic.common.item.sigil;

import java.util.List;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import wayoftime.bloodmagic.core.data.Binding;
import wayoftime.bloodmagic.core.data.SoulTicket;
import wayoftime.bloodmagic.event.ItemBindEvent;
import wayoftime.bloodmagic.tile.TileTeleposer;
import wayoftime.bloodmagic.util.Constants;
import wayoftime.bloodmagic.util.helper.BindableHelper;
import wayoftime.bloodmagic.util.helper.NetworkHelper;
import wayoftime.bloodmagic.util.helper.PlayerHelper;
import wayoftime.bloodmagic.util.helper.TextHelper;

public class ItemSigilTeleposition extends ItemSigilBase
{
	public ItemSigilTeleposition()
	{
		super("teleposition", 1000);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand)
	{
		ItemStack stack = player.getHeldItem(hand);
		if (stack.getItem() instanceof ISigil.Holding)
			stack = ((Holding) stack.getItem()).getHeldItem(stack, player);
		if (PlayerHelper.isFakePlayer(player))
			return new ActionResult<>(ActionResultType.FAIL, stack);

		if (world.isRemote || isUnusable(stack))
		{
			return new ActionResult<>(ActionResultType.CONSUME, stack);
		}

		World storedWorld = getStoredWorld(stack, world);
		if (storedWorld != null && storedWorld instanceof ServerWorld)
		{
			RegistryKey<World> key = getStoredKey(stack, world);
			BlockPos telePos = getStoredPos(stack);
			TileEntity tile = storedWorld.getTileEntity(telePos);
			if (tile instanceof TileTeleposer)
			{
				((TileTeleposer) tile).teleportPlayerToLocation((ServerWorld) storedWorld, player, key, telePos.getX() + 0.5, telePos.getY() + 1, telePos.getZ() + 0.5);
				if (!player.isCreative())
					this.setUnusable(stack, !NetworkHelper.getSoulNetwork(getBinding(stack)).syphonAndDamage(player, SoulTicket.item(stack, world, player, getLpUsed())).isSuccess());

				storedWorld.playSound(null, telePos.getX(), telePos.getY(), telePos.getZ(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.BLOCKS, 1, 1);
				world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.BLOCKS, 1, 1);

				return new ActionResult<>(ActionResultType.PASS, stack);
			}
		}

		return new ActionResult<>(ActionResultType.PASS, stack);
	}

	@Override
	public ActionResultType onItemUse(ItemUseContext context)
	{
		ItemStack stack = context.getItem();
		BlockPos pos = context.getPos();
		World world = context.getWorld();
		PlayerEntity player = context.getPlayer();

		if (world.getTileEntity(pos) instanceof TileTeleposer)
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

		return ActionResultType.SUCCESS;
	}

	public void setStoredPos(ItemStack stack, BlockPos pos)
	{
		if (!stack.hasTag())
		{
			stack.setTag(new CompoundNBT());
		}

		CompoundNBT tag = stack.getTag();

		tag.putInt(Constants.NBT.X_COORD, pos.getX());
		tag.putInt(Constants.NBT.Y_COORD, pos.getY());
		tag.putInt(Constants.NBT.Z_COORD, pos.getZ());
	}

	public BlockPos getStoredPos(ItemStack stack)
	{
		if (!stack.hasTag())
		{
			stack.setTag(new CompoundNBT());
		}

		CompoundNBT tag = stack.getTag();

		return new BlockPos(tag.getInt(Constants.NBT.X_COORD), tag.getInt(Constants.NBT.Y_COORD), tag.getInt(Constants.NBT.Z_COORD));
	}

	public void setWorld(ItemStack stack, World world)
	{
		String worldKey = world.getDimensionKey().getLocation().toString();
		if (!stack.hasTag())
		{
			stack.setTag(new CompoundNBT());
		}

		stack.getTag().putString(Constants.NBT.WORLD, worldKey);
	}

	public RegistryKey<World> getStoredKey(ItemStack stack, World world)
	{
		if (!stack.hasTag())
		{
			return null;
		}

		String worldKey = stack.getTag().getString(Constants.NBT.WORLD);
		RegistryKey<World> registryKey = RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation(worldKey));
		return registryKey;
	}

	public World getStoredWorld(ItemStack stack, World world)
	{
		RegistryKey<World> registryKey = getStoredKey(stack, world);
		if (registryKey == null || world.getServer() == null)
		{
			return null;
		}

		return world.getServer().getWorld(registryKey);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag flag)
	{
		super.addInformation(stack, world, tooltip, flag);
		RegistryKey<World> storedKey = getStoredKey(stack, world);
//		World storedWorld = getStoredWorld(stack, world);
		if (storedKey != null)
		{
			BlockPos storedPos = getStoredPos(stack);
			tooltip.add(new TranslationTextComponent(TextHelper.localizeEffect("tooltip.bloodmagic.telepositionfocus.coords", storedPos.getX(), storedPos.getY(), storedPos.getZ())).mergeStyle(TextFormatting.GRAY));
			tooltip.add(new TranslationTextComponent("tooltip.bloodmagic.telepositionfocus.world", new TranslationTextComponent(storedKey.getLocation().toString())).mergeStyle(TextFormatting.GRAY));
		}
	}
}