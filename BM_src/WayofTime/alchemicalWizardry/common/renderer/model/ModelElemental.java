package WayofTime.alchemicalWizardry.common.renderer.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.world.World;

public class ModelElemental extends ModelBase
{
    //fields
    ModelRenderer body;
    ModelRenderer Shape2;
    ModelRenderer Shape1;
    ModelRenderer Shape3;

    public ModelElemental()
    {
        textureWidth = 64;
        textureHeight = 32;
        body = new ModelRenderer(this, 33, 0);
        body.addBox(-3F, -3F, -3F, 6, 6, 6);
        body.setRotationPoint(0F, 14F, 0F);
        body.setTextureSize(64, 32);
        body.mirror = true;
        setRotation(body, 0F, 0F, 0F);
        Shape2 = new ModelRenderer(this, 0, 0);
        Shape2.addBox(-4F, -4F, -4F, 8, 8, 8);
        Shape2.setRotationPoint(0F, 14F, 0F);
        Shape2.setTextureSize(64, 32);
        Shape2.mirror = true;
        setRotation(Shape2, ((float) Math.PI / 4F), ((float) Math.PI / 4F), 0F);
        Shape1 = new ModelRenderer(this, 0, 0);
        Shape1.addBox(-4F, -4F, -4F, 8, 8, 8);
        Shape1.setRotationPoint(0F, 14F, 0F);
        Shape1.setTextureSize(64, 32);
        Shape1.mirror = true;
        setRotation(Shape1, 0F, ((float) Math.PI / 4F), ((float) Math.PI / 4F));
        Shape3 = new ModelRenderer(this, 0, 0);
        Shape3.addBox(-4F, -4F, -4F, 8, 8, 8);
        Shape3.setRotationPoint(0F, 14F, 0F);
        Shape3.setTextureSize(64, 32);
        Shape3.mirror = true;
        setRotation(Shape3, ((float) Math.PI / 4F), 0F, ((float) Math.PI / 4F));
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        body.render(f5);
        Shape2.render(f5);
        Shape1.render(f5);
        Shape3.render(f5);
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
        World world = entity.worldObj;

        if (world == null)
        {
            return;
        }

        int ratio = 20;
        float rot = (entity.worldObj.getWorldTime() % ratio) / ratio;
        Shape1.rotateAngleX = f / 5;
        Shape2.rotateAngleZ = f / 5;
        Shape3.rotateAngleY = f / 5;
        EntityBlaze d;
    }
}
