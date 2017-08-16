package WayofTime.bloodmagic.client.render.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * This is a direct copy of the meteor model from 1.7.10. As such it probably
 * needs to be... better.
 */
public class ModelMeteor extends ModelBase {
    //fields
    ModelRenderer Shape1;
    ModelRenderer Shape2;
    ModelRenderer Shape3;
    ModelRenderer Shape4;
    ModelRenderer Shape5;
    ModelRenderer Shape6;
    ModelRenderer Shape7;

    public ModelMeteor() {
        textureWidth = 64;
        textureHeight = 64;
        Shape1 = new ModelRenderer(this, 0, 0);
        Shape1.addBox(-8F, -8F, -8F, 16, 16, 16);
        Shape1.setRotationPoint(0F, 0F, 0F);
        Shape1.setTextureSize(64, 64);
        Shape1.mirror = true;
        setRotation(Shape1, 0F, 0F, 0F);
        Shape2 = new ModelRenderer(this, 0, 32);
        Shape2.addBox(3F, -10F, -1F, 12, 12, 12);
        Shape2.setRotationPoint(0F, 0F, 0F);
        Shape2.setTextureSize(64, 64);
        Shape2.mirror = true;
        setRotation(Shape2, 0F, 0F, 0F);
        Shape3 = new ModelRenderer(this, 0, 32);
        Shape3.addBox(0F, 0F, -10F, 12, 12, 12);
        Shape3.setRotationPoint(0F, 0F, 0F);
        Shape3.setTextureSize(64, 64);
        Shape3.mirror = true;
        setRotation(Shape3, 0F, 0F, 0F);
        Shape4 = new ModelRenderer(this, 0, 32);
        Shape4.addBox(1F, 2F, 2F, 12, 12, 12);
        Shape4.setRotationPoint(0F, 0F, 0F);
        Shape4.setTextureSize(64, 64);
        Shape4.mirror = true;
        setRotation(Shape4, 0F, 0F, 0F);
        Shape5 = new ModelRenderer(this, 0, 32);
        Shape5.addBox(-12F, -5F, 0F, 12, 12, 12);
        Shape5.setRotationPoint(0F, 0F, 0F);
        Shape5.setTextureSize(64, 64);
        Shape5.mirror = true;
        setRotation(Shape5, 0F, 0F, 0F);
        Shape6 = new ModelRenderer(this, 0, 32);
        Shape6.addBox(-13F, -2F, -11F, 12, 12, 12);
        Shape6.setRotationPoint(0F, 0F, 0F);
        Shape6.setTextureSize(64, 64);
        Shape6.mirror = true;
        setRotation(Shape6, 0F, 0F, 0F);
        Shape7 = new ModelRenderer(this, 0, 32);
        Shape7.addBox(-6F, -14F, -9F, 12, 12, 12);
        Shape7.setRotationPoint(0F, 0F, 0F);
        Shape7.setTextureSize(64, 64);
        Shape7.mirror = true;
        setRotation(Shape7, 0F, 0F, 0F);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        Shape1.render(f5);
        Shape2.render(f5);
        Shape3.render(f5);
        Shape4.render(f5);
        Shape5.render(f5);
        Shape6.render(f5);
        Shape7.render(f5);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    }
}