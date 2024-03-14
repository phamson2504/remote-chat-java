package com.chat;

import java.io.Serializable;

public abstract class Message implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public final static int STRING_MESSAGE = 0x1;
    public final static int FILE_MESSAGE = 0x2;

    private int type;
    private String sender;

    public Message() {
        this.type = 0x0;
    }

    public void setCurrentType(int type) {
        this.type = type;
    }

    public int getCurrentType() {
        return this.type;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSender() {
        return this.sender;
    }
}