package backend.controllers;

import backend.services.CarServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("http://localhost:3000")
@Slf4j
public class CarController {

    @Autowired
    private CarServiceImpl carService;

    @GetMapping("/cars")
    public ResponseEntity<?> getCars(){
        try {
            return ResponseEntity.ok(carService.getCars());
        } catch (MqttException exception){
            return ResponseEntity.status(500).body("Mqtt problems on server");
        }
    }

}
