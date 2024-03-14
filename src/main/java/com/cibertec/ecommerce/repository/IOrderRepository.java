package com.cibertec.ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cibertec.ecommerce.model.Orders;
import com.cibertec.ecommerce.model.User;

@Repository
public interface IOrderRepository extends JpaRepository<Orders, Integer> {
	List<Orders> findByUser (User user);

}
