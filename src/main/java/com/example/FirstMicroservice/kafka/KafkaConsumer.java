package com.example.FirstMicroservice.kafka;

import com.example.FirstMicroservice.dto.PersonDTO;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Service
public class KafkaConsumer {

    private BlockingQueue<PersonDTO> queue = new ArrayBlockingQueue<>(1);

    @KafkaListener(topics = "topic_response") // создает свой поток
    public void listen(ConsumerRecord<String, PersonDTO> record) {
        queue.offer(record.value());
    }

    public PersonDTO getQueue() throws InterruptedException {
        return queue.take();
    }

    //покумекать с созданием потока
}
