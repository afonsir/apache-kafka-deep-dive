# General Kafka commands

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

# Generating a Java project to use Kafka

- Create Maven project:

```bash
mvn --batch-mode archetype:generate \
  --define archetypeGroupId=org.apache.maven.archetypes \
  --define groupId=com.github.afonsir \
  --define artifactId=kafka-app
```

- Add the following dependencies to **pom.xml** file:

```xml
<!-- https://mvnrepository.com/artifact/org.apache.kafka/kafka-clients -->
<dependency>
  <groupId>org.apache.kafka</groupId>
  <artifactId>kafka-clients</artifactId>
  <version>2.2.1</version>
</dependency>

<!-- https://mvnrepository.com/artifact/org.slf4j/slf4j-simple -->
<dependency>
  <groupId>org.slf4j</groupId>
  <artifactId>slf4j-simple</artifactId>
  <version>1.7.26</version>
</dependency>
```

- Compile the application sources:

```bash
mvn compile
```

- To test a class:

```bash
mvn exec:java --define exec.mainClass="com.github.afonsir.Producer"
```
