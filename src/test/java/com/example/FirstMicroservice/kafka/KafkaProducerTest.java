package com.example.FirstMicroservice.kafka;

import com.example.FirstMicroservice.dto.PersonDTO;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

@ExtendWith(MockitoExtension.class)
class KafkaProducerTest {

    @Mock
    private KafkaTemplate<String, PersonDTO> kafkaTemplate;

    private KafkaProducer kafkaProducer;

    @BeforeEach
    void setUp() {
        kafkaProducer = new KafkaProducer(kafkaTemplate);
    }

    @Test
    void send_SendsMessageToKafka() {
        String topic = "test_topic";
        String key = "test_key";
        PersonDTO personDTO = new PersonDTO.PersonDTOBuilder()
                .setUsername("testUser")
                .build();
        
        ProducerRecord<String, PersonDTO> record = 
                new ProducerRecord<>(topic, key, personDTO);

        kafkaProducer.send(record);

        Mockito.verify(kafkaTemplate).send(topic, key, personDTO);
    }
} 