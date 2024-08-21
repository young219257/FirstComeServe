# OpenJDK 21 이미지를 베이스로 사용
FROM openjdk:21-jdk

# 작업 디렉토리 설정
WORKDIR /app

# Gradle 빌드 파일과 소스 코드를 복사
COPY build.gradle settings.gradle /app/
COPY src /app/src

# 의존성 설치 및 애플리케이션 빌드
RUN ./gradlew build --no-daemon

# JAR 파일을 실행
CMD ["java", "-jar", "build/libs/your-app.jar"]