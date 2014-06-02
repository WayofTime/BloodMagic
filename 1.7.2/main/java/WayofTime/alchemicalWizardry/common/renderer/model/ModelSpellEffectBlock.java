package WayofTime.alchemicalWizardry.common.renderer.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.common.util.ForgeDirection;

public class ModelSpellEffectBlock extends ModelBase
{
  //fields
    ModelRenderer core;
    ModelRenderer frame1;
    ModelRenderer frame2;
    ModelRenderer frame3;
    ModelRenderer frame4;
    ModelRenderer frame5;
    ModelRenderer frame6;
    ModelRenderer frame7;
    ModelRenderer frame8;
    ModelRenderer frame9;
    ModelRenderer frame10;
    ModelRenderer frame11;
    ModelRenderer frame12;
    ModelRenderer inputSpacer1;
    ModelRenderer inputFace;
    ModelRenderer inputSpacer2;
    ModelRenderer inputSpacer3;
    ModelRenderer inputSpacer4;
    ModelRenderer outputFace;
    ModelRenderer outputPlug;
    ModelRenderer outputSpacer1;
    ModelRenderer outputSpacer2;
    ModelRenderer outputSpacer3;
    ModelRenderer outputSpacer4;
  
  public ModelSpellEffectBlock()
  {
    textureWidth = 64;
    textureHeight = 64;
    
    core = new ModelRenderer(this, 0, 0);
    core.addBox(-3F, -3F, -3F, 6, 6, 6);
    core.setRotationPoint(0F, 16F, 0F);
    core.setTextureSize(64, 64);
    core.mirror = true;
    setRotation(core, 0F, 0F, 0F);
    frame1 = new ModelRenderer(this, 16, 18);
    frame1.addBox(3F, -3F, -5F, 2, 6, 2);
    frame1.setRotationPoint(0F, 16F, 0F);
    frame1.setTextureSize(64, 64);
    frame1.mirror = true;
    setRotation(frame1, 0F, 0F, 0F);
    frame2 = new ModelRenderer(this, 0, 18);
    frame2.addBox(-5F, -3F, -5F, 2, 6, 2);
    frame2.setRotationPoint(0F, 16F, 0F);
    frame2.setTextureSize(64, 64);
    frame2.mirror = true;
    setRotation(frame2, 0F, 0F, 0F);
    frame3 = new ModelRenderer(this, 0, 13);
    frame3.addBox(-5F, -5F, -5F, 10, 2, 2);
    frame3.setRotationPoint(0F, 16F, 0F);
    frame3.setTextureSize(64, 64);
    frame3.mirror = true;
    setRotation(frame3, 0F, 0F, 0F);
    frame4 = new ModelRenderer(this, 0, 27);
    frame4.addBox(-5F, 3F, -5F, 10, 2, 2);
    frame4.setRotationPoint(0F, 16F, 0F);
    frame4.setTextureSize(64, 64);
    frame4.mirror = true;
    setRotation(frame4, 0F, 0F, 0F);
    frame5 = new ModelRenderer(this, 0, 34);
    frame5.addBox(-5F, -5F, 3F, 10, 2, 2);
    frame5.setRotationPoint(0F, 16F, 0F);
    frame5.setTextureSize(64, 64);
    frame5.mirror = true;
    setRotation(frame5, 0F, 0F, 0F);
    frame6 = new ModelRenderer(this, 0, 48);
    frame6.addBox(-5F, 3F, 3F, 10, 2, 2);
    frame6.setRotationPoint(0F, 16F, 0F);
    frame6.setTextureSize(64, 64);
    frame6.mirror = true;
    setRotation(frame6, 0F, 0F, 0F);
    frame7 = new ModelRenderer(this, 16, 39);
    frame7.addBox(-5F, -3F, 3F, 2, 6, 2);
    frame7.setRotationPoint(0F, 16F, 0F);
    frame7.setTextureSize(64, 64);
    frame7.mirror = true;
    setRotation(frame7, 0F, 0F, 0F);
    frame8 = new ModelRenderer(this, 0, 39);
    frame8.addBox(3F, -3F, 3F, 2, 6, 2);
    frame8.setRotationPoint(0F, 16F, 0F);
    frame8.setTextureSize(64, 64);
    frame8.mirror = true;
    setRotation(frame8, 0F, 0F, 0F);
    frame9 = new ModelRenderer(this, 25, 9);
    frame9.addBox(-5F, 3F, -3F, 2, 2, 6);
    frame9.setRotationPoint(0F, 16F, 0F);
    frame9.setTextureSize(64, 64);
    frame9.mirror = true;
    setRotation(frame9, 0F, 0F, 0F);
    frame10 = new ModelRenderer(this, 25, 0);
    frame10.addBox(-5F, -5F, -3F, 2, 2, 6);
    frame10.setRotationPoint(0F, 16F, 0F);
    frame10.setTextureSize(64, 64);
    frame10.mirror = true;
    setRotation(frame10, 0F, 0F, 0F);
    frame11 = new ModelRenderer(this, 42, 0);
    frame11.addBox(3F, -5F, -3F, 2, 2, 6);
    frame11.setRotationPoint(0F, 16F, 0F);
    frame11.setTextureSize(64, 64);
    frame11.mirror = true;
    setRotation(frame11, 0F, 0F, 0F);
    frame12 = new ModelRenderer(this, 42, 9);
    frame12.addBox(3F, 3F, -3F, 2, 2, 6);
    frame12.setRotationPoint(0F, 16F, 0F);
    frame12.setTextureSize(64, 64);
    frame12.mirror = true;
    setRotation(frame12, 0F, 0F, 0F);
    inputSpacer1 = new ModelRenderer(this, 25, 27);
    inputSpacer1.addBox(3F, -5F, -8F, 2, 2, 3);
    inputSpacer1.setRotationPoint(0F, 16F, 0F);
    inputSpacer1.setTextureSize(64, 64);
    inputSpacer1.mirror = true;
    setRotation(inputSpacer1, 0F, 0F, 0F);
    inputFace = new ModelRenderer(this, 38, 27);
    inputFace.addBox(-2F, -2F, -8F, 4, 4, 5);
    inputFace.setRotationPoint(0F, 16F, 0F);
    inputFace.setTextureSize(64, 64);
    inputFace.mirror = true;
    setRotation(inputFace, 0F, 0F, 0F);
    inputSpacer2 = new ModelRenderer(this, 25, 27);
    inputSpacer2.addBox(-5F, -5F, -8F, 2, 2, 3);
    inputSpacer2.setRotationPoint(0F, 16F, 0F);
    inputSpacer2.setTextureSize(64, 64);
    inputSpacer2.mirror = true;
    setRotation(inputSpacer2, 0F, 0F, 0F);
    inputSpacer3 = new ModelRenderer(this, 25, 27);
    inputSpacer3.addBox(3F, 3F, -8F, 2, 2, 3);
    inputSpacer3.setRotationPoint(0F, 16F, 0F);
    inputSpacer3.setTextureSize(64, 64);
    inputSpacer3.mirror = true;
    setRotation(inputSpacer3, 0F, 0F, 0F);
    inputSpacer4 = new ModelRenderer(this, 25, 27);
    inputSpacer4.addBox(-5F, 3F, -8F, 2, 2, 3);
    inputSpacer4.setRotationPoint(0F, 16F, 0F);
    inputSpacer4.setTextureSize(64, 64);
    inputSpacer4.mirror = true;
    setRotation(inputSpacer4, 0F, 0F, 0F);
    outputFace = new ModelRenderer(this, 38, 37);
    outputFace.addBox(6F, -2F, -2F, 2, 4, 4);
    outputFace.setRotationPoint(0F, 16F, 0F);
    outputFace.setTextureSize(64, 64);
    outputFace.mirror = true;
    setRotation(outputFace, 0F, 0F, 0F);
    outputPlug = new ModelRenderer(this, 36, 48);
    outputPlug.addBox(3F, -3F, -3F, 2, 6, 6);
    outputPlug.setRotationPoint(0F, 16F, 0F);
    outputPlug.setTextureSize(64, 64);
    outputPlug.mirror = true;
    setRotation(outputPlug, 0F, 0F, 0F);
    outputSpacer1 = new ModelRenderer(this, 25, 48);
    outputSpacer1.addBox(5F, -5F, -5F, 3, 2, 2);
    outputSpacer1.setRotationPoint(0F, 16F, 0F);
    outputSpacer1.setTextureSize(64, 64);
    outputSpacer1.mirror = true;
    setRotation(outputSpacer1, 0F, 0F, 0F);
    outputSpacer2 = new ModelRenderer(this, 25, 48);
    outputSpacer2.addBox(5F, -5F, 3F, 3, 2, 2);
    outputSpacer2.setRotationPoint(0F, 16F, 0F);
    outputSpacer2.setTextureSize(64, 64);
    outputSpacer2.mirror = true;
    setRotation(outputSpacer2, 0F, 0F, 0F);
    outputSpacer3 = new ModelRenderer(this, 25, 48);
    outputSpacer3.addBox(5F, 3F, -5F, 3, 2, 2);
    outputSpacer3.setRotationPoint(0F, 16F, 0F);
    outputSpacer3.setTextureSize(64, 64);
    outputSpacer3.mirror = true;
    setRotation(outputSpacer3, 0F, 0F, 0F);
    outputSpacer4 = new ModelRenderer(this, 25, 48);
    outputSpacer4.addBox(5F, 3F, 3F, 3, 2, 2);
    outputSpacer4.setRotationPoint(0F, 16F, 0F);
    outputSpacer4.setTextureSize(64, 64);
    outputSpacer4.mirror = true;
    setRotation(outputSpacer4, 0F, 0F, 0F);
  }
  
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5, ForgeDirection input, ForgeDirection output)
  {
    super.render(entity, f, f1, f2, f3, f4, f5);
    setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    float xInputRot = 0.0f;
    float yInputRot = 0.0f;
    float zInputRot = 0.0f;
    float xOutputRot = 0.0f;
    float yOutputRot = 0.0f;
    float zOutputRot = 0.0f;

    switch (input)
    {
        case NORTH:
            xInputRot = 0.0f;
            yInputRot = 0.0f;
            zInputRot = 0.0f;
            break;

        case EAST:
            xInputRot = 0.0f;
            yInputRot = (float) (0.5f * Math.PI);
            zInputRot = 0.0f;
            break;

        case SOUTH:
            xInputRot = 0.0f;
            yInputRot = (float) (1.0f * Math.PI);
            zInputRot = 0.0f;
            break;

        case WEST:
            xInputRot = 0.0f;
            yInputRot = (float) (-0.5f * Math.PI);
            zInputRot = 0.0f;
            break;

        case UP:
            xInputRot = (float) (-0.5f * Math.PI);
            yInputRot = 0.0f;
            zInputRot = 0.0f;
            break;

        case DOWN:
            xInputRot = (float) (0.5f * Math.PI);
            yInputRot = 0.0f;
            zInputRot = 0.0f;
            break;

        default:
            break;
    }

    switch (output)
    {
        case NORTH:
            xOutputRot = 0.0f;
            yOutputRot = (float) (0.5f * Math.PI);
            zOutputRot = 0.0f;
            break;

        case EAST:
            xOutputRot = 0.0f;
            yOutputRot = (float) (1.0f * Math.PI);
            zOutputRot = 0.0f;
            break;

        case SOUTH:
            xOutputRot = 0.0f;
            yOutputRot = (float) (-0.5f * Math.PI);
            zOutputRot = 0.0f;
            break;

        case WEST:
            xOutputRot = 0.0f;
            yOutputRot = 0.0f;
            zOutputRot = 0.0f;
            break;

        case UP:
            xOutputRot = 0.0f;
            yOutputRot = 0.0f;
            zOutputRot = (float) (-0.5f * Math.PI);
            break;

        case DOWN:
            xOutputRot = 0.0f;
            yOutputRot = 0.0f;
            zOutputRot = (float) (0.5f * Math.PI);
            break;

        default:
            break;
    }

    this.setRotation(inputFace, xInputRot, yInputRot, zInputRot);
    this.setRotation(outputFace, xOutputRot, yOutputRot, zOutputRot);
    this.setRotation(inputSpacer1, xInputRot, yInputRot, zInputRot);
    this.setRotation(inputSpacer2, xInputRot, yInputRot, zInputRot);
    this.setRotation(inputSpacer3, xInputRot, yInputRot, zInputRot);
    this.setRotation(inputSpacer4, xInputRot, yInputRot, zInputRot);
    this.setRotation(outputPlug, xOutputRot, yOutputRot, zOutputRot);
    this.setRotation(outputSpacer1, xOutputRot, yOutputRot, zOutputRot);
    this.setRotation(outputSpacer2, xOutputRot, yOutputRot, zOutputRot);
    this.setRotation(outputSpacer3, xOutputRot, yOutputRot, zOutputRot);
    this.setRotation(outputSpacer4, xOutputRot, yOutputRot, zOutputRot);
    
    core.render(f5);
    frame1.render(f5);
    frame2.render(f5);
    frame3.render(f5);
    frame4.render(f5);
    frame5.render(f5);
    frame6.render(f5);
    frame7.render(f5);
    frame8.render(f5);
    frame9.render(f5);
    frame10.render(f5);
    frame11.render(f5);
    frame12.render(f5);
    inputSpacer1.render(f5);
    inputFace.render(f5);
    inputSpacer2.render(f5);
    inputSpacer3.render(f5);
    inputSpacer4.render(f5);
    outputFace.render(f5);
    outputPlug.render(f5);
    outputSpacer1.render(f5);
    outputSpacer2.render(f5);
    outputSpacer3.render(f5);
    outputSpacer4.render(f5);
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
