package WayofTime.bloodmagic.client.render.entity;

import WayofTime.bloodmagic.client.render.entity.layer.LayerWill;
import WayofTime.bloodmagic.client.render.model.ModelCorruptedChicken;
import WayofTime.bloodmagic.entity.mob.EntityCorruptedChicken;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderCorruptedChicken extends RenderLiving<EntityCorruptedChicken> {
    private static final ResourceLocation CHICKEN_TEXTURES = new ResourceLocation("textures/entity/chicken.png");

    public RenderCorruptedChicken(RenderManager renderManagerIn) {
        super(renderManagerIn, new ModelCorruptedChicken(0), 0.3f);
        this.addLayer(new LayerWill<>(this, new ModelCorruptedChicken(1.1f)));
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityCorruptedChicken entity) {
        return CHICKEN_TEXTURES;
    }

    @Override
    protected float handleRotationFloat(EntityCorruptedChicken livingBase, float partialTicks) {
        float f = livingBase.oFlap + (livingBase.wingRotation - livingBase.oFlap) * partialTicks;
        float f1 = livingBase.oFlapSpeed + (livingBase.destPos - livingBase.oFlapSpeed) * partialTicks;
        return (MathHelper.sin(f) + 1.0F) * f1;
    }
}