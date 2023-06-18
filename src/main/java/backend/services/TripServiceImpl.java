package backend.services;

import backend.entity.TripEntity;
import backend.repository.TripRepository;
import backend.services.interfaces.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TripServiceImpl implements TripService {

    @Autowired
    private TripRepository tripRepository;

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
    public void startTrip() {

    }

    @Override
    public void endTrip() {

    }
}
