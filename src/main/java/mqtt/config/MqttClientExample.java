package mqtt.config;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttClientExample {
    public static void main(String[] args) {
        String broker = "tcp://25.63.78.43:1883";
        String clientId = "JavaMqttClient";
        MemoryPersistence persistence = new MemoryPersistence();

        try {
            MqttClient mqttClient = new MqttClient(broker, clientId, persistence);

            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);

            System.out.println("Connecting to broker: " + broker);
            mqttClient.connect(connOpts);
            System.out.println("Connected");

            String topic = "test/topic";
            int qos = 1;

            mqttClient.subscribe(topic, qos);
            System.out.println("Subscribed to topic: " + topic);

            mqttClient.setCallback(new MqttCallback() {
                public void connectionLost(Throwable cause) {
                    System.out.println("Connection lost!");
                }

                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    System.out.println("Received message: " + new String(message.getPayload()));
                }

                public void deliveryComplete(IMqttDeliveryToken token) {
                }
            });

            // Publish a test message
            String messageContent = "Hello, MQTT!";
            MqttMessage mqttMessage = new MqttMessage(messageContent.getBytes());
            mqttMessage.setQos(qos);
            mqttClient.publish(topic, mqttMessage);
            System.out.println("Published message: " + messageContent);

            // Wait for a few seconds to receive messages
            Thread.sleep(5000);

            mqttClient.disconnect();
            System.out.println("Disconnected");
        } catch (MqttException me) {
            System.out.println("Error: " + me.getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
