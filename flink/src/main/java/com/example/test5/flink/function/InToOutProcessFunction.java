package com.example.test5.flink.function;

import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;

import com.example.test5.commons.model.Message;

public class InToOutProcessFunction extends ProcessFunction<Message, Message> {

    private static final long serialVersionUID = 1L;

    @Override
    public void processElement(Message value, ProcessFunction<Message, Message>.Context ctx, Collector<Message> out) throws Exception {
        value.setField03("PROCESSED");
        out.collect(value);
    }
}
