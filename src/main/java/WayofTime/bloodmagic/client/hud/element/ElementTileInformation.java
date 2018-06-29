package WayofTime.bloodmagic.client.hud.element;

import WayofTime.bloodmagic.client.Sprite;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.RayTraceResult;
import org.apache.commons.lang3.tuple.Pair;

import java.awt.Color;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class ElementTileInformation<T extends TileEntity> extends HUDElement {

    protected final Class<T> tileClass;
    private final List<Pair<Sprite, Function<T, String>>> information;

    public ElementTileInformation(int width, int lines, Class<T> tileClass) {
        super(width, 18 * lines - 2);

        this.tileClass = tileClass;
        this.information = Lists.newArrayList();
        gatherInformation(information::add);
    }

    public abstract void gatherInformation(Consumer<Pair<Sprite, Function<T, String>>> information);

    @SuppressWarnings("unchecked")
    @Override
    public void draw(ScaledResolution resolution, float partialTicks, int drawX, int drawY) {
        T tile = (T) Minecraft.getMinecraft().world.getTileEntity(Minecraft.getMinecraft().objectMouseOver.getBlockPos());

        int yOffset = 0;
        for (Pair<Sprite, Function<T, String>> sprite : information) {
            sprite.getLeft().draw(drawX, drawY + yOffset);
            int textY = drawY + yOffset + (sprite.getLeft().getTextureHeight() / 4);
            Minecraft.getMinecraft().fontRenderer.drawStringWithShadow((tile != null && tile.getClass() == tileClass) ? sprite.getRight().apply(tile) : "?", drawX + sprite.getLeft().getTextureWidth() + 2, textY, Color.WHITE.getRGB());
            yOffset += sprite.getLeft().getTextureHeight() + 2;
        }
    }

    @Override
    public boolean shouldRender(Minecraft minecraft) {
        RayTraceResult trace = Minecraft.getMinecraft().objectMouseOver;
        if (trace == null || trace.typeOfHit != RayTraceResult.Type.BLOCK)
            return false;

        TileEntity tile = Minecraft.getMinecraft().world.getTileEntity(trace.getBlockPos());
        if (tile == null || !tileClass.isAssignableFrom(tile.getClass()))
            return false;

        return true;
    }
}
