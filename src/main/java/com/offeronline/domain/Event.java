package com.offeronline.domain;

import java.util.Date;

import javax.persistence.Entity;

@Entity
public class Event extends Deal {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Date openDate;
	private Date closeDate;
	
	public Date getOpenDate() {
		return openDate;
	}
	public void setOpenDate(Date openDate) {
		this.openDate = openDate;
	}
	public Date getCloseDate() {
		return closeDate;
	}
	public void setCloseDate(Date closeDate) {
		this.closeDate = closeDate;
	}
}
