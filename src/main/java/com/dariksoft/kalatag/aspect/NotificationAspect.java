package com.dariksoft.kalatag.aspect;

import javax.jms.Destination;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import com.dariksoft.kalatag.domain.Customer;
import com.dariksoft.kalatag.domain.Merchant;
import com.dariksoft.kalatag.domain.Order;
import com.dariksoft.kalatag.domain.Person;
import com.dariksoft.kalatag.service.listener.ChangePasswordMessageCreator;
import com.dariksoft.kalatag.service.listener.GenericMessageCreator;
import com.dariksoft.kalatag.service.listener.RegistrationListener;
import com.dariksoft.kalatag.util.Util;

@Aspect
public class NotificationAspect {

	@Autowired
	JmsTemplate template;

	@Autowired
	Destination emailNotification;

	private Logger log = LoggerFactory.getLogger(RegistrationListener.class);

	@Around("within(com.dariksoft.kalatag.service.CRUDService+) && target(com.dariksoft.kalatag.service.merchant.MerchantServiceImp) && execution(* create(..))")
	public Merchant aroundMerchantCreate(ProceedingJoinPoint pjp)
			throws Throwable {

		Object[] args = pjp.getArgs();
		Merchant merchant = (Merchant) args[0];
		String password = Util.generateRandomPassword();
		String encryptedPassword = Util.toSHA256(password);
		Person person = merchant.getContactPoint();
		person.setPassword(encryptedPassword);
		person.setUsername(merchant.getContact().getEmail());
		try {
			merchant = (Merchant) pjp.proceed();
			person.setPassword(password);
			template.setDefaultDestination(emailNotification);
			MessageCreator messageCreator = new GenericMessageCreator<Person>(
					person);
			template.send(messageCreator);
			person.setPassword(encryptedPassword);
			return merchant;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Around("within(com.dariksoft.kalatag.service.CRUDService+) && target(com.dariksoft.kalatag.service.person.PersonServiceImp) && execution(* create(..))")
	public Person aroundPersonCreate(ProceedingJoinPoint pjp) throws Throwable {

		Object[] args = pjp.getArgs();
		Person person = (Person) args[0];
		String password = Util.generateRandomPassword();
		String encryptedPassword = Util.toSHA256(password);
		log.info("pass=" + encryptedPassword);
		person.setPassword(encryptedPassword);
		try {
			person = (Person) pjp.proceed();
			person.setPassword(password);
			template.setDefaultDestination(emailNotification);
			MessageCreator messageCreator = new GenericMessageCreator<Person>(
					person);
			template.send(messageCreator);
			person.setPassword(encryptedPassword);
			return person;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	@Around("within(com.dariksoft.kalatag.service.CRUDService+) && target(com.dariksoft.kalatag.service.CustomerServiceImp) && execution(* create(..))")
	public Customer aroundCustomerCreate(ProceedingJoinPoint pjp)
			throws Throwable {

		Object[] args = pjp.getArgs();
		Customer customer = (Customer) args[0];
		String password = Util.generateRandomPassword();
		String encryptedPassword = Util.toSHA256(password);
		log.info("pass=" + encryptedPassword);
		customer.setPassword(encryptedPassword);
		try {
			customer = (Customer) pjp.proceed();
			customer.setPassword(password);
			template.setDefaultDestination(emailNotification);
			MessageCreator messageCreator = new GenericMessageCreator<Customer>(
					customer);
			template.send(messageCreator);
			customer.setPassword(encryptedPassword);
			return customer;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	@AfterReturning(pointcut = "within(com.dariksoft.kalatag.service.CRUDService+) && target(com.dariksoft.kalatag.service.order.OrderServiceImp) && execution(* create(..))", returning = "order")
	public void afterOrderCreate(JoinPoint jp, Order order) throws Throwable {
		log.info("after create order id=" + order.getId()
				+ ", sending order to qserver...");
		// order.getDeal().setImages(null);
		// order.getDeal().setThumbnail(null);
		template.setDefaultDestination(emailNotification);
		MessageCreator messageCreator = new GenericMessageCreator<Order>(order);
		template.send(messageCreator);
	}

	@Around("within(com.dariksoft.kalatag.service.CRUDService+) && target(com.dariksoft.kalatag.service.person.PersonServiceImp) && execution(* changePassword(..))")
	public int afterPerosnChangePassword(ProceedingJoinPoint pjp) {
		int ret = 0;
		Object[] args = pjp.getArgs();
		int id = (Integer) args[0];

		String password = (String) args[1];
		String encryptedPassword = Util.toSHA256(password);
		log.info("pass=" + encryptedPassword);

		try {
			ret = (Integer) pjp.proceed();
			template.setDefaultDestination(emailNotification);
			MessageCreator messageCreator = new ChangePasswordMessageCreator(
					id, false);
			template.send(messageCreator);
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		} catch (Throwable e) {
			e.printStackTrace();
			return -2;
		}
	}

}
