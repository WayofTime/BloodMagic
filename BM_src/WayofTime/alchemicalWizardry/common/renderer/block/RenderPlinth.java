package WayofTime.alchemicalWizardry.common.renderer.block;

import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import WayofTime.alchemicalWizardry.common.renderer.model.ModelPlinth;
import WayofTime.alchemicalWizardry.common.tileEntity.TEPlinth;
import cpw.mods.fml.client.FMLClientHandler;

public class RenderPlinth extends TileEntitySpecialRenderer
{
	private ModelPlinth modelPlinth = new ModelPlinth();
	private final RenderItem customRenderItem;

	public RenderPlinth()
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
		if (tileEntity instanceof TEPlinth)
		{
			TEPlinth tileAltar = (TEPlinth) tileEntity;
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glDisable(GL11.GL_CULL_FACE);
			/**
			 * Render the ghost item inside of the Altar, slowly spinning
			 */
			 GL11.glPushMatrix();
			GL11.glTranslatef((float) d0 + 0.5F, (float) d1 + 1.5F, (float) d2 + 0.5F);
			ResourceLocation test = new ResourceLocation("alchemicalwizardry:textures/models/Plinth.png");
			FMLClientHandler.instance().getClient().renderEngine.bindTexture(test);
			GL11.glPushMatrix();
			GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
			//A reference to your Model file. Again, very important.
			modelPlinth.render((Entity)null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
			//Tell it to stop rendering for both the PushMatrix's
			GL11.glPopMatrix();
			GL11.glPopMatrix();
			GL11.glPushMatrix();

			if (tileAltar.getStackInSlot(0) != null)
			{
				float scaleFactor = getGhostItemScaleFactor(tileAltar.getStackInSlot(0));
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
					GL11.glTranslatef((float) d0 + 0.5F, (float) d1 + displacement + 10.4f / 16.0f, (float) d2 + 0.5F - 0.1875f);
				}

				//GL11.glTranslatef((float) tileAltar.xCoord + 0.5F, (float) tileAltar.yCoord + 2.7F, (float) tileAltar.zCoord + 0.5F);
				GL11.glScalef(scaleFactor, scaleFactor, scaleFactor);

				if (!(ghostEntityItem.getEntityItem().getItem() instanceof ItemBlock))
				{
					GL11.glRotatef(90f, 1.0f, 0.0f, 0.0F);
				}

				customRenderItem.doRenderItem(ghostEntityItem, 0, 0, 0, 0, 0);
			}

			GL11.glPopMatrix();
			GL11.glEnable(GL11.GL_CULL_FACE);
			GL11.glEnable(GL11.GL_LIGHTING);
		}
	}

	private float getGhostItemScaleFactor(ItemStack itemStack)
	{
		float scaleFactor = 2.0F / 0.9F;

		if (itemStack != null)
		{
			if (itemStack.getItem() instanceof ItemBlock)
			{
				switch (customRenderItem.getMiniBlockCount(itemStack))
				{
				case 1:
					return 0.90F * scaleFactor / 2;

				case 2:
					return 0.90F * scaleFactor / 2;

				case 3:
					return 0.90F * scaleFactor / 2;

				case 4:
					return 0.90F * scaleFactor / 2;

				case 5:
					return 0.80F * scaleFactor / 2;

				default:
					return 0.90F * scaleFactor / 2;
				}
			}
			else
			{
				switch (customRenderItem.getMiniItemCount(itemStack))
				{
				case 1:
					return 0.65F * scaleFactor;

				case 2:
					return 0.65F * scaleFactor;

				case 3:
					return 0.65F * scaleFactor;

				case 4:
					return 0.65F * scaleFactor;

				default:
					return 0.65F * scaleFactor;
				}
			}
		}

		return scaleFactor;
	}
}
