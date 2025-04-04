package com.example.FirstMicroservice.service;

import com.example.FirstMicroservice.dto.PersonDTO;
import com.example.FirstMicroservice.kafka.KafkaConsumer;
import com.example.FirstMicroservice.kafka.KafkaProducer;
import com.example.FirstMicroservice.util.PasswordEncoding;
import org.apache.kafka.clients.consumer.ConsumerRecord;
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

    public Optional<PersonDTO> getPersonDTO(String username) {
        PersonDTO personDTO = new PersonDTO.PersonDTOBuilder()
                .setUsername(username)
                .build();
        String key = "getPersonByUsername_" + username;
        producerRecord(key, personDTO);
        ConsumerRecord<String, PersonDTO> record = consumerRecord();
        if (key.equals(record.key())) {
            return Optional.ofNullable(record.value());
        }
        return Optional.empty();
    }

    public Optional<PersonDTO> getPersonById(int id) {
        PersonDTO personDTO = new PersonDTO.PersonDTOBuilder()
                .setId(id)
                .build();
        String key = "getPersonById_" + id;
        producerRecord(key, personDTO);
        ConsumerRecord<String, PersonDTO> record = consumerRecord();
        if (key.equals(record.key())) {
            return Optional.ofNullable(record.value());
        }
        return Optional.empty();
    }

    public void createPersonDTO(PersonDTO personDTO) {
        personDTO.setPassword(PasswordEncoding.encode(personDTO.getPassword()));
        String key = "createPerson_" + personDTO.getUsername();
        producerRecord(key, personDTO);
    }

    public void updatePersonDTO(PersonDTO personDTO) {
        String key = "updatePerson_" + personDTO.getUsername();
        producerRecord(key, personDTO);
    }

    public void deletePersonDTO(int id) {
        String key = "deletePersonById_" + id;
        PersonDTO personDTO = new PersonDTO.PersonDTOBuilder()
                .setId(id)
                .build();
        producerRecord(key, personDTO);
    }

    private void producerRecord(String key, PersonDTO personDTO) {
        ProducerRecord<String, PersonDTO> producerRecord =
                new ProducerRecord<>("topic_request", key, personDTO);
        kafkaProducer.send(producerRecord);
    }

    private ConsumerRecord<String, PersonDTO> consumerRecord() {
        try {
            return kafkaConsumer.getQueue();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
