# azk-client

Azkaban client, written in Java, based on Azkaban REST APIs, for managing workflows easily.

## Features

Currently, it supports 3 `AzkCmd`s to manage prepared Azkaban workflows. We have described as in the following list:

- `publish` - Publish our azkaban workflows to the remote Azkaban Web Server.
- `exec`    - Execute a published flow manually.
- `delete`  - Delete a Azkaban workflow.

## How To

### Build

We manage this project by using Maven, you can build it by executing below command lines:

```
git clone https://github.com/shirdrn/azk-client.git
cd azk-client
mvn package
```

You should get packaged file `azk-client-0.0.1-SNAPSHOT-jar-with-dependencies.jar` in the `target` directory.
And then, you can execute the executable file for more information:

```
java -jar target/azk-client-0.0.1-SNAPSHOT-jar-with-dependencies.jar
```

The output looks like:

```
2017-10-12 15:24:26 INFO  AzkClient:45 - Input args: []
usage: deployer
 -c,--config <arg>   Specify required configuration file path.
 -h,--help           Print help detail information.
 -o,--overwrite      Specify whether overwrite published job.
 -t,--type <arg>     Specify operation type: publish|exec|delete
 ```

### Preparation works

Before using this tool, you should create a `properties` file, which includes required information about Azkaban server and our workflow.
For example, we prepare a configuration file `config.properties`:

 ```
 azkaban.server.url = http://172.16.117.62:8099
 azkaban.user = azkaban
 azkaban.password = azkaban
 project.name = test-demo-hourly
 project.description = test-demo-hourly
 job.zip.file = /Users/yanjun/Azkaban/demo/demo-syj.zip
 cron.expr = 0 45 * * * ?
 ```

You can find above file in `main/resources`.

### Publish an Azkaban workflow

Preparing and packaging an Azkaban `.zip` file is required, such as `/Users/yanjun/Azkaban/demo/demo-syj.zip`.
Then we can publish the workflow to the Azkaban server, issue the following command:

```
java -jar target/azkaban-app-deployer-0.0.1-SNAPSHOT-jar-with-dependencies.jar -t publish -c /Users/yanjun/Workspaces/github/azk-client/src/main/resources/config.properties
```

### Execute an Azkaban workflow manually

```
java -jar target/azkaban-app-deployer-0.0.1-SNAPSHOT-jar-with-dependencies.jar -t exec -c /Users/yanjun/Workspaces/github/azk-client/src/main/resources/config.properties
```

### Delete an Azkaban workflow

```
java -jar target/azkaban-app-deployer-0.0.1-SNAPSHOT-jar-with-dependencies.jar -t delete -c /Users/yanjun/Workspaces/github/azk-client/src/main/resources/config.properties
```

## References

- [http://azkaban.github.io/azkaban/docs/latest/#ajax-api](http://azkaban.github.io/azkaban/docs/latest/#ajax-api)

