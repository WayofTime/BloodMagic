package WayofTime.alchemicalWizardry.common.renderer.projectile;

import WayofTime.alchemicalWizardry.common.renderer.model.ModelMeteor;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class RenderMeteor extends Render
{
    public ModelBase model = new ModelMeteor();
    private static final ResourceLocation field_110833_a = new ResourceLocation("alchemicalwizardry", "textures/models/Meteor.png"); //refers to:YourMod/modelsTextureFile/optionalFile/yourTexture.png
    private float scale = 1.0f;

    @Override
    public void doRender(Entity entity, double d0, double d1, double d2, float f, float f1)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef((float) d0, (float) d1, (float) d2);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glScalef(scale, scale, scale);
        this.bindTexture(this.getEntityTexture(entity));
        GL11.glRotatef(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * f1, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(180.0f - entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * f1, 1.0F, 0.0F, 0.0f);
        model.render(entity, 0, (float) d0, (float) d1, (float) d2, f, f1);
        //GL11.glRotatef(entity.getRotationYawHead(), 0.0F, 1.0F, 0.0F);
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        // TODO Auto-generated method stub
        return field_110833_a;
    }
}
