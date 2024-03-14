package com.cibertec.ecommerce.controller;

import java.util.List;
import java.io.IOException;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import com.cibertec.ecommerce.model.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.cibertec.ecommerce.model.Product;
import com.cibertec.ecommerce.service.IProductService;

@RestController
@RequestMapping("api/v1/product")
public class ProductController {

	private final Logger log = LoggerFactory.getLogger(ProductController.class);

	@Autowired
	private IProductService productService;

	@CrossOrigin(origins = "http://localhost:4200")
	@GetMapping(path = "/list")
	public ResponseEntity<Response> products() {
		List<Product> products = productService.findAll();
		return ResponseEntity.status(HttpStatus.OK).body(new Response(products, "Se encontraron productos"));
	}

	@CrossOrigin(origins = "http://localhost:4200")
	@GetMapping(path = "/{id}")
	public ResponseEntity<Response> productById(@PathVariable Integer id) {
		Optional<Product> producto = productService.get(id);
		if (producto.isPresent()){
			return ResponseEntity.status(HttpStatus.OK).body(new Response(producto, "Se encontró el producto"));
		}else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("No se encontró el producto"));
		}
	}

	@CrossOrigin(origins = "http://localhost:4200")
	@PostMapping(path = "/create", produces = "application/json")
	public ResponseEntity<Response>  register(@RequestParam("product") String product, @RequestParam("image") MultipartFile image) throws IOException {
		log.info("Este es el objeto producto {}", product);

		ObjectMapper mapper = new ObjectMapper();
		Product prod = mapper.readValue(product, Product.class);

		prod.setImage(image.getBytes());
		Optional<Product> newproduct = Optional.ofNullable(productService.save(prod));

		return ResponseEntity.status(HttpStatus.OK).body(new Response(newproduct, "Se registró correctamente el producto."));
	}

	@CrossOrigin(origins = "http://localhost:4200")
	@PutMapping(path = "/update", produces = "application/json")
	public ResponseEntity<Response>  update(@RequestParam("product") String product, @RequestParam("image") MultipartFile image) throws IOException {
		log.info("Este es el objeto producto {}", product);

		ObjectMapper mapper = new ObjectMapper();
		Product prod = mapper.readValue(product, Product.class);

		Optional<Product> producto = productService.get(prod.getId());
		if (producto.isPresent()) {
			prod.setImage(image.getBytes());
			Optional<Product> updateproduct =  Optional.ofNullable(productService.update(prod));
            return ResponseEntity.status(HttpStatus.OK).body(new Response(updateproduct, "Se actualizó correctamente el producto."));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("No se encontró el producto"));
        }
	}

	@CrossOrigin(origins = "http://localhost:4200")
	@DeleteMapping(path = "/delete/{id}")
	public ResponseEntity<Response>  delete(@PathVariable Integer id) {
		log.info("Este es el objeto producto {}",id);

		productService.delete(id);

		return ResponseEntity.status(HttpStatus.OK).body(new Response(true, "Se eliminó correctamente el producto."));
	}


}
