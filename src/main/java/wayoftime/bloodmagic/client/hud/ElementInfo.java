package wayoftime.bloodmagic.client.hud;

import net.minecraft.world.phys.Vec2;

public class ElementInfo
{
	public static final ElementInfo DUMMY = new ElementInfo(new Vec2(0F, 0F), ElementRegistry.getRandomColor());

	private final Vec2 defaultPosition;
	private final int boxColor;
	private Vec2 currentPosition;

	public ElementInfo(Vec2 defaultPosition, int boxColor)
	{
		this.defaultPosition = defaultPosition;
		this.boxColor = boxColor;
		this.currentPosition = defaultPosition;
	}

	public Vec2 getDefaultPosition()
	{
		return defaultPosition;
	}

	public int getBoxColor()
	{
		return boxColor;
	}

	public ElementInfo setPosition(Vec2 position)
	{
		this.currentPosition = position;
		return this;
	}

	public Vec2 getPosition()
	{
		return currentPosition;
	}

	public void resetPosition()
	{
		this.currentPosition = defaultPosition;
	}
}