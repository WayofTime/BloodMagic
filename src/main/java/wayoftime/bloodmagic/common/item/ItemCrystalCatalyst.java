package wayoftime.bloodmagic.common.item;

import java.util.List;

import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.api.compat.EnumDemonWillType;
import wayoftime.bloodmagic.common.block.BlockDemonCrystal;
import wayoftime.bloodmagic.common.tile.TileDemonCrystal;

public class ItemCrystalCatalyst extends Item
{
	public final EnumDemonWillType type;
	public final double injectedWill;
	public final double speedModifier;
	public final double conversionRate;
	public final double maxInjectedWill;

	public ItemCrystalCatalyst(EnumDemonWillType type, double injectedWill, double speedModifier, double conversionRate, double maxInjectedWill)
	{
		super(new Item.Properties().tab(BloodMagic.TAB));
		this.type = type;
		this.injectedWill = injectedWill;
		this.speedModifier = speedModifier;
		this.conversionRate = conversionRate;
		this.maxInjectedWill = maxInjectedWill;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flag)
	{
		tooltip.add(new TranslatableComponent("tooltip.bloodmagic.crystalCatalyst").withStyle(ChatFormatting.GRAY));
	}

	@Override
	public InteractionResult useOn(UseOnContext context)
	{
		ItemStack stack = context.getItemInHand();
		BlockPos pos = context.getClickedPos();
		Level world = context.getLevel();

		BlockEntity tile = world.getBlockEntity(pos);
		if (tile instanceof TileDemonCrystal)
		{
			if (!world.isClientSide)
			{
				TileDemonCrystal crystalTile = (TileDemonCrystal) tile;

				if (applyCatalyst(crystalTile))
				{
					ServerLevel server = (ServerLevel) world;
//					EnumDemonWillType type = state.getValue(BlockDemonCrystal.TYPE);
					ItemStack crystalStack = BlockDemonCrystal.getItemStackDropped(type, 1);

					ItemParticleOption particleData = new ItemParticleOption(ParticleTypes.ITEM, crystalStack);
					for (int i = 0; i < 8; ++i)
					{
						server.sendParticles(particleData, pos.getX() + 0.5, pos.getY() + 0.6, pos.getZ() + 0.5, 1, 0.2, 0.2, 0.2, 0.03);
					}
					stack.setCount(stack.getCount() - 1);
					world.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1, 1);
				}
			}

			return InteractionResult.SUCCESS;
		}

		return InteractionResult.FAIL;
	}

	public boolean applyCatalyst(TileDemonCrystal crystalTile)
	{
		if (type.equals(crystalTile.getWillType()) && (crystalTile.getInjectedWill() + injectedWill) <= maxInjectedWill)
		{
			crystalTile.applyCatalyst(injectedWill, speedModifier, conversionRate);
			return true;
		}

		return false;
	}
}
