FROM openjdk:8
ARG JAR_FILE

ENV RRROR_PATH=/opt/rrror
ENV APP_JAR_FILE=app.jar

RUN mkdir -p ${RRROR_PATH}
COPY ${JAR_FILE} ${RRROR_PATH}/${APP_JAR_FILE}
WORKDIR ${RRROR_PATH}
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]