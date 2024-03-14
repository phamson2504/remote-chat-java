import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import javax.imageio.ImageIO;

public class SendScreen extends Thread{
	Socket socket = null;
	Robot robot = null;
	Rectangle rectangle = null;
	private boolean continueLoop = true;
	OutputStream oPS = null;
	
	public SendScreen(Socket sc, Robot robot, Rectangle rectangle) {
		this.socket = sc;
		this.robot = robot;
		this.rectangle = rectangle;
		start();
	}
	public void run()
	{
		try {
			oPS = socket.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		while(continueLoop)
		{
			BufferedImage image = robot.createScreenCapture(rectangle);
			try {
				ImageIO.write(image, "jpeg", oPS);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			try{
                Thread.sleep(100);
            }
			catch(Exception e){
                /////
            }
		}
	}

}
