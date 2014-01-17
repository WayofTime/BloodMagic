package WayofTime.alchemicalWizardry.common.renderer.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class ModelBoulderFist extends ModelBase
{
    //fields
    ModelRenderer leftFist;
    ModelRenderer leftArm;
    ModelRenderer body;
    ModelRenderer leftLeg1;
    ModelRenderer leftLeg2;
    ModelRenderer leftFoot;
    ModelRenderer rightFist;
    ModelRenderer rightArm;
    ModelRenderer rightLeg1;
    ModelRenderer rightLeg2;
    ModelRenderer rightFoot;
    ModelRenderer head;

    public ModelBoulderFist()
    {
        textureWidth = 64;
        textureHeight = 64;
        leftFist = new ModelRenderer(this, 33, 52);
        leftFist.addBox(-1F, 12F, -3F, 6, 6, 6);
        leftFist.setRotationPoint(5F, 6F, -6F);
        leftFist.setTextureSize(64, 64);
        leftFist.mirror = true;
        setRotation(leftFist, 0F, 0F, 0F);
        leftArm = new ModelRenderer(this, 48, 33);
        leftArm.addBox(0F, -2F, -2F, 4, 14, 4);
        leftArm.setRotationPoint(5F, 6F, -6F);
        leftArm.setTextureSize(64, 64);
        leftArm.mirror = true;
        setRotation(leftArm, 0F, 0F, 0F);
        body = new ModelRenderer(this, 0, 40);
        body.addBox(-5F, -2F, -3F, 10, 18, 6);
        body.setRotationPoint(0F, 6F, -6F);
        body.setTextureSize(64, 64);
        body.mirror = true;
        setRotation(body, 1.22173F, 0F, 0F);
        leftLeg1 = new ModelRenderer(this, 0, 25);
        leftLeg1.addBox(0F, -1F, -1F, 4, 6, 2);
        leftLeg1.setRotationPoint(5F, 11F, 7F);
        leftLeg1.setTextureSize(64, 64);
        leftLeg1.mirror = true;
        setRotation(leftLeg1, -((float) Math.PI / 4F), 0F, 0F);
        leftLeg2 = new ModelRenderer(this, 1, 25);
        leftLeg2.addBox(0F, 5F, -1F, 4, 2, 12);
        leftLeg2.setRotationPoint(5F, 11F, 7F);
        leftLeg2.setTextureSize(64, 64);
        leftLeg2.mirror = true;
        setRotation(leftLeg2, -((float) Math.PI / 4F), 0F, 0F);
        leftFoot = new ModelRenderer(this, 22, 25);
        leftFoot.addBox(0F, 11F, -1F, 4, 2, 5);
        leftFoot.setRotationPoint(5F, 11F, 7F);
        leftFoot.setTextureSize(64, 64);
        leftFoot.mirror = true;
        setRotation(leftFoot, 0F, 0F, 0F);
        rightFist = new ModelRenderer(this, 33, 52);
        rightFist.mirror = true;
        rightFist.addBox(-5F, 12F, -3F, 6, 6, 6);
        rightFist.setRotationPoint(-5F, 6F, -6F);
        rightFist.setTextureSize(64, 64);
        rightFist.mirror = true;
        setRotation(rightFist, 0F, 0F, 0F);
        rightFist.mirror = false;
        rightArm = new ModelRenderer(this, 48, 33);
        rightArm.mirror = true;
        rightArm.addBox(-4F, -2F, -2F, 4, 14, 4);
        rightArm.setRotationPoint(-5F, 6F, -6F);
        rightArm.setTextureSize(64, 64);
        rightArm.mirror = true;
        setRotation(rightArm, 0F, 0F, 0F);
        rightArm.mirror = false;
        rightLeg1 = new ModelRenderer(this, 0, 25);
        rightLeg1.mirror = true;
        rightLeg1.addBox(-4F, -1F, -1F, 4, 6, 2);
        rightLeg1.setRotationPoint(-5F, 11F, 7F);
        rightLeg1.setTextureSize(64, 64);
        rightLeg1.mirror = true;
        setRotation(rightLeg1, -((float) Math.PI / 4F), 0F, 0F);
        rightLeg1.mirror = false;
        rightLeg2 = new ModelRenderer(this, 1, 25);
        rightLeg2.mirror = true;
        rightLeg2.addBox(-4F, 5F, -1F, 4, 2, 12);
        rightLeg2.setRotationPoint(-5F, 11F, 7F);
        rightLeg2.setTextureSize(64, 64);
        rightLeg2.mirror = true;
        setRotation(rightLeg2, -((float) Math.PI / 4F), 0F, 0F);
        rightLeg2.mirror = false;
        rightFoot = new ModelRenderer(this, 22, 25);
        rightFoot.mirror = true;
        rightFoot.addBox(-4F, 11F, -1F, 4, 2, 5);
        rightFoot.setRotationPoint(-5F, 11F, 7F);
        rightFoot.setTextureSize(64, 64);
        rightFoot.mirror = true;
        setRotation(rightFoot, 0F, 0F, 0F);
        rightFoot.mirror = false;
        head = new ModelRenderer(this, 0, 0);
        head.addBox(-3F, -5F, -5F, 6, 6, 6);
        head.setRotationPoint(0F, 5F, -7F);
        head.setTextureSize(64, 64);
        head.mirror = true;
        setRotation(head, 0F, 0F, 0F);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        leftFist.render(f5);
        leftArm.render(f5);
        body.render(f5);
        leftLeg1.render(f5);
        leftLeg2.render(f5);
        leftFoot.render(f5);
        rightFist.render(f5);
        rightArm.render(f5);
        rightLeg1.render(f5);
        rightLeg2.render(f5);
        rightFoot.render(f5);
        head.render(f5);
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
        this.leftFoot.rotateAngleX = MathHelper.cos(f * 0.6662F) * 0.8F * f1;
        this.rightFoot.rotateAngleX = MathHelper.cos(f * 0.6662F + (float) Math.PI) * 0.8F * f1;
        this.leftLeg1.rotateAngleX = leftFoot.rotateAngleX - ((float) Math.PI / 4F);
        this.rightLeg1.rotateAngleX = rightFoot.rotateAngleX - ((float) Math.PI / 4F);
        this.leftLeg2.rotateAngleX = leftFoot.rotateAngleX - ((float) Math.PI / 4F);
        this.rightLeg2.rotateAngleX = rightFoot.rotateAngleX - ((float) Math.PI / 4F);
        this.rightArm.rotateAngleX = MathHelper.cos(f * 0.6662F + (float) Math.PI) * 0.9f * f1;
        this.leftArm.rotateAngleX = MathHelper.cos(f * 0.6662F) * 0.9f * f1;
        this.leftFist.rotateAngleX = leftArm.rotateAngleX;
        this.rightFist.rotateAngleX = rightArm.rotateAngleX;
    }
}