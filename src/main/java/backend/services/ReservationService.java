package backend.services;

import backend.DTO.CarInfoResponse;
import backend.DTO.CarStatus;
import backend.DTO.ReservationInfoResponse;
import backend.DTO.UserInfoResponse;
import backend.entity.CarEntity;
import backend.entity.ReservationEntity;
import backend.entity.UserEntity;
import backend.repository.CarPostgresRepository;
import backend.repository.ReservationRepository;
import backend.repository.UserRepository;
import backend.util.CommonUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CarPostgresRepository carPostgresRepository;

    public List<ReservationEntity> findByCarId(String carId) {
        return reservationRepository.findByCarId(carId);
    }

    public List<ReservationEntity> findByDriverId(Long driverId) {
        return reservationRepository.findByDriverId(driverId);
    }

    public Optional<ReservationEntity> findByDriverAndTimeBetween(Long driverId, Date start, Date end) {
        return reservationRepository.findByDriverAndTimeBetween(driverId, start, end);
    }

    @Transactional
    public ReservationEntity create(Long userId, String carId) {
        ReservationEntity reservationEntity = new ReservationEntity();
        reservationEntity.setCarId(carId);
        reservationEntity.setDriverId(userId);
        reservationEntity.setStartTime(new Date());
        reservationEntity.setEndTime(null);
        reservationRepository.save(reservationEntity);

        CarEntity car = carPostgresRepository.findByCarNumber(carId);
        car.setStatus(CarStatus.RESERVED);

        return reservationEntity;
    }

    @Transactional
    public void finish(Long reservationId) {
        Optional<ReservationEntity> entity = reservationRepository.findById(reservationId);
        entity.ifPresent(reservationEntity -> reservationEntity.setEndTime(new Date()));

        entity.ifPresent(reservationEntity -> {
            reservationEntity.setEndTime(new Date());
            CarEntity car = carPostgresRepository.findByCarNumber(reservationEntity.getCarId());
            car.setStatus(CarStatus.RESERVED);
        });
    }

    public List<ReservationInfoResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(this::buildReservation)
                .toList();
    }

    public ReservationInfoResponse findCurrentByDriver(Long driverId) {
        ReservationEntity entity = reservationRepository.findCurrentByDriver(driverId);
        if (entity != null) {
            return buildReservation(entity);
        }
        return null;
    }

    public ReservationInfoResponse findCurrentByCar(String carId) {
        ReservationEntity entity = reservationRepository.findCurrentByCar(carId);
        if (entity != null) {
            return buildReservation(entity);
        }
        return null;
    }

    private ReservationInfoResponse buildReservation(ReservationEntity reservationEntity) {
        Optional<UserEntity> userOptional = userRepository.findById(reservationEntity.getDriverId());
        Optional<CarEntity> carOptional = carPostgresRepository.findById(reservationEntity.getCarId());

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

        return ReservationInfoResponse.builder()
                .id(reservationEntity.getId())
                .user(user)
                .car(car)
                .startTime(CommonUtils.formatDate(reservationEntity.getStartTime()))
                .endTime(CommonUtils.formatDate(reservationEntity.getEndTime()))
                .build();
    }


}
