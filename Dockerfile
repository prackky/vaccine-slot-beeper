FROM openjdk:11-slim
VOLUME /tmp
RUN mkdir /application
COPY . /application
WORKDIR /application
RUN /application/mvnw install -DskipTests
RUN mv /application/target/vaccinebookingvocal.jar /application
EXPOSE 8080
CMD ["java", "-jar", "/application/vaccinebookingvocal.jar"]