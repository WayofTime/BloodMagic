package WayofTime.alchemicalWizardry.common.renderer.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelPedestal extends ModelBase
{
    //fields
    ModelRenderer base;
    ModelRenderer top;
    ModelRenderer middle;

    public ModelPedestal()
    {
        textureWidth = 64;
        textureHeight = 32;
        base = new ModelRenderer(this, 0, 0);
        base.addBox(0F, 0F, 0F, 10, 1, 10);
        base.setRotationPoint(-5F, 23F, -5F);
        base.setTextureSize(64, 32);
        base.mirror = true;
        setRotation(base, 0F, 0F, 0F);
        top = new ModelRenderer(this, 0, 19);
        top.addBox(0F, 0F, 0F, 6, 1, 6);
        top.setRotationPoint(-3F, 14F, -3F);
        top.setTextureSize(64, 32);
        top.mirror = true;
        setRotation(top, 0F, 0F, 0F);
        middle = new ModelRenderer(this, 50, 0);
        middle.addBox(0F, 0F, 0F, 2, 8, 2);
        middle.setRotationPoint(-1F, 15F, -1F);
        middle.setTextureSize(64, 32);
        middle.mirror = true;
        setRotation(middle, 0F, 0F, 0F);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        base.render(f5);
        top.render(f5);
        middle.render(f5);
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
