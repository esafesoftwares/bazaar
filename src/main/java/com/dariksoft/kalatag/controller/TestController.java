package com.dariksoft.kalatag.controller;

import java.text.SimpleDateFormat;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dariksoft.kalatag.domain.Gender;
import com.dariksoft.kalatag.domain.Person;
import com.dariksoft.kalatag.service.person.PersonService;
import com.dariksoft.kalatag.service.person.PersonServiceImp;


@Controller
@RequestMapping(value = "/test")
public class TestController {
	
	public PersonService getService() {
		return service;
	}

	public void setService(PersonService service) {
		this.service = service;
	}

	@Autowired
	PersonService service;
	
	@RequestMapping( method = RequestMethod.GET)
	public String addPerson(Model model) {
		try {
			Person p = new Person();
			p.setFirstName("asdasdasd");
			p.setLastName("rrrrr");
			p.setGender(Gender.Female);
			p.setBirthday( new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH).parse("1974/09/06"));
			p.setUsername("hhhhhh");
			service.create(p);
//			service.sendRegisterationNotification(p);
			model.addAttribute("msg", p.toString());
			return "test";
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

}