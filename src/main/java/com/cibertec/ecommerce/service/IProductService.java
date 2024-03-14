package com.cibertec.ecommerce.service;

import java.util.List;
import java.util.Optional;

import com.cibertec.ecommerce.model.Product;

public interface IProductService {
	public Product save(Product product);
	public Optional<Product> get(Integer id);
	public Product update(Product product);
	public void delete(Integer id);
	public List<Product> findAll();

}
