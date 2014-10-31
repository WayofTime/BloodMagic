package WayofTime.alchemicalWizardry.client.renderer;

import WayofTime.alchemicalWizardry.api.alchemy.energy.IReagentHandler;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentContainerInfo;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentRegistry;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class RenderHelper
{
    public static boolean showEquippedItem = true;
    public static boolean enableItemName = false;
    public static boolean enabled = true;
    public static boolean showInChat = true;

    private static int xOffsetDefault = +50;
    public static int xOffset = xOffsetDefault;
    private static int yOffsetDefault = 2;
    public static int yOffset = yOffsetDefault;
    private static int yOffsetBottomCenterDefault = 41;
    public static int yOffsetBottomCenter = yOffsetBottomCenterDefault;
    private static boolean applyXOffsetToCenterDefault = true;
    public static boolean applyXOffsetToCenter = applyXOffsetToCenterDefault;
    private static boolean applyYOffsetToMiddleDefault = false;
    public static boolean applyYOffsetToMiddle = applyYOffsetToMiddleDefault;

    public static String listMode = "horizontal";
    public static String alignMode = "bottomcenter";

    private static ScaledResolution scaledResolution;

    public static boolean onTickInGame(Minecraft mc)
    {
        if (enabled && (mc.inGameHasFocus || mc.currentScreen == null || (mc.currentScreen instanceof GuiChat && showInChat))
                && !mc.gameSettings.showDebugInfo)
        {
            EntityPlayer player = mc.thePlayer;
            World world = mc.theWorld;
            if (SpellHelper.canPlayerSeeAlchemy(player))
            {
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                scaledResolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
                displayArmorStatus(mc);
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            }

        }

        return true;
    }

    private static List<HUDElement> getHUDElements(Minecraft mc)
    {
        List<HUDElement> elements = new ArrayList();

        MovingObjectPosition movingobjectposition = mc.objectMouseOver;
        World world = mc.theWorld;

        if (movingobjectposition == null)
        {
            return elements;
        } else
        {
            if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
            {
                int x = movingobjectposition.blockX;
                int y = movingobjectposition.blockY;
                int z = movingobjectposition.blockZ;

                TileEntity tile = world.getTileEntity(x, y, z);

                if (!(tile instanceof IReagentHandler))
                {
                    return elements;
                }

                IReagentHandler relay = (IReagentHandler) tile;

                ReagentContainerInfo[] infos = relay.getContainerInfo(ForgeDirection.getOrientation(movingobjectposition.sideHit));

                if (infos != null)
                {
                    for (ReagentContainerInfo info : infos)
                    {
                        if (info == null || info.reagent == null || info.reagent.reagent == null)
                        {
                            continue;
                        }

                        ItemStack itemStack = ReagentRegistry.getItemForReagent(info.reagent.reagent);

                        if (itemStack != null)
                            elements.add(new HUDElement(itemStack, 16, 16, 2, info.reagent.amount));
                    }
                }
            }
        }

        return elements;
    }

    private static int getX(int width)
    {
        if (alignMode.toLowerCase().contains("center"))
            return scaledResolution.getScaledWidth() / 2 - width / 2 + (applyXOffsetToCenter ? xOffset : 0);
        else if (alignMode.toLowerCase().contains("right"))
            return scaledResolution.getScaledWidth() - width - xOffset;
        else
            return xOffset;
    }

    private static int getY(int rowCount, int height)
    {
        if (alignMode.toLowerCase().contains("middle"))
            return (scaledResolution.getScaledHeight() / 2) - ((rowCount * height) / 2) + (applyYOffsetToMiddle ? yOffset : 0);
        else if (alignMode.equalsIgnoreCase("bottomleft") || alignMode.equalsIgnoreCase("bottomright"))
            return scaledResolution.getScaledHeight() - (rowCount * height) - yOffset;
        else if (alignMode.equalsIgnoreCase("bottomcenter"))
            return scaledResolution.getScaledHeight() - (rowCount * height) - yOffsetBottomCenter;
        else
            return yOffset;
    }

    private static int getElementsWidth(List<HUDElement> elements)
    {
        int r = 0;
        for (HUDElement he : elements)
            r += he.width();

        return r;
    }

    private static void displayArmorStatus(Minecraft mc)
    {
        List<HUDElement> elements = getHUDElements(mc);

        if (elements.size() > 0)
        {
            int yOffset = enableItemName ? 18 : 16;

            if (listMode.equalsIgnoreCase("vertical"))
            {
                int yBase = getY(elements.size(), yOffset);

                for (HUDElement e : elements)
                {
                    e.renderToHud((alignMode.toLowerCase().contains("right") ? getX(0) : getX(e.width())), yBase);
                    yBase += yOffset;
                }
            } else if (listMode.equalsIgnoreCase("horizontal"))
            {
                int totalWidth = getElementsWidth(elements);
                int yBase = getY(1, yOffset);
                int xBase = getX(totalWidth);
                int prevX = 0;

                for (HUDElement e : elements)
                {
                    e.renderToHud(xBase + prevX + (alignMode.toLowerCase().contains("right") ? e.width() : 0), yBase);
                    prevX += (e.width());
                }
            } else if (listMode.equalsIgnoreCase("compound"))
            {
                //TODO
            }
        }
    }
}
