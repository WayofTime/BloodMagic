package WayofTime.bloodmagic.client.render.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelMimic extends ModelBase {
    /**
     * Spider's first leg
     */
    public ModelRenderer mimicLeg1;
    /**
     * Spider's second leg
     */
    public ModelRenderer mimicLeg2;
    /**
     * Spider's third leg
     */
    public ModelRenderer mimicLeg3;
    /**
     * Spider's fourth leg
     */
    public ModelRenderer mimicLeg4;
    /**
     * Spider's fifth leg
     */
    public ModelRenderer mimicLeg5;
    /**
     * Spider's sixth leg
     */
    public ModelRenderer mimicLeg6;
    /**
     * Spider's seventh leg
     */
    public ModelRenderer mimicLeg7;
    /**
     * Spider's eight leg
     */
    public ModelRenderer mimicLeg8;

    public ModelMimic() {
        this.mimicLeg1 = new ModelRenderer(this, 18, 0);
        this.mimicLeg1.addBox(-15.0F, -1.0F, -1.0F, 16, 2, 2, 0.0F);
        this.mimicLeg1.setRotationPoint(-4.0F, 15.0F, 2.0F);
        this.mimicLeg2 = new ModelRenderer(this, 18, 0);
        this.mimicLeg2.addBox(-1.0F, -1.0F, -1.0F, 16, 2, 2, 0.0F);
        this.mimicLeg2.setRotationPoint(4.0F, 15.0F, 2.0F);
        this.mimicLeg3 = new ModelRenderer(this, 18, 0);
        this.mimicLeg3.addBox(-15.0F, -1.0F, -1.0F, 16, 2, 2, 0.0F);
        this.mimicLeg3.setRotationPoint(-4.0F, 15.0F, 1.0F);
        this.mimicLeg4 = new ModelRenderer(this, 18, 0);
        this.mimicLeg4.addBox(-1.0F, -1.0F, -1.0F, 16, 2, 2, 0.0F);
        this.mimicLeg4.setRotationPoint(4.0F, 15.0F, 1.0F);
        this.mimicLeg5 = new ModelRenderer(this, 18, 0);
        this.mimicLeg5.addBox(-15.0F, -1.0F, -1.0F, 16, 2, 2, 0.0F);
        this.mimicLeg5.setRotationPoint(-4.0F, 15.0F, 0.0F);
        this.mimicLeg6 = new ModelRenderer(this, 18, 0);
        this.mimicLeg6.addBox(-1.0F, -1.0F, -1.0F, 16, 2, 2, 0.0F);
        this.mimicLeg6.setRotationPoint(4.0F, 15.0F, 0.0F);
        this.mimicLeg7 = new ModelRenderer(this, 18, 0);
        this.mimicLeg7.addBox(-15.0F, -1.0F, -1.0F, 16, 2, 2, 0.0F);
        this.mimicLeg7.setRotationPoint(-4.0F, 15.0F, -1.0F);
        this.mimicLeg8 = new ModelRenderer(this, 18, 0);
        this.mimicLeg8.addBox(-1.0F, -1.0F, -1.0F, 16, 2, 2, 0.0F);
        this.mimicLeg8.setRotationPoint(4.0F, 15.0F, -1.0F);
    }

    /**
     * Sets the models various rotation angles then renders the model.
     */
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
        this.mimicLeg1.render(scale);
        this.mimicLeg2.render(scale);
        this.mimicLeg3.render(scale);
        this.mimicLeg4.render(scale);
        this.mimicLeg5.render(scale);
        this.mimicLeg6.render(scale);
        this.mimicLeg7.render(scale);
        this.mimicLeg8.render(scale);
    }

    /**
     * Sets the model's various rotation angles. For bipeds, par1 and par2 are
     * used for animating the movement of arms and legs, where par1 represents
     * the time(so that arms and legs swing back and forth) and par2 represents
     * how "far" arms and legs can swing at most.
     */
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
        this.mimicLeg1.rotateAngleZ = -((float) Math.PI / 4F);
        this.mimicLeg2.rotateAngleZ = ((float) Math.PI / 4F);
        this.mimicLeg3.rotateAngleZ = -0.58119464F;
        this.mimicLeg4.rotateAngleZ = 0.58119464F;
        this.mimicLeg5.rotateAngleZ = -0.58119464F;
        this.mimicLeg6.rotateAngleZ = 0.58119464F;
        this.mimicLeg7.rotateAngleZ = -((float) Math.PI / 4F);
        this.mimicLeg8.rotateAngleZ = ((float) Math.PI / 4F);

        this.mimicLeg1.rotateAngleY = ((float) Math.PI / 4F);
        this.mimicLeg2.rotateAngleY = -((float) Math.PI / 4F);
        this.mimicLeg3.rotateAngleY = 0.3926991F;
        this.mimicLeg4.rotateAngleY = -0.3926991F;
        this.mimicLeg5.rotateAngleY = -0.3926991F;
        this.mimicLeg6.rotateAngleY = 0.3926991F;
        this.mimicLeg7.rotateAngleY = -((float) Math.PI / 4F);
        this.mimicLeg8.rotateAngleY = ((float) Math.PI / 4F);
        float f3 = -(MathHelper.cos(limbSwing * 0.6662F * 2.0F + 0.0F) * 0.4F) * limbSwingAmount;
        float f4 = -(MathHelper.cos(limbSwing * 0.6662F * 2.0F + (float) Math.PI) * 0.4F) * limbSwingAmount;
        float f5 = -(MathHelper.cos(limbSwing * 0.6662F * 2.0F + ((float) Math.PI / 2F)) * 0.4F) * limbSwingAmount;
        float f6 = -(MathHelper.cos(limbSwing * 0.6662F * 2.0F + ((float) Math.PI * 3F / 2F)) * 0.4F) * limbSwingAmount;
        float f7 = Math.abs(MathHelper.sin(limbSwing * 0.6662F + 0.0F) * 0.4F) * limbSwingAmount;
        float f8 = Math.abs(MathHelper.sin(limbSwing * 0.6662F + (float) Math.PI) * 0.4F) * limbSwingAmount;
        float f9 = Math.abs(MathHelper.sin(limbSwing * 0.6662F + ((float) Math.PI / 2F)) * 0.4F) * limbSwingAmount;
        float f10 = Math.abs(MathHelper.sin(limbSwing * 0.6662F + ((float) Math.PI * 3F / 2F)) * 0.4F) * limbSwingAmount;
        this.mimicLeg1.rotateAngleY += f3;
        this.mimicLeg2.rotateAngleY += -f3;
        this.mimicLeg3.rotateAngleY += f4;
        this.mimicLeg4.rotateAngleY += -f4;
        this.mimicLeg5.rotateAngleY += f5;
        this.mimicLeg6.rotateAngleY += -f5;
        this.mimicLeg7.rotateAngleY += f6;
        this.mimicLeg8.rotateAngleY += -f6;
        this.mimicLeg1.rotateAngleZ += f7;
        this.mimicLeg2.rotateAngleZ += -f7;
        this.mimicLeg3.rotateAngleZ += f8;
        this.mimicLeg4.rotateAngleZ += -f8;
        this.mimicLeg5.rotateAngleZ += f9;
        this.mimicLeg6.rotateAngleZ += -f9;
        this.mimicLeg7.rotateAngleZ += f10;
        this.mimicLeg8.rotateAngleZ += -f10;
    }
}