package wayoftime.bloodmagic.common.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.anointment.Anointment;

public class ItemAnointmentProvider extends Item
{
	public Anointment anointment;

	public ItemAnointmentProvider(Anointment anointment)
	{
		super(new Item.Properties().maxStackSize(16).group(BloodMagic.TAB));

		this.anointment = anointment;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand)
	{
		ItemStack stack = player.getHeldItem(hand);
		ItemStack weaponStack = player.getHeldItem(hand == Hand.MAIN_HAND ? Hand.OFF_HAND : Hand.MAIN_HAND);

//		if (world.isRemote && !unusable)
//		{
//			Vector3d vec = player.getLookVec();
//			double wantedVelocity = 1.7;
//
//			// TODO - Revisit after potions
////			if (player.isPotionActive(RegistrarBloodMagic.BOOST))
////			{
////				int amplifier = player.getActivePotionEffect(RegistrarBloodMagic.BOOST).getAmplifier();
////				wantedVelocity += (1 + amplifier) * (0.35);
////			}
//
//			player.setMotion(vec.x * wantedVelocity, vec.y * wantedVelocity, vec.z * wantedVelocity);
//		}
//
//		world.playSound(player, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat())
//				* 0.8F);

		if (!world.isRemote)
		{
			if (!weaponStack.isEmpty() && isItemValidForApplication(weaponStack))
			{
				ToolType d;
			}
		}

		return super.onItemRightClick(world, player, hand);
	}

	public boolean isItemValidForApplication(ItemStack stack)
	{
		return isItemTool(stack) || isItemSword(stack);
	}

	public static boolean isItemTool(ItemStack stack)
	{
		return !stack.getItem().getToolTypes(stack).isEmpty();
	}

	public static boolean isItemSword(ItemStack stack)
	{
		return stack.getItem() instanceof SwordItem;
	}
}
