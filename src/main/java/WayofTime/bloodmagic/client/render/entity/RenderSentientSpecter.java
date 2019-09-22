package WayofTime.bloodmagic.client.render.entity;

import WayofTime.bloodmagic.entity.mob.EntitySentientSpecter;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.layers.*;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.layers.HeadLayer;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.item.UseAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.HandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderSentientSpecter extends BipedRenderer<EntitySentientSpecter> {
    public static final ResourceLocation texture = new ResourceLocation("bloodmagic", "textures/entities/specter.png");

    public RenderSentientSpecter(EntityRendererManager renderManager) {
        super(renderManager, new ModelBiped(0.0F), 0);
        this.addLayer(new BipedArmorLayer(this));
        this.addLayer(new HeldItemLayer(this));
        this.addLayer(new ArrowLayer(this));
        this.addLayer(new HeadLayer(this.getMainModel().bipedHead));
    }

    public ModelBiped getMainModel() {
        return (ModelBiped) super.getMainModel();
    }

    /**
     * Renders the desired {@code T} type Entity.
     */
    public void doRender(EntitySentientSpecter entity, double x, double y, double z, float entityYaw, float partialTicks) {
        this.setModelVisibilities(entity);
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    private void setModelVisibilities(EntitySentientSpecter clientPlayer) {
        ModelBiped modelplayer = this.getMainModel();

        ItemStack itemstack = clientPlayer.getHeldItemMainhand();
        ItemStack itemstack1 = clientPlayer.getHeldItemOffhand();
        modelplayer.setVisible(false);

        modelplayer.isSneak = clientPlayer.isSneaking();
        ModelBiped.ArmPose modelbiped$armpose = ModelBiped.ArmPose.EMPTY;
        ModelBiped.ArmPose modelbiped$armpose1 = ModelBiped.ArmPose.EMPTY;

        if (!itemstack.isEmpty()) {
            modelbiped$armpose = ModelBiped.ArmPose.ITEM;

            if (clientPlayer.getItemInUseCount() > 0) {
                UseAction enumaction = itemstack.getItemUseAction();

                if (enumaction == UseAction.BLOCK) {
                    modelbiped$armpose = ModelBiped.ArmPose.BLOCK;
                } else if (enumaction == UseAction.BOW) {
                    modelbiped$armpose = ModelBiped.ArmPose.BOW_AND_ARROW;
                }
            }
        }

        if (!itemstack1.isEmpty()) {
            modelbiped$armpose1 = ModelBiped.ArmPose.ITEM;

            if (clientPlayer.getItemInUseCount() > 0) {
                UseAction enumaction1 = itemstack1.getItemUseAction();

                if (enumaction1 == UseAction.BLOCK) {
                    modelbiped$armpose1 = ModelBiped.ArmPose.BLOCK;
                }
            }
        }

        if (clientPlayer.getPrimaryHand() == HandSide.RIGHT) {
            modelplayer.rightArmPose = modelbiped$armpose;
            modelplayer.leftArmPose = modelbiped$armpose1;
        } else {
            modelplayer.rightArmPose = modelbiped$armpose1;
            modelplayer.leftArmPose = modelbiped$armpose;
        }

    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called
     * unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntitySentientSpecter entity) {
        return texture;
    }

    public void transformHeldFull3DItemLayer() {
        GlStateManager.translate(0.0F, 0.1875F, 0.0F);
    }

    /**
     * Allows the render to do state modifications necessary before the model is
     * rendered.
     */
    protected void preRenderCallback(EntitySentientSpecter entitylivingbaseIn, float partialTickTime) {
        float f = 0.9375F;
        GlStateManager.scale(0.9375F, 0.9375F, 0.9375F);
    }
}