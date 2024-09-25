FROM openjdk:17
ARG JAR_FILE=build/libs/*.jar # 경로 설정
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
