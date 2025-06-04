package com.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.config.TopicConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic fraudAlertsTopic() {
        return TopicBuilder.name("fraud-alerts")
                .partitions(3)
                .replicas(1)
                .config(TopicConfig.RETENTION_MS_CONFIG, "604800000")
                .build();
    }

    @Bean
    public NewTopic transactionEventsTopic() {
        return TopicBuilder.name("transaction-events")
                .partitions(3)
                .replicas(1)
                .build();
    }
}