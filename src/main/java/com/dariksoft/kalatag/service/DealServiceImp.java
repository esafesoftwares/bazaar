package com.dariksoft.kalatag.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dariksoft.kalatag.dao.DealDao;
import com.dariksoft.kalatag.domain.Deal;
import com.dariksoft.kalatag.domain.DealLabel;
import com.dariksoft.kalatag.domain.ItemCategory;
import com.dariksoft.kalatag.domain.ItemStatus;

@Service
public class DealServiceImp extends CRUDServiceImp<Deal> implements DealService{

	@Autowired DealDao dealDao;
	
	@Override
	public int getSold() {
	
		return 0;
	}

	@Override
	@Transactional
	public List<Deal> findDealsByStatus(ItemStatus status) {
		return dealDao.findDealsByStatus(status);
	}
	
	@Override
	@Transactional
	public List<Deal> findDealsByLabelAndStatus(DealLabel label, ItemStatus status){
		return dealDao.findDealsByLabelAndStatus(label, status);
	}

	@Override
	@Transactional
	public List<Deal> findDealsByStatusAndNotLabel(DealLabel label, ItemStatus status){
		
		return dealDao.findDealsByStatusAndNotLabel(label, status);
	}

	@Override
	@Transactional
	public List<Deal> findDealsByCategoryAndStatusAndNotLabel(
			ItemCategory category, DealLabel label, ItemStatus status) {
		return dealDao.findDealsByCategoryAndStatusAndNotLabel(category, label, status);
	}
}
