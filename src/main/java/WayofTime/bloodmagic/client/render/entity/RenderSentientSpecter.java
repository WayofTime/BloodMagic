package WayofTime.bloodmagic.client.render.entity;

import WayofTime.bloodmagic.entity.mob.EntitySentientSpecter;
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

@SideOnly(Side.CLIENT)
public class RenderSentientSpecter extends RenderBiped<EntitySentientSpecter> {
    public static final ResourceLocation texture = new ResourceLocation("bloodmagic", "textures/entities/specter.png");

    public RenderSentientSpecter(RenderManager renderManager) {
        super(renderManager, new ModelBiped(0.0F), 0);
        this.addLayer(new LayerBipedArmor(this));
        this.addLayer(new LayerHeldItem(this));
        this.addLayer(new LayerArrow(this));
        this.addLayer(new LayerCustomHead(this.getMainModel().bipedHead));
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
                EnumAction enumaction = itemstack.getItemUseAction();

                if (enumaction == EnumAction.BLOCK) {
                    modelbiped$armpose = ModelBiped.ArmPose.BLOCK;
                } else if (enumaction == EnumAction.BOW) {
                    modelbiped$armpose = ModelBiped.ArmPose.BOW_AND_ARROW;
                }
            }
        }

        if (!itemstack1.isEmpty()) {
            modelbiped$armpose1 = ModelBiped.ArmPose.ITEM;

            if (clientPlayer.getItemInUseCount() > 0) {
                EnumAction enumaction1 = itemstack1.getItemUseAction();

                if (enumaction1 == EnumAction.BLOCK) {
                    modelbiped$armpose1 = ModelBiped.ArmPose.BLOCK;
                }
            }
        }

        if (clientPlayer.getPrimaryHand() == EnumHandSide.RIGHT) {
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