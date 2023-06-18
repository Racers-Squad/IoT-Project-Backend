package backend.controllers;

import backend.DTO.ReservationInfoResponse;
import backend.entity.ReservationEntity;
import backend.entity.UserEntity;
import backend.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController("/reservation")
public class CarRentalController {

    @Autowired
    private ReservationService reservationService;


    @GetMapping
    public List<ReservationInfoResponse> getAllReservations() {
        return reservationService.findAll();
    }

    @PostMapping("/reservation/start")
    public ReservationEntity reserveCar(@RequestBody String carId) {
        UserEntity userEntity = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return reservationService.create(userEntity.getId(), carId);
    }

    @PostMapping("/reservation/stop")
    public void finishReservation(@RequestBody Long reservationId) {
        reservationService.finish(reservationId);
    }

}
