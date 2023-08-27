package wayoftime.bloodmagic.client.render.entity.layers;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import wayoftime.bloodmagic.common.item.ItemLivingArmor;
import wayoftime.bloodmagic.core.LivingArmorRegistrar;
import wayoftime.bloodmagic.core.living.ILivingContainer;
import wayoftime.bloodmagic.core.living.LivingStats;
import wayoftime.bloodmagic.core.living.LivingUtil;

public class BloodElytraLayer<T extends LivingEntity, M extends EntityModel<T>> extends ElytraLayer<T, M>
{
	private static final ResourceLocation TEXTURE_BLOOD_ELYTRA = new ResourceLocation("bloodmagic", "textures/entities/bloodelytra.png");

	public BloodElytraLayer(RenderLayerParent<T, M> p_174493_, EntityModelSet p_174494_)
	{
		super(p_174493_, p_174494_);
	}

	@Override
	public boolean shouldRender(ItemStack stack, T entity)
	{
		if (stack.getItem() instanceof ILivingContainer && entity instanceof Player && LivingUtil.hasFullSet((Player) entity))
			return LivingStats.fromPlayer((Player) entity, true).getLevel(LivingArmorRegistrar.UPGRADE_ELYTRA.get().getKey()) > 0;
		return false;
	}

	@Override
	public ResourceLocation getElytraTexture(ItemStack stack, T entity)
	{
		return TEXTURE_BLOOD_ELYTRA;
	}
}
