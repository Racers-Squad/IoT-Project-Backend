package machineSimulator;

import backend.services.cars.CarMessage;
import org.eclipse.paho.client.mqttv3.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

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
        IMqttClient publisher = new MqttClient("tcp://" + System.getenv("BROKER_IP")+":1883",publisherId);

        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setCleanSession(true);
        options.setConnectionTimeout(10);
        publisher.connect(options);
        return publisher;
    }

    public void sendData() throws MqttException, InterruptedException, IOException {
        while (true) {
            if (!publisher.isConnected()) {
                System.err.println("Not connected");
                return;
            }
            MqttMessage msg = generateMessage();
            msg.setQos(0);
            msg.setRetained(true);
            System.out.println(msg);
            publisher.publish(machineNumber, msg);
            System.out.println("Message sended");
            Thread.sleep(1000);
        }
    }

    private MqttMessage readEngineTemp() {
        double temp =  80 + rnd.nextDouble() * 20.0;
        byte[] payload = String.format("T:%04.2f",temp)
                .getBytes();
        return new MqttMessage(payload);
    }

    private MqttMessage generateMessage() throws IOException {
        Map<Long, Object> values = generateParams();
        CarMessage carMessage = new CarMessage();
        carMessage.setValidFrom(new Date());
        carMessage.setCarId(machineNumber);
        carMessage.setValues(values);
        byte[] data = serialize(carMessage);
        return new MqttMessage(data);
    }

    private byte[] serialize(CarMessage params) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(params);
        return baos.toByteArray();
    }

    private Map<Long, Object> generateParams() {
        Map<Long, Object> values = new HashMap<>();
        values.put(1L, 0);
        values.put(2L, 1);
        values.put(3L, ThreadLocalRandom.current().nextInt(10, 70));
        values.put(4L, ThreadLocalRandom.current().nextDouble(23.2, 24.8));
        values.put(5L, ThreadLocalRandom.current().nextInt(470, 520));
        return values;
    }
}
