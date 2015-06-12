package pneumaticCraft.api.client;

import java.lang.reflect.Method;

import net.minecraft.client.gui.FontRenderer;

public class GuiElementRenderer{
    private static Method drawGaugeMethod;

    /**
     * Draws a Pressure Gauge, the same which is also used in many PneumaticCraft applications.
     * @param fontRenderer          fontrenderer used to draw the numbers of the pressure gauge.
     * @param minPressure           The minimal pressure that needs to be displayed (this is -1 in most applications).
     * @param maxPressure           The maximal pressure that needs to be rendererd (this is 7 for tier one machines, and 25 for tier two).
     * @param dangerPressure        The transition pressure from green to red (this is 5 for tier one, and 29 for tier two machines).
     * @param minWorkingPressure    The transition pressure from yellow to green (variates per machine).
     * @param currentPressure       The pressure that the needle should point to.
     * @param xPos                  x position of the gauge.
     * @param yPos                  y position of the gauge.
     * @param zLevel                z position of the gauge (Gui#zLevel, -90, for in normal GUI's).
     */
    public static void drawPressureGauge(FontRenderer fontRenderer, float minPressure, float maxPressure, float dangerPressure, float minWorkingPressure, float currentPressure, int xPos, int yPos, float zLevel){
        try {
            if(drawGaugeMethod == null) {
                drawGaugeMethod = Class.forName("pneumaticCraft.client.gui.GuiUtils").getMethod("drawPressureGauge", FontRenderer.class, float.class, float.class, float.class, float.class, float.class, int.class, int.class, float.class);
            }
            drawGaugeMethod.invoke(null, fontRenderer, minPressure, maxPressure, dangerPressure, minWorkingPressure, currentPressure, xPos, yPos, zLevel);
        } catch(Exception e) {
            System.err.println("Failed to render a Pressure Gauge from PneumaticCraft.");
        }
    }
}
