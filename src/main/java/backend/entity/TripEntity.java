package backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name="trip", schema = "public")
public class TripEntity {

    @Id
    @Column(name="id", columnDefinition = "INT PRIMARY KEY")
    private Integer id;

    @Column(name="driverID", columnDefinition = "INT REFERENCES Driver(ID)")
    private Integer driverId;

    @Column(name="carID", columnDefinition = "varchar(15) REFERENCES Car(ID)")
    private String carId;

    @Column(name="startLocation", columnDefinition = "VARCHAR(100)")
    private String startLocation;

    @Column(name="endLocation", columnDefinition = "VARCHAR(100)")
    private String endLocation;

    @Column(name="startTime", columnDefinition = "TIMESTAMP")
    private Date startTime;

    @Column(name="endTime", columnDefinition = "TIMESTAMP")
    private Date endTime;
}
