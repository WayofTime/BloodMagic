package WayofTime.alchemicalWizardry.client.renderer;

import java.util.Random;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemCloth;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ForgeHooksClient;

@SideOnly(Side.CLIENT) public class BMRenderItem extends RenderItem {
	private static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");
	private RenderBlocks renderBlocksRi = new RenderBlocks();
	private Random random = new Random();
	
	public BMRenderItem() {
		this.shadowSize = 0.15F;
		this.shadowOpaque = 0.75F;
	}
	
	@Override public void doRender(EntityItem entity, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
		ItemStack itemstack = entity.getEntityItem();
		
		if (itemstack.getItem() != null) {
			this.bindEntityTexture(entity);
			TextureUtil.func_152777_a(false, false, 1.0F);
			this.random.setSeed(187L);
			GL11.glPushMatrix();
			float f2 = shouldBob() ? MathHelper.sin(((float) entity.age + p_76986_9_) / 10.0F + entity.hoverStart) * 0.1F + 0.1F : 0F;
			float f3 = (((float) entity.age + p_76986_9_) / 20.0F + entity.hoverStart) * (180F / (float) Math.PI);
			byte b0 = 1;
			
			if (entity.getEntityItem().stackSize > 1) {
				b0 = 2;
			}
			
			if (entity.getEntityItem().stackSize > 5) {
				b0 = 3;
			}
			
			if (entity.getEntityItem().stackSize > 20) {
				b0 = 4;
			}
			
			if (entity.getEntityItem().stackSize > 40) {
				b0 = 5;
			}
			
			b0 = getMiniBlockCount(itemstack, b0);
			
			GL11.glTranslatef((float) p_76986_2_, (float) p_76986_4_ + f2, (float) p_76986_6_);
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			float f6;
			float f7;
			int k;
			
			if (ForgeHooksClient.renderEntityItem(entity, itemstack, f2, f3, random, renderManager.renderEngine, field_147909_c, b0)) {
				;
			} else // Code Style break here to prevent the patch from editing
					// this line
			if (itemstack.getItemSpriteNumber() == 0 && itemstack.getItem() instanceof ItemBlock && RenderBlocks.renderItemIn3d(Block.getBlockFromItem(itemstack.getItem()).getRenderType())) {
				Block block = Block.getBlockFromItem(itemstack.getItem());
				GL11.glRotatef(f3, 0.0F, 1.0F, 0.0F);
				
				if (renderInFrame) {
					GL11.glScalef(1.25F, 1.25F, 1.25F);
					GL11.glTranslatef(0.0F, 0.05F, 0.0F);
					GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
				}
				
				float f9 = 0.25F;
				k = block.getRenderType();
				
				if (k == 1 || k == 19 || k == 12 || k == 2) {
					f9 = 0.5F;
				}
				
				if (block.getRenderBlockPass() > 0) {
					GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
					GL11.glEnable(GL11.GL_BLEND);
					OpenGlHelper.glBlendFunc(770, 771, 1, 0);
				}
				
				GL11.glScalef(f9, f9, f9);
				
				for (int l = 0; l < b0; ++ l) {
					GL11.glPushMatrix();
					
					if (l > 0) {
						f6 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.2F / f9;
						f7 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.2F / f9;
						float f8 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.2F / f9;
						GL11.glTranslatef(f6, f7, f8);
					}
					
					this.renderBlocksRi.renderBlockAsItem(block, itemstack.getItemDamage(), 1.0F);
					GL11.glPopMatrix();
				}
				
				if (block.getRenderBlockPass() > 0) {
					GL11.glDisable(GL11.GL_BLEND);
				}
			} else {
				float f5;
				
				if (/* itemstack.getItemSpriteNumber() == 1 && */itemstack.getItem().requiresMultipleRenderPasses()) {
					if (renderInFrame) {
						GL11.glScalef(0.5128205F, 0.5128205F, 0.5128205F);
						GL11.glTranslatef(0.0F, -0.05F, 0.0F);
					} else {
						GL11.glScalef(0.5F, 0.5F, 0.5F);
					}
					
					for (int j = 0; j < itemstack.getItem().getRenderPasses(itemstack.getItemDamage()); ++ j) {
						this.random.setSeed(187L);
						IIcon iicon1 = itemstack.getItem().getIcon(itemstack, j);
						
						if (this.renderWithColor) {
							k = itemstack.getItem().getColorFromItemStack(itemstack, j);
							f5 = (float) (k >> 16 & 255) / 255.0F;
							f6 = (float) (k >> 8 & 255) / 255.0F;
							f7 = (float) (k & 255) / 255.0F;
							GL11.glColor4f(f5, f6, f7, 1.0F);
							this.BMrenderDroppedItem(entity, iicon1, b0, p_76986_9_, f5, f6, f7, j);
						} else {
							this.BMrenderDroppedItem(entity, iicon1, b0, p_76986_9_, 1.0F, 1.0F, 1.0F, j);
						}
					}
				} else {
					if (itemstack != null && itemstack.getItem() instanceof ItemCloth) {
						GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
						GL11.glEnable(GL11.GL_BLEND);
						OpenGlHelper.glBlendFunc(770, 771, 1, 0);
					}
					
					if (renderInFrame) {
						GL11.glScalef(0.5128205F, 0.5128205F, 0.5128205F);
						GL11.glTranslatef(0.0F, -0.05F, 0.0F);
					} else {
						GL11.glScalef(0.5F, 0.5F, 0.5F);
					}
					
					IIcon iicon = itemstack.getIconIndex();
					
					if (this.renderWithColor) {
						int i = itemstack.getItem().getColorFromItemStack(itemstack, 0);
						float f4 = (float) (i >> 16 & 255) / 255.0F;
						f5 = (float) (i >> 8 & 255) / 255.0F;
						f6 = (float) (i & 255) / 255.0F;
						this.BMrenderDroppedItem(entity, iicon, b0, p_76986_9_, f4, f5, f6);
					} else {
						this.BMrenderDroppedItem(entity, iicon, b0, p_76986_9_, 1.0F, 1.0F, 1.0F);
					}
					
					if (itemstack != null && itemstack.getItem() instanceof ItemCloth) {
						GL11.glDisable(GL11.GL_BLEND);
					}
				}
			}
			
			GL11.glDisable(GL12.GL_RESCALE_NORMAL);
			GL11.glPopMatrix();
			this.bindEntityTexture(entity);
			TextureUtil.func_147945_b();
		}
	}
	
	private void BMrenderDroppedItem(EntityItem entity, IIcon icon, int param3, float param4, float param5, float param6, float param7) {
		BMrenderDroppedItem(entity, icon, param3, param4, param5, param6, param7, 0);
	}
	
	private void BMrenderDroppedItem(EntityItem entity, IIcon icon, int p_77020_3_, float p_77020_4_, float p_77020_5_, float p_77020_6_, float p_77020_7_, int pass) {
		Tessellator tessellator = Tessellator.instance;
		
		if (icon == null) {
			TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
			ResourceLocation resourcelocation = texturemanager.getResourceLocation(entity.getEntityItem().getItemSpriteNumber());
			icon = ((TextureMap) texturemanager.getTexture(resourcelocation)).getAtlasSprite("missingno");
		}
		
		float f14 = ((IIcon) icon).getMinU();
		float f15 = ((IIcon) icon).getMaxU();
		float f4 = ((IIcon) icon).getMinV();
		float f5 = ((IIcon) icon).getMaxV();
		float f7 = 0.5F;
		float f8 = 0.25F;
		float f10;
		
		GL11.glPushMatrix();
		
		if (renderInFrame) {
			GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
		} else {
			GL11.glRotatef((((float) entity.age + p_77020_4_) / 20.0F + entity.hoverStart) * (180F / (float) Math.PI), 0.0F, 1.0F, 0.0F);
		}
		
		float f9 = 0.0625F;
		f10 = 0.021875F;
		ItemStack itemstack = entity.getEntityItem();
		int j = itemstack.stackSize;
		byte b0;
		
		if (j < 2) {
			b0 = 1;
		} else if (j < 16) {
			b0 = 2;
		} else if (j < 32) {
			b0 = 3;
		} else {
			b0 = 4;
		}
		
		b0 = getMiniItemCount(itemstack, b0);
		
		GL11.glTranslatef(-f7, -f8, -((f9 + f10) * (float) b0 / 2.0F));
		
		for (int k = 0; k < b0; ++ k) {
			// Makes items offset when in 3D, like when in 2D, looks much
			// better. Considered a vanilla bug...
			if (k > 0 && shouldSpreadItems()) {
				float x = (random.nextFloat() * 2.0F - 1.0F) * 0.3F / 0.5F;
				float y = (random.nextFloat() * 2.0F - 1.0F) * 0.3F / 0.5F;
				GL11.glTranslatef(x, y, f9 + f10);
			} else {
				GL11.glTranslatef(0f, 0f, f9 + f10);
			}
			
			if (itemstack.getItemSpriteNumber() == 0) {
				this.bindTexture(TextureMap.locationBlocksTexture);
			} else {
				this.bindTexture(TextureMap.locationItemsTexture);
			}
			
			GL11.glColor4f(p_77020_5_, p_77020_6_, p_77020_7_, 1.0F);
			ItemRenderer.renderItemIn2D(tessellator, f15, f4, f14, f5, ((IIcon) icon).getIconWidth(), ((IIcon) icon).getIconHeight(), f9);
			
			if (itemstack.hasEffect(pass)) {
				GL11.glDepthFunc(GL11.GL_EQUAL);
				GL11.glDisable(GL11.GL_LIGHTING);
				this.renderManager.renderEngine.bindTexture(RES_ITEM_GLINT);
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);
				float f11 = 0.76F;
				GL11.glColor4f(0.5F * f11, 0.25F * f11, 0.8F * f11, 1.0F);
				GL11.glMatrixMode(GL11.GL_TEXTURE);
				GL11.glPushMatrix();
				float f12 = 0.125F;
				GL11.glScalef(f12, f12, f12);
				float f13 = (float) (Minecraft.getSystemTime() % 3000L) / 3000.0F * 8.0F;
				GL11.glTranslatef(f13, 0.0F, 0.0F);
				GL11.glRotatef(-50.0F, 0.0F, 0.0F, 1.0F);
				ItemRenderer.renderItemIn2D(tessellator, 0.0F, 0.0F, 1.0F, 1.0F, 255, 255, f9);
				GL11.glPopMatrix();
				GL11.glPushMatrix();
				GL11.glScalef(f12, f12, f12);
				f13 = (float) (Minecraft.getSystemTime() % 4873L) / 4873.0F * 8.0F;
				GL11.glTranslatef(-f13, 0.0F, 0.0F);
				GL11.glRotatef(10.0F, 0.0F, 0.0F, 1.0F);
				ItemRenderer.renderItemIn2D(tessellator, 0.0F, 0.0F, 1.0F, 1.0F, 255, 255, f9);
				GL11.glPopMatrix();
				GL11.glMatrixMode(GL11.GL_MODELVIEW);
				GL11.glDisable(GL11.GL_BLEND);
				GL11.glEnable(GL11.GL_LIGHTING);
				GL11.glDepthFunc(GL11.GL_LEQUAL);
			}
		}
		
		GL11.glPopMatrix();
	}
}