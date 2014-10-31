package WayofTime.alchemicalWizardry.common.renderer.block;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import WayofTime.alchemicalWizardry.api.rituals.Rituals;
import WayofTime.alchemicalWizardry.common.renderer.MRSRenderer;
import WayofTime.alchemicalWizardry.common.tileEntity.TEMasterStone;

public class RenderMasterStone extends TileEntitySpecialRenderer
{
    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double d0, double d1, double d2, float f)
    {
        if (tileEntity instanceof TEMasterStone)
        {
        	String str = ((TEMasterStone) tileEntity).getCurrentRitual();
        	MRSRenderer renderer = Rituals.getRendererForKey(str);
        	
        	if(renderer != null)
        	{
        		renderer.renderAt(((TEMasterStone) tileEntity), d0, d1, d2);
        	}
        }
    }
}