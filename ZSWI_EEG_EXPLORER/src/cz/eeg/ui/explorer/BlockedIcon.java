package cz.eeg.ui.explorer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.filechooser.FileView;

 /**
 * Instance of {@link BlockedIcon} draw red cross
 * to icon image from {@link Icon} of file system.
 *
 * @author IT Crowd
 */
public class BlockedIcon implements Icon {
	
	private BufferedImage image;
	private int size;
	
	public BlockedIcon(FileView typIkon, Icon ikona) {
		if (ikona instanceof BlockedIcon) {
			image = ((BlockedIcon) ikona).image;
		} else {
			image = new BufferedImage(ikona.getIconWidth(), ikona.getIconHeight(), 
					BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2 = (Graphics2D) image.getGraphics();
			ikona.paintIcon(null, g2, 0, 0);
			
			g2.setStroke(new BasicStroke(3));
			g2.setColor(Color.RED);
			
			size = ikona.getIconWidth();
			
			g2.drawLine(size - 10, size - 10, size - 2, size - 2);
			g2.drawLine(size - 10, size - 2, size - 2, size - 10);
			
		}
	}

	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		final Graphics2D g2D = (Graphics2D) g;
		g2D.drawImage(image, x, y, null);
	}
	
	@Override
	public int getIconWidth() {
		return size;
	}
	
	@Override
	public int getIconHeight() {
		return size;
	}
}
