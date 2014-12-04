package com.dariksoft.kalatag.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Customer extends Person {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ManyToOne(cascade= CascadeType.ALL)
	@JoinColumn(name = "contact_id", nullable = true )
	private Contact contact;

	public Contact getContact() {
		return contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}
}
