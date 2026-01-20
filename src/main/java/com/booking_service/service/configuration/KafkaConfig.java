package com.booking_service.service.configuration;


import com.booking_service.service.consumer.BookingIntentMessage;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.kafka.autoconfigure.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@Configuration
public class KafkaConfig {

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, BookingIntentMessage>
    bookingIntentKafkaFactory(KafkaProperties props) {

        JsonDeserializer<BookingIntentMessage> deserializer =
                new JsonDeserializer<>(BookingIntentMessage.class);
        deserializer.addTrustedPackages("*");

        DefaultKafkaConsumerFactory<String, BookingIntentMessage> cf =
                new DefaultKafkaConsumerFactory<>(
                        props.buildConsumerProperties(),
                        new StringDeserializer(),
                        deserializer
                );

        ConcurrentKafkaListenerContainerFactory<String, BookingIntentMessage> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(cf);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        return factory;
    }
}
