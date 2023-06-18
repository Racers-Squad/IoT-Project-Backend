package backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "reservations")
@Getter
@Setter
public class ReservationEntity {
    @Id
    @SequenceGenerator(name = "reservation_id_sequense", sequenceName = "reservation_id_sequense", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reservation_id_sequense")
    @Column(name = "id")
    private Long id;

    @Column(name = "driverid")
    private Long driverId;

    @Column(name = "carid")
    private String carId;

    @Column(name = "starttime")
    private Date startTime;

    @Column(name = "endtime")
    private Date endTime;

    public ReservationEntity() {
    }
}
