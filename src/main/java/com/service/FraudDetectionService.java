package com.service;

import com.model.Transaction;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class FraudDetectionService {

    private static final String INPUT_TOPIC = "fraud_db_server.public.transaction";
    private static final Duration WINDOW_SIZE = Duration.ofMinutes(5);
    private static final String OUTPUT_TOPIC = "fraud-alerts";

    @Bean
    public KStream<Windowed<String>, String> kStream(StreamsBuilder builder) {
        JsonSerde<Transaction> transactionSerde = new JsonSerde<>(Transaction.class);
        Serde<String> stringSerde = Serdes.String();

        KStream<String, Transaction> stream = builder.stream(
                INPUT_TOPIC,
                Consumed.with(stringSerde, transactionSerde)
        );


        KTable<Windowed<String>, Long> counts = stream
                .filter((key, tx) -> tx.getStatus() == Transaction.Status.NEW)
                .selectKey((key, tx) -> tx.getUserId().toString())
                .groupByKey(Grouped.with(Serdes.String(), transactionSerde))
                .windowedBy(TimeWindows.ofSizeWithNoGrace(WINDOW_SIZE))
                .count();


        counts.toStream()
                .filter((windowedKey, count) -> count >= 3)
                .map((windowedKey, count) ->
                        new KeyValue<>(
                                windowedKey.key(),
                                "Fraud detected: " + count + " User: " + windowedKey.key()
                        )
                )
                .to(OUTPUT_TOPIC, Produced.with(stringSerde, stringSerde));

        return counts.toStream().mapValues(Object::toString);
    }
}