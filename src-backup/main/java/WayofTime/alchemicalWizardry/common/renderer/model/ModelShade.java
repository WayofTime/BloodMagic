package WayofTime.alchemicalWizardry.common.renderer.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelShade extends ModelBase
{
    //fields
    ModelRenderer body;
    ModelRenderer tail1;
    ModelRenderer leftArm;
    ModelRenderer rightArm;
    ModelRenderer tail2;
    ModelRenderer head;

    public ModelShade()
    {
        textureWidth = 64;
        textureHeight = 64;
        body = new ModelRenderer(this, 0, 45);
        body.addBox(-6F, 0F, -3F, 12, 12, 6);
        body.setRotationPoint(0F, -4F, 0F);
        body.setTextureSize(64, 64);
        body.mirror = true;
        setRotation(body, 0F, 0F, 0F);
        tail1 = new ModelRenderer(this, 37, 43);
        tail1.addBox(-2F, 1F, -2F, 4, 6, 4);
        tail1.setRotationPoint(0F, 8F, 0F);
        tail1.setTextureSize(64, 64);
        tail1.mirror = true;
        setRotation(tail1, 0.122173F, 0F, 0F);
        leftArm = new ModelRenderer(this, 0, 0);
        leftArm.addBox(0F, -4F, -2F, 4, 20, 4);
        leftArm.setRotationPoint(6F, -2F, 0F);
        leftArm.setTextureSize(64, 64);
        leftArm.mirror = true;
        setRotation(leftArm, 0F, 0F, 0F);
        rightArm = new ModelRenderer(this, 0, 0);
        rightArm.mirror = true;
        rightArm.addBox(-4F, -4F, -2F, 4, 20, 4);
        rightArm.setRotationPoint(-6F, -2F, 0F);
        rightArm.setTextureSize(64, 64);
        rightArm.mirror = true;
        setRotation(rightArm, 0F, 0F, 0F);
        rightArm.mirror = false;
        tail2 = new ModelRenderer(this, 37, 54);
        tail2.addBox(-1.5F, 6F, -3F, 3, 6, 3);
        tail2.setRotationPoint(0F, 8F, 0F);
        tail2.setTextureSize(64, 64);
        tail2.mirror = true;
        setRotation(tail2, 0.4363323F, 0F, 0F);
        head = new ModelRenderer(this, 17, 0);
        head.addBox(-4F, -8F, -4F, 8, 8, 8);
        head.setRotationPoint(0F, -4F, -1F);
        head.setTextureSize(64, 64);
        head.mirror = true;
        setRotation(head, 0F, 0F, 0F);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        body.render(f5);
        tail1.render(f5);
        leftArm.render(f5);
        rightArm.render(f5);
        tail2.render(f5);
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
    }
}
