package com.chat;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ChatBus {
    private Socket socket;

    public ChatBus(Socket socket) {
        this.socket = socket;
    }

    public void sendMessage(Message message) throws IOException {
        ObjectOutputStream dos = new ObjectOutputStream(this.socket.getOutputStream());
        dos.writeObject(message);
    }

    public Message stringMessage() throws IOException, ClassNotFoundException {
        Message message = null;
        ObjectInputStream dis = new ObjectInputStream(this.socket.getInputStream());
        message = (Message) dis.readObject();
        return message;
    }

    public Socket getSocket() {
        return this.socket;
    }
}
