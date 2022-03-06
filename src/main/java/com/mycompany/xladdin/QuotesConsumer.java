package com.mycompany.xladdin;


import java.time.Duration;
import java.util.*;

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
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.impl.SimpleLogger;

public class QuotesConsumer extends Thread   {

    ConcurrentHashMap<String,Quote> quotes = null;

    static QuotesConsumer consumer = null;


    public QuotesConsumer()
    {
        quotes = new ConcurrentHashMap<String, Quote>();
    }

    public static QuotesConsumer getInstance() {
        if(consumer == null) {
            consumer = new QuotesConsumer();
        }

        return consumer;
    }


    public void run()
    {
        String grp_id="third_app";
        String topic="my-topic";
        //Creating consumer properties
        Properties properties =new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9094");
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,   StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG,grp_id);
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");
        //creating consumer

        KafkaConsumer<String,String> consumer= new KafkaConsumer<String,String>(properties);
        //Subscribing
        consumer.subscribe(Arrays.asList(topic));

        while(true){
            ConsumerRecords<String,String> records=consumer.poll(Duration.ofMillis(100));
            for(ConsumerRecord<String,String> record: records){
                System.out.println("Key: "+ record.key() + ", Value:" +record.value());
                System.out.println("Partition:" + record.partition()+",Offset:"+record.offset());
                Quote q = new Quote();
                q.setId(record.key());
                q.setStatus(record.value());
                quotes.put(record.key(),q);
            }
        }
    }

    public static void main(String args[])
    {

        QuotesConsumer quotesConsumer = getInstance();
        quotesConsumer.subscribe(); //subscribe forever
        System.out.println("finished");
    }

    Map<String,Quote> getQuotes()
    {
        return this.quotes;
    }

    QuotesConsumer subscribe()
    {
        String bootstrapServers="localhost:9092";
        String grp_id="third_app";



        //polling
        QuotesConsumer c = new QuotesConsumer();
        c.start();
        return c;

    }


}
