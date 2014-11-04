package bloodutils.api.interfaces;

/** 
 * Copied from WaslieCore, to make it no longer require it in the API. (https://github.com/wasliebob/WaslieCore/blob/master/src/main/java/wasliecore/interfaces/IElement.java)
 */
public interface IEntryElement{
	/**
	 * In here you need to draw the element
	 */
	public void drawElement();
	
	/**
	 * @param mX
	 * Mouse X Position
	 * @param mY
	 * Mouse Y Position
	 * @return is the mouse in a element
	 */
	public boolean isMouseInElement(int mX, int mY);
	
	/**
	 * This get's called when you enter the element
	 * @param mX
	 * Mouse X Position
	 * @param mY
	 * Mouse Y Position
	 */
	
	public void onMouseEnter(int mX, int mY);
	
	/**
	 * This get's called when you click within the element
	 * @param mX
	 * Mouse X Position
	 * @param mY
	 * Mouse Y Position
	 * @param type
	 * Type of click (right, left, scroll)
	 */
	public void onMouseClick(int mX, int mY, int type);
}