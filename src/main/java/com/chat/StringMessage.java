package com.chat;

public class StringMessage extends Message {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String content;

    public StringMessage(String sender, String content) {
        this.setCurrentType(Message.STRING_MESSAGE);
        this.setSender(sender);
        this.content = content;
    }

    public String getContent() {
        return this.content;
    }
}
