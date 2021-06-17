# General Commands

- To create a topic:
```bash
/bin/kafka-topics \
  --create \
  --topic [TOPIC_NAME] \
  --bootstrap-server kafka-[BROKER_ID]:9092
```

- To create a consumer from a topic:
```bash
/bin/kafka-console-consumer \
  --topic [TOPIC_NAME] \
  --from-beginning \
  --bootstrap-server kafka-[BROKER_ID]:9092
```

- To produce messages to a topic:
```bash
/bin/kafka-console-producer \
  --topic [TOPIC_NAME] \
  --bootstrap-server kafka-[BROKER_ID]:9092
```

- To consume from a group:
```bash
/bin/kafka-console-consumer \
  --bootstrap-server kafka-[BROKER_ID]:9092 \
  --topic [TOPIC_NAME] \
  --group [GROUP_NAME]
```

- To show group details:
```bash
/bin/kafka-consumer-groups \
  --bootstrap-server kafka-[BROKER_ID]:9092 \
  --group [GROUP_NAME] \
  --describe
```

- To connect to Zookeeper:
```bash
/bin/zookeeper-shell zookeeper-[SERVER_ID]:2181/kafka
```

## Testing the Kafka Cluster:

1. Create a file with random data:

```bash
base64 /dev/urandom | head --bytes 10000 | egrep --text --only-matching "\w" | tr --delete '\n' > random_data_file.txt
```

2. Start producing to a topic:

```bash
/bin/kafka-producer-perf-test \
  --topic [TOPIC_NAME] \
  --num-records 10000 \
  --throughput 10 \
  --payload-file random_data_file.txt \
  --producer-props \
    acks=1 \
    bootstrap.servers=kafka-1:9092,kafka-2:9092,kafka-3:9092 \
  --payload-delimiter A
```

3. Stop some of the brokers containers, and check the topic details:

```bash
/bin/kafka-topics \
  --bootstrap-server kafka-[BROKER_ID]:9092 \
  --topic [TOPIC_NAME] \
  --describe
```
