package backend.services;

import backend.DTO.CarInfoResponse;
import backend.DTO.CarStatus;
import backend.DTO.ReservationInfoResponse;
import backend.DTO.TotalCarInfoResponse;
import backend.entity.CarEntity;
import backend.entity.CarParameters;
import backend.repository.CarPostgresRepository;
import backend.services.cars.CarMessage;
import backend.services.cars.ZhiguliParamsResolver;
import backend.services.interfaces.CarService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Autowired
    private ReservationService reservationService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final Map<String, IMqttClient> clientsMap = new HashMap<>();

    public List<CarInfoResponse> getCars() throws MqttException {
        List<CarEntity> entities = carPostgresRepository.findAll();
        return entities.stream()
                .map(this::buildCarInfoResponse)
                .toList();
    }

    public CarInfoResponse buildCarInfoResponse(CarEntity entity) {
        CarInfoResponse carInfoResponse = new CarInfoResponse();
        carInfoResponse.setId(entity.getId());
        carInfoResponse.setCarBrand(entity.getCarBrand());
        carInfoResponse.setModel(entity.getModel());
        carInfoResponse.setStatus(entity.getStatus().getLabel());
        carInfoResponse.setYear(entity.getYear());

        ReservationInfoResponse reservation = reservationService.findCurrentByCar(entity.getId());
        if (reservation != null) {
            carInfoResponse.setReservation(reservation.getId());
        } else {
            carInfoResponse.setReservation(null);
        }

        return carInfoResponse;
    }

    public boolean addCar(String carNumber, String carBrand){
        try {
            IMqttMessageListener listener = new IMqttMessageListener() {
                @Override
                public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                    String json = new String(mqttMessage.getPayload());
                    CarMessage message = objectMapper.readValue(json, CarMessage.class);
                    if (carPostgresRepository.findByCarNumber(message.getCarId()) == null){
                        CarEntity entity = new CarEntity();
                        entity.setModel("Kalina");
                        entity.setCarBrand(carBrand);
                        entity.setYear(2007);
                        entity.setStatus(CarStatus.FREE);
                        carPostgresRepository.save(entity);
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

    public void deleteCar(String carId) throws MqttException {
        carPostgresRepository.deleteById(carId);
        var client = clientsMap.get(carId);
        client.disconnect();
        client.close();
        clientsMap.remove(carId);
    }

    public CarEntity getCar(String id) {
        return carPostgresRepository.getById(id);
    }

    public TotalCarInfoResponse getFullCarInfo(String carId) {
        CarParameters carParameters = mongoSaver.readActualParameters(carId);
        CarEntity car = carPostgresRepository.findByCarNumber(carId);

        TotalCarInfoResponse response = new TotalCarInfoResponse();
        response.setCarInfoResponse(buildCarInfoResponse(car));
        response.setCarParameters(carParameters);

        return response;
    }
}
