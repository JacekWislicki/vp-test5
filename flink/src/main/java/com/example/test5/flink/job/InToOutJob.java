package com.example.test5.flink.job;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.connector.sink2.Sink;
import org.apache.flink.api.connector.source.Source;
import org.apache.flink.connector.pulsar.sink.PulsarSink;
import org.apache.flink.connector.pulsar.source.PulsarSource;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import com.example.test5.commons.model.Message;
import com.example.test5.commons.utils.Config;
import com.example.test5.flink.function.InToOutProcessFunction;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class InToOutJob extends BaseJob {

    private static final long serialVersionUID = 1L;

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment environment = prepareEnvironment();

        PulsarSource<Message> source = createPulsarSource(Config.IN_TOPIC_NAME, Config.IN_TOPIC_SUB, Message.class);
        PulsarSink<Message> sink = createPulsarSink(Config.OUT_TOPIC_NAME, Message.class);
        new InToOutJob(source, sink).build(environment);
        environment.execute();
    }

    private final Source<Message, ?, ?> pulsarSource;
    private final Sink<Message> sink;

    void build(StreamExecutionEnvironment environment) {
        DataStream<Message> pulsarStream =
            environment.fromSource(pulsarSource, WatermarkStrategy.noWatermarks(), "pulsar-source", TypeInformation.of(Message.class));

        pulsarStream
            .process(new InToOutProcessFunction())
            .sinkTo(sink);
    }
}
