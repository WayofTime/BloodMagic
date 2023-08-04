package wayoftime.bloodmagic.common.item;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
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
		super(new Item.Properties().stacksTo(16));
		this.anointRL = anointRL;
		this.colour = colour;
		this.level = level;
		this.maxDamage = maxDamage;
//		this.anointment = anointment;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand)
	{
		ItemStack stack = player.getItemInHand(hand);
		ItemStack weaponStack = player.getItemInHand(hand == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND
				: InteractionHand.MAIN_HAND);
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

		if (!world.isClientSide)
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
						SoundEvent soundevent = SoundEvents.BOTTLE_EMPTY;
						world.playSound(null, player.blockPosition(), soundevent, SoundSource.BLOCKS, 1.0F, 1.0F);
					}
					stack.shrink(1);
					holder.toItemStack(weaponStack);
					return InteractionResultHolder.consume(stack);
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
								: ParticleTypes.ENTITY_EFFECT, player.getRandomX(0.3D), player.getRandomY(), player.getRandomZ(0.3D), d0, d1, d2);
					}

					return InteractionResultHolder.consume(stack);
				}
			}
		}

		return super.use(world, player, hand);
	}

	public boolean isItemValidForApplication(ItemStack stack)
	{
		return isItemTool(stack) || isItemSword(stack);
	}

	public static boolean isItemTool(ItemStack stack)
	{
		for (ToolAction action : validToolActions())
		{
			if (stack.canPerformAction(action))
			{
				return true;
			}
		}

		return false;
	}

	public static boolean isItemSword(ItemStack stack)
	{
		return stack.getItem() instanceof SwordItem;
	}

	public static List<ToolAction> validToolActions()
	{
		List<ToolAction> actionList = new ArrayList<>();
		actionList.add(ToolActions.AXE_DIG);
		actionList.add(ToolActions.SHOVEL_DIG);
		actionList.add(ToolActions.SWORD_DIG);
		actionList.add(ToolActions.PICKAXE_DIG);
		actionList.add(ToolActions.HOE_DIG);
		return actionList;
	}

	public int getColor()
	{
		return colour;
	}
}
