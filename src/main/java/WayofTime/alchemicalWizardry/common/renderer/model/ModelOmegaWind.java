package WayofTime.alchemicalWizardry.common.renderer.model;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * OmegaWind.tcn - TechneToTabulaImporter
 * Created using Tabula 4.1.0
 */
public class ModelOmegaWind extends ModelBiped
{
    public ModelRenderer facePlate1;
    public ModelRenderer facePlate2;
    public ModelRenderer facePlate3;
    public ModelRenderer facePlate4;
    public ModelRenderer forePlate;
    public ModelRenderer topWingPlate;
    public ModelRenderer leftWingPlate1;
    public ModelRenderer leftWingPlate3;
    public ModelRenderer rightWingPlate1;
    public ModelRenderer rightWingPlate2;
    public ModelRenderer rightWingPlate3;
    public ModelRenderer topPlate;
    public ModelRenderer backPlate;
    public ModelRenderer leftWingPlate2;
    public ModelRenderer chestMain;
    public ModelRenderer chestPlate1;
    public ModelRenderer chestPlate3;
    public ModelRenderer chestPlate4;
    public ModelRenderer chestPlate5;
    public ModelRenderer chestPlate6;
    public ModelRenderer chestOrnament;
    public ModelRenderer belt;
    public ModelRenderer chestPlate2;
    public ModelRenderer rightArm;
    public ModelRenderer rightShoulder;
    public ModelRenderer rightArmPlate1;
    public ModelRenderer rightArmPlate2;
    public ModelRenderer rightArmPlate3;
    public ModelRenderer rightFloater1;
    public ModelRenderer rightFloater2;
    public ModelRenderer rightFloater3;
    public ModelRenderer rightFloater4;
    public ModelRenderer leftArm;
    public ModelRenderer leftShoulder;
    public ModelRenderer leftArmPlate1;
    public ModelRenderer leftArmPlate2;
    public ModelRenderer leftArmPlate3;
    public ModelRenderer leftFloater1;
    public ModelRenderer leftFloater2;
    public ModelRenderer leftFloater3;
    public ModelRenderer leftFloater4;
    public ModelRenderer rightLeg;
    public ModelRenderer rightLegFloater1;
    public ModelRenderer rightLegFloater2;
    public ModelRenderer rightLegFloater3;
    public ModelRenderer rightFoot2;
    public ModelRenderer rightFootPlate;
    public ModelRenderer rightFoot1;
    public ModelRenderer leftLeg;
    public ModelRenderer leftLegFloater1;
    public ModelRenderer leftLegFloater2;
    public ModelRenderer leftLegFloater3;
    public ModelRenderer leftFoot1;
    public ModelRenderer leftFoot2;
    public ModelRenderer leftFootPlate;

    public ModelOmegaWind(float f, boolean addHelmet, boolean addChestPiece, boolean addLeggings, boolean addBoots)
    {
        super(f, 0.0f, 128, 128);
        this.rightWingPlate2 = new ModelRenderer(this, 33, 82);
        this.rightWingPlate2.mirror = true;
        this.rightWingPlate2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.rightWingPlate2.addBox(-5.0F, -4.900000095367432F, -2.5F, 1, 2, 3, 0.0F);
        this.setRotateAngle(rightWingPlate2, 0.13962633907794952F, -0.0F, 0.0F);
        this.leftFloater1 = new ModelRenderer(this, 0, 78);
        this.leftFloater1.setRotationPoint(1.0F, 7.0F, 0.0F);
        this.leftFloater1.addBox(3.5F, -2.0F, -1.5F, 1, 3, 3, 0.0F);
        this.setRotateAngle(leftFloater1, -0.148352986419518F, -0.5040510879759623F, 0.30002209841782523F);

        this.leftWingPlate3 = new ModelRenderer(this, 38, 92);
        this.leftWingPlate3.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.leftWingPlate3.addBox(3.5F, -5.0F, -3.9000000953674316F, 1, 5, 9, 0.0F);
        this.setRotateAngle(leftWingPlate3, 0.0F, 0.12217304855585097F, 0.0F);
        this.leftFoot1 = new ModelRenderer(this, 0, 110);
        this.leftFoot1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.leftFoot1.addBox(-2.0F, 9.5F, -5.0F, 5, 3, 8, 0.0F);
        this.rightArmPlate1 = new ModelRenderer(this, 21, 51);
        this.rightArmPlate1.mirror = true;
        this.rightArmPlate1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.rightArmPlate1.addBox(-5.5F, -2.0F, -4.0F, 1, 4, 8, 0.0F);
        this.setRotateAngle(rightArmPlate1, 0.0F, -0.0F, 0.5585053606381855F);
        this.topPlate = new ModelRenderer(this, 59, 92);
        this.topPlate.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.topPlate.addBox(-4.5F, -8.100000381469727F, -4.0F, 9, 1, 8, 0.0F);

        this.leftShoulder = new ModelRenderer(this, 0, 66);
        this.leftShoulder.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.leftShoulder.addBox(-1.0F, -3.0F, -3.0F, 5, 5, 6, 0.0F);
        this.leftArmPlate3 = new ModelRenderer(this, 21, 51);
        this.leftArmPlate3.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.leftArmPlate3.addBox(2.5F, 1.0F, -4.0F, 1, 4, 8, 0.0F);
        this.setRotateAngle(leftArmPlate3, 0.0F, -0.0F, -0.5585053606381855F);
        this.leftFloater4 = new ModelRenderer(this, 0, 78);
        this.leftFloater4.setRotationPoint(1.0F, 7.0F, 0.0F);
        this.leftFloater4.addBox(3.5F, -2.0F, -1.5F, 1, 3, 3, 0.0F);
        this.setRotateAngle(leftFloater4, -1.5707963267948966F, -1.3089969389957472F, 1.5707963267948966F);
        this.leftWingPlate1 = new ModelRenderer(this, 42, 77);
        this.leftWingPlate1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.leftWingPlate1.addBox(4.0F, -7.900000095367432F, -4.5F, 1, 3, 11, 0.0F);
        this.setRotateAngle(leftWingPlate1, 0.13962633907794952F, -0.0F, 0.0F);
        this.rightShoulder = new ModelRenderer(this, 0, 66);
        this.rightShoulder.mirror = true;
        this.rightShoulder.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.rightShoulder.addBox(-4.0F, -3.0F, -3.0F, 5, 5, 6, 0.0F);
        this.leftLegFloater3 = new ModelRenderer(this, 0, 78);
        this.leftLegFloater3.mirror = true;
        this.leftLegFloater3.setRotationPoint(0.0F, 5.0F, 0.0F);
        this.leftLegFloater3.addBox(4.0F, -1.5F, -1.5F, 1, 3, 3, 0.0F);
        this.setRotateAngle(leftLegFloater3, -0.7681194038027044F, -1.2027063875492923F, 0.8026769229921922F);
        this.facePlate4 = new ModelRenderer(this, 42, 63);
        this.facePlate4.mirror = true;
        this.facePlate4.setRotationPoint(0.0F, 0.5F, 0.0F);
        this.facePlate4.addBox(-2.0F, -3.5F, -5.5F, 4, 3, 1, 0.0F);
        this.setRotateAngle(facePlate4, 0.05235987901687623F, 0.34906584024429316F, 0.0F);
        this.leftLegFloater1 = new ModelRenderer(this, 0, 78);
        this.leftLegFloater1.mirror = true;
        this.leftLegFloater1.setRotationPoint(0.0F, 5.0F, 0.0F);
        this.leftLegFloater1.addBox(4.0F, -1.5F, -1.5F, 1, 3, 3, 0.0F);
        this.setRotateAngle(leftLegFloater1, 0.0F, -0.0F, 0.2617993877991494F);
        this.rightLegFloater2 = new ModelRenderer(this, 0, 78);
        this.rightLegFloater2.setRotationPoint(0.0F, 5.0F, 0.0F);
        this.rightLegFloater2.addBox(-5.0F, -1.5F, -1.5F, 1, 3, 3, 0.0F);
        this.setRotateAngle(rightLegFloater2, -0.7681194038027044F, 1.2027063875492923F, -0.8026769229921922F);
        this.leftFloater3 = new ModelRenderer(this, 0, 78);
        this.leftFloater3.setRotationPoint(1.0F, 7.0F, 0.0F);
        this.leftFloater3.addBox(3.5F, -2.0F, -1.5F, 1, 3, 3, 0.0F);
        this.setRotateAngle(leftFloater3, 1.5707963267948966F, 1.3089969389957472F, 1.5707963267948966F);
        this.leftWingPlate2 = new ModelRenderer(this, 33, 82);
        this.leftWingPlate2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.leftWingPlate2.addBox(4.0F, -4.900000095367432F, -2.5F, 1, 2, 3, 0.0F);
        this.setRotateAngle(leftWingPlate2, 0.13962633907794952F, -0.0F, 0.0F);
        this.facePlate3 = new ModelRenderer(this, 42, 63);
        this.facePlate3.setRotationPoint(0.0F, 0.5F, 0.0F);
        this.facePlate3.addBox(-2.0F, -3.5F, -5.5F, 4, 3, 1, 0.0F);
        this.setRotateAngle(facePlate3, 0.05235987901687623F, -0.34906584024429316F, 0.0F);
        this.rightLegFloater3 = new ModelRenderer(this, 0, 78);
        this.rightLegFloater3.setRotationPoint(0.0F, 5.0F, 0.0F);
        this.rightLegFloater3.addBox(-5.0F, -1.5F, -1.5F, 1, 3, 3, 0.0F);
        this.setRotateAngle(rightLegFloater3, 0.7681194038027044F, -1.2027063875492923F, -0.8026769229921922F);
        this.rightArmPlate3 = new ModelRenderer(this, 21, 51);
        this.rightArmPlate3.mirror = true;
        this.rightArmPlate3.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.rightArmPlate3.addBox(-3.5F, 1.0F, -4.0F, 1, 4, 8, 0.0F);
        this.setRotateAngle(rightArmPlate3, 0.0F, -0.0F, 0.5585053606381855F);
        this.chestPlate6 = new ModelRenderer(this, 29, 33);
        this.chestPlate6.mirror = true;
        this.chestPlate6.setRotationPoint(0.0F, 3.0F, -3.0F);
        this.chestPlate6.addBox(-6.0F, -1.0F, 0.0F, 5, 2, 1, 0.0F);
        this.setRotateAngle(chestPlate6, 0.17457550921422682F, -0.060447682496676876F, -0.5246555175572628F);
        this.belt = new ModelRenderer(this, 0, 85);
        this.belt.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.belt.addBox(-5.0F, 11.0F, -3.0F, 10, 2, 6, 0.0F);
        this.rightArmPlate2 = new ModelRenderer(this, 21, 51);
        this.rightArmPlate2.mirror = true;
        this.rightArmPlate2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.rightArmPlate2.addBox(-4.5F, -0.5F, -4.0F, 1, 4, 8, 0.0F);
        this.setRotateAngle(rightArmPlate2, 0.0F, -0.0F, 0.5585053606381855F);
        this.rightFoot2 = new ModelRenderer(this, 27, 110);
        this.rightFoot2.mirror = true;
        this.rightFoot2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.rightFoot2.addBox(-3.0F, 7.5F, -3.0F, 5, 2, 6, 0.0F);
        this.rightLegFloater1 = new ModelRenderer(this, 0, 78);
        this.rightLegFloater1.setRotationPoint(0.0F, 5.0F, 0.0F);
        this.rightLegFloater1.addBox(-5.0F, -1.5F, -1.5F, 1, 3, 3, 0.0F);
        this.setRotateAngle(rightLegFloater1, 0.0F, -0.0F, -0.2617993877991494F);
        this.leftFloater2 = new ModelRenderer(this, 0, 78);
        this.leftFloater2.setRotationPoint(1.0F, 7.0F, 0.0F);
        this.leftFloater2.addBox(3.5F, -2.0F, -1.5F, 1, 3, 3, 0.0F);
        this.setRotateAngle(leftFloater2, 0.148352986419518F, 0.5040510879759623F, 0.30002209841782523F);
        this.rightFloater2 = new ModelRenderer(this, 0, 78);
        this.rightFloater2.mirror = true;
        this.rightFloater2.setRotationPoint(-1.0F, 7.0F, 0.0F);
        this.rightFloater2.addBox(-4.5F, -2.0F, -1.5F, 1, 3, 3, 0.0F);
        this.setRotateAngle(rightFloater2, -1.5707963267948966F, 1.3089969389957472F, -1.5707963267948966F);
        this.rightWingPlate1 = new ModelRenderer(this, 42, 77);
        this.rightWingPlate1.mirror = true;
        this.rightWingPlate1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.rightWingPlate1.addBox(-5.0F, -7.900000095367432F, -4.5F, 1, 3, 11, 0.0F);
        this.setRotateAngle(rightWingPlate1, 0.13962633907794952F, -0.0F, 0.0F);

        this.leftArm = new ModelRenderer(this, 0, 51);
        this.leftArm.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.leftArm.addBox(-1.5F, 2.0F, -2.5F, 5, 9, 5, 0.0F);
        this.rightLeg = new ModelRenderer(this, 0, 94);
        this.rightLeg.mirror = true;
        this.rightLeg.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.rightLeg.addBox(-2.5F, 0.0F, -2.5F, 5, 10, 5, 0.0F);
        this.leftLegFloater2 = new ModelRenderer(this, 0, 78);
        this.leftLegFloater2.mirror = true;
        this.leftLegFloater2.setRotationPoint(0.0F, 5.0F, 0.0F);
        this.leftLegFloater2.addBox(4.0F, -1.5F, -1.5F, 1, 3, 3, 0.0F);
        this.setRotateAngle(leftLegFloater2, 0.7681194038027044F, 1.2027063875492923F, 0.8026769229921922F);
        this.chestMain = new ModelRenderer(this, 0, 33);
        this.chestMain.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.chestMain.addBox(-4.5F, -0.5F, -2.5F, 9, 12, 5, 0.0F);
        this.chestPlate4 = new ModelRenderer(this, 29, 33);
        this.chestPlate4.mirror = true;
        this.chestPlate4.setRotationPoint(0.0F, 3.0F, -3.0F);
        this.chestPlate4.addBox(-6.0F, -1.0F, 0.0F, 5, 2, 1, 0.0F);
        this.setRotateAngle(chestPlate4, 0.10467716894167224F, -0.060447682496676876F, 0.5246555175572628F);
        this.leftLeg = new ModelRenderer(this, 0, 94);
        this.leftLeg.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.leftLeg.addBox(-2.5F, 0.0F, -2.5F, 5, 10, 5, 0.0F);

        this.rightFloater3 = new ModelRenderer(this, 0, 78);
        this.rightFloater3.mirror = true;
        this.rightFloater3.setRotationPoint(-1.0F, 7.0F, 0.0F);
        this.rightFloater3.addBox(-4.5F, -2.0F, -1.5F, 1, 3, 3, 0.0F);
        this.setRotateAngle(rightFloater3, 0.148352986419518F, -0.5040510879759623F, -0.30002209841782523F);
        this.chestPlate2 = new ModelRenderer(this, 29, 33);
        this.chestPlate2.setRotationPoint(0.0F, 3.0F, -3.0F);
        this.chestPlate2.addBox(1.0F, -1.0F, 0.0F, 5, 2, 1, 0.0F);
        this.setRotateAngle(chestPlate2, 0.13962633907794952F, 0.06981316953897476F, 0.0F);
        this.rightFootPlate = new ModelRenderer(this, 21, 94);
        this.rightFootPlate.mirror = true;
        this.rightFootPlate.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.rightFootPlate.addBox(-1.5F, 9.5F, -3.5F, 1, 3, 7, 0.0F);
        this.setRotateAngle(rightFootPlate, 0.0F, -0.0F, 0.22689280275926282F);
        this.chestOrnament = new ModelRenderer(this, 29, 37);
        this.chestOrnament.setRotationPoint(0.0F, 3.0F, -3.0F);
        this.chestOrnament.addBox(-2.0F, -2.0F, -0.5F, 4, 4, 1, 0.0F);
        this.rightFloater1 = new ModelRenderer(this, 0, 78);
        this.rightFloater1.mirror = true;
        this.rightFloater1.setRotationPoint(-1.0F, 7.0F, 0.0F);
        this.rightFloater1.addBox(-4.5F, -2.0F, -1.5F, 1, 3, 3, 0.0F);
        this.setRotateAngle(rightFloater1, -0.148352986419518F, 0.5040510879759623F, -0.30002209841782523F);
        this.leftArmPlate2 = new ModelRenderer(this, 21, 51);
        this.leftArmPlate2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.leftArmPlate2.addBox(3.5F, -0.5F, -4.0F, 1, 4, 8, 0.0F);
        this.setRotateAngle(leftArmPlate2, 0.0F, -0.0F, -0.5585053606381855F);
        this.rightFoot1 = new ModelRenderer(this, 0, 110);
        this.rightFoot1.mirror = true;
        this.rightFoot1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.rightFoot1.addBox(-3.0F, 9.5F, -5.0F, 5, 3, 8, 0.0F);
        this.chestPlate3 = new ModelRenderer(this, 29, 33);
        this.chestPlate3.setRotationPoint(0.0F, 3.0F, -3.0F);
        this.chestPlate3.addBox(1.0F, -1.0F, 0.0F, 5, 2, 1, 0.0F);
        this.setRotateAngle(chestPlate3, 0.17457550921422682F, 0.060447682496676876F, 0.5246555175572628F);
        this.chestPlate1 = new ModelRenderer(this, 29, 33);
        this.chestPlate1.setRotationPoint(0.0F, 3.0F, -3.0F);
        this.chestPlate1.addBox(1.0F, -1.0F, 0.0F, 5, 2, 1, 0.0F);
        this.setRotateAngle(chestPlate1, 0.10467716894167224F, 0.060447682496676876F, -0.5246555175572628F);
        this.rightArm = new ModelRenderer(this, 0, 51);
        this.rightArm.mirror = true;
        this.rightArm.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.rightArm.addBox(-3.5F, 2.0F, -2.5F, 5, 9, 5, 0.0F);
        this.leftFoot2 = new ModelRenderer(this, 27, 110);
        this.leftFoot2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.leftFoot2.addBox(-2.0F, 7.5F, -3.0F, 5, 2, 6, 0.0F);
        this.leftFootPlate = new ModelRenderer(this, 21, 94);
        this.leftFootPlate.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.leftFootPlate.addBox(0.5F, 9.5F, -3.5F, 1, 3, 7, 0.0F);
        this.setRotateAngle(leftFootPlate, 0.0F, -0.0F, -0.22689280275926282F);
        this.facePlate1 = new ModelRenderer(this, 23, 64);
        this.facePlate1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.facePlate1.addBox(-4.0F, -4.5F, -4.5F, 8, 2, 1, 0.0F);
        this.leftArmPlate1 = new ModelRenderer(this, 21, 51);
        this.leftArmPlate1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.leftArmPlate1.addBox(4.5F, -2.0F, -4.0F, 1, 4, 8, 0.0F);
        this.setRotateAngle(leftArmPlate1, 0.0F, -0.0F, -0.5585053606381855F);
        this.topWingPlate = new ModelRenderer(this, 34, 68);
        this.topWingPlate.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.topWingPlate.addBox(-4.5F, -2.5F, -9.100000381469727F, 9, 3, 1, 0.0F);
        this.setRotateAngle(topWingPlate, -0.9773843884468076F, -0.0F, 0.0F);
        this.rightWingPlate3 = new ModelRenderer(this, 38, 92);
        this.rightWingPlate3.mirror = true;
        this.rightWingPlate3.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.rightWingPlate3.addBox(-4.5F, -5.0F, -3.9000000953674316F, 1, 5, 9, 0.0F);
        this.setRotateAngle(rightWingPlate3, 0.0F, -0.12217304855585097F, 0.0F);

        this.backPlate = new ModelRenderer(this, 59, 102);
        this.backPlate.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.backPlate.addBox(-4.5F, -7.5F, 4.0F, 9, 8, 1, 0.0F);
        this.setRotateAngle(backPlate, 0.1047197580337524F, -0.0F, 0.0F);
        this.facePlate2 = new ModelRenderer(this, 23, 68);
        this.facePlate2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.facePlate2.addBox(-1.0F, -4.5F, -5.0F, 2, 2, 1, 0.0F);
        this.rightFloater4 = new ModelRenderer(this, 0, 78);
        this.rightFloater4.mirror = true;
        this.rightFloater4.setRotationPoint(-1.0F, 7.0F, 0.0F);
        this.rightFloater4.addBox(-4.5F, -2.0F, -1.5F, 1, 3, 3, 0.0F);
        this.setRotateAngle(rightFloater4, 1.5707963267948966F, -1.3089969389957472F, -1.5707963267948966F);
        this.forePlate = new ModelRenderer(this, 23, 77);
        this.forePlate.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.forePlate.addBox(-4.0F, -7.900000095367432F, -4.5F, 8, 3, 1, 0.0F);
        this.setRotateAngle(forePlate, 0.13962633907794952F, -0.0F, 0.0F);
        this.chestPlate5 = new ModelRenderer(this, 29, 33);
        this.chestPlate5.mirror = true;
        this.chestPlate5.setRotationPoint(0.0F, 3.0F, -3.0F);
        this.chestPlate5.addBox(-6.0F, -1.0F, 0.0F, 5, 2, 1, 0.0F);
        this.setRotateAngle(chestPlate5, 0.13962633907794952F, -0.06981316953897476F, 0.0F);

        this.bipedBody.cubeList.clear();
        this.bipedHead.cubeList.clear();
        this.bipedHeadwear.cubeList.clear();
        this.bipedLeftArm.cubeList.clear();
        this.bipedLeftLeg.cubeList.clear();
        this.bipedRightArm.cubeList.clear();
        this.bipedRightLeg.cubeList.clear();
        
        if(addChestPiece)
        {
            this.bipedLeftArm.addChild(this.leftFloater1);
            this.bipedRightArm.addChild(this.rightArmPlate1);
            this.bipedLeftArm.addChild(this.leftShoulder);
            this.bipedLeftArm.addChild(this.leftArmPlate3);
            this.bipedLeftArm.addChild(this.leftFloater4);
            this.bipedRightArm.addChild(this.rightShoulder);
            this.bipedLeftArm.addChild(this.leftFloater3);
            this.bipedRightArm.addChild(this.rightArmPlate3);
            this.bipedBody.addChild(this.chestPlate6);
            this.bipedRightArm.addChild(this.rightArmPlate2);
            this.bipedLeftArm.addChild(this.leftFloater2);
            this.bipedRightArm.addChild(this.rightFloater2);
            this.bipedLeftArm.addChild(this.leftArm);
            this.bipedBody.addChild(this.chestMain);
            this.bipedBody.addChild(this.chestPlate4);
            this.bipedRightArm.addChild(this.rightFloater3);
            this.bipedBody.addChild(this.chestPlate2);
            this.bipedBody.addChild(this.chestOrnament);
            this.bipedRightArm.addChild(this.rightFloater1);
            this.bipedLeftArm.addChild(this.leftArmPlate2);
            this.bipedBody.addChild(this.chestPlate3);
            this.bipedBody.addChild(this.chestPlate1);
            this.bipedRightArm.addChild(this.rightArm);
            this.bipedRightArm.addChild(this.rightFloater4);
            this.bipedBody.addChild(this.chestPlate5);
            this.bipedLeftArm.addChild(this.leftArmPlate1);
        }
        
        if(addLeggings)
        {
            this.bipedLeftLeg.addChild(this.leftLegFloater3);
            this.bipedLeftLeg.addChild(this.leftLegFloater1);
            this.bipedRightLeg.addChild(this.rightLegFloater2);
            this.bipedRightLeg.addChild(this.rightLegFloater3);
            this.bipedBody.addChild(this.belt);
            this.bipedRightLeg.addChild(this.rightLegFloater1);
            this.bipedRightLeg.addChild(this.rightLeg);
            this.bipedLeftLeg.addChild(this.leftLegFloater2);
            this.bipedLeftLeg.addChild(this.leftLeg);
        }
        
        if(addBoots)
        {
            this.bipedLeftLeg.addChild(this.leftFoot1);
            this.bipedRightLeg.addChild(this.rightFoot2);
            this.bipedRightLeg.addChild(this.rightFootPlate);
            this.bipedRightLeg.addChild(this.rightFoot1);
            this.bipedLeftLeg.addChild(this.leftFoot2);
            this.bipedLeftLeg.addChild(this.leftFootPlate);
        }
        
        if(addHelmet)
        {
            this.bipedHead.addChild(this.rightWingPlate2);
            this.bipedHead.addChild(this.leftWingPlate3);
            this.bipedHead.addChild(this.topPlate);
            this.bipedHead.addChild(this.leftWingPlate1);
            this.bipedHead.addChild(this.facePlate4);
            this.bipedHead.addChild(this.leftWingPlate2);
            this.bipedHead.addChild(this.facePlate3);
            this.bipedHead.addChild(this.rightWingPlate1);
            this.bipedHead.addChild(this.facePlate1);
            this.bipedHead.addChild(this.topWingPlate);
            this.bipedHead.addChild(this.rightWingPlate3);
            this.bipedHead.addChild(this.backPlate);
            this.bipedHead.addChild(this.facePlate2);
            this.bipedHead.addChild(this.forePlate);
        }
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) 
    { 
    	this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        this.bipedBody.render(f5);
        this.bipedRightLeg.render(f5);
        this.bipedLeftLeg.render(f5);
        this.bipedRightArm.render(f5);
        this.bipedLeftArm.render(f5);
        this.bipedHead.render(f5);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
