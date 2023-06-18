package backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class StatisticService {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private CarServiceImpl carService;

    @Autowired
    private UserServiceImpl userService;

    public Map<String, Object> getReservationStats(Long userId, String carId, Date start, Date end) {

    }

    public Map<String, Object> getTripStats(Long userId, String carId, Date start, Date end) {

    }
}
