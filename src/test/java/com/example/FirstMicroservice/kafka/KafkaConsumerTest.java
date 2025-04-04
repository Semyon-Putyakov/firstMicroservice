package com.example.FirstMicroservice.kafka;

import com.example.FirstMicroservice.dto.PersonDTO;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class KafkaConsumerTest {

    private KafkaConsumer kafkaConsumer;

    @BeforeEach
    void setUp() {
        kafkaConsumer = new KafkaConsumer();
    }

    @Test
    void listen_AddsRecordToQueue() throws InterruptedException {
        String topic = "topic_response";
        String key = "test_key";
        PersonDTO personDTO = new PersonDTO.PersonDTOBuilder()
                .setUsername("testUser")
                .build();
        
        ConsumerRecord<String, PersonDTO> record = 
                new ConsumerRecord<>(topic, 0, 0, key, personDTO);

        kafkaConsumer.listen(record);
        ConsumerRecord<String, PersonDTO> result = kafkaConsumer.getQueue();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(key, result.key());
        Assertions.assertEquals(personDTO.getUsername(), result.value().getUsername());
    }
} 