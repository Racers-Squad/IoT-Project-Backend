package backend.services.interfaces;

import backend.entity.TripEntity;

import java.util.List;

public interface TripService {

    List<TripEntity> getTrips();

    List<TripEntity> getTripsByCar(String carId);

    List<TripEntity> getTripsByDriver(Integer driverId);

    TripEntity startTrip(String carId);

    TripEntity endTrip(String carId);
}
