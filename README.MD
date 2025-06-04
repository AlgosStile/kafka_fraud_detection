После того как код приложения на java будет реализован переходим к созданию SSL

Создайте SSL сертификаты в директории docker/ssl:

```bash
# Генерация CA
openssl req -new -x509 -keyout ca.key -out ca.crt -days 365 -subj "/CN=Kafka-CA" -passout pass:changeit

# Генерация сертификата для Kafka
keytool -keystore kafka.jks -alias kafka -validity 365 -genkey -keyalg RSA -storepass changeit -keypass changeit -dname "CN=kafka"

# Создание CSR
keytool -keystore kafka.jks -alias kafka -certreq -file cert-file -storepass changeit

# Подпись сертификата
openssl x509 -req -CA ca.crt -CAkey ca.key -in cert-file -out cert-signed -days 365 -CAcreateserial -passin pass:changeit

# Импорт CA и сертификата в хранилище
keytool -keystore kafka.jks -alias CARoot -import -file ca.crt -storepass changeit -noprompt
keytool -keystore kafka.jks -alias kafka -import -file cert-signed -storepass changeit

# Создание truststore
keytool -keystore truststore.jks -alias CARoot -import -file ca.crt -storepass changeit -noprompt
```

## Запуск приложения

```bash
# Удаление контейнеров
docker-compose down

# Запуск контейнеров
docker-compose up -d

# Переходим в директорию docker и запускаем скрипт
./register-connector.sh

# Далее запускаем Spring Boot приложение в FraudDetectionApplication

```

### Если необходима полная перезагрузка:

```bash
docker-compose down -v --remove-orphans
docker system prune -a -f
docker-compose up -d
````


## Тестирование работы:

 - Отправка тестовых транзакций
 ```bash
curl -X POST http://localhost:8080/transactions -H "Content-Type: application/json" -d '{
"amount": 1500.00,
"currency": "USD",
"userId": 12345
}'

# Проверка логов Kafka Streams
docker-compose logs kafka | grep "Fraud detected"
 ```