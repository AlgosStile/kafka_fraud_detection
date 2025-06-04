#!/bin/bash

echo "Waiting for Connect to start..."
while ! curl -s http://localhost:8083/ > /dev/null; do
  sleep 7
done
echo "Connect is ready!"

curl -X POST http://localhost:8083/connectors -H "Content-Type: application/json" -d '{
  "name": "transaction-connector",
  "config": {
    "connector.class": "io.debezium.connector.postgresql.PostgresConnector",
    "database.hostname": "postgres",
    "database.port": "5432",
    "database.user": "postgres",
    "database.password": "postgres",
    "database.dbname": "transactions_db",
    "topic.prefix": "fraud_db_server",
    "table.include.list": "public.transaction",
    "plugin.name": "pgoutput",
    "tombstones.on.delete": "false",
    "transforms": "unwrap",
    "transforms.unwrap.type": "io.debezium.transforms.ExtractNewRecordState",
    "transforms.unwrap.drop.tombstones": "false"
  }
}'

echo -e "\n\nConnector registered. Checking status..."
curl http://localhost:8083/connectors/transaction-connector/status