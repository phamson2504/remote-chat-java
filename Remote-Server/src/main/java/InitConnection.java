import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.chat.ChatBus;
import com.chat.ChatUI;

public class InitConnection {
	ServerSocket socket = null;
	ServerSocket socketChat = null;
	DataInputStream password = null;
	DataOutputStream verify = null;
	String width = "", height = "";
	
	InitConnection(int port, int portChat, String passwordValue) {
		Robot robot = null;
		Rectangle rectangle = null;
		try {
			System.out.println(" watting for client to get connected");
			socket = new ServerSocket(port);
			socketChat = new ServerSocket(portChat);
			GraphicsEnvironment gEv = GraphicsEnvironment.getLocalGraphicsEnvironment();
			GraphicsDevice gDV = gEv.getDefaultScreenDevice();
			Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
			width = String.valueOf(dim.getWidth());
			height = String.valueOf(dim.getHeight());
			rectangle = new Rectangle(dim);
			robot = new Robot(gDV);
			
			while(true)
			{
				Socket sc = socket.accept();
				Socket scChat = socketChat.accept();
				password = new DataInputStream(sc.getInputStream());
				verify = new DataOutputStream(sc.getOutputStream());
				String psword = password.readUTF();
				if (psword.equals(passwordValue)) {
					verify.writeUTF("valid");
					verify.writeUTF(width);
					verify.writeUTF(height);
					new SendScreen(sc, robot, rectangle);
					new ReceiveEvents(sc, robot);
					new ChatUI(new ChatBus(scChat));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
