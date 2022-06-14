package wayoftime.bloodmagic.common.item;

import java.util.List;

import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.ConfigManager;
import wayoftime.bloodmagic.event.SacrificeKnifeUsedEvent;
import wayoftime.bloodmagic.util.Constants;
import wayoftime.bloodmagic.util.DamageSourceBloodMagic;
import wayoftime.bloodmagic.util.helper.IncenseHelper;
import wayoftime.bloodmagic.util.helper.NBTHelper;
import wayoftime.bloodmagic.util.helper.PlayerHelper;
import wayoftime.bloodmagic.util.helper.PlayerSacrificeHelper;

public class ItemSacrificialDagger extends Item
{

	public ItemSacrificialDagger()
	{
		super(new Item.Properties().stacksTo(1).tab(BloodMagic.TAB));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flag)
	{
//		tooltip.addAll(Arrays.asList(TextHelper.cutLongString(TextHelper.localizeEffect("tooltip.bloodmagic.sacrificialDagger.desc"))));
		tooltip.add(new TranslatableComponent("tooltip.bloodmagic.sacrificialdagger.desc").withStyle(ChatFormatting.GRAY));

//		if (stack.getItemDamage() == 1)
//			list.add(TextHelper.localizeEffect("tooltip.bloodmagic.sacrificialDagger.creative"));
	}

	@Override
	public void releaseUsing(ItemStack stack, Level worldIn, LivingEntity entityLiving, int timeLeft)
	{
		if (entityLiving instanceof Player && !entityLiving.getCommandSenderWorld().isClientSide)
			if (PlayerSacrificeHelper.sacrificePlayerHealth((Player) entityLiving))
				IncenseHelper.setHasMaxIncense(stack, (Player) entityLiving, false);
	}

	@Override
	public boolean isFoil(ItemStack stack)
	{
		return IncenseHelper.getHasMaxIncense(stack) || super.isFoil(stack);
	}

	@Override
	public int getUseDuration(ItemStack stack)
	{
		return 72000;
	}

	@Override
	public UseAnim getUseAnimation(ItemStack stack)
	{
		return UseAnim.BOW;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand)
	{
		ItemStack stack = player.getItemInHand(hand);
		if (PlayerHelper.isFakePlayer(player))
			return super.use(world, player, hand);

		if (this.canUseForSacrifice(stack))
		{
			player.startUsingItem(hand);
			return InteractionResultHolder.success(stack);
		}

		int lpAdded = ConfigManager.COMMON.sacrificialDaggerConversion.get() * 2;

//		RayTraceResult rayTrace = rayTrace(world, player, false);
//		if (rayTrace != null && rayTrace.typeOfHit == RayTraceResult.Type.BLOCK)
//		{
//			TileEntity tile = world.getTileEntity(rayTrace.getBlockPos());
//
//			if (tile != null && tile instanceof TileAltar && stack.getItemDamage() == 1)
//				lpAdded = ((TileAltar) tile).getCapacity();
//		}

		if (!player.abilities.instabuild)
		{
			SacrificeKnifeUsedEvent evt = new SacrificeKnifeUsedEvent(player, true, true, 2, lpAdded);
			if (MinecraftForge.EVENT_BUS.post(evt))
				return super.use(world, player, hand);

			if (evt.shouldDrainHealth)
			{
				player.invulnerableTime = 0;

				player.hurt(DamageSourceBloodMagic.INSTANCE, 0.001F);
				player.setHealth(Math.max(player.getHealth() - 1.998F, 0.0001f));
				if (player.getHealth() <= 0.001f && !world.isClientSide)
				{
					player.invulnerableTime = 0;
					player.hurt(DamageSourceBloodMagic.INSTANCE, 10);
				}
			}

			if (!evt.shouldFillAltar)
				return super.use(world, player, hand);

			lpAdded = evt.lpAdded;
		} else if (player.isShiftKeyDown())
		{
			lpAdded = Integer.MAX_VALUE;
		}

		double posX = player.getX();
		double posY = player.getY();
		double posZ = player.getZ();
		world.playSound(player, posX, posY, posZ, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 2.6F + (world.random.nextFloat() - world.random.nextFloat()) * 0.8F);

		for (int l = 0; l < 8; ++l)
			world.addParticle(DustParticleOptions.REDSTONE, posX + Math.random() - Math.random(), posY + Math.random() - Math.random(), posZ + Math.random() - Math.random(), 0, 0, 0);

		if (!world.isClientSide && PlayerHelper.isFakePlayer(player))
			return super.use(world, player, hand);

		// TODO - Check if SoulFray is active
		PlayerSacrificeHelper.findAndFillAltar(world, player, lpAdded, false);

		return super.use(world, player, hand);
	}

	@Override
	public void inventoryTick(ItemStack stack, Level world, Entity entity, int itemSlot, boolean isSelected)
	{
		if (!world.isClientSide && entity instanceof Player)
			this.setUseForSacrifice(stack, this.isPlayerPreparedForSacrifice(world, (Player) entity));
	}

	public boolean isPlayerPreparedForSacrifice(Level world, Player player)
	{
		return !world.isClientSide && (PlayerSacrificeHelper.getPlayerIncense(player) > 0);
	}

	public boolean canUseForSacrifice(ItemStack stack)
	{
		stack = NBTHelper.checkNBT(stack);
		return stack.getTag().getBoolean(Constants.NBT.SACRIFICE);
	}

	public void setUseForSacrifice(ItemStack stack, boolean sacrifice)
	{
		stack = NBTHelper.checkNBT(stack);
		stack.getTag().putBoolean(Constants.NBT.SACRIFICE, sacrifice);
	}

//	@Override
//	@SideOnly(Side.CLIENT)
//	public ItemMeshDefinition getMeshDefinition()
//	{
//		return stack -> {
//			String variant = "type=normal";
//			if (stack.getItemDamage() != 0)
//				variant = "type=creative";
//
//			if (canUseForSacrifice(stack))
//				variant = "type=ceremonial";
//
//			return new ModelResourceLocation(getRegistryName(), variant);
//		};
//	}
//
//	@Override
//	public void gatherVariants(Consumer<String> variants)
//	{
//		variants.accept("type=normal");
//		variants.accept("type=creative");
//		variants.accept("type=ceremonial");
//	}
//
//	public enum DaggerType implements ISubItem
//	{
//
//		NORMAL, CREATIVE,;
//
//		@Nonnull
//		@Override
//		public String getInternalName()
//		{
//			return name().toLowerCase(Locale.ROOT);
//		}
//
//		@Nonnull
//		@Override
//		public ItemStack getStack(int count)
//		{
//			return new ItemStack(RegistrarBloodMagicItems.SACRIFICIAL_DAGGER, count, ordinal());
//		}
//	}
}