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

import WayofTime.alchemicalWizardry.common.renderer.model.ModelWritingTable;
import WayofTime.alchemicalWizardry.common.tileEntity.TEWritingTable;
import cpw.mods.fml.client.FMLClientHandler;

public class RenderWritingTable extends TileEntitySpecialRenderer
{
	private ModelWritingTable modelWritingTable = new ModelWritingTable();
	private final RenderItem customRenderItem;
	//	private final RenderItem customRenderItem1;
	//	private final RenderItem customRenderItem2;
	//	private final RenderItem customRenderItem3;
	//	private final RenderItem customRenderItem4;
	//	private final RenderItem customRenderItem5;

	public RenderWritingTable()
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
		if (tileEntity instanceof TEWritingTable)
		{
			TEWritingTable tileAltar = (TEWritingTable) tileEntity;
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glDisable(GL11.GL_CULL_FACE);
			GL11.glPushMatrix();
			GL11.glTranslatef((float) d0 + 0.5F, (float) d1 + 1.5F, (float) d2 + 0.5F);
			ResourceLocation test = new ResourceLocation("alchemicalwizardry:textures/models/WritingTable.png");
			FMLClientHandler.instance().getClient().renderEngine.bindTexture(test);
			GL11.glPushMatrix();
			GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
			//A reference to your Model file. Again, very important.
			modelWritingTable.render((Entity)null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
			//Tell it to stop rendering for both the PushMatrix's
			GL11.glPopMatrix();
			GL11.glPopMatrix();

			for (int i = 1; i <= 6; i++)
			{
				GL11.glPushMatrix();

				if (tileAltar.getStackInSlot(i) != null)
				{
					float scaleFactor = getGhostItemScaleFactor(tileAltar.getStackInSlot(i));
					float rotationAngle = (float)(720.0 * (System.currentTimeMillis() & 0x3FFFL) / 0x3FFFL);
					EntityItem ghostEntityItem = new EntityItem(tileAltar.worldObj);
					ghostEntityItem.hoverStart = 0.0F;
					ghostEntityItem.setEntityItemStack(tileAltar.getStackInSlot(i));
					//translateGhostItemByOrientation(ghostEntityItem.getEntityItem(), d0, d1, d2, ForgeDirection.DOWN);
					float displacementX = getXDisplacementForSlot(i);
					float displacementY = getYDisplacementForSlot(i);
					float displacementZ = getZDisplacementForSlot(i);

					if (ghostEntityItem.getEntityItem().getItem() instanceof ItemBlock)
					{
						GL11.glTranslatef((float) d0 + 0.5F + displacementX, (float) d1 + displacementY + 0.7F, (float) d2 + 0.5F + displacementZ);
					}
					else
					{
						GL11.glTranslatef((float) d0 + 0.5F + displacementX, (float) d1 + displacementY + 0.6F, (float) d2 + 0.5F + displacementZ);
					}

					//GL11.glTranslatef((float) tileAltar.xCoord + 0.5F, (float) tileAltar.yCoord + 2.7F, (float) tileAltar.zCoord + 0.5F);
					GL11.glScalef(scaleFactor, scaleFactor, scaleFactor);
					GL11.glRotatef(rotationAngle, 0.0F, 1.0F, 0.0F);
					customRenderItem.doRenderItem(ghostEntityItem, 0, 0, 0, 0, 0);
				}

				GL11.glPopMatrix();
			}

			GL11.glEnable(GL11.GL_CULL_FACE);
			GL11.glEnable(GL11.GL_LIGHTING);
		}
	}

	private float getGhostItemScaleFactor(ItemStack itemStack)
	{
		float scaleFactor = 0.8F;

		if (itemStack != null)
		{
			if (itemStack.getItem() instanceof ItemBlock)
			{
				switch (customRenderItem.getMiniBlockCount(itemStack))
				{
				case 1:
					return 0.90F * scaleFactor;

				case 2:
					return 0.90F * scaleFactor;

				case 3:
					return 0.90F * scaleFactor;

				case 4:
					return 0.90F * scaleFactor;

				case 5:
					return 0.80F * scaleFactor;

				default:
					return 0.90F * scaleFactor;
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

	private float getXDisplacementForSlot(int slot)
	{
		switch (slot)
		{
		case 0:
			return 0.0f;

		case 1:
			return -0.375f;

		case 2:
			return -0.125f;

		case 3:
			return 0.3125f;

		case 4:
			return 0.3125f;

		case 5:
			return -0.125f;

		default:
			return 0.0f;
		}
	}

	private float getYDisplacementForSlot(int slot)
	{
		switch (slot)
		{
		case 0:
			return 0.4f;

		case 1:
			return -0.35f;

		case 6:
			return 0.4f;

		default:
			return -0.35f;
		}
	}

	private float getZDisplacementForSlot(int slot)
	{
		switch (slot)
		{
		case 0:
			return 0.0f;

		case 1:
			return 0.0f;

		case 2:
			return 0.375f;

		case 3:
			return 0.25f;

		case 4:
			return -0.25f;

		case 5:
			return -0.375f;

		default:
			return 0.0f;
		}
	}
}