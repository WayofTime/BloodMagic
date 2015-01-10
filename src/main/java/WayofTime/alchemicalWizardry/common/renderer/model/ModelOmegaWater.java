package WayofTime.alchemicalWizardry.common.renderer.model;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelOmegaWater extends ModelBiped
{
  //fields
    ModelRenderer belt;

    ModelRenderer chestMain;
    ModelRenderer chestPlate1;
    ModelRenderer chestPlate2;
    ModelRenderer chestPlate3;
    ModelRenderer chestPlate4;
    ModelRenderer leftTank;
    ModelRenderer rightTank;
    ModelRenderer tankBrace;
    ModelRenderer leftArmPlate1;
    ModelRenderer leftArmPlate2;
    ModelRenderer leftShoulder;
    ModelRenderer leftArm;
    ModelRenderer rightArm;
    ModelRenderer rightArmPlate1;
    ModelRenderer rightArmPlate2;
    ModelRenderer rightShoulder;
    ModelRenderer leftLeg;
    ModelRenderer leftLegBrace;
    ModelRenderer leftLegPouch;
    ModelRenderer rightLeg;
    ModelRenderer leftFoot;
    ModelRenderer leftFootBrace;
    ModelRenderer leftFootPlate;
    ModelRenderer rightFoot;
    ModelRenderer rightFootBrace;
    ModelRenderer rightFootPlate;
    ModelRenderer topHeadPlate;
    ModelRenderer facePlate1;
    ModelRenderer facePlate2;
    ModelRenderer facePlate3;
    ModelRenderer facePlate4;
    ModelRenderer facePlate5;
    ModelRenderer leftPlate1;
    ModelRenderer leftPlate2;
    ModelRenderer leftPlate3;
    ModelRenderer backPlate2;
    ModelRenderer leftPlate4;
    ModelRenderer backPlate1;
    ModelRenderer rightPlate1;
    ModelRenderer rightPlate2;
    ModelRenderer rightPlate3;
    ModelRenderer rightPlate4;
  
    public ModelOmegaWater(float f, boolean addHelmet, boolean addChestPiece, boolean addLeggings, boolean addBoots)
  {
        super(f, 0.0f, 256, 128);
    textureWidth = 256;
    textureHeight = 128;
    
      belt = new ModelRenderer(this, 29, 42);
      belt.addBox(-5F, 0F, -3F, 10, 2, 6);
      belt.setRotationPoint(0F, 11F, 0F);
      belt.setTextureSize(256, 128);
      belt.mirror = true;
      setRotation(belt, 0F, 0F, 0F);
      
      chestMain = new ModelRenderer(this, 0, 42);
      chestMain.addBox(-4.5F, -0.5F, -2.5F, 9, 13, 5);
      chestMain.setRotationPoint(0F, 0F, 0F);
      chestMain.setTextureSize(256, 128);
      chestMain.mirror = true;
      setRotation(chestMain, 0F, 0F, 0F);
      
      chestPlate1 = new ModelRenderer(this, 0, 60);
      chestPlate1.addBox(-4F, 1F, -2.5F, 8, 3, 2);
      chestPlate1.setRotationPoint(0F, 0F, 0F);
      chestPlate1.setTextureSize(256, 128);
      chestPlate1.mirror = true;
      setRotation(chestPlate1, -0.3490659F, 0F, 0F);
      
      chestPlate2 = new ModelRenderer(this, 0, 66);
      chestPlate2.mirror = true;
      chestPlate2.addBox(-4.5F, 2.5F, -3.5F, 3, 3, 1);
      chestPlate2.setRotationPoint(0F, 0F, 0F);
      chestPlate2.setTextureSize(256, 128);
      chestPlate2.mirror = true;
      setRotation(chestPlate2, 0F, 0F, 0F);
      chestPlate2.mirror = false;
      
      chestPlate3 = new ModelRenderer(this, 0, 66);
      chestPlate3.addBox(1.5F, 2.5F, -3.5F, 3, 3, 1);
      chestPlate3.setRotationPoint(0F, 0F, 0F);
      chestPlate3.setTextureSize(256, 128);
      chestPlate3.mirror = true;
      setRotation(chestPlate3, 0F, 0F, 0F);
      
      chestPlate4 = new ModelRenderer(this, 0, 71);
      chestPlate4.addBox(-1.5F, 4.5F, -3.5F, 3, 5, 1);
      chestPlate4.setRotationPoint(0F, 0F, 0F);
      chestPlate4.setTextureSize(256, 128);
      chestPlate4.mirror = true;
      setRotation(chestPlate4, 0F, 0F, 0F);
      
      leftTank = new ModelRenderer(this, 9, 66);
      leftTank.addBox(1F, 1F, 2.5F, 2, 8, 1);
      leftTank.setRotationPoint(0F, 0F, 0F);
      leftTank.setTextureSize(256, 128);
      leftTank.mirror = true;
      setRotation(leftTank, 0F, 0F, 0F);
      
      rightTank = new ModelRenderer(this, 9, 66);
      rightTank.addBox(-3F, 1F, 2.5F, 2, 8, 1);
      rightTank.setRotationPoint(0F, 0F, 0F);
      rightTank.setTextureSize(256, 128);
      rightTank.mirror = true;
      setRotation(rightTank, 0F, 0F, 0F);
      
      tankBrace = new ModelRenderer(this, 0, 78);
      tankBrace.addBox(-4F, 1.5F, 2F, 8, 2, 1);
      tankBrace.setRotationPoint(0F, 0F, 0F);
      tankBrace.setTextureSize(256, 128);
      tankBrace.mirror = true;
      setRotation(tankBrace, 0F, 0F, 0F);
      
      leftArmPlate1 = new ModelRenderer(this, 0, 82);
      leftArmPlate1.addBox(-0.4F-4F, -2F-3F, -4F-1, 6, 3, 1);
      leftArmPlate1.setRotationPoint(5F, 2F, 0F);
      leftArmPlate1.setTextureSize(256, 128);
      leftArmPlate1.mirror = true;
      setRotation(leftArmPlate1, -0.3490659F, 0F, -0.2617994F);
      
      leftArmPlate2 = new ModelRenderer(this, 0, 82);
      leftArmPlate2.addBox(-0.4F-4F, -2F-3F, -4F-1, 6, 3, 1);
      leftArmPlate2.setRotationPoint(5F, 4F, 0F);
      leftArmPlate2.setTextureSize(256, 128);
      leftArmPlate2.mirror = true;
      setRotation(leftArmPlate2, -0.3490659F, 0F, -0.2617994F);
      
      leftShoulder = new ModelRenderer(this, 21, 82);
      leftShoulder.addBox(-1F-5F, -3F-2F, -3F, 6, 5, 6);
      leftShoulder.setRotationPoint(5F, 2F, 0F);
      leftShoulder.setTextureSize(256, 128);
      leftShoulder.mirror = true;
      setRotation(leftShoulder, 0F, 0F, 0F);
      
      leftArm = new ModelRenderer(this, 0, 87);
      leftArm.addBox(-1F-5.5F, 1.5F-2F, -2.5F, 5, 9, 5);
      leftArm.setRotationPoint(5F, 2F, 0F);
      leftArm.setTextureSize(256, 128);
      leftArm.mirror = true;
      setRotation(leftArm, 0F, 0F, 0F);
      
      rightArm = new ModelRenderer(this, 0, 87);
      rightArm.mirror = true;
      rightArm.addBox(-4F+5.5F, 1.5F-2F, -2.5F, 5, 9, 5);
      rightArm.setRotationPoint(-5F, 2F, 0F);
      rightArm.setTextureSize(256, 128);
      rightArm.mirror = true;
      setRotation(rightArm, 0F, 0F, 0F);
      rightArm.mirror = false;
      
      rightArmPlate1 = new ModelRenderer(this, 0, 82);
      rightArmPlate1.mirror = true;
      rightArmPlate1.addBox(-4.6F+4F, -2.2F-3F, -4F-1, 6, 3, 1);
      rightArmPlate1.setRotationPoint(-6F, 2F, 0F);
      rightArmPlate1.setTextureSize(256, 128);
      rightArmPlate1.mirror = true;
      setRotation(rightArmPlate1, -0.3490659F, 0F, 0.2617994F);
      rightArmPlate1.mirror = false;
      
      rightArmPlate2 = new ModelRenderer(this, 0, 82);
      rightArmPlate2.mirror = true;
      rightArmPlate2.addBox(-4.6F+4F, -2.2F-3F, -4F-1, 6, 3, 1);
      rightArmPlate2.setRotationPoint(-6F, 4F, 0F);
      rightArmPlate2.setTextureSize(256, 128);
      rightArmPlate2.mirror = true;
      setRotation(rightArmPlate2, -0.3490659F, 0F, 0.2617994F);
      rightArmPlate2.mirror = false;
      
      rightShoulder = new ModelRenderer(this, 21, 82);
      rightShoulder.mirror = true;
      rightShoulder.addBox(-4F+5F, -3F-2F, -3F, 6, 5, 6);
      rightShoulder.setRotationPoint(-6F, 2F, 0F);
      rightShoulder.setTextureSize(256, 128);
      rightShoulder.mirror = true;
      setRotation(rightShoulder, 0F, 0F, 0F);
      rightShoulder.mirror = false;
      
      leftLeg = new ModelRenderer(this, 29, 51);
      leftLeg.addBox(-4.5F, -12F, -2.5F, 5, 10, 5);
      leftLeg.setRotationPoint(2F, 12F, 0F);
      leftLeg.setTextureSize(256, 128);
      leftLeg.mirror = true;
      setRotation(leftLeg, 0F, 0F, 0F);
      
      leftLegBrace = new ModelRenderer(this, 38, 67);
      leftLegBrace.addBox(-2F, -7F, -3F, 3, 1, 6);
      leftLegBrace.setRotationPoint(2F, 12F, 0F);
      leftLegBrace.setTextureSize(256, 128);
      leftLegBrace.mirror = true;
      setRotation(leftLegBrace, 0F, 0F, 0F);
      
      leftLegPouch = new ModelRenderer(this, 29, 67);
      leftLegPouch.addBox(0.5F, 3F-12F, -1.5F, 1, 4, 3);
      leftLegPouch.setRotationPoint(2F, 12F, 0F);
      leftLegPouch.setTextureSize(256, 128);
      leftLegPouch.mirror = true;
      setRotation(leftLegPouch, 0F, 0F, 0F);
      
      rightLeg = new ModelRenderer(this, 29, 51);
      rightLeg.mirror = true;
      rightLeg.addBox(-0.5F, 0F-12F, -2.5F, 5, 10, 5);
      rightLeg.setRotationPoint(-2F, 12F, 0F);
      rightLeg.setTextureSize(256, 128);
      rightLeg.mirror = true;
      setRotation(rightLeg, 0F, 0F, 0F);
      rightLeg.mirror = false;
      
      leftFoot = new ModelRenderer(this, 21, 103);
      leftFoot.addBox(-4F, 9.5F-12F, -5F, 5, 3, 8);
      leftFoot.setRotationPoint(2F, 12F, 0F);
      leftFoot.setTextureSize(256, 128);
      leftFoot.mirror = true;
      setRotation(leftFoot, 0F, 0F, 0F);
      
      leftFootBrace = new ModelRenderer(this, 21, 94);
      leftFootBrace.addBox(-4F, 7.5F-12F, -3F, 5, 2, 6);
      leftFootBrace.setRotationPoint(2F, 12F, 0F);
      leftFootBrace.setTextureSize(256, 128);
      leftFootBrace.mirror = true;
      setRotation(leftFootBrace, 0F, 0F, 0F);
      
      leftFootPlate = new ModelRenderer(this, 21, 115);
      leftFootPlate.addBox(-3.5F, 7F-8F, 4F-9F, 4, 3, 1);
      leftFootPlate.setRotationPoint(2F, 12F, 0F);
      leftFootPlate.setTextureSize(256, 128);
      leftFootPlate.mirror = true;
      setRotation(leftFootPlate, -0.8726646F, 0F, 0F);
      
      rightFoot = new ModelRenderer(this, 21, 103);
      rightFoot.mirror = true;
      rightFoot.addBox(-1F, 9.5F-12F, -5F, 5, 3, 8);
      rightFoot.setRotationPoint(-2F, 12F, 0F);
      rightFoot.setTextureSize(256, 128);
      rightFoot.mirror = true;
      setRotation(rightFoot, 0F, 0F, 0F);
      rightFoot.mirror = false;
      
      rightFootBrace = new ModelRenderer(this, 21, 94);
      rightFootBrace.mirror = true;
      rightFootBrace.addBox(-1F, 7.5F-12F, -3F, 5, 2, 6);
      rightFootBrace.setRotationPoint(-2F, 12F, 0F);
      rightFootBrace.setTextureSize(256, 128);
      rightFootBrace.mirror = true;
      setRotation(rightFootBrace, 0F, 0F, 0F);
      rightFootBrace.mirror = false;
      
      rightFootPlate = new ModelRenderer(this, 21, 115);
      rightFootPlate.mirror = true;
      rightFootPlate.addBox(-0.5F, 7F-8F, 4F-9F, 4, 3, 1);
      rightFootPlate.setRotationPoint(-2F, 12F, 0F);
      rightFootPlate.setTextureSize(256, 128);
      rightFootPlate.mirror = true;
      setRotation(rightFootPlate, -0.8726646F, 0F, 0F);
      rightFootPlate.mirror = false;
      
      topHeadPlate = new ModelRenderer(this, 58, 19);
      topHeadPlate.addBox(-4F, -8.5F, -4.5F, 8, 1, 9);
      topHeadPlate.setRotationPoint(0F, 0F, 0F);
      topHeadPlate.setTextureSize(256, 128);
      topHeadPlate.mirror = true;
      setRotation(topHeadPlate, 0F, 0F, 0F);
      
      facePlate1 = new ModelRenderer(this, 58, 0);
      facePlate1.addBox(-4F, -8.5F, -5F, 8, 4, 1);
      facePlate1.setRotationPoint(0F, 0F, 0F);
      facePlate1.setTextureSize(256, 128);
      facePlate1.mirror = true;
      setRotation(facePlate1, 0F, 0F, 0F);
      
      facePlate2 = new ModelRenderer(this, 58, 6);
      facePlate2.addBox(-1F, -4.5F, -5F, 2, 2, 1);
      facePlate2.setRotationPoint(0F, 0F, 0F);
      facePlate2.setTextureSize(256, 128);
      facePlate2.mirror = true;
      setRotation(facePlate2, 0F, 0F, 0F);
      
      facePlate3 = new ModelRenderer(this, 58, 15);
      facePlate3.addBox(-4F, -4.5F, -4.5F, 8, 2, 1);
      facePlate3.setRotationPoint(0F, 0F, 0F);
      facePlate3.setTextureSize(256, 128);
      facePlate3.mirror = true;
      setRotation(facePlate3, 0F, 0F, 0F);
      
      facePlate4 = new ModelRenderer(this, 58, 10);
      facePlate4.mirror = true;
      facePlate4.addBox(-2F, -3.5F, -5.5F, 4, 3, 1);
      facePlate4.setRotationPoint(0F, 0.5F, 0F);
      facePlate4.setTextureSize(256, 128);
      facePlate4.mirror = true;
      setRotation(facePlate4, 0.0523599F, 0.3490659F, 0F);
      facePlate4.mirror = false;
      
      facePlate5 = new ModelRenderer(this, 58, 10);
      facePlate5.addBox(-2F, -3.5F, -5.5F, 4, 3, 1);
      facePlate5.setRotationPoint(0F, 0.5F, 0F);
      facePlate5.setTextureSize(256, 128);
      facePlate5.mirror = true;
      setRotation(facePlate5, 0.0523599F, -0.3490659F, 0F);
      
      leftPlate1 = new ModelRenderer(this, 77, 0);
      leftPlate1.addBox(4.5F, -5.2F, -4.6F, 1, 3, 9);
      leftPlate1.setRotationPoint(0F, -2F, 0F);
      leftPlate1.setTextureSize(256, 128);
      leftPlate1.mirror = true;
      setRotation(leftPlate1, 0F, 0F, -0.1919862F);
      
      leftPlate2 = new ModelRenderer(this, 77, 0);
      leftPlate2.addBox(4.5F, -5.2F, -4.6F, 1, 3, 9);
      leftPlate2.setRotationPoint(0F, 0F, 0F);
      leftPlate2.setTextureSize(256, 128);
      leftPlate2.mirror = true;
      setRotation(leftPlate2, 0F, 0F, -0.1919862F);
      
      leftPlate3 = new ModelRenderer(this, 77, 0);
      leftPlate3.addBox(4.5F, -5.2F, -4.6F, 1, 3, 9);
      leftPlate3.setRotationPoint(0F, 2F, 0F);
      leftPlate3.setTextureSize(256, 128);
      leftPlate3.mirror = true;
      setRotation(leftPlate3, 0F, 0F, -0.1919862F);
      
      backPlate2 = new ModelRenderer(this, 98, 0);
      backPlate2.addBox(-4F, -5.5F, 4.5F, 8, 4, 1);
      backPlate2.setRotationPoint(0F, 2F, 0F);
      backPlate2.setTextureSize(256, 128);
      backPlate2.mirror = true;
      setRotation(backPlate2, 0.122173F, 0F, 0F);
      
      leftPlate4 = new ModelRenderer(this, 98, 6);
      leftPlate4.addBox(3.5F, -5.8F, -4.6F, 1, 2, 9);
      leftPlate4.setRotationPoint(0F, 4F, 0F);
      leftPlate4.setTextureSize(256, 128);
      leftPlate4.mirror = true;
      setRotation(leftPlate4, 0F, 0F, 0F);
      
      backPlate1 = new ModelRenderer(this, 98, 0);
      backPlate1.addBox(-4F, -5.5F, 4.5F, 8, 4, 1);
      backPlate1.setRotationPoint(0F, -2F, 0F);
      backPlate1.setTextureSize(256, 128);
      backPlate1.mirror = true;
      setRotation(backPlate1, 0.122173F, 0F, 0F);
      
      rightPlate1 = new ModelRenderer(this, 77, 0);
      rightPlate1.mirror = true;
      rightPlate1.addBox(-5.5F, -5.2F, -4.6F, 1, 3, 9);
      rightPlate1.setRotationPoint(0F, -2F, 0F);
      rightPlate1.setTextureSize(256, 128);
      rightPlate1.mirror = true;
      setRotation(rightPlate1, 0F, 0F, 0.1919862F);
      rightPlate1.mirror = false;
      
      rightPlate2 = new ModelRenderer(this, 77, 0);
      rightPlate2.mirror = true;
      rightPlate2.addBox(-5.5F, -5.2F, -4.6F, 1, 3, 9);
      rightPlate2.setRotationPoint(0F, 0F, 0F);
      rightPlate2.setTextureSize(256, 128);
      rightPlate2.mirror = true;
      setRotation(rightPlate2, 0F, 0F, 0.1919862F);
      rightPlate2.mirror = false;
      
      rightPlate3 = new ModelRenderer(this, 77, 0);
      rightPlate3.mirror = true;
      rightPlate3.addBox(-5.5F, -5.2F, -4.6F, 1, 3, 9);
      rightPlate3.setRotationPoint(0F, 2F, 0F);
      rightPlate3.setTextureSize(256, 128);
      rightPlate3.mirror = true;
      setRotation(rightPlate3, 0F, 0F, 0.1919862F);
      rightPlate3.mirror = false;
      
      rightPlate4 = new ModelRenderer(this, 98, 6);
      rightPlate4.mirror = true;
      rightPlate4.addBox(-4.5F, -5.8F, -4.6F, 1, 2, 9);
      rightPlate4.setRotationPoint(0F, 4F, 0F);
      rightPlate4.setTextureSize(256, 128);
      rightPlate4.mirror = true;
      setRotation(rightPlate4, 0F, 0F, 0F);
      rightPlate4.mirror = false;
      
      this.bipedHead.cubeList.clear();
      this.bipedHeadwear.cubeList.clear();
      if (addHelmet)
      {
          this.bipedHead.addChild(this.facePlate1);
          this.bipedHead.addChild(this.facePlate2);
          this.bipedHead.addChild(this.facePlate3);
          this.bipedHead.addChild(this.facePlate4);
          this.bipedHead.addChild(this.facePlate5);
          this.bipedHead.addChild(this.topHeadPlate);
          this.bipedHead.addChild(this.leftPlate1);
          this.bipedHead.addChild(this.rightPlate1);
          this.bipedHead.addChild(this.leftPlate2);
          this.bipedHead.addChild(this.rightPlate2);
          this.bipedHead.addChild(this.leftPlate3);
          this.bipedHead.addChild(this.rightPlate3);
          this.bipedHead.addChild(this.leftPlate4);
          this.bipedHead.addChild(this.rightPlate4);
          this.bipedHead.addChild(this.backPlate1);
          this.bipedHead.addChild(this.backPlate2);
          
      }

      this.bipedBody.cubeList.clear();
      if (addChestPiece)
      {
          this.bipedBody.addChild(this.chestMain);
          this.bipedBody.addChild(this.chestPlate1);
          this.bipedBody.addChild(this.chestPlate2);
          this.bipedBody.addChild(this.chestPlate3);
          this.bipedBody.addChild(this.chestPlate4);
          this.bipedBody.addChild(this.leftTank);
          this.bipedBody.addChild(this.rightTank);
          this.bipedBody.addChild(this.tankBrace);
      }
      if (addLeggings)
      {
          this.bipedBody.addChild(this.belt);
      }

      this.bipedRightArm.cubeList.clear();
      if (addChestPiece)
      {
          this.bipedRightArm.addChild(this.rightArm);
          this.bipedRightArm.addChild(this.rightShoulder);
          this.bipedRightArm.addChild(this.rightArmPlate1);
          this.bipedRightArm.addChild(this.rightArmPlate2);

      }

      this.bipedLeftArm.cubeList.clear();
      if (addChestPiece)
      {
    	  this.bipedLeftArm.addChild(this.leftArm);
          this.bipedLeftArm.addChild(this.leftShoulder);
          this.bipedLeftArm.addChild(this.leftArmPlate1);
          this.bipedLeftArm.addChild(this.leftArmPlate2);
      }

      this.bipedLeftLeg.cubeList.clear();
      if (addBoots)
      {
          this.bipedLeftLeg.addChild(this.leftFoot);
          this.bipedLeftLeg.addChild(this.leftFootBrace);
          this.bipedLeftLeg.addChild(this.leftFootPlate);
      }
      if (addLeggings)
      {
          this.bipedLeftLeg.addChild(this.leftLeg);
          this.bipedLeftLeg.addChild(this.leftLegBrace);
          this.bipedLeftLeg.addChild(this.leftLegPouch);
      }

      this.bipedRightLeg.cubeList.clear();
      if (addBoots)
      {
          this.bipedRightLeg.addChild(this.rightFoot);
          this.bipedRightLeg.addChild(this.rightFootBrace);
          this.bipedRightLeg.addChild(this.rightFootPlate);
      }
      if (addLeggings)
      {
          this.bipedRightLeg.addChild(this.rightLeg);
      }
  }
  
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
  {
	  setRotationAngles(f, f1, f2, f3, f4, f5, entity);

      this.bipedHead.render(f5);
      this.bipedBody.render(f5);
      this.bipedLeftArm.render(f5);
      this.bipedRightArm.render(f5);
      this.bipedLeftLeg.render(f5);
      this.bipedRightLeg.render(f5);
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
