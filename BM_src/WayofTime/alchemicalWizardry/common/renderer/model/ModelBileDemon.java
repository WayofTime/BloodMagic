package WayofTime.alchemicalWizardry.common.renderer.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;

public class ModelBileDemon extends ModelBase
{
    //fields
    ModelRenderer belly;
    ModelRenderer chest;
    ModelRenderer head;
    ModelRenderer nose;
    ModelRenderer leftHorn;
    ModelRenderer leftArmSpacer;
    ModelRenderer leftArm;
    ModelRenderer leftChain;
    ModelRenderer leftBall;
    ModelRenderer rightHorn;
    ModelRenderer rightChain;
    ModelRenderer rightBall;
    ModelRenderer rightArmSpacer;
    ModelRenderer rightArm;

    public ModelBileDemon()
    {
        textureWidth = 128;
        textureHeight = 64;
        belly = new ModelRenderer(this, 0, 31);
        belly.addBox(-8F, -1F, -10F, 16, 15, 18);
        belly.setRotationPoint(0F, 10F, 0F);
        belly.setTextureSize(128, 64);
        belly.mirror = true;
        setRotation(belly, 0F, 0F, 0F);
        chest = new ModelRenderer(this, 70, 46);
        chest.addBox(-7F, -4F, -6F, 14, 4, 14);
        chest.setRotationPoint(0F, 10F, 0F);
        chest.setTextureSize(128, 64);
        chest.mirror = true;
        setRotation(chest, -0.1115358F, 0F, 0F);
        head = new ModelRenderer(this, 0, 0);
        head.addBox(-4F, -8F, -4F, 8, 8, 8);
        head.setRotationPoint(0F, 6F, 3F);
        head.setTextureSize(128, 64);
        head.mirror = true;
        setRotation(head, 0F, 0F, 0F);
        nose = new ModelRenderer(this, 0, 0);
        nose.addBox(-1F, -4F, -5F, 2, 1, 1);
        nose.setRotationPoint(0F, 6F, 3F);
        nose.setTextureSize(128, 64);
        nose.mirror = true;
        setRotation(nose, 0F, 0F, 0F);
        leftHorn = new ModelRenderer(this, 93, 1);
        leftHorn.addBox(4F, -7F, 0F, 16, 1, 1);
        leftHorn.setRotationPoint(0F, 6F, 3F);
        leftHorn.setTextureSize(128, 64);
        leftHorn.mirror = true;
        setRotation(leftHorn, 0F, 0F, 0F);
        leftArmSpacer = new ModelRenderer(this, 80, 1);
        leftArmSpacer.addBox(0F, -2F, -2F, 1, 4, 4);
        leftArmSpacer.setRotationPoint(7F, 8F, 3F);
        leftArmSpacer.setTextureSize(128, 64);
        leftArmSpacer.mirror = true;
        setRotation(leftArmSpacer, 0F, 0F, 0F);
        leftArm = new ModelRenderer(this, 62, 1);
        leftArm.addBox(1F, -2F, -2F, 4, 18, 4);
        leftArm.setRotationPoint(7F, 8F, 3F);
        leftArm.setTextureSize(128, 64);
        leftArm.mirror = true;
        setRotation(leftArm, 0F, 0F, 0F);
        leftChain = new ModelRenderer(this, 95, 5);
        leftChain.addBox(17F, -6F, 0F, 1, 6, 1);
        leftChain.setRotationPoint(0F, 6F, 3F);
        leftChain.setTextureSize(128, 64);
        leftChain.mirror = true;
        setRotation(leftChain, 0F, 0F, 0F);
        leftBall = new ModelRenderer(this, 107, 4);
        leftBall.addBox(15F, 0F, -2F, 5, 5, 5);
        leftBall.setRotationPoint(0F, 6F, 3F);
        leftBall.setTextureSize(128, 64);
        leftBall.mirror = true;
        setRotation(leftBall, 0F, 0F, 0F);
        rightHorn = new ModelRenderer(this, 93, 1);
        rightHorn.mirror = true;
        rightHorn.addBox(-20F, -7F, 0F, 16, 1, 1);
        rightHorn.setRotationPoint(0F, 6F, 3F);
        rightHorn.setTextureSize(128, 64);
        rightHorn.mirror = true;
        setRotation(rightHorn, 0F, 0F, 0F);
        rightHorn.mirror = false;
        rightChain = new ModelRenderer(this, 95, 5);
        rightChain.mirror = true;
        rightChain.addBox(-18F, -6F, 0F, 1, 6, 1);
        rightChain.setRotationPoint(0F, 6F, 3F);
        rightChain.setTextureSize(128, 64);
        rightChain.mirror = true;
        setRotation(rightChain, 0F, 0F, 0F);
        rightChain.mirror = false;
        rightBall = new ModelRenderer(this, 107, 4);
        rightBall.mirror = true;
        rightBall.addBox(-20F, 0F, -2F, 5, 5, 5);
        rightBall.setRotationPoint(0F, 6F, 3F);
        rightBall.setTextureSize(128, 64);
        rightBall.mirror = true;
        setRotation(rightBall, 0F, 0F, 0F);
        rightBall.mirror = false;
        rightArmSpacer = new ModelRenderer(this, 80, 1);
        rightArmSpacer.mirror = true;
        rightArmSpacer.addBox(-1F, -2F, -2F, 1, 4, 4);
        rightArmSpacer.setRotationPoint(-7F, 8F, 3F);
        rightArmSpacer.setTextureSize(128, 64);
        rightArmSpacer.mirror = true;
        setRotation(rightArmSpacer, 0F, 0F, 0F);
        rightArmSpacer.mirror = false;
        rightArm = new ModelRenderer(this, 62, 1);
        rightArm.mirror = true;
        rightArm.addBox(-5F, -2F, -2F, 4, 18, 4);
        rightArm.setRotationPoint(-7F, 8F, 3F);
        rightArm.setTextureSize(128, 64);
        rightArm.mirror = true;
        setRotation(rightArm, 0F, 0F, 0F);
        rightArm.mirror = false;
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        float scale = 1.3f;
        GL11.glScalef(scale, scale, scale);
        GL11.glTranslatef(0.0f, -(6.0f / 16.0f), 0.0f);
        belly.render(f5);
        chest.render(f5);
        head.render(f5);
        nose.render(f5);
        leftHorn.render(f5);
        leftArmSpacer.render(f5);
        leftArm.render(f5);
        leftChain.render(f5);
        leftBall.render(f5);
        rightHorn.render(f5);
        rightChain.render(f5);
        rightBall.render(f5);
        rightArmSpacer.render(f5);
        rightArm.render(f5);
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
        this.rightArm.rotateAngleX = MathHelper.cos(f * 0.3662F + (float) Math.PI) * 1.0F * f1;
        this.leftArm.rotateAngleX = MathHelper.cos(f * 0.3662F) * 1.0F * f1;
        this.rightArmSpacer.rotateAngleX = MathHelper.cos(f * 0.3662F + (float) Math.PI) * 1.0F * f1;
        this.leftArmSpacer.rotateAngleX = MathHelper.cos(f * 0.3662F) * 1.0F * f1;
        this.leftBall.rotateAngleX = this.head.rotateAngleX;
        this.leftBall.rotateAngleY = this.head.rotateAngleY;
        this.rightBall.rotateAngleX = this.head.rotateAngleX;
        this.rightBall.rotateAngleY = this.head.rotateAngleY;
        this.leftChain.rotateAngleX = this.head.rotateAngleX;
        this.leftChain.rotateAngleY = this.head.rotateAngleY;
        this.rightChain.rotateAngleX = this.head.rotateAngleX;
        this.rightChain.rotateAngleY = this.head.rotateAngleY;
        this.leftHorn.rotateAngleX = this.head.rotateAngleX;
        this.leftHorn.rotateAngleY = this.head.rotateAngleY;
        this.rightHorn.rotateAngleX = this.head.rotateAngleX;
        this.rightHorn.rotateAngleY = this.head.rotateAngleY;
        this.nose.rotateAngleX = this.head.rotateAngleX;
        this.nose.rotateAngleY = this.head.rotateAngleY;
    }
}