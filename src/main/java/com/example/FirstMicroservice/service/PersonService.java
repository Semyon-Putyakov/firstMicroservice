package com.example.FirstMicroservice.service;


import com.example.FirstMicroservice.dto.PersonDTO;
import com.example.FirstMicroservice.kafka.KafkaConsumer;
import com.example.FirstMicroservice.kafka.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonService {

    private final KafkaConsumer kafkaConsumer;
    private final KafkaProducer kafkaProducer;

    @Autowired
    public PersonService(KafkaConsumer kafkaConsumer, KafkaProducer kafkaProducer) {
        this.kafkaConsumer = kafkaConsumer;
        this.kafkaProducer = kafkaProducer;
    }


    public Optional<PersonDTO> getPersonDTO(String username) throws InterruptedException {
        PersonDTO personDTO = new PersonDTO.PersonDTOBuilder()
                .setUsername(username)
                .build();

        ProducerRecord<String, PersonDTO> producerRecord =
                new ProducerRecord<>("topic_request", username, personDTO);
        kafkaProducer.send(producerRecord);
        return Optional.ofNullable(kafkaConsumer.getQueue());

    }
}
