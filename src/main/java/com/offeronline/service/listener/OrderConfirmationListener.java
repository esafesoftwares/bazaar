package com.offeronline.service.listener;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;

import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.offeronline.domain.Coupon;
import com.offeronline.domain.Order;
import com.offeronline.service.order.OrderService;
import com.offeronline.util.Util;

@Component("orderConfirmationListener")
public class OrderConfirmationListener {

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private OrderService orderService;

	// @Autowired
	// private CouponService couponService;
	
	Locale locale = new Locale("fa");

	private Logger log = LoggerFactory
			.getLogger(OrderConfirmationListener.class);

	public void onMessage(Order order) {

		try {
			log.debug("---------->Order confirmation message from Qserver. order id="
					+ order.getId());
			List<Order> orders = orderService.confirmOrder(order);
			if (orders.size() > 0)
				for (Order ord : orders) {
					log.debug("Order confirmation: id:" + ord.getId()
							+ ", Customer: " + ord.getPerson().getFirstName()
							+ " " + ord.getPerson().getLastName()
							+ ", Status: " + ord.getStatus());

					sendOrderConfirmEmail(ord);
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendOrderConfirmEmail(Order order) {

		try {
			
			String[] params = new String[11];
			params[0] = order.getPerson().getFirstName();
			params[1] = order.getPerson().getLastName();
			params[2] = order.getDeal().getName();
			params[3] = order.getOption().getName();
			params[4] = order.getPerson().getFirstName() + " "
					+ order.getPerson().getLastName();
			params[5] = order.getDeal().getMerchant().getName();
			params[6] = order.getDeal().getMerchant().getContact().getAddress();
			params[7] = order.getCoupons().get(0).getIssueDate().toString();
			params[8] = order.getOption().getPrice() + "";
			params[9] = messageSource.getMessage("kalatag.currency", null, locale);

			StringBuffer sb = new StringBuffer();
			for (Coupon c : order.getCoupons()) {
				sb.append(c.getCode());
				sb.append("<br>");
			}
			params[10] = sb.toString();

			ClassLoader classLoader = getClass().getClassLoader();
			File file = new File(classLoader.getResource(messageSource.getMessage("mail.user.receipt", null,locale)).getFile());
			
			String html = Util.readFile(file.getAbsolutePath(), StandardCharsets.UTF_8);
			MessageFormat mf = new MessageFormat(html);
			String htmlText = mf.format(params);
			
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true,	"utf-8");
			MimeMultipart multipart = new MimeMultipart("related");
			BodyPart messageBodyPart = new MimeBodyPart();

			// add html part
			messageBodyPart.setContent(htmlText, "text/html; charset=utf-8");
			multipart.addBodyPart(messageBodyPart);

			// add image
			byte[] thumbnail = order.getDeal().getThumbnail();
			if (thumbnail != null) {
				messageBodyPart = new MimeBodyPart();
				ByteArrayDataSource dealImgDs = new ByteArrayDataSource(
						thumbnail, "image/jpeg");
				messageBodyPart.setDataHandler(new DataHandler(dealImgDs));
				messageBodyPart.setHeader("Content-ID", "<image>");
				multipart.addBodyPart(messageBodyPart);
			}

			/*
			 * ******************************************** WARNING!
			 * ******************************************** should be change for
			 * all coupons
			 */
			if (!order.getCoupons().isEmpty()) {
				// add QR code
				byte[] qrcode = order.getCoupons().get(0).getQrcode();
				if (qrcode != null) {
					messageBodyPart = new MimeBodyPart();
					ByteArrayDataSource qrCodeDs = new ByteArrayDataSource(
							qrcode, "image/jpeg");
					messageBodyPart.setDataHandler(new DataHandler(qrCodeDs));
					messageBodyPart.setHeader("Content-ID", "<qrcode>");
					multipart.addBodyPart(messageBodyPart);
				}

				// add barcode
				byte[] barcode = order.getCoupons().get(0).getBarcode();
				if (barcode != null) {
					messageBodyPart = new MimeBodyPart();
					ByteArrayDataSource barcodeDs = new ByteArrayDataSource(
							barcode, "image/jpeg");
					messageBodyPart.setDataHandler(new DataHandler(barcodeDs));
					messageBodyPart.setHeader("Content-ID", "<barcode>");
					multipart.addBodyPart(messageBodyPart);
				}
			}

			mimeMessage.setContent(multipart);
			helper.setTo(order.getPerson().getUsername());
			helper.setSubject(messageSource.getMessage(
					"email.user.receipt.subject", null, locale));
			mailSender.send(mimeMessage);
			log.info("For order confirmation an email to "
					+ order.getPerson().getUsername() + " has been sent.");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
