package com.example.FirstMicroservice.service;

import com.example.FirstMicroservice.dto.PersonDTO;
import com.example.FirstMicroservice.kafka.KafkaConsumer;
import com.example.FirstMicroservice.kafka.KafkaProducer;
import com.example.FirstMicroservice.util.PasswordEncoding;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonService {
    private final KafkaConsumer kafkaConsumer;
    private final KafkaProducer kafkaProducer;
    private PasswordEncoding passwordEncoding;

    @Autowired
    public PersonService(KafkaConsumer kafkaConsumer, KafkaProducer kafkaProducer) {
        this.kafkaConsumer = kafkaConsumer;
        this.kafkaProducer = kafkaProducer;
    }

    public Optional<PersonDTO> getPersonDTO(String username) throws InterruptedException {

        PersonDTO personDTO = new PersonDTO.PersonDTOBuilder()
                .setUsername(username)
                .build();

        String key = "getPersonByUsername_" + username;
        
        ProducerRecord<String, PersonDTO> producerRecord =
                new ProducerRecord<>("topic_request",key,personDTO);

        kafkaProducer.send(producerRecord);

        ConsumerRecord<String, PersonDTO> record = kafkaConsumer.getQueue();


        if (key.equals(record.key())) {
            return Optional.of(record.value());
        }
        return Optional.empty();
    }

    public void createPersonDTO(PersonDTO personDTO) {
        System.out.println("create:" + personDTO.toString());

        personDTO.setPassword(passwordEncoding.encode(personDTO.getPassword()));

        String key = "createPerson_" + personDTO.getUsername();

        ProducerRecord<String,PersonDTO> record = 
                new ProducerRecord<>("topic_request", key, personDTO);
        System.out.println("create:" + record.value().toString());
        System.out.println("before send");
        kafkaProducer.send(record);
    }
}
