import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.chat.ChatBus;
import com.chat.ChatUI;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Authentication extends JFrame implements ActionListener{

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private Socket cs = null;
	private Socket csChat = null;
	InetSocketAddress inetAddress;
	InetSocketAddress inetAddressChat;
	DataOutputStream passchk = null;
	DataInputStream verification = null;
	String width = "",height = "";
	String verify = "";	
	private JTextField textField;

	public Authentication(Socket socket, Socket socketChat) {
		this.cs = socket;
		this.csChat = socketChat;
		setType(Type.POPUP);
		setTitle("Login Form");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 352, 111);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setHorizontalAlignment(SwingConstants.CENTER);
		lblPassword.setFont(new Font("Segoe UI Variable", Font.BOLD, 14));
		lblPassword.setBounds(10, 11, 81, 19);
		contentPane.add(lblPassword);
		
		textField = new JTextField();
		textField.setFont(new Font("Segoe UI Variable", Font.PLAIN, 14));
		textField.setBounds(109, 10, 215, 19);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JButton btnSubmit = new JButton("Submit");
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnSubmit.setFont(new Font("Segoe UI Variable", Font.BOLD, 14));
		btnSubmit.setBounds(109, 39, 93, 28);
		contentPane.add(btnSubmit);
		
		btnSubmit.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String password = textField.getText();
		try {
			passchk = new DataOutputStream(cs.getOutputStream());
			verification = new DataInputStream(cs.getInputStream());
			passchk.writeUTF(password);
			verify = verification.readUTF();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		if (verify.equals("valid")) {
			try {
				width = verification.readUTF();
				height = verification.readUTF();
			} catch (IOException e2) {
				e2.printStackTrace();
			}
			dispose();
			new CreateFrame(cs, width, height);
			new ChatUI(new ChatBus(csChat));
		}
		else {
			System.out.println("password is incorrect");
			JOptionPane.showMessageDialog(this, "password is incrrect", "Error", JOptionPane.ERROR_MESSAGE);
			dispose();
			
		}
	
	}
}
