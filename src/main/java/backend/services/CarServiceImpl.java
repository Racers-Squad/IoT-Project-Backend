package backend.services;

import backend.DTO.CarInfoResponse;
import backend.entity.CarEntity;
import backend.repository.CarPostgresRepository;
import backend.services.cars.CarMessage;
import backend.services.cars.CarParamsResolver;
import backend.services.cars.ZhiguliParamsResolver;
import backend.services.interfaces.CarService;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class CarServiceImpl implements CarService {

    @Autowired
    private MqttServiceImpl mqttService;

    @Autowired
    private MongoSaver mongoSaver;

    @Autowired
    private CarPostgresRepository carPostgresRepository;

    private Map<String, IMqttClient> clientsMap = new HashMap<>();

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

    public boolean addCar(String carNumber, String carBrand){
        try {
            IMqttMessageListener listener = new IMqttMessageListener() {
                @Override
                public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                    ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(mqttMessage.getPayload()));
                    CarMessage message = (CarMessage) ois.readObject();
                    if (carPostgresRepository.findByCarNumber(message.getCarId()) == null){
                        carPostgresRepository.save(new CarEntity(message.getCarId(), carBrand));
                    }
                    ZhiguliParamsResolver paramsResolver = new ZhiguliParamsResolver();
                    mongoSaver.save(paramsResolver.convertToGlobal(message.getCarId(), message.getValidFrom(), message.getValues()));
                }
            };
            IMqttClient client = mqttService.createClientWithTopic(carNumber, listener);
            clientsMap.put(carNumber, client);
            return true;
        } catch (MqttException exception){
            return false;
        }
    }
}
