# Система мониторинга с использованием Spring Kafka

### Описание проекта:
Система мониторинга, которая отслеживает работу различных компонентов приложения с помощью Spring Kafka. Эта система включает в себя Producer для отправки метрик, Consumer для их обработки и анализа, а также REST API для просмотра метрик.

### REST API
URL: http://localhost:8080

- ```POST /send``` - отправка всех метрик.

URL: http://localhost:8081

- ```GET /metrics/``` - получение всех метрик.
- ```GET /metrics/{id}``` - получение метрики по Id.
- ```POST /metrics/save``` - сохранение метрики.
- ```DELETE /metrics/deleteAll``` - удаление всех метрик.

### Запуск приложения:
- Клонировать проект в среду разработки.
- Настроить подключение к базе данных в файле application.properties.
- Запустить метод ```main``` в файле ```ConsumerServiceApplication.java```
- Запустить метод ```main``` в файле ```ProducerServiceApplication.java```
- Запустить файл ```docker-compose.yml```
- После этого будут доступны эндпоинты для отправки и обработки метрик.
### Технологии, используемые в проекте:
```
- Java 17
- Spring Boot
- Spring Data JPA
- Spring Kafka
- SpringDoc
- Maven
- REST API
- Lombok
- PostgreSQL
- Liquibase
```