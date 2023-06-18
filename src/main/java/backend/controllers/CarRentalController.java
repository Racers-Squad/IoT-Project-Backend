package backend.controllers;

import backend.DTO.ReservationInfoResponse;
import backend.entity.ReservationEntity;
import backend.entity.UserEntity;
import backend.services.ReservationService;
import backend.services.UserServiceImpl;
import backend.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController("/reservation")
public class CarRentalController {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private UserServiceImpl userService;

    @GetMapping
    public List<ReservationInfoResponse> getAllReservations() {
        return reservationService.findAll();
    }

    @PostMapping("/reservation/start")
    public ReservationEntity reserveCar(@RequestBody Map<String, Object> json) {
        String email = (String) json.get("email");
        String carId = (String) json.get("carId");

        UserEntity user = userService.findByEmail(email);
        if (user == null) {
            return null;
        }
        return reservationService.create(user.getId(), carId);
    }

    @PostMapping("/reservation/stop")
    public ResponseEntity<?> finishReservation(@RequestBody Long reservationId) {
        reservationService.finish(reservationId);
        return ResponseEntity.ok("Reservation is finished");
    }

}
