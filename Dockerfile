
# ============================================
# Stage 1: BUILD
# ============================================
FROM maven:3.9.9-eclipse-temurin-17 AS build

WORKDIR /build

COPY pom.xml .
COPY domain/pom.xml domain/
COPY rest/pom.xml rest/
COPY client/pom.xml client/
COPY rest-integration-tests/pom.xml rest-integration-tests/

COPY domain/src domain/src
COPY rest/src rest/src
COPY client/src client/src
COPY rest-integration-tests/src rest-integration-tests/src

RUN mvn -B -ntp -DskipTests -pl rest -am package

# ============================================
# Stage 2: RUNTIME
# ============================================
FROM tomcat:10.1-jre17-temurin AS runtime

RUN groupadd --system appgroup && \
    useradd --system --gid appgroup \
    --no-create-home --shell /usr/sbin/nologin appuser && \
    rm -rf /usr/local/tomcat/webapps/* && \
    mkdir -p \
        /usr/local/tomcat/webapps \
        /usr/local/tomcat/conf/Catalina/localhost \
        /usr/local/tomcat/logs \
        /usr/local/tomcat/temp \
        /usr/local/tomcat/work && \
    chown -R appuser:appgroup \
        /usr/local/tomcat/webapps \
        /usr/local/tomcat/conf/Catalina \
        /usr/local/tomcat/logs \
        /usr/local/tomcat/temp \
        /usr/local/tomcat/work

COPY --from=build \
    /build/rest/target/rest-1.0-SNAPSHOT.war \
    /usr/local/tomcat/webapps/ROOT.war

RUN chown appuser:appgroup /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080

USER appuser

CMD ["catalina.sh", "run"]
