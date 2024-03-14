package com.cibertec.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cibertec.ecommerce.model.Product;

@Repository
public interface IProductRepository extends JpaRepository<Product, Integer> {

}
