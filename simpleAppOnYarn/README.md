## build
- mvn clean package

## Run
```
export YARN_ROOT_LOGGER=DEBUG,console
yarn --cluster xxx jar simpleAppOnYarn-1.0-SNAPSHOT.jar com.chandson.infra.Client -jar simpleAppOnYarn-1.0-SNAPSHOT.jar
```
