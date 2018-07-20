package WayofTime.bloodmagic.client.hud.element;

import WayofTime.bloodmagic.client.Sprite;
import net.minecraft.client.gui.ScaledResolution;

public class ElementSprite extends HUDElement {

    private final Sprite sprite;

    public ElementSprite(Sprite sprite) {
        super(sprite.getTextureWidth(), sprite.getTextureHeight());

        this.sprite = sprite;
    }

    @Override
    public void draw(ScaledResolution resolution, float partialTicks, int drawX, int drawY) {
        sprite.draw(drawX, drawY);
    }
}
