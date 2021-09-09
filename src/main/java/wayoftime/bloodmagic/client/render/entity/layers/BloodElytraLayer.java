package wayoftime.bloodmagic.client.render.entity.layers;

import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import wayoftime.bloodmagic.common.item.ItemLivingArmor;
import wayoftime.bloodmagic.core.LivingArmorRegistrar;
import wayoftime.bloodmagic.core.living.LivingStats;
import wayoftime.bloodmagic.core.living.LivingUtil;

public class BloodElytraLayer<T extends LivingEntity, M extends EntityModel<T>> extends ElytraLayer<T, M>
{
	private static final ResourceLocation TEXTURE_BLOOD_ELYTRA = new ResourceLocation("bloodmagic", "textures/entities/bloodelytra.png");

	public BloodElytraLayer(IEntityRenderer rendererIn)
	{
		super(rendererIn);
	}

	@Override
	public boolean shouldRender(ItemStack stack, T entity)
	{
		if (stack.getItem() instanceof ItemLivingArmor && entity instanceof PlayerEntity && LivingUtil.hasFullSet((PlayerEntity) entity))
			return LivingStats.fromPlayer((PlayerEntity) entity).getLevel(LivingArmorRegistrar.UPGRADE_ELYTRA.get().getKey()) > 0;
		return false;
	}

	@Override
	public ResourceLocation getElytraTexture(ItemStack stack, T entity)
	{
		return TEXTURE_BLOOD_ELYTRA;
	}
}
