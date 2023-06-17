package backend.entity;

import backend.DTO.CarStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "car", schema = "public")
public class CarEntity {

    @Id
    @Column(name = "id", columnDefinition = "varchar(15) PRIMARY KEY NOT NULL")
    private String id;

    @Column(name="carBrand", columnDefinition = "varchar(50) NOT NULL")
    private String carBrand;

    @Column(name = "model")
    private String model;

    @Column(name = "year")
    private Integer year;

    @Column(name = "status")
    private CarStatus status;

}
