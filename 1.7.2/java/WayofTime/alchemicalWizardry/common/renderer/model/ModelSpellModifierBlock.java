package WayofTime.alchemicalWizardry.common.renderer.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.common.util.ForgeDirection;

public class ModelSpellModifierBlock extends ModelBase
{
  //fields
    ModelRenderer core;
    ModelRenderer inputMain;
    ModelRenderer Shape2;
    ModelRenderer Shape3;
    ModelRenderer Shape4;
    ModelRenderer Shape5;
    ModelRenderer Shape6;
    ModelRenderer Shape7;
    ModelRenderer Shape8;
    ModelRenderer Shape9;
    ModelRenderer outputMain;
    ModelRenderer Shape11;
    ModelRenderer Shape12;
    ModelRenderer Shape13;
    ModelRenderer Shape14;
    ModelRenderer output1;
    ModelRenderer output2;
    ModelRenderer output3;
    ModelRenderer output4;
  
  public ModelSpellModifierBlock()
  {
    textureWidth = 64;
    textureHeight = 64;
    
      core = new ModelRenderer(this, 0, 0);
      core.addBox(-3F, -3F, -3F, 6, 6, 6);
      core.setRotationPoint(0F, 16F, 0F);
      core.setTextureSize(64, 64);
      core.mirror = true;
      setRotation(core, 0F, 0F, 0F);
      inputMain = new ModelRenderer(this, 25, 18);
      inputMain.addBox(-2F, -2F, -8F, 4, 4, 1);
      inputMain.setRotationPoint(0F, 16F, 0F);
      inputMain.setTextureSize(64, 64);
      inputMain.mirror = true;
      setRotation(inputMain, 0F, 0F, 0F);
      Shape2 = new ModelRenderer(this, 0, 13);
      Shape2.addBox(-5F, -5F, -8F, 10, 2, 2);
      Shape2.setRotationPoint(0F, 16F, 0F);
      Shape2.setTextureSize(64, 64);
      Shape2.mirror = true;
      setRotation(Shape2, 0F, 0F, 0F);
      Shape3 = new ModelRenderer(this, 0, 27);
      Shape3.addBox(-5F, 3F, -8F, 10, 2, 2);
      Shape3.setRotationPoint(0F, 16F, 0F);
      Shape3.setTextureSize(64, 64);
      Shape3.mirror = true;
      setRotation(Shape3, 0F, 0F, 0F);
      Shape4 = new ModelRenderer(this, 16, 18);
      Shape4.addBox(3F, -3F, -8F, 2, 6, 2);
      Shape4.setRotationPoint(0F, 16F, 0F);
      Shape4.setTextureSize(64, 64);
      Shape4.mirror = true;
      setRotation(Shape4, 0F, 0F, 0F);
      Shape5 = new ModelRenderer(this, 0, 18);
      Shape5.addBox(-5F, -3F, -8F, 2, 6, 2);
      Shape5.setRotationPoint(0F, 16F, 0F);
      Shape5.setTextureSize(64, 64);
      Shape5.mirror = true;
      setRotation(Shape5, 0F, 0F, 0F);
      Shape6 = new ModelRenderer(this, 0, 32);
      Shape6.addBox(-1F, -6F, -7F, 2, 1, 5);
      Shape6.setRotationPoint(0F, 16F, 0F);
      Shape6.setTextureSize(64, 64);
      Shape6.mirror = true;
      setRotation(Shape6, 0F, 0F, 0F);
      Shape7 = new ModelRenderer(this, 15, 32);
      Shape7.addBox(-2F, -6F, -2F, 4, 1, 4);
      Shape7.setRotationPoint(0F, 16F, 0F);
      Shape7.setTextureSize(64, 64);
      Shape7.mirror = true;
      setRotation(Shape7, 0F, 0F, 0F);
      Shape8 = new ModelRenderer(this, 15, 39);
      Shape8.addBox(-2F, 5F, -2F, 4, 1, 4);
      Shape8.setRotationPoint(0F, 16F, 0F);
      Shape8.setTextureSize(64, 64);
      Shape8.mirror = true;
      setRotation(Shape8, 0F, 0F, 0F);
      Shape9 = new ModelRenderer(this, 0, 39);
      Shape9.addBox(-1F, 5F, -7F, 2, 1, 5);
      Shape9.setRotationPoint(0F, 16F, 0F);
      Shape9.setTextureSize(64, 64);
      Shape9.mirror = true;
      setRotation(Shape9, 0F, 0F, 0F);
      outputMain = new ModelRenderer(this, 51, 23);
      outputMain.addBox(7F, -2F, -2F, 1, 4, 4);
      outputMain.setRotationPoint(0F, 16F, 0F);
      outputMain.setTextureSize(64, 64);
      outputMain.mirror = true;
      setRotation(outputMain, 0F, 0F, 0F);
      Shape11 = new ModelRenderer(this, 13, 46);
      Shape11.addBox(5F, -2F, -2F, 1, 4, 4);
      Shape11.setRotationPoint(0F, 16F, 0F);
      Shape11.setTextureSize(64, 64);
      Shape11.mirror = true;
      setRotation(Shape11, 0F, 0F, 0F);
      Shape12 = new ModelRenderer(this, 0, 46);
      Shape12.addBox(5F, -1F, -7F, 1, 2, 5);
      Shape12.setRotationPoint(0F, 16F, 0F);
      Shape12.setTextureSize(64, 64);
      Shape12.mirror = true;
      setRotation(Shape12, 0F, 0F, 0F);
      Shape13 = new ModelRenderer(this, 0, 56);
      Shape13.addBox(-6F, -1F, -7F, 1, 2, 5);
      Shape13.setRotationPoint(0F, 16F, 0F);
      Shape13.setTextureSize(64, 64);
      Shape13.mirror = true;
      setRotation(Shape13, 0F, 0F, 0F);
      Shape14 = new ModelRenderer(this, 13, 56);
      Shape14.addBox(-6F, -2F, -2F, 1, 4, 4);
      Shape14.setRotationPoint(0F, 16F, 0F);
      Shape14.setTextureSize(64, 64);
      Shape14.mirror = true;
      setRotation(Shape14, 0F, 0F, 0F);
      output1 = new ModelRenderer(this, 51, 18);
      output1.addBox(5F, -5F, -5F, 3, 2, 2);
      output1.setRotationPoint(0F, 16F, 0F);
      output1.setTextureSize(64, 64);
      output1.mirror = true;
      setRotation(output1, 0F, 0F, 0F);
      output2 = new ModelRenderer(this, 51, 18);
      output2.addBox(5F, -5F, 3F, 3, 2, 2);
      output2.setRotationPoint(0F, 16F, 0F);
      output2.setTextureSize(64, 64);
      output2.mirror = true;
      setRotation(output2, 0F, 0F, 0F);
      output3 = new ModelRenderer(this, 51, 18);
      output3.addBox(5F, 3F, -5F, 3, 2, 2);
      output3.setRotationPoint(0F, 16F, 0F);
      output3.setTextureSize(64, 64);
      output3.mirror = true;
      setRotation(output3, 0F, 0F, 0F);
      output4 = new ModelRenderer(this, 51, 18);
      output4.addBox(5F, 3F, 3F, 3, 2, 2);
      output4.setRotationPoint(0F, 16F, 0F);
      output4.setTextureSize(64, 64);
      output4.mirror = true;
      setRotation(output4, 0F, 0F, 0F);
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
    this.setRotation(Shape2, xInputRot, yInputRot, zInputRot);
    this.setRotation(Shape3, xInputRot, yInputRot, zInputRot);
    this.setRotation(Shape4, xInputRot, yInputRot, zInputRot);
    this.setRotation(Shape5, xInputRot, yInputRot, zInputRot);
    this.setRotation(Shape6, xInputRot, yInputRot, zInputRot);
    this.setRotation(Shape7, xInputRot, yInputRot, zInputRot);
    this.setRotation(Shape8, xInputRot, yInputRot, zInputRot);
    this.setRotation(Shape9, xInputRot, yInputRot, zInputRot);
    this.setRotation(Shape12, xInputRot, yInputRot, zInputRot);
    this.setRotation(Shape11, xInputRot, yInputRot, zInputRot);
    this.setRotation(Shape13, xInputRot, yInputRot, zInputRot);
    this.setRotation(Shape14, xInputRot, yInputRot, zInputRot);

    this.setRotation(outputMain, xOutputRot, yOutputRot, zOutputRot);
    this.setRotation(output1, xOutputRot, yOutputRot, zOutputRot);
    this.setRotation(output2, xOutputRot, yOutputRot, zOutputRot);
    this.setRotation(output3, xOutputRot, yOutputRot, zOutputRot);
    this.setRotation(output4, xOutputRot, yOutputRot, zOutputRot);

    
    core.render(f5);
    inputMain.render(f5);
    Shape2.render(f5);
    Shape3.render(f5);
    Shape4.render(f5);
    Shape5.render(f5);
    Shape6.render(f5);
    Shape7.render(f5);
    Shape8.render(f5);
    Shape9.render(f5);
    outputMain.render(f5);
    Shape11.render(f5);
    Shape12.render(f5);
    Shape13.render(f5);
    Shape14.render(f5);
    output1.render(f5);
    output2.render(f5);
    output3.render(f5);
    output4.render(f5);
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

