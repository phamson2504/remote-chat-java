package com.chat;

import java.io.IOException;
import java.net.Socket;

public class client {
	static int port = 8888;
	public static void main(String[] args) {
		try {
			Socket sc = new Socket("192.168.1.220", port);
			ChatBus chat_bus = new ChatBus(sc);
			new ChatUI(chat_bus).setVisible(true);;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
