package backend.controllers;

import backend.DTO.StatisticRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/statistic")
public class StatisticController {

    @GetMapping
    public Object getStatistic(@RequestBody StatisticRequest request) {
        return null;
    }

}
