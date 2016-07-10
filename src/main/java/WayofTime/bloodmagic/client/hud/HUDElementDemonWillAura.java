package WayofTime.bloodmagic.client.hud;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.soul.EnumDemonWillType;

public class HUDElementDemonWillAura extends HUDElement
{
    protected Map<EnumDemonWillType, ResourceLocation> crystalTextures = new HashMap<EnumDemonWillType, ResourceLocation>();

    private double maxBarSize = 54;

    public HUDElementDemonWillAura()
    {
        super(5, 5, RenderGameOverlayEvent.ElementType.HOTBAR);
        crystalTextures.put(EnumDemonWillType.DEFAULT, new ResourceLocation(Constants.Mod.MODID, "textures/models/DefaultCrystal.png"));
        crystalTextures.put(EnumDemonWillType.CORROSIVE, new ResourceLocation(Constants.Mod.MODID, "textures/models/CorrosiveCrystal.png"));
        crystalTextures.put(EnumDemonWillType.DESTRUCTIVE, new ResourceLocation(Constants.Mod.MODID, "textures/models/DestructiveCrystal.png"));
        crystalTextures.put(EnumDemonWillType.VENGEFUL, new ResourceLocation(Constants.Mod.MODID, "textures/models/VengefulCrystal.png"));
        crystalTextures.put(EnumDemonWillType.STEADFAST, new ResourceLocation(Constants.Mod.MODID, "textures/models/SteadfastCrystal.png"));
    }

    @Override
    public void render(Minecraft minecraft, ScaledResolution resolution, float partialTicks)
    {
//        ItemStack sigilHolding = minecraft.thePlayer.getHeldItemMainhand();
//        // TODO - Clean this mess
//        // Check mainhand for Sigil of Holding
//        if (sigilHolding == null)
//            return;
//        if (!(sigilHolding.getItem() == ModItems.sigilHolding))
//            sigilHolding = minecraft.thePlayer.getHeldItemOffhand();
//        // Check offhand for Sigil of Holding
//        if (sigilHolding == null)
//            return;
//        if (!(sigilHolding.getItem() == ModItems.sigilHolding))
//            return;

        Gui ingameGui = minecraft.ingameGUI;
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer vertexBuffer = tessellator.getBuffer();

        minecraft.getTextureManager().bindTexture(new ResourceLocation(Constants.Mod.MODID, "textures/gui/demonWillBar.png"));
        GlStateManager.color(1.0F, 1.0F, 1.0F);
        this.drawTexturedModalRect(getXOffset(), getYOffset(), 45, 0, 45, 65);

//        GlStateManager.pushMatrix();

        double maxAmount = 100;

        for (EnumDemonWillType type : EnumDemonWillType.values())
        {
            minecraft.getTextureManager().bindTexture(crystalTextures.get(type));

            double amount = 10 + type.ordinal() * 15;
            double ratio = Math.max(Math.min(amount / maxAmount, 1), 0);

            double x = getXOffset() + 8 + type.ordinal() * 6;
            double y = getYOffset() + 5 + (1 - ratio) * maxBarSize;
            double height = maxBarSize * ratio;
            double width = 5;

            vertexBuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
            vertexBuffer.pos((double) (x), (double) (y + height), 0).tex(0, 1).endVertex();
            vertexBuffer.pos((double) (x + width), (double) (y + height), 0).tex(1d / 8d, 1).endVertex();
            vertexBuffer.pos((double) (x + width), (double) (y), 0).tex(1d / 8d, 1 - ratio).endVertex();
            vertexBuffer.pos((double) (x), (double) (y), 0).tex(0, 1 - ratio).endVertex();
            tessellator.draw();
        }

        minecraft.getTextureManager().bindTexture(new ResourceLocation(Constants.Mod.MODID, "textures/gui/demonWillBar.png"));
        GlStateManager.color(1.0F, 1.0F, 1.0F);
        this.drawTexturedModalRect(getXOffset(), getYOffset(), 0, 0, 45, 65);
    }

    @Override
    public boolean shouldRender(Minecraft minecraft)
    {
        return true;
    }
}
