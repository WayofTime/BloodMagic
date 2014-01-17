package WayofTime.alchemicalWizardry.common.renderer.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class ModelIceDemon extends ModelBase
{
    //fields
    ModelRenderer head;
    ModelRenderer leftHorn;
    ModelRenderer rightHorn;
    ModelRenderer body;
    ModelRenderer leftArm;
    ModelRenderer leftWrist;
    ModelRenderer leftIcicle1;
    ModelRenderer leftIcicle2;
    ModelRenderer leftIcicle3;
    ModelRenderer leftLeg;
    ModelRenderer rightArm;
    ModelRenderer rightWrist;
    ModelRenderer rightIcicle1;
    ModelRenderer rightIcicle2;
    ModelRenderer rightIcicle3;
    ModelRenderer rightLeg;
    ModelRenderer Shape1;

    public ModelIceDemon()
    {
        textureWidth = 64;
        textureHeight = 64;
        head = new ModelRenderer(this, 40, 0);
        head.addBox(-3F, -8F, -3F, 6, 8, 6);
        head.setRotationPoint(0F, -3F, 0F);
        head.setTextureSize(64, 64);
        head.mirror = true;
        setRotation(head, 0F, 0F, 0F);
        leftHorn = new ModelRenderer(this, 0, 0);
        leftHorn.addBox(3F, -7F, 2F, 1, 1, 10);
        leftHorn.setRotationPoint(0F, -3F, 0F);
        leftHorn.setTextureSize(64, 64);
        leftHorn.mirror = true;
        setRotation(leftHorn, 0.4363323F, 0F, 0F);
        rightHorn = new ModelRenderer(this, 0, 0);
        rightHorn.mirror = true;
        rightHorn.addBox(-4F, -7F, 2F, 1, 1, 10);
        rightHorn.setRotationPoint(0F, -3F, 0F);
        rightHorn.setTextureSize(64, 64);
        rightHorn.mirror = true;
        setRotation(rightHorn, 0.4363323F, 0F, 0F);
        rightHorn.mirror = false;
        body = new ModelRenderer(this, 40, 15);
        body.addBox(-4F, 0F, -2F, 8, 13, 4);
        body.setRotationPoint(0F, -3F, 0F);
        body.setTextureSize(64, 64);
        body.mirror = true;
        setRotation(body, 0F, 0F, 0F);
        leftArm = new ModelRenderer(this, 0, 48);
        leftArm.addBox(0F, -2F, -2F, 4, 12, 4);
        leftArm.setRotationPoint(4F, -1F, 0F);
        leftArm.setTextureSize(64, 64);
        leftArm.mirror = true;
        setRotation(leftArm, 0F, 0F, 0F);
        leftWrist = new ModelRenderer(this, 32, 57);
        leftWrist.addBox(0F, 6F, -2.5F, 5, 2, 5);
        leftWrist.setRotationPoint(4F, -1F, 0F);
        leftWrist.setTextureSize(64, 64);
        leftWrist.mirror = true;
        setRotation(leftWrist, 0F, 0F, 0F);
        leftIcicle1 = new ModelRenderer(this, 0, 0);
        leftIcicle1.addBox(4.9F, 0F, -0.5F, 1, 6, 1);
        leftIcicle1.setRotationPoint(4F, -1F, 0F);
        leftIcicle1.setTextureSize(64, 64);
        leftIcicle1.mirror = true;
        setRotation(leftIcicle1, 0F, 0F, 0.1396263F);
        leftIcicle2 = new ModelRenderer(this, 0, 0);
        leftIcicle2.addBox(5F, 0F, 0F, 1, 6, 1);
        leftIcicle2.setRotationPoint(4F, -1F, 0F);
        leftIcicle2.setTextureSize(64, 64);
        leftIcicle2.mirror = true;
        setRotation(leftIcicle2, 0F, 0.5585054F, 0.1919862F);
        leftIcicle3 = new ModelRenderer(this, 0, 0);
        leftIcicle3.addBox(5F, 0F, -1F, 1, 6, 1);
        leftIcicle3.setRotationPoint(4F, -1F, 0F);
        leftIcicle3.setTextureSize(64, 64);
        leftIcicle3.mirror = true;
        setRotation(leftIcicle3, 0F, -0.5585054F, 0.1919862F);
        leftLeg = new ModelRenderer(this, 16, 46);
        leftLeg.addBox(-2F, 0F, -2F, 4, 14, 4);
        leftLeg.setRotationPoint(2F, 10F, 0F);
        leftLeg.setTextureSize(64, 64);
        leftLeg.mirror = true;
        setRotation(leftLeg, 0F, 0F, 0F);
        rightArm = new ModelRenderer(this, 0, 48);
        rightArm.mirror = true;
        rightArm.addBox(-4F, -2F, -2F, 4, 12, 4);
        rightArm.setRotationPoint(-4F, -1F, 0F);
        rightArm.setTextureSize(64, 64);
        rightArm.mirror = true;
        setRotation(rightArm, 0F, 0F, 0F);
        rightArm.mirror = false;
        rightWrist = new ModelRenderer(this, 32, 57);
        rightWrist.mirror = true;
        rightWrist.addBox(-5F, 6F, -2.5F, 5, 2, 5);
        rightWrist.setRotationPoint(-4F, -1F, 0F);
        rightWrist.setTextureSize(64, 64);
        rightWrist.mirror = true;
        setRotation(rightWrist, 0F, 0F, 0F);
        rightWrist.mirror = false;
        rightIcicle1 = new ModelRenderer(this, 0, 0);
        rightIcicle1.addBox(-5.9F, 0F, -0.5F, 1, 6, 1);
        rightIcicle1.setRotationPoint(-4F, -1F, 0F);
        rightIcicle1.setTextureSize(64, 64);
        rightIcicle1.mirror = true;
        setRotation(rightIcicle1, 0F, 0F, -0.1396263F);
        rightIcicle2 = new ModelRenderer(this, 0, 0);
        rightIcicle2.addBox(-6F, 0F, 0F, 1, 6, 1);
        rightIcicle2.setRotationPoint(-4F, -1F, 0F);
        rightIcicle2.setTextureSize(64, 64);
        rightIcicle2.mirror = true;
        setRotation(rightIcicle2, 0F, -0.5585054F, -0.1919862F);
        rightIcicle3 = new ModelRenderer(this, 0, 0);
        rightIcicle3.addBox(-6F, 0F, -1F, 1, 6, 1);
        rightIcicle3.setRotationPoint(-4F, -1F, 0F);
        rightIcicle3.setTextureSize(64, 64);
        rightIcicle3.mirror = true;
        setRotation(rightIcicle3, 0F, 0.5585054F, -0.1919862F);
        rightLeg = new ModelRenderer(this, 16, 46);
        rightLeg.mirror = true;
        rightLeg.addBox(-2F, 0F, -2F, 4, 14, 4);
        rightLeg.setRotationPoint(-2F, 10F, 0F);
        rightLeg.setTextureSize(64, 64);
        rightLeg.mirror = true;
        setRotation(rightLeg, 0F, 0F, 0F);
        rightLeg.mirror = false;
        Shape1 = new ModelRenderer(this, 0, 12);
        Shape1.addBox(-0.5F, 0F, -0.5F, 1, 10, 1);
        Shape1.setRotationPoint(0F, 8F, 1.5F);
        Shape1.setTextureSize(64, 64);
        Shape1.mirror = true;
        setRotation(Shape1, 0.5948578F, 0F, 0F);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        head.render(f5);
        leftHorn.render(f5);
        rightHorn.render(f5);
        body.render(f5);
        leftArm.render(f5);
        leftWrist.render(f5);
        leftIcicle1.render(f5);
        leftIcicle2.render(f5);
        leftIcicle3.render(f5);
        leftLeg.render(f5);
        rightArm.render(f5);
        rightWrist.render(f5);
        rightIcicle1.render(f5);
        rightIcicle2.render(f5);
        rightIcicle3.render(f5);
        rightLeg.render(f5);
        Shape1.render(f5);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z)
    {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
    {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        this.head.rotateAngleX = f4 / (180F / (float) Math.PI);
        this.head.rotateAngleY = f3 / (180F / (float) Math.PI);
        this.leftLeg.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
        this.rightLeg.rotateAngleX = MathHelper.cos(f * 0.6662F + (float) Math.PI) * 1.4F * f1;
        this.rightArm.rotateAngleX = MathHelper.cos(f * 0.6662F + (float) Math.PI) * 1.4F * f1;
        this.leftArm.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
        this.leftHorn.rotateAngleX = head.rotateAngleX + 0.4363323F;
        this.leftHorn.rotateAngleY = head.rotateAngleY;
        this.rightHorn.rotateAngleX = head.rotateAngleX + 0.4363323F;
        this.rightHorn.rotateAngleY = head.rotateAngleY;
        this.rightIcicle1.rotateAngleX = rightArm.rotateAngleX;
        this.rightIcicle2.rotateAngleX = rightArm.rotateAngleX;
        this.rightIcicle3.rotateAngleX = rightArm.rotateAngleX;
        this.leftIcicle1.rotateAngleX = leftArm.rotateAngleX;
        this.leftIcicle2.rotateAngleX = leftArm.rotateAngleX;
        this.leftIcicle3.rotateAngleX = leftArm.rotateAngleX;
        this.rightWrist.rotateAngleX = rightArm.rotateAngleX;
        this.leftWrist.rotateAngleX = leftArm.rotateAngleX;
    }
}
