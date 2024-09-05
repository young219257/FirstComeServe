# OpenJDK 21 이미지를 베이스로 사용
FROM openjdk:21-jdk


# JAR 파일의 위치를 정의
ARG JAR_FILE=build/libs/apigateway-serve-0.0.1-SNAPSHOT.jar

#jar 파일을 컨테이너에 복사
COPY ${JAR_FILE} app.jar

# JAR 파일을 실행
CMD ["java", "-jar", "/app.jar"]