package com.example.FirstMicroservice.kafka;

import com.example.FirstMicroservice.dto.PersonDTO;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {

    private final KafkaTemplate<String, PersonDTO> kafkaTemplate;

    @Autowired
    public KafkaProducer(KafkaTemplate<String, PersonDTO> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(ProducerRecord<String, PersonDTO> record) {
        kafkaTemplate.send("topic_request", record.key(), record.value());
    }


}
