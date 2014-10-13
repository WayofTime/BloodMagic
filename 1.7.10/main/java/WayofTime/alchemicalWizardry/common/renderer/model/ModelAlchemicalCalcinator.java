package WayofTime.alchemicalWizardry.common.renderer.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelAlchemicalCalcinator extends ModelBase
{
    //fields
    ModelRenderer top1;
    ModelRenderer top2;
    ModelRenderer top3;
    ModelRenderer top4;
    ModelRenderer tank;
    ModelRenderer centreCollumn;
    ModelRenderer furnaceShape;
    ModelRenderer glassWindow;
    ModelRenderer Shape1;
    ModelRenderer Shape2;
    ModelRenderer Shape3;
    ModelRenderer Shape4;
    ModelRenderer Shape5;

    public ModelAlchemicalCalcinator()
    {
        textureWidth = 128;
        textureHeight = 128;

        top1 = new ModelRenderer(this, 0, 34);
        top1.addBox(4F, -8F, -8F, 4, 3, 16);
        top1.setRotationPoint(0F, 16F, 0F);
        top1.setTextureSize(128, 128);
        top1.mirror = true;
        setRotation(top1, 0F, 0F, 0F);
        top2 = new ModelRenderer(this, 41, 34);
        top2.addBox(-8F, -8F, -8F, 4, 3, 16);
        top2.setRotationPoint(0F, 16F, 0F);
        top2.setTextureSize(128, 128);
        top2.mirror = true;
        setRotation(top2, 0F, 0F, 0F);
        top3 = new ModelRenderer(this, 25, 55);
        top3.addBox(-4F, -8F, 4F, 8, 3, 4);
        top3.setRotationPoint(0F, 16F, 0F);
        top3.setTextureSize(128, 128);
        top3.mirror = true;
        setRotation(top3, 0F, 0F, 0F);
        top4 = new ModelRenderer(this, 0, 55);
        top4.addBox(-4F, -8F, -8F, 8, 3, 4);
        top4.setRotationPoint(0F, 16F, 0F);
        top4.setTextureSize(128, 128);
        top4.mirror = true;
        setRotation(top4, 0F, 0F, 0F);
        tank = new ModelRenderer(this, 0, 0);
        tank.addBox(-8F, -5F, -8F, 16, 4, 16);
        tank.setRotationPoint(0F, 16F, 0F);
        tank.setTextureSize(128, 128);
        tank.mirror = true;
        setRotation(tank, 0F, 0F, 0F);
        centreCollumn = new ModelRenderer(this, 0, 21);
        centreCollumn.addBox(-4F, -5F, -4F, 8, 4, 8);
        centreCollumn.setRotationPoint(0F, 16F, 0F);
        centreCollumn.setTextureSize(128, 128);
        centreCollumn.mirror = true;
        setRotation(centreCollumn, 0F, 0F, 0F);
        furnaceShape = new ModelRenderer(this, 0, 63);
        furnaceShape.addBox(-8F, -1F, -8F, 16, 5, 16);
        furnaceShape.setRotationPoint(0F, 16F, 0F);
        furnaceShape.setTextureSize(128, 128);
        furnaceShape.mirror = true;
        setRotation(furnaceShape, 0F, 0F, 0F);
        glassWindow = new ModelRenderer(this, 0, 85);
        glassWindow.addBox(-4F, -8F, -4F, 8, 0, 8);
        glassWindow.setRotationPoint(0F, 16F, 0F);
        glassWindow.setTextureSize(128, 128);
        glassWindow.mirror = true;
        setRotation(glassWindow, 0F, 0F, 0F);
        Shape1 = new ModelRenderer(this, 0, 94);
        Shape1.addBox(-8F, 4F, -8F, 4, 4, 4);
        Shape1.setRotationPoint(0F, 16F, 0F);
        Shape1.setTextureSize(128, 128);
        Shape1.mirror = true;
        setRotation(Shape1, 0F, 0F, 0F);
        Shape2 = new ModelRenderer(this, 0, 103);
        Shape2.addBox(-4F, 6F, -4F, 8, 1, 8);
        Shape2.setRotationPoint(0F, 16F, 0F);
        Shape2.setTextureSize(128, 128);
        Shape2.mirror = true;
        setRotation(Shape2, 0F, 0F, 0F);
        Shape3 = new ModelRenderer(this, 0, 94);
        Shape3.addBox(4F, 4F, -8F, 4, 4, 4);
        Shape3.setRotationPoint(0F, 16F, 0F);
        Shape3.setTextureSize(128, 128);
        Shape3.mirror = true;
        setRotation(Shape3, 0F, 0F, 0F);
        Shape4 = new ModelRenderer(this, 0, 94);
        Shape4.addBox(-8F, 4F, 4F, 4, 4, 4);
        Shape4.setRotationPoint(0F, 16F, 0F);
        Shape4.setTextureSize(128, 128);
        Shape4.mirror = true;
        setRotation(Shape4, 0F, 0F, 0F);
        Shape5 = new ModelRenderer(this, 0, 94);
        Shape5.addBox(4F, 4F, 4F, 4, 4, 4);
        Shape5.setRotationPoint(0F, 16F, 0F);
        Shape5.setTextureSize(128, 128);
        Shape5.mirror = true;
        setRotation(Shape5, 0F, 0F, 0F);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        top1.render(f5);
        top2.render(f5);
        top3.render(f5);
        top4.render(f5);
        tank.render(f5);
        centreCollumn.render(f5);
        furnaceShape.render(f5);
        glassWindow.render(f5);
        Shape1.render(f5);
        Shape2.render(f5);
        Shape3.render(f5);
        Shape4.render(f5);
        Shape5.render(f5);
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
    }

}
