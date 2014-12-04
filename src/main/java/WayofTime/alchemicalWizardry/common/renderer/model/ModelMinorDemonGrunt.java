package WayofTime.alchemicalWizardry.common.renderer.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class ModelMinorDemonGrunt extends ModelBase
{
  //fields
    ModelRenderer head;
    ModelRenderer chest;
    ModelRenderer midrift;
    ModelRenderer rightarm;
    ModelRenderer leftarm;
    ModelRenderer rightleg;
    ModelRenderer leftleg;
    ModelRenderer middle;
  
  public ModelMinorDemonGrunt()
  {
    textureWidth = 128;
    textureHeight = 64;
    
      head = new ModelRenderer(this, 0, 0);
      head.addBox(-4F, -8F, -4F, 8, 8, 8);
      head.setRotationPoint(0F, 0F, 0F);
      head.setTextureSize(128, 64);
      head.mirror = true;
      setRotation(head, 0F, 0F, 0F);
      chest = new ModelRenderer(this, 16, 16);
      chest.addBox(-5F, 0F, -3.5F, 10, 7, 7);
      chest.setRotationPoint(0F, 0F, 0F);
      chest.setTextureSize(128, 64);
      chest.mirror = true;
      setRotation(chest, 0F, 0F, 0F);
      midrift = new ModelRenderer(this, 16, 33);
      midrift.addBox(-4F, 7F, -2F, 8, 5, 4);
      midrift.setRotationPoint(0F, 0F, 0F);
      midrift.setTextureSize(128, 64);
      midrift.mirror = true;
      setRotation(midrift, 0F, 0F, 0F);
      rightarm = new ModelRenderer(this, 50, 16);
      rightarm.mirror = true;
      rightarm.addBox(-3F, -2F, -2F, 4, 12, 4);
      rightarm.setRotationPoint(-6F, 2F, 0F);
      rightarm.setTextureSize(128, 64);
      rightarm.mirror = true;
      setRotation(rightarm, 0F, 0F, 0F);
      rightarm.mirror = false;
      leftarm = new ModelRenderer(this, 50, 16);
      leftarm.addBox(-1F, -2F, -2F, 4, 12, 4);
      leftarm.setRotationPoint(6F, 2F, 0F);
      leftarm.setTextureSize(128, 64);
      leftarm.mirror = true;
      setRotation(leftarm, 0F, 0F, 0F);
      rightleg = new ModelRenderer(this, 0, 16);
      rightleg.mirror = true;
      rightleg.addBox(-2F, 0F, -2F, 4, 12, 4);
      rightleg.setRotationPoint(-2F, 12F, 0F);
      rightleg.setTextureSize(128, 64);
      rightleg.mirror = true;
      setRotation(rightleg, 0F, 0F, 0F);
      rightleg.mirror = false;
      leftleg = new ModelRenderer(this, 0, 16);
      leftleg.addBox(-2F, 0F, -2F, 4, 12, 4);
      leftleg.setRotationPoint(2F, 12F, 0F);
      leftleg.setTextureSize(128, 64);
      leftleg.mirror = true;
      setRotation(leftleg, 0F, 0F, 0F);
      middle = new ModelRenderer(this, 16, 43);
      middle.addBox(-2F, 7F, -3F, 4, 3, 1);
      middle.setRotationPoint(0F, 0F, 0F);
      middle.setTextureSize(128, 64);
      middle.mirror = true;
      setRotation(middle, 0F, 0F, 0F);
  }
  
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
  {
    super.render(entity, f, f1, f2, f3, f4, f5);
    setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    head.render(f5);
    chest.render(f5);
    midrift.render(f5);
    rightarm.render(f5);
    leftarm.render(f5);
    rightleg.render(f5);
    leftleg.render(f5);
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
    
    this.head.rotateAngleX = f4 / (180F / (float) Math.PI);
    this.head.rotateAngleY = f3 / (180F / (float) Math.PI);

    this.leftleg.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
    this.rightleg.rotateAngleX = MathHelper.cos(f * 0.6662F + (float) Math.PI) * 1.0F * f1;
    this.rightarm.rotateAngleX = MathHelper.cos(f * 0.6662F + (float) Math.PI) * 1.0F * f1;
    this.leftarm.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
  }

}
