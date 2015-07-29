package WayofTime.alchemicalWizardry.common.renderer.block;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.FMLClientHandler;

import org.lwjgl.opengl.GL11;

import WayofTime.alchemicalWizardry.common.renderer.model.ModelSpellModifierBlock;
import WayofTime.alchemicalWizardry.common.tileEntity.TESpellModifierBlock;

public class RenderSpellModifierBlock extends TileEntitySpecialRenderer
{
    private ModelSpellModifierBlock modelSpellModifierBlock = new ModelSpellModifierBlock();

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double d0, double d1, double d2, float f, int i)
    {
        if (tileEntity instanceof TESpellModifierBlock)
        {
            TESpellModifierBlock tileSpellBlock = (TESpellModifierBlock) tileEntity;
            GL11.glPushMatrix();
            GL11.glTranslatef((float) d0 + 0.5F, (float) d1 + 1.5F, (float) d2 + 0.5F);
            ResourceLocation test;
            int meta = tileEntity.getBlockMetadata();
            String resource = tileSpellBlock.getResourceLocationForMeta(meta);
            test = new ResourceLocation(resource);

            FMLClientHandler.instance().getClient().renderEngine.bindTexture(test);
            GL11.glPushMatrix();
            GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
            this.modelSpellModifierBlock.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, tileSpellBlock.getInputDirection(), tileSpellBlock.getOutputDirection());
            GL11.glPopMatrix();
            GL11.glPopMatrix();
        }
    }
}