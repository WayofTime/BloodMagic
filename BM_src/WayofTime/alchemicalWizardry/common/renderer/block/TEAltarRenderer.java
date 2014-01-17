package WayofTime.alchemicalWizardry.common.renderer.block;

import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

import WayofTime.alchemicalWizardry.common.renderer.model.ModelBloodAltar;
import WayofTime.alchemicalWizardry.common.tileEntity.TEAltar;

public class TEAltarRenderer extends TileEntitySpecialRenderer
{
	private ModelBloodAltar modelBloodAltar = new ModelBloodAltar();
	private final RenderItem customRenderItem;

	public TEAltarRenderer()
	{
		customRenderItem = new RenderItem()
		{
			@Override
			public boolean shouldBob()
			{
				return false;
			}
		};
		customRenderItem.setRenderManager(RenderManager.instance);
	}

	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double d0, double d1, double d2, float f)
	{
		modelBloodAltar.renderBloodAltar((TEAltar)tileEntity, d0, d1, d2);
		modelBloodAltar.renderBloodLevel((TEAltar)tileEntity, d0, d1, d2);

		if (tileEntity instanceof TEAltar)
		{
			TEAltar tileAltar = (TEAltar) tileEntity;
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glDisable(GL11.GL_CULL_FACE);
			/**
			 * Render the ghost item inside of the Altar, slowly spinning
			 */
			 GL11.glPushMatrix();

			if (tileAltar.getStackInSlot(0) != null)
			{
				float scaleFactor = getGhostItemScaleFactor(tileAltar.getStackInSlot(0));
				float rotationAngle = (float)(720.0 * (System.currentTimeMillis() & 0x3FFFL) / 0x3FFFL);
				EntityItem ghostEntityItem = new EntityItem(tileAltar.worldObj);
				ghostEntityItem.hoverStart = 0.0F;
				ghostEntityItem.setEntityItemStack(tileAltar.getStackInSlot(0));
				//translateGhostItemByOrientation(ghostEntityItem.getEntityItem(), d0, d1, d2, ForgeDirection.DOWN);
				float displacement = 0.2F;

				if (ghostEntityItem.getEntityItem().getItem() instanceof ItemBlock)
				{
					GL11.glTranslatef((float) d0 + 0.5F, (float) d1 + displacement + 0.7F, (float) d2 + 0.5F);
				}
				else
				{
					GL11.glTranslatef((float) d0 + 0.5F, (float) d1 + displacement + 0.6F, (float) d2 + 0.5F);
				}

				//GL11.glTranslatef((float) tileAltar.xCoord + 0.5F, (float) tileAltar.yCoord + 2.7F, (float) tileAltar.zCoord + 0.5F);
				GL11.glScalef(scaleFactor, scaleFactor, scaleFactor);
				GL11.glRotatef(rotationAngle, 0.0F, 1.0F, 0.0F);
				customRenderItem.doRenderItem(ghostEntityItem, 0, 0, 0, 0, 0);
			}

			GL11.glPopMatrix();
			GL11.glEnable(GL11.GL_CULL_FACE);
			GL11.glEnable(GL11.GL_LIGHTING);
		}
	}

	private float getGhostItemScaleFactor(ItemStack itemStack)
	{
		float scaleFactor = 1.0F;

		if (itemStack != null)
		{
			if (itemStack.getItem() instanceof ItemBlock)
			{
				switch (customRenderItem.getMiniBlockCount(itemStack))
				{
				case 1:
					return 0.90F;

				case 2:
					return 0.90F;

				case 3:
					return 0.90F;

				case 4:
					return 0.90F;

				case 5:
					return 0.80F;

				default:
					return 0.90F;
				}
			}
			else
			{
				switch (customRenderItem.getMiniItemCount(itemStack))
				{
				case 1:
					return 0.65F;

				case 2:
					return 0.65F;

				case 3:
					return 0.65F;

				case 4:
					return 0.65F;

				default:
					return 0.65F;
				}
			}
		}

		return scaleFactor;
	}
}
