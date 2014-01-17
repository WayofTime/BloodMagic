package WayofTime.alchemicalWizardry.common.renderer.model;

import WayofTime.alchemicalWizardry.common.entity.mob.EntityLowerGuardian;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

public class ModelLowerGuardian extends ModelBase
{
    //fields
    ModelRenderer Body;
    ModelRenderer Torso;
    ModelRenderer Head;
    ModelRenderer leftArm;
    ModelRenderer rightArm;
    ModelRenderer leftLeg;
    ModelRenderer leftFoot;
    ModelRenderer rightLeg;
    ModelRenderer rightFoot;
    ModelRenderer leftHorn;
    ModelRenderer hornAppendage1;
    ModelRenderer hornAppendage2;
    ModelRenderer rightHorn;
    ModelRenderer hornAppendage3;
    ModelRenderer hornAppendage4;

    public ModelLowerGuardian()
    {
        textureWidth = 64;
        textureHeight = 64;
        Body = new ModelRenderer(this, 0, 0);
        Body.addBox(-8F, -7F, -4F, 16, 14, 8);
        Body.setRotationPoint(0F, -3F, 0F);
        Body.setTextureSize(64, 64);
        Body.mirror = true;
        setRotation(Body, 0F, 0F, 0F);
        Torso = new ModelRenderer(this, 0, 25);
        Torso.addBox(-4F, 0F, -3F, 8, 4, 6);
        Torso.setRotationPoint(0F, 4F, 0F);
        Torso.setTextureSize(64, 64);
        Torso.mirror = true;
        setRotation(Torso, 0F, 0F, 0F);
        Head = new ModelRenderer(this, 29, 25);
        Head.addBox(-4F, -8F, -4F, 8, 8, 8);
        Head.setRotationPoint(0F, -10F, 0F);
        Head.setTextureSize(64, 64);
        Head.mirror = true;
        setRotation(Head, 0F, 0F, 0F);
        leftArm = new ModelRenderer(this, 17, 42);
        leftArm.addBox(0F, -2F, -2F, 4, 18, 4);
        leftArm.setRotationPoint(8F, -8F, 0F);
        leftArm.setTextureSize(64, 64);
        leftArm.mirror = true;
        setRotation(leftArm, 0F, 0F, 0F);
        rightArm = new ModelRenderer(this, 17, 42);
        rightArm.mirror = true;
        rightArm.addBox(-4F, -2F, -2F, 4, 18, 4);
        rightArm.setRotationPoint(-8F, -8F, 0F);
        rightArm.setTextureSize(64, 64);
        rightArm.mirror = true;
        setRotation(rightArm, 0F, 0F, 0F);
        rightArm.mirror = false;
        leftLeg = new ModelRenderer(this, 0, 42);
        leftLeg.addBox(0F, -2F, -2F, 4, 17, 4);
        leftLeg.setRotationPoint(4F, 6F, 0F);
        leftLeg.setTextureSize(64, 64);
        leftLeg.mirror = true;
        setRotation(leftLeg, 0F, 0F, 0F);
        leftFoot = new ModelRenderer(this, 34, 42);
        leftFoot.addBox(0F, 15F, -6F, 4, 3, 8);
        leftFoot.setRotationPoint(4F, 6F, 0F);
        leftFoot.setTextureSize(64, 64);
        leftFoot.mirror = true;
        setRotation(leftFoot, 0F, 0F, 0F);
        rightLeg = new ModelRenderer(this, 0, 42);
        rightLeg.mirror = true;
        rightLeg.addBox(-4F, -2F, -2F, 4, 17, 4);
        rightLeg.setRotationPoint(-4F, 6F, 0F);
        rightLeg.setTextureSize(64, 64);
        rightLeg.mirror = true;
        setRotation(rightLeg, 0F, 0F, 0F);
        rightLeg.mirror = false;
        rightFoot = new ModelRenderer(this, 34, 42);
        rightFoot.mirror = true;
        rightFoot.addBox(-4F, 15F, -6F, 4, 3, 8);
        rightFoot.setRotationPoint(-4F, 6F, 0F);
        rightFoot.setTextureSize(64, 64);
        rightFoot.mirror = true;
        setRotation(rightFoot, 0F, 0F, 0F);
        rightFoot.mirror = false;
        leftHorn = new ModelRenderer(this, 0, 0);
        leftHorn.addBox(4F, -11F, 0F, 1, 6, 1);
        leftHorn.setRotationPoint(0F, -10F, 0F);
        leftHorn.setTextureSize(64, 64);
        leftHorn.mirror = true;
        setRotation(leftHorn, 0F, 0F, 0F);
        hornAppendage1 = new ModelRenderer(this, 0, 0);
        hornAppendage1.addBox(4F, -7F, -1F, 1, 1, 1);
        hornAppendage1.setRotationPoint(0F, -10F, 0F);
        hornAppendage1.setTextureSize(64, 64);
        hornAppendage1.mirror = true;
        setRotation(hornAppendage1, 0F, 0F, 0F);
        hornAppendage2 = new ModelRenderer(this, 0, 0);
        hornAppendage2.addBox(4F, -6F, 1F, 1, 1, 1);
        hornAppendage2.setRotationPoint(0F, -10F, 0F);
        hornAppendage2.setTextureSize(64, 64);
        hornAppendage2.mirror = true;
        setRotation(hornAppendage2, 0F, 0F, 0F);
        rightHorn = new ModelRenderer(this, 0, 0);
        rightHorn.mirror = true;
        rightHorn.addBox(-5F, -11F, 0F, 1, 6, 1);
        rightHorn.setRotationPoint(0F, -10F, 0F);
        rightHorn.setTextureSize(64, 64);
        rightHorn.mirror = true;
        setRotation(rightHorn, 0F, 0F, 0F);
        rightHorn.mirror = false;
        hornAppendage3 = new ModelRenderer(this, 0, 0);
        hornAppendage3.mirror = true;
        hornAppendage3.addBox(-5F, -7F, -1F, 1, 1, 1);
        hornAppendage3.setRotationPoint(0F, -10F, 0F);
        hornAppendage3.setTextureSize(64, 64);
        hornAppendage3.mirror = true;
        setRotation(hornAppendage3, 0F, 0F, 0F);
        hornAppendage3.mirror = false;
        hornAppendage4 = new ModelRenderer(this, 0, 0);
        hornAppendage4.mirror = true;
        hornAppendage4.addBox(-5F, -6F, 1F, 1, 1, 1);
        hornAppendage4.setRotationPoint(0F, -10F, 0F);
        hornAppendage4.setTextureSize(64, 64);
        hornAppendage4.mirror = true;
        setRotation(hornAppendage4, 0F, 0F, 0F);
        hornAppendage4.mirror = false;
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        Body.render(f5);
        Torso.render(f5);
        Head.render(f5);
        leftArm.render(f5);
        rightArm.render(f5);
        leftLeg.render(f5);
        leftFoot.render(f5);
        rightLeg.render(f5);
        rightFoot.render(f5);
        leftHorn.render(f5);
        hornAppendage1.render(f5);
        hornAppendage2.render(f5);
        rightHorn.render(f5);
        hornAppendage3.render(f5);
        hornAppendage4.render(f5);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z)
    {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    public void setLivingAnimations(EntityLivingBase par1EntityLivingBase, float par2, float par3, float par4)
    {
        EntityLowerGuardian entityLowerGuardian = (EntityLowerGuardian) par1EntityLivingBase;
        int i = entityLowerGuardian.getAttackTimer();

        if (i > 0)
        {
            this.rightLeg.rotateAngleX = -2.0F + 1.5F * this.func_78172_a((float) i - par4, 10.0F);
            this.rightFoot.rotateAngleX = -2.0F + 1.5F * this.func_78172_a((float) i - par4, 10.0F);
            //this.ironGolemLeftArm.rotateAngleX = -2.0F + 1.5F * this.func_78172_a((float)i - par4, 10.0F);
        }
    }

    private float func_78172_a(float par1, float par2)
    {
        return (Math.abs(par1 % par2 - par2 * 0.5F) - par2 * 0.25F) / (par2 * 0.25F);
    }

    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
    {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        this.Head.rotateAngleX = f4 / (180F / (float) Math.PI);
        this.Head.rotateAngleY = f3 / (180F / (float) Math.PI);
        this.leftLeg.rotateAngleX = MathHelper.cos(f * 0.3662F) * 1.0F * f1;
        this.rightLeg.rotateAngleX = MathHelper.cos(f * 0.3662F + (float) Math.PI) * 1.0F * f1;
        this.leftFoot.rotateAngleX = MathHelper.cos(f * 0.3662F) * 1.0F * f1;
        this.rightFoot.rotateAngleX = MathHelper.cos(f * 0.3662F + (float) Math.PI) * 1.0F * f1;
        this.rightArm.rotateAngleX = MathHelper.cos(f * 0.3662F + (float) Math.PI) * 1.0F * f1;
        this.leftArm.rotateAngleX = MathHelper.cos(f * 0.3662F) * 1.0F * f1;
        this.hornAppendage1.rotateAngleX = this.Head.rotateAngleX;
        this.hornAppendage1.rotateAngleY = this.Head.rotateAngleY;
        this.hornAppendage2.rotateAngleX = this.Head.rotateAngleX;
        this.hornAppendage2.rotateAngleY = this.Head.rotateAngleY;
        this.hornAppendage3.rotateAngleX = this.Head.rotateAngleX;
        this.hornAppendage3.rotateAngleY = this.Head.rotateAngleY;
        this.hornAppendage4.rotateAngleX = this.Head.rotateAngleX;
        this.hornAppendage4.rotateAngleY = this.Head.rotateAngleY;
        this.leftHorn.rotateAngleX = this.Head.rotateAngleX;
        this.leftHorn.rotateAngleY = this.Head.rotateAngleY;
        this.rightHorn.rotateAngleX = this.Head.rotateAngleX;
        this.rightHorn.rotateAngleY = this.Head.rotateAngleY;
    }
}
