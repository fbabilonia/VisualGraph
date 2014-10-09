package nyc.babilonia.data;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;

/**
 * 
 * @author Fernando Babilonia
 * Base class for both Points and Edges used to share several setters and getters shared
 * by both classes.
 */
public abstract class GraphComponent
{
	protected Stroke stroke;
	protected Color  color , fontColor = Color.BLACK;
	protected abstract void _draw(Graphics2D g2d, Color color);
	/**
	 * Draws component with given color.
	 * @param g2d - Graphics2D object of the application.
	 * @param drawColor - Color the component will be drawn in .
	 */
	public void draw(Graphics2D g2d , Color drawColor)
	{
		_draw(g2d,drawColor);
	}
	/**
	 * Will draw GraphComponent with default color.
	 * @param g2d - Graphics2D component of the application.
	 */
	public void draw(Graphics2D g2d)
	{
		_draw(g2d , color);
	}
	//setters and getters for classes.
	public void setColor(Color newColor)
	{
		color = newColor;
	}
	public Color getColor()
	{
		return color;
	}
	public void setFontColor(Color newFontColor)
	{
		fontColor = newFontColor;
	}
	public Color getFontColor()
	{
		return fontColor;
	}
	public void setStroke(Stroke newStroke)
	{
		stroke = newStroke;
	}
	public Stroke getStroke()
	{
		return stroke;
	}
}
