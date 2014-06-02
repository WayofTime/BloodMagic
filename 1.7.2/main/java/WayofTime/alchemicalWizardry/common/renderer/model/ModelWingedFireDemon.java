package WayofTime.alchemicalWizardry.common.renderer.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class ModelWingedFireDemon extends ModelBase
{
    //fields
    ModelRenderer leftLegPlate;
    ModelRenderer leftLeg;
    ModelRenderer codPiece;
    ModelRenderer rightLegPlate;
    ModelRenderer rightLeg;
    ModelRenderer body;
    ModelRenderer leftShoulder;
    ModelRenderer leftArm;
    ModelRenderer head;
    ModelRenderer rightShoulder;
    ModelRenderer rightArm;
    ModelRenderer leftWing;
    ModelRenderer rightWing;
    ModelRenderer leftHorn1;
    ModelRenderer rightHorn1;
    ModelRenderer leftHorn2;
    ModelRenderer rightHorn2;

    public ModelWingedFireDemon()
    {
        textureWidth = 64;
        textureHeight = 64;
        leftLegPlate = new ModelRenderer(this, 40, 36);
        leftLegPlate.addBox(0F, -3F, -3F, 6, 6, 6);
        leftLegPlate.setRotationPoint(2F, 5F, 0F);
        leftLegPlate.setTextureSize(64, 64);
        leftLegPlate.mirror = true;
        setRotation(leftLegPlate, 0F, 0F, 0F);
        leftLeg = new ModelRenderer(this, 48, 16);
        leftLeg.addBox(1F, 3F, -2F, 4, 16, 4);
        leftLeg.setRotationPoint(2F, 5F, 0F);
        leftLeg.setTextureSize(64, 64);
        leftLeg.mirror = true;
        setRotation(leftLeg, 0F, 0F, 0F);
        codPiece = new ModelRenderer(this, 48, 0);
        codPiece.addBox(-2F, 0F, -2F, 4, 6, 4);
        codPiece.setRotationPoint(0F, 1F, 0F);
        codPiece.setTextureSize(64, 64);
        codPiece.mirror = true;
        setRotation(codPiece, 0F, 0F, 0F);
        rightLegPlate = new ModelRenderer(this, 40, 36);
        rightLegPlate.mirror = true;
        rightLegPlate.addBox(-6F, -3F, -3F, 6, 6, 6);
        rightLegPlate.setRotationPoint(-2F, 5F, 0F);
        rightLegPlate.setTextureSize(64, 64);
        rightLegPlate.mirror = true;
        setRotation(rightLegPlate, 0F, 0F, 0F);
        rightLegPlate.mirror = false;
        rightLeg = new ModelRenderer(this, 48, 16);
        rightLeg.mirror = true;
        rightLeg.addBox(-5F, 3F, -2F, 4, 16, 4);
        rightLeg.setRotationPoint(-2F, 5F, 0F);
        rightLeg.setTextureSize(64, 64);
        rightLeg.mirror = true;
        setRotation(rightLeg, 0F, 0F, 0F);
        rightLeg.mirror = false;
        body = new ModelRenderer(this, 0, 44);
        body.addBox(-5F, -14F, -3F, 10, 14, 6);
        body.setRotationPoint(0F, 1F, 0F);
        body.setTextureSize(64, 64);
        body.mirror = true;
        setRotation(body, 0F, 0F, 0F);
        leftShoulder = new ModelRenderer(this, 0, 29);
        leftShoulder.addBox(0F, -5F, -4F, 8, 7, 8);
        leftShoulder.setRotationPoint(5F, -10F, 0F);
        leftShoulder.setTextureSize(64, 64);
        leftShoulder.mirror = true;
        setRotation(leftShoulder, 0F, 0F, 0F);
        leftArm = new ModelRenderer(this, 32, 0);
        leftArm.addBox(3F, 2F, -2F, 4, 12, 4);
        leftArm.setRotationPoint(5F, -10F, 0F);
        leftArm.setTextureSize(64, 64);
        leftArm.mirror = true;
        setRotation(leftArm, 0F, 0F, 0F);
        head = new ModelRenderer(this, 32, 48);
        head.addBox(-4F, -7F, -4F, 8, 8, 8);
        head.setRotationPoint(0F, -14F, -1F);
        head.setTextureSize(64, 64);
        head.mirror = true;
        setRotation(head, 0F, 0F, 0F);
        rightShoulder = new ModelRenderer(this, 0, 29);
        rightShoulder.mirror = true;
        rightShoulder.mirror = true;
        rightShoulder.addBox(-8F, -5F, -4F, 8, 7, 8);
        rightShoulder.setRotationPoint(-5F, -10F, 0F);
        rightShoulder.setTextureSize(64, 64);
        rightShoulder.mirror = true;
        setRotation(rightShoulder, 0F, 0F, 0F);
        rightShoulder.mirror = false;
        rightArm = new ModelRenderer(this, 32, 0);
        rightArm.mirror = true;
        rightArm.mirror = true;
        rightArm.addBox(-7F, 2F, -2F, 4, 12, 4);
        rightArm.setRotationPoint(-5F, -10F, 0F);
        rightArm.setTextureSize(64, 64);
        rightArm.mirror = true;
        setRotation(rightArm, 0F, 0F, 0F);
        rightArm.mirror = false;
        leftWing = new ModelRenderer(this, 0, 0);
        leftWing.addBox(0F, -2F, 0F, 16, 12, 0);
        leftWing.setRotationPoint(0F, -11F, 3F);
        leftWing.setTextureSize(64, 64);
        leftWing.mirror = true;
        setRotation(leftWing, 0F, -0.5061455F, 0F);
        rightWing = new ModelRenderer(this, 0, 0);
        rightWing.mirror = true;
        rightWing.addBox(-16F, -2F, 0F, 16, 12, 0);
        rightWing.setRotationPoint(0F, -11F, 3F);
        rightWing.setTextureSize(64, 64);
        rightWing.mirror = true;
        setRotation(rightWing, 0F, 0.5061455F, 0F);
        rightWing.mirror = false;
        leftHorn1 = new ModelRenderer(this, 0, 12);
        leftHorn1.addBox(4F, -9F, -1F, 1, 5, 1);
        leftHorn1.setRotationPoint(0F, -14F, -1F);
        leftHorn1.setTextureSize(64, 64);
        leftHorn1.mirror = true;
        setRotation(leftHorn1, 0F, 0F, 0F);
        rightHorn1 = new ModelRenderer(this, 0, 12);
        rightHorn1.mirror = true;
        rightHorn1.addBox(-5F, -9F, -1F, 1, 5, 1);
        rightHorn1.setRotationPoint(0F, -14F, -1F);
        rightHorn1.setTextureSize(64, 64);
        rightHorn1.mirror = true;
        setRotation(rightHorn1, 0F, 0F, 0F);
        rightHorn1.mirror = false;
        leftHorn2 = new ModelRenderer(this, 4, 12);
        leftHorn2.addBox(4F, -10F, 0F, 1, 5, 1);
        leftHorn2.setRotationPoint(0F, -14F, -1F);
        leftHorn2.setTextureSize(64, 64);
        leftHorn2.mirror = true;
        setRotation(leftHorn2, 0F, 0F, 0F);
        rightHorn2 = new ModelRenderer(this, 4, 12);
        rightHorn2.mirror = true;
        rightHorn2.addBox(-5F, -10F, 0F, 1, 5, 1);
        rightHorn2.setRotationPoint(0F, -14F, -1F);
        rightHorn2.setTextureSize(64, 64);
        rightHorn2.mirror = true;
        setRotation(rightHorn2, 0F, 0F, 0F);
        rightHorn2.mirror = false;
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        leftLegPlate.render(f5);
        leftLeg.render(f5);
        codPiece.render(f5);
        rightLegPlate.render(f5);
        rightLeg.render(f5);
        body.render(f5);
        leftShoulder.render(f5);
        leftArm.render(f5);
        head.render(f5);
        rightShoulder.render(f5);
        rightArm.render(f5);
        leftWing.render(f5);
        rightWing.render(f5);
        leftHorn1.render(f5);
        rightHorn1.render(f5);
        leftHorn2.render(f5);
        rightHorn2.render(f5);
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
        this.leftHorn1.rotateAngleX = head.rotateAngleX;
        this.leftHorn1.rotateAngleY = head.rotateAngleY;
        this.leftHorn2.rotateAngleX = head.rotateAngleX;
        this.leftHorn2.rotateAngleY = head.rotateAngleY;
        this.rightHorn1.rotateAngleX = head.rotateAngleX;
        this.rightHorn1.rotateAngleY = head.rotateAngleY;
        this.rightHorn2.rotateAngleX = head.rotateAngleX;
        this.rightHorn2.rotateAngleY = head.rotateAngleY;
        this.leftLeg.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
        this.rightLeg.rotateAngleX = MathHelper.cos(f * 0.6662F + (float) Math.PI) * 1.0F * f1;
        this.rightArm.rotateAngleX = MathHelper.cos(f * 0.6662F + (float) Math.PI) * 1.0F * f1;
        this.leftArm.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
        this.leftShoulder.rotateAngleX = this.leftArm.rotateAngleX;
        this.rightShoulder.rotateAngleX = this.rightArm.rotateAngleX;
    }
}