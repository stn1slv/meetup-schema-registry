# Schema Registry Demo

![Demo case](pics/SchemaRegistryDemo.svg)

## Preparing
You have to add some entries to your ```/etc/hosts``` file:
```
127.0.0.1            keycloak
127.0.0.1            kafka
127.0.0.1            schema-registry
```
That's needed for host resolution because Kafka brokers and Kafka clients connecting to Keycloak have to use the same hostname to ensure the compatibility of generated access tokens. Also, when Kafka client connects to Kafka broker running inside docker image, the broker will redirect the client to ```kafka:9092```.

##### MacOS
Required software:
- Docker engine & docker compose (Docker Desktop for Mac or [Rancher Desktop](https://github.com/rancher-sandbox/rancher-desktop/))
- [Git](https://github.com/git/git)
- [Apache Maven](https://github.com/apache/maven)
- [httpie](https://github.com/httpie/httpie) (or any other http client)
- [kcat](https://github.com/edenhill/kcat) (or any other kafka client)

You can install using following command:
```
brew install git httpie kcat maven
```

##### Linux
Required software:
- Docker engine & docker compose
- [Git](https://github.com/git/git)
- [Apache Maven](https://github.com/apache/maven)
- [httpie](https://github.com/httpie/httpie) (or any other http client)
- [kcat](https://github.com/edenhill/kcat) (or any other kafka client)

On Ubuntu you can install last three tools using the following command:
```
sudo apt update && sudo apt install git httpie kafkacat maven -y
```

##### Windows
Required software:
- Docker Desktop for Windows
- [Git](https://git-scm.com/download/win) 
- [Apache Maven](https://github.com/apache/maven)
- Windows Subsystem for Linux (for httpie and kcat/kafkacat)

## Start environment
1. Clone this repository
```
git clone https://github.com/stn1slv/schema-registry-demo
```
2. Use root directory of the repo
```
cd schema-registry-demo
```
3. Cleanup
```
docker rm keycloak kafka zookeeper schema-registry sr-init kc-init
```
4. Start KeyCloak, Apicurio Registry and Kafka
```
docker-compose up
```

## Start demo applications
1. Start ServerA app
```
mvn clean spring-boot:run -f ServerA/pom.xml
```
2. Start ServerB app
```
mvn clean spring-boot:run -f ServerB/pom.xml
```

## Testing
#### JSON Schema
###### Send JSON message via http endpoint
John made a purchase of item 20223 for Jane:
```
cat examples/purchaseOrderV1_Alice.json | http POST 'http://localhost:8085/doSomething' Content-Type:'application/json'
```
Alice sent a present to Bob:
```
cat examples/purchaseOrderV1_John.json | http POST 'http://localhost:8085/doSomething' Content-Type:'application/json'
```
#### XML Schema
###### Send XML message to Input topic in Kafka
John made a purchase of item 20223 for Jane:
```
kcat -P -b 127.0.0.1 -t input examples/purchaseOrderV1_Alice.xml
```
Alice sent a present to Bob:
```
kcat -P -b 127.0.0.1 -t input examples/purchaseOrderV1_John.xml
```
###### Monitor messages in DLQ topic
In case of validation failure the message will be moved  to dlq topic:
```
kcat -b 127.0.0.1 -t dlq -f '\nKey: %k\t\nHeaders: %h \t\nValue: %s\\n--\n'
```
###### Monitor messages in Output topic
In case of successful validation the message will be moved to output topic:
```
kcat -b 127.0.0.1 -t output
```
