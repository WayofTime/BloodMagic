package WayofTime.alchemicalWizardry.common.renderer.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelWritingTable extends ModelBase
{
    //fields
    ModelRenderer base;
    ModelRenderer support;
    ModelRenderer appendage1;
    ModelRenderer appendage2;
    ModelRenderer appendage3;
    ModelRenderer appendage4;
    ModelRenderer appendage5;
    ModelRenderer outputPad;
    ModelRenderer input1;
    ModelRenderer input5;
    ModelRenderer input4;
    ModelRenderer input3;
    ModelRenderer Shape1;

    public ModelWritingTable()
    {
        textureWidth = 64;
        textureHeight = 64;
        base = new ModelRenderer(this, 0, 0);
        base.addBox(0F, 0F, 0F, 16, 2, 16);
        base.setRotationPoint(-8F, 22F, -8F);
        base.setTextureSize(64, 32);
        base.mirror = true;
        setRotation(base, 0F, 0F, 0F);
        support = new ModelRenderer(this, 0, 0);
        support.addBox(0F, 0F, 0F, 2, 12, 2);
        support.setRotationPoint(-1F, 10F, -1F);
        support.setTextureSize(64, 32);
        support.mirror = true;
        setRotation(support, 0F, 0F, 0F);
        appendage1 = new ModelRenderer(this, 48, 0);
        appendage1.addBox(1F, 0F, 0F, 7, 11, 0);
        appendage1.setRotationPoint(0F, 10F, 0F);
        appendage1.setTextureSize(64, 32);
        appendage1.mirror = true;
        setRotation(appendage1, 0F, 0F, 0F);
        appendage2 = new ModelRenderer(this, 48, 0);
        appendage2.addBox(1F, 0F, 0F, 7, 11, 0);
        appendage2.setRotationPoint(0F, 10F, 0F);
        appendage2.setTextureSize(64, 32);
        appendage2.mirror = true;
        setRotation(appendage2, 0F, ((float) Math.PI * 2F / 5F), 0F);
        appendage3 = new ModelRenderer(this, 48, 0);
        appendage3.addBox(1F, 0F, 0F, 7, 11, 0);
        appendage3.setRotationPoint(0F, 10F, 0F);
        appendage3.setTextureSize(64, 32);
        appendage3.mirror = true;
        setRotation(appendage3, 0F, 2.513274F, 0F);
        appendage4 = new ModelRenderer(this, 48, 0);
        appendage4.addBox(1F, 0F, 0F, 7, 11, 0);
        appendage4.setRotationPoint(0F, 10F, 0F);
        appendage4.setTextureSize(64, 32);
        appendage4.mirror = true;
        setRotation(appendage4, 0F, -2.513274F, 0F);
        appendage5 = new ModelRenderer(this, 48, 0);
        appendage5.addBox(1F, 0F, 0F, 7, 11, 0);
        appendage5.setRotationPoint(0F, 10F, 0F);
        appendage5.setTextureSize(64, 32);
        appendage5.mirror = true;
        setRotation(appendage5, 0F, -((float) Math.PI * 2F / 5F), 0F);
        outputPad = new ModelRenderer(this, 0, 20);
        outputPad.addBox(0F, 0F, 0F, 4, 1, 4);
        outputPad.setRotationPoint(-2F, 9F, -2F);
        outputPad.setTextureSize(64, 64);
        outputPad.mirror = true;
        setRotation(outputPad, 0F, 0F, 0F);
        input1 = new ModelRenderer(this, 0, 20);
        input1.addBox(4F, 0F, -2F, 4, 1, 4);
        input1.setRotationPoint(0F, 21F, 0F);
        input1.setTextureSize(64, 64);
        input1.mirror = true;
        setRotation(input1, 0F, 0F, 0F);
        input5 = new ModelRenderer(this, 0, 20);
        input5.addBox(0F, 0F, 0F, 4, 1, 4);
        input5.setRotationPoint(0F, 21F, -8F);
        input5.setTextureSize(64, 64);
        input5.mirror = true;
        setRotation(input5, 0F, 0F, 0F);
        input4 = new ModelRenderer(this, 0, 20);
        input4.addBox(-7F, 0F, -6F, 4, 1, 4);
        input4.setRotationPoint(0F, 21F, 0F);
        input4.setTextureSize(64, 64);
        input4.mirror = true;
        setRotation(input4, 0F, 0F, 0F);
        input3 = new ModelRenderer(this, 0, 20);
        input3.addBox(-7F, 0F, 2F, 4, 1, 4);
        input3.setRotationPoint(0F, 21F, 0F);
        input3.setTextureSize(64, 64);
        input3.mirror = true;
        setRotation(input3, 0F, 0F, 0F);
        Shape1 = new ModelRenderer(this, 0, 20);
        Shape1.addBox(0F, 0F, 4F, 4, 1, 4);
        Shape1.setRotationPoint(0F, 21F, 0F);
        Shape1.setTextureSize(64, 64);
        Shape1.mirror = true;
        setRotation(Shape1, 0F, 0F, 0F);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        base.render(f5);
        support.render(f5);
        appendage1.render(f5);
        appendage2.render(f5);
        appendage3.render(f5);
        appendage4.render(f5);
        appendage5.render(f5);
        outputPad.render(f5);
        input1.render(f5);
        input5.render(f5);
        input4.render(f5);
        input3.render(f5);
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
    }
}
