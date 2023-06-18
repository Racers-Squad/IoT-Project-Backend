package backend.services;

import backend.entity.TripEntity;
import backend.repository.ReservationRepository;
import backend.repository.TripRepository;
import backend.services.cars.CarParameter;
import backend.services.interfaces.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TripServiceImpl implements TripService {

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private MongoSaver mongoSaver;

    @Autowired
    private ReservationRepository reservationRepository;

    @Override
    public List<TripEntity> getTrips(){
        return tripRepository.findAll();
    }

    @Override
    public List<TripEntity> getTripsByCar(String carId){
        return tripRepository.findAllByCarId(carId);
    }

    @Override
    public List<TripEntity> getTripsByDriver(Integer driverId) {
        return tripRepository.findAllByDriverId(driverId);
    }

    @Override
    public TripEntity startTrip(String carId) {
        String currentCoordinate = mongoSaver.readActualParameters(carId).getParameters().get(CarParameter.GPS_CORDS).toString();
        TripEntity entity = new TripEntity();
        entity.setDriverId(reservationRepository.findCurrentByCar(carId).getDriverId());
        entity.setCarId(carId);
        entity.setStartLocation(currentCoordinate);
        entity.setStartTime(new Date());
        return tripRepository.save(entity);
    }

    @Override
    public TripEntity endTrip(String carId) {
        String currentCoordinate = mongoSaver.readActualParameters(carId).getParameters().get(CarParameter.GPS_CORDS).toString();
        TripEntity entity = tripRepository.findCurrentTrip(carId);
        entity.setEndTime(new Date());
        entity.setEndLocation(currentCoordinate);
        return tripRepository.save(entity);
    }
}
