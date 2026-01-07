# Introduction
The Java Grep App is a command-line tool that replicates the core behavior of the Linux `grep`/`egrep` command using Java.
The project was designed as a learning exercise to strengthen fundamental Java concepts such as file I/O, regular expressions, exception handling, logging, stream and lambda functions and Maven project structure.

Two different implementations were developed within the same project: one based on the traditional Java file system API, and another using Java Streams API and lambda expressions. The application is version-controlled with Git and containerized using Docker for easy deployment and portability.

# Quick Start
### Prerequisites
* Java Development Kit (JDK) 8 or higher
* Maven
* Docker (optional, for containerized execution)

### Build the Project
````
mvn clean package
````

### Run the Application
```
java -cp target/grep-1.0-SNAPSHOT.jar ca.jrvs.apps.grep.JavaGrepImp <regex> <inputDirectory> <outputFile>
```

#### Arguments
* **regex**: Regular expression to search for
* **inputDirectory**: Root directory to scan **recursively**
* **outputFile**: Output file where matched lines will be written (overwritten if it exists)

#### Example
```
# To execute the app
java -cp target/grep-1.0-SNAPSHOT.jar ca.jrvs.apps.grep.JavaGrepImp generated /home/student/finalProjectUniversity /home/professor/banned

# To see the result in that case
cat /home/professor/banned
```

# Implementation
## Pseudocode
The overall processing flow is as follows:
1. Recursively traverse the input directory to discover all files.
2. Read each file line by line.
3. Check whether each line matches the provided regular expression.
4. Collect all matching lines.
5. Write the matched lines to the specified output file.

## Technologies
* Java (JDK 8 / Java 17 compatible)
* Maven (compiler target/source 1.8) for build and dependency management
* SLF4J with Log4j2 backend for logging
* GitHub for version control
* Docker for application containerization
* IntelliJ iDEA as IDE

## Key Components
The application contains two distinct implementations located in the same grep package, in two different classes.

#### File System Based Implementation
* Directory Traversal: Implemented using java.io.File with recursive traversal
* File Reading: FileReader and BufferedReader for efficient line-by-line reading

#### Stream-Based Implementation
* Directory Traversal: Uses java.nio.file.Files.walk
* File Processing: Java Streams API
* Improved Readability: Declarative and functional programming style

#### Both Implementation
* Regex Matching: java.util.regex.Pattern and Matcher
* File Writing: FileOutputStream, OutputStreamWriter, and BufferedWriter

## About Performance
One potential performance issue arises when processing very large files or deeply nested directories, as excessive memory usage may occur if data is buffered or collected unnecessarily.
This can be handled by ensuring strict line-by-line streaming, avoiding in-memory aggregation, and improving backpressure handling when writing output.

# Test
Basic functions such as Regex and Stream API functions have been tested manually, but also with JUnit.

App Testing was performed manually by executing the application with:
* Various regular expressions
* Different directory structures
* Input files including empty files, large files, and nested directories

The results were validated by comparing the application output with the expected behavior of the Linux egrep command on the same datasets.

# Deployment
The application is packaged as an executable JAR using **Maven** and deployed inside a **Docker image** to simplify distribution and execution across environments.
* A `Dockerfile` is provided in the GitHub repository
* The Docker image runs the Java Grep application in a lightweight, reproducible environment
* GitHub is used for source control, collaboration, and version tracking

# Improvement
As the project was mostly an educational one to improve Java core concepts, we have a lot of improvements to do, such as:
* Adding automated unit and integration tests using JUnit
* Supporting additional Regex command-line options (case-insensitive search, file name output, line numbers)
* Improving performance for very large datasets