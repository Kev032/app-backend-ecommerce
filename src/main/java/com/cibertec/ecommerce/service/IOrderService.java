package com.cibertec.ecommerce.service;

import java.util.List;
import java.util.Optional;

import com.cibertec.ecommerce.model.OrderDetail;
import com.cibertec.ecommerce.model.Orders;
import com.cibertec.ecommerce.model.User;

public interface IOrderService {
	List<Orders> findAll();
	Optional<Orders> findById(Integer id);
	Orders save (Orders orders);
	String generedOrderNumber();
	List<Orders> findByUser (User user);

	List<OrderDetail> getOrderDetailsByOrderId(Integer orderId);
}
