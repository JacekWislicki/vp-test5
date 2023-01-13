package com.example.test5.pulsar.producer;

import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.api.Schema;

import com.example.test5.commons.model.Message;
import com.example.test5.commons.utils.Config;

class OutTopicProducer extends AbstractProducer<Message> {

    OutTopicProducer(String topic) throws PulsarClientException {
        super(topic, Schema.AVRO(Message.class));
    }

    private Message buildMessage() {
        return new Message("OUT");
    }

    public static void main(String[] args) throws PulsarClientException {
        try (OutTopicProducer producer = new OutTopicProducer(Config.OUT_TOPIC_NAME);) {
            Message message = producer.buildMessage();
            producer.produce(message);
        }
    }
}
