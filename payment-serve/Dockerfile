# OpenJDK 21 이미지를 베이스로 사용
FROM openjdk:21-jdk

ARG JAR_FILE=build/libs/payment-serve-0.0.1-SNAPSHOT.jar

COPY ${JAR_FILE} app.jar

# JAR 파일을 실행
CMD ["java", "-jar", "/app.jar"]