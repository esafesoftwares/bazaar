package com.offeronline.domain;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Merchant implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private int id;
	

	
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="contact_point_id", nullable=true)
	@JsonIgnore
	private Person contactPoint;
	@NotEmpty
	private String name;
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="contact_id", nullable=true)
	
	private Contact contact;
	
	@OneToMany(targetEntity = Deal.class, mappedBy = "merchant")
	@JsonIgnore
	private Set<Deal> items;
	public Person getContactPoint() {
		return contactPoint;
	}
	public void setContactPoint(Person contactPoint) {
		this.contactPoint = contactPoint;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Contact getContact() {
		return contact;
	}
	public void setContact(Contact contact) {
		this.contact = contact;
	}
	public Set<Deal> getItems() {
		return items;
	}
	public void setItems(Set<Deal> items) {
		this.items = items;
	}

}
