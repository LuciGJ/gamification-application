package com.luci.gamification.account;

public class EmailDetails {

	// class to store email information

	private String recipient;
	private String msgBody;
	private String subject;

	// constructors

	public EmailDetails() {
	}

	public EmailDetails(String recipient, String msgBody, String subject) {
		this.recipient = recipient;
		this.msgBody = msgBody;
		this.subject = subject;

	}

	// getters and setters

	public String getRecipient() {
		return recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	public String getMsgBody() {
		return msgBody;
	}

	public void setMsgBody(String msgBody) {
		this.msgBody = msgBody;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

}
