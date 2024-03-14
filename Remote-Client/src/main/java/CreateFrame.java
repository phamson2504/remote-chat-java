import java.awt.BorderLayout;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;

import com.chat.Message;

public class CreateFrame extends Thread{
	String width = "", hieght = "";
	private JFrame frame = new JFrame();
	private JDesktopPane desktopPane = new JDesktopPane();
	private Socket cs = null;
	private JInternalFrame internalFrame = new JInternalFrame("Server Screen", true, true, true);
	private JPanel cJPanel = new JPanel();
	
	public CreateFrame(Socket cs, String width, String height) {
		this.width = width;
		this.hieght = height;
		this.cs = cs;
		start();
	}
	public void drawGUI()
	{
		frame.add(desktopPane, BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setExtendedState(frame.getExtendedState()|JFrame.MAXIMIZED_BOTH);
		frame.setVisible(true);
		internalFrame.setLayout(new BorderLayout());
		internalFrame.getContentPane().add(cJPanel, BorderLayout.CENTER);
		internalFrame.setSize(100,100);
		desktopPane.add(internalFrame);
		try {
			internalFrame.setMaximum(true);
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
		cJPanel.setFocusable(true);
		internalFrame.setVisible(true);
	}
	public void run() {
		InputStream in = null;
		drawGUI();
		try {
			in = cs.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		new RecevingScreen(in, cJPanel);
		new SendEvents(cs, cJPanel, width, hieght);
		
	}

}
