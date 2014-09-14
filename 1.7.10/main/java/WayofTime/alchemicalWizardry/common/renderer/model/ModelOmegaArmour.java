package WayofTime.alchemicalWizardry.common.renderer.model;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelOmegaArmour extends ModelBiped
{
//	ModelRenderer head;
//    ModelRenderer body;
//    ModelRenderer rightarm;
//    ModelRenderer leftarm;
//    ModelRenderer rightleg;
//    ModelRenderer leftleg;
    ModelRenderer rightArmMain;
    ModelRenderer rightKnucklePlate;
    ModelRenderer rightKnuckleBrace;
    ModelRenderer rightKnuckle1;
    ModelRenderer rightKnuckle2;
    ModelRenderer rightKnuckle3;
    ModelRenderer rightKnuckle4;
    ModelRenderer rightKnuckle5;
    ModelRenderer rightKnuckle6;
    ModelRenderer rightShoulder1;
    ModelRenderer rightShoulder2;
    ModelRenderer rightShoulder3;
    ModelRenderer mainPlate;
    ModelRenderer chestPlate1;
    ModelRenderer chestPlate2;
    ModelRenderer chestPlate3;
    ModelRenderer chestPlate4;
    ModelRenderer chestPlate5;
    ModelRenderer chestPlate6;
    ModelRenderer belt;
    ModelRenderer leftArmMain;
    ModelRenderer leftKnucklePlate;
    ModelRenderer leftKnuckleBrace;
    ModelRenderer leftKnuckle1;
    ModelRenderer leftKnuckle2;
    ModelRenderer leftKnuckle3;
    ModelRenderer leftKnuckle4;
    ModelRenderer leftKnuckle5;
    ModelRenderer leftKnuckle6;
    ModelRenderer leftShoulder1;
    ModelRenderer leftShoulder2;
    ModelRenderer leftShoulder3;
    ModelRenderer leftBootBottom;
    ModelRenderer leftBootPlate;
    ModelRenderer leftBootBrace;
    ModelRenderer leftBootWing1;
    ModelRenderer leftBootWing2;
    ModelRenderer rightBootBottom;
    ModelRenderer rightBootPlate;
    ModelRenderer rightBootBrace;
    ModelRenderer rightBootWing1;
    ModelRenderer rightBootWing2;
    ModelRenderer leftLegSidePlate;
    ModelRenderer leftLegMain;
    ModelRenderer leftLegPlate1;
    ModelRenderer leftLegPlate2;
    ModelRenderer leftLegPlate3;
    ModelRenderer leftLegPlate4;
    ModelRenderer rightLegSidePlate;
    ModelRenderer rightLegMain;
    ModelRenderer rightLegPlate1;
    ModelRenderer rightLegPlate2;
    ModelRenderer rightLegPlate3;
    ModelRenderer rightLegPlate4;
  
  public ModelOmegaArmour(float f, boolean addHelmet, boolean addChestPiece, boolean addLeggings, boolean addBoots)
  {
	  super(f, 0.0f, 128, 128);
	  textureWidth = 128;
	  textureHeight = 128;
	  
//	  boolean addHelmet = true;
//	  boolean addChestPiece = true;
//	  boolean addLeggings = true;
//	  boolean addBoots = true;
    
	  /* Duplicate player model */
	  {
//      head = new ModelRenderer(this, 0, 0);
//      head.addBox(-4F, -8F, -4F, 8, 8, 8);
//      head.setRotationPoint(0F, 0F, 0F);
//      head.setTextureSize(128, 128);
//      head.mirror = true;
//      setRotation(head, 0F, 0F, 0F);
//      body = new ModelRenderer(this, 16, 16);
//      body.addBox(-4F, 0F, -2F, 8, 12, 4);
//      body.setRotationPoint(0F, 0F, 0F);
//      body.setTextureSize(128, 128);
//      body.mirror = true;
//      setRotation(body, 0F, 0F, 0F);
//      rightarm = new ModelRenderer(this, 40, 16);
//      rightarm.addBox(-3F, -2F, -2F, 4, 12, 4);
//      rightarm.setRotationPoint(0F, 0F, 0F);
//      rightarm.setTextureSize(128, 128);
//      rightarm.mirror = true;
//      setRotation(rightarm, 0F, 0F, 0F);
//      leftarm = new ModelRenderer(this, 40, 16);
//      leftarm.addBox(-1F, -2F, -2F, 4, 12, 4);
//      leftarm.setRotationPoint(0F, 0F, 0F);
//      leftarm.setTextureSize(128, 128);
//      leftarm.mirror = true;
//      setRotation(leftarm, 0F, 0F, 0F);
//      rightleg = new ModelRenderer(this, 0, 16);
//      rightleg.addBox(-2F, 0F, -2F, 4, 12, 4);
//      rightleg.setRotationPoint(-2F, 12F, 0F);
//      rightleg.setTextureSize(128, 128);
//      rightleg.mirror = true;
//      setRotation(rightleg, 0F, 0F, 0F);
//      leftleg = new ModelRenderer(this, 0, 16);
//      leftleg.addBox(-2F, 0F, -2F, 4, 12, 4);
//      leftleg.setRotationPoint(2F, 12F, 0F);
//      leftleg.setTextureSize(128, 128);
//      leftleg.mirror = true;
//      setRotation(leftleg, 0F, 0F, 0F);
	  }
	  
	  /* Right arm */
	  {
	      rightArmMain = new ModelRenderer(this, 0, 33);
	      rightArmMain.mirror = true;
	      rightArmMain.addBox(-3.5F, -2.5F, -2.5F, 5, 11, 5);
	      rightArmMain.setRotationPoint(0F, 0F, 0F);
	      rightArmMain.setTextureSize(128, 128);
	      rightArmMain.mirror = true;
	      setRotation(rightArmMain, 0F, 0F, 0F);
	      
	      rightKnucklePlate = new ModelRenderer(this, 0, 50);
	      rightKnucklePlate.addBox(-4F, 4F, -1.5F, 1, 5, 3);
	      rightKnucklePlate.setRotationPoint(0F, 0F, 0F);
	      rightKnucklePlate.setTextureSize(128, 128);
	      rightKnucklePlate.mirror = true;
	      setRotation(rightKnucklePlate, 0F, 0F, 0F);

	      rightKnuckleBrace = new ModelRenderer(this, 9, 50);
	      rightKnuckleBrace.mirror = true;
	      rightKnuckleBrace.addBox(-4F, 3F, -3F, 2, 1, 6);
	      rightKnuckleBrace.setRotationPoint(0F, 0F, 0F);
	      rightKnuckleBrace.setTextureSize(128, 128);
	      setRotation(rightKnuckleBrace, 0F, 0F, 0F);

	      rightKnuckle1 = new ModelRenderer(this, 0, 59);
	      rightKnuckle1.mirror = true;
	      rightKnuckle1.addBox(-4F, 7F, -2.5F, 1, 6, 1);
	      rightKnuckle1.setRotationPoint(0F, 0F, 0F);
	      rightKnuckle1.setTextureSize(128, 128);
	      setRotation(rightKnuckle1, 0F, 0F, 0F);

	      rightKnuckle2 = new ModelRenderer(this, 5, 59);
	      rightKnuckle2.mirror = true;
	      rightKnuckle2.addBox(-3F, 11F, -2.5F, 1, 3, 1);
	      rightKnuckle2.setRotationPoint(0F, 0F, 0F);
	      rightKnuckle2.setTextureSize(128, 128);
	      setRotation(rightKnuckle2, 0F, 0F, 0F);

	      rightKnuckle3 = new ModelRenderer(this, 0, 59);
	      rightKnuckle3.mirror = true;
	      rightKnuckle3.addBox(-4.5F, 8F, -0.5F, 1, 6, 1);
	      rightKnuckle3.setRotationPoint(0F, 0F, 0F);
	      rightKnuckle3.setTextureSize(128, 128);
	      setRotation(rightKnuckle3, 0F, 0F, 0F);
	      
	      rightKnuckle4 = new ModelRenderer(this, 5, 59);
	      rightKnuckle4.mirror = true;
	      rightKnuckle4.addBox(-3.5F, 12F, -0.5F, 1, 3, 1);
	      rightKnuckle4.setRotationPoint(0F, 0F, 0F);
	      rightKnuckle4.setTextureSize(128, 128);
	      setRotation(rightKnuckle4, 0F, 0F, 0F);

	      rightKnuckle5 = new ModelRenderer(this, 0, 59);
	      rightKnuckle5.mirror = true;
	      rightKnuckle5.addBox(-4F, 7F, 1.5F, 1, 6, 1);
	      rightKnuckle5.setRotationPoint(0F, 0F, 0F);
	      rightKnuckle5.setTextureSize(128, 128);
	      setRotation(rightKnuckle5, 0F, 0F, 0F);
	      
	      rightKnuckle6 = new ModelRenderer(this, 5, 59);
	      rightKnuckle6.mirror = true;
	      rightKnuckle6.addBox(-3F, 11F, 1.5F, 1, 3, 1);
	      rightKnuckle6.setRotationPoint(0F, 0F, 0F);
	      rightKnuckle6.setTextureSize(128, 128);
	      setRotation(rightKnuckle6, 0F, 0F, 0F);
	      
	      rightShoulder1 = new ModelRenderer(this, 10, 59);
	      rightShoulder1.mirror = true;
	      rightShoulder1.addBox(-5F, -3F, -4F, 1, 4, 8);
	      rightShoulder1.setRotationPoint(0F, 0F, 0F);
	      rightShoulder1.setTextureSize(128, 128);
	      setRotation(rightShoulder1, 0F, 0F, 0.6981317F);

	      rightShoulder2 = new ModelRenderer(this, 10, 59);
	      rightShoulder2.mirror = true;
	      rightShoulder2.addBox(-4F, -1.5F, -4F, 1, 4, 8);
	      rightShoulder2.setRotationPoint(0F, 0F, 0F);
	      rightShoulder2.setTextureSize(128, 128);
	      setRotation(rightShoulder2, 0F, 0F, 0.6981317F);

	      rightShoulder3 = new ModelRenderer(this, 10, 59);
	      rightShoulder3.mirror = true;
	      rightShoulder3.addBox(-3F, 0F, -4F, 1, 4, 8);
	      rightShoulder3.setRotationPoint(0F, 0F, 0F);
	      rightShoulder3.setTextureSize(128, 128);
	      setRotation(rightShoulder3, 0F, 0F, 0.6981317F);
	  }
	  
	  /* Chest piece main body */
	  {
	      mainPlate = new ModelRenderer(this, 31, 33);
	      mainPlate.addBox(-4.5F, -0.5F, -3F, 9, 12, 6);
	      mainPlate.setRotationPoint(0F, 0F, 0F);
	      mainPlate.setTextureSize(128, 128);
	      mainPlate.mirror = true;
	      setRotation(mainPlate, 0F, 0F, 0F);
	      
	      chestPlate1 = new ModelRenderer(this, 63, 33);
	      chestPlate1.addBox(-1.5F, 3F, -4.5F, 6, 2, 1);
	      chestPlate1.setRotationPoint(0F, -3F, -1F);
	      chestPlate1.setTextureSize(128, 128);
	      setRotation(chestPlate1, 0.3490659F, 0F, -0.2617994F);
	      
	      chestPlate2 = new ModelRenderer(this, 63, 33);
	      chestPlate2.addBox(-1.5F, 3F, -4.5F, 6, 2, 1);
	      chestPlate2.setRotationPoint(0F, -1.5F, -1F);
	      chestPlate2.setTextureSize(128, 128);
	      setRotation(chestPlate2, 0.3490659F, 0F, -0.2617994F);
	      
	      chestPlate3 = new ModelRenderer(this, 63, 33);
	      chestPlate3.addBox(-1.5F, 3F, -4.5F, 6, 2, 1);
	      chestPlate3.setRotationPoint(0F, 0F, -1F);
	      chestPlate3.setTextureSize(128, 128);
	      setRotation(chestPlate3, 0.3490659F, 0F, -0.2617994F);
	      
	      chestPlate4 = new ModelRenderer(this, 63, 33);
	      chestPlate4.mirror = true;
	      chestPlate4.addBox(-4.5F, 3F, -4.5F, 6, 2, 1);
	      chestPlate4.setRotationPoint(0F, -3F, -1F);
	      chestPlate4.setTextureSize(128, 128);
	      setRotation(chestPlate4, 0.3490659F, 0F, 0.2617994F);

	      chestPlate5 = new ModelRenderer(this, 63, 33);
	      chestPlate5.mirror = true;
	      chestPlate5.addBox(-4.5F, 3F, -4.5F, 6, 2, 1);
	      chestPlate5.setRotationPoint(0F, -1.5F, -1F);
	      chestPlate5.setTextureSize(128, 128);
	      setRotation(chestPlate5, 0.3490659F, 0F, 0.2617994F);

	      chestPlate6 = new ModelRenderer(this, 63, 33);
	      chestPlate6.mirror = true;
	      chestPlate6.addBox(-4.5F, 3F, -4.5F, 6, 2, 1);
	      chestPlate6.setRotationPoint(0F, 0F, -1F);
	      chestPlate6.setTextureSize(128, 128);
	      setRotation(chestPlate6, 0.3490659F, 0F, 0.2617994F);
	  }
	  
	  /* Left arm */
	  {
	      leftArmMain = new ModelRenderer(this, 0, 33);
	      leftArmMain.addBox(-1.5F, -2.533333F, -2.5F, 5, 11, 5);
	      leftArmMain.setRotationPoint(0F, 0F, 0F);
	      leftArmMain.setTextureSize(128, 128);
	      leftArmMain.mirror = true;
	      setRotation(leftArmMain, 0F, 0F, 0F);
	      
	      leftKnucklePlate = new ModelRenderer(this, 0, 50);
	      leftKnucklePlate.addBox(3F, 4F, -1.5F, 1, 5, 3);
	      leftKnucklePlate.setRotationPoint(0F, 0F, 0F);
	      leftKnucklePlate.setTextureSize(128, 128);
	      leftKnucklePlate.mirror = true;
	      setRotation(leftKnucklePlate, 0F, 0F, 0F);
	      
	      leftKnuckleBrace = new ModelRenderer(this, 9, 50);
	      leftKnuckleBrace.addBox(2F, 3F, -3F, 2, 1, 6);
	      leftKnuckleBrace.setRotationPoint(0F, 0F, 0F);
	      leftKnuckleBrace.setTextureSize(128, 128);
	      leftKnuckleBrace.mirror = true;
	      setRotation(leftKnuckleBrace, 0F, 0F, 0F);
	      
	      leftKnuckle1 = new ModelRenderer(this, 0, 59);
	      leftKnuckle1.addBox(3F, 7F, -2.5F, 1, 6, 1);
	      leftKnuckle1.setRotationPoint(0F, 0F, 0F);
	      leftKnuckle1.setTextureSize(128, 128);
	      leftKnuckle1.mirror = true;
	      setRotation(leftKnuckle1, 0F, 0F, 0F);
	      
	      leftKnuckle2 = new ModelRenderer(this, 5, 59);
	      leftKnuckle2.addBox(2F, 11F, -2.5F, 1, 3, 1);
	      leftKnuckle2.setRotationPoint(0F, 0F, 0F);
	      leftKnuckle2.setTextureSize(128, 128);
	      leftKnuckle2.mirror = true;
	      setRotation(leftKnuckle2, 0F, 0F, 0F);
	      
	      leftKnuckle3 = new ModelRenderer(this, 0, 59);
	      leftKnuckle3.addBox(3.5F, 8F, -0.5F, 1, 6, 1);
	      leftKnuckle3.setRotationPoint(0F, 0F, 0F);
	      leftKnuckle3.setTextureSize(128, 128);
	      leftKnuckle3.mirror = true;
	      setRotation(leftKnuckle3, 0F, 0F, 0F);
	      
	      leftKnuckle4 = new ModelRenderer(this, 5, 59);
	      leftKnuckle4.addBox(2.5F, 12F, -0.5F, 1, 3, 1);
	      leftKnuckle4.setRotationPoint(0F, 0F, 0F);
	      leftKnuckle4.setTextureSize(128, 128);
	      leftKnuckle4.mirror = true;
	      setRotation(leftKnuckle4, 0F, 0F, 0F);
	      
	      leftKnuckle5 = new ModelRenderer(this, 0, 59);
	      leftKnuckle5.addBox(3F, 7F, 1.5F, 1, 6, 1);
	      leftKnuckle5.setRotationPoint(0F, 0F, 0F);
	      leftKnuckle5.setTextureSize(128, 128);
	      leftKnuckle5.mirror = true;
	      setRotation(leftKnuckle5, 0F, 0F, 0F);
	      
	      leftKnuckle6 = new ModelRenderer(this, 5, 59);
	      leftKnuckle6.addBox(2F, 11F, 1.5F, 1, 3, 1);
	      leftKnuckle6.setRotationPoint(0F, 0F, 0F);
	      leftKnuckle6.setTextureSize(128, 128);
	      leftKnuckle6.mirror = true;
	      setRotation(leftKnuckle6, 0F, 0F, 0F);
	      
	      leftShoulder1 = new ModelRenderer(this, 10, 59);
	      leftShoulder1.addBox(4F, -3F, -4F, 1, 4, 8);
	      leftShoulder1.setRotationPoint(0F, 0F, 0F);
	      leftShoulder1.setTextureSize(128, 128);
	      leftShoulder1.mirror = true;
	      setRotation(leftShoulder1, 0F, 0F, -0.6981317F);
	      
	      leftShoulder2 = new ModelRenderer(this, 10, 59);
	      leftShoulder2.addBox(3F, -1.5F, -4F, 1, 4, 8);
	      leftShoulder2.setRotationPoint(0F, 0F, 0F);
	      leftShoulder2.setTextureSize(128, 128);
	      leftShoulder2.mirror = true;
	      setRotation(leftShoulder2, 0F, 0F, -0.6981317F);
	      
	      leftShoulder3 = new ModelRenderer(this, 10, 59);
	      leftShoulder3.addBox(2F, 0F, -4F, 1, 4, 8);
	      leftShoulder3.setRotationPoint(0F, 0F, 0F);
	      leftShoulder3.setTextureSize(128, 128);
	      leftShoulder3.mirror = true;
	      setRotation(leftShoulder3, 0F, 0F, -0.6981317F);
      }
      
      /* Left boot */
      {
	      leftBootBottom = new ModelRenderer(this, 0, 84);
	      leftBootBottom.addBox(-2.5F, 9.5F, -5.5F, 6, 3, 8);
	      leftBootBottom.setRotationPoint(0F, 0F, 0F);
	      leftBootBottom.setTextureSize(128, 128);
	      leftBootBottom.mirror = true;
	      setRotation(leftBootBottom, 0F, 0F, 0F);
	      
	      leftBootPlate = new ModelRenderer(this, 0, 96);
	      leftBootPlate.addBox(-2F, 6F, 6F, 5, 3, 1);
	      leftBootPlate.setRotationPoint(0F, 0F, 0F);
	      leftBootPlate.setTextureSize(128, 128);
	      leftBootPlate.mirror = true;
	      setRotation(leftBootPlate, -1.151917F, 0F, 0F);
	      
	      leftBootBrace = new ModelRenderer(this, 0, 72);
	      leftBootBrace.addBox(-2F, 7F, -3F, 5, 3, 6);
	      leftBootBrace.setRotationPoint(0F, 0F, 0F);
	      leftBootBrace.setTextureSize(128, 128);
	      leftBootBrace.mirror = true;
	      setRotation(leftBootBrace, 0F, 0F, 0F);
	      
	      leftBootWing1 = new ModelRenderer(this, 13, 96);
	      leftBootWing1.addBox(3F, 7F, -4F, 1, 1, 7);
	      leftBootWing1.setRotationPoint(0F, 0F, 0F);
	      leftBootWing1.setTextureSize(128, 128);
	      leftBootWing1.mirror = true;
	      setRotation(leftBootWing1, 0.2617994F, 0.1745329F, 0F);
	      
	      leftBootWing2 = new ModelRenderer(this, 13, 96);
	      leftBootWing2.addBox(3F, 8F, -5F, 1, 1, 7);
	      leftBootWing2.setRotationPoint(0F, 0F, 0F);
	      leftBootWing2.setTextureSize(128, 128);
	      leftBootWing2.mirror = true;
	      setRotation(leftBootWing2, 0.2617994F, 0.1745329F, 0F);
  	  }
      
      
      /* Right boot */
      {
		  rightBootBottom = new ModelRenderer(this, 0, 84);
		  rightBootBottom.mirror = true;
		  rightBootBottom.addBox(-3.5F, 9.5F, -5.5F, 6, 3, 8);
		  rightBootBottom.setRotationPoint(0F, 0F, 0F);
		  rightBootBottom.setTextureSize(128, 128);
		  setRotation(rightBootBottom, 0F, 0F, 0F);

		  rightBootPlate = new ModelRenderer(this, 0, 96);
		  rightBootPlate.mirror = true;
		  rightBootPlate.addBox(-3F, 6F, 6F, 5, 3, 1);
		  rightBootPlate.setRotationPoint(0F, 0F, 0F);
		  rightBootPlate.setTextureSize(128, 128);
		  setRotation(rightBootPlate, -1.151917F, 0F, 0F);

		  rightBootBrace = new ModelRenderer(this, 0, 72);
		  rightBootBrace.mirror = true;
		  rightBootBrace.addBox(-3F, 7F, -3F, 5, 3, 6);
		  rightBootBrace.setRotationPoint(0F, 0F, 0F);
		  rightBootBrace.setTextureSize(128, 128);
		  setRotation(rightBootBrace, 0F, 0F, 0F);
		  
		  rightBootWing1 = new ModelRenderer(this, 13, 96);
		  rightBootWing1.mirror = true;
		  rightBootWing1.addBox(-4F, 7F, -4F, 1, 1, 7);
		  rightBootWing1.setRotationPoint(0F, 0F, 0F);
		  rightBootWing1.setTextureSize(128, 128);
		  setRotation(rightBootWing1, 0.2617994F, -0.1745329F, 0F);
		  
		  rightBootWing2 = new ModelRenderer(this, 13, 96);
		  rightBootWing2.mirror = true;
		  rightBootWing2.addBox(-4F, 8F, -5F, 1, 1, 7);
		  rightBootWing2.setRotationPoint(0F, 0F, 0F);
		  rightBootWing2.setTextureSize(128, 128);
		  setRotation(rightBootWing2, 0.2617994F, -0.1745329F, 0F);
      }
      
      /* Main legs */
      {
	      belt = new ModelRenderer(this, 31, 52);
	      belt.addBox(-5F, 9.5F, -3.5F, 10, 2, 7);
	      belt.setRotationPoint(0F, 0F, 0F);
	      belt.setTextureSize(128, 128);
	      belt.mirror = true;
	      setRotation(belt, 0F, 0F, 0F);
      }
      
      /* Left leg */
      {
	      leftLegSidePlate = new ModelRenderer(this, 31, 71);
	      leftLegSidePlate.addBox(-0.5F, 12F, -3F, 1, 6, 6);
	      leftLegSidePlate.setRotationPoint(-2F, -12F, 0F);
	      leftLegSidePlate.setTextureSize(128, 128);
	      leftLegSidePlate.mirror = true;
	      setRotation(leftLegSidePlate, 0F, 0F, -0.3490659F);
	      
	      leftLegMain = new ModelRenderer(this, 53, 62);
	      leftLegMain.addBox(-0.5F, 11F, -2.5F, 5, 9, 5);
	      leftLegMain.setRotationPoint(-2F, -12F, 0F);
	      leftLegMain.setTextureSize(128, 128);
	      leftLegMain.mirror = true;
	      setRotation(leftLegMain, 0F, 0F, 0F);
	      
	      leftLegPlate1 = new ModelRenderer(this, 46, 71);
	      leftLegPlate1.addBox(-2.5F, 11F, -3F, 2, 6, 1);
	      leftLegPlate1.setRotationPoint(-2F, -12F, 0F);
	      leftLegPlate1.setTextureSize(128, 128);
	      leftLegPlate1.mirror = true;
	      setRotation(leftLegPlate1, 0F, 0F, -0.3490659F);
	      
	      leftLegPlate2 = new ModelRenderer(this, 46, 71);
	      leftLegPlate2.addBox(-2.5F, 11F, 2F, 2, 6, 1);
	      leftLegPlate2.setRotationPoint(-2F, -12F, 0F);
	      leftLegPlate2.setTextureSize(128, 128);
	      leftLegPlate2.mirror = true;
	      setRotation(leftLegPlate2, 0F, 0F, -0.3490659F);
	      
	      leftLegPlate3 = new ModelRenderer(this, 31, 62);
	      leftLegPlate3.addBox(0F, 11.9F, -1F, 4, 7, 1);
	      leftLegPlate3.setRotationPoint(-2F, -12F, 0F);
	      leftLegPlate3.setTextureSize(128, 128);
	      leftLegPlate3.mirror = true;
	      setRotation(leftLegPlate3, -0.1745329F, 0F, 0F);
	      
	      leftLegPlate4 = new ModelRenderer(this, 42, 62);
	      leftLegPlate4.addBox(0F, 11.9F, 0F, 4, 7, 1);
	      leftLegPlate4.setRotationPoint(-2F, -12F, 0F);
	      leftLegPlate4.setTextureSize(128, 128);
	      leftLegPlate4.mirror = true;
	      setRotation(leftLegPlate4, 0.1745329F, 0F, 0F);
      }
      
      /* Right leg */
      {
	      rightLegSidePlate = new ModelRenderer(this, 31, 71);
	      rightLegSidePlate.mirror = true;
	      rightLegSidePlate.addBox(-0.5F, 12F, -3F, 1, 6, 6);
	      rightLegSidePlate.setRotationPoint(2F, -12F, 0F);
	      rightLegSidePlate.setTextureSize(128, 128);
	      setRotation(rightLegSidePlate, 0F, 0F, 0.3490659F);

	      rightLegMain = new ModelRenderer(this, 40, 93);
	      rightLegMain.mirror = true;
	      rightLegMain.addBox(-4.5F, 11F, -2.5F, 5, 9, 5);
	      rightLegMain.setRotationPoint(2F, -12F, 0F);
	      rightLegMain.setTextureSize(128, 128);
	      setRotation(rightLegMain, 0F, 0F, 0F);

	      rightLegPlate1 = new ModelRenderer(this, 46, 71);
	      rightLegPlate1.mirror = true;
	      rightLegPlate1.addBox(0.5F, 11F, -3F, 2, 6, 1);
	      rightLegPlate1.setRotationPoint(2F, -12F, 0F);
	      rightLegPlate1.setTextureSize(128, 128);
	      setRotation(rightLegPlate1, 0F, 0F, 0.3490659F);

	      rightLegPlate2 = new ModelRenderer(this, 46, 71);
	      rightLegPlate2.mirror = true;
	      rightLegPlate2.addBox(0.5F, 11F, 2F, 2, 6, 1);
	      rightLegPlate2.setRotationPoint(2F, -12F, 0F);
	      rightLegPlate2.setTextureSize(128, 128);
	      setRotation(rightLegPlate2, 0F, 0F, 0.3490659F);

	      rightLegPlate3 = new ModelRenderer(this, 31, 62);
	      rightLegPlate3.mirror = true;
	      rightLegPlate3.addBox(-4F, 11.9F, -1F, 4, 7, 1);
	      rightLegPlate3.setRotationPoint(2F, -12F, 0F);
	      rightLegPlate3.setTextureSize(128, 128);
	      setRotation(rightLegPlate3, -0.1745329F, 0F, 0F);

	      rightLegPlate4 = new ModelRenderer(this, 42, 62);
	      rightLegPlate4.mirror = true;
	      rightLegPlate4.addBox(-4F, 11.9F, 0F, 4, 7, 1);
	      rightLegPlate4.setRotationPoint(2F, -12F, 0F);
	      rightLegPlate4.setTextureSize(128, 128);
	      setRotation(rightLegPlate4, 0.1745329F, 0F, 0F);
      }
      
      this.bipedHead.cubeList.clear();
      this.bipedHeadwear.cubeList.clear();
      if(addHelmet)
      {
    	  
      }
      
      this.bipedBody.cubeList.clear();
      if(addChestPiece)
      {
    	  this.bipedBody.addChild(this.chestPlate1);
    	  this.bipedBody.addChild(this.chestPlate2);
    	  this.bipedBody.addChild(this.chestPlate3);
    	  this.bipedBody.addChild(this.chestPlate4);
    	  this.bipedBody.addChild(this.chestPlate5);
    	  this.bipedBody.addChild(this.chestPlate6);
    	  this.bipedBody.addChild(this.mainPlate);
      }
      if(addLeggings)
      {
    	  this.bipedBody.addChild(this.belt);
      }
      
      this.bipedRightArm.cubeList.clear();
      if(addChestPiece)
      {
    	  this.bipedRightArm.addChild(rightArmMain);
    	  this.bipedRightArm.addChild(this.rightKnucklePlate);
    	  this.bipedRightArm.addChild(this.rightKnuckleBrace);
    	  this.bipedRightArm.addChild(this.rightKnuckle1);
    	  this.bipedRightArm.addChild(this.rightKnuckle2);
    	  this.bipedRightArm.addChild(this.rightKnuckle3);
    	  this.bipedRightArm.addChild(this.rightKnuckle4);
    	  this.bipedRightArm.addChild(this.rightKnuckle5);
    	  this.bipedRightArm.addChild(this.rightKnuckle6);
    	  this.bipedRightArm.addChild(this.rightArmMain);
    	  this.bipedRightArm.addChild(this.rightShoulder1);
    	  this.bipedRightArm.addChild(this.rightShoulder2);
    	  this.bipedRightArm.addChild(this.rightShoulder3);
      }
      
      this.bipedLeftArm.cubeList.clear();
      if(addChestPiece)
      {
    	  this.bipedLeftArm.addChild(leftArmMain);
    	  this.bipedLeftArm.addChild(this.leftKnucklePlate);
    	  this.bipedLeftArm.addChild(this.leftKnuckleBrace);
    	  this.bipedLeftArm.addChild(this.leftKnuckle1);
    	  this.bipedLeftArm.addChild(this.leftKnuckle2);
    	  this.bipedLeftArm.addChild(this.leftKnuckle3);
    	  this.bipedLeftArm.addChild(this.leftKnuckle4);
    	  this.bipedLeftArm.addChild(this.leftKnuckle5);
    	  this.bipedLeftArm.addChild(this.leftKnuckle6);
    	  this.bipedLeftArm.addChild(this.leftArmMain);
    	  this.bipedLeftArm.addChild(this.leftShoulder1);
    	  this.bipedLeftArm.addChild(this.leftShoulder2);
    	  this.bipedLeftArm.addChild(this.leftShoulder3);
      }
      
      this.bipedLeftLeg.cubeList.clear();
      if(addBoots)
      {
    	  this.bipedLeftLeg.addChild(this.leftBootBottom);
    	  this.bipedLeftLeg.addChild(this.leftBootBrace);
    	  this.bipedLeftLeg.addChild(this.leftBootPlate);
    	  this.bipedLeftLeg.addChild(this.leftBootWing1);
    	  this.bipedLeftLeg.addChild(this.leftBootWing2);
      }
      if(addLeggings)
      {
    	  this.bipedLeftLeg.addChild(this.leftLegMain);
    	  this.bipedLeftLeg.addChild(this.leftLegSidePlate);
    	  this.bipedLeftLeg.addChild(this.leftLegPlate1);
    	  this.bipedLeftLeg.addChild(this.leftLegPlate2);
    	  this.bipedLeftLeg.addChild(this.leftLegPlate3);
    	  this.bipedLeftLeg.addChild(this.leftLegPlate4);
      }
      
      this.bipedRightLeg.cubeList.clear();
      if(addBoots)
      {
    	  this.bipedRightLeg.addChild(this.rightBootBottom);
    	  this.bipedRightLeg.addChild(this.rightBootBrace);
    	  this.bipedRightLeg.addChild(this.rightBootPlate);
    	  this.bipedRightLeg.addChild(this.rightBootWing1);
    	  this.bipedRightLeg.addChild(this.rightBootWing2);
      }
      if(addLeggings)
      {
    	  this.bipedRightLeg.addChild(this.rightLegMain);
    	  this.bipedRightLeg.addChild(this.rightLegSidePlate);
    	  this.bipedRightLeg.addChild(this.rightLegPlate1);
    	  this.bipedRightLeg.addChild(this.rightLegPlate2);
    	  this.bipedRightLeg.addChild(this.rightLegPlate3);
    	  this.bipedRightLeg.addChild(this.rightLegPlate4);
      }
  }
  
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
  {
//    super.render(entity, f, f1, f2, f3, f4, f5);
	  
	  
	  
	  setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    
	  this.bipedHead.render(f5);
	  this.bipedBody.render(f5);
	  this.bipedLeftArm.render(f5);
	  this.bipedRightArm.render(f5);
	  this.bipedLeftLeg.render(f5);
	  this.bipedRightLeg.render(f5);
    
//    head.render(f5);
//    body.render(f5);
//    rightarm.render(f5);
//    leftarm.render(f5);
//    rightleg.render(f5);
//    leftleg.render(f5);
//    rightArmMain.render(f5);
//    rightKnucklePlate.render(f5);
//    rightKnuckleBrace.render(f5);
//    rightKnuckle1.render(f5);
//    rightKnuckle2.render(f5);
//    rightKnuckle3.render(f5);
//    rightKnuckle4.render(f5);
//    rightKnuckle5.render(f5);
//    rightKnuckle6.render(f5);
//    rightShoulder1.render(f5);
//    rightShoulder2.render(f5);
//    rightShoulder3.render(f5);
//    mainPlate.render(f5);
//    chestPlate1.render(f5);
//    chestPlate2.render(f5);
//    chestPlate3.render(f5);
//    chestPlate4.render(f5);
//    chestPlate5.render(f5);
//    chestPlate6.render(f5);
//    belt.render(f5);
//    leftArmMain.render(f5);
//    leftKnucklePlate.render(f5);
//    leftKnuckleBrace.render(f5);
//    leftKnuckle1.render(f5);
//    leftKnuckle2.render(f5);
//    leftKnuckle3.render(f5);
//    leftKnuckle4.render(f5);
//    leftKnuckle5.render(f5);
//    leftKnuckle6.render(f5);
//    leftShoulder1.render(f5);
//    leftShoulder2.render(f5);
//    leftShoulder3.render(f5);
//    leftBootBottom.render(f5);
//    leftBootPlate.render(f5);
//    leftBootBrace.render(f5);
//    leftBootWing1.render(f5);
//    leftBootWing2.render(f5);
//    rightBootBottom.render(f5);
//    rightBootPlate.render(f5);
//    rightBootBrace.render(f5);
//    rightBootWing1.render(f5);
//    rightBootWing2.render(f5);
//    
//    {
//	    leftLegSidePlate.render(f5);
//	    leftLegMain.render(f5);
//	    leftLegPlate1.render(f5);
//	    leftLegPlate2.render(f5);
//	    leftLegPlate3.render(f5);
//	    leftLegPlate4.render(f5);
//  	}
//    
//    {
//    	rightLegSidePlate.render(f5);
//        rightLegMain.render(f5);
//	    rightLegPlate1.render(f5);
//	    rightLegPlate2.render(f5);
//	    rightLegPlate3.render(f5);
//	    rightLegPlate4.render(f5);
//    }
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
