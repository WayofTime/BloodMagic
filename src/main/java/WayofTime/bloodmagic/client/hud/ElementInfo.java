package WayofTime.bloodmagic.client.hud;

import javax.vecmath.Vector2f;

public class ElementInfo {

    public static final ElementInfo DUMMY = new ElementInfo(new Vector2f(0F, 0F), ElementRegistry.getRandomColor());

    private final Vector2f defaultPosition;
    private final int boxColor;
    private Vector2f currentPosition;

    public ElementInfo(Vector2f defaultPosition, int boxColor) {
        this.defaultPosition = defaultPosition;
        this.boxColor = boxColor;
        this.currentPosition = defaultPosition;
    }

    public Vector2f getDefaultPosition() {
        return defaultPosition;
    }

    public int getBoxColor() {
        return boxColor;
    }

    public ElementInfo setPosition(Vector2f position) {
        this.currentPosition = position;
        return this;
    }

    public Vector2f getPosition() {
        return currentPosition;
    }

    public void resetPosition() {
        this.currentPosition = defaultPosition;
    }
}
