package com.example.test5.commons.utils;

public class Config {

    public static final String VP_LOCAL_ENV = "local";
    public static final String VP_ENV = "vp.environment";

    public static final String SERVICE_URL = "pulsar://localhost:6650";
    public static final String ADMI_URL = "http://localhost:8080";

    public static final String IN_TOPIC_NAME = "persistent://public/default/test5-in";
    public static final String IN_TOPIC_SUB = "test5-in-flink";

    public static final String OUT_TOPIC_NAME = "persistent://public/default/test5-out";
    public static final String OUT_TOPIC_SUB = "test5-out-pulsar";

    private Config() {}
}
