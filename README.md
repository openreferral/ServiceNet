# ServiceNet
This application was generated using JHipster 5.6.1, you can find documentation and help at [https://www.jhipster.tech/documentation-archive/v5.6.1](https://www.jhipster.tech/documentation-archive/v5.6.1).

##Setting up the environment

To run the application locally you will need to install PostgreSQL, MongoDB and Java 11.
Create a new database named ServiceNet in PostgreSQL and set up the following environment variables:
 * JAVA_HOME
 * GOOGLE_API_KEY
 
Optionally, you can install JHipster generator with the following command:

```bash
npm install -g generator-jhipster
```

## IntelliJ Idea development

1. Install Lombok, MapStruct nad NodeJS plugins under File -> Settings -> Plugins -> Browse repositories... search for the Lombok, MapStruct support and NodeJS plugins and install them all.
2. Check the Enable annotation processing checkbox under File -> Settings -> Build, Execution, Deployment -> Compiler -> Annotation Processors.
3. Import google styleguide settings under File -> Settings -> Editor -> Code Style, click on the little gear next to Scheme. Click on import Scheme and select the google-styleguide file (https://raw.githubusercontent.com/google/styleguide/gh-pages/intellij-java-google-style.xml) as the current code style for the project.

## Development

To start your application in the dev profile, simply run:

```bash
./mvnw
```

For further instructions on how to develop with JHipster, have a look at [Using JHipster in development][].

## Building for production

To optimize the ServiceNet application for production, run:

```bash
./mvnw -Pprod clean package
```

To ensure everything worked, run:

```bash
java -jar target/*.war
```

Refer to [Using JHipster in production][] for more details.

## UI Development
To enable live reloading of your client-side code, run:
```bash
npm start
```

## Testing

To launch your application's tests, run:

```bash
./mvnw clean test
```

For more information, refer to the [Running tests page][].

### Code quality

Sonar is used to analyse code quality. You can start a local Sonar server (accessible on http://localhost:9001) with:

```bash
docker-compose -f src/main/docker/sonar.yml up -d
```

Then, run a Sonar analysis:

```bash
./mvnw -Pprod clean test sonar:sonar
```

For more information, refer to the [Code quality page][].

## Using Docker to simplify development (optional)

You can use Docker to improve your JHipster development experience. A number of docker-compose configuration are available in the [src/main/docker](src/main/docker) folder to launch required third party services.

For example, to start a  database in a docker container, run:

```bash
docker-compose -f src/main/docker/postgresql.yml up -d
```

To stop it and remove the container, run:

```bash
docker-compose -f src/main/docker/postgresql.yml down
```

You can also fully dockerize your application and all the services that it depends on.
To achieve this, first build a docker image of your app by running:

```bash
./mvnw package -Pprod jib:dockerBuild
```

Then run:

```bash
docker-compose -f src/main/docker/app.yml up -d
```

For more information refer to [Using Docker and Docker-Compose][], this page also contains information on the docker-compose sub-generator (`jhipster docker-compose`), which is able to generate docker configurations for one or several JHipster applications.

## Continuous Integration (optional)

To configure CI for your project, run the ci-cd sub-generator (`jhipster ci-cd`), this will let you generate configuration files for a number of Continuous Integration systems. Consult the [Setting up Continuous Integration][] page for more information.

[JHipster Homepage and latest documentation]: https://www.jhipster.tech
[JHipster 5.6.1 archive]: https://www.jhipster.tech/documentation-archive/v5.6.1

[Using JHipster in development]: https://www.jhipster.tech/documentation-archive/v5.6.1/development/
[Using Docker and Docker-Compose]: https://www.jhipster.tech/documentation-archive/v5.6.1/docker-compose
[Using JHipster in production]: https://www.jhipster.tech/documentation-archive/v5.6.1/production/
[Running tests page]: https://www.jhipster.tech/documentation-archive/v5.6.1/running-tests/
[Code quality page]: https://www.jhipster.tech/documentation-archive/v5.6.1/code-quality/
[Setting up Continuous Integration]: https://www.jhipster.tech/documentation-archive/v5.6.1/setting-up-ci/
