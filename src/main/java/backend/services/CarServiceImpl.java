package backend.services;

import backend.DTO.CarInfoResponse;
import backend.services.interfaces.CarService;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class CarServiceImpl implements CarService {

    @Autowired
    private MqttServiceImpl mqttService;
    public List<CarInfoResponse> getCars() throws MqttException {
        List<String> topics = new ArrayList<>();
        List<String> brands = new ArrayList<>();
        List<CarInfoResponse> result = new ArrayList<>();
        CountDownLatch receivedSignal = new CountDownLatch(10);
        IMqttMessageListener listener = new IMqttMessageListener() {
            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                String message = new String(mqttMessage.getPayload());
                log.info("Received message: " + message);
                topics.add(topic);
                //TODO Сделать нормальную обработку марки машины
                brands.add(message);
                receivedSignal.await(1, TimeUnit.MINUTES);
            }
        };

        IMqttClient client = mqttService.createClientWithAllTopics(listener);
        for (int i = 0; i < topics.size(); i++){
            CarInfoResponse carInfoResponse = new CarInfoResponse(i, topics.get(i), brands.get(i));
            result.add(carInfoResponse);
        }
        return result;
    }
}
