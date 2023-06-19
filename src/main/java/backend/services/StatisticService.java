package backend.services;

import backend.DTO.ReservationInfoResponse;
import backend.DTO.TripInfoResponse;
import backend.entity.ReservationEntity;
import backend.entity.TripEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
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


        Map<String, Object> temp = new HashMap<>();
        temp.put("Средняя продолжительность", avgDuration);
        temp.put("Частая машина", popularCar);

        result.put("stats", temp);

        List<ReservationInfoResponse> reservationInfos = new ArrayList<>();
        for (ReservationEntity entity : reservations) {
            reservationInfos.add(reservationService.buildReservation(entity));
        }

        result.put("details", reservationInfos);

        return result;
    }


    public Map<String, Object> getTripStats(Long userId, String carId, Date start, Date end) {
        Map<String, Object> result = new HashMap<>();
        AtomicReference<Long> tripCount = new AtomicReference<>(0L);
        AtomicReference<Long> tripHours = new AtomicReference<>(0L);
        List<TripEntity> trips = tripService.getTrips();
        Stream<TripEntity> tripStream = trips.stream();
        if (userId != null){
            tripStream = tripStream.filter(tripEntity -> Objects.equals(tripEntity.getDriverId(), userId));
        }
        if (carId != null){
            tripStream = tripStream.filter(tripEntity -> Objects.equals(tripEntity.getCarId(), carId));
        }
        if (start != null){
            tripStream = tripStream.filter(tripEntity -> tripEntity.getStartTime().getTime() > start.getTime());
        }
        if (end != null){
            tripStream = tripStream.filter(tripEntity -> tripEntity.getEndTime().getTime() < end.getTime());
        }
        trips = tripStream.toList();
        trips.forEach(tripEntity -> {
            tripCount.updateAndGet(v -> v + 1);
            tripHours.updateAndGet(v -> v + tripEntity.getEndTime().getTime() - tripEntity.getStartTime().getTime());
        });

        Map<String, Object> stats = new HashMap<>();
        stats.put("Кол-во поездок", tripCount);
        stats.put("Часов в пути", (double) tripHours.get() / 1000 / 60 / 60);
        stats.put("Средняя продолжительность поездки", tripHours.get() / trips.size());
        stats.put("Самая длинная поездка", trips.stream().mapToDouble(tripService::calculateDuration).max().orElse(0));
        stats.put("Самая короткая поездка", trips.stream().mapToDouble(tripService::calculateDuration).min().orElse(0));
        result.put("stats", stats);

        List<TripInfoResponse> details = new ArrayList<>();
        for (TripEntity entity : trips) {
            details.add(tripService.buildTripInfo(entity));
        }

        result.put("details", details);

        return result;
    }
}
