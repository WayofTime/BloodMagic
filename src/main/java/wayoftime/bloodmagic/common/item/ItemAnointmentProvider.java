package wayoftime.bloodmagic.common.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.anointment.AnointmentData;
import wayoftime.bloodmagic.anointment.AnointmentHolder;
import wayoftime.bloodmagic.core.AnointmentRegistrar;

public class ItemAnointmentProvider extends Item
{
//	public Anointment anointment;
	public ResourceLocation anointRL;
	private int colour;
	private int level;
	private int maxDamage;

	public ItemAnointmentProvider(ResourceLocation anointRL, int colour, int level, int maxDamage)
	{
		super(new Item.Properties().maxStackSize(16).group(BloodMagic.TAB));
		this.anointRL = anointRL;
		this.colour = colour;
		this.level = level;
		this.maxDamage = maxDamage;
//		this.anointment = anointment;
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
				AnointmentHolder holder = AnointmentHolder.fromItemStack(weaponStack);
				if (holder == null)
				{
					holder = new AnointmentHolder();
				}

				if (holder.applyAnointment(weaponStack, AnointmentRegistrar.ANOINTMENT_MAP.get(anointRL), new AnointmentData(level, 0, maxDamage)))
				{
//					if (world instanceof ServerWorld)
					{
						SoundEvent soundevent = SoundEvents.ITEM_BOTTLE_EMPTY;
						world.playSound(null, player.getPosition(), soundevent, SoundCategory.BLOCKS, 1.0F, 1.0F);
					}
					stack.shrink(1);
					holder.toItemStack(weaponStack);
					return ActionResult.resultSuccess(stack);
				}
			}
		} else
		{
			if (!weaponStack.isEmpty() && isItemValidForApplication(weaponStack))
			{
				AnointmentHolder holder = AnointmentHolder.fromItemStack(weaponStack);
				if (holder == null)
				{
					holder = new AnointmentHolder();
				}
				if (holder.canApplyAnointment(weaponStack, AnointmentRegistrar.ANOINTMENT_MAP.get(anointRL), new AnointmentData(level, 0, maxDamage)))
				{
					boolean flag1 = false;
					double d0 = (double) (colour >> 16 & 255) / 255.0D;
					double d1 = (double) (colour >> 8 & 255) / 255.0D;
					double d2 = (double) (colour >> 0 & 255) / 255.0D;
					for (int i = 0; i < 16; i++)
					{
						world.addParticle(flag1 ? ParticleTypes.AMBIENT_ENTITY_EFFECT
								: ParticleTypes.ENTITY_EFFECT, player.getPosXRandom(0.3D), player.getPosYRandom(), player.getPosZRandom(0.3D), d0, d1, d2);
					}
				}
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

	public int getColor()
	{
		return colour;
	}
}
