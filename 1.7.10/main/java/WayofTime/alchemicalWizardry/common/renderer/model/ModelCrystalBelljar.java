package WayofTime.alchemicalWizardry.common.renderer.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelCrystalBelljar extends ModelBase
{
    //fields
    ModelRenderer handle1;
    ModelRenderer handle2;
    ModelRenderer jar1;
    ModelRenderer woodBottom;
    ModelRenderer jar2;
    ModelRenderer jar3;
    ModelRenderer jar4;
    ModelRenderer jar5;

    public ModelCrystalBelljar()
    {
        textureWidth = 128;
        textureHeight = 64;

        handle1 = new ModelRenderer(this, 0, 17);
        handle1.addBox(-2F, -7F, -2F, 4, 1, 4);
        handle1.setRotationPoint(0F, 16F, 0F);
        handle1.setTextureSize(128, 64);
        handle1.mirror = true;
        setRotation(handle1, 0F, 0F, 0F);
        handle2 = new ModelRenderer(this, 0, 23);
        handle2.addBox(-1F, -6F, -1F, 2, 1, 2);
        handle2.setRotationPoint(0F, 16F, 0F);
        handle2.setTextureSize(128, 64);
        handle2.mirror = true;
        setRotation(handle2, 0F, 0F, 0F);
        jar1 = new ModelRenderer(this, 0, 27);
        jar1.addBox(-4F, -5F, -4F, 8, 1, 8);
        jar1.setRotationPoint(0F, 16F, 0F);
        jar1.setTextureSize(128, 64);
        jar1.mirror = true;
        setRotation(jar1, 0F, 0F, 0F);
        woodBottom = new ModelRenderer(this, 0, 0);
        woodBottom.addBox(-7F, 6F, -7F, 14, 2, 14);
        woodBottom.setRotationPoint(0F, 16F, 0F);
        woodBottom.setTextureSize(128, 64);
        woodBottom.mirror = true;
        setRotation(woodBottom, 0F, 0F, 0F);
        jar2 = new ModelRenderer(this, 0, 38);
        jar2.addBox(-5F, -4F, 4F, 10, 10, 1);
        jar2.setRotationPoint(0F, 16F, 0F);
        jar2.setTextureSize(128, 64);
        jar2.mirror = true;
        setRotation(jar2, 0F, 0F, 0F);
        jar3 = new ModelRenderer(this, 46, 38);
        jar3.addBox(4F, -4F, -4F, 1, 10, 8);
        jar3.setRotationPoint(0F, 16F, 0F);
        jar3.setTextureSize(128, 64);
        jar3.mirror = true;
        setRotation(jar3, 0F, 0F, 0F);
        jar4 = new ModelRenderer(this, 23, 38);
        jar4.addBox(-5F, -4F, -5F, 10, 10, 1);
        jar4.setRotationPoint(0F, 16F, 0F);
        jar4.setTextureSize(128, 64);
        jar4.mirror = true;
        setRotation(jar4, 0F, 0F, 0F);
        jar5 = new ModelRenderer(this, 65, 38);
        jar5.addBox(-5F, -4F, -4F, 1, 10, 8);
        jar5.setRotationPoint(0F, 16F, 0F);
        jar5.setTextureSize(128, 64);
        jar5.mirror = true;
        setRotation(jar5, 0F, 0F, 0F);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        handle1.render(f5);
        handle2.render(f5);
        jar1.render(f5);
        woodBottom.render(f5);
        jar2.render(f5);
        jar3.render(f5);
        jar4.render(f5);
        jar5.render(f5);
    }

    public void renderSpecialItem(Entity entity, float f, float f1, float f2, float f3, float f4, float f5, int part)
    {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        if (part == 0)
        {
            woodBottom.render(f5);
        } else
        {
            handle1.render(f5);
            handle2.render(f5);
            jar1.render(f5);
            jar2.render(f5);
            jar3.render(f5);
            jar4.render(f5);
            jar5.render(f5);
        }
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
