package WayofTime.bloodmagic.client.render.entity;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerArrow;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import WayofTime.bloodmagic.entity.mob.EntitySentientSpecter;

@SideOnly(Side.CLIENT)
public class RenderSentientSpecter extends RenderBiped<EntitySentientSpecter>
{
    public static final ResourceLocation texture = new ResourceLocation("bloodmagic", "textures/entities/specter.png");

    public RenderSentientSpecter(RenderManager renderManager)
    {
        super(renderManager, new ModelBiped(0.0F), 0.1F);
        this.addLayer(new LayerBipedArmor(this));
        this.addLayer(new LayerHeldItem(this));
        this.addLayer(new LayerArrow(this));
        this.addLayer(new LayerCustomHead(this.getMainModel().bipedHead));
    }

    public ModelBiped getMainModel()
    {
        return (ModelBiped) super.getMainModel();
    }

    /**
     * Renders the desired {@code T} type Entity.
     */
    public void doRender(EntitySentientSpecter entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
//        if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.RenderPlayerEvent.Pre(entity, this, partialTicks, x, y, z)))
//            return;
//        if (!entity.isUser() || this.renderManager.renderViewEntity == entity)
        {
            double d0 = y;

            this.setModelVisibilities(entity);
//            GlStateManager.enableBlendProfile(GlStateManager.Profile.PLAYER_SKIN);
            super.doRender(entity, x, d0, z, entityYaw, partialTicks);
//            GlStateManager.disableBlendProfile(GlStateManager.Profile.PLAYER_SKIN);
        }
//        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.RenderPlayerEvent.Post(entity, this, partialTicks, x, y, z));
    }

    private void setModelVisibilities(EntitySentientSpecter clientPlayer)
    {
        ModelBiped modelplayer = this.getMainModel();

        ItemStack itemstack = clientPlayer.getHeldItemMainhand();
        ItemStack itemstack1 = clientPlayer.getHeldItemOffhand();
        modelplayer.setInvisible(true);

        modelplayer.isSneak = clientPlayer.isSneaking();
        ModelBiped.ArmPose modelbiped$armpose = ModelBiped.ArmPose.EMPTY;
        ModelBiped.ArmPose modelbiped$armpose1 = ModelBiped.ArmPose.EMPTY;

        if (itemstack != null)
        {
            modelbiped$armpose = ModelBiped.ArmPose.ITEM;

            if (clientPlayer.getItemInUseCount() > 0)
            {
                EnumAction enumaction = itemstack.getItemUseAction();

                if (enumaction == EnumAction.BLOCK)
                {
                    modelbiped$armpose = ModelBiped.ArmPose.BLOCK;
                } else if (enumaction == EnumAction.BOW)
                {
                    modelbiped$armpose = ModelBiped.ArmPose.BOW_AND_ARROW;
                }
            }
        }

        if (itemstack1 != null)
        {
            modelbiped$armpose1 = ModelBiped.ArmPose.ITEM;

            if (clientPlayer.getItemInUseCount() > 0)
            {
                EnumAction enumaction1 = itemstack1.getItemUseAction();

                if (enumaction1 == EnumAction.BLOCK)
                {
                    modelbiped$armpose1 = ModelBiped.ArmPose.BLOCK;
                }
            }
        }

        if (clientPlayer.getPrimaryHand() == EnumHandSide.RIGHT)
        {
            modelplayer.rightArmPose = modelbiped$armpose;
            modelplayer.leftArmPose = modelbiped$armpose1;
        } else
        {
            modelplayer.rightArmPose = modelbiped$armpose1;
            modelplayer.leftArmPose = modelbiped$armpose;
        }

    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called
     * unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntitySentientSpecter entity)
    {
        return texture;
    }

    public void transformHeldFull3DItemLayer()
    {
        GlStateManager.translate(0.0F, 0.1875F, 0.0F);
    }

    /**
     * Allows the render to do state modifications necessary before the model is
     * rendered.
     */
    protected void preRenderCallback(EntitySentientSpecter entitylivingbaseIn, float partialTickTime)
    {
        float f = 0.9375F;
        GlStateManager.scale(0.9375F, 0.9375F, 0.9375F);
    }

//    public void renderRightArm(EntitySentientSpecter clientPlayer)
//    {
//        float f = 1.0F;
//        GlStateManager.color(1.0F, 1.0F, 1.0F);
//        float f1 = 0.0625F;
//        ModelBiped modelplayer = this.getMainModel();
//        this.setModelVisibilities(clientPlayer);
//        GlStateManager.enableBlend();
//        modelplayer.swingProgress = 0.0F;
//        modelplayer.isSneak = false;
//        modelplayer.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, clientPlayer);
//        modelplayer.bipedRightArm.rotateAngleX = 0.0F;
//        modelplayer.bipedRightArm.render(0.0625F);
//        modelplayer.bipedRightArmwear.rotateAngleX = 0.0F;
//        modelplayer.bipedRightArmwear.render(0.0625F);
//        GlStateManager.disableBlend();
//    }
//
//    public void renderLeftArm(EntitySentientSpecter clientPlayer)
//    {
//        float f = 1.0F;
//        GlStateManager.color(1.0F, 1.0F, 1.0F);
//        float f1 = 0.0625F;
//        ModelBiped modelplayer = this.getMainModel();
//        this.setModelVisibilities(clientPlayer);
//        GlStateManager.enableBlend();
//        modelplayer.isSneak = false;
//        modelplayer.swingProgress = 0.0F;
//        modelplayer.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, clientPlayer);
//        modelplayer.bipedLeftArm.rotateAngleX = 0.0F;
//        modelplayer.bipedLeftArm.render(0.0625F);
//        modelplayer.bipedLeftArmwear.rotateAngleX = 0.0F;
//        modelplayer.bipedLeftArmwear.render(0.0625F);
//        GlStateManager.disableBlend();
//    }
}