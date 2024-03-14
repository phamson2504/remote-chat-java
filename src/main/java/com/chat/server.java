package com.chat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class server {
	static int port = 8888;
	public static void main(String[] args) {
		try (ServerSocket socket = new ServerSocket(port)) {
			Socket sc = socket.accept();
			
			ChatBus chat_bus = new ChatBus(sc);
			new ChatUI(chat_bus).setVisible(true);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
