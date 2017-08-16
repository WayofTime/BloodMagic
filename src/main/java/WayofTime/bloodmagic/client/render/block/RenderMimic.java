package WayofTime.bloodmagic.client.render.block;

import WayofTime.bloodmagic.tile.TileMimic;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderMimic extends TileEntitySpecialRenderer<TileMimic> {
    public void render(TileMimic mimic, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        if (mimic.getStackInSlot(0) != null) {
            TileEntity testTile = mimic.mimicedTile;
            if (mimic != null) {
                TileEntityRendererDispatcher.instance.render(testTile, x, y, z, partialTicks, destroyStage);
            }
        }
    }
}