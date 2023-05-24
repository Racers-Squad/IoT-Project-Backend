package mqtt.config;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class Art {
    public static void main(String[] args) throws MqttException, InterruptedException {
        String publisherId = UUID.randomUUID().toString();
        IMqttClient subscriber = new MqttClient("tcp://25.63.78.43:1883",publisherId);

        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setCleanSession(true);
        options.setConnectionTimeout(10);
        subscriber.connect(options);

        CountDownLatch receivedSignal = new CountDownLatch(10);
        subscriber.subscribe(Main.TOPIC, (topic, msg) -> {
            byte[] payload = msg.getPayload();

            String  message = new String(payload);
            System.out.println(message);

            receivedSignal.countDown();
        });
        receivedSignal.await(1, TimeUnit.MINUTES);
    }
}
