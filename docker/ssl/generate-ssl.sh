#!/bin/bash

# Создаем CA
openssl req -new -x509 -keyout ca.key -out ca.crt -days 365 -subj "/CN=myCA" -passout pass:changeit

# Создаем хранилища
keytool -keystore kafka.server.truststore.jks -alias CARoot -import -file ca.crt -storepass changeit -noprompt
keytool -keystore kafka.server.keystore.jks -alias localhost -validity 365 -genkey -keyalg RSA -storepass changeit -dname "CN=kafka"

# Подписываем сертификат
keytool -keystore kafka.server.keystore.jks -alias localhost -certreq -file cert-file -storepass changeit
openssl x509 -req -CA ca.crt -CAkey ca.key -in cert-file -out cert-signed -days 365 -CAcreateserial -passin pass:changeit
keytool -keystore kafka.server.keystore.jks -alias CARoot -import -file ca.crt -storepass changeit -noprompt
keytool -keystore kafka.server.keystore.jks -alias localhost -import -file cert-signed -storepass changeit -noprompt

# Создаем файл с паролем
echo "changeit" > ssl_creds

# JAAS config
cat > jaas.config <<EOF
KafkaServer {
  org.apache.kafka.common.security.plain.PlainLoginModule required
  username="admin"
  password="admin-secret"
  user_admin="admin-secret";
};
EOF