package WayofTime.alchemicalWizardry.common.renderer.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelEnergyBazookaMainProjectile extends ModelBase
{
    //fields
    ModelRenderer thirdWarHead;
    ModelRenderer firstWarHead;
    ModelRenderer secondWarHead;
    ModelRenderer support1;
    ModelRenderer mainBody;
    ModelRenderer support2;
    ModelRenderer support3;
    ModelRenderer support4;
    ModelRenderer base1;
    ModelRenderer base2;
    ModelRenderer base3;

    public ModelEnergyBazookaMainProjectile()
    {
        textureWidth = 64;
        textureHeight = 32;
        thirdWarHead = new ModelRenderer(this, 43, 0);
        thirdWarHead.addBox(-1F, -1F, -9F, 2, 2, 1);
        thirdWarHead.setRotationPoint(0F, 0F, 0F);
        thirdWarHead.setTextureSize(64, 32);
        thirdWarHead.mirror = true;
        setRotation(thirdWarHead, 0F, 0F, 0F);
        firstWarHead = new ModelRenderer(this, 52, 0);
        firstWarHead.addBox(-2F, -2F, -8F, 4, 4, 2);
        firstWarHead.setRotationPoint(0F, 0F, 0F);
        firstWarHead.setTextureSize(64, 32);
        firstWarHead.mirror = true;
        setRotation(firstWarHead, 0F, 0F, 0F);
        secondWarHead = new ModelRenderer(this, 48, 8);
        secondWarHead.addBox(-3F, -3F, -6F, 6, 6, 2);
        secondWarHead.setRotationPoint(0F, 0F, 0F);
        secondWarHead.setTextureSize(64, 32);
        secondWarHead.mirror = true;
        setRotation(secondWarHead, 0F, 0F, 0F);
        support1 = new ModelRenderer(this, 0, 0);
        support1.addBox(2F, 2F, -4F, 1, 1, 9);
        support1.setRotationPoint(0F, 0F, 0F);
        support1.setTextureSize(64, 32);
        support1.mirror = true;
        setRotation(support1, 0F, 0F, 0F);
        mainBody = new ModelRenderer(this, 0, 19);
        mainBody.addBox(-2F, -2F, -4F, 4, 4, 9);
        mainBody.setRotationPoint(0F, 0F, 0F);
        mainBody.setTextureSize(64, 32);
        mainBody.mirror = true;
        setRotation(mainBody, 0F, 0F, 0F);
        support2 = new ModelRenderer(this, 0, 0);
        support2.addBox(-3F, 2F, -4F, 1, 1, 9);
        support2.setRotationPoint(0F, 0F, 0F);
        support2.setTextureSize(64, 32);
        support2.mirror = true;
        setRotation(support2, 0F, 0F, 0F);
        support3 = new ModelRenderer(this, 0, 0);
        support3.addBox(-3F, -3F, -4F, 1, 1, 9);
        support3.setRotationPoint(0F, 0F, 0F);
        support3.setTextureSize(64, 32);
        support3.mirror = true;
        setRotation(support3, 0F, 0F, 0F);
        support4 = new ModelRenderer(this, 0, 0);
        support4.addBox(2F, -3F, -4F, 1, 1, 9);
        support4.setRotationPoint(0F, 0F, 0F);
        support4.setTextureSize(64, 32);
        support4.mirror = true;
        setRotation(support4, 0F, 0F, 0F);
        base1 = new ModelRenderer(this, 28, 0);
        base1.addBox(-3F, -3F, 5F, 6, 6, 1);
        base1.setRotationPoint(0F, 0F, 0F);
        base1.setTextureSize(64, 32);
        base1.mirror = true;
        setRotation(base1, 0F, 0F, 0F);
        base2 = new ModelRenderer(this, 28, 9);
        base2.addBox(-2F, -2F, 6F, 4, 4, 1);
        base2.setRotationPoint(0F, 0F, 0F);
        base2.setTextureSize(64, 32);
        base2.mirror = true;
        setRotation(base2, 0F, 0F, 0F);
        base3 = new ModelRenderer(this, 28, 15);
        base3.addBox(-3F, -3F, 7F, 6, 6, 1);
        base3.setRotationPoint(0F, 0F, 0F);
        base3.setTextureSize(64, 32);
        base3.mirror = true;
        setRotation(base3, 0F, 0F, 0F);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        thirdWarHead.render(f5);
        firstWarHead.render(f5);
        secondWarHead.render(f5);
        support1.render(f5);
        mainBody.render(f5);
        support2.render(f5);
        support3.render(f5);
        support4.render(f5);
        base1.render(f5);
        base2.render(f5);
        base3.render(f5);
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