package com.luci.gamification.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.luci.gamification.account.EmailDetails;



@Service
public class EmailServiceImpl implements EmailService {

	// the implementation of the EmailService interface
	
	// used to send the email
	@Autowired
	private JavaMailSender javaMailSender;

	@Override
	public void sendEmail(EmailDetails details) {
		try {

			// create a SimpleMailMessage object and use the data from EmailDetails to set its properties
			
			SimpleMailMessage mailMessage = new SimpleMailMessage();

			mailMessage.setFrom("gamification@coolapp.com");
			mailMessage.setTo(details.getRecipient());
			mailMessage.setText(details.getMsgBody());
			mailMessage.setSubject(details.getSubject());

			javaMailSender.send(mailMessage);
		}

		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
