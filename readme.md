# DTO Maven Plugin

## Description

DTO Maven Plugin generates DTO for Java Bean classes.
Licensed under Apache License Version 2.0.

## How to build DTO Maven Plugin

* All you need is [Java](http://www.java.com) and [Maven 2](http://maven.apache.org/download.html) installed on your system.
* Then just install the plugin in your local repository :

    mvn clean install

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
                <include>fr.package.Bean</include>
                <include>fr.package.**.AnotherBean</include>
              </includes>
            </configuration>
          </plugin>
        </plugins>
      </build>
      ...
    </project>
	
## License

   Copyright 2011 Wilfried Petit

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

