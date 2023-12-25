# app-revenues-service
This service was created following the rules of a technical challenge.

## Tools needed
Those are the tools needed to run the project:
* [Docker](https://www.docker.com/products/docker-desktop/)
* [Maven](https://maven.apache.org/download.cgi)
* [Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)

*>It's highly suggested to use IntelliJ as IDE, since it's going to abstract the needs to install Java and Maven, making necessary just to download and install docker.*

## Set up before starting service
Before starting service you have to:
* Paste your app-financial-metrics entries inside the file in /src/main/resources/input/metric/app-financial-metrics.csv

*sorry for that*

## Starting service
This service uses Spring Boot Docker Compose, so once it starts to boot it'll also build all the necessary containers to do that. Follow those commands to start the project:

``mvn clean install spring-boot:run``

If you want to force the dependency downloads, run the same command with ``-U`` argument.

``mvn clean install -U spring-boot:run``

After the service is booted, two new folders will be created in the root project:
* input/company
* output

In order to start the job you'll need to move a csv file with the app companies to /input/company, doesn't need to have any particular name, just be a *.csv in the right format.

>After the job is finished a result file will be generated into the output folder.

*>There's no need to use `docker-compose up`, since the project is using **spring-boot-docker-compose**.*

## How to run the project with new Financial Metrics to be processed
I was not enable to fix it in time, so the financial metrics file is not using dynamic processing, turning impossible to load a new file once the service is already running.

>In order to rerun the project, you need to stop the process, do the *Set up before starting service* and after *Starting service* again.

*sorry for that once more*

## Next steps
Those are my next steps if I had time:
* Include fault tolerance policies while writing files;
* Create end to end job test, with dynamic files being created just for it and deleted after the test runs;
* Separate in two jobs, one responsible only for including new companies and another one for processing and evaluating financial metrics;
* Making both jobs (described above) run with JobLauncher through dynamic file input into the folder;
* Delete files already processed;
* Better organize the code;
* Create unitary tests for readers and writers too;
* Create dynamic variables for constants like coefficients used for ltvCacRadio and paybackPeriod;
* Create message bundle with at least defaultLanguage;
* Include tons of logger for each part of each step;
* Improve README file with some architecture and db images.

## Project Structure
This is the project structure used in the project, and it's insights, explanation divide in packages:
* **batch**: Here we have the main job configuration (BatchConfig) and some step scoped configurations (StepScopedConfiguration) which create beans of processors and writers;
* **domain**: Domain holds the three packages that composes the domain structure of the project, being those entity, enums and item:
    * **entity**: Inside are all entities used for the project, being those are our database entity mirrors;
    * **enums**: Inside is only one Enum, RiskRatingEnum;
    * **item**: Items are the public knows DTOs for Spring Batch processing, so when it comes from a file or it goes to a file it's an item.
* **integration**: Spring Integration are responsible for interacting with the outside world in order to interact with our service, here its used to launch our job when receive a new app companies file.
* **processor**: Processors are responsible for receiving a data, processing it and delivery a processed data, that delivered data can be in a new format. Processors normally are heavily loaded with business rules.
* **reader**: Readers are responsible for reading data, that could be from many places, like a CSV file or a database table for example.
* **repository**: Our repositories running with JPA to interact with the database.
* **util**: Some util classes that exists to help us out.
* **writer**: Writers are responsible for writing data down, that could be writing it inside a new file or writing on a table for example.

*>Aside of internal project structure is a **docs** folder, inside are the step by step diagram (stepsDiagram.drawio) and an ER Diagram (erDiagram_app_revenues.drawio) reflecting the database structure.*

## Database connection configuration
If you want to connect in the database through a Database UI Manager, those are the configurations:
* **Host**: 127.0.0.1
* **Port**: 5432
* **User**: myuser
* **Password**: secret
* **Database**: apprevenuesdb

## Technologies used
Those are the technologies used for built this project:
* [Docker](https://www.docker.com/)
* [Docker Compose](https://docs.docker.com/compose/)
* [Maven](https://maven.apache.org/)
* [Java 17](https://www.oracle.com/java)
* [Spring Initializr](https://start.spring.io/)
* [Spring Boot and Spring Stack](https://spring.io/projects/spring-boot)
* [Spring Batch](https://spring.io/projects/spring-batch/)
* [MySQL](https://www.postgresql.org/)
* [Flyway Migration](https://flywaydb.org/)
* [Lombok](https://projectlombok.org/)
* [Jackson](https://github.com/FasterXML/jackson)
* [JUnit 5](https://junit.org/junit5/)
* [Mockito](https://site.mockito.org/)