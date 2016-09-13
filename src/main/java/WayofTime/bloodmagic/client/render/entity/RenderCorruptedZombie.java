package WayofTime.bloodmagic.client.render.entity;

import java.util.List;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.model.ModelZombieVillager;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import WayofTime.bloodmagic.entity.mob.EntityCorruptedZombie;

import com.google.common.collect.Lists;

@SideOnly(Side.CLIENT)
public class RenderCorruptedZombie extends RenderBiped<EntityCorruptedZombie>
{
    private static final ResourceLocation ZOMBIE_TEXTURES = new ResourceLocation("bloodmagic", "textures/entities/zombie_raw.png");
    private final ModelBiped defaultModel;
    private final ModelZombieVillager zombieVillagerModel;
    private final List<LayerRenderer<EntityCorruptedZombie>> defaultLayers;

    public RenderCorruptedZombie(RenderManager renderManagerIn)
    {
        super(renderManagerIn, new ModelZombie(), 0.5F, 1.0F);
        LayerRenderer<?> layerrenderer = (LayerRenderer) this.layerRenderers.get(0);
        this.defaultModel = this.modelBipedMain;
        this.zombieVillagerModel = new ModelZombieVillager();
        this.addLayer(new LayerHeldItem(this));
        LayerBipedArmor layerbipedarmor = new LayerBipedArmor(this)
        {
            protected void initArmor()
            {
                this.modelLeggings = new ModelZombie(0.5F, true);
                this.modelArmor = new ModelZombie(1.0F, true);
            }
        };
        this.addLayer(layerbipedarmor);
        this.defaultLayers = Lists.newArrayList(this.layerRenderers);

        if (layerrenderer instanceof LayerCustomHead)
        {
            this.removeLayer(layerrenderer);
            this.addLayer(new LayerCustomHead(this.zombieVillagerModel.bipedHead));
        }

        this.removeLayer(layerbipedarmor);
    }

    /**
     * Allows the render to do state modifications necessary before the model is
     * rendered.
     */
    protected void preRenderCallback(EntityCorruptedZombie entitylivingbaseIn, float partialTickTime)
    {
        super.preRenderCallback(entitylivingbaseIn, partialTickTime);
    }

    /**
     * Renders the desired {@code T} type Entity.
     */
    public void doRender(EntityCorruptedZombie entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        this.swapArmor(entity);
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called
     * unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntityCorruptedZombie entity)
    {

        {
            return ZOMBIE_TEXTURES;
        }
    }

    private void swapArmor(EntityCorruptedZombie zombie)
    {

        {
            this.mainModel = this.defaultModel;
            this.layerRenderers = this.defaultLayers;
        }

        this.modelBipedMain = (ModelBiped) this.mainModel;
    }

    protected void rotateCorpse(EntityCorruptedZombie entityLiving, float p_77043_2_, float p_77043_3_, float partialTicks)
    {

        super.rotateCorpse(entityLiving, p_77043_2_, p_77043_3_, partialTicks);
    }
}