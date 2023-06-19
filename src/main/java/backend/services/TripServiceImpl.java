package backend.services;

import backend.DTO.CarInfoResponse;
import backend.DTO.TripInfoResponse;
import backend.DTO.UserInfoResponse;
import backend.entity.CarEntity;
import backend.entity.TripEntity;
import backend.entity.UserEntity;
import backend.repository.CarPostgresRepository;
import backend.repository.ReservationRepository;
import backend.repository.TripRepository;
import backend.repository.UserRepository;
import backend.services.cars.CarParameter;
import backend.services.interfaces.TripService;
import backend.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TripServiceImpl implements TripService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CarPostgresRepository carPostgresRepository;

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

    public TripInfoResponse buildTripInfo(TripEntity tripEntity) {
        Optional<UserEntity> userOptional = userRepository.findById(tripEntity.getDriverId());
        Optional<CarEntity> carOptional = carPostgresRepository.findById(tripEntity.getCarId());

        CarInfoResponse car = null;
        UserInfoResponse user = null;

        if (userOptional.isPresent()) {
            UserEntity userEntity = userOptional.get();
            user = UserInfoResponse.builder()
                    .id(userEntity.getId())
                    .name(userEntity.getName())
                    .surname(userEntity.getSurname())
                    .phone(userEntity.getPhone())
                    .email(userEntity.getEmail())
                    .build();
        }

        if (carOptional.isPresent()) {
            CarEntity carEntity = carOptional.get();
            car = CarInfoResponse.builder()
                    .id(carEntity.getId())
                    .carBrand(carEntity.getCarBrand())
                    .model(carEntity.getModel())
                    .year(carEntity.getYear())
                    .status(carEntity.getStatus().getLabel())
                    .build();
        }

        return TripInfoResponse.builder()
                .id(tripEntity.getId())
                .user(user)
                .car(car)
                .startTime(CommonUtils.formatDate(tripEntity.getStartTime()))
                .endTime(CommonUtils.formatDate(tripEntity.getEndTime()))
                .startLocation(tripEntity.getStartLocation())
                .endLocation(tripEntity.getEndLocation())
                .duration(calculateDuration(tripEntity))
                .build();
    }

    public Double calculateDuration(TripEntity tripEntity) {
        return (double)(tripEntity.getEndTime().getTime() - tripEntity.getStartTime().getTime()) / 1000 / 3600;
    }
}
