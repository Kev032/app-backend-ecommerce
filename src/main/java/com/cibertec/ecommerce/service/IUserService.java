package com.cibertec.ecommerce.service;

import java.util.List;
import java.util.Optional;

import com.cibertec.ecommerce.model.User;

public interface IUserService {
	List<User> findAll();
	Optional<User> findById(Integer id);
	User save (User user);
	Optional<User> findByEmail(String email);

}
