package WayofTime.bloodmagic.client.render;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.item.armour.ItemLivingArmour;
import WayofTime.bloodmagic.livingArmour.LivingArmour;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelElytra;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class LayerBloodElytra implements LayerRenderer<AbstractClientPlayer> {

    private static final ResourceLocation TEXTURE_BLOOD_ELYTRA = new ResourceLocation("bloodmagic", "textures/entities/bloodElytra.png");
    private final RenderPlayer renderPlayer;
    private final ModelElytra modelElytra = new ModelElytra();

    public LayerBloodElytra(RenderPlayer renderPlayer) {
        this.renderPlayer = renderPlayer;
    }

    @Override
    public void doRenderLayer(AbstractClientPlayer clientPlayer, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (LivingArmour.hasFullSet(clientPlayer)) {
            ItemStack chestStack = clientPlayer.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
            if (ItemLivingArmour.hasUpgrade(BloodMagic.MODID + ".upgrade.elytra", chestStack)) {
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                GlStateManager.enableBlend();

                renderPlayer.bindTexture(TEXTURE_BLOOD_ELYTRA);

                GlStateManager.pushMatrix();
                GlStateManager.translate(0.0F, 0.0F, 0.125F);
                modelElytra.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, clientPlayer);
                modelElytra.render(clientPlayer, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);

                if (chestStack.isItemEnchanted())
                    LayerArmorBase.renderEnchantedGlint(this.renderPlayer, clientPlayer, this.modelElytra, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);

                GlStateManager.popMatrix();
            }
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
