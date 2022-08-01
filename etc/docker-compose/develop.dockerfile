ARG WILDFLY_BASE_IMAGE

FROM $WILDFLY_BASE_IMAGE

RUN /opt/jboss/wildfly/bin/add-user.sh admin admin --silent

CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0", "-P", "/sampler/wildfly.properties", "--debug", "*:8787", "--server-config=standalone-microprofile.xml"]
