package com.dariksoft.bazaar.domain;

import java.util.Date;

public class Event extends Item {

	
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
