package WayofTime.alchemicalWizardry.common.renderer.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class ModelSmallEarthGolem extends ModelBase
{
    //fields
    ModelRenderer leftLeg;
    ModelRenderer rightLeg;
    ModelRenderer body;
    ModelRenderer head;
    ModelRenderer chest1;
    ModelRenderer chest2;
    ModelRenderer chest3;
    ModelRenderer leftArm;
    ModelRenderer rightArm;
    ModelRenderer back1;

    public ModelSmallEarthGolem()
    {
        textureWidth = 32;
        textureHeight = 32;
        leftLeg = new ModelRenderer(this, 13, 0);
        leftLeg.addBox(-1F, 0F, -1F, 2, 5, 2);
        leftLeg.setRotationPoint(1F, 19F, 0F);
        leftLeg.setTextureSize(32, 32);
        leftLeg.mirror = true;
        setRotation(leftLeg, 0F, 0F, 0F);
        rightLeg = new ModelRenderer(this, 13, 0);
        rightLeg.mirror = true;
        rightLeg.addBox(-1F, 0F, -1F, 2, 5, 2);
        rightLeg.setRotationPoint(-1F, 19F, 0F);
        rightLeg.setTextureSize(32, 32);
        rightLeg.mirror = true;
        setRotation(rightLeg, 0F, 0F, 0F);
        rightLeg.mirror = false;
        body = new ModelRenderer(this, 0, 7);
        body.addBox(-2F, 0F, -1F, 4, 5, 2);
        body.setRotationPoint(0F, 14F, 0F);
        body.setTextureSize(32, 32);
        body.mirror = true;
        setRotation(body, 0F, 0F, 0F);
        head = new ModelRenderer(this, 0, 0);
        head.addBox(-1.5F, -3F, -2F, 3, 3, 3);
        head.setRotationPoint(0F, 14F, 0F);
        head.setTextureSize(32, 32);
        head.mirror = true;
        setRotation(head, 0F, 0F, 0F);
        chest1 = new ModelRenderer(this, 22, 0);
        chest1.addBox(-1F, 0.5F, -2F, 2, 3, 1);
        chest1.setRotationPoint(0F, 14F, 0F);
        chest1.setTextureSize(32, 32);
        chest1.mirror = true;
        setRotation(chest1, 0F, 0F, 0F);
        chest2 = new ModelRenderer(this, 22, 5);
        chest2.addBox(1F, 1.5F, -2F, 1, 1, 1);
        chest2.setRotationPoint(0F, 14F, 0F);
        chest2.setTextureSize(32, 32);
        chest2.mirror = true;
        setRotation(chest2, 0F, 0F, 0F);
        chest3 = new ModelRenderer(this, 22, 5);
        chest3.mirror = true;
        chest3.addBox(-2F, 1.5F, -2F, 1, 1, 1);
        chest3.setRotationPoint(0F, 14F, 0F);
        chest3.setTextureSize(32, 32);
        chest3.mirror = true;
        setRotation(chest3, 0F, 0F, 0F);
        chest3.mirror = false;
        leftArm = new ModelRenderer(this, 13, 7);
        leftArm.addBox(0F, -1F, -1F, 2, 5, 2);
        leftArm.setRotationPoint(2F, 15F, 0F);
        leftArm.setTextureSize(32, 32);
        leftArm.mirror = true;
        setRotation(leftArm, 0F, 0F, 0F);
        rightArm = new ModelRenderer(this, 13, 7);
        rightArm.mirror = true;
        rightArm.addBox(-2F, -1F, -1F, 2, 5, 2);
        rightArm.setRotationPoint(-2F, 15F, 0F);
        rightArm.setTextureSize(32, 32);
        rightArm.mirror = true;
        setRotation(rightArm, 0F, 0F, 0F);
        rightArm.mirror = false;
        back1 = new ModelRenderer(this, 22, 8);
        back1.addBox(-1.5F, 1.5F, 1F, 3, 1, 1);
        back1.setRotationPoint(0F, 14F, 0F);
        back1.setTextureSize(32, 32);
        back1.mirror = true;
        setRotation(back1, 0F, 0F, 0F);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        leftLeg.render(f5);
        rightLeg.render(f5);
        body.render(f5);
        head.render(f5);
        chest1.render(f5);
        chest2.render(f5);
        chest3.render(f5);
        leftArm.render(f5);
        rightArm.render(f5);
        back1.render(f5);
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
    }
}