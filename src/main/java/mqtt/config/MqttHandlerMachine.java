package mqtt.config;

import org.eclipse.paho.client.mqttv3.*;

import java.util.Random;
import java.util.UUID;

public class MqttHandlerMachine {
    private final Random rnd = new Random();

    private final String machineNumber;

    public String getMachineNumber() {
        return machineNumber;
    }

    private IMqttClient publisher;

    public MqttHandlerMachine(String machineNumber){
        this.machineNumber = machineNumber;
        try {
            this.publisher = generateMqttConnection();
        } catch (MqttException e) {
            System.err.println("Can't create mqtt connection.");
        }
    }


    public IMqttClient generateMqttConnection() throws MqttException{
        String publisherId = UUID.randomUUID().toString();
        IMqttClient publisher = new MqttClient("tcp://localhost:1883",publisherId);

        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setCleanSession(true);
        options.setConnectionTimeout(10);
        publisher.connect(options);
        return publisher;
    }

    public void sendData() throws MqttException, InterruptedException {
        while (true) {
            if (!publisher.isConnected()) {
                System.err.println("Not connected");
                return;
            }
            MqttMessage msg = readEngineTemp();
            msg.setQos(0);
            msg.setRetained(true);
            publisher.publish(machineNumber, msg);
            Thread.sleep(1000);
        }
    }

    private MqttMessage readEngineTemp() {
        double temp =  80 + rnd.nextDouble() * 20.0;
        byte[] payload = String.format("T:%04.2f",temp)
                .getBytes();
        return new MqttMessage(payload);
    }
}
