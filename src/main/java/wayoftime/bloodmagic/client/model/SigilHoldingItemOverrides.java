package wayoftime.bloodmagic.client.model;

import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import wayoftime.bloodmagic.common.item.sigil.ItemSigilHolding;

public class SigilHoldingItemOverrides extends ItemOverrides
{
	public final ResourceLocation baseModelLoc;
	public final BakedModel baseModel;

	public SigilHoldingItemOverrides(ResourceLocation baseModelLoc, BakedModel baseModel)
	{
		this.baseModelLoc = baseModelLoc;
		this.baseModel = baseModel;
	}

	@Override
	public BakedModel resolve(BakedModel model, ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int var)
	{
		Player player = entity instanceof Player ? (Player) entity : null;
		ItemStack sigilStack = ((ItemSigilHolding) stack.getItem()).getHeldItem(stack, player);

		BakedModel bakedModel = baseModel.getOverrides().resolve(baseModel, stack, level, entity, var);

		bakedModel = bakedModel == null
				? Minecraft.getInstance().getItemRenderer().getItemModelShaper().getModelManager().getMissingModel()
				: bakedModel;

		BakedModel sigilModel = null;

		if (sigilStack.isEmpty())
		{
			sigilModel = bakedModel;
		} else
		{
			sigilModel = Minecraft.getInstance().getItemRenderer().getModel(sigilStack, level, entity, var);
		}

		return new ChildBakedModel(bakedModel, sigilModel);
	}
}
