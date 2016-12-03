package WayofTime.bloodmagic.util.handler.event;

import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.block.BlockBloodTank;
import WayofTime.bloodmagic.client.render.block.RenderBloodTank;
import WayofTime.bloodmagic.tile.TileBloodTank;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.apache.commons.lang3.tuple.Pair;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;
import java.util.List;
import java.util.Map;

/**
 * Shamelessly taken off of the Mekanism repository written by {@author aidancbrady}
 * https://github.com/aidancbrady/Mekanism
 */
public class BakedCustomItemModel implements IPerspectiveAwareModel
{
    private IBakedModel baseModel;
    private ItemStack stack;

    private TransformType prevTransform;

    private static VertexFormat prevFormat = null;
    private static int prevMode = -1;

    private static final RenderBloodTank tankRenderer = (RenderBloodTank) TileEntityRendererDispatcher.instance.mapSpecialRenderers.get(TileBloodTank.class);

    public BakedCustomItemModel(IBakedModel model, ItemStack stack)
    {
        this.baseModel = model;
        this.stack = stack;
    }

    private void doRender(TransformType transformType)
    {
        Block block = Block.getBlockFromItem(stack.getItem());

        if (transformType == TransformType.GUI)
        {
            GlStateManager.rotate(180F, 0.0F, 1.0F, 0.0F);
        }

        if (block instanceof BlockBloodTank)
        {
            GlStateManager.pushMatrix();

            FluidStack fluid = null;
            float capacity = TileBloodTank.capacities[stack.getItemDamage()] * Fluid.BUCKET_VOLUME;
            int amount = 0;
            if (stack.hasTagCompound() && stack.getTagCompound().hasKey(Constants.NBT.TANK))
            {
                NBTTagCompound tag = stack.getTagCompound().getCompoundTag(Constants.NBT.TANK);
                fluid = FluidStack.loadFluidStackFromNBT(tag);
                if (fluid != null)
                    amount = tag.getInteger("Amount");
            }

            if (fluid != null && amount > 0)
                tankRenderer.renderFluid(amount / capacity, fluid.getFluid(), -0.498, -0.49, -0.498);

            GlStateManager.popMatrix();
        }
    }

    @Override
    public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand)
    {
        Tessellator tessellator = Tessellator.getInstance();
        pauseRenderer(tessellator);

        GlStateManager.pushMatrix();
        GlStateManager.translate(0.5F, 0.5F, 0.5F);
        GlStateManager.rotate(180, 0.0F, 1.0F, 0.0F);
        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        doRender(prevTransform);
        GlStateManager.enableLighting();
        GlStateManager.enableLight(0);
        GlStateManager.enableLight(1);
        GlStateManager.enableColorMaterial();
        GlStateManager.colorMaterial(1032, 5634);
        GlStateManager.enableCull();
        GlStateManager.popMatrix();

        resumeRenderer(tessellator);

        return baseModel.getQuads(state, side, rand);
    }

    @Override
    public boolean isAmbientOcclusion()
    {
        return baseModel.isAmbientOcclusion();
    }

    @Override
    public boolean isGui3d()
    {
        return baseModel.isGui3d();
    }

    @Override
    public boolean isBuiltInRenderer()
    {
        return baseModel.isBuiltInRenderer();
    }

    @Override
    public TextureAtlasSprite getParticleTexture()
    {
        return baseModel.getParticleTexture();
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms()
    {
        return baseModel.getItemCameraTransforms();
    }

    private static TRSRTransformation get(float tx, float ty, float tz, float ax, float ay, float az, float s)
    {
        return new TRSRTransformation(new Vector3f(tx / 16, ty / 16, tz / 16), TRSRTransformation.quatFromXYZDegrees(new Vector3f(ax, ay, az)), new Vector3f(s, s, s), null);
    }

    public static Map<TransformType, TRSRTransformation> transforms = ImmutableMap.<TransformType, TRSRTransformation>builder()
            .put(TransformType.GUI,                         get(0, 0, 0, 30F, 225F, 0, 0.625F))
            .put(TransformType.THIRD_PERSON_RIGHT_HAND,     get(0, 2.5F, 0, 75F, 45F, 0, 0.375F))
            .put(TransformType.THIRD_PERSON_LEFT_HAND,      get(0, 2.5F, 0, 75F, 45F, 0, 0.375F))
            .put(TransformType.FIRST_PERSON_RIGHT_HAND,     get(0, 0, 0, 0, 45F, 0, 0.4f))
            .put(TransformType.FIRST_PERSON_LEFT_HAND,      get(0, 0, 0, 0, 225F, 0, 0.4F))
            .put(TransformType.GROUND,                      get(0, 2F, 0, 0, 0, 0, 0.25F))
            .put(TransformType.HEAD,                        get(0, 0, 0, 0, 0, 0, 1F))
            .put(TransformType.FIXED,                       get(0, 0, 0, 0, 0, 0, 1F))
            .put(TransformType.NONE,                        get(0, 0, 0, 0, 0, 0, 0))
            .build();

    @Override
    public Pair<? extends IPerspectiveAwareModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType)
    {
        prevTransform = cameraTransformType;
        return Pair.of(this, transforms.get(cameraTransformType).getMatrix());
    }

    @Override
    public ItemOverrideList getOverrides()
    {
        return ItemOverrideList.NONE;
    }

    public static void pauseRenderer(Tessellator tess)
    {
        if (isDrawing(tess))
        {
            prevFormat = tess.getBuffer().getVertexFormat();
            prevMode = tess.getBuffer().getDrawMode();
            tess.draw();
        }
    }

    public static void resumeRenderer(Tessellator tess)
    {
        if (prevFormat != null)
        {
            tess.getBuffer().begin(prevMode, prevFormat);
        }

        prevFormat = null;
        prevMode = -1;
    }

    public static boolean isDrawing(Tessellator tess)
    {
        return isDrawing(tess.getBuffer());
    }

    public static boolean isDrawing(VertexBuffer buffer)
    {
        return (Boolean) ReflectionHelper.getPrivateValue(VertexBuffer.class, buffer, "isDrawing", "field_179010_r");
    }
}
