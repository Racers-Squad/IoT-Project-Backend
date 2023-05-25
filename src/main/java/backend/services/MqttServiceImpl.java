package backend.services;

import backend.services.interfaces.MqttService;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MqttServiceImpl implements MqttService {
    @Override
    public IMqttClient createClientWithAllTopics(IMqttMessageListener listener) throws MqttException{
        String publisherId = UUID.randomUUID().toString();
        IMqttClient subscriber = new MqttClient("tcp://localhost:1883",publisherId);
        MqttConnectOptions options = generateConnectionOptions();
        subscriber.connect(options);
        subscriber.subscribe("#", listener);
        return subscriber;
    }

    @Override
    public IMqttClient createClientWithTopic(String topic, IMqttMessageListener listener) throws MqttException {
        String publisherId = UUID.randomUUID().toString();
        IMqttClient subscriber = new MqttClient("tcp://localhost:1883",publisherId);
        MqttConnectOptions options = generateConnectionOptions();
        subscriber.connect(options);
        subscriber.subscribe(topic, listener);
        return subscriber;
    }

    private MqttConnectOptions generateConnectionOptions(){
        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setCleanSession(true);
        options.setConnectionTimeout(10);
        return options;
    }


}
