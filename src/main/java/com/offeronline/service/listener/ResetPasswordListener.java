package com.offeronline.service.listener;

import java.util.Locale;

import javax.mail.BodyPart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.offeronline.domain.Person;

@Component("resetPasswordListener")
public class ResetPasswordListener {

private Logger log = LoggerFactory.getLogger(ChangePasswordListener.class);
	
	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private MessageSource messageSource;
	
	
	public void onMessage(Person person) {

		try {
			
			sendEmail(person);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void sendEmail(Person person) {

		try {
			Locale locale = LocaleContextHolder.getLocale();
			log.debug("locale from context=" + locale);
			// locale = new Locale("es_ES");
			// locale = new Locale("ar_AE");
			locale = new Locale("fa");
			log.debug("locale=" + locale);

			String[] params = new String[4];
			params[0] = person.getFirstName();
			params[1] = person.getLastName();
			params[2] = person.getUsername();
			params[3] = person.getPassword();

			String htmlText = messageSource.getMessage("email.user.password.reset", params, locale);

			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
			MimeMultipart multipart = new MimeMultipart("related");
			BodyPart messageBodyPart = new MimeBodyPart();

			// add html part
			messageBodyPart.setContent(htmlText,"text/html; charset=utf-8");
			multipart.addBodyPart(messageBodyPart);

			mimeMessage.setContent(multipart);
			helper.setTo(person.getUsername());
			helper.setSubject(messageSource.getMessage("email.user.password.reset.subject", null, locale));
			mailSender.send(mimeMessage);
			log.info("For reset password notification an email to "
					+ person.getUsername() + " has been sent.");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
