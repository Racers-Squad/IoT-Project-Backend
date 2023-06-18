package backend.services;

import backend.entity.TripEntity;
import backend.services.interfaces.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

@Service
public class StatisticService {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private CarServiceImpl carService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private TripService tripService;

    public Map<String, Object> getReservationStats(Long userId, String carId, Date start, Date end) {

    }

    public Map<String, Object> getTripStats(Long userId, String carId, Date start, Date end) {
        Map<String, Object> result = new HashMap<>();
        AtomicReference<Long> tripCount = new AtomicReference<>(0L);
        AtomicReference<Long> tripHours = new AtomicReference<>(0L);
        Stream<TripEntity> tripStream = tripService.getTrips().stream();
        if (userId != null){
            tripStream.filter(tripEntity -> tripEntity.getDriverId() == userId);
        }
        if (carId != null){
            tripStream.filter(tripEntity -> tripEntity.getCarId() == carId);
        }
        if (start != null){
            tripStream.filter(tripEntity -> tripEntity.getStartTime().getTime() > start.getTime());
        }
        if (end != null){
            tripStream.filter(tripEntity -> tripEntity.getEndTime().getTime() < end.getTime());
        }
        tripStream.forEach(tripEntity -> {
            tripCount.updateAndGet(v -> v + 1);
            tripHours.updateAndGet(v -> v + tripEntity.getEndTime().getTime() - tripEntity.getStartTime().getTime());
        });
        result.put("TripCount", tripCount);
        result.put("TripHours", (double) tripHours.get() / 1000 / 60 / 60);
        return result;
    }
}
