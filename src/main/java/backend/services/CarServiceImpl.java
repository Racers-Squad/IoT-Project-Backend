package backend.services;

import backend.DTO.CarInfoResponse;
import backend.entity.CarEntity;
import backend.repository.CarPostgresRepository;
import backend.services.cars.CarMessage;
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
        List<CarInfoResponse> result = new ArrayList<>();
        List<CarEntity> entities = carPostgresRepository.findAll();
        for (int i = 0; i < entities.size(); i++){
            result.add(new CarInfoResponse(i, entities.get(i).getCarNumber(), entities.get(i).getCarBrand()));
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
