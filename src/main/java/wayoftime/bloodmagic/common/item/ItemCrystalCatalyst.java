package wayoftime.bloodmagic.common.item;

import java.util.List;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.api.compat.EnumDemonWillType;
import wayoftime.bloodmagic.common.block.BlockDemonCrystal;
import wayoftime.bloodmagic.tile.TileDemonCrystal;

public class ItemCrystalCatalyst extends Item
{
	public final EnumDemonWillType type;
	public final double injectedWill;
	public final double speedModifier;
	public final double conversionRate;
	public final double maxInjectedWill;

	public ItemCrystalCatalyst(EnumDemonWillType type, double injectedWill, double speedModifier, double conversionRate, double maxInjectedWill)
	{
		super(new Item.Properties().group(BloodMagic.TAB));
		this.type = type;
		this.injectedWill = injectedWill;
		this.speedModifier = speedModifier;
		this.conversionRate = conversionRate;
		this.maxInjectedWill = maxInjectedWill;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag flag)
	{
		tooltip.add(new TranslationTextComponent("tooltip.bloodmagic.crystalCatalyst").mergeStyle(TextFormatting.GRAY));
	}

	@Override
	public ActionResultType onItemUse(ItemUseContext context)
	{
		ItemStack stack = context.getItem();
		BlockPos pos = context.getPos();
		World world = context.getWorld();

		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof TileDemonCrystal)
		{
			if (!world.isRemote)
			{
				TileDemonCrystal crystalTile = (TileDemonCrystal) tile;

				if (applyCatalyst(crystalTile))
				{
					ServerWorld server = (ServerWorld) world;
//					EnumDemonWillType type = state.getValue(BlockDemonCrystal.TYPE);
					ItemStack crystalStack = BlockDemonCrystal.getItemStackDropped(type, 1);

					ItemParticleData particleData = new ItemParticleData(ParticleTypes.ITEM, crystalStack);
					for (int i = 0; i < 8; ++i)
					{
						server.spawnParticle(particleData, pos.getX() + 0.5, pos.getY() + 0.6, pos.getZ() + 0.5, 1, 0.2, 0.2, 0.2, 0.03);
					}
					stack.setCount(stack.getCount() - 1);
					world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1, 1);
				}
			}

			return ActionResultType.SUCCESS;
		}

		return ActionResultType.FAIL;
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
