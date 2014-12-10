package WayofTime.alchemicalWizardry.common.renderer.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class ModelMinorDemonGruntGuardian extends ModelBase
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
    ModelRenderer helmet;
    ModelRenderer leftClaw;
    ModelRenderer rightClaw;
    ModelRenderer leftShoulder;
    ModelRenderer rightShoulder;
    ModelRenderer bar1;
    ModelRenderer bar2;
  
  public ModelMinorDemonGruntGuardian()
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
      helmet = new ModelRenderer(this, 67, 0);
      helmet.addBox(-4.5F, -8.5F, -4.5F, 9, 9, 9);
      helmet.setRotationPoint(0F, 0F, 0F);
      helmet.setTextureSize(128, 64);
      helmet.mirror = true;
      setRotation(helmet, 0F, 0F, 0F);
      leftClaw = new ModelRenderer(this, 67, 23);
      leftClaw.addBox(1.5F, 6F, -2.5F, 2, 6, 5);
      leftClaw.setRotationPoint(6F, 2F, 0F);
      leftClaw.setTextureSize(128, 64);
      leftClaw.mirror = true;
      setRotation(leftClaw, 0F, 0F, 0F);
      rightClaw = new ModelRenderer(this, 67, 23);
      rightClaw.mirror = true;
      rightClaw.addBox(-3.5F, 6F, -2.5F, 2, 6, 5);
      rightClaw.setRotationPoint(-6F, 2F, 0F);
      rightClaw.setTextureSize(128, 64);
      rightClaw.mirror = true;
      setRotation(rightClaw, 0F, 0F, 0F);
      rightClaw.mirror = false;
      leftShoulder = new ModelRenderer(this, 67, 35);
      leftShoulder.addBox(-1F, -2.5F, -2.5F, 5, 4, 5);
      leftShoulder.setRotationPoint(6F, 2F, 0F);
      leftShoulder.setTextureSize(128, 64);
      leftShoulder.mirror = true;
      setRotation(leftShoulder, 0F, 0F, 0F);
      rightShoulder = new ModelRenderer(this, 67, 35);
      rightShoulder.mirror = true;
      rightShoulder.addBox(-4F, -2.5F, -2.5F, 5, 4, 5);
      rightShoulder.setRotationPoint(-6F, 2F, 0F);
      rightShoulder.setTextureSize(128, 64);
      rightShoulder.mirror = true;
      setRotation(rightShoulder, 0F, 0F, 0F);
      rightShoulder.mirror = false;
      bar1 = new ModelRenderer(this, 67, 20);
      bar1.addBox(-3F, 3F, 4F, 14, 1, 1);
      bar1.setRotationPoint(0F, 0F, 0F);
      bar1.setTextureSize(128, 64);
      bar1.mirror = true;
      setRotation(bar1, 0F, 0F, 0.7853982F);
      bar2 = new ModelRenderer(this, 67, 20);
      bar2.addBox(-11F, 3F, 4F, 14, 1, 1);
      bar2.setRotationPoint(0F, 0F, 0F);
      bar2.setTextureSize(128, 64);
      bar2.mirror = true;
      setRotation(bar2, 0F, 0F, -0.7853982F);
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
    helmet.render(f5);
    leftClaw.render(f5);
    rightClaw.render(f5);
    leftShoulder.render(f5);
    rightShoulder.render(f5);
    bar1.render(f5);
    bar2.render(f5);
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
    
    this.helmet.rotateAngleX = this.head.rotateAngleX;
    this.helmet.rotateAngleY = this.head.rotateAngleY;

    this.leftleg.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
    this.rightleg.rotateAngleX = MathHelper.cos(f * 0.6662F + (float) Math.PI) * 1.0F * f1;
    this.rightarm.rotateAngleX = MathHelper.cos(f * 0.6662F + (float) Math.PI) * 1.0F * f1;
    this.leftarm.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
    
    this.leftClaw.rotateAngleX = this.leftarm.rotateAngleX;
    this.leftShoulder.rotateAngleX = this.leftarm.rotateAngleX;
    
    this.rightClaw.rotateAngleX = this.rightarm.rotateAngleX;
    this.rightShoulder.rotateAngleX = this.rightarm.rotateAngleX;
  }
}