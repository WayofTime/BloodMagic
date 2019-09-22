package WayofTime.bloodmagic.client.render;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.item.armour.ItemLivingArmour;
import WayofTime.bloodmagic.livingArmour.LivingArmour;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.model.ModelElytra;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.entity.layers.ArmorLayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class LayerBloodElytra implements LayerRenderer<AbstractClientPlayerEntity> {

    private static final ResourceLocation TEXTURE_BLOOD_ELYTRA = new ResourceLocation("bloodmagic", "textures/entities/bloodElytra.png");
    private final PlayerRenderer renderPlayer;
    private final ModelElytra modelElytra = new ModelElytra();

    public LayerBloodElytra(PlayerRenderer renderPlayer) {
        this.renderPlayer = renderPlayer;
    }

    @Override
    public void doRenderLayer(AbstractClientPlayerEntity clientPlayer, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (LivingArmour.hasFullSet(clientPlayer)) {
            ItemStack chestStack = clientPlayer.getItemStackFromSlot(EquipmentSlotType.CHEST);
            if (ItemLivingArmour.hasUpgrade(BloodMagic.MODID + ".upgrade.elytra", chestStack)) {
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                GlStateManager.enableBlend();

                renderPlayer.bindTexture(TEXTURE_BLOOD_ELYTRA);

                GlStateManager.pushMatrix();
                GlStateManager.translate(0.0F, 0.0F, 0.125F);
                modelElytra.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, clientPlayer);
                modelElytra.render(clientPlayer, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);

                if (chestStack.isItemEnchanted())
                    ArmorLayer.renderEnchantedGlint(this.renderPlayer, clientPlayer, this.modelElytra, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);

                GlStateManager.popMatrix();
            }
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
