package wayoftime.bloodmagic.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.altar.IBloodAltar;
import wayoftime.bloodmagic.util.helper.PlayerHelper;
import wayoftime.bloodmagic.util.helper.PlayerSacrificeHelper;

import java.util.List;

public class ItemBloodProvider extends Item
{
	protected final String tooltipBase;
	public final int lpProvided;

	public ItemBloodProvider(String name, int lpProvided)
	{
		super(new Item.Properties().stacksTo(64));

		this.tooltipBase = "tooltip.bloodmagic.blood_provider." + name + ".";
		this.lpProvided = lpProvided;
	}

	public ItemBloodProvider(String name)
	{
		this(name, 0);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand)
	{
		ItemStack stack = player.getItemInHand(hand);
		if (PlayerHelper.isFakePlayer(player))
			return super.use(world, player, hand);

		IBloodAltar altarEntity = PlayerSacrificeHelper.getAltar(world, player.blockPosition());
		if (altarEntity != null)
		{
			double posX = player.getX();
			double posY = player.getY();
			double posZ = player.getZ();
			world.playSound(player, posX, posY, posZ, SoundEvents.GLASS_BREAK, SoundSource.BLOCKS, 0.5F, 2.6F + (world.random.nextFloat() - world.random.nextFloat()) * 0.8F);

			for (int l = 0; l < 8; ++l)
				world.addParticle(DustParticleOptions.REDSTONE, posX + Math.random() - Math.random(), posY + Math.random() - Math.random(), posZ + Math.random() - Math.random(), 0, 0, 0);

			if (!world.isClientSide && PlayerHelper.isFakePlayer(player))
				return super.use(world, player, hand);

			altarEntity.fillMainTank(lpProvided);
			if (!player.isCreative())
			{
				stack.shrink(1);
			}
		}

		return super.use(world, player, hand);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flag)
	{
		tooltip.add(Component.translatable(tooltipBase + "desc").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY));

		super.appendHoverText(stack, world, tooltip, flag);
	}
}