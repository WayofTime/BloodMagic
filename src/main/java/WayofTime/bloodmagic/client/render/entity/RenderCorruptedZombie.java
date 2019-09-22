package WayofTime.bloodmagic.client.render.entity;

import WayofTime.bloodmagic.client.render.entity.layer.LayerWill;
import WayofTime.bloodmagic.entity.mob.EntityCorruptedZombie;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.model.ModelZombieVillager;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.layers.*;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderCorruptedZombie extends BipedRenderer<EntityCorruptedZombie> {
    private static final ResourceLocation ZOMBIE_TEXTURES = new ResourceLocation("textures/entity/zombie/zombie.png");
    private final ModelZombieVillager zombieVillagerModel;

    public RenderCorruptedZombie(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new ModelZombie(), 0.5F);
        LayerRenderer<?> layerrenderer = this.layerRenderers.get(0);
        this.zombieVillagerModel = new ModelZombieVillager();
        this.addLayer(new HeldItemLayer(this));
        BipedArmorLayer layerbipedarmor = new BipedArmorLayer(this) {
            protected void initArmor() {
                this.modelLeggings = new ModelZombie(0.5F, true);
                this.modelArmor = new ModelZombie(1.0F, true);
            }
        };
        this.addLayer(layerbipedarmor);

        if (layerrenderer instanceof HeadLayer) {
            layerRenderers.remove(layerrenderer);
            this.addLayer(new HeadLayer(this.zombieVillagerModel.bipedHead));
        }

        this.layerRenderers.remove(layerbipedarmor);
        this.addLayer(new LayerWill<>(this, new ModelZombie(1.2f, false)));
    }

    /**
     * Allows the render to do state modifications necessary before the model is
     * rendered.
     */
    protected void preRenderCallback(EntityCorruptedZombie entitylivingbaseIn, float partialTickTime) {
        super.preRenderCallback(entitylivingbaseIn, partialTickTime);
    }

    /**
     * Renders the desired {@code T} type Entity.
     */
    public void doRender(EntityCorruptedZombie entity, double x, double y, double z, float entityYaw, float partialTicks) {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called
     * unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntityCorruptedZombie entity) {
        return ZOMBIE_TEXTURES;
    }
}