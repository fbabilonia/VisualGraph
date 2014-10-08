package nyc.babilonia.VisualGraph.data;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;

public abstract class GraphComponent
{
	protected Stroke stroke;
	protected Color  color , fontColor = Color.BLACK;
	protected abstract void _draw(Graphics2D g2d, Color color);
	public void draw(Graphics2D g2d , Color drawColor)
	{
		Color oldColor = g2d.getColor();
		Stroke oldStroke = g2d.getStroke();
		g2d.setStroke(stroke);
		g2d.setColor(drawColor);
		_draw(g2d,drawColor);
		g2d.setStroke(oldStroke);
		g2d.setColor(oldColor);
	}
	public void draw(Graphics2D g2d)
	{
		draw(g2d , color);
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
