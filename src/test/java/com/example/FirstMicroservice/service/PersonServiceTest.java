package com.example.FirstMicroservice.service;

import com.example.FirstMicroservice.dto.PersonDTO;
import com.example.FirstMicroservice.kafka.KafkaConsumer;
import com.example.FirstMicroservice.kafka.KafkaProducer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    @Mock
    private KafkaConsumer kafkaConsumer;

    @Mock
    private KafkaProducer kafkaProducer;

    private PersonService personService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        personService = new PersonService(kafkaConsumer, kafkaProducer);
    }

    @Test
    void getPersonDTO_WhenUserExists_ReturnsPersonDTO() throws InterruptedException {
        String username = "testUser";
        PersonDTO expectedPerson = new PersonDTO.PersonDTOBuilder()
                .setUsername(username)
                .setPassword("password")
                .setId(1)
                .build();
        ConsumerRecord<String, PersonDTO> record = new ConsumerRecord<>("topic_response",
                0, 0, "getPersonByUsername_" + username, expectedPerson);
        Mockito.when(kafkaConsumer.getQueue()).thenReturn(record);

        Optional<PersonDTO> result = personService.getPersonDTO(username);

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(username, result.get().getUsername());
        Mockito.verify(kafkaProducer, times(1)).send(any());
    }

    @Test
    void getPersonDTO_WhenUserDoesNotExist_ReturnsEmpty() throws InterruptedException {
        String username = "nonExistentUser";
        ConsumerRecord<String, PersonDTO> record = new ConsumerRecord<>("topic_response", 
                0, 0, "different_key", null);
        Mockito.when(kafkaConsumer.getQueue()).thenReturn(record);

        Optional<PersonDTO> result = personService.getPersonDTO(username);

        Assertions.assertTrue(result.isEmpty());
        Mockito.verify(kafkaProducer, times(1)).send(any());
    }

    @Test
    void getPersonById_WhenPersonExists_ReturnsPersonDTO() throws InterruptedException {
        int id = 1;
        PersonDTO expectedPerson = new PersonDTO.PersonDTOBuilder()
                .setId(id)
                .setUsername("testUser")
                .build();
        ConsumerRecord<String, PersonDTO> record = new ConsumerRecord<>("topic_response",
                0, 0, "getPersonById_" + id, expectedPerson);
        Mockito.when(kafkaConsumer.getQueue()).thenReturn(record);

        Optional<PersonDTO> result = personService.getPersonById(id);

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(id, result.get().getId());
        Mockito.verify(kafkaProducer, times(1)).send(any());
    }

    @Test
    void createPersonDTO_EncryptsPasswordAndSendsToKafka() {
        PersonDTO personDTO = new PersonDTO.PersonDTOBuilder()
                .setUsername("newUser")
                .setPassword("password123")
                .build();

        personService.createPersonDTO(personDTO);

        Mockito.verify(kafkaProducer, times(1)).send(any());
    }
} 