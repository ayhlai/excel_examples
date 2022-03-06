package com.mycompany.xladdin;


import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import org.slf4j.impl.SimpleLogger;

public class QuotesConsumer {

    HashMap<String,Quote> quotes = new HashMap<String, Quote>();

    static QuotesConsumer consumer = null;

    public static QuotesConsumer getInstance() {
        if(consumer == null) {
            consumer = new QuotesConsumer();
        }

        return consumer;
    }

    public static void main(String args[])
    {

        QuotesConsumer quotesConsumer = getInstance();
        quotesConsumer.subscribe(); //subscribe forever
        System.out.println("finished");
    }

    HashMap<String,Quote> getQuotes()
    {
        return this.quotes;
    }

    void subscribe()
    {
        Logger logger= (Logger) LoggerFactory.getLogger(QuotesConsumer.class.getName());
        String bootstrapServers="localhost:9092";
        String grp_id="third_app";
        String topic="my-topic";
        //Creating consumer properties
        Properties properties=new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9094");
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,   StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG,grp_id);
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");
        //creating consumer
        KafkaConsumer<String,String> consumer= new KafkaConsumer<String,String>(properties);
        //Subscribing
        consumer.subscribe(Arrays.asList(topic));
        //polling
        while(true){
            ConsumerRecords<String,String> records=consumer.poll(Duration.ofMillis(100));
            for(ConsumerRecord<String,String> record: records){
                System.out.print("Key: "+ record.key() + ", Value:" +record.value());
                System.out.print("Partition:" + record.partition()+",Offset:"+record.offset());
                logger.info("Key: "+ record.key() + ", Value:" +record.value());
                logger.info("Partition:" + record.partition()+",Offset:"+record.offset());
                Quote q = new Quote();
                q.setId(record.key());
                q.setStatus(record.value());
                quotes.put(record.key(),q);
            }


        }
    }


}