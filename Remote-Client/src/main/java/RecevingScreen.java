import java.awt.Graphics;
import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class RecevingScreen extends Thread{

	private JPanel cJPanel = null;
	private boolean continueLoop = true;
	InputStream oin = null;
	Image image =null;
	
	public RecevingScreen(InputStream in, JPanel cJPanel) {
		this.oin = in;
		this.cJPanel = cJPanel;
		start();
	}
	
	public void run()
	{
		try {
			while (continueLoop) {
					byte[] bytes = new byte[1024 * 1024];
					int count = 0;
					do {
						count += oin.read(bytes,count,bytes.length-count); 
					}while(!(count > 4 && bytes[count - 2] == (byte)-1 && bytes[count - 1] == (byte) -39));
					image = ImageIO.read(new ByteArrayInputStream(bytes));
					image = image.getScaledInstance(cJPanel.getWidth(), cJPanel.getHeight(), Image.SCALE_FAST);
					Graphics graphics = cJPanel.getGraphics();
					graphics.drawImage(image, 0, 0, cJPanel.getWidth(), cJPanel.getHeight(), cJPanel);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
