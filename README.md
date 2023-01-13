# Reproduction of https://issues.apache.org/jira/browse/FLINK-30681
## Create Topics and Schemas
### Message Schema
Copy test5.json (the schema file) to /pulsar/schemas (or update the scripts below for another location)
### IN topic
```bash 
./pulsar-admin topics create-partitioned-topic --partitions 1 persistent://public/default/test5-in
./pulsar-admin topics create-subscription persistent://public/default/test5-in -s test5-in-flink
./pulsar-admin schemas upload \
    --filename /pulsar/schemas/test5.json \
    persistent://public/default/test5-in
```
### OUT topic
```bash 
./pulsar-admin topics create-partitioned-topic --partitions 1 persistent://public/default/test5-out
./pulsar-admin topics create-subscription persistent://public/default/test5-out -s test5-out-pulsar
./pulsar-admin schemas upload \
    --filename /pulsar/schemas/test5.json \
    persistent://public/default/test5-out
```
### Cleanup (execute to re-create the initial state)
```bash
./pulsar-admin topics unload persistent://public/default/test5-in
./pulsar-admin topics delete-partitioned-topic persistent://public/default/test5-in
./pulsar-admin schemas delete persistent://public/default/test5-in

./pulsar-admin topics unload persistent://public/default/test5-out
./pulsar-admin topics delete-partitioned-topic persistent://public/default/test5-out
./pulsar-admin schemas delete persistent://public/default/test5-out
```
## Problem Reproduction
Both the Flink job and the Pulsar function can be packaged deployed to any environment, still the steps below describe running them from IDE:
1. Start the Pulsar function: debug (or run) com.example.test5.pulsar.function.OutReadFunctionMain.
2. Start the Flink job: debug (or run) com.example.test5.flink.job.InToOutJob.
3. Produce a massage to the OUT topic with com.example.test5.pulsar.producer.OutTopicProducer
4. OutReadFunction receives and processes the message
5. Produce a massage to the IN topic with com.example.test5.pulsar.producer.InTopicProducer
6. InToOutJob receives, processes and writes the message to OUT
7. OutReadFunction does not receive the last message
8. Producing any other message to OUT (from OutTopicProducer or via InToOutJob) does not result moving the ledger position for OUT.
