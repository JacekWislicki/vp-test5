package com.example.test5.pulsar.function;

import org.apache.pulsar.functions.api.Context;
import org.apache.pulsar.functions.api.Function;

import com.example.test5.commons.model.Message;

public class OutReadFunction implements Function<Message, Void> {

    @Override
    public Void process(Message input, Context context) throws Exception {
        System.out.println(">>>>>>>>>>>>> " + input);
        return null;
    }
}