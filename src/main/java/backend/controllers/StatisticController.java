package backend.controllers;

import backend.DTO.StatisticRequest;
import backend.DTO.StatisticType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("/statistic")
public class StatisticController {

    @GetMapping
    public Object getStatistic(@RequestParam Long userId,
                               @RequestParam String carId,
                               @RequestParam String startTime,
                               @RequestParam String endTime,
                               @RequestParam StatisticType type) {
        return null;
    }

}
