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
		super(new Item.Properties().maxStackSize(64).group(BloodMagic.TAB));

		this.tooltipBase = "tooltip.bloodmagic.blood_provider." + name + ".";
		this.lpProvided = lpProvided;
	}

	public ItemBloodProvider(String name)
	{
		this(name, 0);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand)
	{
		ItemStack stack = player.getHeldItem(hand);
		if (PlayerHelper.isFakePlayer(player))
			return super.onItemRightClick(world, player, hand);

		IBloodAltar altarEntity = PlayerSacrificeHelper.getAltar(world, player.getPosition());
		if (altarEntity != null)
		{
			double posX = player.getPosX();
			double posY = player.getPosY();
			double posZ = player.getPosZ();
			world.playSound(player, posX, posY, posZ, SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.BLOCKS, 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);

			for (int l = 0; l < 8; ++l)
				world.addParticle(RedstoneParticleData.REDSTONE_DUST, posX + Math.random() - Math.random(), posY + Math.random() - Math.random(), posZ + Math.random() - Math.random(), 0, 0, 0);

			if (!world.isRemote && PlayerHelper.isFakePlayer(player))
				return super.onItemRightClick(world, player, hand);

			altarEntity.fillMainTank(lpProvided);
			if (!player.isCreative())
			{
				stack.shrink(1);
			}
		}

		return super.onItemRightClick(world, player, hand);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag flag)
	{
		tooltip.add(new TranslationTextComponent(tooltipBase + "desc").mergeStyle(TextFormatting.ITALIC).mergeStyle(TextFormatting.GRAY));

		super.addInformation(stack, world, tooltip, flag);
	}
}