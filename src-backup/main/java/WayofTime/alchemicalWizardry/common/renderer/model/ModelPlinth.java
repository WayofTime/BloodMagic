package WayofTime.alchemicalWizardry.common.renderer.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelPlinth extends ModelBase
{
    //fields
    ModelRenderer base;
    ModelRenderer table;
    ModelRenderer pillar;
    ModelRenderer edge1;
    ModelRenderer edge2;
    ModelRenderer edge3;
    ModelRenderer edge4;

    public ModelPlinth()
    {
        textureWidth = 64;
        textureHeight = 64;
        base = new ModelRenderer(this, 0, 16);
        base.addBox(0F, 0F, 0F, 10, 2, 10);
        base.setRotationPoint(-5F, 22F, -5F);
        base.setTextureSize(64, 64);
        base.mirror = true;
        setRotation(base, 0F, 0F, 0F);
        table = new ModelRenderer(this, 0, 0);
        table.addBox(0F, 0F, 0F, 14, 1, 14);
        table.setRotationPoint(-7F, 11F, -7F);
        table.setTextureSize(64, 64);
        table.mirror = true;
        setRotation(table, 0F, 0F, 0F);
        pillar = new ModelRenderer(this, 0, 32);
        pillar.addBox(0F, 0F, 0F, 6, 10, 6);
        pillar.setRotationPoint(-3F, 12F, -3F);
        pillar.setTextureSize(64, 64);
        pillar.mirror = true;
        setRotation(pillar, 0F, 0F, 0F);
        edge1 = new ModelRenderer(this, 0, 29);
        edge1.addBox(0F, 0F, 0F, 14, 1, 1);
        edge1.setRotationPoint(-7F, 10F, 6F);
        edge1.setTextureSize(64, 64);
        edge1.mirror = true;
        setRotation(edge1, 0F, 0F, 0F);
        edge2 = new ModelRenderer(this, 0, 29);
        edge2.addBox(0F, 0F, 0F, 14, 1, 1);
        edge2.setRotationPoint(-7F, 10F, -7F);
        edge2.setTextureSize(64, 64);
        edge2.mirror = true;
        setRotation(edge2, 0F, 0F, 0F);
        edge3 = new ModelRenderer(this, 25, 32);
        edge3.addBox(0F, 0F, 0F, 1, 1, 12);
        edge3.setRotationPoint(-7F, 10F, -6F);
        edge3.setTextureSize(64, 64);
        edge3.mirror = true;
        setRotation(edge3, 0F, 0F, 0F);
        edge4 = new ModelRenderer(this, 25, 32);
        edge4.addBox(0F, 0F, 0F, 1, 1, 12);
        edge4.setRotationPoint(6F, 10F, -6F);
        edge4.setTextureSize(64, 64);
        edge4.mirror = true;
        setRotation(edge4, 0F, 0F, 0F);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        base.render(f5);
        table.render(f5);
        pillar.render(f5);
        edge1.render(f5);
        edge2.render(f5);
        edge3.render(f5);
        edge4.render(f5);
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
