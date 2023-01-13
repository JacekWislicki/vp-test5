package com.example.test5.flink.job;

import java.io.Serializable;
import java.util.Arrays;

import org.apache.flink.api.common.RuntimeExecutionMode;
import org.apache.flink.api.connector.sink2.Sink;
import org.apache.flink.api.connector.source.Source;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.MemorySize;
import org.apache.flink.configuration.TaskManagerOptions;
import org.apache.flink.connector.base.DeliveryGuarantee;
import org.apache.flink.connector.pulsar.sink.PulsarSink;
import org.apache.flink.connector.pulsar.sink.writer.serializer.PulsarSerializationSchema;
import org.apache.flink.connector.pulsar.source.PulsarSource;
import org.apache.flink.connector.pulsar.source.PulsarSourceOptions;
import org.apache.flink.connector.pulsar.source.enumerator.cursor.StartCursor;
import org.apache.flink.connector.pulsar.source.reader.deserializer.PulsarDeserializationSchema;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.pulsar.client.api.Schema;
import org.apache.pulsar.client.api.SubscriptionType;

import com.example.test5.commons.utils.Config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuppressWarnings("java:S119") //not single letter type identifier
public abstract class BaseJob implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final String DEFAULT_MEMORY = "128MB";

    protected static StreamExecutionEnvironment prepareEnvironment() {
        StreamExecutionEnvironment environment;
        String environmentName = System.getenv(Config.VP_ENV);
        if (Config.VP_LOCAL_ENV.equals(environmentName)) {
            var configuration = new Configuration();
            configuration.set(TaskManagerOptions.NETWORK_MEMORY_FRACTION, 0.2f);
            configuration.set(TaskManagerOptions.NETWORK_MEMORY_MIN, MemorySize.parse(DEFAULT_MEMORY));
            configuration.set(TaskManagerOptions.NETWORK_MEMORY_MAX, MemorySize.parse(DEFAULT_MEMORY));
            environment = StreamExecutionEnvironment.createLocalEnvironment(configuration);
        } else {
            environment = StreamExecutionEnvironment.getExecutionEnvironment();
        }
        environment.setRuntimeMode(RuntimeExecutionMode.STREAMING);
        return environment;
    }

    public static <OUT> PulsarSink<OUT> createPulsarSink(String topic, Class<OUT> outClass) {
        return PulsarSink.builder()
            .setAdminUrl(Config.ADMI_URL)
            .setServiceUrl(Config.SERVICE_URL)
            .setTopics(Arrays.asList(topic))
            .setSerializationSchema(PulsarSerializationSchema.pulsarSchema(Schema.AVRO(outClass), outClass))
            .setDeliveryGuarantee(DeliveryGuarantee.EXACTLY_ONCE) // requires Pulsar transactions enabled (transactionCoordinatorEnabled=true)
            .build();
    }

    public static <IN> PulsarSource<IN> createPulsarSource(String topic, String subscription, Class<IN> inClass) {
        return PulsarSource.builder()
            .setAdminUrl(Config.ADMI_URL)
            .setServiceUrl(Config.SERVICE_URL)
            .setStartCursor(StartCursor.earliest())
            .setTopics(topic)
            .setDeserializationSchema(PulsarDeserializationSchema.pulsarSchema(Schema.AVRO(inClass), inClass))
            .setSubscriptionName(subscription)
            .setSubscriptionType(SubscriptionType.Shared)
            .setConfig(PulsarSourceOptions.PULSAR_AUTO_COMMIT_CURSOR_INTERVAL, 1000L)
            .setConfig(PulsarSourceOptions.PULSAR_ACK_RECEIPT_ENABLED, true)
            .setConfig(PulsarSourceOptions.PULSAR_ENABLE_AUTO_ACKNOWLEDGE_MESSAGE, true)
            .build();
    }

    @SuppressWarnings("unchecked")
    protected <T> Sink<T> enforceSinkType(Sink<?> sink) {
        return (Sink<T>) sink;
    }

    @SuppressWarnings("unchecked")
    protected <T> Source<T, ?, ?> enforceSourceType(Source<?, ?, ?> source) {
        return (Source<T, ?, ?>) source;
    }
}
