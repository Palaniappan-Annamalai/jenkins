FROM openjdk:11
WORKDIR /usr/app-1
COPY target/jenkins-*.jar jenkins-product.jar
CMD ["java", "-jar","jenkins-product.jar"]

