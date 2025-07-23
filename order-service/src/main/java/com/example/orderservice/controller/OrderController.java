package com.example.orderservice.controller;

import com.example.common.dto.ProductDto;
import com.example.common.event.OrderCreatedEvent;
import com.example.orderservice.client.ProductClient;
import com.example.orderservice.dto.CreateOrderRequestDto;
import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.kafka.OrderKafkaProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final ProductClient productClient;
    private final OrderKafkaProducer orderKafkaProducer;

    // 전체 주문 보기
    @GetMapping
    public List<OrderDto> getOrders() {
        List<ProductDto> products = productClient.getProducts();

        return List.of(
                new OrderDto("1", "Coldstone", products)
        );
    }

    // 주문하기
    @PostMapping
    public String createOrder(@RequestBody CreateOrderRequestDto createOrderRequestDto) {
        String orderId = UUID.randomUUID().toString();

        OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent(
                orderId,
                createOrderRequestDto.getProductId(),
                createOrderRequestDto.getQuantity()
        );

        orderKafkaProducer.send(orderCreatedEvent);

        return "Order Success: " + orderId;
    }
}
