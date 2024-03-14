package com.cibertec.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "order_detail")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetail {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idDetail;
	private String description;
	private Integer quantity;
	private double price;
	private double total;


	@ManyToOne(targetEntity = Orders.class)
	@JoinColumn(name = "order_id")
	@JsonBackReference
	private Orders orders;

}
