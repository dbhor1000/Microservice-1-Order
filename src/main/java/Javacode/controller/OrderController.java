package Javacode.controller;

import JavacodeLibs.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
class OrderController {

    @Autowired
    private KafkaTemplate<String, Order> kafkaTemplate;

    private static final String TOPIC = "new_orders";
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @PostMapping("/order")
    public String createOrder(@RequestBody Order order) {
        // Validate the order (simple validation for example)
        if (order == null || order.getOrderId() == null || order.getUserId() == null || order.getItems() == null) {
            logger.warn("Invalid order received: {}", order.getOrderId());
            return "Invalid order";
        }

        try {
            String key = order.getOrderId();
            // Publish the order to Kafka
            kafkaTemplate.executeInTransaction(operations -> {
                operations.send(TOPIC, key, order);
                logger.info("OUT: Posted order: {}", order.getOrderId());
                return null;
            });
            logger.info("Order received: {}", order.getOrderId());
            return "Order received: " + order.getOrderId();
        } catch (Exception e) {
            logger.error("Error sending order to Kafka: {}", order, e);
            return "Error processing order";
        }
    }
}
