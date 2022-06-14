package wayoftime.bloodmagic.common.item.soul;

import java.util.List;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
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
import wayoftime.bloodmagic.entity.projectile.EntitySoulSnare;

public class ItemSoulSnare extends Item
{
	public static String[] names = { "base" };

	public ItemSoulSnare()
	{
		super(new Item.Properties().stacksTo(16).tab(BloodMagic.TAB));

//		setTranslationKey(BloodMagic.MODID + ".soulSnare.");
//		setCreativeTab(BloodMagic.TAB_BM);
//		setHasSubtypes(true);
//		setMaxStackSize(16);
	}

	@Override
	public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand hand)
	{
		ItemStack stack = playerIn.getItemInHand(hand);
		if (!playerIn.isCreative())
		{
			stack.shrink(1);
		}

		worldIn.playSound((PlayerEntity) null, playerIn.getX(), playerIn.getY(), playerIn.getZ(), SoundEvents.SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));

		if (!worldIn.isClientSide)
		{
//			System.out.println("Attempting to spawn");
			EntitySoulSnare snare = new EntitySoulSnare(worldIn, playerIn);
			snare.shootFromRotation(playerIn, playerIn.xRot, playerIn.yRot, 0.0F, 1.5F, 1.0F);
			worldIn.addFreshEntity(snare);
//			
//			SnowballEntity snowballentity = new SnowballEntity(worldIn, playerIn);
//	         snowballentity.setItem(itemstack);
//	         snowballentity.shootFromRotation(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 1.5F, 1.0F);
//	         worldIn.addEntity(snowballentity);
		}

		return new ActionResult<>(ActionResultType.SUCCESS, stack);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag flag)
	{
		tooltip.add(new TranslationTextComponent("tooltip.bloodmagic.soulSnare.desc").withStyle(TextFormatting.GRAY));

		super.appendHoverText(stack, world, tooltip, flag);
	}
}