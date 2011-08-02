# DTO Maven Plugin

## Description

DTO Maven Plugin generates DTO for Java Bean classes.

## How to build DTO Maven Plugin

* All you need is [Java](http://www.java.com) and [Maven 2](http://maven.apache.org/download.html) installed on your system.
* Then just install the plugin in your local repository.

    mvn clean install.

## How to use it

Add the dto-maven-plugin in the build plugins of your pom.xml and let's go:

    <project>
      ...
      <build>
        <plugins>
          <plugin>
            <groupId>fr.maven.dto</groupId>
            <artifactId>dto-maven-plugin</artifactId>
            <version>1.0</version>
            <configuration>
              <!-- The directory where the DTO classes will be generated, default value is target/generated -->
              <sourceRoot>target/generated</sourceRoot>
              <!-- The list of classes you want to generate DTO classes for.
              <includes>
                <include>fr.example.Bean</include>
                <include>fr.example.AnotherBean</include>
              </includes>
            </configuration>
          </plugin>
        </plugins>
      </build>
      ...
    </project>

