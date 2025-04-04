package com.example.FirstMicroservice.kafka;

import com.example.FirstMicroservice.dto.PersonDTO;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Service
public class KafkaConsumer {

    private BlockingQueue<ConsumerRecord<String, PersonDTO>> queue = new ArrayBlockingQueue<>(10);

    @KafkaListener(topics = "topic_response", groupId = "response")
    public void listen(ConsumerRecord<String, PersonDTO> record) {
        queue.offer(record);
    }

    public ConsumerRecord<String, PersonDTO> getQueue() throws InterruptedException {
        return queue.take(); // блокает
    }
}
