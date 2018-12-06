package WayofTime.bloodmagic.client.hud.element;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.proxy.ClientProxy;
import WayofTime.bloodmagic.soul.EnumDemonWillType;
import WayofTime.bloodmagic.util.Utils;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public class ElementDemonAura extends HUDElement {

    private static final ResourceLocation BAR_LOCATION = new ResourceLocation(BloodMagic.MODID, "textures/hud/bars.png");

    private final List<EnumDemonWillType> orderedTypes = Lists.newArrayList(
            EnumDemonWillType.DEFAULT,
            EnumDemonWillType.CORROSIVE,
            EnumDemonWillType.STEADFAST,
            EnumDemonWillType.DESTRUCTIVE,
            EnumDemonWillType.VENGEFUL
    );

    public ElementDemonAura() {
        super(80, 46);
    }

    @Override
    public void draw(ScaledResolution resolution, float partialTicks, int drawX, int drawY) {
        Minecraft minecraft = Minecraft.getMinecraft();
        EntityPlayer player = minecraft.player;

        minecraft.getTextureManager().bindTexture(BAR_LOCATION);
        GlStateManager.color(1.0F, 1.0F, 1.0F);
        this.drawTexturedModalRect(drawX, drawY, 0, 210, 80, 46);

        double maxAmount = Utils.getDemonWillResolution(player);

        int i = 0;
        for (EnumDemonWillType type : orderedTypes) {
            i++;
            GlStateManager.color(1.0F, 1.0F, 1.0F);
            minecraft.getTextureManager().bindTexture(BAR_LOCATION);
            int textureXOffset = (i > 3) ? (i - 3) : (3 - i);
            int maxBarSize = 30 - 2 * textureXOffset;

            double amount = ClientProxy.currentAura == null ? 0 : ClientProxy.currentAura.getWill(type);
            double ratio = Math.max(Math.min(amount / maxAmount, 1), 0);

            double width = maxBarSize * ratio * 2;
            double height = 2;
            double x = drawX + 2 * textureXOffset + 10;
            double y = drawY + 4 * i + 10;

            double textureX = 2 * textureXOffset + 2 * 42;
            double textureY = 4 * i + 220;

            this.drawTexturedModalRect(x, y, textureX, textureY, width, height);

            if (player.isSneaking()) {
                GlStateManager.pushMatrix();
                GlStateManager.translate(x - 2 * textureXOffset + 70, (y - 1), 0);
                GlStateManager.scale(0.5, 0.5, 1);
                minecraft.fontRenderer.drawStringWithShadow(String.valueOf((int) amount), 0, 2, 0xffffff);
                GlStateManager.popMatrix();
            }
        }
    }

    @Override
    public boolean shouldRender(Minecraft minecraft) {
        return Utils.canPlayerSeeDemonWill(Minecraft.getMinecraft().player);
    }
}
