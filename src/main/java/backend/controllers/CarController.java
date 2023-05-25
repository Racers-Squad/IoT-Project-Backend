package backend.controllers;

import backend.DTO.AddCarRequest;
import backend.services.CarServiceImpl;
import backend.services.MongoSaver;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("http://localhost:3000")
@Slf4j
public class CarController {

    @Autowired
    private CarServiceImpl carService;

    @Autowired
    private MongoSaver mongoSaver;

    @GetMapping("/cars")
    public ResponseEntity<?> getCars(){
        try {
            return ResponseEntity.ok(carService.getCars());
        } catch (MqttException exception){
            return ResponseEntity.status(500).body("Mqtt problems on server");
        }
    }

    @PostMapping("/cars/add")
    public ResponseEntity<?> addCar(@RequestBody AddCarRequest request){
        if (carService.addCar(request.getCarNumber(), request.getCarBrand())){
            return ResponseEntity.ok("Car added.");
        } else {
            return ResponseEntity.status(500).body("Mqtt problems with adding car.");
        }
    }


    @GetMapping("/car")
    public ResponseEntity<?> getCarInfo(@RequestParam String carNumber) {
        return ResponseEntity.ok(mongoSaver.readActualParameters(carNumber));
    }

}
