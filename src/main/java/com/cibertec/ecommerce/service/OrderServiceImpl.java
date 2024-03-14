package com.cibertec.ecommerce.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.cibertec.ecommerce.model.Orders;
import com.cibertec.ecommerce.model.OrderDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cibertec.ecommerce.model.User;
import com.cibertec.ecommerce.repository.IOrderRepository;

@Service
public class OrderServiceImpl implements IOrderService {
	
	@Autowired
	private IOrderRepository orderRepository;

	@Override
	public Orders save(Orders orders) {
		return orderRepository.save(orders);
	}

	@Override
	public List<Orders> findAll() {
		return orderRepository.findAll();
	}
	// 0000010
	public String generedOrderNumber() {
		int numero=0;
		String numeroConcatenado="";

		List<Orders> ordenes = findAll();

		List<Integer> numeros= new ArrayList<Integer>();

		ordenes.stream().forEach(o -> numeros.add( Integer.parseInt( o.getNumber().substring(3))));

		if (ordenes.isEmpty()) {
			numero=1;
		}else {
			numero= numeros.stream().max(Integer::compare).get();
			numero++;
		}

		if (numero<10) { //0000001000
			numeroConcatenado="0000"+String.valueOf(numero);
		}else if(numero<100) {
			numeroConcatenado="000"+String.valueOf(numero);
		}else if(numero<1000) {
			numeroConcatenado="00"+String.valueOf(numero);
		}else if(numero<10000) {
			numeroConcatenado="0"+String.valueOf(numero);
		}		
		
		return "ORD"+numeroConcatenado;
	}

	@Override
	public List<Orders> findByUser(User user) {
		return orderRepository.findByUser(user);
	}

	@Override
	public Optional<Orders> findById(Integer id) {
		return orderRepository.findById(id);
	}


	public List<OrderDetail> getOrderDetailsByOrderId(Integer orderId) {
		Orders orders = orderRepository.findById(orderId).orElse(null);
		if (orders != null) {
			return orders.getOrderDetails();
		}
		return new ArrayList<>();
	}


}
