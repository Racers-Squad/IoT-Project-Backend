package backend.controllers;

import backend.DTO.StatisticRequest;
import backend.DTO.StatisticType;
import backend.services.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;

@RestController("/statistic")
public class StatisticController {

    @Autowired
    private StatisticService statisticService;

    @GetMapping
    public Object getStatistic(@RequestParam Long userId,
                               @RequestParam String carId,
                               @RequestParam String startTime,
                               @RequestParam String endTime,
                               @RequestParam StatisticType type) {
        if (type == StatisticType.RESERVATIONS){
            return statisticService.getReservationStats(userId, carId, Date.valueOf(startTime), Date.valueOf(endTime));
        } else {
            return statisticService.getTripStats(userId, carId, Date.valueOf(startTime), Date.valueOf(endTime));
        }
    }

}
