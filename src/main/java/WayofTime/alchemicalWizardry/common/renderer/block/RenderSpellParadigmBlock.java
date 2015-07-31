package WayofTime.alchemicalWizardry.common.renderer.block;

import WayofTime.alchemicalWizardry.common.renderer.model.ModelSpellParadigmBlock;
import WayofTime.alchemicalWizardry.common.tileEntity.TESpellParadigmBlock;
import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderSpellParadigmBlock extends TileEntitySpecialRenderer
{
    private ModelSpellParadigmBlock modelSpellParadigmBlock = new ModelSpellParadigmBlock();

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double d0, double d1, double d2, float f)
    {
        if (tileEntity instanceof TESpellParadigmBlock)
        {
            TESpellParadigmBlock tileSpellBlock = (TESpellParadigmBlock) tileEntity;
            GL11.glPushMatrix();
            GL11.glTranslatef((float) d0 + 0.5F, (float) d1 + 1.5F, (float) d2 + 0.5F);
            ResourceLocation test;
            int meta = tileEntity.getWorldObj().getBlockMetadata(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
            String resource = tileSpellBlock.getResourceLocationForMeta(meta);
            test = new ResourceLocation(resource);

            FMLClientHandler.instance().getClient().renderEngine.bindTexture(test);
            GL11.glPushMatrix();
            GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
            this.modelSpellParadigmBlock.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, tileSpellBlock.getInputDirection(), tileSpellBlock.getOutputDirection());
            GL11.glPopMatrix();
            GL11.glPopMatrix();
        }
    }
}