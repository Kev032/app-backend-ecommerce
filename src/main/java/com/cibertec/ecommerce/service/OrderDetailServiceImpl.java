package com.cibertec.ecommerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cibertec.ecommerce.model.OrderDetail;
import com.cibertec.ecommerce.repository.IOrderDetailRepository;

@Service
public class OrderDetailServiceImpl implements IOrderDetailService {
	
	@Autowired
	private IOrderDetailRepository orderDetailRepository;

	@Override
	public OrderDetail save(OrderDetail orderDetails) {
		return orderDetailRepository.save(orderDetails);
	}




}
