package WayofTime.bloodmagic.util.handler.event;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class CustomModelFactory implements IBakedModel
{
    private IBakedModel baseModel;

    private BlockOverrideList override = new BlockOverrideList();

    public CustomModelFactory(IBakedModel base)
    {
        baseModel = base;
    }

    @Override
    public ItemOverrideList getOverrides()
    {
        return override;
    }

    @Override
    public List<BakedQuad> getQuads(IBlockState state, EnumFacing facing, long rand)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isAmbientOcclusion()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isGui3d()
    {
        return baseModel.isGui3d();
    }

    @Override
    public boolean isBuiltInRenderer()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public TextureAtlasSprite getParticleTexture()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms()
    {
        throw new UnsupportedOperationException();
    }

    private class BlockOverrideList extends ItemOverrideList
    {
        public BlockOverrideList()
        {
            super(new ArrayList<ItemOverride>());
        }

        @Override
        public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, World world, EntityLivingBase entity)
        {
            return new BakedCustomItemModel(baseModel, stack);
        }
    }
}
