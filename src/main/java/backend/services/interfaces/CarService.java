package backend.services.interfaces;

import backend.DTO.CarInfoResponse;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.List;

public interface CarService {

    public List<CarInfoResponse> getCars() throws InterruptedException, MqttException;
}
