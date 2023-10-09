Generál egy kulcstárat, amely tartalmazza a Kafka szerver privát kulcsát és nyilvános tanúsítványát. TODO: "Subject Alternative Name" (SAN) pontosítása.

    keytool -keystore kafka.server.keystore.jks -alias kafka-server -validity 365 -genkey -keyalg RSA -storepass kafka-server-pass -keypass kafka-server-pass -dname "CN=localhost, O=organization, L=city, C=country" -ext SAN=DNS:localhost,DNS:bs-kafka,IP:192.168.0.181

Exportálja a Kafka szerver tanúsítványát, hogy meg lehessen osztani:

    keytool -keystore kafka.server.keystore.jks -alias kafka-server -export -file kafka-server-cert.cer -storepass kafka-server-pass

Szerver oldali TLS (azaz a kliensek megbíznak a szerverben, de a szerver nem ellenőrzi a klienseket),
ezért a Kafka szerver kulcstára a saját tanúsítványát fogja tartalmazni.
Importálja a Kafka szerver tanúsítványát a saját kulcstárába:

    keytool -keystore kafka.server.truststore.jks -alias kafka-server -import -file kafka-server-cert.cer -storepass kafka-server-pass -noprompt

Ahhoz, hogy a kliens megbízzon a Kafka szerverben, szüksége van a Kafka szerver tanúsítványára a saját kulcstárában:

    keytool -keystore client.truststore.jks -alias kafka-server -import -file kafka-server-cert.cer -storepass client-pass -noprompt
