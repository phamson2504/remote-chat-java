import java.net.InetSocketAddress;
import java.net.Socket;

import javax.swing.JOptionPane;


public class Main {
	static String port = "9999";
	static String portChat = "8888";
	public static void main(String[] args) {
		String ip = JOptionPane.showInputDialog("please enter server ip");
		new Main().initialize(ip,Integer.parseInt(port));
	}
	
	private void initialize(String ip, int parseInt) {
		try {
			Socket sc= new Socket(ip, parseInt);
			Socket scChat = new Socket(ip, Integer.parseInt(portChat));
			
			System.out.println("Connecting to server");
			
			Authentication authentication = new Authentication(sc , scChat);
			authentication.setVisible(true);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
