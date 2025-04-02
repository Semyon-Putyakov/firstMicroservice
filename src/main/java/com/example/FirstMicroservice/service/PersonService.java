package com.example.FirstMicroservice.service;


import com.example.FirstMicroservice.dto.PersonDTO;
import com.example.FirstMicroservice.kafka.KafkaConsumer;
import com.example.FirstMicroservice.kafka.KafkaProducer;
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


    public Optional<PersonDTO> getPersonDTO(String username) throws InterruptedException {

        PersonDTO personDTO = new PersonDTO.PersonDTOBuilder()
                .setUsername(username)
                .build();

        String key = "getPersonByUsername_" + username;

        ProducerRecord<String, PersonDTO> producerRecord =
                new ProducerRecord<>(key, personDTO);


        kafkaProducer.send(producerRecord);

        ConsumerRecord<String, PersonDTO> record = kafkaConsumer.getQueue();


        if (key.equals(record.key())) {
            return Optional.of(record.value());
        }
        return Optional.empty();
    }

    public void createPersonDTO(PersonDTO personDTO) {

        String key = "createPerson_" + personDTO.getUsername();

        ProducerRecord<String,PersonDTO> record = new ProducerRecord<>(key, personDTO);
        kafkaProducer.send(record);

    }
}
