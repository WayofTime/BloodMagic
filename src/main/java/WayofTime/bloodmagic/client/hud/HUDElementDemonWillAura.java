package WayofTime.bloodmagic.client.hud;

import java.util.ArrayList;
import java.util.List;

import WayofTime.bloodmagic.BloodMagic;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import WayofTime.bloodmagic.api.soul.EnumDemonWillType;
import WayofTime.bloodmagic.proxy.ClientProxy;
import WayofTime.bloodmagic.util.Utils;

public class HUDElementDemonWillAura extends HUDElement
{
    protected List<EnumDemonWillType> barOrder = new ArrayList<EnumDemonWillType>();

    public HUDElementDemonWillAura()
    {
        super(5, 5, RenderGameOverlayEvent.ElementType.HOTBAR);

        barOrder.add(EnumDemonWillType.DEFAULT);
        barOrder.add(EnumDemonWillType.CORROSIVE);
        barOrder.add(EnumDemonWillType.STEADFAST);
        barOrder.add(EnumDemonWillType.DESTRUCTIVE);
        barOrder.add(EnumDemonWillType.VENGEFUL);
    }

    @Override
    public void render(Minecraft minecraft, ScaledResolution resolution, float partialTicks)
    {
        EntityPlayer player = minecraft.player;

        if (!Utils.canPlayerSeeDemonWill(player))
        {
            return;
        }

        minecraft.getTextureManager().bindTexture(new ResourceLocation(BloodMagic.MODID, "textures/hud/bars.png"));
        GlStateManager.color(1.0F, 1.0F, 1.0F);
        this.drawTexturedModalRect(getXOffset(), getYOffset(), 0, 105 * 2, 80, 46);

        double maxAmount = Utils.getDemonWillResolution(player);

        int i = 0;
        for (EnumDemonWillType type : barOrder)
        {
            i++;
            GlStateManager.color(1.0F, 1.0F, 1.0F);
            minecraft.getTextureManager().bindTexture(new ResourceLocation(BloodMagic.MODID, "textures/hud/bars.png"));
            int textureXOffset = (i > 3) ? (i - 3) : (3 - i);
            int maxBarSize = 30 - 2 * textureXOffset;

            double amount = ClientProxy.currentAura == null ? 0 : ClientProxy.currentAura.getWill(type);
            double ratio = Math.max(Math.min(amount / maxAmount, 1), 0);

            double width = maxBarSize * ratio * 2;
            double height = 2;
            double x = getXOffset() + 2 * textureXOffset + 10;
            double y = getYOffset() + 4 * i + 10;

            double textureX = 2 * textureXOffset + 2 * 42;
            double textureY = 4 * i + 220;

            this.drawTexturedModalRect(x, y, textureX, textureY, width, height);

            if (player.isSneaking())
            {
                GlStateManager.pushMatrix();
                String value = "" + (int) amount;
                GlStateManager.translate(x - 2 * textureXOffset - value.length() * 0 + 70, (y - 1), 0);
                GlStateManager.scale(0.5, 0.5, 1);
                minecraft.fontRendererObj.drawStringWithShadow("" + (int) amount, 0, 2, 0xffffff);
                GlStateManager.popMatrix();
            }
        }
    }

    @Override
    public boolean shouldRender(Minecraft minecraft)
    {
        return true;
    }
}
