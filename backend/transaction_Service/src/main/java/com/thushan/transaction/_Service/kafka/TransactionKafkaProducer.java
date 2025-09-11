package com.thushan.transaction._Service.kafka;


import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionKafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendTransactionEvent(String message) {
        kafkaTemplate.send("transaction-events", message);
    }
}
