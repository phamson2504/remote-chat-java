package com.chat;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.net.InetAddress;
import java.nio.file.Files;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileSystemView;

public class ChatUI extends JFrame {

	private static final long serialVersionUID = 1L;

	public final static int MAX_LENGTH_LINE = 70;
	private JPanel contenPanel;
	private JScrollPane scrollPane;
	private JTextArea txtMess;

	private GroupLayout layout;
	private JScrollPane scrollMess;
	private GroupLayout.ParallelGroup parallel;
	private GroupLayout.SequentialGroup sequential;

	private ChatBus chatBus;

	/**
	 * Create the frame.
	 */
	public ChatUI(ChatBus chatBus) {
		setTitle("Conversation");
		setFont(new Font("Arial", Font.BOLD, 14));
		setBounds(100, 100, 480, 420);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

		this.chatBus = chatBus;

		getContentPane().setLayout(null);
		contenPanel = new JPanel();
		layout = new GroupLayout(contenPanel);
		parallel = layout.createParallelGroup();
		sequential = layout.createSequentialGroup();
		layout.setHorizontalGroup(parallel);
		layout.setVerticalGroup(sequential);

		contenPanel.setLayout(layout);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 10, 446, 279);
		scrollPane.setViewportView(contenPanel);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		getContentPane().add(scrollPane);

		scrollMess = new JScrollPane();
		txtMess = new JTextArea();
		txtMess.setFont(new Font("Consolas", Font.PLAIN, 14));
		scrollMess.setViewportView(txtMess);
		scrollMess.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollMess.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollMess = new JScrollPane(txtMess);
		scrollMess.setBounds(10, 299, 343, 59);
		getContentPane().add(scrollMess);

		JButton btnSend = new JButton("Send");
		btnSend.setBounds(371, 299, 85, 28);
		getContentPane().add(btnSend);

		JButton btnFile = new JButton("File");
		btnFile.setBounds(371, 330, 85, 28);
		getContentPane().add(btnFile);

		btnSend.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					String content = txtMess.getText();
					if (!content.trim().equals("")) {
						StringMessage mess = new StringMessage(InetAddress.getLocalHost().getHostName(), content);
						chatBus.sendMessage(mess);
						int gap = scrollPane.getWidth() - 180;

						JLabel label = new JLabel("You send a message:" + content);
						label.setFont(new Font("consolas", Font.PLAIN, 14));
						addMesstoPanel(label, gap, "green");
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		btnFile.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(FileSystemView.getFileSystemView().getHomeDirectory());
				fileChooser.showDialog(btnFile, "ok");

				File dir = fileChooser.getSelectedFile();
				if (dir != null) {
					if (dir.length() > 30 * 1024 * 1024) {
						JOptionPane.showMessageDialog(null, "File too large, please send file < 30MB");
					}
					btnFile.setEnabled(false);
					Thread send_thread = new Thread(() -> {
						try {
							FileMessage fileMessage = new FileMessage(InetAddress.getLocalHost().getHostName(),
									dir.getName(), dir.length(), Files.readAllBytes(dir.toPath()));
							chatBus.sendMessage(fileMessage);

							int gap = scrollPane.getWidth() - 180;
							fileMessage.setSender("You");
							FileLabel fileLabel = new FileLabel(fileMessage);
							addMesstoPanel(fileLabel, gap, "green");
							btnFile.setEnabled(true);
						} catch (IOException exception) {
							btnFile.setEnabled(false);
							JOptionPane.showMessageDialog(null, "Can't send file:\n" + exception.getMessage());
						}
					});
				    send_thread.setDaemon(true);
		            send_thread.start();
				}
			}
		});
		new MessagesThread().start();
		setVisible(true);
	}

	class MessagesThread extends Thread {
		public void run() {
			Message obj_message = null;
			StringMessage str_message = null;
			try {
				while (true) {
					obj_message = chatBus.stringMessage();
					if (obj_message != null) {
						if (obj_message.getCurrentType() == Message.STRING_MESSAGE) {
							str_message = (StringMessage) obj_message;
							JLabel label = new JLabel(
									str_message.getSender() + " send a message:" + str_message.getContent());
							label.setFont(new Font("consolas", Font.PLAIN, 14));
							addMesstoPanel(label, 0, "blue");
						}

						else {
							FileMessage file_message = (FileMessage) obj_message;
							FileLabel file_label = new FileLabel(file_message);
							addMesstoPanel(file_label, 0, "blue");
						}
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	private void addMesstoPanel(JLabel label, int gap, String color_header) {
		EventQueue.invokeLater(() -> {
			label.setText(handleMessage(label.getText(), color_header));

			parallel.addGroup(layout.createSequentialGroup().addContainerGap(gap, gap).addComponent(label));
			sequential.addComponent(label).addGap(10, 10, 10);

			scrollPane.revalidate();

			// TODO: move scroll to bottom
			scrollPane.getViewport().setViewPosition(new Point(0, scrollPane.getVerticalScrollBar().getMaximum()));
			scrollPane.getViewport().setViewPosition(new Point(0, scrollPane.getVerticalScrollBar().getMaximum()));
		});
	}

	private String handleMessage(String message, String color_header) {
		String formatted_message = "<html>";
		formatted_message += getHeaderName(message, color_header);
		formatted_message += multiLinesString(message);
		formatted_message += "</html>";
		return formatted_message;
	}

	private String getHeaderName(String message, String color_header) {
		if (message.contains(":")) {
			String header_name = "<font color='" + color_header + "'>";
			header_name += message.substring(0, message.indexOf(':') + 1) + "</font><br>";
			return header_name;
		}
		return "";
	}

	private String multiLinesString(String message) {
		message = message.substring(message.indexOf(':') + 1);
		if (message.length() > ChatUI.MAX_LENGTH_LINE) {
			int loops = message.length() / ChatUI.MAX_LENGTH_LINE;
			int index = 0;
			String content = "";
			for (int i = 0; i < loops; ++i) {
				content += message.substring(index, index + ChatUI.MAX_LENGTH_LINE) + "<br>";
				index += ChatUI.MAX_LENGTH_LINE;
			}
			content += message.substring(index);
			return content;
		}
		return message;
	}
}
