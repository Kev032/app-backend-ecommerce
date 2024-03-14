package com.cibertec.ecommerce.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.cibertec.ecommerce.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cibertec.ecommerce.service.IOrderDetailService;
import com.cibertec.ecommerce.service.IOrderService;
import com.cibertec.ecommerce.service.IUserService;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final Logger log = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private IUserService userService;

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IOrderDetailService orderDetailService;

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping(path = "/list")
    public ResponseEntity<Response> orderFindAll() {

        List<Orders> orders = orderService.findAll();
        log.info("ordenes {}", orders);

        return ResponseEntity.status(HttpStatus.OK).body(new Response(orders, "Se encontraron ordenes"));
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping(path = "/{id}")
    public ResponseEntity<Response> orderById(@PathVariable Integer id) {
        log.info("Id de la orden: {}", id);
        Optional<Orders> order = orderService.findById(id);
        log.info("ordenes {}", order.get());

        if (!order.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("No se encontraró la orden con id: " + id));
        }

        User oUser = new User();
        oUser.setId(order.get().getUser().getId());
        oUser.setName(order.get().getUser().getName());
        oUser.setEmail(order.get().getUser().getEmail());
        oUser.setAddress(order.get().getUser().getAddress());
        oUser.setPhone(order.get().getUser().getPhone());
        oUser.setType(order.get().getUser().getType());
        oUser.setPassword("");

        order.get().setUser( oUser);
        return ResponseEntity.status(HttpStatus.OK).body(new Response(order, "Se encontraron ordenes"));
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping(path = "/client/{id}")
    public ResponseEntity<Response> orderByClient(@PathVariable Integer id) {
        Optional<User> user = userService.findById(id);
        if (!user.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("No se encontraron ordernes asociadas al cliente"));
        }
        Optional<List<Orders>> ordenes = Optional.ofNullable(orderService.findByUser(user.get()));

        if (!ordenes.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("No se encontraron ordenes"));
        }
        user.get().setPassword("");
        ordenes.get().stream().forEach(o -> o.setUser( user.get()));
        return ResponseEntity.status(HttpStatus.OK).body(new Response(ordenes, "Se encontraron ordenes"));
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping(path = "/detail/{id}")
    public ResponseEntity<Response> orderDetail(@PathVariable Integer id) {
        log.info("Id de la orden: {}", id);
        List<OrderDetail> orderDetails = orderService.getOrderDetailsByOrderId(id);

        if (orderDetails.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("No se encontraron detalles"));
        }

        return ResponseEntity.status(HttpStatus.OK).body(new Response(orderDetails, "Se encontraron detalles"));
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping(path = "/create")
    public ResponseEntity<Response> saveOrder(@RequestBody Orders orders) {
        log.info("Orden {}", orders);

        Orders order = new Orders();
        order.setOrderDetails(new ArrayList<>());

        Date dateCreate = new Date();
        order.setCreation_date(dateCreate);
        order.setTotal(orders.getTotal());
        order.setUser(orders.getUser());
        order.setNumber(orderService.generedOrderNumber());

        for (OrderDetail dt : orders.getOrderDetails()) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrders(order);
            orderDetail.setDescription(dt.getDescription());
            orderDetail.setPrice(dt.getPrice());
            orderDetail.setQuantity(dt.getQuantity());
            orderDetail.setTotal(dt.getTotal());
            order.getOrderDetails().add(orderDetail);
        }

        orderService.save(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(new Response(order, "Se registro correctamente la orden."));
    }


}
