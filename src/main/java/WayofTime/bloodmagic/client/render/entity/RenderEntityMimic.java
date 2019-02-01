package WayofTime.bloodmagic.client.render.entity;

import WayofTime.bloodmagic.client.render.model.ModelMimic;
import WayofTime.bloodmagic.entity.mob.EntityMimic;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySkullRenderer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderEntityMimic extends RenderLiving<EntityMimic> {
    private static final ResourceLocation SPIDER_TEXTURES = new ResourceLocation("textures/entity/spider/spider.png");
    Minecraft minecraft = Minecraft.getMinecraft();

    public RenderEntityMimic(RenderManager renderManagerIn) {
        super(renderManagerIn, new ModelMimic(), 1.0F);
    }

    @Override
    public void doRender(EntityMimic mimic, double x, double y, double z, float entityYaw, float partialTicks) {
        super.doRender(mimic, x, y, z, entityYaw, partialTicks);

        GlStateManager.pushMatrix();
        if (mimic.getMimicItemStack() != null) {
            GlStateManager.pushMatrix();

            if (this.renderOutlines) {
                GlStateManager.enableColorMaterial();
                GlStateManager.enableOutlineMode(this.getTeamColor(mimic));
            }

            GlStateManager.translate(x, y, z);

            ItemStack itemstack = mimic.getMimicItemStack();
            Item item = itemstack.getItem();
            Minecraft minecraft = Minecraft.getMinecraft();
            GlStateManager.pushMatrix();

            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            if (item == Items.SKULL) {
                float f2 = 1.1875F;
                GlStateManager.scale(1.1875F, -1.1875F, -1.1875F);

                GameProfile gameprofile = null;

                if (itemstack.hasTagCompound()) {
                    NBTTagCompound nbttagcompound = itemstack.getTagCompound();

                    if (nbttagcompound.hasKey("SkullOwner", 10)) {
                        gameprofile = NBTUtil.readGameProfileFromNBT(nbttagcompound.getCompoundTag("SkullOwner"));
                    } else if (nbttagcompound.hasKey("SkullOwner", 8)) {
                        String s = nbttagcompound.getString("SkullOwner");

                        if (!StringUtils.isNullOrEmpty(s)) {
                            gameprofile = TileEntitySkull.updateGameProfile(new GameProfile(null, s));
                            nbttagcompound.setTag("SkullOwner", NBTUtil.writeGameProfile(new NBTTagCompound(), gameprofile));
                        }
                    }
                }

                TileEntitySkullRenderer.instance.renderSkull(-0.5F, 0.0F, -0.5F, EnumFacing.UP, 180.0F, itemstack.getMetadata(), gameprofile, -1, 0);
            } else if (!(item instanceof ItemArmor) || ((ItemArmor) item).getEquipmentSlot() != EntityEquipmentSlot.HEAD) {
                GlStateManager.translate(0, 0.5f, 0);
                GlStateManager.rotate(-(mimic.prevRotationYawHead + partialTicks * (mimic.rotationYawHead - mimic.prevRotationYawHead)) - 180, 0, 1, 0);

                minecraft.getItemRenderer().renderItem(mimic, itemstack, ItemCameraTransforms.TransformType.HEAD);
            }

            GlStateManager.popMatrix();

            if (this.renderOutlines) {
                GlStateManager.disableOutlineMode();
                GlStateManager.disableColorMaterial();
            }

            GlStateManager.popMatrix();
            super.doRender(mimic, x, y, z, entityYaw, partialTicks);

        }
        GlStateManager.popMatrix();

        if (!this.renderOutlines) {
            this.renderLeash(mimic, x, y, z, entityYaw, partialTicks);
        }
    }

    @Override
    protected float getDeathMaxRotation(EntityMimic mimic) {
        return 180.0F;
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called
     * unless you call Render.bindEntityTexture.
     */
    @Override
    protected ResourceLocation getEntityTexture(EntityMimic mimic) {
        return SPIDER_TEXTURES;
    }
}