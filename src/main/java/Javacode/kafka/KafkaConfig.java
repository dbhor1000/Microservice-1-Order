package Javacode.kafka;

import JavacodeLibs.Order;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

//@Configuration
//public class KafkaConfig {
//
//    @Bean
//    public ProducerFactory<String, Order> producerFactory() {
//        Map<String, Object> configProps = new HashMap<>();
//        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
//        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
//        configProps.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "order-transactional-id"); // Enable transactional support
//        DefaultKafkaProducerFactory<String, Order> producerFactory = new DefaultKafkaProducerFactory<>(configProps);
//        producerFactory.setTransactionIdPrefix("order-transactional-id");
//        return producerFactory;
//    }
//
//    @Bean
//    public KafkaTemplate<String, Order> kafkaTemplate() {
//        KafkaTemplate<String, Order> kafkaTemplate = new KafkaTemplate<>(producerFactory());
//        kafkaTemplate.setObservationEnabled(true); // Enable observation
//        return kafkaTemplate;
//    }
//}

@Configuration
public class KafkaConfig {

    @Value("${kafka.topic.partitions}")
    private int numberOfPartitions;

    @Bean
    public NewTopic topic() {
        return TopicBuilder.name("new_orders")
                .partitions(numberOfPartitions)
                .replicas(1)
                .build();
    }

    @Bean
    public ProducerFactory<String, Order> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        configProps.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "order-transactional-id"); // Enable transactional support
        DefaultKafkaProducerFactory<String, Order> producerFactory = new DefaultKafkaProducerFactory<>(configProps);
        producerFactory.setTransactionIdPrefix("order-transactional-id");
        return producerFactory;
    }

    @Bean
    public KafkaTemplate<String, Order> kafkaTemplate() {
        KafkaTemplate<String, Order> kafkaTemplate = new KafkaTemplate<>(producerFactory());
        kafkaTemplate.setObservationEnabled(true); // Enable observation
        return kafkaTemplate;
    }
}