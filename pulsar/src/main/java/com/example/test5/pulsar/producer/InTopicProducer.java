package com.example.test5.pulsar.producer;

import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.api.Schema;

import com.example.test5.commons.model.Message;
import com.example.test5.commons.utils.Config;

class InTopicProducer extends AbstractProducer<Message> {

    InTopicProducer(String topic) throws PulsarClientException {
        super(topic, Schema.AVRO(Message.class));
    }

    private Message buildMessage() {
        return new Message("IN");
    }

    public static void main(String[] args) throws PulsarClientException {
        try (InTopicProducer producer = new InTopicProducer(Config.IN_TOPIC_NAME);) {
            Message message = producer.buildMessage();
            producer.produce(message);
        }
    }
}
