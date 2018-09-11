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