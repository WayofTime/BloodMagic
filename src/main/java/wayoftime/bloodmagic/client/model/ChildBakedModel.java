package wayoftime.bloodmagic.client.model;

import java.util.List;
import java.util.Random;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.state.BlockState;

public class ChildBakedModel implements BakedModel
{
	public final BakedModel baseModel;
	public final BakedModel heldModel;

	public ChildBakedModel(BakedModel baseModel, BakedModel heldModel)
	{
		this.baseModel = baseModel;
		this.heldModel = heldModel;
	}

	@Override
	public BakedModel applyTransform(ItemDisplayContext transformType, PoseStack poseStack, boolean applyLeftHandTransform) {
		switch (transformType)
		{
		case FIRST_PERSON_LEFT_HAND:
		case FIRST_PERSON_RIGHT_HAND:
		case FIXED:
		case GROUND:
		case HEAD:
		case THIRD_PERSON_LEFT_HAND:
		case THIRD_PERSON_RIGHT_HAND:
		default:
			// TODO: Change to cacheing system.
			return net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(poseStack, heldModel, transformType, applyLeftHandTransform);
		case NONE:
		case GUI:
			return net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(poseStack, baseModel, transformType, applyLeftHandTransform);
		}
	}

	@Override
	public List<BakedQuad> getQuads(BlockState p_119123_, Direction p_119124_, RandomSource p_119125_)
	{
		return baseModel.getQuads(p_119123_, p_119124_, p_119125_);
	}

	@Override
	public boolean usesBlockLight()
	{
		return baseModel.usesBlockLight();
	}

	@Override
	public boolean useAmbientOcclusion()
	{
		return true;
	}

	@Override
	public boolean isGui3d()
	{
		return false;
	}

	@Override
	public boolean isCustomRenderer()
	{
		return true;
	}

	@Override
	public TextureAtlasSprite getParticleIcon()
	{
		return baseModel.getParticleIcon();
	}

	@Override
	public ItemOverrides getOverrides()
	{
		return baseModel.getOverrides();
	}

	@Override
	public ItemTransforms getTransforms()
	{
		return ItemTransforms.NO_TRANSFORMS;
	}
}
