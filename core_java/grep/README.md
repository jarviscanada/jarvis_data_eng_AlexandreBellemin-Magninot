# Introduction

The Java Grep App is a command-line tool that brings the essence of Bash's `egrep` command to Java, delivering familiar functionality in a new environment.
Its development focused on deepening practical skills in core Java: file I/O, regular expressions, exception handling, logging, and Maven project structure.

The application recursively scans a given directory, reads all files, searches for lines within the files that match a user-provided regular expression, and writes the matched lines to an output file specified by the user.

# Quick Start

### Prerequisites
* Java Development Kit (JDK) 8 or higher
* Maven
* Git

### Build the Project
````
mvn clean package
````

### Run the Application
```
java -cp target/grep-1.0-SNAPSHOT.jar ca.jrvs.apps.grep.JavaGrepImp <regex> <inputDirectory> <outputFile>
```

#### Arguments
* regex: Regular expression to search for
* inputDirectory: Directory to scan
* outputFile: File where matched lines will be written (overwrite)

#### Example
```
java -cp target/grep-1.0-SNAPSHOT.jar ca.jrvs.apps.grep.JavaGrepImp generated /home/student/finalProjectUniversity /home/professor/banned
```

# Implementation

The application is implemented as a Maven project using Java.

### Core Logic
The processing flow is:
1. Traverse the input directory recursively to list all the files.
2. Read each file line by line.
3. Check if a line contains the given regex pattern.
4. Collect matched lines
5. Write matched lines to the output file.

### Key Components
* Directory Traversal: Implemented using `java.io.File` with recursion
* File Reading: Uses `FileReader` and `BufferedReader` for efficient line-by-line reading
* Regex Matching: Implemented using `java.util.regex.Pattern` and Matcher
* File Writing: Uses `FileOutputStream`, `OutputStreamWriter`, and `BufferedWriter`
* Logging: Implemented using SLF4J with a Log4j2 backend
* Build Tool: Maven for dependency management
* Version Control: GitHub

### Technologies
* Java (JDK 8 / Java 17 compatible)
* Maven (compiler target/source 1.8)
* SLF4J for logging with a Log4j2 backend.

# Tests
Testing was performed manually by running the application with various:
* Regular expressions
* Directory structures
* Input files (empty files, filled files, nested directories)

Results were validated by comparing the output with the expected behavior of the Bash `egrep` command.

# Deployment
* The application is packaged as a runnable JAR using **Maven**.
* **GitHub** for collaboration and feature addition;

# Improvements
As the project was mostly an educational one to improve Java core concepts, we have a lot of improvements to do, such as:
* Add automated unit tests using JUnit.
* Containerize the application using Docker.
* Support additional regex features.

