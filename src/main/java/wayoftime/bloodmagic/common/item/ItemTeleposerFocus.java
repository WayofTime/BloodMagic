package wayoftime.bloodmagic.common.item;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import wayoftime.bloodmagic.core.data.Binding;
import wayoftime.bloodmagic.event.ItemBindEvent;
import wayoftime.bloodmagic.tile.TileTeleposer;
import wayoftime.bloodmagic.util.Constants;
import wayoftime.bloodmagic.util.helper.BindableHelper;
import wayoftime.bloodmagic.util.helper.TextHelper;

public class ItemTeleposerFocus extends ItemBindableBase implements ITeleposerFocus
{
	public final int range;

	public ItemTeleposerFocus(int range)
	{
		super();
		this.range = range;
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

	@Override
	public AxisAlignedBB getEntityRangeOffset(World world, BlockPos teleposerPos)
	{
		return new AxisAlignedBB(-range, 1, -range, range + 1, 2 * range + 2, range + 1);
	}

	@Override
	public List<BlockPos> getBlockListOffset(World world)
	{
		List<BlockPos> posList = new ArrayList<>();

		for (int i = -range; i <= range; i++)
		{
			for (int j = 1; j <= 2 * range + 1; j++)
			{
				for (int k = -range; k <= range; k++)
				{
					posList.add(new BlockPos(i, j, k));
				}
			}
		}

		return posList;
	}
}
