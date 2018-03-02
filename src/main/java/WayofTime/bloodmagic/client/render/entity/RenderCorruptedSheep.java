package WayofTime.bloodmagic.client.render.entity;

import WayofTime.bloodmagic.client.render.entity.layer.LayerAlchemyCircle;
import WayofTime.bloodmagic.client.render.entity.layer.LayerCorruptedSheepWool;
import WayofTime.bloodmagic.client.render.entity.layer.LayerWill;
import WayofTime.bloodmagic.client.render.model.ModelCorruptedSheep;
import WayofTime.bloodmagic.client.render.model.ModelCorruptedSheep2;
import WayofTime.bloodmagic.entity.mob.EntityCorruptedSheep;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderCorruptedSheep extends RenderLiving<EntityCorruptedSheep> {
    private static final ResourceLocation SHEARED_SHEEP_TEXTURES = new ResourceLocation("textures/entity/sheep/sheep.png");

    public RenderCorruptedSheep(RenderManager renderManagerIn) {
        super(renderManagerIn, new ModelCorruptedSheep2(0), 0.7F);
        this.addLayer(new LayerCorruptedSheepWool(this));
        this.addLayer(new LayerWill<>(this, new ModelCorruptedSheep(1.1f)));
        this.addLayer(new LayerWill<>(this, new ModelCorruptedSheep2(1.1f)));
        this.addLayer(new LayerAlchemyCircle<>());
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityCorruptedSheep entity) {
        return SHEARED_SHEEP_TEXTURES;
    }
}