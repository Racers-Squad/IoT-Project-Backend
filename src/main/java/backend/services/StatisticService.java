package backend.services;

import backend.entity.TripEntity;
import backend.services.interfaces.TripService;
import backend.DTO.CarInfoResponse;
import backend.DTO.ReservationInfoResponse;
import backend.entity.ReservationEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
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
    private TripServiceImpl tripService;

    public Map<String, Object> getReservationStats(Long userId, String carId, Date start, Date end) {
        List<ReservationEntity> reservations = reservationService.findAllEntites();
        Stream<ReservationEntity> stream = reservations.stream();
        stream = stream.filter(it -> it.getEndTime() != null);
        if (start != null) {
            stream = stream.filter(it -> it.getStartTime().after(start));
        }
        if (end != null) {
            stream = stream.filter(it -> it.getEndTime().before(end));
        }
        if (userId != null) {
            stream = stream.filter(it -> it.getDriverId().equals(userId));
        }
        if (carId != null) {
            stream = stream.filter(it -> it.getCarId().equals(carId));
        }
        reservations = stream.toList();

        Map<String, Object> result = new HashMap<>();

        long avgDuration = reservations.stream()
                .mapToLong(it -> (it.getStartTime().getTime() + it.getEndTime().getTime()))
                .sum();
        avgDuration = avgDuration / reservations.size();
        avgDuration /= (double)(1000 * 3600 * 24);

        Optional<Map.Entry<String, Long>> max = reservations.stream()
                .collect(Collectors.groupingBy(it -> String.valueOf(it.getCarId()),
                        HashMap::new, Collectors.counting()))
                .entrySet().stream()
                .max((a, b) -> (int) (a.getValue() - b.getValue()));

        String popularCar = "None";
        if (max.isPresent()) {
            popularCar = carService.getCar(max.get().getKey()).getCarBrand();
        }

        result.put("Средняя продолжительность", avgDuration);
        result.put("Частая машина", popularCar);

        List<ReservationInfoResponse> reservationInfos = new ArrayList<>();
        for (ReservationEntity entity : reservations) {
            reservationInfos.add(reservationService.buildReservation(entity));
        }

        result.put("Детали", reservationInfos);

        return result;
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
