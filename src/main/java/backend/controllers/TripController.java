package backend.controllers;

import backend.services.ReservationService;
import backend.services.TripServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("http://localhost:3000")
@Slf4j
public class TripController {

    @Autowired
    private TripServiceImpl tripService;

    @GetMapping("/trip")
    public ResponseEntity<?> getAllTrips() {
        return ResponseEntity.ok(tripService.getTrips());
    }

    @GetMapping("/trip/{carId}")
    public ResponseEntity<?> getAllByCar(@PathVariable String carId) {
        return ResponseEntity.ok(tripService.getTripsByCar(carId));
    }

    @GetMapping("/trip/{driverId}")
    public ResponseEntity<?> getAllByDriver(@PathVariable Integer driverId) {
        return ResponseEntity.ok(tripService.getTripsByDriver(driverId));
    }

    @GetMapping("/trip/start/{carId}")
    public ResponseEntity<?> startTrip(@PathVariable String carId){
        log.debug("Started trip for car: " + carId);
        return ResponseEntity.ok(tripService.startTrip(carId));
    }

    @GetMapping("/trip/end/{carId}")
    public ResponseEntity<?> endTrip(@PathVariable String carId){
        log.debug("Ended trip for car: " + carId);
        return ResponseEntity.ok(tripService.endTrip(carId));
    }

}
