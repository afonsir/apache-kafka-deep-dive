package com.github.afonsir;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class Producer {
    public static void main(String[] args) {
        String bootstrapServers = "kafka-1:9092";

        // Producer configs
        Properties properties = new Properties();

        properties.setProperty( ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers );
        properties.setProperty( ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName() );
        properties.setProperty( ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName() );

        // Idempotence
        properties.setProperty( ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true" );
        properties.setProperty( ProducerConfig.ACKS_CONFIG, "all" );
        properties.setProperty( ProducerConfig.RETRIES_CONFIG, Integer.toString(Integer.MAX_VALUE) );
        properties.setProperty( ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, "5" );

        // create producer
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>( properties );

        // create producer record
        ProducerRecord<String, String> record = new ProducerRecord<String, String>( "test", "hello world" );

        // send data
        producer.send( record );

        // flush data
        producer.flush();

        // close producer
        producer.close();
    }
}
