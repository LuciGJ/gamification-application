package com.luci.gamification.service;

import com.luci.gamification.account.EmailDetails;

public interface EmailService {
	// interface used to defined the method for sending an email
	public void sendEmail(EmailDetails details);
}
