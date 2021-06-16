- To create a topic named **topic-name**:
```bash
/bin/kafka-topics \
  --create \
  --topic topic-name \
  --bootstrap-server localhost:9092
```

- To create a consumer from **topic-name** topic:
```bash
/bin/kafka-console-consumer \
  --topic topic-name \
  --from-beginning \
  --bootstrap-server localhost:9092
```

- To produce messages to the **topic-name** topic:
```bash
/bin/kafka-console-producer \
  --topic topic-name \
  --bootstrap-server localhost:9092
```
