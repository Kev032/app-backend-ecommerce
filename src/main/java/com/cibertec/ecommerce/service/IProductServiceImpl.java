package com.cibertec.ecommerce.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cibertec.ecommerce.model.Product;
import com.cibertec.ecommerce.repository.IProductRepository;

@Service
public class IProductServiceImpl implements IProductService {
	
	@Autowired
	private IProductRepository productoRepository;

	@Override
	public Product save(Product product) {
		return productoRepository.save(product);
	}

	@Override
	public Optional<Product> get(Integer id) {
		return productoRepository.findById(id);
	}

	@Override
	public Product update(Product product) {
		return productoRepository.save(product);
	}

	@Override
	public void delete(Integer id) {
		productoRepository.deleteById(id);
	}

	@Override
	public List<Product> findAll() {
		return productoRepository.findAll();
	}

}
