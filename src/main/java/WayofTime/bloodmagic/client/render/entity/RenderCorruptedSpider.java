package WayofTime.bloodmagic.client.render.entity;

import WayofTime.bloodmagic.client.render.entity.layer.LayerCorruptedSpiderEyes;
import WayofTime.bloodmagic.client.render.entity.layer.LayerWill;
import WayofTime.bloodmagic.client.render.model.ModelCorruptedSpider;
import WayofTime.bloodmagic.entity.mob.EntityCorruptedSpider;
import net.minecraft.client.model.ModelSpider;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderCorruptedSpider extends RenderLiving<EntityCorruptedSpider> {
    private static final ResourceLocation SPIDER_TEXTURES = new ResourceLocation("textures/entity/spider/spider.png");

    public RenderCorruptedSpider(RenderManager renderManagerIn) {
        super(renderManagerIn, new ModelSpider(), 1.0F);
        this.addLayer(new LayerCorruptedSpiderEyes(this));
        this.addLayer(new LayerWill<>(this, new ModelCorruptedSpider(1.1f)));
    }

    protected float getDeathMaxRotation(EntityCorruptedSpider entityLivingBaseIn) {
        return 180.0F;
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called
     * unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntityCorruptedSpider entity) {
        return SPIDER_TEXTURES;
    }
}