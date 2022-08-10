FROM registry.access.redhat.com/ubi8/openjdk-17:1.11


COPY --chown=185 quarkus-sample/quarkus-sample-rest-service/pom.xml /deployments/lib/
COPY --chown=185 /etc/docker-compose/.mvn/ /deployments/lib/
COPY --chown=185 /etc/docker-compose/mvnw /deployments/lib/

USER 185

WORKDIR /deployments/lib/

CMD ["./mvnw", "quarkus:dev"]

#ENV JAVA_OPTS="-Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager"
#ENV JAVA_APP_JAR="/deployments/quarkus-run.jar"

