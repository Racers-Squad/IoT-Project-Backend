package backend.services.interfaces;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttException;

public interface MqttService {

    public IMqttClient createClientWithAllTopics(IMqttMessageListener listener) throws MqttException;

    public IMqttClient createClientWithTopic(String topic);
}
