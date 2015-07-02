package WayofTime.alchemicalWizardry.client.renderer;

import WayofTime.alchemicalWizardry.BloodMagicConfiguration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

public class HUDElement
{
    public final ItemStack itemStack;
    public final int iconW;
    public final int iconH;
    public final int padW;
    public final int value;
    private int elementW;
    private int elementH;
    private String itemName = "";
    private int itemNameW;
    private String itemDamage = "";
    private int itemDamageW;
    private Minecraft mc = Minecraft.getMinecraft();

    private static final int offset = 5;

    public boolean enableItemName = false;
    public boolean showValue = true;
    public boolean showDamageOverlay = false;
    public boolean showItemCount = false;

    static RenderItem itemRenderer = new RenderItem();

    public HUDElement(ItemStack itemStack, int iconW, int iconH, int padW, int value)
    {
        this.itemStack = itemStack;
        this.iconW = iconW;
        this.iconH = iconH;
        this.padW = padW;
        this.value = value;

        initSize();
    }

    public int width()
    {
        return elementW;
    }

    public int height()
    {
        return elementH;
    }

    private void initSize()
    {
        elementH = enableItemName ? Math.max(Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT * 2, iconH) :
                Math.max(mc.fontRenderer.FONT_HEIGHT, iconH);

        if (itemStack != null)
        {
            int damage;
            int maxDamage;

            if (showValue)
            {
                maxDamage = itemStack.getMaxDamage() + 1;
                damage = maxDamage - itemStack.getItemDamageForDisplay();

                boolean showSpecialValue = true;
                boolean showValue = false;
                boolean showPercent = false;

                boolean showMaxDamage = true;
                boolean thresholdPercent = true;

                if (showSpecialValue)
                {
                    itemDamage = "\247" + ColourThreshold.getColorCode(BloodMagicConfiguration.colorList,
                            (thresholdPercent ? damage * 100 / maxDamage : damage)) + this.value;
                } else if (showValue)
                    itemDamage = "\247" + ColourThreshold.getColorCode(BloodMagicConfiguration.colorList,
                            (thresholdPercent ? damage * 100 / maxDamage : damage)) + damage +
                            (showMaxDamage ? "/" + maxDamage : "");
                else if (showPercent)
                    itemDamage = "\247" + ColourThreshold.getColorCode(BloodMagicConfiguration.colorList,
                            (thresholdPercent ? damage * 100 / maxDamage : damage)) +
                            (damage * 100 / maxDamage) + "%";
            }

            itemDamageW = mc.fontRenderer.getStringWidth(HUDUtils.stripCtrl(itemDamage));
            elementW = padW + iconW + padW + itemDamageW + offset;

            if (enableItemName)
            {
                itemName = itemStack.getDisplayName();
                elementW = padW + iconW + padW +
                        Math.max(mc.fontRenderer.getStringWidth(HUDUtils.stripCtrl(itemName)), itemDamageW);
            }

            itemNameW = mc.fontRenderer.getStringWidth(HUDUtils.stripCtrl(itemName));
        }
    }

    public void renderToHud(int x, int y)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(32826 /* GL_RESCALE_NORMAL_EXT */);
        RenderHelper.enableStandardItemLighting();
        RenderHelper.enableGUIStandardItemLighting();
        itemRenderer.zLevel = 200.0F;

        //if (ArmorStatusHUD.alignMode.toLowerCase().contains("right"))
        boolean toRight = true;
        if (toRight)
        {
            itemRenderer.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.getTextureManager(), itemStack, x - (iconW + padW), y);
            HUDUtils.renderItemOverlayIntoGUI(mc.fontRenderer, itemStack, x - (iconW + padW), y, showDamageOverlay, showItemCount);

            RenderHelper.disableStandardItemLighting();
            GL11.glDisable(32826 /* GL_RESCALE_NORMAL_EXT */);
            GL11.glDisable(GL11.GL_BLEND);

            mc.fontRenderer.drawStringWithShadow(itemName + "\247r", x - (padW + iconW + padW) - itemNameW, y, 0xffffff);
            mc.fontRenderer.drawStringWithShadow(itemDamage + "\247r", x - (padW + iconW + padW) - itemDamageW,
                    y + (enableItemName ? elementH / 2 : elementH / 4), 0xffffff);
        } else
        {
            itemRenderer.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.getTextureManager(), itemStack, x, y);
            HUDUtils.renderItemOverlayIntoGUI(mc.fontRenderer, itemStack, x, y, showDamageOverlay, showItemCount);

            RenderHelper.disableStandardItemLighting();
            GL11.glDisable(32826 /* GL_RESCALE_NORMAL_EXT */);
            GL11.glDisable(GL11.GL_BLEND);

            mc.fontRenderer.drawStringWithShadow(itemName + "\247r", x + iconW + padW, y, 0xffffff);
            mc.fontRenderer.drawStringWithShadow(itemDamage + "\247r", x + iconW + padW,
                    y + (enableItemName ? elementH / 2 : elementH / 4), 0xffffff);
        }
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }
}