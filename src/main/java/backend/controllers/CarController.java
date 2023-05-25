package backend.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("http://localhost:3000")
@Slf4j
public class CarController {

    @GetMapping("/cars")
    public ResponseEntity<?> getCars(){
        int carsAmount = 5;
        log.info("Cars amount: " + carsAmount);
        return ResponseEntity.ok(carsAmount);
    }

}
