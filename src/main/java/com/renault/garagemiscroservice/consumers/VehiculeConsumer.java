package com.renault.garagemiscroservice.consumers;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class VehiculeConsumer {
    @KafkaListener(
            topics = {"${spring.kafka.topic}"}
            , groupId = "${spring.kafka.consumer.group-id}")
    public void onMessage(ConsumerRecord<Integer, String> consumerRecord) throws JsonProcessingException {

        log.info("ConsumerRecord : {} ", consumerRecord);

    }

}
