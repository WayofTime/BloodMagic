package WayofTime.bloodmagic.client.render;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import WayofTime.bloodmagic.api.alchemyCrafting.AlchemyCircleRenderer;
import WayofTime.bloodmagic.tile.TileAlchemyArray;

public class RenderAlchemyArray extends TileEntitySpecialRenderer
{
    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float partialTicks, int destroyStage)
    {
        if (tileEntity instanceof TileAlchemyArray)
        {
        	AlchemyCircleRenderer renderer = new AlchemyCircleRenderer(new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/SightSigil.png")); //Temporary renderer for testing
        	renderer.renderAt(tileEntity, x, y, z);
        }
    }
}
