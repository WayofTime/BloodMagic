package wayoftime.bloodmagic.common.item;

import java.util.List;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.ConfigHandler;
import wayoftime.bloodmagic.event.SacrificeKnifeUsedEvent;
import wayoftime.bloodmagic.util.Constants;
import wayoftime.bloodmagic.util.DamageSourceBloodMagic;
import wayoftime.bloodmagic.util.helper.NBTHelper;
import wayoftime.bloodmagic.util.helper.PlayerHelper;
import wayoftime.bloodmagic.util.helper.PlayerSacrificeHelper;

public class ItemSacrificialDagger extends Item
{

	public ItemSacrificialDagger()
	{
		super(new Item.Properties().maxStackSize(1).group(BloodMagic.TAB));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag flag)
	{
//		tooltip.addAll(Arrays.asList(TextHelper.cutLongString(TextHelper.localizeEffect("tooltip.bloodmagic.sacrificialDagger.desc"))));
		tooltip.add(new TranslationTextComponent("tooltip.bloodmagic.sacrificialdagger.desc"));

//		if (stack.getItemDamage() == 1)
//			list.add(TextHelper.localizeEffect("tooltip.bloodmagic.sacrificialDagger.creative"));
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft)
	{
		if (entityLiving instanceof PlayerEntity && !entityLiving.getEntityWorld().isRemote)
			PlayerSacrificeHelper.sacrificePlayerHealth((PlayerEntity) entityLiving);
	}

	@Override
	public int getUseDuration(ItemStack stack)
	{
		return 72000;
	}

	@Override
	public UseAction getUseAction(ItemStack stack)
	{
		return UseAction.BOW;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand)
	{
		ItemStack stack = player.getHeldItem(hand);
		if (PlayerHelper.isFakePlayer(player))
			return super.onItemRightClick(world, player, hand);

		if (this.canUseForSacrifice(stack))
		{
			player.setActiveHand(hand);
			return ActionResult.resultSuccess(stack);
		}

		int lpAdded = ConfigHandler.values.sacrificialDaggerConversion * 2;

//		RayTraceResult rayTrace = rayTrace(world, player, false);
//		if (rayTrace != null && rayTrace.typeOfHit == RayTraceResult.Type.BLOCK)
//		{
//			TileEntity tile = world.getTileEntity(rayTrace.getBlockPos());
//
//			if (tile != null && tile instanceof TileAltar && stack.getItemDamage() == 1)
//				lpAdded = ((TileAltar) tile).getCapacity();
//		}

		if (!player.abilities.isCreativeMode)
		{
			SacrificeKnifeUsedEvent evt = new SacrificeKnifeUsedEvent(player, true, true, 2, lpAdded);
			if (MinecraftForge.EVENT_BUS.post(evt))
				return super.onItemRightClick(world, player, hand);

			if (evt.shouldDrainHealth)
			{
				player.hurtResistantTime = 0;
				player.attackEntityFrom(DamageSourceBloodMagic.INSTANCE, 0.001F);
				player.setHealth(Math.max(player.getHealth() - 1.998F, 0.0001f));
				if (player.getHealth() <= 0.001f && !world.isRemote)
				{
					player.onDeath(DamageSourceBloodMagic.INSTANCE);
					player.setHealth(0);
				}
//                player.attackEntityFrom(BloodMagicAPI.getDamageSource(), 2.0F);
			}

			if (!evt.shouldFillAltar)
				return super.onItemRightClick(world, player, hand);

			lpAdded = evt.lpAdded;
		}

		double posX = player.getPosX();
		double posY = player.getPosY();
		double posZ = player.getPosZ();
		world.playSound(null, posX, posY, posZ, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F
				+ (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);

		for (int l = 0; l < 8; ++l) world.addParticle(RedstoneParticleData.REDSTONE_DUST, posX + Math.random()
				- Math.random(), posY + Math.random() - Math.random(), posZ + Math.random() - Math.random(), 0, 0, 0);

		if (!world.isRemote && PlayerHelper.isFakePlayer(player))
			return super.onItemRightClick(world, player, hand);

		// TODO - Check if SoulFray is active
		PlayerSacrificeHelper.findAndFillAltar(world, player, lpAdded, false);

		return super.onItemRightClick(world, player, hand);
	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected)
	{
		if (!world.isRemote && entity instanceof PlayerEntity)
			this.setUseForSacrifice(stack, this.isPlayerPreparedForSacrifice(world, (PlayerEntity) entity));
	}

	public boolean isPlayerPreparedForSacrifice(World world, PlayerEntity player)
	{
		return !world.isRemote && (PlayerSacrificeHelper.getPlayerIncense(player) > 0);
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