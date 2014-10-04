package nyc.babilonia.VisualGraph;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Stroke;

public abstract class GraphComponent
{
	protected Stroke stroke;
	protected Color  color , fontColor;
	public abstract void draw(Graphics2D g2d, Color color);
	public void draw(Graphics2D g2d)
	{
		Color oldColor = g2d.getColor();
		Stroke oldStroke = g2d.getStroke();
		g2d.setStroke(stroke);
		draw(g2d , color);
		g2d.setStroke(oldStroke);
		g2d.setColor(oldColor);
	}
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
