package com.example.test5.pulsar.function;

import java.util.Arrays;

import org.apache.pulsar.common.functions.FunctionConfig;
import org.apache.pulsar.functions.LocalRunner;

import com.example.test5.commons.utils.Config;

public class OutReadFunctionMain {

    public static void main(String[] args) throws Exception {
        FunctionConfig functionConfig = new FunctionConfig();
        functionConfig.setName(OutReadFunction.class.getSimpleName());
        functionConfig.setInputs(Arrays.asList(Config.OUT_TOPIC_NAME));
        functionConfig.setSubName(Config.OUT_TOPIC_SUB);
        functionConfig.setClassName(OutReadFunction.class.getName());
        functionConfig.setRuntime(FunctionConfig.Runtime.JAVA);

        LocalRunner localRunner = LocalRunner.builder().functionConfig(functionConfig).build();
        localRunner.start(false);
    }
}