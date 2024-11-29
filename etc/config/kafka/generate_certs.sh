#!/bin/bash

# Define target directory for all generated files
TARGET_DIR="."

# Generate a CA (Certificate Authority) keypair with extended CA capabilities
keytool -genkeypair -alias ca -keyalg RSA -keysize 2048 -dname "CN=Kafka-Security-CA,OU=None,O=None,L=None,S=None,C=None" -ext bc:c=ca:true -ext KeyUsage:critical=keyCertSign,digitalSignature -ext ExtendedKeyUsage=serverAuth,clientAuth -keypass trusted-ca-pass -keystore ${TARGET_DIR}/ca.jks -storepass trusted-ca-pass -storetype JKS -validity 365

# Export the CA certificate to a file
keytool -exportcert -alias ca -keystore ${TARGET_DIR}/ca.jks -storepass trusted-ca-pass -rfc -file ${TARGET_DIR}/ca.crt

# Generate a server keypair for Kafka broker
keytool -genkeypair -alias server -keyalg RSA -keysize 2048 -dname "CN=bs-kafka-secure,OU=None,O=None,L=None,S=None,C=None" -ext "SAN=DNS:bs-kafka-secure,DNS:localhost" -keypass kafka-server-pass -keystore ${TARGET_DIR}/kafka.server.keystore.jks -storepass kafka-server-pass -storetype JKS -validity 365

# Generate a certificate signing request (CSR) for the server
keytool -certreq -alias server -keystore ${TARGET_DIR}/kafka.server.keystore.jks -storepass kafka-server-pass -file ${TARGET_DIR}/server.csr

# Sign the server's CSR with the CA key
keytool -gencert -alias ca -keystore ${TARGET_DIR}/ca.jks -storepass trusted-ca-pass -infile ${TARGET_DIR}/server.csr -outfile ${TARGET_DIR}/server.crt -rfc

# Import the CA certificate into server's keystore
keytool -importcert -alias ca -file ${TARGET_DIR}/ca.crt -keystore ${TARGET_DIR}/kafka.server.keystore.jks -storepass kafka-server-pass -noprompt

# Import the signed server certificate into server's keystore
keytool -importcert -alias server -file ${TARGET_DIR}/server.crt -keystore ${TARGET_DIR}/kafka.server.keystore.jks -storepass kafka-server-pass -noprompt

# Import the CA certificate into server's truststore
keytool -importcert -alias ca -file ${TARGET_DIR}/ca.crt -keystore ${TARGET_DIR}/kafka.server.truststore.jks -storepass kafka-server-pass -noprompt

# Import the CA certificate into client's truststore
keytool -importcert -alias ca -file ${TARGET_DIR}/ca.crt -keystore ${TARGET_DIR}/client.truststore.jks -storepass client-pass -noprompt
