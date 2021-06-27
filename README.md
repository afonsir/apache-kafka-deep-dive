# General Kafka commands

- To create a topic:
```bash
/bin/kafka-topics \
  --bootstrap-server kafka-[BROKER_ID]:9092 \
  --create \
  --topic [TOPIC_NAME]
```

- To create a consumer from a topic:
```bash
/bin/kafka-console-consumer \
  --bootstrap-server kafka-[BROKER_ID]:9092 \
  --topic [TOPIC_NAME] \
  --from-beginning
```

- To produce messages to a topic:
```bash
/bin/kafka-console-producer \
  --bootstrap-server kafka-[BROKER_ID]:9092 \
  --topic [TOPIC_NAME]
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

## More topic commands

- Create a topic if a topic with the same name does NOT exist:

```bash
/bin/kafka-topics \
  --bootstrap-server kafka-[BROKER_ID]:9092 \
  --create \
  --topic [TOPIC_NAME] \
  --replication-factor 1 \
  --partitions 3 \
  --if-not-exists
```

- Alter the number of partitions (can only go up):

```bash
/bin/kafka-topics \
  --bootstrap-server kafka-[BROKER_ID]:9092 \
  --alter \
  --topic [TOPIC_NAME] \
  --partitions 6
```

- Delete a topic (this is irreversible):

```bash
/bin/kafka-topics \
  --bootstrap-server kafka-[BROKER_ID]:9092 \
  --delete \
  --topic [TOPIC_NAME]
```

- List all topics:

```bash
/bin/kafka-topics \
  --bootstrap-server kafka-[BROKER_ID]:9092 \
  --list
```

- Describe all the topics at once:

```bash
/bin/kafka-topics \
  --bootstrap-server kafka-[BROKER_ID]:9092 \
  --describe
```

- Identify any overrides to topics (configs added to the defaults):

```bash
/bin/kafka-topics \
  --bootstrap-server kafka-[BROKER_ID]:9092 \
  --describe \
  --topics-with-overrides
```

- Topics that are not in-sync with all replicas:

```bash
/bin/kafka-topics \
  --bootstrap-server kafka-[BROKER_ID]:9092 \
  --describe \
  --under-replicated-partitions
```

- Topics without a leader replica:

```bash
/bin/kafka-topics \
  --bootstrap-server kafka-[BROKER_ID]:9092 \
  --describe \
  --unavailable-partitions
```

- Describe the configurations for all topics (only in addition to the defaults):

```bash
/bin/kafka-configs \
  --bootstrap-server kafka-[BROKER_ID]:9092 \
  --describe \
  --entity-type topics
```

- Describe the configurations for a specific topic (defaults will not show):

```bash
/bin/kafka-configs \
  --bootstrap-server kafka-[BROKER_ID]:9092 \
  --describe \
  --entity-type topics \
  --entity-name [TOPIC_NAME]
```

- Change the topics message retention:

```bash
/bin/kafka-configs \
  --bootstrap-server kafka-[BROKER_ID]:9092 \
  --alter \
  --entity-type topics \
  --entity-name [TOPIC_NAME] \
  --add-config retention.ms=3600000
```

- Describe the configurations for all brokers (defaults will not show):

```bash
/bin/kafka-configs \
  --bootstrap-server kafka-[BROKER_ID]:9092 \
  --entity-type brokers \
  --entity-default \
  --describe
```

- Describe the configuration for BROKER_ID (defaults will not show):

```bash
/bin/kafka-configs \
  --bootstrap-server kafka-[BROKER_ID]:9092 \
  --entity-type brokers \
  --entity-name [BROKER_ID] \
  --describe
```

- Add a custom config to BROKER_ID that will change it's log cleaner thread count:

```bash
/bin/kafka-configs \
  --bootstrap-server kafka-[BROKER_ID]:9092 \
  --entity-type brokers \
  --entity-name [BROKER_ID] \
  --alter \
  --add-config log.cleaner.threads=2
```

- Remove all custom configs (not including defaults) from BROKER_ID:

```bash
/bin/kafka-configs \
  --bootstrap-server kafka-[BROKER_ID]:9092 \
  --entity-type brokers \
  --entity-name [BROKER_ID] \
  --alter \
  --delete-config log.cleaner.threads
```

## More consumer group commands:

- List all the consumer groups:

```bash
/bin/consumer-groups \
  --bootstrap-server kafka-[BROKER_ID]:9092 \
  --list
```

- Describe a specific consumer group:

```bash
/bin/consumer-groups \
  --bootstrap-server kafka-[BROKER_ID]:9092 \
  --describe \
  --group [GROUP_NAME]
```

- Describe the active members of the group:

```bash
/bin/consumer-groups \
  --bootstrap-server kafka-[BROKER_ID]:9092 \
  --describe \
  --group [GROUP_NAME] \
  --members
```

- If the group has active members, get a more verbose output:

```bash
/bin/consumer-groups \
  --bootstrap-server kafka-[BROKER_ID]:9092 \
  --describe \
  --group [GROUP_NAME] \
  --members \
  --verbose
```

- Describe the state of the group:

```bash
/bin/consumer-groups \
  --bootstrap-server kafka-[BROKER_ID]:9092 \
  --describe \
  --group [GROUP_NAME] \
  --state
```

- Delete a consumer group (only works if there are no active members):

```bash
/bin/consumer-groups \
  --bootstrap-server kafka-[BROKER_ID]:9092 \
  --delete \
  --group [GROUP_NAME]
```

- Delete multiple consumer groups:

```bash
/bin/consumer-groups \
  --bootstrap-server kafka-[BROKER_ID]:9092 \
  --delete \
  --group [GROUP_NAME] \
  --group [GROUP_NAME]
```

- Reset offsets for a consumer group:

```bash
/bin/consumer-groups \
  --bootstrap-server kafka-[BROKER_ID]:9092 \
  --reset-offsets \
  --group [GROUP_NAME] \
  --topic [TOPIC_NAME] \
  --to-latest
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

# Consuming from a SQLite Database

- Create Kafka Consumer as a Docker container:

```bash
docker container run \
  --tty \
  --interactive \
  --rm \
  --name sqlite-demo \
  --network host \
  confluentinc/docker-demo-base:3.3.0
```

- Start the container service:

```bash
cd /tmp && confluent start
```

- Install SQLite:

```bash
apt-get update --quiet && apt-get install --quiet --yes sqlite3
```

- Create and populate the database:

```
sqlite3 test.db

CREATE TABLE accounts (
  id   INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
  name VARCHAR(255)
);

INSERT INTO accounts(name) VALUES('chad');
INSERT INTO accounts(name) VALUES('terry');

.quit
```

- Restart the connect service:

```bash
confluent stop connect
```

- Start connect service in standalone mode, with SQLite default configurations:

```bash
connect-standalone \
  -daemon /etc/schema-registry/connect-avro-standalone.properties \
  /etc/kafka-connect-jdbc/source-quickstart-sqlite.properties
```

- Check the connect service:

```bash
cat /logs/connectStandalone.out |grep -i "finished"

curl --silent http://localhost:8083/connectors
```

- Check if the topic was create:

```bash
kafka-topics --list --zookeeper localhost:2181 | grep test-sqlite-jdbc
```

- Create a new consumer:

```bash
kafka-avro-console-consumer \
  --new-consumer \
  --bootstrap-server localhost:9092 \
  --topic test-sqlite-jdbc-accounts \
  --from-beginning
```

- In another shell session, insert a new record to the database:

```bash
docker exec --tty --interactive sqlite-demo /bin/bash

cd /tmp && sqlite3 test.db

INSERT INTO accounts(name) VALUES('william');

.quit
```
