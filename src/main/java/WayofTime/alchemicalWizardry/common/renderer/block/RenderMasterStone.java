package WayofTime.alchemicalWizardry.common.renderer.block;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import WayofTime.alchemicalWizardry.api.renderer.MRSRenderer;
import WayofTime.alchemicalWizardry.api.rituals.Rituals;
import WayofTime.alchemicalWizardry.common.tileEntity.TEMasterStone;

public class RenderMasterStone extends TileEntitySpecialRenderer
{
    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double d0, double d1, double d2, float f, int i)
    {
        if (tileEntity instanceof TEMasterStone)
        {
            String str = ((TEMasterStone) tileEntity).getCurrentRitual();
            MRSRenderer renderer = Rituals.getRendererForKey(str);

            if (renderer != null)
            {
                renderer.renderAt(((TEMasterStone) tileEntity), d0, d1, d2);
            }
        }
    }
}