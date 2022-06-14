package wayoftime.bloodmagic.common.item;

import java.util.List;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.altar.IBloodAltar;
import wayoftime.bloodmagic.util.helper.PlayerHelper;
import wayoftime.bloodmagic.util.helper.PlayerSacrificeHelper;

public class ItemBloodProvider extends Item
{
	protected final String tooltipBase;
	public final int lpProvided;

	public ItemBloodProvider(String name, int lpProvided)
	{
		super(new Item.Properties().stacksTo(64).tab(BloodMagic.TAB));

		this.tooltipBase = "tooltip.bloodmagic.blood_provider." + name + ".";
		this.lpProvided = lpProvided;
	}

	public ItemBloodProvider(String name)
	{
		this(name, 0);
	}

	@Override
	public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand)
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
			world.playSound(player, posX, posY, posZ, SoundEvents.GLASS_BREAK, SoundCategory.BLOCKS, 0.5F, 2.6F + (world.random.nextFloat() - world.random.nextFloat()) * 0.8F);

			for (int l = 0; l < 8; ++l)
				world.addParticle(RedstoneParticleData.REDSTONE, posX + Math.random() - Math.random(), posY + Math.random() - Math.random(), posZ + Math.random() - Math.random(), 0, 0, 0);

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
	public void appendHoverText(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag flag)
	{
		tooltip.add(new TranslationTextComponent(tooltipBase + "desc").withStyle(TextFormatting.ITALIC).withStyle(TextFormatting.GRAY));

		super.appendHoverText(stack, world, tooltip, flag);
	}
}