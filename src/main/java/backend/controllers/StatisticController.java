package backend.controllers;

import backend.DTO.StatisticRequest;
import backend.DTO.StatisticType;
import backend.entity.UserEntity;
import backend.services.StatisticService;
import backend.services.UserServiceImpl;
import backend.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.text.ParseException;

@RestController("/statistic")
public class StatisticController {

    @Autowired
    private StatisticService statisticService;

    @Autowired
    private UserServiceImpl userService;

    @GetMapping
    public Object getStatistic(@RequestParam(required = false) String email,
                               @RequestParam(required = false) String carId,
                               @RequestParam(required = false) String startTime,
                               @RequestParam(required = false) String endTime,
                               @RequestParam StatisticType type) throws ParseException {
        Long userId = null;
        if (email != null) {
            UserEntity user = userService.findByEmail(email);
            userId = user.getId();
        }
        if (type == StatisticType.RESERVATIONS){
            return statisticService.getReservationStats(userId, carId, CommonUtils.parseDate(startTime), CommonUtils.parseDate(endTime));
        } else {
            return statisticService.getTripStats(userId, carId, CommonUtils.parseDate(startTime), CommonUtils.parseDate(endTime));
        }
    }

}
