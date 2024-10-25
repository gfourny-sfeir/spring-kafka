# schema

> Schema AVRO

## Utilisation avec maven

Lors de la modification du schéma avro, il est possible de générer les classes java au travers d'un profil maven, cela évite de les embarquer dans le jar en mode nominal.
Pour générer les classes java, et ainsi observer le succès ou l'échec de vos modifications, exécuter la commande suivante:

````shell
mvn clean package -Pbuild-java
````

## Utilisation du schéma dans les µservices

Le zip peut être importé dans un projet maven en utilisant le plugin maven-dependency-plugin comme dans l'exemple suivant.<br/>

```xml
<plugins>
  <plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-dependency-plugin</artifactId>
    <executions>
      <execution>
        <id>unpack-schema</id>
        <phase>generate-sources</phase>
        <goals>
          <goal>unpack</goal>
        </goals>
        <configuration>
          <artifactItems>
            <artifactItem>
              <groupId>fr.example.kafka</groupId>
              <artifactId>schema</artifactId>
              <version>${rejeu-schema-version}</version>
              <type>zip</type>
              <outputDirectory>${project.build.directory}/avro</outputDirectory>
              <overWrite>true</overWrite>
            </artifactItem>
          </artifactItems>
        </configuration>
      </execution>
    </executions>
  </plugin>
  <plugin>
    <groupId>org.apache.avro</groupId>
    <artifactId>avro-maven-plugin</artifactId>
    <version>${avro-maven-plugin.version}</version>
    <executions>
      <execution>
        <id>avro-to-java</id>
        <phase>generate-sources</phase>
        <goals>
          <goal>schema</goal>
        </goals>
        <configuration>
          <sourceDirectory>${project.build.directory}/avro</sourceDirectory>
          <outputDirectory>${project.build.directory}/generated-sources/avro</outputDirectory>
          <testSourceDirectory>${project.build.directory}/src/test/resources/avro/</testSourceDirectory>
          <testOutputDirectory>${project.build.directory}/generated-test-sources/avro</testOutputDirectory>
          <stringType>String</stringType>
          <enableDecimalLogicalType>true</enableDecimalLogicalType>
          <imports>
            <import>${project.build.directory}/avro/schema.avsc</import>
          </imports>
        </configuration>
      </execution>
    </executions>
  </plugin>
</plugins>
```