# 2018-09-cijug
Code and examples from my September 2018 CIJUG presentation

## Tomcat

For this I used Tomcat 9.0.11, however just about any version that supports Java 8+ should work fine.

## Configuring Tomcat Jolokia User

By default jolokia 1.6+ requires http auth by default. You'll set this up by adding the jolokia role and a user with that role into $TOMCAT_HOME/conf/tomcat-users.xml like so:

```xml
<tomcat-users>
  <role rolename="jolokia"/>
  <user username="jolokia" password="jolokia" roles="jolokia"/>
</tomcat-users>
```

## Running hawt.io

To start hawt.io run the following command:
 
```bash
java -jar lib/hawtio-app-2.0.3.jar --port 8090`
```

You'll configure this to connect to your container. Assuming you're connecting to the running tomcat instance as it's configured here's what you'll set up:

```
Scheme: http
Host: localhost
Port: 8081
Path: jolokia
```

When you try to connect it will prompt you for a username and password. Use whatever you configured above.

## Running telegraf

I installed using homebrew, and ran telegraf using the config file shown like so:

```bash
$ brew install telegraf
$ telegraf -config telegraf/telegraf.conf
```

Normally you'd run it as a service, but here we'll just run the command so we can specify config.

## Running influxdb

First create a network because we've got other container that will need to connect to this

```bash
docker network create influxdb
```

Then start influxdb

```bash
$ docker run -d --rm --name influxdb --net=influxdb -p 8086:8086 \
    -e INFLUXDB_ADMIN_USER=admin -e INFLUXDB_ADMIN_PASSWORD=password \
    -e INFLUXDB_USER=cijug -e INFLUXDB_USER_PASSWORD=password \
    -e INFLUXDB_DB=cijug \
    -v $PWD/influxdb:/var/lib/influxdb \
    influxdb
```

## Running Chronograf

```bash
$ docker run -d --rm --name chronograf --net=influxdb -p 8888:8888 \
    --volume "$PWD/chronograf:/var/lib/chronograf" \
    chronograf --influxdb-url=http://influxdb:8086
```

## Running grafana

```bash
$ docker run -d --rm --name grafana --net=influxdb -p 3000:3000\
    --volume "$PWD/grafana:/var/lib/grafana" \
    grafana/grafana:5.1.0
```

## Accessing all-the-things

Now that you've done all of that you should have all of the following things running:

### Example deployed application

URL: http://localhost:8081/hello/
Jolokia: http://localhost:8081/jolokia
Hawtio: http://localhost:8090/hawtio/
Influxdb: http://localhost:8086/debug/vars (this might be meaningless, but you should see lots of output)
Chronograf: http://localhost:8888
Grafana: http://localhost:3000

And now that all of those are running if you have telegraf running we're pushing metrics into our database.

### Example stand alone app
URL: http://localhost:8082
API Endopint: http://localhost:8082/api/greeting
Slow API Endpoint: http://localhost:8082/api/slow-greeting
Actuator metrics: http://localhost:8082/actuator/metrics/
Health Endpoint: http://localhost:8082/actuator/health
