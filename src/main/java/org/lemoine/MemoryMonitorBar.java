package org.lemoine;

// Taken from http://forums.oracle.com/forums/thread.jspa?threadID=1756114&tstart=60
// In this thread, the author opensources it....

import java.awt.*;
import javax.swing.*;
 
 
/**
 * Memory monitor component.
 *
 * @author	Antoine Lemoine
 * @version	1.0 (Released November 25, 2005)
 */
public class MemoryMonitorBar extends JPanel
{
 
	/*--------------------------------------------------------------------------
			Constructors
	--------------------------------------------------------------------------*/
 
	public MemoryMonitorBar()
	{
		setLayout(new FlowLayout(FlowLayout.CENTER,5,0));
 
		this.label = new JLabel();
		add(label);
		
		Thread thread =
			new Thread()
			{
				public void run()
				{
					repaint();
					try { Thread.sleep(1000); } catch (Exception e) {}
				}
			};
	}
 
	/*--------------------------------------------------------------------------
			Protected redefined methods
	--------------------------------------------------------------------------*/
 
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
 
		long freeMemory = Runtime.getRuntime().freeMemory();
		long totalMemory = Runtime.getRuntime().totalMemory();
		long maxMemory = Runtime.getRuntime().maxMemory();
 
		long memoryUsed = totalMemory-freeMemory;
 
		label.setText((memoryUsed/(1024*1024))+" of "+(maxMemory/(1024*1024))+" MB used");
 
		Insets insets = getInsets();
 
		int width = getWidth()-insets.left-insets.right;
		int height = getHeight()-insets.top-insets.bottom;
 
		int x0 = insets.left;
		int x1 = insets.left+(int)(width*memoryUsed/maxMemory);
		int x2 = insets.left+(int)(width*totalMemory/maxMemory);
		int x3 = insets.left+width;
 
		int y0 = insets.top;
		int y1 = insets.top+height;
 
		Graphics2D g2d = (Graphics2D)g;
		Paint oldPaint = g2d.getPaint();
		g2d.setPaint(new GradientPaint(0,0,Color.white,0,height,new Color(0x0072B6)));
		g2d.fillRect(x0,y0,x1,y1);
 
		g2d.setPaint(oldPaint);
 
		g2d.setColor(new Color(0x999999));
		g2d.fillRect(x1,y0,x2,y1);
 
		g2d.setColor(getBackground());
		g2d.fillRect(x2,y0,x3,y1);
	}
 
	/*--------------------------------------------------------------------------
			Private attributes
	--------------------------------------------------------------------------*/
 
	private JLabel label;
 
}