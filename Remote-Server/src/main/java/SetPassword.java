import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;
import javax.swing.JButton;

public class SetPassword extends JFrame implements ActionListener{

	static String port = "9999";
	static String portChat = "8888";
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField;

	public SetPassword() {
		setTitle("Set password for client");
		setFont(new Font("Arial", Font.BOLD, 14));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 400, 105);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblSetPassword = new JLabel("Set Password");
		lblSetPassword.setFont(new Font("Segoe UI Variable", Font.BOLD, 14));
		lblSetPassword.setBounds(10, 14, 90, 13);
		contentPane.add(lblSetPassword);
		
		textField = new JTextField(15);
		textField.setFont(new Font("Segoe UI Variable", Font.PLAIN, 14));
		textField.setBounds(136, 10, 240, 19);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JButton btnNewButton = new JButton("Submit");
		btnNewButton.setFont(new Font("Segoe UI Variable", Font.PLAIN, 14));
		btnNewButton.setBounds(136, 39, 97, 26);
		contentPane.add(btnNewButton);
		
		btnNewButton.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String passwordValue = textField.getText();
		dispose();
		new InitConnection(Integer.parseInt(port),Integer.parseInt(portChat),passwordValue);
	}
}
