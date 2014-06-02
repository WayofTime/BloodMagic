package WayofTime.alchemicalWizardry.common.renderer.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.common.util.ForgeDirection;


public class ModelSpellEnhancementBlock extends ModelBase
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
    ModelRenderer outputMain;
    ModelRenderer inputMain;
    ModelRenderer output1;
    ModelRenderer output2;
    ModelRenderer output3;
    ModelRenderer output4;
    ModelRenderer input1;
    ModelRenderer input2;
    ModelRenderer input3;
    ModelRenderer input4;
    ModelRenderer outputSecond;
  
  public ModelSpellEnhancementBlock()
  {
    textureWidth = 128;
    textureHeight = 64;
    
      core = new ModelRenderer(this, 0, 0);
      core.addBox(-3F, -3F, -3F, 6, 6, 6);
      core.setRotationPoint(0F, 16F, 0F);
      core.setTextureSize(128, 64);
      core.mirror = true;
      setRotation(core, 0F, 0F, 0F);
      frame1 = new ModelRenderer(this, 0, 32);
      frame1.addBox(-7F, 5F, -7F, 14, 2, 2);
      frame1.setRotationPoint(0F, 16F, 0F);
      frame1.setTextureSize(128, 64);
      frame1.mirror = true;
      setRotation(frame1, 0F, 0F, 0F);
      frame2 = new ModelRenderer(this, 24, 19);
      frame2.addBox(5F, -5F, -7F, 2, 10, 2);
      frame2.setRotationPoint(0F, 16F, 0F);
      frame2.setTextureSize(128, 64);
      frame2.mirror = true;
      setRotation(frame2, 0F, 0F, 0F);
      frame3 = new ModelRenderer(this, 0, 19);
      frame3.addBox(-7F, -5F, -7F, 2, 10, 2);
      frame3.setRotationPoint(0F, 16F, 0F);
      frame3.setTextureSize(128, 64);
      frame3.mirror = true;
      setRotation(frame3, 0F, 0F, 0F);
      frame4 = new ModelRenderer(this, 0, 14);
      frame4.addBox(-7F, -7F, -7F, 14, 2, 2);
      frame4.setRotationPoint(0F, 16F, 0F);
      frame4.setTextureSize(128, 64);
      frame4.mirror = true;
      setRotation(frame4, 0F, 0F, 0F);
      frame5 = new ModelRenderer(this, 0, 57);
      frame5.addBox(-7F, 5F, 5F, 14, 2, 2);
      frame5.setRotationPoint(0F, 16F, 0F);
      frame5.setTextureSize(128, 64);
      frame5.mirror = true;
      setRotation(frame5, 0F, 0F, 0F);
      frame6 = new ModelRenderer(this, 0, 44);
      frame6.addBox(5F, -5F, 5F, 2, 10, 2);
      frame6.setRotationPoint(0F, 16F, 0F);
      frame6.setTextureSize(128, 64);
      frame6.mirror = true;
      setRotation(frame6, 0F, 0F, 0F);
      frame7 = new ModelRenderer(this, 24, 44);
      frame7.addBox(-7F, -5F, 5F, 2, 10, 2);
      frame7.setRotationPoint(0F, 16F, 0F);
      frame7.setTextureSize(128, 64);
      frame7.mirror = true;
      setRotation(frame7, 0F, 0F, 0F);
      frame8 = new ModelRenderer(this, 0, 39);
      frame8.addBox(-7F, -7F, 5F, 14, 2, 2);
      frame8.setRotationPoint(0F, 16F, 0F);
      frame8.setTextureSize(128, 64);
      frame8.mirror = true;
      setRotation(frame8, 0F, 0F, 0F);
      frame9 = new ModelRenderer(this, 66, 14);
      frame9.addBox(5F, 5F, -5F, 2, 2, 10);
      frame9.setRotationPoint(0F, 16F, 0F);
      frame9.setTextureSize(128, 64);
      frame9.mirror = true;
      setRotation(frame9, 0F, 0F, 0F);
      frame10 = new ModelRenderer(this, 40, 14);
      frame10.addBox(-7F, 5F, -5F, 2, 2, 10);
      frame10.setRotationPoint(0F, 16F, 0F);
      frame10.setTextureSize(128, 64);
      frame10.mirror = true;
      setRotation(frame10, 0F, 0F, 0F);
      frame11 = new ModelRenderer(this, 66, 0);
      frame11.addBox(5F, -7F, -5F, 2, 2, 10);
      frame11.setRotationPoint(0F, 16F, 0F);
      frame11.setTextureSize(128, 64);
      frame11.mirror = true;
      setRotation(frame11, 0F, 0F, 0F);
      frame12 = new ModelRenderer(this, 40, 0);
      frame12.addBox(-7F, -7F, -5F, 2, 2, 10);
      frame12.setRotationPoint(0F, 16F, 0F);
      frame12.setTextureSize(128, 64);
      frame12.mirror = true;
      setRotation(frame12, 0F, 0F, 0F);
      outputMain = new ModelRenderer(this, 78, 36);
      outputMain.addBox(6F, -2F, -2F, 2, 4, 4);
      outputMain.setRotationPoint(0F, 16F, 0F);
      outputMain.setTextureSize(128, 64);
      outputMain.mirror = true;
      setRotation(outputMain, 0F, 0F, 0F);
      inputMain = new ModelRenderer(this, 40, 36);
      inputMain.addBox(-2F, -2F, -8F, 4, 4, 5);
      inputMain.setRotationPoint(0F, 16F, 0F);
      inputMain.setTextureSize(128, 64);
      inputMain.mirror = true;
      setRotation(inputMain, 0F, 0F, 0F);
      output1 = new ModelRenderer(this, 80, 30);
      output1.addBox(5F, -5F, -5F, 3, 2, 2);
      output1.setRotationPoint(0F, 16F, 0F);
      output1.setTextureSize(128, 64);
      output1.mirror = true;
      setRotation(output1, 0F, 0F, 0F);
      output2 = new ModelRenderer(this, 80, 30);
      output2.addBox(5F, -5F, 3F, 3, 2, 2);
      output2.setRotationPoint(0F, 16F, 0F);
      output2.setTextureSize(128, 64);
      output2.mirror = true;
      setRotation(output2, 0F, 0F, 0F);
      output3 = new ModelRenderer(this, 80, 30);
      output3.addBox(5F, 3F, -5F, 3, 2, 2);
      output3.setRotationPoint(0F, 16F, 0F);
      output3.setTextureSize(128, 64);
      output3.mirror = true;
      setRotation(output3, 0F, 0F, 0F);
      output4 = new ModelRenderer(this, 80, 30);
      output4.addBox(5F, 3F, 3F, 3, 2, 2);
      output4.setRotationPoint(0F, 16F, 0F);
      output4.setTextureSize(128, 64);
      output4.mirror = true;
      setRotation(output4, 0F, 0F, 0F);
      input1 = new ModelRenderer(this, 40, 27);
      input1.addBox(3F, -5F, -8F, 2, 2, 5);
      input1.setRotationPoint(0F, 16F, 0F);
      input1.setTextureSize(128, 64);
      input1.mirror = true;
      setRotation(input1, 0F, 0F, 0F);
      input2 = new ModelRenderer(this, 40, 27);
      input2.addBox(-5F, -5F, -8F, 2, 2, 5);
      input2.setRotationPoint(0F, 16F, 0F);
      input2.setTextureSize(128, 64);
      input2.mirror = true;
      setRotation(input2, 0F, 0F, 0F);
      input3 = new ModelRenderer(this, 40, 27);
      input3.addBox(3F, 3F, -8F, 2, 2, 5);
      input3.setRotationPoint(0F, 16F, 0F);
      input3.setTextureSize(128, 64);
      input3.mirror = true;
      setRotation(input3, 0F, 0F, 0F);
      input4 = new ModelRenderer(this, 40, 27);
      input4.addBox(-5F, 3F, -8F, 2, 2, 5);
      input4.setRotationPoint(0F, 16F, 0F);
      input4.setTextureSize(128, 64);
      input4.mirror = true;
      setRotation(input4, 0F, 0F, 0F);
      outputSecond = new ModelRenderer(this, 78, 47);
      outputSecond.addBox(4F, -3F, -3F, 1, 6, 6);
      outputSecond.setRotationPoint(0F, 16F, 0F);
      outputSecond.setTextureSize(128, 64);
      outputSecond.mirror = true;
      setRotation(outputSecond, 0F, 0F, 0F);
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

    this.setRotation(inputMain, xInputRot, yInputRot, zInputRot);
    this.setRotation(outputMain, xOutputRot, yOutputRot, zOutputRot);
    this.setRotation(input1, xInputRot, yInputRot, zInputRot);
    this.setRotation(input2, xInputRot, yInputRot, zInputRot);
    this.setRotation(input3, xInputRot, yInputRot, zInputRot);
    this.setRotation(input4, xInputRot, yInputRot, zInputRot);
    this.setRotation(outputSecond, xOutputRot, yOutputRot, zOutputRot);
    this.setRotation(output1, xOutputRot, yOutputRot, zOutputRot);
    this.setRotation(output2, xOutputRot, yOutputRot, zOutputRot);
    this.setRotation(output3, xOutputRot, yOutputRot, zOutputRot);
    this.setRotation(output4, xOutputRot, yOutputRot, zOutputRot);
    
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
    outputMain.render(f5);
    inputMain.render(f5);
    output1.render(f5);
    output2.render(f5);
    output3.render(f5);
    output4.render(f5);
    input1.render(f5);
    input2.render(f5);
    input3.render(f5);
    input4.render(f5);
    outputSecond.render(f5);
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
