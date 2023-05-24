package mqtt.config;

import org.eclipse.paho.client.mqttv3.*;

import java.util.Random;
import java.util.UUID;

public class Main {

    public static final String TOPIC = "engine/temperature";
    private static Random rnd = new Random();

    public static void main(String[] args) throws MqttException, InterruptedException {
        String publisherId = UUID.randomUUID().toString();
        final IMqttClient publisher = new MqttClient("tcp://25.63.78.43:1883",publisherId);

        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setCleanSession(true);
        options.setConnectionTimeout(10);
        publisher.connect(options);



        while (true) {
            if (!publisher.isConnected()) {
                System.err.println("Not connected");
                return;
            }
            MqttMessage msg = readEngineTemp();
            msg.setQos(0);
            msg.setRetained(true);
            publisher.publish(TOPIC, msg);

            System.out.println("Send");

            Thread.sleep(1000);
        }
    }

    private static MqttMessage readEngineTemp() {
        double temp =  80 + rnd.nextDouble() * 20.0;
        byte[] payload = String.format("T:%04.2f",temp)
                .getBytes();
        return new MqttMessage(payload);
    }
}
